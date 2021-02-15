package bai20;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("resource")
public class Server {
	public static final String PLUS = "+";
	public static final String MINUS = "-";
	public static final String MULTIPLY = "*";
	public static final String DIVIDE = "/";
	public static List<String> operators = new ArrayList<>();

	static {
		operators.add(PLUS);
		operators.add(MINUS);
		operators.add(MULTIPLY);
		operators.add(DIVIDE);
	}

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(7);
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

}
