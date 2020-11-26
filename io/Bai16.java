import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Bai16 {
	private static List<SinhVien> data = new ArrayList<SinhVien>();

	static {
		SinhVien a, b, c, d, e;
		data.add(a = new SinhVien("K1101", "Ân"));
		data.add(b = new SinhVien("K1102", "Bảo"));
		data.add(c = new SinhVien("K1103", "Cường"));
		data.add(d = new SinhVien("K1104", "Đăng"));
		data.add(e = new SinhVien("K1105", "Én"));

		MonHoc ltm1 = new MonHoc("214252", "Lập trình mạng", 5.8);
		MonHoc ltm2 = new MonHoc("214252", "Lập trình mạng", 6.8);
		MonHoc ltm3 = new MonHoc("214252", "Lập trình mạng", 7.8);
		MonHoc ltm4 = new MonHoc("214252", "Lập trình mạng", 8.8);
		MonHoc ltm5 = new MonHoc("214252", "Lập trình mạng", 9.8);

		MonHoc ltw1 = new MonHoc("214462", "Lập trình web", 7.5);
		MonHoc ltw2 = new MonHoc("214462", "Lập trình web", 7.6);
		MonHoc ltw3 = new MonHoc("214462", "Lập trình web", 7.7);
		MonHoc ltw4 = new MonHoc("214462", "Lập trình web", 7.8);
		MonHoc ltw5 = new MonHoc("214462", "Lập trình web", 7.9);

		MonHoc ltnc2 = new MonHoc("214331", "Lập trình nâng cao", 8);
		MonHoc ltnc4 = new MonHoc("214331", "Lập trình nâng cao", 9);

		a.addMonHoc(ltm1, ltw1);
		b.addMonHoc(ltm2, ltw2, ltnc2);
		c.addMonHoc(ltm3, ltw3);
		d.addMonHoc(ltm4, ltw4, ltnc4);
		e.addMonHoc(ltm5, ltw5);
	}

	public static void export(String path, List<SinhVien> list, String charset, String delimited) throws IOException {
		File dFile = new File(path);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(dFile), charset);
		PrintWriter pw = new PrintWriter(osw, true);

		for (SinhVien sv : list)
			pw.println(sv.line(delimited));

		pw.close();
	}

	public static List<SinhVien> importList(String path, String charset, String delimited) throws IOException {
		File sFile = new File(path);
		if (!sFile.exists() || !sFile.isFile())
			return null;

		List<SinhVien> result = new ArrayList<SinhVien>();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(sFile), charset);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] arr = line.split(delimited);
			SinhVien sv = new SinhVien(arr[0], arr[1]);
			result.add(sv);

			if (arr.length > 3) {
				MonHoc m = new MonHoc();
				for (int i = 2; i < arr.length; i++)
					if (i % 2 == 0)
						m.setTenMH(arr[i]);
					else {
						m.setDiem(Double.parseDouble(arr[i]));
						sv.addMonHoc(m);
						m = new MonHoc();
					}
			}
		}
		br.close();
		return result;
	}

	public static void main(String[] args) throws IOException {
		String path = "path file out";
		String charset = "UTF-8";
		String delimited = "\t";
		export(path, data, charset, delimited);
		printList(importList(path, charset, delimited));
	}

	public static void printList(List<SinhVien> list) {
		if (list == null) {
			System.out.println("null");
			return;
		}
		for (SinhVien sv : list)
			System.out.println(sv.toString2());
	}

}
