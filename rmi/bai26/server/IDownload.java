package bai26.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDownload extends Remote {
	
	int openFile(String filename) throws RemoteException;

	byte[] getData(int key) throws RemoteException;

	void close(int key) throws RemoteException;
}
