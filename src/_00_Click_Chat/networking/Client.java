package _00_Click_Chat.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {
	private String ip;
	private int port;

	Socket connection;

	ObjectOutputStream os;
	ObjectInputStream is;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start(){
		try {

			connection = new Socket(ip, port);

			os = new ObjectOutputStream(connection.getOutputStream());
			is = new ObjectInputStream(connection.getInputStream());

			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while (connection.isConnected()) {
			try {
				JOptionPane.showMessageDialog(null, is.readObject(), "CLIENT - Message from Server", JOptionPane.INFORMATION_MESSAGE);
				System.out.println(is.readObject());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Connection Lost", "CLIENT", JOptionPane.INFORMATION_MESSAGE);
				try {
					connection.close();
				} catch (IOException ioe) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendClick() {
		try {
			if (os != null) {
				os.writeObject("CLICK SENT FROM CLIENT");
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObjectInputStream getOIS() {return is;}
	public ObjectOutputStream getOOS() {return os;}
}
