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
	private static volatile long id = 0;
	private static Map<Long, InputStream> map = new HashMap<>();

	protected DownloadImpl() throws RemoteException {
		super();
	}

	@Override
	public long openFile(String filename) throws RemoteException {
		try {
			File sFile = new File(Server.getServer_dir() + "\\" + filename);
			InputStream bis = new BufferedInputStream(new FileInputStream(sFile));
			long sid = id++;
			map.put(sid, bis);
			return sid;

		} catch (FileNotFoundException e) {
			return -1;
		}
	}

	@Override
	public byte[] getData(long sessionID) throws RemoteException {
		InputStream bis = map.get(sessionID);
		if (bis == null)
			return null;

		try {
			int readBytes;
			byte[] data = new byte[1024 * 1024];

			if ((readBytes = bis.read(data)) < 0)
				return null;

			if (readBytes == data.length)
				return data;
			return Arrays.copyOf(data, readBytes);

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void close(long sessionID) throws RemoteException {
		try {
			map.remove(sessionID).close();
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
