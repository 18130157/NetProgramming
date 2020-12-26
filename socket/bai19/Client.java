package bai19;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
	// giả sử tất cả dữ liệu, file của Client đều nằm trong client_dir
	private static String client_dir = "D:\\TestLTM\\client";
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedReader userIn;
	private BufferedOutputStream bos;

	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			netIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			String greeting = netIn.readUTF();
			System.out.println(greeting);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void download() {
		System.out.println("Download file");
		System.out.println("Syntax: get source_file dest_file\n");

		try {
			userIn = new BufferedReader(new InputStreamReader(System.in));
			String command;
			StringTokenizer tokenizer;

			while (true) {
				command = userIn.readLine().trim();
				if (command.equalsIgnoreCase("Exit")) {
					netOut.writeUTF("Quit");
					netOut.flush();
					if (netIn.readUTF().equals("Bye"))
						break;
				}

				tokenizer = new StringTokenizer(command);
				if (tokenizer.countTokens() != 3 || !tokenizer.nextToken().equalsIgnoreCase("Get")) {
					System.err.println("Invalid command !");
					continue;
				}

				netOut.writeUTF(tokenizer.nextToken());
				netOut.flush();
				if (netIn.readUTF().equals("Not Ready")) {
					System.out.println("Source file not found");
					continue;
				}

				long size = netIn.readLong();
				File dFile = new File(client_dir + "\\" + tokenizer.nextToken());
				bos = new BufferedOutputStream(new FileOutputStream(dFile));

				int readBytes, receiveBytes = 0;
				byte[] data = new byte[1024 * 1024];

				while (receiveBytes < size) {
					readBytes = netIn.read(data);
					bos.write(data, 0, readBytes);
					receiveBytes += readBytes;
				}
				bos.close();
				System.out.println("Download " + dFile.getName() + " done !");

			}

			userIn.close();
			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		Client c = new Client("127.0.0.1", 7);
		c.download();
	}

}
