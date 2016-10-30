import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class MainUI {
	public final ImageIcon icon = new ImageIcon(MainUI.class.getResource("/icon.png"));
	public final int PACKET_SIZE = 1024 * 10;
	public static String CHARSET;

	public static enum MSG_TYPE {
		REQUEST, RESPONSE, ERROR, STATUS;
	}

	private JFrame frmUdpClient;

	private JPanel InputPanel;

	private JLabel LbSelFileList;
	private JLabel LbReponse;
	private JLabel LbFileList;

	private JButton BtnLoadList;

	private JScrollPane MsgScrollPane;
	private JScrollPane ListScrollPane;

	//	private JTextArea TaResponse;
	private JTextPane TaResponse;
	private JList<String> FileList;
	private MyListModel<String> FileListModel;

	private final String SERVER_HOST = "127.0.0.1";
	private final int SERVER_PORT = 3113;
	private final String PLAY = "play";
	private final String LIST = "list";

	private DatagramSocket ClientSocket;
	private byte[] buffer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.show();
					window.updataFileList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() {
		if (File.separatorChar == '/')
			this.CHARSET = "UTF-8";
		else
			this.CHARSET = "BIG5";
		initialize();
		initSocket();
	}

	public void initSocket() {
		try {
			this.ClientSocket = new DatagramSocket();
			this.ClientSocket.setSoTimeout(3000);
		} catch (IOException e) {
			System.out.println("initSocket ERROR");
			e.printStackTrace();
		}
	}

	public DatagramPacket receive(int size) {
		try {
			DatagramPacket tmp = new DatagramPacket(new byte[size], size);
			this.ClientSocket.receive(tmp);
			return tmp;
		} catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				sendMsg("伺服器無回應", MSG_TYPE.ERROR);
				System.out.println("Server not reply.");
			} else {
				System.out.println("receive ERROR");
				e.printStackTrace();
			}
		}
		return null;
	}

	public void updataFileList() {
		try {
			// send update list request
			DatagramPacket request = new DatagramPacket(this.LIST.getBytes(), this.LIST.getBytes().length, getAddress(this.SERVER_HOST), this.SERVER_PORT);
			this.ClientSocket.send(request);

			// receive response of list byte array size
			DatagramPacket response = receive(this.PACKET_SIZE);
			if (response != null) {
				String ls = new String(response.getData(), 0, response.getLength(), CHARSET);
				int listSize = Integer.parseInt(ls);

				// receive real list data
				response = receive(listSize);
				Vector<String> rl = bytesToObject(response.getData());

				// updata the client list
				this.FileListModel.remove();
				for (int i = 0; i < rl.size(); i++)
					this.FileListModel.insert(rl.get(i));
				this.FileList.updateUI();
				sendMsg("更新媒體清單成功", MSG_TYPE.RESPONSE);
			}
		} catch (IOException | ClassNotFoundException e) {
			sendMsg("更新媒體清單失敗", MSG_TYPE.ERROR);
			System.out.println("updateFileList ERROR");
			e.printStackTrace();
		}
	}

	private Vector<String> bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		return (Vector<String>) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
	}

	private InetAddress getAddress(String host) {
		try {
			return InetAddress.getByName(host);
		} catch (Exception e) {
			System.out.println("getAddress ERROR");
			e.printStackTrace();
		}
		return null;
	}

	private void initialize() {
		frmUdpClient = new JFrame();
		frmUdpClient.setTitle("UDP Audio / Video Client");
		frmUdpClient.setIconImage(icon.getImage());
		frmUdpClient.setSize(360, 590);
		frmUdpClient.setMinimumSize(new Dimension(360, 590));
		frmUdpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUdpClient.setLocationRelativeTo(null);
		frmUdpClient.getContentPane().setLayout(null);
		frmUdpClient.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int h = frmUdpClient.getHeight();
				int w = frmUdpClient.getWidth();
				int hd = 0;
				int wd = 30;
				int nw = w - wd;
				int nh = h - hd;

				LbSelFileList.setSize(nw, 30);
				LbReponse.setSize(nw, 30);
				LbFileList.setSize(nw, 30);
				BtnLoadList.setSize(nw, 30);
				MsgScrollPane.setSize(nw, 100);
				ListScrollPane.setSize(nw, nh - 300);
				InputPanel.setSize(nw, nh);
				InputPanel.updateUI();
			}
		});

		InputPanel = new JPanel();
		InputPanel.setBounds(10, 10, 330, 530);
		frmUdpClient.getContentPane().add(InputPanel);
		InputPanel.setLayout(null);

		LbSelFileList = new JLabel("請選擇所要聽或看的影音檔案（點選兩下即可播放）");
		LbSelFileList.setHorizontalAlignment(SwingConstants.CENTER);
		LbSelFileList.setBounds(0, 0, 330, 30);
		InputPanel.add(LbSelFileList);
		LbSelFileList.setFont(new Font("微軟正黑體", Font.BOLD, 14));

		BtnLoadList = new JButton("重新整理清單");
		BtnLoadList.setBounds(0, 30, 330, 30);
		BtnLoadList.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnLoadList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updataFileList();
			}
		});
		InputPanel.add(BtnLoadList);

		LbReponse = new JLabel("訊息");
		LbReponse.setHorizontalAlignment(SwingConstants.CENTER);
		LbReponse.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		LbReponse.setBounds(0, 70, 330, 30);
		InputPanel.add(LbReponse);

		MsgScrollPane = new JScrollPane();
		MsgScrollPane.setBounds(0, 100, 330, 100);
		InputPanel.add(MsgScrollPane);

		TaResponse = new JTextPane();
		TaResponse.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		TaResponse.setEditable(false);
		MsgScrollPane.setViewportView(TaResponse);

		LbFileList = new JLabel("媒體清單");
		LbFileList.setHorizontalAlignment(SwingConstants.CENTER);
		LbFileList.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		LbFileList.setBounds(0, 200, 330, 30);
		InputPanel.add(LbFileList);

		ListScrollPane = new JScrollPane();
		ListScrollPane.setBounds(0, 230, 330, 300);
		InputPanel.add(ListScrollPane);

		FileListModel = new MyListModel<String>();

		FileList = new JList<String>() {
			@Override
			public int locationToIndex(Point location) {
				int index = super.locationToIndex(location);
				if (index != -1 && !getCellBounds(index, index).contains(location)) {
					return -1;
				} else {
					return index;
				}
			}
		};
		FileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FileList.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		FileList.setModel(FileListModel);
		FileList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				// Double-click detected
				try {
					if (evt.getClickCount() == 2) {
						JList l = (JList) evt.getSource();
						MyListModel lm = (MyListModel) l.getModel();
						int index = l.locationToIndex(evt.getPoint());
						if (index >= 0) {
							int idx = l.getSelectedIndex();
							String FileName = (String) lm.getElementAt(idx);
							sendMsg(String.format("請求 \"%s\" 檔案", FileName), MSG_TYPE.REQUEST);

							// send play request
							DatagramPacket request = new DatagramPacket(PLAY.getBytes(), PLAY.getBytes().length, getAddress(SERVER_HOST), SERVER_PORT);
							ClientSocket.send(request);

							// send file name
							request = new DatagramPacket(FileName.getBytes(), FileName.getBytes().length, getAddress(SERVER_HOST), SERVER_PORT);
							ClientSocket.send(request);

							// receive find response
							DatagramPacket response = receive(PACKET_SIZE);
							String responseStr = new String(response.getData(), 0, response.getLength(), CHARSET);
							if (responseStr.equalsIgnoreCase("find")) {
								// create file and receive data
								sendMsg(String.format("\"%s\" 檔案已找到，開始接收檔案！", FileName), MSG_TYPE.RESPONSE);

								// receive file size
								response = receive(PACKET_SIZE);
								int fileSize = Integer.parseInt(new String(response.getData(), 0, response.getLength(), CHARSET));

								// receive file data
								FileOutputStream fw = new FileOutputStream(FileName);
								int count = 0;
								boolean open = false;
								while (true) {
									response = receive(PACKET_SIZE);
									String result = new String(response.getData(), 0, response.getLength(), CHARSET);
									if (result.equals("done"))
										break;
									fw.write(response.getData(), 0, response.getLength());
									count++;
									double rate = ((double) (count * PACKET_SIZE) / fileSize) * 100.0;
									if (!open && (rate > 10 && rate < 15)) {
										open = true;
										Desktop.getDesktop().open(new File(FileName));
									}
								}
								fw.close();
								sendMsg(String.format("\"%s\" 檔案下載完成！", FileName), MSG_TYPE.STATUS);
							} else {
								sendMsg(String.format("\"%s\" 檔案未找到，重新更新媒體清單！", FileName), MSG_TYPE.ERROR);
								updataFileList();
							}
						} else {
							l.clearSelection();
						}
					}
				} catch (IOException e) {
					System.out.println("FileList Double Click ERROR");
					e.printStackTrace();
				}
			}
		});
		ListScrollPane.setViewportView(FileList);
	}

	public void show() {
		frmUdpClient.setVisible(true);
	}

	public void hide() {
		frmUdpClient.setVisible(false);
	}

	public void sendMsg(String msg, MSG_TYPE type) {
		String t;
		Color c;
		switch (type) {
			case REQUEST:
				t = "請求: ";
				c = new Color(0, 0, 204);
				break;
			case RESPONSE:
				t = "回應: ";
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
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		try {
			Document doc = this.TaResponse.getDocument();
			doc.insertString(doc.getLength(), msg + "\n", aset);
			this.TaResponse.setCaretPosition(doc.getLength());
		} catch (Exception e1) {
			System.out.println("sendMsg ERROR");
			e1.printStackTrace();
		}
	}
}

class MyListModel<Object> extends AbstractListModel<Object> {
	private Vector<Object> data = new Vector<Object>();

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Object getElementAt(int index) {
		return data.get(index);
	}

	public void remove(int index) {
		data.remove(index);
	}

	public void remove() {
		data.clear();
	}

	public void insert(int index, Object obj) {
		data.add(index, (Object) obj);
	}

	public void insert(Object obj) {
		data.addElement((Object) obj);
	}
}
