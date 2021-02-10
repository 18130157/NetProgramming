package bai26.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import base.Student;

public class LookupImpl extends UnicastRemoteObject implements ILookup {
	private static final long serialVersionUID = 1L;
	private static volatile long id = 0;
	private static Map<String, Connection> mapConn = new HashMap<>();
	private static Map<String, PreparedStatement> mapPrepStmt = new HashMap<>();
	private static Map<String, ResultSet> mapRes = new HashMap<>();
	private static Map<String, Integer> mapFound = new HashMap<>();

	protected LookupImpl() throws RemoteException {
		super();
	}

	@Override
	public String sendCommand(String command) throws RemoteException {
		StringTokenizer tokenizer = new StringTokenizer(command);
		if (tokenizer.countTokens() < 2)
			return "Invalid command !";

		String action = tokenizer.nextToken();
		if (action.equalsIgnoreCase(Server.FB_NAME))
			return findByName(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_AGE))
			return findByAge(tokenizer);
		if (action.equalsIgnoreCase(Server.FB_SCORE))
			return findByScore(tokenizer);

		return "Invalid command !";

	}

	public String findByName(StringTokenizer tokenizer) throws RemoteException {
		try {
			String value = tokenizer.nextToken();
			while (tokenizer.hasMoreTokens())
				value += " " + tokenizer.nextToken();

			Connection conn = getConnection();
			PreparedStatement prepStmt = prepareStatement(conn, "SELECT * FROM Student WHERE Name LIKE ?");
			prepStmt.setString(1, '%' + value + '%');
			ResultSet res = prepStmt.executeQuery();

			String sid = String.valueOf(id++);
			put(sid, conn, prepStmt, res);
			return sid;

		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}

	}

	public String findByAge(StringTokenizer tokenizer) throws RemoteException {
		int age;
		try {
			age = Integer.parseInt(tokenizer.nextToken());
		} catch (NumberFormatException e) {
			return "Invalid age !";
		}

		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = prepareStatement(conn, "SELECT * FROM Student WHERE Age = ?");
			prepStmt.setInt(1, age);
			ResultSet res;
			res = prepStmt.executeQuery();

			String sid = String.valueOf(id++);
			put(sid, conn, prepStmt, res);
			return sid;

		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public String findByScore(StringTokenizer tokenizer) throws RemoteException {
		double score;
		try {
			score = Double.parseDouble(tokenizer.nextToken());
		} catch (NumberFormatException e) {
			return "Invalid score !";
		}

		try {
			Connection conn = getConnection();
			PreparedStatement prepStmt = prepareStatement(conn, "SELECT * FROM Student WHERE Score = ?");
			prepStmt.setDouble(1, score);
			ResultSet res = prepStmt.executeQuery();

			String sid = String.valueOf(id++);
			put(sid, conn, prepStmt, res);
			return sid;

		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public Connection getConnection() throws RemoteException {
		try {
			return DriverManager.getConnection(Server.dbUrl, "", "");
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public PreparedStatement prepareStatement(Connection conn, String sql) throws RemoteException {
		try {
			return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public void put(String sid, Connection conn, PreparedStatement prepStmt, ResultSet res) throws RemoteException {
		try {
			mapConn.put(sid, conn);
			mapPrepStmt.put(sid, prepStmt);
			mapRes.put(sid, res);
			res.last();
			mapFound.put(sid, res.getRow());
			res.beforeFirst();
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public int getFoundNumber(String sessionID) throws RemoteException {
		return mapFound.get(sessionID);
	}

	@Override
	public String getLine(String sessionID) throws RemoteException {
		ResultSet res = mapRes.get(sessionID);
		if (res == null)
			return null;
		try {
			if (res.next())
				return new Student(res.getInt("ID"), res.getString("Name"),
						res.getInt("Age"), res.getDouble("Score")).toString();
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
		return null;
	}

	@Override
	public void close(String sessionID) throws RemoteException {
		try {
			mapFound.remove(sessionID);
			mapRes.remove(sessionID).close();
			mapPrepStmt.remove(sessionID).close();
			mapConn.remove(sessionID).close();

		} catch (SQLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
