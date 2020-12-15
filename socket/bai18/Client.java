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

	public void upload() throws IOException {
		System.out.println("Upload file");
		System.out.println("Syntax: copy source_file dest_file\n");

		userIn = new BufferedReader(new InputStreamReader(System.in));
		String line;
		StringTokenizer sTokenizer;

		while ((line = userIn.readLine()) != null) {
			if (line.trim().equalsIgnoreCase("Exit")) {
				netIn.close();
				netOut.close();
				userIn.close();
				return;
			}

			sTokenizer = new StringTokenizer(line);

			if (sTokenizer.countTokens() == 3 && sTokenizer.nextToken().equalsIgnoreCase("Copy")) {
				File sFile = new File(sTokenizer.nextToken());
				if (!sFile.exists() || !sFile.isFile()) {
					System.err.println("Invalid source file\n");
					continue;
				}
				
				netOut.writeUTF(sTokenizer.nextToken());	// dest
				netOut.writeLong(sFile.length());
				netOut.flush();

				bis = new BufferedInputStream(new FileInputStream(sFile));
				int readBytes;
				byte[] data = new byte[1024 * 1024];
				System.out.println("Uploading...");
				while ((readBytes = bis.read(data)) != -1)
					netOut.write(data, 0, readBytes);
				netOut.close();

				bis.close();
				System.out.println("Upload done !");
				break;
			} else
				System.out.println("Syntax: copy source_file dest_file\n");
		}

		netIn.close();
		netOut.close();
		userIn.close();
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client();
		client.upload();
	}

}
