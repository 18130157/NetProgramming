package bai26.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.StringTokenizer;

import bai26.server.IDownload;
import bai26.server.ILookup;
import bai26.server.IUpload;

public class Client {
	// giả sử tất cả dữ liệu, file của Client đều nằm trong client_dir
	private static String client_dir = "D:\\TestLTM\\client";
	private Registry reg;
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
		System.out.println("All service names:\tdownload");
		System.out.println("\t\t\tupload");
		System.out.println("\t\t\tlookup");

		userIn = new BufferedReader(new InputStreamReader(System.in));
		String str;
		Remote remote;
		try {
			while (true) {

				str = userIn.readLine().trim().toLowerCase();
				if (str.equals("exit"))
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

				if (str.equals("upload")) {
					IUpload up = (IUpload) remote;
					upload(up);
				}

				if (str.equals("lookup")) {
					ILookup lo = (ILookup) remote;
					lookup(lo);
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

				long ID = down.openFile(tokenizer.nextToken());
				if (ID == -1) {
					System.out.println("Source file not found !");
					continue;
				}

				File dFile = new File(client_dir + "\\" + tokenizer.nextToken());
				OutputStream bos = new BufferedOutputStream(new FileOutputStream(dFile));
				byte[] data;

				while ((data = down.getData(ID)) != null)
					bos.write(data);
				bos.close();

				System.out.println("Download " + dFile.getName() + " done !");

				down.close(ID);
			}

			System.out.println("Please enter service name !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void upload(IUpload up) {
		System.out.println("Welcome upload service");
		System.out.println("Command: source_file_name [ dest_file_name ]");
		String command;
		StringTokenizer tokenizer;

		try {
			while (true) {
				command = userIn.readLine().trim();
				if (command.equalsIgnoreCase("Exit"))
					break;

				tokenizer = new StringTokenizer(command);
				int n = tokenizer.countTokens();
				if (n < 1 || n > 2) {
					System.err.println("Invalid command !");
					continue;
				}

				File sFile = new File(client_dir + "\\" + tokenizer.nextToken());
				if (!sFile.exists() || !sFile.isFile()) {
					System.out.println("Source file not found !");
					continue;
				}

				InputStream bis = new BufferedInputStream(new FileInputStream(sFile));

				String dFile = (n == 2) ? tokenizer.nextToken() : sFile.getName();
				long ID = up.sendFilename(dFile);

				int readBytes;
				byte[] data = new byte[1024 * 1024];

				while ((readBytes = bis.read(data)) != -1)
					if (readBytes == data.length)
						up.writeData(ID, data);
					else
						up.writeData(ID, Arrays.copyOf(data, readBytes));

				bis.close();
				up.close(ID);

				System.out.println("Upload " + dFile + " done !");

			}

			System.out.println("Please enter service name !");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void lookup(ILookup lo) {
		System.out.println("Welcome look up student information service");
		System.out.println("Commands:\t findByName name");
		System.out.println("\t\t findByAge age");
		System.out.println("\t\t findByScore score\n");

		String command, response, ID;
		try {
			while (true) {
				command = userIn.readLine().trim();
				if (command.equalsIgnoreCase("Exit"))
					break;

				response = lo.sendCommand(command);
				if (response.startsWith("Invalid")) {
					System.err.println(response);
					continue;
				}

				int nFound = lo.getFoundNumber(ID = response);
				if (nFound == 0)
					System.out.println("Student not found");
				else if (nFound == 1) {
					System.out.println("1 student was found");
					System.out.println(lo.getLine(ID));
				} else {
					System.out.println(nFound + " students were found");
					for (int i = 0; i < nFound; i++)
						System.out.println(lo.getLine(ID));
				}
				
				lo.close(ID);
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
