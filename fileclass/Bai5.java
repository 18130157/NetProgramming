import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bai5 {
	private static List<File> list = new ArrayList<File>();

	public static void findAll(String path, String... ext) {
		File file = new File(path);
		if (!file.exists())
			return;

		if (file.isFile() && isExtended(file, ext))
			list.add(file);
		else if (file.isDirectory())
			for (File f : file.listFiles())
				findAll(f.getPath(), ext);
	}

	public static boolean isExtended(File file, String... ext) {
		for (String str : ext)
			if (file.getName().endsWith(str))
				return true;
		return false;
	}

	public static void main(String[] args) throws IOException {
		String path = "path";
		String[] ext = { ".docx", ".txt" };

		findAll(path, ext);
		for (File f : list)
			System.out.println(f.getAbsolutePath());
		list.clear();

		findAll(path, ".txt", ".docx", ".pdf");
		for (File f : list)
			System.out.println(f.getAbsolutePath());

	}
}
