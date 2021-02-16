package de14_2018_2019;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class RegisterImpl extends UnicastRemoteObject implements IRegister {
	private static final long serialVersionUID = 1L;

	private static int sid = 1;
	private static Map<Integer, String> mapID = new HashMap<>();
	private static Map<Integer, OutputStream> mapOutStream = new HashMap<>();
	
	private static RandomAccessFile raf;
	static {
		try {
			raf = new RandomAccessFile(Server.infoFile, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected RegisterImpl() throws RemoteException {
		super();
	}

	@Override
	public String getBanner() throws RemoteException {
		return "welcome to Registration...";
	}

	@Override
	public String sendCommand(String command) throws RemoteException {
		if (command.equalsIgnoreCase(Server.QUIT))
			return "Bye";

		StringTokenizer tokenizer = new StringTokenizer(command, Server.delim);
		int count = tokenizer.countTokens();
		String action = tokenizer.nextToken().trim();

		if (action.equalsIgnoreCase(Server.VIEW_INFO)) {
			if (count != 2)
				return "Invalid VIEW_INFO command !";
			return lookup(tokenizer.nextToken().trim());
		} else

		if (action.equalsIgnoreCase(Server.REGISTER)) {
			if (count != 4)
				return "Invalid REGISTER command !";

			final Candidate c = new Candidate();
			if (!c.load(tokenizer))
				return "Invalid date of birth !";

			int sessionID = sid++;
			String id = String.format("MS%03d", sessionID);

			c.setId(id);
			mapID.put(sessionID, id);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						save(c);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}).start();

			return id;

		} else
			return "Invalid command !";
	}

	public synchronized void save(Candidate c) throws RemoteException {
		try {
			long size = raf.length();
			raf.seek(0);
			int count = raf.readInt();
			raf.seek(0);
			raf.writeInt(++count);
			raf.seek(size);
			c.save(raf);

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public String lookup(String id) throws RemoteException {
		try {
			int num = Integer.parseInt(id.substring(2));

			DataInputStream dis = new DataInputStream(new FileInputStream(Server.infoFile));
			int count = dis.readInt();
			if (num > count) {
				dis.close();
				return "Candidate not found !";
			}

			String idr;
			for (int i = 0; i < count; i++) {
				if ((idr = dis.readUTF()).equalsIgnoreCase(id)) {
					Candidate c = new Candidate();
					c.setId(idr);
					c.load(dis);
					dis.close();
					return "Candidate found\n" + c.toString();
				}
				dis.readUTF();
				dis.readUTF();
				dis.readUTF();
			}

			dis.close();
			return "Candidate not found !";

		} catch (IndexOutOfBoundsException | NumberFormatException e) {
			return "Candidate not found !";
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String sendFoto(int sessionID, String command) throws RemoteException {
		if (command.equalsIgnoreCase(Server.QUIT)) {
			if (mapID.containsKey(sessionID))
				mapID.remove(sessionID);
			return "Bye";
		}

		StringTokenizer tokenizer = new StringTokenizer(command, Server.delim);
		if (tokenizer.countTokens() != 2 || !tokenizer.nextToken().trim().equalsIgnoreCase(Server.SEND_FOTO))
			return "Invalid SEND_FOTO command !";

		String id, fotoName;
		if ((id = mapID.get(sessionID)) != null && (fotoName = getFotoName(id, tokenizer.nextToken().trim())) != null) {
			try {
				OutputStream bos = new BufferedOutputStream(new FileOutputStream("Data/" + fotoName));
				mapOutStream.put(sessionID, bos);
				return "OK";

			} catch (FileNotFoundException e) {
				throw new RemoteException(e.getMessage());
			}

		} else
			return "Invalid foto filename !";
	}

	public String getFotoName(String id, String path) throws RemoteException {
		int index = path.lastIndexOf('.');
		if (index < 5 || !id.equalsIgnoreCase(path.substring(path.lastIndexOf('\\') + 1, index)))
			return null;
		return id + path.substring(index).toLowerCase();
	}

	@Override
	public void write(int sessionID, byte[] data) throws RemoteException {
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
	public void close(int sessionID) throws RemoteException {
		try {
			mapOutStream.remove(sessionID).close();
			mapID.remove(sessionID);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int getSessionID(String ID) throws RemoteException {
		for (Entry<Integer, String> e : mapID.entrySet())
			if (e.getValue().equals(ID))
				return e.getKey();
		return 0;
	}

}
