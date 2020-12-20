package bai20;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class Server {

	public static void main(String[] args){
		try {
			ServerSocket server = new ServerSocket(7);
			System.out.println("Waiting for client...");
			
			int count = 0;
			Socket socket;
			Thread thread;
			
			while(true) {
				socket = server.accept();
				System.out.println(String.format("Client %d - %s connected", ++count, socket.getInetAddress()));
				thread = new Thread(new ServerProcess(socket));
				thread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
