package ChatApplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Server extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JTextPane txtContent;
	private JTextField txtMsg;
	private final Action onSendMessage = new SwingAction();
	
	private SocketService socketService;
	
	/**
	 * Create the frame.
	 */
	public Server() {
		setTitle("Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(900, 100, 450, 500);
		
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(176, 224, 230));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		txtContent = new JTextPane();
		txtContent.setBounds(21, 42, 389, 356);
		txtContent.setEditable(false);
		txtContent.setBackground(new Color(248, 248, 255));
		contentPane.add(txtContent);
		
		txtMsg = new JTextField();
		txtMsg.setBounds(21, 409, 298,32);
		contentPane.add(txtMsg);
		
		JLabel txtServerStatus = new JLabel("Server status:");
		txtServerStatus.setBounds(21, 11, 242, 20);
		txtServerStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(txtServerStatus);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(335, 409, 75, 32);
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnSend.setBackground(new Color(255,255,255));
		btnSend.setAction(onSendMessage);
		contentPane.add(btnSend);
		
		try {
			ServerSocket serverSocket = new ServerSocket(8080);
			
			Thread th = new Thread() {
				
				@Override
				public void run() {
					try {
						txtServerStatus.setText("Server listening on port 8080");
						Socket socket = serverSocket.accept();
						
						//Socket service
						socketService = new SocketService(socket, txtContent);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			th.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private class SwingAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;

		public SwingAction() {
			putValue(NAME, "Send");
			putValue(SHORT_DESCRIPTION, "Click to send");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//sendMessage
			if (txtMsg.getText().trim().length() == 0) {
				return;
			}
			
			String msg = "[Server]: " + txtMsg.getText().trim();
			txtMsg.setText("");
			
			socketService.send(msg);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater( new Runnable() {
			
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} );
	}
}