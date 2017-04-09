import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ChatServer extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static TextField sendText = new TextField("");
	static TextArea showInf = new TextArea("");
	Button send = new Button("Send");
	Panel buttom = new Panel(new BorderLayout());
	static Read read;
	Socket sockets;
	static boolean flag = true;

	public ChatServer(String name) {
		// TODO 自动生成的构造函数存根
		super(name);
		setBounds(200, 200, 400, 400);
		add(showInf, BorderLayout.CENTER);
		buttom.add(sendText, BorderLayout.CENTER);
		buttom.add(send, BorderLayout.EAST);
		add(buttom, BorderLayout.SOUTH);
		showInf.setEditable(false);
		sendText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 发送信息
					try {
						sendMessage(sockets, sendText.getText());
					} catch (NullPointerException e2) {
						// TODO 自动生成的 catch 块
						showInf.append("\n\t\t无在线人员\n\n");
					}
					sendText.setText("");
				}
			}
		});

		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				try {
					sendMessage(sockets, sendText.getText());
				} catch (NullPointerException e2) {
					// TODO 自动生成的 catch 块
					showInf.append("\n\t\t无在线人员\n\n");
				}
				sendText.setText("");
				sendText.requestFocus();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					try {
						sockets.close();
					} catch (NullPointerException e2) {
						// TODO 自动生成的 catch 块
						System.exit(0);
					}
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块

				}
				System.exit(0);
			}
		});
		setVisible(true);
		sendText.requestFocus();
	}
	
	public static void sendMessage(Socket sockets, String s) {
		try {
			DataOutputStream dos = new DataOutputStream(sockets.getOutputStream());
			if (s != null) {
				showInf.append(new Date().toString() + "\n");
				showInf.append(s + "\n");
				dos.writeUTF(s);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			showInf.append("\n\t\t对方已离线\n\n");
		}
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ChatServer cs = new ChatServer("Server");
		ServerSocket ss;
		try {
			ss = new ServerSocket(6666);
			while (true) {
				cs.sockets = ss.accept();
				showInf.append("\t\t" + cs.sockets.getPort() + "已上线\n\n");
				read = new Read(cs.sockets, showInf);
				read.start();
			}

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println(e.getMessage());
		}

	}

}
