package de19_2020_2021;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class Server {
	public static String dir = "Data";
	public static String delim = "|";
	// REGISTER|Họ_và_tên|Ngày_sinh|Nơi_Cư_Trú
	public static final String REGISTER = "REGISTER";

	// SEND_FOTO|Source_file_name
	public static final String SEND_FOTO = "SEND_FOTO";

	// VIEW_INFO|Mã_Số_Đã_Đăng_Ký
	public static final String VIEW_INFO = "VIEW_INFO";

	// QUIT
	public static final String QUIT = "QUIT";

	public static void main(String[] args) {
		try {
			File destDir = new File(dir);
			if (!destDir.exists())
				destDir.mkdirs();

			int count = 0;
			Socket socket;
			ServerSocket server = new ServerSocket(2000);
			System.out.println("Waiting for client...");

			while (true) {
				socket = server.accept();
				System.out.println(String.format("Client %d - %s connected", ++count, socket.getInetAddress()));
				new Thread(new ServerProcess(socket)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
