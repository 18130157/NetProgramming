package de14_2018_2019;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Client {
	private Registry registry;
	private BufferedReader userIn;

	public Client() {
		try {
			registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			IRegister reg = (IRegister) registry.lookup("register");

			System.out.println(reg.getBanner());
			userIn = new BufferedReader(new InputStreamReader(System.in));
			int sid;
			String command, response;
			outer: while (true) {
				command = userIn.readLine().trim();
				if (command.length() == 0)
					continue;

				response = reg.sendCommand(command);
				if (response.equals("Bye"))
					break;

				if (response.startsWith("Invalid") || response.startsWith("Candidate")) {
					System.out.println(response);
					continue;
				}

				sid = reg.getSessionID(response);
				System.out.println("Registration successful");
				System.out.println("Your ID: " + response);
				System.out.println("Please send foto whose filename same your ID");

				while (true) {
					command = userIn.readLine().trim();
					if (command.length() == 0)
						continue;

					if (command.equalsIgnoreCase(Server.QUIT)) {
						reg.sendFoto(sid, command);
						break outer;
					}

					StringTokenizer tokenizer = new StringTokenizer(command, Server.delim);
					if (tokenizer.countTokens() != 2 ||
							!tokenizer.nextToken().trim().equalsIgnoreCase(Server.SEND_FOTO)) {
						System.out.println("Invalid SEND_FOTO command !");
						continue;
					}

					File sFile = new File(tokenizer.nextToken().trim());
					if (!sFile.exists() || !sFile.isFile()) {
						System.out.println("Source file not found !");
						continue;
					}

					response = reg.sendFoto(sid, command);
					if (response.startsWith("Invalid")) {
						System.out.println(response);
						continue;
					}

					InputStream bis = new BufferedInputStream(new FileInputStream(sFile));
					int bytesRead;
					byte[] data = new byte[1024 * 1024];

					while ((bytesRead = bis.read(data)) != -1)
						if (bytesRead == data.length)
							reg.write(sid, data);
						else
							reg.write(sid, Arrays.copyOf(data, bytesRead));

					bis.close();
					reg.close(sid);

					System.out.println("Upload foto " + sFile.getName() + " done !");
					break;
				}

			}

			userIn.close();
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client().run();
	}

}
