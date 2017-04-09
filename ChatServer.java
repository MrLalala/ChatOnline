import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {
	boolean started = false;
	ServerSocket ss = null;
	ArrayList<Client> clients = new ArrayList<Client>();

	public void start() {
		try {
			ss = new ServerSocket(6666);
			started = true;
		} catch (BindException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("a client connected!");
				new Thread(c).start();
				for(int i = 0;i<clients.size();i++){
					c.send("\t\t"+clients.get(i).s.getPort()+"\t����Ի�\n\n");
				}
				clients.add(c);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class Client implements Runnable {

		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;

		public Client(Socket s) {
			// TODO �Զ����ɵĹ��캯�����
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}

		public void send(String s) {
			try {
				dos.writeUTF(s);
			} catch (IOException e) {
				// TODO: handle exception
				clients.remove(this);
			}
		}

		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			try {
				while (bConnected) {

					String temp =s.getPort()+":\t"+ dis.readUTF();
					for (int i = 0; i < clients.size(); i++) {
						Client client = clients.get(i);
						client.send(temp);
					}
				}
			} catch (EOFException e) {
				// TODO �Զ����ɵ� catch ��
				System.out.println("�뿪һλ");
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				try{
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) s.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ChatServer().start();
	}
}
