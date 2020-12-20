package bai20;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	private BufferedReader userIn;

	public Client() {
		try {
			socket = new Socket("127.0.0.1", 7);
			netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			netOut = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println(netIn.readLine()); // print "Welcome !"
			System.out.println("Command: <number1> <operator> <number2>\n");
			userIn = new BufferedReader(new InputStreamReader(System.in));
			String command, response;

			while (true) {
				command = userIn.readLine();
				netOut.println(command.trim());
				response = netIn.readLine();

				if (response.equals("Bye"))
					break;

				if (response.startsWith("Invalid"))
					System.err.println(response + "\n");
				else
					System.out.println(response + "\n");
			}

			netIn.close();
			netOut.close();
			userIn.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client().run();
	}
}
