package de14_2018_2019;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	public static String infoFile = "Data/info.dat";
	public static String delim = "|";
	// REGISTER|Họ_và_tên|Ngày_sinh|Nơi_Cư_Trú
	public static final String REGISTER = "REGISTER";

	// SEND_FOTO|file_name
	// ??? SEND_FOTO|file_path chứ nhỉ ?
	// Nếu chỉ có file_name thì ảnh mà Client muốn upload phải nằm cùng cấp với folder src ?
	public static final String SEND_FOTO = "SEND_FOTO";

	// VIEW_INFO|Mã_Số_Đã_Đăng_Ký
	public static final String VIEW_INFO = "VIEW_INFO";

	// QUIT
	public static final String QUIT = "QUIT";

	public static void main(String[] args) {
		try {
			File dir = new File("Data");
			if (!dir.exists())
				dir.mkdirs();
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(infoFile));
			dos.writeInt(0);
			dos.close();

		
			Registry registry = LocateRegistry.createRegistry(1099);
			IRegister re = new RegisterImpl();
			registry.rebind("register", re);
			System.out.println("Server is running...");
		
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
