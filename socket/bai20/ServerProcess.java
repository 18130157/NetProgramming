package bai20;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class ServerProcess implements Runnable {
//	private ServerSocket server;
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;

	private static DecimalFormat df;

	static {
		StringBuilder stb = new StringBuilder();
		for (int i = 1; i <= 32; i++)
			stb.append('#');
		stb.insert(16, '.');
		df = new DecimalFormat(stb.toString());
		df.setRoundingMode(RoundingMode.HALF_UP);
	}

	public ServerProcess(Socket socket) {
		try {
			this.socket = socket;
			netIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			netOut = new PrintWriter(new BufferedOutputStream(this.socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			netOut.println("Welcome !");
			String command = "", operator;
			double n1, n2, re; // number1 (left), number2 (right), result

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

				operator = findOperator(command);
				if (operator == null) {
					netOut.println("Invalid command");
					continue;
				}

				StringTokenizer tokenizer = new StringTokenizer(command, operator);

				try {
					n1 = Double.parseDouble(tokenizer.nextToken());
					n2 = Double.parseDouble(tokenizer.nextToken());
				} catch (NumberFormatException e) {
					netOut.println("Invalid operand");
					continue;
				}

				re = calc(n1, n2, operator);
				netOut.println(String.format("%s %s %s = %s", text(n1), operator, text(n2), text(re)));
			}

			netIn.close();
			netOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String findOperator(String command) {
		if (new StringTokenizer(command, "+").countTokens() == 2)
			return "+";
		if (new StringTokenizer(command, "-").countTokens() == 2)
			return "-";
		if (new StringTokenizer(command, "*").countTokens() == 2)
			return "*";
		if (new StringTokenizer(command, "/").countTokens() == 2)
			return "/";
		return null;
	}

	public double calc(double number1, double number2, String operator) {
		switch (operator) {
		case "+":
			return number1 + number2;
		case "-":
			return number1 - number2;
		case "*":
			return number1 * number2;
		case "/":
			return number1 / number2;
		}
		return Double.NaN;
	}

	public String text(double number) {
		return df.format(number);
	}

}
