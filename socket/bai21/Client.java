package bai21;

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

	public Client() {
		try {
			socket = new Socket("127.0.0.1", 7);
			netIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			netOut = new PrintWriter(new OutputStreamWriter(
						new BufferedOutputStream(socket.getOutputStream()), "UTF-8"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println(netIn.readLine()); // print "Welcome !"
			System.out.println("Look up student information");
			System.out.println("Commands:\t findByName name");
			System.out.println("\t\t findByAge  age");
			System.out.println("\t\t findByScore score\n");

			userIn = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
			String command, response;
			int nFound;

			while (true) {
				command = userIn.readLine();
				netOut.println(command.trim());
				response = netIn.readLine();
				if (response.equals("Bye"))
					break;

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
