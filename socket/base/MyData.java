package base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyData {
	public static List<Student> students = new ArrayList<>();
	public static Map<String, String> mapUser = new HashMap<>();
	
	static {
		students.add(new Student(1, "Nguyen Canh Ty", 18, 7.2));
		students.add(new Student(2, "Tran Tan Suu", 19, 8.5));
		students.add(new Student(3, "Le Nham Dan", 20, 9.4));
		students.add(new Student(4, "Nguyen Canh Ty", 21, 9));
		students.add(new Student(5, "Pham Quy Mao", 18, 9));
		students.add(new Student(6, "Huynh Giap Thin", 21, 9.4));
		students.add(new Student(7, "Phan At Ty", 18, 9.4));
		
		mapUser.put("ti", "2020");
		mapUser.put("suu", "2021");
		mapUser.put("dan", "2022");
		mapUser.put("meo", "2023");
		mapUser.put("thin", "2024");
	}
}
