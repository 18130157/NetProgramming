import java.io.File;
import java.io.IOException;

public class Bai9 {

	public static boolean folderCopy(String sFolder, String destFolder, boolean moved) throws IOException {
		File srcFolder = new File(sFolder);
		if (!srcFolder.exists() || !srcFolder.isDirectory())
			return false;
		File dFolder = new File(destFolder);
		if (!dFolder.exists())
			dFolder.mkdirs();
		else if (!dFolder.isDirectory() || checkSubFolder(srcFolder, dFolder))
			return false; // destination folder can't be a subfolder of source folder

		for (File file : srcFolder.listFiles()) {
			if (file.isFile())
				Bai8.fileCopy(file.getPath(), dFolder.getPath() + "\\" + file.getName(), moved);
			else if (file.isDirectory()) {
				File subDir = new File(dFolder.getPath() + "\\" + file.getName());
				subDir.mkdir();
				folderCopy(file.getPath(), subDir.getPath(), moved);
			}
		}
		if (moved)
			Bai1.delete(sFolder);
		return true;
	}

	public static boolean checkSubFolder(File folder, File subFolder) throws IOException {
		if (!folder.exists() || !subFolder.exists() || !folder.isDirectory() || !subFolder.isDirectory())
			return false;
		return subFolder.getCanonicalPath().startsWith(folder.getCanonicalPath());
	}

	public static void main(String[] args) throws IOException {
		String sFolder = "path source folder";
		String destFolder = "path dest folder";
		boolean moved = true;
		long start = System.currentTimeMillis();
		System.out.println(folderCopy(sFolder, destFolder, moved));
		System.out.println(System.currentTimeMillis() - start + "ms");
	}

}
