package bai22.adp;

import java.util.StringTokenizer;

import base.MyData;

public class LoginState implements State {
	private ServerProcess sp;

	public LoginState(ServerProcess sp) {
		this.sp = sp;
	}

	@Override
	public void handle() {
		String command, action, uname = "", pass;
		StringTokenizer tokenizer;

		while (true) {
			try {
				command = sp.readLine();
				if (command.equalsIgnoreCase("Quit")) {
					sp.setCurrent(sp.getQuit());
					sp.run();
					return;
				}
			} catch (Exception e) { // client click Terminate on console
				return;
			}

			tokenizer = new StringTokenizer(command);
			if (tokenizer.countTokens() != 2) {
				sp.println("Invalid login command !");
				continue;
			}

			action = tokenizer.nextToken();

			if (action.equalsIgnoreCase("USER")) {
				uname = tokenizer.nextToken();
				if (checkUname(uname))
					sp.println("OK");
				else
					sp.println("Username not found !");
			}

			else if (action.equalsIgnoreCase("PASS")) {
				pass = tokenizer.nextToken();
				if (checkLogin(uname, pass)) {
					sp.println("Login successfully !");
					sp.setCurrent(sp.getLookup());
					sp.run();
					return;
				} else
					sp.println("Incorrect username or password !");
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

}
