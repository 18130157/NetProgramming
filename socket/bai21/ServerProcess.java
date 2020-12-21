package bai21;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import base.Student;

public class ServerProcess implements Runnable {
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;

	public ServerProcess(Socket socket) {
		this.socket = socket;
		try {
			netIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
			netOut = new PrintWriter(new OutputStreamWriter(
						new BufferedOutputStream(this.socket.getOutputStream()), "UTF-8"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			netOut.println("Welcome !");
			String command = "", type;
			StringTokenizer tokenizer;
			List<Student> re;

			while (true) {
				try {
					command = netIn.readLine();
					if (command.equalsIgnoreCase("Quit")) {
						netOut.println("Bye");
						break;
					}
				} catch (Exception e) { // client click Terminate on console
					break;
				}

				tokenizer = new StringTokenizer(command);
				if (tokenizer.countTokens() < 2 || !isCommand((type = tokenizer.nextToken()))) {
					netOut.println("Invalid command !");
					continue;
				}

				re = find(type, tokenizer);
				if (re != null) {
					netOut.println(re.size());
					for (Student st : re)
						netOut.println(st.toString());
				}

			}

			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isCommand(String command) {
		for (String c : Server.cmds)
			if (command.equalsIgnoreCase(c))
				return true;
		return false;
	}

	public List<Student> find(String type, StringTokenizer tokenizer) {
		List<Student> re = new ArrayList<>();
		String value = tokenizer.nextToken();

		if (type.equalsIgnoreCase(Server.FB_NAME)) {
			while (tokenizer.hasMoreTokens())
				value += " " + tokenizer.nextToken();

			for (Student st : Server.list)
				if (value.equalsIgnoreCase(st.getName()))
					re.add(st);
			return re;
		}

		if (type.equalsIgnoreCase(Server.FB_AGE)) {
			int age;
			try {
				age = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				netOut.println("Invalid age !");
				return null;
			}
			for (Student st : Server.list)
				if (age == st.getAge())
					re.add(st);
			return re;
		}

		if (type.equalsIgnoreCase(Server.FB_SCORE)) {
			double score;
			try {
				score = Double.parseDouble(value);
			} catch (NumberFormatException e) {
				netOut.println("Invalid score !");
				return null;
			}
			for (Student st : Server.list)
				if (Double.compare(score, st.getScore()) == 0)
					re.add(st);
			return re;
		}

		return null;
	}

}
