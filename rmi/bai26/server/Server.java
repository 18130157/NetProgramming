package bai26.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	// giả sử tất cả dữ liệu, file của Server đều nằm trong server_dir
	private static String server_dir = "D:\\TestLTM\\server";
	
	public static String getServer_dir() { return server_dir; }

	public static void setServer_dir(String server_dir) { Server.server_dir = server_dir; }

	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.createRegistry(12345);
			IDownload d = new DownloadImpl();
			reg.rebind("download", d);
			System.out.println("Server is running !");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
