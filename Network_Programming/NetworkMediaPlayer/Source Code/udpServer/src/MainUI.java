import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	public final static ImageIcon icon = new ImageIcon(MainUI.class.getResource("/icon.png"));
	private JFrame frmUdpServer;

	private JPanel InputPanel;
	private JPanel BtnPanel;

	private JScrollPane FlScrollPane;
	private JScrollPane ResponseScrollPane;

	private JLabel LbAddFile;
	private JLabel LbResponse;

	private JButton BtnAddFile;
	private JButton BtnRemoveFile;
	private JButton BtnClearResponse;

	private MyListModel<String> FileListModel;
	private JList<String> FileList;
	private static JTextPane TaResponse;

	private Server ServerThread;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() {
		this.ServerThread = new Server();
		initialize();
		show();
		this.ServerThread.start();
	}

	private void initialize() {
		frmUdpServer = new JFrame();
		frmUdpServer.setTitle("UDP Audio / Video Server");
		frmUdpServer.setIconImage(icon.getImage());
		frmUdpServer.setSize(480, 550);
		frmUdpServer.setMinimumSize(new Dimension(480, 550));
		frmUdpServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUdpServer.setLocationRelativeTo(null);
		frmUdpServer.getContentPane().setLayout(null);
		frmUdpServer.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int h = frmUdpServer.getHeight();
				int w = frmUdpServer.getWidth();
				int hd = 60;
				int wd = 30;
				int nw = w - wd;
				int nh = h - hd;
				BtnPanel.setSize(nw - 90, 30);
				FlScrollPane.setSize(nw, 200);
				ResponseScrollPane.setSize(nw, nh - 300);
				BtnClearResponse.setSize(nw - 90, 30);
				InputPanel.setSize(nw, nh);
				InputPanel.updateUI();
			}
		});

		InputPanel = new JPanel();
		InputPanel.setBounds(10, 10, 450, 490);
		frmUdpServer.getContentPane().add(InputPanel);
		InputPanel.setLayout(null);

		LbAddFile = new JLabel("媒體清單");
		LbAddFile.setHorizontalAlignment(SwingConstants.CENTER);
		LbAddFile.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		LbAddFile.setBounds(0, 0, 80, 30);
		InputPanel.add(LbAddFile);

		BtnPanel = new JPanel();
		BtnPanel.setBounds(90, 0, 360, 30);
		InputPanel.add(BtnPanel);
		BtnPanel.setLayout(new GridLayout(1, 0, 0, 0));

		BtnAddFile = new JButton("選擇並新增目錄或檔案");
		BtnAddFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fileChooser.showOpenDialog(frmUdpServer);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					FileListModel.insert(selectedFile.getAbsolutePath());
					FileList.updateUI();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						System.out.println("BtnAddFile sleep ERROR");
					}
					ServerThread.getServer().setList(FileListModel.getData());
				}
			}
		});
		BtnPanel.add(BtnAddFile);
		BtnAddFile.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		BtnRemoveFile = new JButton("點選並刪除目錄或檔案");
		BtnRemoveFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int sel = FileList.getSelectedIndex();
				if (sel >= 0) {
					String selStr = (String) FileListModel.getElementAt(sel);
					JDialog.setDefaultLookAndFeelDecorated(true);
					int response = JOptionPane.showConfirmDialog(frmUdpServer, "確定刪除\"" + selStr + "\"目錄或檔案嗎？", "刪除確認", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						FileListModel.remove(sel);
						FileList.updateUI();
						ServerThread.getServer().setList(FileListModel.getData());
					}
				}
			}
		});
		BtnPanel.add(BtnRemoveFile);
		BtnRemoveFile.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		FlScrollPane = new JScrollPane();
		FlScrollPane.setBounds(0, 40, 450, 200);
		InputPanel.add(FlScrollPane);

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
		FileList.setModel(FileListModel);
		FileList.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		FlScrollPane.setViewportView(FileList);

		LbResponse = new JLabel("伺服器狀態");
		LbResponse.setHorizontalAlignment(SwingConstants.CENTER);
		LbResponse.setBounds(0, 250, 80, 30);
		LbResponse.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		InputPanel.add(LbResponse);

		BtnClearResponse = new JButton("清除狀態資訊");
		BtnClearResponse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TaResponse.setText("");
				ServerThread.getServer().sendMsg("等待客戶端連接", MServer.MSG_TYPE.STATUS);
			}
		});
		BtnClearResponse.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		BtnClearResponse.setBounds(90, 250, 360, 30);
		InputPanel.add(BtnClearResponse);

		ResponseScrollPane = new JScrollPane();
		ResponseScrollPane.setBounds(0, 290, 450, 200);
		InputPanel.add(ResponseScrollPane);

		TaResponse = new JTextPane();
		TaResponse.setEditable(false);
		TaResponse.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		ResponseScrollPane.setViewportView(TaResponse);
	}

	public void show() {
		this.frmUdpServer.setVisible(true);
	}

	public void hide() {
		this.frmUdpServer.setVisible(false);
	}

	public static void appendResponse(String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		try {
			Document doc = TaResponse.getDocument();
			doc.insertString(doc.getLength(), msg + "\n", aset);
			TaResponse.setCaretPosition(doc.getLength());
		} catch (Exception e1) {
			System.out.println("sendMsg ERROR");
			e1.printStackTrace();
		}
	}
}

class Server extends Thread {
	private MServer server;

	public Server() {
		this.server = new MServer();
	}

	@Override
	public void run() {
		this.server.running();
	}

	public synchronized MServer getServer() {
		return this.server;
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

	public Vector<Object> getData() {
		return this.data;
	}

	public void remove(int index) {
		data.remove(index);
	}

	public void remove() {
		data.clear();
	}

	public void insert(int index, Object obj) {
		boolean found = false;
		for (int i = 0; i < this.data.size(); i++)
			if (this.data.get(i).equals(obj))
				found = true;
		if (!found)
			data.add(index, (Object) obj);
	}

	public void insert(Object obj) {
		boolean found = false;
		for (int i = 0; i < this.data.size(); i++)
			if (this.data.get(i).equals(obj))
				found = true;
		if (!found)
			data.addElement((Object) obj);
	}
}