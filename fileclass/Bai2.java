import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bai2 {
	private static String fullPath = "";
	private static List<File> list = new ArrayList<File>();;

	public static boolean findFirst(String path, String pattern) throws IOException {
		File file = new File(path);
		if (!file.exists())
			return false;
		/*
		 * Nếu path chỉ đến 1 file và file đó có name khớp pattern => return path chính
		 * file đó
		 */
		if (file.isFile() && file.getName().matches(pattern)) {
			fullPath = file.getCanonicalPath();
			return true;
		}
		/*
		 * Nếu path chỉ đến folder thì tìm trong folder này file/directory có name khớp
		 * pattern
		 */
		if (file.isDirectory()) {
			File[] arr = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					String str = new File(name).getName();
					return str.matches(pattern);
				}
			});
			if (arr.length == 0) // không có file/directory nào khớp pattern => return false
				return false;
			else { // có ít nhất 1 file/directory khớp pattern
					// => lấy phần tử đầu tiên vì tên method là findFirst
				fullPath = arr[0].getCanonicalPath();
				return true;
			}
		}
		return false;
	}

	/*
	 * tìm tất cả file/directory chỉ định bởi path có chứa chuỗi quy định bởi
	 * pattern
	 */
	public static void findAll(String path, String pattern) throws IOException {
		File file = new File(path);
		if (!file.exists())
			return;

		if (file.isFile() && file.getName().matches(pattern))
			if (!isContains(list, file)) {
				list.add(file);
				return;
			}

		if (file.isDirectory()) {
			File[] arr = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					String str = new File(name).getName();
					return str.matches(pattern);
				}
			});
			Collections.addAll(list, arr);

			for (File f : file.listFiles())
				findAll(f.getPath(), pattern);
		}

	}

	public static void main(String[] args) throws IOException {
		String path = "path";
		String pattern = ".*.docx$";
		System.out.println(findFirst(path, pattern));
		System.out.println(fullPath);
		findAll(path, pattern);
		for (File f : list)
			System.out.println(f.getCanonicalPath());

	}

	public static boolean isContains(List<File> list, File file) throws IOException {
		for (File f : list)
			if (f.getCanonicalPath().equals(file.getCanonicalPath()))
				return true;
		return false;
	}

}
