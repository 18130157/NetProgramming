package de19_2020_2021;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedReader userIn;
	private String ID;

	public Client() {
		try {
			socket = new Socket("127.0.0.1", 2000);
			netIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			String greeting = netIn.readUTF();
			System.out.println(greeting);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			userIn = new BufferedReader(new InputStreamReader(System.in));
			String command, response;
			StringTokenizer tokenizer;

			while (true) {
				command = userIn.readLine().trim();
				if (command.length() == 0)
					continue;

				tokenizer = new StringTokenizer(command, Server.delim);
				if (tokenizer.countTokens() == 2 && tokenizer.nextToken().trim().equalsIgnoreCase(Server.SEND_FOTO)) {
					response = sendFoto(tokenizer);
					System.out.println(response);
					continue;
				}

				netOut.writeUTF(command);
				netOut.flush();
				response = netIn.readUTF();
				if (response.equals("Bye"))
					break;

				if (response.startsWith("Invalid") || response.startsWith("Candidate")) {
					System.out.println(response);
					continue;
				}

				System.out.println("Registration successful");
				System.out.println("Your ID: " + (ID = response));
				System.out.println("Please send foto file");
				System.out.println("JPG file: size <= 100kB");

			}

			userIn.close();
			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendFoto(StringTokenizer tokenizer) throws IOException {
		if (ID == null)
			return "Invalid command !\nPlease register before sending foto";

		File sFile = new File(tokenizer.nextToken().trim());

		if (!sFile.exists() || !sFile.isFile())
			return "Source file not found !";

		long size = sFile.length();
		String name = sFile.getName();
		if (size > 102400 || !name.toLowerCase().endsWith(".jpg"))
			return "Invalid source file !";

		netOut.writeUTF(Server.SEND_FOTO + Server.delim + size);
		netOut.flush();
		upload(sFile);
		return String.format("%s %s for %s done !", netIn.readUTF(), name, ID);

	}

	public void upload(File file) throws IOException {
		int bytesRead;
		byte[] data = new byte[1024];
		InputStream bis = new BufferedInputStream(new FileInputStream(file));
		while ((bytesRead = bis.read(data)) != -1) {
			netOut.write(data, 0, bytesRead);
			netOut.flush();
		}
		bis.close();
	}

	public static void main(String[] args) {
		new Client().run();
	}

}
