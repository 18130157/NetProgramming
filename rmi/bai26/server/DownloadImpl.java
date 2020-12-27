package bai26.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DownloadImpl extends UnicastRemoteObject implements IDownload {
	private static final long serialVersionUID = 1L;
	private static Map<Integer, InputStream> map = new HashMap<>();

	protected DownloadImpl() throws RemoteException {
		super();
	}

	@Override
	public int openFile(String filename) throws RemoteException {
		try {
			File sFile = new File(Server.getServer_dir() + "\\" + filename);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sFile));

			int key = map.size();
			map.put(key, bis);
			return key;

		} catch (FileNotFoundException e) {
			return -1;
		}
	}

	@Override
	public byte[] getData(int key) throws RemoteException {
		InputStream bis = map.get(key);
		if (bis == null)
			return null;

		try {
			int readBytes;
			byte[] data = new byte[1024 * 1024];

			if ((readBytes = bis.read(data)) != -1) {
				if (readBytes == data.length)
					return data;
				return Arrays.copyOf(data, readBytes);
			}

			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void close(int key) throws RemoteException {
		try {
			map.remove(key).close();

		} catch (NullPointerException e) {
			return;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
