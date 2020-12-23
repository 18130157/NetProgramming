package bai22.core;

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

import bai21.Server;
import base.MyData;
import base.Student;

public class ServerProcess implements Runnable {

	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	private boolean quit;

	public ServerProcess(Socket socket) {
		this.socket = socket;
		quit = false;
		try {
			netIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			netOut = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(this.socket.getOutputStream())),
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		netOut.println("Welcome !");
		handleLogin();
		if (!quit)
			handleLookup();

		try {
			netIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		netOut.close();
	}

	public void handleLogin() {
		String command, action, uname = "", pass;
		StringTokenizer tokenizer;

		while (true) {
			try {
				command = netIn.readLine();
				if (command.equalsIgnoreCase("Quit")) {
					netOut.println("Bye");
					quit = true;
					return;
				}
			} catch (Exception e) { // client click Terminate on console
				return;
			}

			tokenizer = new StringTokenizer(command);
			if (tokenizer.countTokens() != 2) {
				netOut.println("Invalid login command !");
				continue;
			}

			action = tokenizer.nextToken();

			if (action.equalsIgnoreCase("USER")) {
				uname = tokenizer.nextToken();
				if (checkUname(uname))
					netOut.println("OK");
				else
					netOut.println("Username not found !");
			}

			else if (action.equalsIgnoreCase("PASS")) {
				pass = tokenizer.nextToken();
				if (checkLogin(uname, pass)) {
					netOut.println("Login successfully !");
					return;
				} else
					netOut.println("Incorrect username or password !");
			}
		}

	}

	public void handleLookup() {
		String command;
		StringTokenizer tokenizer;
		List<Student> re;

		while (true) {
			try {
				command = netIn.readLine();
				if (command.equalsIgnoreCase("Quit")) {
					netOut.println("Bye");
					quit = true;
					return;
				}
			} catch (Exception e) { // client click Terminate on console
				return;
			}

			tokenizer = new StringTokenizer(command);
			if (tokenizer.countTokens() < 2) {
				netOut.println("Invalid look up command !");
				continue;
			}

			re = find(tokenizer.nextToken(), tokenizer);
			if (re != null) {
				netOut.println(re.size());
				for (Student st : re)
					netOut.println(st.toString());
			}
		}

	}

	public boolean checkUname(String uname) {
		return MyData.mapUser.containsKey(uname);
	}

	public boolean checkLogin(String uname, String pass) {
		try {
			return MyData.mapUser.get(uname).equals(pass);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public List<Student> find(String action, StringTokenizer tokenizer) {
		if (action.equalsIgnoreCase(Server.FB_NAME))
			return findByName(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_AGE))
			return findByAge(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_SCORE))
			return findByScore(tokenizer);

		netOut.println("Invalid look up command !");
		return null;
	}

	public List<Student> findByName(StringTokenizer tokenizer) {
		List<Student> re = new ArrayList<>();
		String value = tokenizer.nextToken();
		while (tokenizer.hasMoreTokens())
			value += " " + tokenizer.nextToken();
		for (Student st : MyData.students)
			if (value.equalsIgnoreCase(st.getName()))
				re.add(st);
		return re;
	}

	public List<Student> findByAge(StringTokenizer tokenizer) {
		List<Student> re = new ArrayList<>();
		String value = tokenizer.nextToken();
		int age;
		try {
			age = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			netOut.println("Invalid age !");
			return null;
		}
		for (Student st : MyData.students)
			if (age == st.getAge())
				re.add(st);
		return re;
	}

	public List<Student> findByScore(StringTokenizer tokenizer) {
		List<Student> re = new ArrayList<>();
		String value = tokenizer.nextToken();
		double score;
		try {
			score = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			netOut.println("Invalid score !");
			return null;
		}
		for (Student st : MyData.students)
			if (Double.compare(score, st.getScore()) == 0)
				re.add(st);
		return re;
	}

}
