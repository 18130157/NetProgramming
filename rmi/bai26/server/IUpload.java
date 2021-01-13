package bai26.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUpload extends Remote {

	long sendFilename(String filename) throws RemoteException;

	void writeData(long sessionID, byte[] data) throws RemoteException;

	void close(long sessionID) throws RemoteException;
}
