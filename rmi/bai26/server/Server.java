package bai26.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	// giả sử tất cả dữ liệu, file của Server đều nằm trong server_dir
	private static String server_dir = "D:\\TestLTM\\server";

	public static final String FB_NAME = "findByName";
	public static final String FB_AGE = "findByAge";
	public static final String FB_SCORE = "findByScore";
	public static String dbUrl = "jdbc:odbc:students";

	public static String getServer_dir() { return server_dir; }

	public static void setServer_dir(String server_dir) { Server.server_dir = server_dir; }

	public static void main(String[] args) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Registry reg = LocateRegistry.createRegistry(12345);
			IDownload d = new DownloadImpl();
			IUpload u = new UploadImpl();
			ILookup lo = new LookupImpl();

			reg.rebind("download", d);
			reg.rebind("upload", u);
			reg.rebind("lookup", lo);
			System.out.println("Server is running !");

		} catch (RemoteException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
