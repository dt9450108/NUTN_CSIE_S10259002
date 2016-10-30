import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

public class MServer {
	public static final int PACKET_SIZE = 1024 * 10;
	public static final String EOC = "bye";
	public static String CHARSET;

	public static enum MSG_TYPE {
		REQUEST, COMPLISH, ERROR, STATUS;
	}

	private final AudioVideoFilter AUDIO_VIDEO_FILTER = new AudioVideoFilter();
	private final String HOST = "127.0.0.1";
	private final int PORT = 3113;

	private DatagramSocket ServerSocket;
	private DatagramPacket ClientRequest;
	private boolean Running = true;
	private Vector<UdpFile> List;

	public MServer() {
		try {
			this.ServerSocket = new DatagramSocket(this.PORT);
			this.ServerSocket.setSoTimeout(0);
			this.List = new Vector<UdpFile>();
			if (File.separatorChar == '/')
				this.CHARSET = "UTF-8";
			else
				this.CHARSET = "BIG5";
		} catch (IOException e) {
			this.ServerSocket = null;
			System.out.println("MServer SocketException ERROR");
			e.printStackTrace();
		}
	}

	public void running() {
		try {
			while (this.Running) {
				sendMsg("等待客戶端連接", MSG_TYPE.STATUS);
				receive();
				switch (getCommand()) {
					case 1:
						new Sender(this.ClientRequest.getAddress(), this.ClientRequest.getPort(), 0, null).start();
						break;
					case 2: {
						receive();
						String name = new String(this.ClientRequest.getData(), 0, this.ClientRequest.getLength(), MServer.CHARSET);
						new Sender(this.ClientRequest.getAddress(), this.ClientRequest.getPort(), 1, name).start();
						break;
					}
					default:
						System.out.println("Not handle");
						sendMsg("無此功能", MSG_TYPE.ERROR);
						break;
				}
			}
			this.ServerSocket.close();
		} catch (IOException e) {
			System.out.println("MServer running ERROR");
			e.printStackTrace();
		}
	}

	public void setList(Vector<String> lists) {
		List = getFiles(lists);
		sendMsg("媒體清單共有 " + List.size() + " 個影音檔案", MSG_TYPE.COMPLISH);
	}

	public void receive() throws IOException {
		this.ClientRequest = new DatagramPacket(new byte[MServer.PACKET_SIZE], MServer.PACKET_SIZE);
		this.ServerSocket.receive(this.ClientRequest);
	}

	public void sendMsg(String msg, MSG_TYPE type) {
		String t;
		Color c;
		switch (type) {
			case REQUEST:
				t = "請求: ";
				c = new Color(0, 0, 204);
				break;
			case COMPLISH:
				t = "完成: ";
				c = new Color(50, 127, 54);
				break;
			case ERROR:
				t = "錯誤: ";
				c = Color.red;
				break;
			case STATUS:
				t = "狀態: ";
				c = Color.black;
				break;
			default:
				t = "Unknown: ";
				c = Color.black;
		}
		MainUI.appendResponse(t + msg, c);
	}

	private int getCommand() throws IOException {
		String str = new String(this.ClientRequest.getData(), 0, this.ClientRequest.getLength(), CHARSET);

		if (str.equalsIgnoreCase("list"))
			return 1;
		else if (str.equalsIgnoreCase("play"))
			return 2;
		return -1;
	}

	private Vector<UdpFile> getFiles(Vector<String> items) {
		Vector<UdpFile> t = new Vector<UdpFile>();

		for (int i = 0; i < items.size(); i++) {
			File f = new File(items.get(i));
			if (f.isDirectory()) {
				File[] fl = f.listFiles(this.AUDIO_VIDEO_FILTER);
				for (File fn : fl)
					t.addElement(new UdpFile(fn.getParent(), fn.getName(), t.size() + 1));
			} else {
				if (this.AUDIO_VIDEO_FILTER.accept(f))
					t.addElement(new UdpFile(f.getParent(), f.getName(), t.size() + 1));
			}
		}
		return t;
	}

	private class Sender extends Thread {
		private DatagramSocket tss;
		private InetAddress cip;
		private int cport;
		private DatagramPacket crequest;
		private byte[] buffer;
		private final int delay = 5; // 5 millisecond
		private int type; // -1 represent send list
		private String target;

		public Sender(InetAddress cip, int cport, int type, String target) throws SocketException {
			super("Sender for " + cip.getHostAddress() + ":" + cport);
			this.tss = new DatagramSocket(0);
			this.cip = cip;
			this.cport = cport;
			this.buffer = new byte[MServer.PACKET_SIZE];
			this.crequest = new DatagramPacket(this.buffer, MServer.PACKET_SIZE);
			this.type = type;
			this.target = target;
		}

