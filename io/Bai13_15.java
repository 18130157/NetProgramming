import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Bai13_15 {
	private static List<SinhVien> data = new ArrayList<SinhVien>();

	static {
		data.add(new SinhVien("K1101", "Thỏ", 18, 7.2));
		data.add(new SinhVien("K1102", "Én", 19, 8.5));
		data.add(new SinhVien("K1103", "Lê Viết Nhả", 20, 8.8));
		data.add(new SinhVien("K1104", "Vàng", 21, 9.4));
		data.add(new SinhVien("K1105", "Xám", 22, 9));
	}

	public static void save(String path, List<SinhVien> list) throws IOException {
		File dFile = new File(path);
		RandomAccessFile raf = new RandomAccessFile(dFile, "rw");
		raf.writeInt(list.size());

		for (int i = 0; i < list.size(); i++)
			raf.writeLong(0L);

		long pos;
		for (int i = 0; i < list.size(); i++) {
			pos = raf.getFilePointer();
			raf.seek(4L + i * 8);
			raf.writeLong(pos);
			raf.seek(pos);
			list.get(i).save(raf);
		}
		raf.close();
	}

	public static SinhVien getSV(String path, int n) throws IOException {
		File sFile = new File(path);
		if (!sFile.exists() || !sFile.isFile())
			return null;

		RandomAccessFile raf = new RandomAccessFile(sFile, "rw");
		int size = raf.readInt();
		if (n < 1 || n > size) {
			raf.close();
			return null;
		}

		raf.seek(4L + (n - 1) * 8);
		long pos = raf.readLong();
		raf.seek(pos);

		SinhVien sv = new SinhVien();
		sv.load(raf);

		raf.close();
		return sv;
	}

	public static void updateNameSV(String path, int n, String name) throws IOException {
		File sFile = new File(path);
		if (!sFile.exists() || !sFile.isFile())
			return;

		RandomAccessFile raf = new RandomAccessFile(sFile, "rw");
		int size = raf.readInt();
		if (n == 0 || n > size) {
			raf.close();
			return;
		}
		// lấy length hiện tại của file
		long length = raf.length();
		// lấy tất cả các pointer
		List<Long> pointers = new ArrayList<Long>();
		for (int i = 0; i < size; i++)
			pointers.add(raf.readLong());
		// lấy pointer của sinh viên bị update name
		long p = pointers.get(n - 1);
		// lưu lại các thông tin không bị update của "sinh viên sẽ bị update name"
		raf.seek(p);
		String maSv = raf.readUTF();
		raf.readUTF();
		int tuoi = raf.readInt();
		double diem = raf.readDouble();
		// lưu các sinh viên phía sau "sinh viên bị update name"
		Queue<SinhVien> queue = new LinkedList<SinhVien>();
		SinhVien sv;
		for (int i = 0; i < size - n; i++) {
			sv = new SinhVien();
			sv.load(raf);
			queue.offer(sv);
		}
		// update name cho sinh viên
		raf.seek(p);
		raf.writeUTF(maSv);
		raf.writeUTF(name);
		raf.writeInt(tuoi);
		raf.writeDouble(diem);

		/*
		 * Sau khi update name, nếu có sự chênh lệch số bytes giữa "new name" và "old name"
		 * => Phải update lại pointer của các sinh viên phía sau
		 * 
		 * Ví dụ name là "Vàng", update thành "Lê Viết Nhả"
		 * => số bytes "new name" > số bytes "old name"
		 * => pointer của các sinh viên phía sau phải tăng lên
		 * 
		 * Ngược lại nếu số bytes "new name" < "old name"
		 * Thì pointer của các sinh viên phía sau phải giảm xuống
		 *
		 */

		long d = (n == size) ? raf.getFilePointer() - length : raf.getFilePointer() - pointers.get(n);
		if (d != 0)
			for (int i = n; i < size; i++)
				pointers.set(i, pointers.get(i) + d);

		// write lại các sinh viên phía sau
		while (!queue.isEmpty()) {
			sv = queue.poll();
			sv.save(raf);
		}

		// update lại pointer của các sinh viên phía sau
		if (d != 0) {
			raf.seek(4L + n * 8);
			for (int i = n; i < size; i++)
				raf.writeLong(pointers.get(i));
		}

		// chỗ này nếu số bytes "new name" < "old name"
		// => phải setLength lại để: length sau khi update phải nhỏ hơn length ban đầu
		// nếu không setLength lại thì vẫn đọc được chính xác sinh viên
		// nhưng file sẽ bị dư một ít bytes ở cuối file (dùng HxD để thấy rõ hơn)
		if (d < 0)
			raf.setLength(length + d);

		raf.close();
	}

	public static void main(String[] args) throws IOException {
		String path = "path file out";
		save(path, data);

		System.out.println("Update SV thứ 3\n");
		System.out.println(getSV(path, 3));
		updateNameSV(path, 3, "Chấm"); // chênh lệch 10 bytes
		System.out.println("——————————————————————————————————————————————");

		System.out.println("Sau khi update");
		for (int i = 1; i <= 5; i++)
			System.out.println("\n" + getSV(path, i));
	}

}
