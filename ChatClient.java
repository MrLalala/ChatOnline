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
		// TODO �Զ����ɵĹ��캯�����
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
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				System.exit(0);
			}

		});
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
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
			// TODO �Զ����ɵ� catch ��
			showInf.append("\n\t\t�Է�������\n");	
		}
	}
	
	public static void main(String[] args) {
		new ChatClient("ChatClient");
		try {
			socket = new Socket("127.0.0.1",6666);
			read = new Read(socket, showInf);
			read.start();
		} catch (UnknownHostException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("����ȷ��������ַ");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			showInf.append("\n\t\t�Է�������,5s���˳�\n\n");
			try {
				for(int i = 5;i>0;i--){
					showInf.append("\n\t\t\t"+i+"s\n\n");
					Thread.sleep(1000);
				}
				System.exit(0);
			} catch (InterruptedException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
	}
}