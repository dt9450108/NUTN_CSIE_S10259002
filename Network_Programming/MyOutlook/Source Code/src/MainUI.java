import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class MainUI {
	public final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public final static ImageIcon icon = new ImageIcon(MainUI.class.getResource("/icon.png"));
	private JFrame MailFrame;
	private SearchWindow sw;

	private JPanel LoginPanel;
	private JPanel MailViewPanel;
	private JPanel MailEditPanel;
	private JPanel MailPoolPanel;
	private JPanel MailPoolOpPanel;
	private JPanel DummyPanel;

	private JSplitPane MainSplitePane;
	private JSplitPane ContentSplitPane;
	private JScrollPane FunctScrollPane;
	private JScrollPane MailViewScrollPane;
	private JScrollPane MailEditScrollPane;
	private JScrollPane MailPoolScrollPane;

	private GridBagLayout gbl_MailViewPanel;
	private GridBagConstraints gbc_ViewUpPanel;
	private GridBagConstraints gbc_MailViewScrollPane;
	private GridBagLayout gbl_MailPoolPanel;
	private GridBagConstraints gbc_MailPoolOpPanel;
	private GridBagConstraints gbc_MailPoolScrollPane;
	private GridBagLayout gbl_MailEditPanel;
	private GridBagConstraints gbc_LbmeSender;
	private GridBagConstraints gbc_TfmeSender;
	private GridBagConstraints gbc_LbmeReceivers;
	private GridBagConstraints gbc_TfmeReceivers;
	private GridBagConstraints gbc_LbmeSubject;
	private GridBagConstraints gbc_TfmeSubject;
	private GridBagConstraints gbc_BtnmeSend;
	private GridBagConstraints gbc_BtnmeClear;
	private GridBagConstraints gbc_BtnmeSaveDraft;
	private GridBagConstraints gbc_BtnmeCancel;
	private GridBagConstraints gbc_MailEditScrollPane;
	private GridBagConstraints gbc_LbViewSender;
	private GridBagConstraints gbc_LbViewSenderText;
	private GridBagConstraints gbc_LbViewReceivers;
	private GridBagConstraints gbc_LbViewReceiversText;
	private GridBagConstraints gbc_LbViewSubject;
	private GridBagConstraints gbc_LbViewSubjectText;
	private GridBagConstraints gbc_LbViewDate;
	private GridBagConstraints gbc_LbViewDateText;

	private JTextField TfServerHost;
	private JTextField TfUsername;
	private JTextField TfmeSender;
	private JTextField TfmeReceivers;
	private JTextField TfmeSubject;
	private JPasswordField TfPassword;

	private SiButton BtnLogin;
	private SiButton BtnLogout;
	private SiButton BtnRefresh;
	private SiButton BtnmeSend;
	private SiButton BtnmeClear;
	private SiButton BtnmeSaveDraft;
	private SiButton BtnmeCancel;
	private SiButton BtnmpReply;
	private SiButton BtnmpForward;
	private SiButton BtnmpSearch;
	private SiButton BtnmpMove;
	private SiButton BtnmpDelete;

	private JLabel LbServerHost;
	private JLabel LbUsername;
	private JLabel LbPassword;
	private JLabel LbViewSender;
	private JLabel LbViewReceivers;
	private JLabel LbViewSubject;
	private JLabel LbViewDate;
	private JLabel LbViewSenderText;
	private JLabel LbViewReceiversText;
	private JLabel LbViewSubjectText;
	private JLabel LbViewDateText;
	private JLabel LbmeSender;
	private JLabel LbmeReceivers;
	private JLabel LbmeSubject;

	private JTextArea TaEmailContent;
	private JTextArea TaMailEditContent;

	private MailTableModel MailPoolTableModel;

	private JTable FunctTable;
	private JTable MailPoolTable;

	private Smtp smtpServer;
	private Pop popServer;
	private Manager mailManager;
	private boolean IS_LOGINT = false;
	private Mail DraftMailTmp;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.MailFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() throws UnsupportedEncodingException {
		sw = new SearchWindow();
		initialize();
		smtpServer = null;
		popServer = null;
		DraftMailTmp = null;
	}

	public void updateMailCategoriesCount() {
		int send = 0;
		int receive = 0;
		int draft = 0;
		int delete = 0;
		for (int i = 0; i < mailManager.getMails().size(); i++) {
			switch (mailManager.getMails().get(i).getStatus()) {
				case SEND:
					send++;
					break;
				case RECEIVE:
					receive++;
					break;
				case DRAFT:
					draft++;
					break;
				case DELETE:
					delete++;
					break;
				default:
					System.out.println("MainUI updateMailCategoriesCount ERROR");
			}
		}

		if (receive > 0)
			FunctTable.getModel().setValueAt("收件匣(" + receive + ")", 1, 0);
		else
			FunctTable.getModel().setValueAt("收件匣", 1, 0);

		if (send > 0)
			FunctTable.getModel().setValueAt("寄件備份(" + send + ")", 2, 0);
		else
			FunctTable.getModel().setValueAt("寄件備份", 2, 0);

		if (draft > 0)
			FunctTable.getModel().setValueAt("草稿匣(" + draft + ")", 3, 0);
		else
			FunctTable.getModel().setValueAt("草稿匣", 3, 0);

		if (delete > 0)
			FunctTable.getModel().setValueAt("回收筒(" + delete + ")", 4, 0);
		else
			FunctTable.getModel().setValueAt("回收筒", 4, 0);
	}

	private void initialize() {
		MailFrame = new JFrame();
		MailFrame.setTitle("Hao Outlook");
		MailFrame.setIconImage(icon.getImage());
		MailFrame.setSize(955, 600);
		MailFrame.setMinimumSize(new Dimension(955, 600));
		MailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MailFrame.getContentPane().setLayout(null);
		MailFrame.setLocationRelativeTo(null);
		MailFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int h = MailFrame.getHeight();
				int w = MailFrame.getWidth();
				MainSplitePane.setSize(w - 25, h - 90);
				MainSplitePane.updateUI();
			}
		});
		MailFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (mailManager != null && IS_LOGINT)
					mailManager.storeData(TfUsername.getText());
			};
		});

		LoginPanel = new JPanel();
		LoginPanel.setBounds(5, 5, 930, 30);
		MailFrame.getContentPane().add(LoginPanel);
		LoginPanel.setLayout(null);

		LbServerHost = new JLabel("伺服器位置");
		LbServerHost.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		LbServerHost.setBounds(0, 0, 80, 30);
		LoginPanel.add(LbServerHost);
		TfServerHost = new JTextField();
		TfServerHost.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		TfServerHost.setBounds(80, 0, 130, 30);
		TfServerHost.setColumns(10);
		LoginPanel.add(TfServerHost);

		LbUsername = new JLabel("使用者帳號");
		LbUsername.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		LbUsername.setBounds(215, 0, 80, 30);
		LoginPanel.add(LbUsername);
		TfUsername = new JTextField();
		TfUsername.setBounds(295, 0, 130, 30);
		TfUsername.setColumns(10);
		LoginPanel.add(TfUsername);

		LbPassword = new JLabel("使用者密碼");
		LbPassword.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		LbPassword.setBounds(430, 0, 80, 30);
		LoginPanel.add(LbPassword);
		TfPassword = new JPasswordField();
		TfPassword.setBounds(510, 0, 130, 30);
		LoginPanel.add(TfPassword);

		BtnLogin = new SiButton("登入", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnLogin.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnLogin.setBounds(650, 0, 80, 30);
		BtnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (IS_LOGINT)
					return;
				String host = TfServerHost.getText();
				String username = TfUsername.getText();
				String password = new String(TfPassword.getPassword());

				// check server if null
				if (host == null || host.length() <= 0) {
					alert("請輸入伺服器位置");
					return;
				}

				// check username if email format
				if (username == null || username.indexOf("@") == -1) {
					alert("使用者帳號信箱格式錯誤");
					return;
				}

				// check password if null
				if (password == null || password.length() <= 0) {
					alert("請輸入使用者密碼");
					return;
				}

				smtpServer = new Smtp(host, username, password);
				popServer = new Pop(host, username, password);
				if (smtpServer.login() && popServer.login()) {
					System.out.println("Success to login");
					TfServerHost.setEditable(false);
					TfUsername.setEditable(false);
					TfPassword.setEditable(false);
					BtnLogin.setEnabled(false);
					BtnRefresh.setEnabled(true);
					FunctTable.setEnabled(true);

					mailManager = new Manager();
					mailManager.readData(TfUsername.getText());
					int maxMailCount = 0;
					for (int i = 0; i < mailManager.getMails().size(); i++)
						if (maxMailCount < mailManager.getMails().get(i).getId())
							maxMailCount = mailManager.getMails().get(i).getId();
					Mail.Count = ++maxMailCount;
					updateMailCategoriesCount();
					Refresh();
					IS_LOGINT = true;
				} else {
					System.out.println("Fail to login");
					alert("無法登入伺服器，請檢查「伺服器位置」、「使用者帳號」或「使用者密碼」是否輸入正確");
					IS_LOGINT = false;
				}
			}
		});
		LoginPanel.add(BtnLogin);

		BtnLogout = new SiButton("登出", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnLogout.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnLogout.setBounds(740, 0, 80, 30);
		BtnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (smtpServer != null && popServer != null) {
					if (!smtpServer.getSmtp().isClosed() && smtpServer.getSmtp().isConnected())
						smtpServer.logout();
					if (!popServer.getPopServer().isClosed() && popServer.getPopServer().isConnected())
						popServer.logout();
					TfServerHost.setEditable(true);
					TfUsername.setEditable(true);
					TfPassword.setEditable(true);
					BtnLogin.setEnabled(true);
					BtnRefresh.setEnabled(false);

					FunctTable.getModel().setValueAt("收件匣", 1, 0);
					FunctTable.getModel().setValueAt("寄件備份", 2, 0);
					FunctTable.getModel().setValueAt("草稿匣", 3, 0);
					FunctTable.getModel().setValueAt("回收筒", 4, 0);
					FunctTable.setEnabled(false);
					FunctTable.clearSelection();
					clearMailView();
					clearMailEditPanel();
					clearMailPool();
					ContentSplitPane.setLeftComponent(DummyPanel);
					ContentSplitPane.setDividerLocation(MailFrame.getHeight() / 2);

					if (mailManager != null)
						mailManager.storeData(TfUsername.getText());
					mailManager = null;

					IS_LOGINT = false;
				} else {
					System.out.println("MailUI BtnLogout ERROR");
					IS_LOGINT = false;
				}
			}
		});
		LoginPanel.add(BtnLogout);

		BtnRefresh = new SiButton("重新整理", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnRefresh.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnRefresh.setBounds(830, 0, 100, 30);
		BtnRefresh.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Refresh();
			};
		});
		BtnRefresh.setEnabled(false);
		LoginPanel.add(BtnRefresh);

		MainSplitePane = new JSplitPane();
		MainSplitePane.setOneTouchExpandable(true);
		MainSplitePane.setContinuousLayout(true);
		MainSplitePane.setBounds(5, 45, 930, 510);
		MainSplitePane.setDividerLocation(100);
		MailFrame.getContentPane().add(MainSplitePane);

		ContentSplitPane = new JSplitPane();
		ContentSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		ContentSplitPane.setOneTouchExpandable(true);
		ContentSplitPane.setContinuousLayout(true);
		ContentSplitPane.setDividerLocation(200);
		MainSplitePane.setRightComponent(ContentSplitPane);

		DummyPanel = new JPanel();
		DummyPanel.setBounds(0, 0, 400, 400);
		DummyPanel.setLayout(null);
		ContentSplitPane.setLeftComponent(DummyPanel);

		MailViewPanel = new JPanel();
		ContentSplitPane.setRightComponent(MailViewPanel);
		gbl_MailViewPanel = new GridBagLayout();
		gbl_MailViewPanel.columnWidths = new int[] { 50, 0, 0, 0, 0, 0 };
		gbl_MailViewPanel.rowHeights = new int[] { 15, 15, 15, 15, 0, 0 };
		gbl_MailViewPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_MailViewPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		MailViewPanel.setLayout(gbl_MailViewPanel);

		LbViewSender = new JLabel("來源：");
		gbc_LbViewSender = new GridBagConstraints();
		gbc_LbViewSender.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewSender.fill = GridBagConstraints.BOTH;
		gbc_LbViewSender.gridx = 0;
		gbc_LbViewSender.gridy = 0;
		MailViewPanel.add(LbViewSender, gbc_LbViewSender);
		LbViewSender.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewSenderText = new JLabel("");
		gbc_LbViewSenderText = new GridBagConstraints();
		gbc_LbViewSenderText.weightx = 1.0;
		gbc_LbViewSenderText.fill = GridBagConstraints.BOTH;
		gbc_LbViewSenderText.gridwidth = 4;
		gbc_LbViewSenderText.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewSenderText.gridx = 1;
		gbc_LbViewSenderText.gridy = 0;
		MailViewPanel.add(LbViewSenderText, gbc_LbViewSenderText);
		LbViewSenderText.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewReceivers = new JLabel("收信：");
		gbc_LbViewReceivers = new GridBagConstraints();
		gbc_LbViewReceivers.fill = GridBagConstraints.BOTH;
		gbc_LbViewReceivers.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewReceivers.gridx = 0;
		gbc_LbViewReceivers.gridy = 1;
		MailViewPanel.add(LbViewReceivers, gbc_LbViewReceivers);
		LbViewReceivers.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewReceiversText = new JLabel("");
		gbc_LbViewReceiversText = new GridBagConstraints();
		gbc_LbViewReceiversText.weightx = 1.0;
		gbc_LbViewReceiversText.fill = GridBagConstraints.BOTH;
		gbc_LbViewReceiversText.gridwidth = 4;
		gbc_LbViewReceiversText.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewReceiversText.gridx = 1;
		gbc_LbViewReceiversText.gridy = 1;
		MailViewPanel.add(LbViewReceiversText, gbc_LbViewReceiversText);
		LbViewReceiversText.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewSubject = new JLabel("標題：");
		gbc_LbViewSubject = new GridBagConstraints();
		gbc_LbViewSubject.fill = GridBagConstraints.BOTH;
		gbc_LbViewSubject.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewSubject.gridx = 0;
		gbc_LbViewSubject.gridy = 2;
		MailViewPanel.add(LbViewSubject, gbc_LbViewSubject);
		LbViewSubject.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewSubjectText = new JLabel("");
		gbc_LbViewSubjectText = new GridBagConstraints();
		gbc_LbViewSubjectText.weightx = 1.0;
		gbc_LbViewSubjectText.fill = GridBagConstraints.BOTH;
		gbc_LbViewSubjectText.gridwidth = 4;
		gbc_LbViewSubjectText.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewSubjectText.gridx = 1;
		gbc_LbViewSubjectText.gridy = 2;
		MailViewPanel.add(LbViewSubjectText, gbc_LbViewSubjectText);
		LbViewSubjectText.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewDate = new JLabel("日期：");
		gbc_LbViewDate = new GridBagConstraints();
		gbc_LbViewDate.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewDate.fill = GridBagConstraints.BOTH;
		gbc_LbViewDate.gridx = 0;
		gbc_LbViewDate.gridy = 3;
		MailViewPanel.add(LbViewDate, gbc_LbViewDate);
		LbViewDate.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		LbViewDateText = new JLabel("");
		gbc_LbViewDateText = new GridBagConstraints();
		gbc_LbViewDateText.insets = new Insets(0, 0, 0, 0);
		gbc_LbViewDateText.weightx = 1.0;
		gbc_LbViewDateText.gridwidth = 4;
		gbc_LbViewDateText.fill = GridBagConstraints.BOTH;
		gbc_LbViewDateText.gridx = 1;
		gbc_LbViewDateText.gridy = 3;
		MailViewPanel.add(LbViewDateText, gbc_LbViewDateText);
		LbViewDateText.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		MailViewScrollPane = new JScrollPane();
		gbc_MailViewScrollPane = new GridBagConstraints();
		gbc_MailViewScrollPane.gridwidth = 5;
		gbc_MailViewScrollPane.weighty = 1.0;
		gbc_MailViewScrollPane.weightx = 1.0;
		gbc_MailViewScrollPane.fill = GridBagConstraints.BOTH;
		gbc_MailViewScrollPane.insets = new Insets(0, 0, 0, 0);
		gbc_MailViewScrollPane.gridx = 0;
		gbc_MailViewScrollPane.gridy = 4;
		MailViewPanel.add(MailViewScrollPane, gbc_MailViewScrollPane);

		TaEmailContent = new JTextArea();
		MailViewScrollPane.setViewportView(TaEmailContent);
		TaEmailContent.setLineWrap(true);
		TaEmailContent.setTabSize(4);
		TaEmailContent.setEditable(false);
		TaEmailContent.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		// Content split pane up
		MailEditPanel = new JPanel();
		//		ContentSplitPane.setLeftComponent(MailEditPanel);
		gbl_MailEditPanel = new GridBagLayout();
		gbl_MailEditPanel.columnWidths = new int[] { 65, 65, 65, 65, 65, 0 };
		gbl_MailEditPanel.rowHeights = new int[] { 20, 20, 20, 25, 0, 0 };
		gbl_MailEditPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_MailEditPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		MailEditPanel.setLayout(gbl_MailEditPanel);

		LbmeSender = new JLabel("寄件人：");
		gbc_LbmeSender = new GridBagConstraints();
		gbc_LbmeSender.insets = new Insets(0, 0, 0, 0);
		gbc_LbmeSender.fill = GridBagConstraints.BOTH;
		gbc_LbmeSender.gridx = 0;
		gbc_LbmeSender.gridy = 0;
		MailEditPanel.add(LbmeSender, gbc_LbmeSender);
		LbmeSender.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		TfmeSender = new JTextField();
		gbc_TfmeSender = new GridBagConstraints();
		gbc_TfmeSender.gridwidth = 4;
		gbc_TfmeSender.insets = new Insets(0, 0, 0, 0);
		gbc_TfmeSender.fill = GridBagConstraints.BOTH;
		gbc_TfmeSender.weightx = 1.0;
		gbc_TfmeSender.gridx = 1;
		gbc_TfmeSender.gridy = 0;
		MailEditPanel.add(TfmeSender, gbc_TfmeSender);
		TfmeSender.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		TfmeSender.setColumns(10);

		LbmeReceivers = new JLabel("收件人：");
		gbc_LbmeReceivers = new GridBagConstraints();
		gbc_LbmeReceivers.fill = GridBagConstraints.BOTH;
		gbc_LbmeReceivers.insets = new Insets(0, 0, 0, 0);
		gbc_LbmeReceivers.gridx = 0;
		gbc_LbmeReceivers.gridy = 1;
		MailEditPanel.add(LbmeReceivers, gbc_LbmeReceivers);
		LbmeReceivers.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		TfmeReceivers = new JTextField();
		gbc_TfmeReceivers = new GridBagConstraints();
		gbc_TfmeReceivers.gridwidth = 4;
		gbc_TfmeReceivers.weightx = 1.0;
		gbc_TfmeReceivers.fill = GridBagConstraints.BOTH;
		gbc_TfmeReceivers.insets = new Insets(0, 0, 0, 0);
		gbc_TfmeReceivers.gridx = 1;
		gbc_TfmeReceivers.gridy = 1;
		MailEditPanel.add(TfmeReceivers, gbc_TfmeReceivers);
		TfmeReceivers.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		TfmeReceivers.setColumns(10);

		LbmeSubject = new JLabel("標　題：");
		gbc_LbmeSubject = new GridBagConstraints();
		gbc_LbmeSubject.insets = new Insets(0, 0, 0, 0);
		gbc_LbmeSubject.fill = GridBagConstraints.BOTH;
		gbc_LbmeSubject.gridx = 0;
		gbc_LbmeSubject.gridy = 2;
		MailEditPanel.add(LbmeSubject, gbc_LbmeSubject);
		LbmeSubject.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		TfmeSubject = new JTextField();
		gbc_TfmeSubject = new GridBagConstraints();
		gbc_TfmeSubject.gridwidth = 4;
		gbc_TfmeSubject.insets = new Insets(0, 0, 0, 0);
		gbc_TfmeSubject.fill = GridBagConstraints.BOTH;
		gbc_TfmeSubject.weightx = 1.0;
		gbc_TfmeSubject.gridx = 1;
		gbc_TfmeSubject.gridy = 2;
		MailEditPanel.add(TfmeSubject, gbc_TfmeSubject);
		TfmeSubject.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		TfmeSubject.setColumns(10);

		BtnmeSend = new SiButton("傳送", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		gbc_BtnmeSend = new GridBagConstraints();
		gbc_BtnmeSend.insets = new Insets(0, 0, 0, 0);
		gbc_BtnmeSend.fill = GridBagConstraints.BOTH;
		gbc_BtnmeSend.gridx = 0;
		gbc_BtnmeSend.gridy = 3;
		BtnmeSend.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmeSend.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String sender = TfmeSender.getText();
				String[] receivers = TfmeReceivers.getText().replaceAll(" ", "").split(",");
				String subject = TfmeSubject.getText();
				String content = TaMailEditContent.getText();

				if (sender == null || sender.indexOf("@") == -1) {
					alert("寄件者格式錯誤或未輸入");
					return;
				}

				for (int i = 0; i < receivers.length; i++) {
					if (receivers[i] == null || receivers[i].indexOf("@") == -1) {
						alert("收件者格式錯誤或未輸入，請使用\",\"隔開不同的收件信箱");
						return;
					}
				}

				if (subject.length() <= 0 && content.length() <= 0) {
					alert("信件「主旨」與信件「內容」不得同時為空");
					return;
				}

				// close smtp socket
				if (smtpServer != null) {
					if (!smtpServer.getSmtp().isClosed()) {
						smtpServer.logout();
						smtpServer.login();
					} else {
						smtpServer.login();
					}
				}

				if (smtpServer.sendMail(subject, receivers, content)) {
					clearMailEditPanel();
					mailManager.addMail(new Mail(sender, new Vector<String>(Arrays.asList(receivers)), subject, content, MainUI.DATE_TIME_FORMAT.format(new Date()), Mail.STATUS.SEND));
					FunctTable.getSelectionModel().setSelectionInterval(1, 1);
					FunctSelect(1);
					if (mailManager.getMails().removeElement(DraftMailTmp)) {
						DraftMailTmp = null;
						System.out.println("Remove draft mail success");
					} else {
						System.out.println("Remove draft mail failure");
					}
					updateMailCategoriesCount();
					inform("信件已成功寄出，寄件後，請點選「重新整理」按鈕更新頁面，假設您寄信給自己的話！");
				} else {
					inform("信件無法寄出");
				}
			};
		});
		MailEditPanel.add(BtnmeSend, gbc_BtnmeSend);

		BtnmeClear = new SiButton("清除", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		gbc_BtnmeClear = new GridBagConstraints();
		gbc_BtnmeClear.insets = new Insets(0, 0, 0, 0);
		gbc_BtnmeClear.fill = GridBagConstraints.BOTH;
		gbc_BtnmeClear.gridx = 1;
		gbc_BtnmeClear.gridy = 3;
		BtnmeClear.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmeClear.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (confirm("確定清除所有欄位嗎？")) {
					clearMailEditPanel();
				}
			};
		});
		MailEditPanel.add(BtnmeClear, gbc_BtnmeClear);

		BtnmeSaveDraft = new SiButton("儲存草稿", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		gbc_BtnmeSaveDraft = new GridBagConstraints();
		gbc_BtnmeSaveDraft.insets = new Insets(0, 0, 0, 0);
		gbc_BtnmeSaveDraft.fill = GridBagConstraints.BOTH;
		gbc_BtnmeSaveDraft.gridx = 2;
		gbc_BtnmeSaveDraft.gridy = 3;
		BtnmeSaveDraft.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmeSaveDraft.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String sender = TfmeSender.getText();
				String[] receivers = TfmeReceivers.getText().replaceAll(" ", "").split(",");
				String subject = TfmeSubject.getText();
				String content = TaMailEditContent.getText();

				if (sender == null || receivers == null || subject == null || content == null || (TfmeReceivers.getText().length() == 0 && subject.length() == 0 && content.length() == 0)) {
					alert("草稿信件內容不得全部為空");
					return;
				}

				if (DraftMailTmp != null) {
					DraftMailTmp.setReceivers(new Vector<String>(Arrays.asList(receivers)));
					DraftMailTmp.setSubject(subject);
					DraftMailTmp.setContent(content);
					DraftMailTmp.setDate(MainUI.DATE_TIME_FORMAT.format(new Date()));
					updateMailCategoriesCount();
					FunctSelect(3);
					setMailView(DraftMailTmp);
					DraftMailTmp = null;
				} else {
					mailManager.addMail(new Mail(sender, new Vector<String>(Arrays.asList(receivers)), subject, content, MainUI.DATE_TIME_FORMAT.format(new Date()), Mail.STATUS.DRAFT));
					FunctTable.getSelectionModel().setSelectionInterval(3, 3);
					FunctSelect(3);
					updateMailCategoriesCount();
				}
			};
		});
		MailEditPanel.add(BtnmeSaveDraft, gbc_BtnmeSaveDraft);

		BtnmeCancel = new SiButton("返回", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		gbc_BtnmeCancel = new GridBagConstraints();
		gbc_BtnmeCancel.insets = new Insets(0, 0, 0, 0);
		gbc_BtnmeCancel.fill = GridBagConstraints.BOTH;
		gbc_BtnmeCancel.gridx = 3;
		gbc_BtnmeCancel.gridy = 3;
		BtnmeCancel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmeCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = FunctTable.getSelectedRow();
				if (row >= 0) {
					if (row == 0)
						row = 1;
					FunctTable.getSelectionModel().setSelectionInterval(row, row);
					FunctSelect(row);
					updateMailCategoriesCount();
					DraftMailTmp = null;
				}
			};
		});
		MailEditPanel.add(BtnmeCancel, gbc_BtnmeCancel);

		MailEditScrollPane = new JScrollPane();
		gbc_MailEditScrollPane = new GridBagConstraints();
		gbc_MailEditScrollPane.weighty = 1.0;
		gbc_MailEditScrollPane.weightx = 1.0;
		gbc_MailEditScrollPane.gridwidth = 5;
		gbc_MailEditScrollPane.fill = GridBagConstraints.BOTH;
		gbc_MailEditScrollPane.insets = new Insets(0, 0, 0, 0);
		gbc_MailEditScrollPane.gridx = 0;
		gbc_MailEditScrollPane.gridy = 4;
		MailEditPanel.add(MailEditScrollPane, gbc_MailEditScrollPane);

		TaMailEditContent = new JTextArea();
		TaMailEditContent.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		MailEditScrollPane.setViewportView(TaMailEditContent);

		// Content split pane up for mail pool panel
		MailPoolPanel = new JPanel();
		//		ContentSplitPane.setLeftComponent(MailPoolPanel);
		gbl_MailPoolPanel = new GridBagLayout();
		gbl_MailPoolPanel.columnWidths = new int[] { 0, 0 };
		gbl_MailPoolPanel.rowHeights = new int[] { 30, 0, 0 };
		gbl_MailPoolPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_MailPoolPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		MailPoolPanel.setLayout(gbl_MailPoolPanel);

		MailPoolOpPanel = new JPanel();
		gbc_MailPoolOpPanel = new GridBagConstraints();
		gbc_MailPoolOpPanel.weightx = 1.0;
		gbc_MailPoolOpPanel.fill = GridBagConstraints.BOTH;
		gbc_MailPoolOpPanel.gridx = 0;
		gbc_MailPoolOpPanel.gridy = 0;
		MailPoolPanel.add(MailPoolOpPanel, gbc_MailPoolOpPanel);
		MailPoolOpPanel.setLayout(new GridLayout(1, 0, 0, 0));

		BtnmpReply = new SiButton("回信", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnmpReply.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmpReply.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Vector<Integer> row = new Vector<Integer>();
				for (int i = 0; i < MailPoolTableModel.getRowCount(); i++)
					if (((boolean) MailPoolTableModel.getValueAt(i, 0)))
						row.addElement(i);
				if (row.size() == 1 && (boolean) MailPoolTableModel.getValueAt(row.get(0), 0)) {
					int mailIdx = mailManager.search((int) MailPoolTableModel.getValueAt(row.get(0), 4));
					if (mailIdx >= 0) {
						Mail mail = mailManager.getMails().get(mailIdx);
						TfmeSender.setText(TfUsername.getText());
						TfmeSender.setEditable(false);
						int frow = FunctTable.getSelectedRow();
						if (frow == 3) {
							TfmeReceivers.setText(mail.getReceiverStr());
							TfmeSubject.setText(mail.getSubject());
							TaMailEditContent.setText(mail.getContent());
							DraftMailTmp = mail;
						} else {
							TfmeReceivers.setText(mail.getSender());
							TfmeSubject.setText("Re: " + mail.getSubject());
							TaMailEditContent.setText("\n\n\n\n-----Original message-----\nSubject:" + mail.getSubject() + "\n" + mail.getContent());
						}
						ContentSplitPane.setLeftComponent(MailEditPanel);
						ContentSplitPane.setDividerLocation(MailFrame.getHeight() / 2);
					}
				} else {
					alert("請勾選單一信件回信");
				}
				clearMailView();
			};
		});
		MailPoolOpPanel.add(BtnmpReply);

		BtnmpForward = new SiButton("轉寄", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnmpForward.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmpForward.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Vector<Integer> row = new Vector<Integer>();
				for (int i = 0; i < MailPoolTableModel.getRowCount(); i++)
					if (((boolean) MailPoolTableModel.getValueAt(i, 0)))
						row.addElement(i);
				if (row.size() == 1 && (boolean) MailPoolTableModel.getValueAt(row.get(0), 0)) {
					int mailIdx = mailManager.search((int) MailPoolTableModel.getValueAt(row.get(0), 4));
					if (mailIdx >= 0) {
						Mail mail = mailManager.getMails().get(mailIdx);
						TfmeSender.setText(TfUsername.getText());
						TfmeSender.setEditable(false);
						int frow = FunctTable.getSelectedRow();
						if (frow == 3) {
							TfmeReceivers.setText(mail.getReceiverStr());
							TfmeSubject.setText(mail.getSubject());
							TaMailEditContent.setText(mail.getContent());
							DraftMailTmp = mail;
						} else {
							TfmeReceivers.setText("");
							TfmeSubject.setText("Fw: " + mail.getSubject());
							TaMailEditContent.setText("\n\n\n\n-----Forwarded message-----\nSubject:" + mail.getSubject() + "\n" + mail.getContent());
						}
						ContentSplitPane.setLeftComponent(MailEditPanel);
						ContentSplitPane.setDividerLocation(MailFrame.getHeight() / 2);
						TfmeReceivers.requestFocus();
					}
				} else {
					alert("請勾選單一信件轉寄");
				}
				clearMailView();
			};
		});
		MailPoolOpPanel.add(BtnmpForward);

		BtnmpSearch = new SiButton("搜尋", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnmpSearch.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmpSearch.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				sw.open();
			};
		});
		MailPoolOpPanel.add(BtnmpSearch);

		BtnmpMove = new SiButton("移至", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnmpMove.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmpMove.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int thisBtnWidth = BtnmpMove.getWidth();
				JPopupMenu moveMenu = new JPopupMenu();
				JMenuItem receiveBox = new JMenuItem("收件匣");
				JMenuItem sendBox = new JMenuItem("寄件匣");
				JMenuItem draftBox = new JMenuItem("草稿匣");
				JMenuItem trashBox = new JMenuItem("回收筒");
				ActionListener menuListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String target = e.getActionCommand();
						int curTarget = FunctTable.getSelectedRow();
						if (curTarget < 0 || curTarget == 0)
							return;
						Mail.STATUS ts = Mail.STATUS.NONE;
						switch (curTarget) {
							case 1:
								ts = Mail.STATUS.RECEIVE;
								break;
							case 2:
								ts = Mail.STATUS.SEND;
								break;
							case 3:
								ts = Mail.STATUS.DRAFT;
								break;
							case 4:
								ts = Mail.STATUS.DELETE;
								break;
							default:
								System.out.println("Something ERROR in BtnmpMove");
						}

						if (target.equals("收件匣"))
							PopItemClick(ts, Mail.STATUS.RECEIVE, target);
						else if (target.equals("寄件匣"))
							PopItemClick(ts, Mail.STATUS.SEND, target);
						else if (target.equals("草稿匣"))
							PopItemClick(ts, Mail.STATUS.DRAFT, target);
						else if (target.equals("回收筒"))
							PopItemClick(ts, Mail.STATUS.DELETE, target);
						else {
							System.out.println("popup menu ERROR");
						}
					}
				};
				moveMenu.setPreferredSize(new Dimension(thisBtnWidth, 25 * 4));

				receiveBox.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
				sendBox.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
				draftBox.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
				trashBox.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

				receiveBox.setPreferredSize(new Dimension(thisBtnWidth, 25));
				sendBox.setPreferredSize(new Dimension(thisBtnWidth, 25));
				draftBox.setPreferredSize(new Dimension(thisBtnWidth, 25));
				trashBox.setPreferredSize(new Dimension(thisBtnWidth, 25));

				receiveBox.addActionListener(menuListener);
				sendBox.addActionListener(menuListener);
				draftBox.addActionListener(menuListener);
				trashBox.addActionListener(menuListener);

				moveMenu.add(receiveBox);
				moveMenu.add(sendBox);
				moveMenu.add(draftBox);
				moveMenu.add(trashBox);
				moveMenu.show(BtnmpMove, 0, 30);
			};
		});
		MailPoolOpPanel.add(BtnmpMove);

		BtnmpDelete = new SiButton("刪除", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnmpDelete.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		BtnmpDelete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Vector<Integer> srow = new Vector<Integer>();
				for (int i = 0; i < MailPoolTableModel.getRowCount(); i++)
					if (((boolean) MailPoolTableModel.getValueAt(i, 0)))
						srow.addElement((int) MailPoolTableModel.getValueAt(i, 4));

				if (srow.size() > 0) {
					int functType = FunctTable.getSelectedRow();
					if (functType >= 0) {
						if (functType == 4 && confirm("確定永遠刪除此信件嗎？此動作「無法復原」！")) {
							System.out.println("remove forever");
							for (int i = 0; i < srow.size(); i++) {
								int row = -1;
								for (int j = 0; j < MailPoolTableModel.getRowCount(); j++) {
									System.out.println("a, srow : " + MailPoolTableModel.getValueAt(j, 4) + ", " + srow.get(i));
									if (((int) MailPoolTableModel.getValueAt(j, 4)) == srow.get(i)) {
										row = j;
										break;
									}
								}
								if (row >= 0) {
									System.out.println("remove: " + srow.get(i));
									mailManager.delete(srow.get(i));
									MailPoolTableModel.removeRow(row);
								} else {
									System.out.println("remove forever to trash can ERROR");
								}
							}
							MailPoolTable.updateUI();
							updateMailCategoriesCount();
						} else if (functType != 4 && confirm("確定將此信件移至回收筒嗎？")) {
							System.out.println("remove to trash can");
							for (int i = 0; i < srow.size(); i++) {
								int row = -1;
								for (int j = 0; j < MailPoolTableModel.getRowCount(); j++) {
									if (MailPoolTableModel.getValueAt(j, 4) == srow.get(i)) {
										row = j;
										break;
									}
								}
								if (row >= 0) {
									System.out.println("move: " + srow.get(i));
									int mailIdx = mailManager.search(srow.get(i));
									if (mailIdx >= 0) {
										mailManager.getMails().get(mailIdx).setStatus(Mail.STATUS.DELETE);
										MailPoolTableModel.removeRow(row);
									}
								} else {
									System.out.println("remove to trash can ERROR");
								}
							}
							MailPoolTable.updateUI();
							updateMailCategoriesCount();
						} else {
							System.out.println("Do not delete the selected mail");
						}
					}
				} else {
					System.out.println("Not select any row to delete");
					alert("請勾選欲刪除之郵件");
				}
				clearMailView();
			};
		});
		MailPoolOpPanel.add(BtnmpDelete);

		MailPoolScrollPane = new JScrollPane();
		gbc_MailPoolScrollPane = new GridBagConstraints();
		gbc_MailPoolScrollPane.weighty = 1.0;
		gbc_MailPoolScrollPane.weightx = 1.0;
		gbc_MailPoolScrollPane.fill = GridBagConstraints.BOTH;
		gbc_MailPoolScrollPane.gridx = 0;
		gbc_MailPoolScrollPane.gridy = 1;
		MailPoolPanel.add(MailPoolScrollPane, gbc_MailPoolScrollPane);

		// Mail Pool Table Model
		MailPoolTableModel = new MailTableModel();

		MailPoolTable = new JTable();
		MailPoolTable.setFillsViewportHeight(true);
		MailPoolTable.setShowGrid(false);
		MailPoolTable.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		MailPoolTable.setSelectionBackground(new Color(76, 175, 80));
		MailPoolTable.setSelectionForeground(Color.white);
		MailPoolTable.setRowHeight(25);
		MailPoolTable.setModel(MailPoolTableModel);
		MailPoolTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MailPoolTable.getColumnModel().getColumn(4).setMinWidth(0);
		MailPoolTable.getColumnModel().getColumn(4).setMaxWidth(0);
		MailPoolTable.getColumnModel().getColumn(4).setWidth(0);
		MailPoolTable.getTableHeader().setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		MailPoolTable.getTableHeader().setReorderingAllowed(false);
		MailPoolTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					int row = MailPoolTable.getSelectedRow();
					if (row >= 0) {
						int mailId = (int) MailPoolTableModel.getValueAt(row, 4);
						Mail mail = mailManager.getMail(mailId);
						if (mail != null) {
							setMailView(mail);
						} else {
							clearMailView();
							System.out.println("No such mail existed");
						}
					} else {
						System.out.println("MailPoolTable not select any row");
					}
				}
			}
		});
		MailPoolScrollPane.setViewportView(MailPoolTable);

		FunctScrollPane = new JScrollPane();
		MainSplitePane.setLeftComponent(FunctScrollPane);

		FunctTable = new JTable();
		FunctTable.setFillsViewportHeight(true);
		FunctTable.setShowGrid(false);
		FunctTable.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		FunctTable.setSelectionBackground(new Color(76, 175, 80));
		FunctTable.setSelectionForeground(Color.white);
		FunctTable.setRowHeight(25);
		FunctTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FunctTable.getTableHeader().setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		FunctTable.getTableHeader().setReorderingAllowed(false);
		FunctTable.setEnabled(false);
		FunctTable.setModel(new DefaultTableModel(new Object[][] { { "寫信" }, { "收件匣" }, { "寄件備份" }, { "草稿匣" }, { "回收筒" } }, new String[] { "功能" }) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		FunctTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					int row = FunctTable.getSelectedRow();
					FunctSelect(row);
				}
			}
		});
		FunctTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = FunctTable.rowAtPoint(evt.getPoint());
				FunctSelect(row);
			}
		});
		FunctScrollPane.setViewportView(FunctTable);

		sw.getBtnQuery().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				searchWindowMail();
			}
		});

		sw.getBtnCancel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sw.close();
			}
		});
	}

	public void FunctSelect(int row) {
		if (IS_LOGINT && (row >= 0 && row < FunctTable.getModel().getRowCount())) {
			clearMailView();
			if (row >= 0) {
				BtnmpReply.setText("回信");
				switch (row) {
					case 0:
						clearMailEditPanel();
						ContentSplitPane.setLeftComponent(MailEditPanel);
						TfmeSender.setText(TfUsername.getText());
						TfmeSender.setEditable(false);
						break;
					case 1:
						updataMailPoolTable(Mail.STATUS.RECEIVE);
						ContentSplitPane.setLeftComponent(MailPoolPanel);
						break;
					case 2:
						updataMailPoolTable(Mail.STATUS.SEND);
						ContentSplitPane.setLeftComponent(MailPoolPanel);
						break;
					case 3:
						updataMailPoolTable(Mail.STATUS.DRAFT);
						ContentSplitPane.setLeftComponent(MailPoolPanel);
						BtnmpReply.setText("編輯");
						break;
					case 4:
						updataMailPoolTable(Mail.STATUS.DELETE);
						ContentSplitPane.setLeftComponent(MailPoolPanel);
						break;
					default:
						System.out.println("MainUI FunctTable Selection Model ERROR");
				}
			} else {
				ContentSplitPane.setLeftComponent(DummyPanel);
			}
			ContentSplitPane.setDividerLocation(MailFrame.getHeight() / 2);
		}
	}

	public void Refresh() {
		if (popServer != null && IS_LOGINT) {
			if (!popServer.getPopServer().isClosed()) {
				popServer.logout();
				popServer.login();
			} else {
				popServer.login();
			}

			updateMailCategoriesCount();
			Vector<String[]> listNumber = popServer.doList();
			if (listNumber != null) {
				for (int i = 0; i < listNumber.size(); i++) {
					Mail rmail = popServer.doRetrmail(Integer.parseInt(listNumber.get(i)[0]));
					if (rmail != null) {
						rmail.getReceivers().addElement(TfUsername.getText());
						mailManager.addMail(rmail);
						popServer.doDelete(Integer.parseInt(listNumber.get(i)[0]));
					}
				}
				int row = FunctTable.getSelectedRow();
				FunctSelect(1);
				FunctTable.getSelectionModel().setSelectionInterval(1, 1);
			} else {
				System.out.println("MainUI BtnRefresh listNumber is null");
			}
			updateMailCategoriesCount();
			popServer.logout();
		}
	}

	public void clearMailEditPanel() {
		TfmeReceivers.setText("");
		TfmeSubject.setText("");
		TaMailEditContent.setText("");
	}

	public void clearMailPool() {
		MailPoolTableModel.removeAll();
		MailPoolTable.updateUI();
	}

	public void clearMailView() {
		LbViewSenderText.setText("");
		LbViewReceiversText.setText("");
		LbViewSubjectText.setText("");
		LbViewDateText.setText("");
		TaEmailContent.setText("");
	}

	public void setMailView(Mail mail) {
		LbViewSenderText.setText(mail.getSender());
		LbViewReceiversText.setText(mail.getReceiverStr());
		LbViewSubjectText.setText(mail.getSubject());
		LbViewDateText.setText(mail.getDate());
		TaEmailContent.setText(mail.getContent());
	}

	public void updataMailPoolTable(Mail.STATUS type) {
		MailPoolTableModel.removeAll();
		for (int i = 0; i < mailManager.getMails().size(); i++)
			if (mailManager.getMails().get(i).getStatus() == type)
				MailPoolTableModel.insertData(mailManager.getMails().get(i));
		MailPoolTable.updateUI();
	}

	public void alert(String msg) {
		JOptionPane.showMessageDialog(MailFrame, msg, "錯誤", JOptionPane.ERROR_MESSAGE);
	}

	public void inform(String msg) {
		JOptionPane.showMessageDialog(MailFrame, msg, "通知", JOptionPane.PLAIN_MESSAGE);

	}

	public boolean confirm(String msg) {
		JDialog.setDefaultLookAndFeelDecorated(true);
		int response = JOptionPane.showConfirmDialog(MailFrame, msg, "詢問", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	public void searchWindowMail() {
		String s = sw.getTfQueryText().getText();
		if (s.length() == 0)
			return;

		int mailId = -1;
		int row = -1;
		for (int i = 0; i < MailPoolTableModel.getRowCount(); i++) {
			String subject = (String) MailPoolTableModel.getValueAt(i, 1);
			String sender = (String) MailPoolTableModel.getValueAt(i, 2);

			if (subject.indexOf(s) >= 0 || sender.indexOf(s) >= 0) {
				mailId = (int) MailPoolTableModel.getValueAt(i, 4);
				row = i;
				break;
			}
		}

		if (mailId >= 0) {
			// set table view
			MailPoolTable.getSelectionModel().setSelectionInterval(row, row);

			// set mail view
			int mailIndex = mailManager.search(mailId);
			if (mailIndex >= 0) {
				setMailView(mailManager.getMails().get(mailIndex));
			} else {
				System.out.println("Something ERROR at searchWindowMail");
			}
		} else {
			alert("未搜尋到關於「" + s + "」的信件！");
		}
	}

	public void PopItemClick(Mail.STATUS source, Mail.STATUS dest, String target) {
		if (source == dest)
			return;

		Vector<Integer> srow = new Vector<Integer>();
		for (int i = 0; i < MailPoolTableModel.getRowCount(); i++)
			if (((boolean) MailPoolTableModel.getValueAt(i, 0)))
				srow.addElement((int) MailPoolTableModel.getValueAt(i, 4));

		if (srow.size() > 0) {
			if (confirm("確定將選擇的信件移至「" + target + "」嗎？")) {
				for (int i = 0; i < srow.size(); i++) {
					int row = -1;
					for (int j = 0; j < MailPoolTableModel.getRowCount(); j++) {
						if (MailPoolTableModel.getValueAt(j, 4) == srow.get(i)) {
							row = j;
							break;
						}
					}
					if (row >= 0) {
						int mailIdx = mailManager.search(srow.get(i));
						if (mailIdx >= 0) {
							mailManager.getMails().get(mailIdx).setStatus(dest);
							MailPoolTableModel.removeRow(row);
						}
					}
				}
				MailPoolTable.updateUI();
				updateMailCategoriesCount();
				int row = FunctTable.getSelectedRow();
				FunctSelect(row);
				FunctTable.getSelectionModel().setSelectionInterval(row, row);
			} else {
				System.out.println("Do not move the selected mail");
			}
		} else {
			alert("未選擇任何信件做移動");
		}
		clearMailView();
	}
}
