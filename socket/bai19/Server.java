package bai19;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket server;
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedInputStream bis;

	public Server(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for client...");
			socket = server.accept();
			System.out.println("Client connected");
			netIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			netOut.writeUTF("Welcome");
			netOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void upload() throws IOException {
		File sFile;
		while (true) {
			try {
				sFile = new File(netIn.readUTF());
			} catch (IOException e) {	// client enter "exit" or click Terminate on console
				netIn.close();
				netOut.close();
				System.out.println("Client disconnected");
				return;
			}

			if (!sFile.exists() || !sFile.isFile()) {
				netOut.writeUTF("Fail");
				netOut.flush();
				continue;
			}

			netOut.writeUTF("Good");
			netOut.writeLong(sFile.length());
			netOut.flush();
			
			if (!netIn.readUTF().equals("Ready"))
				continue;

			bis = new BufferedInputStream(new FileInputStream(sFile));
			int readBytes;
			byte[] data = new byte[1024 * 1024];
			while ((readBytes = bis.read(data)) != -1)
				netOut.write(data, 0, readBytes);

			bis.close();
			netIn.close();
			netOut.close();
			break;
		}

	}

	public static void main(String[] args) throws IOException {
		Server s = new Server(7);
		s.upload();
	}

}
