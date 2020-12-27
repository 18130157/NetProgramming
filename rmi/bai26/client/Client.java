package bai26.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.StringTokenizer;

import bai26.server.IDownload;

public class Client {
	// giả sử tất cả dữ liệu, file của Client đều nằm trong client_dir
	private static String client_dir = "D:\\TestLTM\\client";
	private Registry reg;
	private OutputStream bos;
	private BufferedReader userIn;

	public Client() {
		try {
			reg = LocateRegistry.getRegistry("127.0.0.1", 12345);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Please enter service name !");
		System.out.println("All service names:\tdownload (ready)");
		System.out.println("\t\t\tupload (coming soon)");
		System.out.println("\t\t\tlookup (coming soon)");

		userIn = new BufferedReader(new InputStreamReader(System.in));
		String str;
		Remote remote;
		try {
			while (true) {

				str = userIn.readLine().trim();
				if (str.equalsIgnoreCase("Exit"))
					break;

				try {
					remote = reg.lookup(str);
				} catch (NotBoundException e) {
					System.err.println("Service not found !");
					continue;
				}

				if (str.equals("download")) {
					IDownload down = (IDownload) remote;
					download(down);
				}

			}

			userIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void download(IDownload down) {
		System.out.println("Welcome download service");
		System.out.println("Command: source_file_name dest_file_name");
		String command;
		StringTokenizer tokenizer;

		try {
			while (true) {
				command = userIn.readLine().trim();
				if (command.equalsIgnoreCase("Exit"))
					break;

				tokenizer = new StringTokenizer(command);
				if (tokenizer.countTokens() != 2) {
					System.err.println("Invalid command !");
					continue;
				}

				int key = down.openFile(tokenizer.nextToken());
				if (key == -1) {
					System.out.println("Source file not found !");
					continue;
				}

				File dFile = new File(client_dir + "\\" + tokenizer.nextToken());
				bos = new BufferedOutputStream(new FileOutputStream(dFile));
				byte[] data;

				while ((data = down.getData(key)) != null)
					bos.write(data);
				bos.close();

				System.out.println("Download " + dFile.getName() + " done !");

				down.close(key);
			}
			
			System.out.println("Please enter service name !");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Client().run();
	}

}
