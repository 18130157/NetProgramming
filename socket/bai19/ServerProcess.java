package bai19;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerProcess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private BufferedInputStream bis;

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

				File sFile = new File(Server.getServer_dir() + "\\" + str);
				if (!sFile.exists() || !sFile.isFile() || !sFile.canRead()) {
					netOut.writeUTF("Not Ready");
					netOut.flush();
					continue;
				}
				netOut.writeUTF("Ready");
				netOut.writeLong(sFile.length());

				bis = new BufferedInputStream(new FileInputStream(sFile));
				int readBytes;
				byte[] data = new byte[1024 * 1024];

				while ((readBytes = bis.read(data)) != -1) {
					netOut.write(data, 0, readBytes);
					netOut.flush();
				}
				bis.close();

			}

			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
