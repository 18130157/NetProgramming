package bai19;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {

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

	public void download() throws IOException {
		System.out.println("Download file");
		System.out.println("Syntax: get source_file dest_file\n");

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

			if (sTokenizer.countTokens() == 3 && sTokenizer.nextToken().equalsIgnoreCase("Get")) {
				netOut.writeUTF(sTokenizer.nextToken());
				netOut.flush();
				if (!netIn.readUTF().equals("Good")) {
					System.err.println("Invalid source file\n");
					continue;
				}
				long size = netIn.readLong();

				String pathDestFile = sTokenizer.nextToken();
				File dFile = createFile(pathDestFile);
				if (dFile == null) {
					System.err.println("Invalid dest file\n");
					netOut.writeUTF("Fail");
					netOut.flush();
					continue;
				}

				netOut.writeUTF("Ready");
				netOut.flush();

				bos = new BufferedOutputStream(new FileOutputStream(dFile));
				System.out.println("Downloading...");
				writeFile(netIn, bos, size);

				bos.close();
				System.out.println("Download done !");
				break;

			} else
				System.out.println("Syntax: get source_file dest_file\n");
		}

		netIn.close();
		netOut.close();
		userIn.close();
	}

	public File createFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			String pathDestDir = file.getCanonicalPath().substring(0,
					file.getCanonicalPath().length() - file.getName().length() - 1);
			File dir = new File(pathDestDir);
			if (!dir.exists())
				if (!dir.mkdirs())
					return null;
		}
		return file;
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
		Client c = new Client("127.0.0.1", 7);
		c.download();

	}

}
