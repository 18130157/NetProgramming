import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class Bai10 {
	public static final String SUFFIX = "_split";

	public static boolean splitFile(String source, long size) throws IOException {
		File sFile = new File(source);
		if (!sFile.exists() || size <= 0)
			return false;

		long nSubFile = sFile.length() / size;
		long oddBytes = sFile.length() % size; // phần dư
		InputStream bis = new BufferedInputStream(new FileInputStream(sFile));
		OutputStream bos = null;
		int readBytes;
		byte[] data = new byte[(int) size];
		// nếu size là số lớn hơn Integer.MAX_VALUE
		// => từ long cast xuống int sẽ bị bug
		// => error ?

		for (long i = 1; i <= nSubFile; i++) {
			bos = new BufferedOutputStream(new FileOutputStream(createSubFilePath(sFile, false, i)));
			readBytes = bis.read(data);
			bos.write(data, 0, readBytes);
			bos.close();
		}

		if (oddBytes > 0) {
			bos = new BufferedOutputStream(new FileOutputStream(createSubFilePath(sFile, true, 0)));
			while ((readBytes = bis.read(data)) != -1)
				bos.write(data, 0, readBytes);
		}
		bis.close();
		bos.close();
		sFile.delete();
		return true;
	}

	public static boolean splitFile(String source, int nSubFile) throws IOException {
		File sFile = new File(source);
		if (!sFile.exists() || nSubFile <= 1)
			return false;
		long size = sFile.length() / nSubFile;
		long odd = sFile.length() - size * nSubFile;
		return splitFile(source, size + odd);
	}

	public static String createSubFilePath(File sFile, boolean odd, long serial) {
		String srcPath = sFile.getAbsolutePath();
		return (odd) ? srcPath + SUFFIX + "X" : srcPath + (SUFFIX + ((serial < 10) ? "0" : "") + serial);
	}

	public static boolean joinFile(String dir, String nameFile) throws IOException {
		File sDir = new File(dir);
		if (!sDir.exists() || !sDir.isDirectory())
			return false;
		File[] files = sDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String str = new File(name).getName();
				return str.matches(nameFile + SUFFIX + ".+");
			}
		});
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		InputStream bis;
		OutputStream bos = new BufferedOutputStream(new FileOutputStream(dir + "\\" + nameFile));
		byte[] data = new byte[1024 * 1024];
		int readBytes;

		for (File f : files) {
			bis = new BufferedInputStream(new FileInputStream(f));
			while ((readBytes = bis.read(data)) != -1)
				bos.write(data, 0, readBytes);
			bis.close();
			f.delete();
		}
		bos.close();
		return true;
	}

	public static void main(String[] args) throws IOException {
		String source = "path source file";
		long size = 1024 * 1024 * 4;
		System.out.println(splitFile(source, size));
		int nSubFile = 20;
		System.out.println(splitFile(source, nSubFile));

		String dir = "path folder contains all sub file";
		String nameFile = "name.ext";
		System.out.println(joinFile(dir, nameFile));
	}
}
