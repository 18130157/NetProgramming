package bai22.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	private BufferedReader userIn;
	private boolean quit;

	public Client() {
		quit = false;
		try {
			socket = new Socket("127.0.0.1", 7);
			netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			netOut = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println(netIn.readLine()); // print "Welcome !"
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Look up student information");
		System.out.println("Please login to look up");

		userIn = new BufferedReader(new InputStreamReader(System.in));
		login();
		if (!quit)
			lookup();

		try {
			userIn.close();
			netIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		netOut.close();
	}

	public void login() {
		System.out.println("POP3 login");
		System.out.println("Commands:\tUSER user_name");
		System.out.println("\t\tPASS password\n");
		try {
			String command, response;

			while (true) {
				command = userIn.readLine();
				netOut.println(command.trim());
				response = netIn.readLine();

				if (response.equals("Bye")) {
					quit = true;
					return;
				}

				if (response.startsWith("Invalid"))
					System.err.println(response);
				else {
					System.out.println(response);
					if (response.startsWith("Login successfully"))
						return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void lookup() {
		System.out.println("Look up student information");
		System.out.println("Commands:\t findByName name");
		System.out.println("\t\t findByAge  age");
		System.out.println("\t\t findByScore score\n");
		try {
			String command, response;
			int nFound;

			while (true) {
				command = userIn.readLine();
				netOut.println(command.trim());
				response = netIn.readLine();
				if (response.equals("Bye")) {
					quit = true;
					return;
				}

				if (response.startsWith("Invalid"))
					System.err.println(response);
				else {
					nFound = Integer.parseInt(response);
					if (nFound == 0)
						System.out.println("Student not found");
					else if (nFound == 1) {
						System.out.println("1 student was found");
						System.out.println(netIn.readLine());
					} else {
						System.out.println(nFound + " students were found");
						for (int i = 0; i < nFound; i++)
							System.out.println(netIn.readLine());
					}
				}
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Client().run();
	}

}
