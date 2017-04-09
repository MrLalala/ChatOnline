import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;


public class ChatClient extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static TextField sendText = new TextField("");
	static TextArea showInf = new TextArea("");
	Button send = new Button("Send");
	Panel buttom = new Panel(new BorderLayout());
	static Read read;
	static Socket socket;
	public ChatClient(String name) {
		// TODO 自动生成的构造函数存根
		super(name);
		setBounds(600, 200, 400, 400);
		add(showInf, BorderLayout.CENTER);
		buttom.add(sendText, BorderLayout.CENTER);
		buttom.add(send, BorderLayout.EAST);
		add(buttom, BorderLayout.SOUTH);
		showInf.setEditable(false);
		sendText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
//System.out.println(sendText.getText());
					sendMessage(sendText.getText());
					sendText.setText("");
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				System.exit(0);
			}

		});
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
//System.out.println(sendText.getText());
				sendMessage(sendText.getText());
				sendText.setText("");
				sendText.requestFocus();
			}
		});
		setVisible(true);
		sendText.requestFocus();
	}
	
	public static void sendMessage(String s){
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			if(s != null){
				showInf.append(new Date().toString()+"\n");
				showInf.append(s+"\n");
				dos.writeUTF(s);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			showInf.append("\n\t\t对方已离线\n");	
		}
	}
	
	public static void main(String[] args) {
		new ChatClient("ChatClient");
		try {
			socket = new Socket("127.0.0.1",6666);
			read = new Read(socket, showInf);
			read.start();
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			System.out.println("不正确的主机地址");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			showInf.append("\n\t\t对方不在线,5s后退出\n\n");
			try {
				for(int i = 5;i>0;i--){
					showInf.append("\n\t\t\t"+i+"s\n\n");
					Thread.sleep(1000);
				}
				System.exit(0);
			} catch (InterruptedException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}
}