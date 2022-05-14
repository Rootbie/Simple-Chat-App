package ChatApplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextPane txtContent;
	private JTextField txtMsg;
	
	private final Action onSendMessage = new SwingAction();
	
	private SocketService socketService;
	
	/**
	 * Create the frame.
	 */
	public Client() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		
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
		
		JLabel txtServerStatus = new JLabel("Client status:");
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
			Socket clientSocket = new Socket("127.0.0.1", 8080);
			txtServerStatus.setText("Connected to 127.0.0.1 | Port 8080");
			
			//Socket service
			socketService = new SocketService(clientSocket, txtContent);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class SwingAction extends AbstractAction {
	
		private static final long serialVersionUID = 1L;

		public SwingAction() {
			putValue(NAME, "Send");
			putValue(SHORT_DESCRIPTION, "Click to send");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Send Message
			if (txtMsg.getText().trim().length() == 0) {
				return;
			}
			
			String msg = "[Client]: " + txtMsg.getText().trim() ;
			txtMsg.setText("");
			
			socketService.send(msg);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
	
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
