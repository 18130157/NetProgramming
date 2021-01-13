package bai26.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILookup extends Remote {
	
	String sendCommand(String command) throws RemoteException;
	
	int getFoundNumber(String sessionID) throws RemoteException;

	String getLine(String sessionID) throws RemoteException;

	void close(String sessionID) throws RemoteException;
}
