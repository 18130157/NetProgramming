package de14_2018_2019;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRegister extends Remote {

	String getBanner() throws RemoteException;

	String sendCommand(String command) throws RemoteException;

	String sendFoto(int sessionID, String command) throws RemoteException;

	void write(int sessionID, byte[] data) throws RemoteException;

	void close(int sessionID) throws RemoteException;
	
	int getSessionID(String ID) throws RemoteException;
	
}