		@Override
		public void run() {
			//connect
			try {
				if (type == 0) {
					// send length of byte of the List
					sendMsg("    " + this.cip.getHostAddress() + ":" + this.cport + " 請求伺服器媒體清單", MSG_TYPE.REQUEST);
					byte[] data = objectToBytes(toStringVector(List));
					String listSize = String.format("%d", data.length);
					DatagramPacket response = new DatagramPacket(listSize.getBytes(), listSize.getBytes().length, cip, cport);
					this.tss.send(response);
					sleep(5 * ((int) Math.log10(data.length)));

					// send the List
					response = new DatagramPacket(data, data.length, cip, cport);
					this.tss.send(response);
					sleep(100);
					sendMsg("    傳送媒體清單完成 " + this.cip.getHostAddress() + ":" + this.cport, MSG_TYPE.COMPLISH);
				} else {
					// send the file .mp3 or .wmv
					sendMsg("    " + this.cip.getHostAddress() + ":" + this.cport + " 請求伺服器媒體檔案：" + this.target, MSG_TYPE.REQUEST);
					int fileIndex = find(this.target);
					if (fileIndex >= 0) {
						// send found to client
						DatagramPacket response = new DatagramPacket("find".getBytes(), "find".getBytes().length, this.cip, this.cport);
						this.tss.send(response);
						sendMsg("    " + this.cip.getHostAddress() + ":" + this.cport + " 開始接收媒體檔案：" + this.target, MSG_TYPE.STATUS);

						// open file and send file size
						File tf = new File(List.get(fileIndex).getAbsolutePath());
						String fs = String.format("%d", tf.length());
						response = new DatagramPacket(fs.getBytes(), fs.getBytes().length, this.cip, this.cport);
						this.tss.send(response);

						// start to transfer file
						FileInputStream f = new FileInputStream(tf);
						this.buffer = new byte[MServer.PACKET_SIZE];
						while (f.read(this.buffer, 0, this.buffer.length) > 0) {
							response = new DatagramPacket(this.buffer, this.buffer.length, this.cip, this.cport);
							this.tss.send(response);
							sleep(this.delay);
						}
						f.close();
						sendDone();
						sendMsg("    傳送媒體檔案完成 " + this.cip.getHostAddress() + ":" + this.cport, MSG_TYPE.COMPLISH);
					} else {
						DatagramPacket response = new DatagramPacket("nfind".getBytes(), "nfind".getBytes().length, this.cip, this.cport);
						this.tss.send(response);
						sendMsg("    " + this.cip.getHostAddress() + ":" + this.cport + " 請求檔案不存在或遺失", MSG_TYPE.ERROR);
					}
				}
			} catch (Exception e) {
				System.out.println(this.getName() + " run ERROR");
				e.printStackTrace();
			}
		}

		private void sendDone() throws IOException {
			String done = "done";
			DatagramPacket response = new DatagramPacket(done.getBytes(), done.getBytes().length, this.cip, this.cport);
			this.tss.send(response);
		}

		private byte[] objectToBytes(Object o) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return baos.toByteArray();
		}

		private int find(String file) {
			for (int i = 0; i < List.size(); i++)
				if (List.get(i).getName().equals(file))
					return i;
			return -1;
		}

		private Vector<String> toStringVector(Vector<UdpFile> v) {
			Vector<String> nv = new Vector<String>();
			for (int i = 0; i < v.size(); i++)
				nv.addElement(v.get(i).getName());
			return nv;
		}
	}

	private class AudioVideoFilter implements FileFilter {
		private final String[] okFileExtensions = new String[] { "mp3", "wmv" };

		@Override
		public boolean accept(File pathname) {
			for (String extension : okFileExtensions)
				if (pathname.getName().toLowerCase().endsWith(extension))
					return true;
			return false;
		}
	}

	private class UdpFile {
		private String parent;
		private String name;
		private int id;

		public UdpFile(String parent, String name, int id) {
			this.parent = parent;
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public String getParent() {
			return parent;
		}

		public int getId() {
			return id;
		}

		public String getAbsolutePath() {
			String p = getDir(this.parent);
			return p + name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public void setId(int id) {
			this.id = id;
		}

		private String getDir(String cur) {
			if (cur.charAt(cur.length() - 1) != File.separatorChar) {
				cur += File.separator;
			}
			return cur;
		}
	}
}
