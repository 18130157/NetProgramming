package bai25;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import base.Student;

public class ServerProcess implements Runnable {
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	private Connection conn;
	private PreparedStatement prepStmt;
	private ResultSet res;

	public ServerProcess(Socket socket) {
		this.socket = socket;
		try {
			netIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			netOut = new PrintWriter(new BufferedOutputStream(this.socket.getOutputStream()), true);
			conn = DriverManager.getConnection(Server.dbUrl, "", "");

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			netOut.println("Welcome !");
			String command;
			StringTokenizer tokenizer;

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
				if (tokenizer.countTokens() < 2) {
					netOut.println("Invalid command !");
					continue;
				}

				if (!find(tokenizer.nextToken(), tokenizer))
					continue;
				res.last();
				netOut.println(res.getRow()); // send nFound
				res.beforeFirst();
				while (res.next())
					netOut.println(new Student(res.getInt("ID"), res.getString("Name"),
							res.getInt("Age"), res.getDouble("Score")));

				res.close();
				prepStmt.close();

			}

			netIn.close();
			netOut.close();
			conn.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean find(String action, StringTokenizer tokenizer) throws SQLException {
		if (action.equalsIgnoreCase(Server.FB_NAME))
			return findByName(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_AGE))
			return findByAge(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_SCORE))
			return findByScore(tokenizer);
		netOut.println("Invalid command !");
		return false;
	}

	public boolean findByName(StringTokenizer tokenizer) throws SQLException {
		String value = tokenizer.nextToken();
		while (tokenizer.hasMoreTokens())
			value += " " + tokenizer.nextToken();
		prepStmt = conn.prepareStatement("SELECT * FROM Student WHERE Name LIKE ?",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		prepStmt.setString(1, '%' + value + '%');
		res = prepStmt.executeQuery();
		return true;
	}

	public boolean findByAge(StringTokenizer tokenizer) throws SQLException {
		int age;
		try {
			age = Integer.parseInt(tokenizer.nextToken());
		} catch (NumberFormatException e) {
			netOut.println("Invalid age !");
			return false;
		}
		prepStmt = conn.prepareStatement("SELECT * FROM Student WHERE Age = ?",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		prepStmt.setInt(1, age);
		res = prepStmt.executeQuery();
		return true;
	}

	public boolean findByScore(StringTokenizer tokenizer) throws SQLException {
		double score;
		try {
			score = Double.parseDouble(tokenizer.nextToken());
		} catch (NumberFormatException e) {
			netOut.println("Invalid score !");
			return false;
		}
		prepStmt = conn.prepareStatement("SELECT * FROM Student WHERE Score = ?",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		prepStmt.setDouble(1, score);
		res = prepStmt.executeQuery();
		return true;
	}

}
