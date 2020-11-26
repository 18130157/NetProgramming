import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Bai12 {
	private static List<SinhVien> data = new ArrayList<SinhVien>();

	static {
		data.add(new SinhVien("K1103", "B", 19, 8.5));
		data.add(new SinhVien("K1102", "A", 18, 7.2));
		data.add(new SinhVien("K1105", "D", 21, 9.4));
		data.add(new SinhVien("K1104", "C", 20, 8.8));
	}

	public static void saveObj(String path, List<SinhVien> list) throws IOException {
		File dFile = new File(path);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dFile));
		oos.writeInt(list.size());
		for (SinhVien s : list)
			oos.writeObject(s);
		oos.close();
	}

	public static List<SinhVien> loadObj(String path) throws Exception {
		File sFile = new File(path);
		if (!sFile.exists() || !sFile.isFile())
			return null;
		List<SinhVien> result = new ArrayList<SinhVien>();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sFile));
		int size = ois.readInt();
		for (int i = 0; i < size; i++)
			result.add((SinhVien) ois.readObject());
		ois.close();
		return result;
	}

	public static void saveAttr(String path, List<SinhVien> list) throws IOException {
		File dFile = new File(path);
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(dFile));
		dos.writeInt(list.size());
		for (SinhVien s : list) {
			dos.writeUTF(s.getMaSV());
			dos.writeUTF(s.getHoTen());
			dos.writeInt(s.getTuoi());
			dos.writeDouble(s.getdTB());
		}
		dos.close();
	}

	public static List<SinhVien> loadAttr(String path) throws IOException {
		File sFile = new File(path);
		if (!sFile.exists() || !sFile.isFile())
			return null;
		List<SinhVien> result = new ArrayList<SinhVien>();
		DataInputStream dis = new DataInputStream(new FileInputStream(sFile));
		int size = dis.readInt();
		for (int i = 0; i < size; i++)
			result.add(new SinhVien(dis.readUTF(), dis.readUTF(), dis.readInt(), dis.readDouble()));
		dis.close();
		return result;
	}

	public static void main(String[] args) throws Exception {
		String path = "path file out";
		saveObj(path, data);
		printList(loadObj(path));
		saveAttr(path, data);
		printList(loadAttr(path));
	}

	public static void printList(List<SinhVien> list) {
		for (SinhVien s : list)
			System.out.println(s + "\n");
	}
}
