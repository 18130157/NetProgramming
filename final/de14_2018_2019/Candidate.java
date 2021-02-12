package de14_2018_2019;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Candidate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id, name, dob, address;

	public Candidate() {
	}

	public void load(DataInput di) throws IOException {
		name = di.readUTF();
		dob = di.readUTF();
		address = di.readUTF();
	}

	public boolean load(StringTokenizer tokenizer) {
		name = tokenizer.nextToken().trim();
		dob = tokenizer.nextToken().trim();
		if (!checkAge())
			return false;
		address = tokenizer.nextToken().trim();
		return true;
	}

	public boolean checkAge() {
		try {
			int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(dob.substring(dob.length() - 4));
			return age >= 0 && age <= 6;
		} catch (IndexOutOfBoundsException | NumberFormatException e) {
			return false;
		}
	}

	public void save(DataOutput dop) throws IOException {
		dop.writeUTF(id);
		dop.writeUTF(name);
		dop.writeUTF(dob);
		dop.writeUTF(address);
	}

	@Override
	public String toString() {
		return String.format("%s — %s — %s — %s", id, name, dob, address);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
