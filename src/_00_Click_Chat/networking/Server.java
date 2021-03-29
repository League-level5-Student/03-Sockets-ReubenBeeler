package _00_Click_Chat.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Server {
	private int port;

	private ServerSocket server;
	private Socket connection;

	ObjectOutputStream os;
	ObjectInputStream is;

	public Server(int port) {
		this.port = port;
	}

	public void start() throws IOException, ClassNotFoundException {
		server = new ServerSocket(port, 100);

		connection = server.accept();

		os = new ObjectOutputStream(connection.getOutputStream());
		is = new ObjectInputStream(connection.getInputStream());

		os.flush();

		while (connection.isConnected()) {
			try {
				JOptionPane.showMessageDialog(null, is.readObject(), "SERVER - Message from Client", JOptionPane.INFORMATION_MESSAGE);
				System.out.println(is.readObject());
			} catch(EOFException e) {
				/*
				JOptionPane.showMessageDialog(null, "Connection Lost", "SERVER", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
				*/
			}
		}
	}
	
	public String getIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "ERROR!!!!!";
		}
	}

	public int getPort() {
		return port;
	}

	public void sendClick() {
		try {
			if (os != null) {
				os.writeObject("CLICK SENT FROM SERVER");
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException {
		server.close();
	}

	public ObjectInputStream getOIS() {return is;}
	public ObjectOutputStream getOOS() {return os;}
}
