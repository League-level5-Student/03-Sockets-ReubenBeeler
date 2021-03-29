package _02_Chat_Application;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import _00_Click_Chat.networking.Client;
import _00_Click_Chat.networking.Server;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

@SuppressWarnings("serial")
public class ChatApp extends JFrame {

	JLabel instructions = new JLabel();
	
	Server server;
	Client client;
	final Type type;
	
	enum Type {
		SERVER, CLIENT
	}
	
	public static void main(String[] args) {
		new ChatApp();
	}
	
	private void setup() {
		setSize(400, 300);
		
		JPanel panel = new JPanel();
		JTextField textField = new JTextField(20);
		JButton button = new JButton("Send");
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					ObjectOutputStream oos = null;
					switch(type) {
					case SERVER:
						oos = server.getOOS();
						break;
					case CLIENT:
						oos = client.getOOS();
						break;
					}
					if (oos != null) {
						oos.writeObject(textField.getText());
						textField.setText("");
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		};
		
		add(panel);
		Container c = new Container();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		Container flow = new Container();
		flow.setLayout(new FlowLayout());
		flow.add(instructions);
		flow.add(textField);
		c.add(flow);
		
		panel.add(c);
		panel.add(button);
		
		button.addActionListener(al);
		textField.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	ChatApp() {
		int response = JOptionPane.showConfirmDialog(null, "Would you like to host a connection?", "ChatApp Connect", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION){
			type = Type.SERVER;
			server = new Server(8080);
			setTitle("SERVER");
			instructions.setText("Message to Client:");
			setup();
			JOptionPane.showMessageDialog(null, "Server started at: " + server.getIPAddress() + "\nPort: " + server.getPort());
			try {
				server.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			type = Type.CLIENT;
			String ipStr = JOptionPane.showInputDialog("Enter the IP Address");
			if (ipStr == null) return;
			else if (ipStr.equals("")) {
				System.err.println("IP Address cannot be empty.");
				return;
			}
			String prtStr = JOptionPane.showInputDialog("Enter the port number");
			if (prtStr == null) return;
			else if (prtStr.equals("")) {
				System.err.println("Invalid Port");
				return;
			}
			int port = Integer.parseInt(prtStr);
			client = new Client(ipStr, port);
			setTitle("CLIENT");
			instructions.setText("Message to Server:");
			setup();
			client.start();
		}
	}

	@Override
	public void dispose() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.dispose();
	}
}