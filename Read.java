import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

class Read extends Thread {
	private Socket socket;
	private DataInputStream dis;
	private TextArea showInf;
	boolean flag = true;
	public Read(Socket s, TextArea t) {
		// TODO 自动生成的构造函数存根
		this.socket = s;
		this.showInf = t;
	}

	public void run() {
		try {
			while (flag) {
				dis = new DataInputStream(socket.getInputStream());
				String temp = dis.readUTF();
				if (temp != null) {
					showInf.append(new Date().toString()+"\n");
					showInf.append(temp+"\n");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			showInf.append("\n\n\t\t对方已离线\n\n");
		} finally {
			try {
				dis.close();
				socket.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}

