package bai25;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private ServerSocket server;
	public static final String FB_NAME = "findByName";
	public static final String FB_AGE = "findByAge";
	public static final String FB_SCORE = "findByScore";
	public static List<String> cmds = new ArrayList<>();
	public static String dbUrl = "jdbc:odbc:students";

	static {
		cmds.add(FB_NAME);
		cmds.add(FB_AGE);
		cmds.add(FB_SCORE);
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Server() {
		try {
			server = new ServerSocket(7);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("Waiting for client...");
			int count = 0;
			Socket socket;
			Thread thread;

			while (true) {
				socket = server.accept();
				System.out.println(String.format("Client %d - %s connected", ++count, socket.getInetAddress()));
				thread = new Thread(new ServerProcess(socket));
				thread.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().run();
	}

}
