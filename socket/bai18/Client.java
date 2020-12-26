package bai18;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
	private BufferedInputStream bis;

	public Client() {
		try {
			socket = new Socket("127.0.0.1", 7);
			netIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			String greeting = netIn.readUTF();
			System.out.println(greeting);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void upload() {
		System.out.println("Upload file");
		System.out.println("Syntax: copy source_file dest_file\n");

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
				if (tokenizer.countTokens() != 3 || !tokenizer.nextToken().equalsIgnoreCase("Copy")) {
					System.err.println("Invalid command !");
					continue;
				}

				File sFile = new File(client_dir + "\\" + tokenizer.nextToken());
				if (!sFile.exists() || !sFile.isFile()) {
					System.err.println("Invalid source file !");
					continue;
				}

				netOut.writeUTF(tokenizer.nextToken()); // send name dest file to server
				netOut.writeLong(sFile.length());
				netOut.flush();

				int readBytes;
				byte[] data = new byte[1024 * 1024];
				bis = new BufferedInputStream(new FileInputStream(sFile));
				while ((readBytes = bis.read(data)) != -1) {
					netOut.write(data, 0, readBytes);
					netOut.flush();
				}
				
				System.out.println("Upload " + sFile.getName() + " done !");
				bis.close();
			}

			userIn.close();
			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client client = new Client();
		client.upload();
	}

}
