package bai22.adp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import base.MyData;
import base.Student;

public class LookupState implements State {
	private ServerProcess sp;

	public LookupState(ServerProcess sp) {
		this.sp = sp;
	}

	@Override
	public void handle() {
		String command;
		StringTokenizer tokenizer;
		List<Student> re;

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
			if (tokenizer.countTokens() < 2) {
				sp.println("Invalid look up command !");
				continue;
			}

			re = find(tokenizer.nextToken(), tokenizer);
			if (re != null) {
				sp.println(re.size());
				for (Student st : re)
					sp.println(st.toString());
			}
		}

	}

	public List<Student> find(String action, StringTokenizer tokenizer) {
		if (action.equalsIgnoreCase(Server.FB_NAME))
			return findByName(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_AGE))
			return findByAge(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_SCORE))
			return findByScore(tokenizer);

		sp.println("Invalid look up command !");
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
			sp.println("Invalid age !");
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
			sp.println("Invalid score !");
			return null;
		}
		for (Student st : MyData.students)
			if (Double.compare(score, st.getScore()) == 0)
				re.add(st);
		return re;
	}

}
