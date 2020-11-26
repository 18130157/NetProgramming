import java.io.File;
import java.io.IOException;

public class Bai6 {

	public static void deleteAll(String path, String... ext) throws IOException {
		File file = new File(path);
		if (!file.exists())
			return;

		if (file.isFile() && isExtended(file, ext))
			file.delete();
		else if (file.isDirectory())
			for (File f : file.listFiles())
				deleteAll(f.getPath(), ext);
	}

	public static boolean isExtended(File file, String... ext) {
		for (String str : ext)
			if (file.getName().endsWith(str))
				return true;
		return false;
	}

	public static void main(String[] args) throws IOException {
		String path = "path";
		deleteAll(path, ".txt", ".docx", ".pdf");
	}
}
