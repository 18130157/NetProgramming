package bai18;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerProcess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedOutputStream bos;

	public ServerProcess(Socket socket) {
		try {
			this.socket = socket;
			netIn = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
			netOut = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));

			netOut.writeUTF("Welcome");
			netOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String str;
			while (true) {
				str = netIn.readUTF();
				if (str.equals("Quit")) {
					netOut.writeUTF("Bye");
					netOut.flush();
					break;
				}

				long size = netIn.readLong();
				bos = new BufferedOutputStream(new FileOutputStream(Server.getServer_dir() + "\\" + str));
				int readBytes, receiveBytes = 0;
				byte[] data = new byte[1024 * 1024];

				while (receiveBytes < size) {
					readBytes = netIn.read(data);
					bos.write(data, 0, readBytes);
					receiveBytes += readBytes;
				}
				
				bos.close();
			}

			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
