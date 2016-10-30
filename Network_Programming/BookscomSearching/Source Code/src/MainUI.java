import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class MainUI {
	private JFrame frame;
	private JPanel InputPanel;
	private JPanel TablePanel;
	private JLabel LbItemNum;
	private JLabel LbKeyword;
	private JPanel TableBottomPanel;

	private GridBagLayout GblTableLayout;
	private GridBagConstraints gbc_LbTableHeaderText;
	private GridBagConstraints gbc_SpDataTable;
	private GridBagConstraints gbc_TableBottomPanel;

	private JTextField TfKeyword;
	private JTextField TfItemNum;
	private JScrollPane SpDataTable;
	private BookTableModel DataTableModel;
	private TableRowSorter<TableModel> DataTableSorter;
	private JTable DataTable;

	private SiButton BtnSubmit;
	private SiButton BtnClear;
	private SiButton BtnRetcs;
	private SiButton btnFirst;
	private SiButton btnPrev;
	private SiButton btnNext;
	private SiButton btnLast;

	private JLabel LbTableHeaderText;
	private JComboBox<Integer> CboxList;

	private static ResponseProcessor rp;
	private static BookFetcher bf;
	public static ImageIcon icon = new ImageIcon(MainUI.class.getResource("/icon.png"));
	private final int rowsPerPage = 12;
	private int rowsCount;
	private int maxPageIndex;
	private int currentPageIndex = 1;
	private boolean TABLE_HAS_DATA = false;
	private ProcessingDialog processingDialog;

	public static void main(String[] args) throws IOException {
		bf = new BookFetcher();
		rp = new ResponseProcessor();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(icon.getImage());
		frame.setMinimumSize(new Dimension(680, 578));
		frame.setSize(680, 578);
		frame.setLocationRelativeTo(null);
		frame.setTitle("博客來書籍搜尋");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int marginRight = 30;
				int mfw = frame.getWidth();
				int nw = mfw - marginRight;

				InputPanel.setLocation((mfw - 655) / 2, 5);
				InputPanel.updateUI();
				TablePanel.setSize(nw, 453);
				TablePanel.updateUI();
			}
		});

		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微軟正黑體", Font.PLAIN, 16)));
		processingDialog = new ProcessingDialog("等待提示");

		InputPanel = new JPanel();
		InputPanel.setBounds(5, 5, 655, 75);
		frame.getContentPane().add(InputPanel);
		InputPanel.setLayout(null);

		LbKeyword = new JLabel("關鍵字");
		LbKeyword.setHorizontalAlignment(SwingConstants.LEFT);
		LbKeyword.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		LbKeyword.setBounds(5, 5, 70, 30);
		InputPanel.add(LbKeyword);

		LbItemNum = new JLabel("搜尋筆數");
		LbItemNum.setHorizontalAlignment(SwingConstants.LEFT);
		LbItemNum.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		LbItemNum.setBounds(370, 5, 70, 30);
		InputPanel.add(LbItemNum);

		TfKeyword = new JTextField();
		TfKeyword.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		TfKeyword.setBounds(5, 40, 350, 30);
		InputPanel.add(TfKeyword);
		TfKeyword.setColumns(10);

		TfItemNum = new JTextField();
		TfItemNum.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		TfItemNum.setBounds(370, 40, 100, 30);
		TfItemNum.setText("50");
		InputPanel.add(TfItemNum);
		TfItemNum.setColumns(10);
		TfItemNum.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = 0;
				if (TfItemNum.getText().matches("\\d+"))
					n = Integer.parseInt(TfItemNum.getText());

				int itemsNum = n - e.getWheelRotation();
				TfItemNum.setText(String.format("%d", itemsNum <= 0 ? 1 : itemsNum));
			}
		});

		BtnSubmit = new SiButton("搜尋", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnSubmit.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		BtnSubmit.setBounds(480, 40, 80, 30);
		InputPanel.add(BtnSubmit);
		BtnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (TABLE_HAS_DATA)
					clearAll();

				// check keyword empty
				String keyword;
				int itemsNum;
				if (TfKeyword.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "請輸入「關鍵字」！");
					return;
				}

				if (!TfItemNum.getText().matches("\\d+")) {
					JOptionPane.showMessageDialog(frame, "請輸入「搜尋筆數」，需為整數！");
					return;
				}

				keyword = TfKeyword.getText();
				itemsNum = Integer.parseInt(TfItemNum.getText());
				int fetchPage = (int) Math.ceil((double) itemsNum / 20);
				String bookscomtw = "http://search.books.com.tw/exep/prod_search.php";

				// start to fetch books and process the response
				processingDialog.setWorker(new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						rp.openf();
						processingDialog.setLocation(frame.getX() + (frame.getWidth() - processingDialog.getWidth()) / 2, frame.getY() + (frame.getHeight() - processingDialog.getHeight()) / 2);
						try {
							rp.setBookLimit(itemsNum);
							for (int i = 1; i <= fetchPage; i++) {
								bf.setRequest(bookscomtw);
								bf.appendfirst("cat", "BKA");
								bf.append("key", keyword);
								bf.append("page", i);
								BufferedReader tmp = bf.send();
								if (tmp == null)
									break;
								rp.process(tmp);
							}
						} catch (Exception e1) {
							System.out.println("MainUI.BtnSubmit.clicked:rp.process ERROR");
						}

						if (rp.getBooks().size() > 0) {
							DataTableModel.insertData(rp.getBooks());
							TABLE_HAS_DATA = true;
							// limit visible rows per page
							rowsCount = DataTableModel.getRowCount();
							int rowsPlusOne = rowsCount % rowsPerPage == 0 ? 0 : 1;
							maxPageIndex = rowsCount / rowsPerPage + rowsPlusOne;
							for (int i = 1; i <= maxPageIndex; i++)
								CboxList.addItem(i);
							DataTablePageManager();
							CboxList.setEnabled(true);
						} else {
							JOptionPane.showMessageDialog(frame, "找不到所查詢的「" + keyword + "」的資料！");
						}

						rp.closef();
						return null;
					}

					@Override
					protected void done() {
						processingDialog.dispose();
						processingDialog.setWorker(null);
						super.done();
					}
				});
				processingDialog.execute();
			}
		});

		BtnClear = new SiButton("清除", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		BtnClear.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		BtnClear.setBounds(570, 40, 80, 30);
		InputPanel.add(BtnClear);
		BtnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearAll();
			}
		});

		BtnRetcs = new SiButton("重設表格寬度", new Color(114, 114, 114), new Color(182, 182, 182), new Color(33, 150, 243), Color.white, new Color(25, 118, 210), new Color(3, 169, 244));
		BtnRetcs.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		BtnRetcs.setBounds(480, 5, 170, 30);
		InputPanel.add(BtnRetcs);
		BtnRetcs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// reset the JTable column width after a user adjust width of the columns
				for (int i = 0; i < 5; i++) {
					DataTable.getColumnModel().getColumn(i).setPreferredWidth(113);
				}
			}
		});

		TablePanel = new JPanel();
		TablePanel.setBounds(5, 87, 565, 453);
		TablePanel.setBorder(null);
		frame.getContentPane().add(TablePanel);
		GblTableLayout = new GridBagLayout();
		GblTableLayout.columnWidths = new int[] { 565, 0 };
		GblTableLayout.rowHeights = new int[] { 30, 393, 30, 0 };
		GblTableLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		GblTableLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		TablePanel.setLayout(GblTableLayout);

		LbTableHeaderText = new JLabel("  /  頁，共 本");
		gbc_LbTableHeaderText = new GridBagConstraints();
		gbc_LbTableHeaderText.fill = GridBagConstraints.BOTH;
		gbc_LbTableHeaderText.insets = new Insets(0, 0, 0, 0);
		gbc_LbTableHeaderText.gridx = 0;
		gbc_LbTableHeaderText.gridy = 0;
		gbc_LbTableHeaderText.weightx = 1.0;
		TablePanel.add(LbTableHeaderText, gbc_LbTableHeaderText);
		LbTableHeaderText.setHorizontalAlignment(SwingConstants.CENTER);
		LbTableHeaderText.setFont(new Font("微軟正黑體", Font.BOLD, 16));

		SpDataTable = new JScrollPane();
		gbc_SpDataTable = new GridBagConstraints();
		gbc_SpDataTable.fill = GridBagConstraints.BOTH;
		gbc_SpDataTable.insets = new Insets(0, 0, 0, 0);
		gbc_SpDataTable.gridx = 0;
		gbc_SpDataTable.gridy = 1;
		gbc_SpDataTable.weightx = 1.0;
		TablePanel.add(SpDataTable, gbc_SpDataTable);

		DataTableModel = new BookTableModel();
		DataTableSorter = new TableRowSorter<TableModel>(DataTableModel) {
			@Override
			public void toggleSortOrder(int column) {
				List<? extends SortKey> sortKeys = getSortKeys();
				if (sortKeys.size() > 0) {
					// if sort key is descending, set sort key to null, then the column is unsorted
					if (sortKeys.get(0).getSortOrder() == SortOrder.DESCENDING) {
						setSortKeys(null);
						return;
					}
				}
				super.toggleSortOrder(column);
			}
		};
		DataTable = new JTable();
		DataTable.setBorder(null);
		DataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DataTable.setModel(DataTableModel);
		DataTable.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 16));
		DataTable.getTableHeader().setPreferredSize(new Dimension(113, 30));
		DataTable.getTableHeader().setBackground(new Color(56, 142, 60));
		DataTable.getTableHeader().setForeground(Color.white);
		DataTable.setDefaultRenderer(Object.class, new BookTableCellRender());
		DataTable.getTableHeader().setReorderingAllowed(false);
		DataTable.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		DataTable.setRowSorter(DataTableSorter);
		SpDataTable.setViewportView(DataTable);

		TableBottomPanel = new JPanel();
		gbc_TableBottomPanel = new GridBagConstraints();
		gbc_TableBottomPanel.fill = GridBagConstraints.BOTH;
		gbc_TableBottomPanel.insets = new Insets(0, 0, 0, 0);
		gbc_TableBottomPanel.gridx = 0;
		gbc_TableBottomPanel.gridy = 2;
		gbc_TableBottomPanel.weightx = 1.0;
		TablePanel.add(TableBottomPanel, gbc_TableBottomPanel);

		btnFirst = new SiButton("|<", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnFirst.setFont(new Font("New Time Roman", Font.BOLD, 14));
		btnFirst.setPreferredSize(new Dimension(40, 25));
		TableBottomPanel.add(btnFirst);
		btnFirst.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentPageIndex = 1;
				DataTablePageManager();
			}
		});

		btnPrev = new SiButton("<", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnPrev.setFont(new Font("New Time Roman", Font.BOLD, 14));
		btnPrev.setPreferredSize(new Dimension(40, 25));
		TableBottomPanel.add(btnPrev);
		btnPrev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentPageIndex > 1)
					currentPageIndex--;
				DataTablePageManager();
			}
		});

		DefaultListCellRenderer CboxListRender = new DefaultListCellRenderer();
		CboxListRender.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		CboxList = new JComboBox<Integer>();
		CboxList.setPreferredSize(new Dimension(50, 25));
		CboxList.setFont(new Font("New Time Roman", Font.BOLD, 14));
		CboxList.setRenderer(CboxListRender);
		TableBottomPanel.add(CboxList);
		CboxList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					currentPageIndex = (int) item;
					DataTablePageManager();
				}
			}
		});

		btnNext = new SiButton(">", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnNext.setFont(new Font("New Time Roman", Font.BOLD, 14));
		btnNext.setPreferredSize(new Dimension(40, 25));
		TableBottomPanel.add(btnNext);
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentPageIndex < maxPageIndex)
					currentPageIndex++;
				DataTablePageManager();
			}
		});

		btnLast = new SiButton(">|", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnLast.setFont(new Font("New Time Roman", Font.BOLD, 14));
		btnLast.setPreferredSize(new Dimension(40, 25));
		TableBottomPanel.add(btnLast);
		btnLast.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentPageIndex = maxPageIndex;
				DataTablePageManager();
			}
		});

		btnFirst.setEnabled(false);
		btnPrev.setEnabled(false);
		btnNext.setEnabled(false);
		btnLast.setEnabled(false);
		CboxList.setEnabled(false);
	}

	public void DataTablePageManager() {
		if (rp.getBooks().size() > 0) {
			DataTable.getSelectionModel().clearSelection();
			DataTableSorter.setRowFilter(new RowFilter<TableModel, Integer>() {
				@Override
				public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
					// low is a minimum scale of a page
					int low = currentPageIndex - 1;
					// up is the maximum number of entries
					int up = entry.getIdentifier();
					return low * rowsPerPage <= up && up < low * rowsPerPage + rowsPerPage;
				}
			});
			btnFirst.setEnabled(currentPageIndex > 1);
			btnPrev.setEnabled(currentPageIndex > 1);
			btnNext.setEnabled(currentPageIndex < maxPageIndex);
			btnLast.setEnabled(currentPageIndex < maxPageIndex);
			LbTableHeaderText.setText(String.format("%d / %d 頁，共 %d 本", currentPageIndex, maxPageIndex, rp.getBooks().size()));
			CboxList.setSelectedIndex(currentPageIndex - 1);
		}
	}

	public void clearAll() {
		TABLE_HAS_DATA = false;
		rp.getBooks().clear();
		DataTableModel.removeAll();
		currentPageIndex = 1;
		LbTableHeaderText.setText("  /  頁，共 本");
		CboxList.removeAllItems();
		DataTableSorter.setSortKeys(null);

		btnFirst.setEnabled(false);
		btnPrev.setEnabled(false);
		btnNext.setEnabled(false);
		btnLast.setEnabled(false);
		CboxList.setEnabled(false);
	}
}
