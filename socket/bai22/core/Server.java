package bai22.core;

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
	public static List<String> lookupCmds = new ArrayList<>();

	static {
		lookupCmds.add(FB_NAME);
		lookupCmds.add(FB_AGE);
		lookupCmds.add(FB_SCORE);
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
