import java.io.File;
import java.io.IOException;

public class Bai3 {
	private static StringBuilder builder = new StringBuilder();

	public static void dirTree(String path) {
		File file = new File(path);
		if (!file.exists() || file.isFile())
			return;

		int indent = 0;
		builder.append(file.getName());
		builder.append("\n");
		for (File f : file.listFiles())
			makeTree(f, indent + 1, builder);

		System.out.println(builder);
	}

	public static void makeTree(File file, int indent, StringBuilder stb) {
		stb.append(getIndentString(indent));
		stb.append("+--");
		stb.append(file.getName());
		stb.append("\n");

		if (file.isDirectory())
			for (File f : file.listFiles())
				makeTree(f, indent + 1, stb);
	}

	public static String getIndentString(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++)
			sb.append("|    ");
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		String path = "path";
		dirTree(path);
	}
}
