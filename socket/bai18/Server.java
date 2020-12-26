package bai18;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class Server {
	// giả sử tất cả dữ liệu, file của Server đều nằm trong server_dir
	private static String server_dir = "D:\\TestLTM\\server";

	public static String getServer_dir() { return server_dir; }

	public static void setServer_dir(String server_dir) { Server.server_dir = server_dir; }

	public static void main(String[] args) {
		try {
			Socket socket;
			ServerSocket server = new ServerSocket(7);
			System.out.println("Waiting for client...");

			while (true) {
				socket = server.accept();
				System.out.println("Client connected");
				new Thread(new ServerProcess(socket)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
