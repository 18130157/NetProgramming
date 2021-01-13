package bai26.server;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class UploadImpl extends UnicastRemoteObject implements IUpload {
	private static final long serialVersionUID = 1L;
	private static long id = 0;
	private static Map<Long, OutputStream> mapOutStream = new HashMap<>();

	protected UploadImpl() throws RemoteException {
		super();
	}

	@Override
	public long sendFilename(String filename) throws RemoteException {
		try {
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(Server.getServer_dir() + "\\" + filename));
			long sid = id++;
			mapOutStream.put(sid, bos);
			return sid;

		} catch (FileNotFoundException e) {
			return -1;
		}
	}

	@Override
	public void writeData(long sessionID, byte[] data) throws RemoteException {
		try {
			OutputStream bos = mapOutStream.get(sessionID);
			if (bos == null)
				return;
			bos.write(data);
			bos.flush();

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void close(long sessionID) throws RemoteException {
		try {
			mapOutStream.remove(sessionID).close();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
