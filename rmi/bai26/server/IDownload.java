package bai26.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDownload extends Remote {
	
	long openFile(String filename) throws RemoteException;

	byte[] getData(long sessionID) throws RemoteException;

	void close(long sessionID) throws RemoteException;
}
