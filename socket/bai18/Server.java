package bai18;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket server;
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedOutputStream bos;

	public Server() {
		try {
			server = new ServerSocket(7);
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

	public void download() throws IOException {
		String dFile;
		try {
			dFile = netIn.readUTF();
		} catch (IOException e) {	// client enter "exit" or click Terminate on console
			netIn.close();
			netOut.close();
			System.out.println("Client disconnected");
			return;
		}

		long size = netIn.readLong();
		bos = new BufferedOutputStream(new FileOutputStream(dFile));
		writeFile(netIn, bos, size);

		bos.close();
		netIn.close();
		netOut.close();
	}

	public void writeFile(InputStream in, OutputStream out, long size) throws IOException {
		int readBytes;
		byte[] data = new byte[1024 * 1024];
		long receiveBytes = 0;

		while (receiveBytes < size) {
			readBytes = in.read(data);
			out.write(data, 0, readBytes);
			receiveBytes += readBytes;
		}
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.download();
	}

}
