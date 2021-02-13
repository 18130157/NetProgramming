package de19_2020_2021;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerProcess implements Runnable {
	private static int number = 1;

	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	
	private RandomAccessFile raf;
	private boolean hasFoto;
	private long pos = -1;

	public ServerProcess(Socket s) {
		try {
			this.socket = s;
			netIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			netOut.writeUTF("Welcome to Registration...");
			netOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String command, response, action;
			StringTokenizer tokenizer;

			while (true) {
				command = netIn.readUTF();

				if (command.equalsIgnoreCase(Server.QUIT)) {
					netOut.writeUTF("Bye");
					netOut.flush();
					break;
				}

				tokenizer = new StringTokenizer(command, Server.delim);
				int count = tokenizer.countTokens();
				action = tokenizer.nextToken().trim();

				if (action.equalsIgnoreCase(Server.VIEW_INFO))
					response = handleViewInfo(count, tokenizer);

				else if (action.equalsIgnoreCase(Server.REGISTER))
					response = handleRegister(count, tokenizer);

				else if (action.equalsIgnoreCase(Server.SEND_FOTO))
					response = handleSendFoto(count, tokenizer);

				else
					response = "Invalid command !";

				netOut.writeUTF(response);
				netOut.flush();

			}

			if (raf != null)
				raf.close();

			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String handleRegister(int count, StringTokenizer tokenizer) throws IOException {
		if (count != 4)
			return "Invalid REGISTER command !";

		Candidate c = new Candidate();
		if (!c.load(tokenizer))
			return "Invalid date of birth !";

		int id = number++;
		String strID = String.format("MS%03d", id);
		c.setId(strID);

		if (raf != null) {
			raf.close();
			hasFoto = false;
		}

		raf = new RandomAccessFile(createInfoFile(strID), "rw");
		c.save(raf);
		pos = raf.getFilePointer();
		raf.writeLong(0);

		return strID;

	}

	public String handleViewInfo(int count, StringTokenizer tokenizer) throws IOException {
		return (count != 2) ? "Invalid VIEW_INFO command !" : lookup(tokenizer.nextToken().trim());
	}

	public String handleSendFoto(int count, StringTokenizer tokenizer) throws IOException {
		if (raf == null)
			return "Invalid command !\nPlease register before sending foto";

		long size = Long.parseLong(tokenizer.nextToken());
		boolean pre = hasFoto;
		if (hasFoto)
			raf.setLength(pos + 8 + size);

		raf.seek(pos);
		raf.writeLong(size);
		download(size);
		hasFoto = true;

		return (pre) ? "Reupload" : "Upload";
	}

	public void download(long size) throws IOException {
		int bytesRead;
		long bytesReceived = 0;
		byte[] data = new byte[1024];

		while (bytesReceived < size) {
			bytesRead = netIn.read(data);
			raf.write(data, 0, bytesRead);
			bytesReceived += bytesRead;
		}
	}

	public static File createInfoFile(String id) {
		return new File(String.format("%s/%s.dat", Server.dir, id.toUpperCase()));
	}

	public String lookup(String id) throws IOException {
		try {
			File file = createInfoFile(id);
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			Candidate c = new Candidate();
			c.load(dis);
			dis.close();
			return "Candidate found\n" + c.toString();

		} catch (FileNotFoundException e) {
			return "Candidate not found !";
		}

	}

}
