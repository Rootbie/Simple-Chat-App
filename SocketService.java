package ChatApplication;

import java.io.*;
import java.net.Socket;

import javax.swing.JTextPane;

public class SocketService {

	private Socket socket;
	private JTextPane txtContent;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public SocketService(Socket socket, JTextPane txtContent) throws IOException {
		this.socket = socket;
		this.txtContent = txtContent;

		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWriter = new PrintWriter(socket.getOutputStream());
		receive();
	}

	private void receive() {
		Runnable runnable = () -> {

			while (true) {
				try {
					String line = bufferedReader.readLine();						
					txtContent.setText(txtContent.getText() + "\n" + line);
			
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		Thread th = new Thread(runnable);
		th.start();
	}

	public void send(String msg) {
		// display
		txtContent.setText(txtContent.getText() + "\n" + msg);

		// sent
		printWriter.println(msg);
		printWriter.flush();
	}

	public void close() {
		try {
			socket.close();

			bufferedReader.close();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
