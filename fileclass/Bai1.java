import java.io.File;

public class Bai1 {
	private static boolean re = true;

	public static boolean delete(String path) {
		File f = new File(path);
		if (!f.exists())
			return false;

		File[] arr = f.listFiles();
		// nếu f là file mà gọi listFiles() thì sẽ trả về null
		// => nếu arr != null và arr.length > 0 thì f là directory không rỗng
		if (arr != null && arr.length > 0)
			for (File file : arr)
				delete(file.getPath());

		re = re && f.delete();
		return re;
	}

	// không xoá thư mục, giữ nguyên cấu trúc thư mục
	public static boolean deleteExtend(String path) {
		File f = new File(path);
		if (!f.exists())
			return false;

		File[] arr = f.listFiles();
		if (arr != null && arr.length > 0)
			for (File file : arr)
				deleteExtend(file.getPath());

		if (f.isFile())
			re = re && f.delete();
		return re;
	}

	public static void main(String[] args) {
		String path = "path";
		System.out.println(delete(path));
		re = true;
//		System.out.println(deleteExtend(path));
	}

}
