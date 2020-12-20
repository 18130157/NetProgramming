package bai21;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import base.Student;

public class Server {
	private ServerSocket server;
	public static final String FB_NAME = "findByName";
	public static final String FB_AGE = "findByAge";
	public static final String FB_SCORE = "findByScore";
	public static List<String> cmds = new ArrayList<>();
	public static List<Student> list = new ArrayList<>();

	static {
		cmds.add(FB_NAME);
		cmds.add(FB_AGE);
		cmds.add(FB_SCORE);
		list.add(new Student(1, "Thỏ", 18, 7.2));
		list.add(new Student(2, "Én", 19, 8.5));
		list.add(new Student(3, "Vàng", 20, 9.4));
		list.add(new Student(4, "Xám", 21, 9));
		list.add(new Student(5, "Én", 18, 9));
		list.add(new Student(6, "Chấm", 21, 9.4));
		list.add(new Student(7, "Cúc Cu", 18, 9.4));
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
