import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Bai8 {

	public static boolean fileCopy(String sFile, String destFile, boolean moved) throws IOException {
		File sourceFile = new File(sFile);
		if (!sourceFile.exists() || !sourceFile.isFile())
			return false;

		File dFile = new File(destFile);
		if (!dFile.exists()) {
			String directoryPath = dFile.getCanonicalPath().substring(0,
					dFile.getCanonicalPath().length() - dFile.getName().length());
			File desDir = new File(directoryPath);
			boolean check = true;
			if (!desDir.exists())
				check = desDir.mkdirs();
			if (!check)
				return false;
		}

		InputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
		OutputStream bos = new BufferedOutputStream(new FileOutputStream(dFile));

		byte[] data = new byte[1024 * 1024]; // 1 MB
		int readBytes;

		while ((readBytes = bis.read(data)) != -1)
			bos.write(data, 0, readBytes);
		bis.close();
		bos.close();

		if (moved)
			sourceFile.delete();
		return true;
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		String sFile = "path source file";
		String destFile = "path dest file";
		boolean moved = false;
		System.out.println(fileCopy(sFile, destFile, moved));
		System.out.println(System.currentTimeMillis() - start + "ms");

	}

}
