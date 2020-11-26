import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class Bai14 {
	// pack & unpack
	// ---------Header--------
	// số file
	// pointer file 1
	// pointer file 2
	// pointer file n
	// -----------------------
	// name file 1
	// size file 1
	// content file 1

	// name file 2
	// size file 2
	// content file 2

	// name file n
	// size file n
	// content file n

	public static void pack(String pathFolder, String destFile) throws IOException {
		File dir = new File(pathFolder);
		if (!dir.exists() || !dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		int n = files.length;
		if (n == 0)
			return;
		RandomAccessFile raf = new RandomAccessFile(new File(destFile), "rw");
		raf.writeInt(n);
		for (int i = 0; i < n; i++)
			raf.writeLong(0L);

		InputStream bis;
		long pos;
		int readBytes;
		byte[] data = new byte[1024 * 1024];
		for (int i = 0; i < n; i++) {
			pos = raf.getFilePointer();
			raf.seek(4 + i * 8);
			raf.writeLong(pos);
			raf.seek(pos);

			raf.writeUTF(files[i].getName());
			raf.writeLong(files[i].length());
			bis = new BufferedInputStream(new FileInputStream(files[i]));

			while ((readBytes = bis.read(data)) != -1)
				raf.write(data, 0, readBytes);
			bis.close();
		}
		raf.close();
	}

	public static void unpack(String pathSourceFile, String pathDestFolder, String name) throws IOException {
		File sFile = new File(pathSourceFile);
		if (!sFile.exists() || !sFile.isFile())
			return;

		RandomAccessFile raf = new RandomAccessFile(sFile, "rw");
		int n = raf.readInt();

		for (int i = 0; i < n; i++) {
			raf.seek(4 + i * 8);
			raf.seek(raf.readLong());

			if (raf.readUTF().equals(name)) {
				OutputStream bos = new BufferedOutputStream(new FileOutputStream(pathDestFolder + "\\unpack-" + name));
				long size = raf.readLong();
				writeFile(raf, bos, size);
				raf.close();
				bos.close();
				return;
			}
		}
		raf.close();
	}

	public static void writeFile(RandomAccessFile in, OutputStream out, long fileSize) throws IOException {
		// ví dụ
		// file size = 250, buf = 100 => write 2 lần 100, 1 lần 50
		// file size = 250, buf = 250 => write 1 lần 250
		// file size = 250, buf = 300 => write 1 lần 250
		byte[] data = new byte[1024 * 1024];
		int readBytes;
		int nFullBufferWrite = (int) (fileSize / data.length);
		int oddBytes = (int) (fileSize % data.length);
		int nWrite = nFullBufferWrite + ((oddBytes > 0) ? 1 : 0);

		for (int i = 0; i < nWrite; i++) {
			readBytes = in.read(data);
			if (i == nWrite - 1 && oddBytes > 0)
				out.write(data, 0, oddBytes);
			else
				out.write(data, 0, readBytes);
		}
	}

	// pack2 & unpack2
	// số file

	// name file 1
	// size file 1
	// content file 1

	// name file 2
	// size file 2
	// content file 2

	// name file n
	// size file n
	// content file n

	public static void pack2(String pathFolder, String destFile) throws IOException {
		File dir = new File(pathFolder);
		if (!dir.exists() || !dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		int n = files.length;
		if (n == 0)
			return;
		RandomAccessFile raf = new RandomAccessFile(new File(destFile), "rw");
		raf.writeInt(n);

		InputStream bis;
		int readBytes;
		byte[] data = new byte[1024 * 1024];

		for (File f : files) {
			raf.writeUTF(f.getName());
			raf.writeLong(f.length());

			bis = new BufferedInputStream(new FileInputStream(f));
			while ((readBytes = bis.read(data)) != -1)
				raf.write(data, 0, readBytes);
			bis.close();
		}
		raf.close();
	}

	public static void unpack2(String pathSourceFile, String pathDestFolder, String name) throws IOException {
		File sFile = new File(pathSourceFile);
		if (!sFile.exists() || !sFile.isFile())
			return;

		RandomAccessFile raf = new RandomAccessFile(sFile, "rw");
		int n = raf.readInt();
		String fileName;
		long size, pos;

		for (int i = 0; i < n; i++) {
			fileName = raf.readUTF();
			size = raf.readLong();
			pos = raf.getFilePointer();

			if (fileName.equals(name)) {
				OutputStream bos = new BufferedOutputStream(new FileOutputStream(pathDestFolder + "\\unpack-" + name));
				writeFile(raf, bos, size);
				raf.close();
				bos.close();
				return;
			} else
				raf.seek(pos + size);
		}
		raf.close();
	}

	public static void main(String[] args) throws IOException {
		String pathFolder = "path source folder contains multi file";
		String destFile = "path dest file";
		String name = "file name in pack";

		pack(pathFolder, destFile);
		unpack(destFile, pathFolder, name);

//		pack2(pathFolder, destFile);
//		unpack2(destFile, pathFolder, name);
	}

}
