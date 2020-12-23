package bai22.adp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcess implements Runnable {
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;

	private State login;
	private State lookup;
	private State quit;
	private State current;

	public ServerProcess(Socket socket) {
		this.socket = socket;
		try {
			netIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			netOut = new PrintWriter(new BufferedOutputStream(this.socket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		netOut.println("Welcome !");

		login = new LoginState(this);
		lookup = new LookupState(this);
		quit = new QuitState(this);
		current = login;
	}

	@Override
	public void run() {
		current.handle();

		try {
			netIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		netOut.close();
	}

	public String readLine() throws IOException {
		return netIn.readLine();
	}

	public void println(String str) {
		netOut.println(str);
	}

	public void println(int x) {
		netOut.println(x);
	}

	public State getCurrent() {
		return current;
	}

	public void setCurrent(State current) {
		this.current = current;
	}

	public State getLogin() {
		return login;
	}

	public State getLookup() {
		return lookup;
	}

	public State getQuit() {
		return quit;
	}

}
