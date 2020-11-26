import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Bai7 {

	public static void copyAll(String sDir, String dDir, String... ext) throws IOException {
		File sourceDir = new File(sDir);
		if (!sourceDir.exists())
			return;

		File desDir = new File(dDir);
		if (!desDir.exists())
			desDir.mkdirs();

		for (File f : sourceDir.listFiles()) {
			if (f.isFile() && isExtended(f, ext)) {
				InputStream fis = new FileInputStream(f);
				OutputStream fos = new FileOutputStream(desDir.getAbsolutePath() + "\\" + f.getName());
				byte[] data = new byte[1024 * 1024 * 10]; // 10 MB
				int readBytes;
				while ((readBytes = fis.read(data)) != -1)
					fos.write(data, 0, readBytes);
				fis.close();
				fos.close();
			} else if (f.isDirectory())
				copyAll(f.getPath(), dDir, ext);
		}
	}

	public static boolean isExtended(File file, String... ext) {
		for (String e : ext)
			if (file.getName().endsWith(e))
				return true;
		return false;
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		String sDir = "path source dir";
		String dDir = "path dest dir";
		String[] ext = { ".pdf", ".docx", ".xlsx" };
		copyAll(sDir, dDir, ext);
//		copyAll(sDir, dDir, ".txt", ".pptx");
		System.out.println(System.currentTimeMillis() - start + "ms");
	}

}
