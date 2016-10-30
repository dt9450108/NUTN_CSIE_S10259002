import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class MainUI {
	// check the student's ID using a regular expression
	public static String STU_NUM_REGEX = "([Ss]{1}[\\d]{8})";

	// get the index of the sports and courses
	public static String[] SPORTS = { "Basketball", "Tennis", "Volleyball", "Soccer" };
	public static String[] COURSE = { "C++", "Python", "Java", "Swift" };

	// set the system button mode
	public static int ADD_MODE = 0;
	public static int MODIFY_MODE = 1;
	public static int DELETE_MODE = 2;
	public static int CURRENT_MODE = ADD_MODE;

	// check if the clear button is triggered
	public static boolean CLEAR_TRIGGER;

	// record the student index in the Manager.Studs and the row number in the Table
	private int foundStuIdx;
	private int foundStuTableRow;
	private SearchWindow sw;

	private JFrame frmStudentData;

	private JPanel OpPanel;
	private JPanel FunctPanel;
	private JPanel DataPanel;
	private JPanel ErrMsgPanel;

	private JLabel LbName;
	private JLabel LbStuNum;
	private JLabel LbSport;
	private JLabel LbCourse;
	private JLabel LbErrMsg;

	private JRadioButton RbBasketball;
	private JRadioButton RbTennis;
	private JRadioButton RbVolleyball;
	private JRadioButton RbSoccer;
	private JRadioButton RbCplusplus;
	private JRadioButton RbJava;
	private JRadioButton RbPython;
	private JRadioButton RbSwift;
	private final ButtonGroup btnGroupSport = new ButtonGroup();
	private final ButtonGroup btnGroupCourse = new ButtonGroup();

	private SiButton btnClear;
	private SiButton btnAdd;
	private SiButton btnModify;
	private SiButton btnDelete;
	private SiButton btnSearch;
	private SiButton btnTbReset;
	private JTextField TfName;
	private JTextField TfStuNum;

	private JScrollPane SpStuTable;
	private JTable TbStudData;

	private Manager StudsManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					MainUI.CLEAR_TRIGGER = false;
					window.frmStudentData.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		StudsManager = new Manager("students.dat");
		StudsManager.readDatas(null);
		Student.count = StudsManager.maxId();
		sw = new SearchWindow();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStudentData = new JFrame();
		frmStudentData.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int marginRight = 14;
				int marginBottom = 40;
				int mfw = frmStudentData.getWidth();
				int mfh = frmStudentData.getHeight();
				DataPanel.setSize((mfw - marginRight - 320), (mfh - marginBottom - 53));
				DataPanel.updateUI();
			}
		});
		URL url = MainUI.class.getResource("/icon.png");
		ImageIcon icon = new ImageIcon(url);
		frmStudentData.setIconImage(icon.getImage());
		frmStudentData.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		frmStudentData.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Save the students' data when the program is closing
				StudsManager.storeDatas();
			}
		});
		frmStudentData.setTitle("Students Information");
		//		frmStudentData.setResizable(false);
		frmStudentData.setMinimumSize(new Dimension(1200, 460));
		frmStudentData.setSize(1200, 460);
		frmStudentData.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStudentData.setLocationRelativeTo(null);
		frmStudentData.getContentPane().setLayout(null);
		frmStudentData.getContentPane().setBackground(Color.decode("#ecf0f1"));

		OpPanel = new JPanel();
		OpPanel.setBorder(new LineBorder(new Color(128, 128, 128)));
		OpPanel.setBackground(Color.white);
		OpPanel.setBounds(6, 6, 300, 365);
		frmStudentData.getContentPane().add(OpPanel);
		OpPanel.setLayout(null);

		JLabel LbInputHint = new JLabel("——————    Input    ——————");
		LbInputHint.setHorizontalAlignment(SwingConstants.CENTER);
		LbInputHint.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		LbInputHint.setBounds(6, 6, 286, 30);
		OpPanel.add(LbInputHint);

		LbStuNum = new JLabel("Student ID");
		LbStuNum.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		LbStuNum.setBounds(6, 41, 130, 30);
		OpPanel.add(LbStuNum);
		LbStuNum.setHorizontalAlignment(SwingConstants.CENTER);

		LbName = new JLabel("Name");
		LbName.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		LbName.setBounds(6, 81, 130, 30);
		OpPanel.add(LbName);
		LbName.setHorizontalAlignment(SwingConstants.CENTER);

		LbSport = new JLabel("Sport");
		LbSport.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		LbSport.setBounds(6, 121, 130, 30);
		OpPanel.add(LbSport);
		LbSport.setHorizontalAlignment(SwingConstants.CENTER);

		LbCourse = new JLabel("Course");
		LbCourse.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		LbCourse.setBounds(150, 121, 130, 30);
		OpPanel.add(LbCourse);
		LbCourse.setHorizontalAlignment(SwingConstants.CENTER);

		TfName = new JTextField();
		TfName.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		TfName.setBounds(150, 81, 130, 30);
		TfName.setBackground(Color.white);
		TfName.setForeground(Color.BLACK);
		OpPanel.add(TfName);
		TfName.setColumns(10);

		TfStuNum = new JTextField();
		TfStuNum.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		TfStuNum.setBounds(150, 41, 130, 30);
		TfStuNum.setBackground(Color.white);
		TfStuNum.setForeground(Color.BLACK);
		OpPanel.add(TfStuNum);
		TfStuNum.setColumns(10);

		RbBasketball = new JRadioButton("Basketball");
		RbBasketball.setBackground(Color.WHITE);
		btnGroupSport.add(RbBasketball);
		RbBasketball.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbBasketball.setBounds(22, 161, 130, 30);
		OpPanel.add(RbBasketball);

		RbTennis = new JRadioButton("Tennis");
		RbTennis.setBackground(Color.WHITE);
		btnGroupSport.add(RbTennis);
		RbTennis.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbTennis.setBounds(22, 241, 130, 30);
		OpPanel.add(RbTennis);

		RbVolleyball = new JRadioButton("Volleyball");
		RbVolleyball.setBackground(Color.WHITE);
		btnGroupSport.add(RbVolleyball);
		RbVolleyball.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbVolleyball.setBounds(22, 201, 130, 30);
		OpPanel.add(RbVolleyball);

		RbSoccer = new JRadioButton("Soccer");
		RbSoccer.setBackground(Color.WHITE);
		btnGroupSport.add(RbSoccer);
		RbSoccer.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbSoccer.setBounds(22, 281, 130, 30);
		OpPanel.add(RbSoccer);

		RbCplusplus = new JRadioButton("C++");
		RbCplusplus.setBackground(Color.WHITE);
		btnGroupCourse.add(RbCplusplus);
		RbCplusplus.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbCplusplus.setBounds(162, 161, 130, 30);
		OpPanel.add(RbCplusplus);

		RbPython = new JRadioButton("Python");
		RbPython.setBackground(Color.WHITE);
		btnGroupCourse.add(RbPython);
		RbPython.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbPython.setBounds(162, 201, 130, 30);
		OpPanel.add(RbPython);

		RbJava = new JRadioButton("Java");
		RbJava.setBackground(Color.WHITE);
		btnGroupCourse.add(RbJava);
		RbJava.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbJava.setBounds(162, 241, 130, 30);
		OpPanel.add(RbJava);

		RbSwift = new JRadioButton("Swift");
		RbSwift.setBackground(Color.WHITE);
		btnGroupCourse.add(RbSwift);
		RbSwift.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		RbSwift.setBounds(162, 281, 130, 30);
		OpPanel.add(RbSwift);

		// btnAdd is a changeable functional button
		btnAdd = new SiButton("Add to the list", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnAdd.setBounds(6, 319, 286, 40);
		OpPanel.add(btnAdd);
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if not find the student in MODIFY_MODE or DELETE_MODE, user couldn't use the btnAdd function
				// but if CURRENT_MODE is ADD_MODE, user could add a new student data
				if (foundStuIdx != -1 || CURRENT_MODE == ADD_MODE)
					writeInfo(foundStuIdx, foundStuTableRow);
			}
		});

		DataPanel = new JPanel();
		DataPanel.setBackground(Color.WHITE);
		DataPanel.setBorder(null);
		DataPanel.setBounds(320, 53, 866, 367);
		frmStudentData.getContentPane().add(DataPanel);
		GridBagLayout gbl_DataPanel = new GridBagLayout();
		gbl_DataPanel.columnWidths = new int[] { 0, 0 };
		gbl_DataPanel.rowHeights = new int[] { 0, 0 };
		gbl_DataPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_DataPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		DataPanel.setLayout(gbl_DataPanel);

		SpStuTable = new JScrollPane();
		GridBagConstraints gbc_SpStuTable = new GridBagConstraints();
		gbc_SpStuTable.fill = GridBagConstraints.BOTH;
		gbc_SpStuTable.gridx = 0;
		gbc_SpStuTable.gridy = 0;
		DataPanel.add(SpStuTable, gbc_SpStuTable);

		TbStudData = new JTable();
		TbStudData.setBorder(new LineBorder(Color.GRAY));
		TbStudData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TbStudData.setModel(new StuTableModel(StudsManager.getStuds()));
		TbStudData.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
		TbStudData.getTableHeader().setPreferredSize(new Dimension(122, 30));
		TbStudData.setDefaultRenderer(Object.class, new StuTableCellRender());
		TbStudData.getColumnModel().getColumn(4).setPreferredWidth(0);
		TbStudData.getColumnModel().getColumn(4).setMinWidth(0);
		TbStudData.getColumnModel().getColumn(4).setMaxWidth(0);
		TbStudData.getTableHeader().setReorderingAllowed(false);
		TbStudData.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		TbStudData.addMouseListener(new MouseAdapter() {
			// prevent repeatedly setStudentInfo();
			private int previousRow = -1;
			private int currentRow = -1;

			public void mouseClicked(MouseEvent e) {
				if (CURRENT_MODE == ADD_MODE) {
					if (CLEAR_TRIGGER) {
						previousRow = -1;
						currentRow = -1;
						CLEAR_TRIGGER = !CLEAR_TRIGGER;
					}
					currentRow = TbStudData.getSelectedRow();
					if (currentRow > -1 && currentRow != previousRow) {
						previousRow = currentRow;
						int stuIdx = StudsManager.search((int) TbStudData.getValueAt(currentRow, 4));
						setStudentInfo(StudsManager.getStuds().get(stuIdx));
					}
				}
			}
		});
		SpStuTable.setViewportView(TbStudData);

		// show a bad operation message
		ErrMsgPanel = new JPanel();
		ErrMsgPanel.setBorder(new LineBorder(Color.GRAY));
		ErrMsgPanel.setBackground(Color.WHITE);
		ErrMsgPanel.setBounds(6, 380, 300, 40);
		frmStudentData.getContentPane().add(ErrMsgPanel);
		ErrMsgPanel.setLayout(new GridLayout(1, 0, 0, 0));

		LbErrMsg = new JLabel("");
		LbErrMsg.setForeground(Color.RED);
		LbErrMsg.setHorizontalAlignment(SwingConstants.CENTER);
		LbErrMsg.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		ErrMsgPanel.add(LbErrMsg);

		FunctPanel = new JPanel();
		FunctPanel.setBounds(320, 6, 866, 41);
		frmStudentData.getContentPane().add(FunctPanel);
		FunctPanel.setLayout(new GridLayout(0, 5, 0, 0));

		// clear the all text and selected option from the input panel
		btnClear = new SiButton("Clear", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		FunctPanel.add(btnClear);
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// first true is to show the cleared message, second is to reset the btnAdd function
				clearInfo(true, true);
			}
		});

		btnSearch = new SiButton("Search", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// open the search window and set the CURRENT_MODE to ADD_MODE (default)
				sw.open();
				CURRENT_MODE = ADD_MODE;
			}
		});
		FunctPanel.add(btnSearch);

		btnModify = new SiButton("Modify", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnModify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// clearing the input panel text in TextField,
				// opening the search window,
				// and setting CURRENT_MODE to MODIFY_MODE
				clearInfo(false, false);
				sw.open();
				CURRENT_MODE = MODIFY_MODE;
			}
		});
		FunctPanel.add(btnModify);

		btnDelete = new SiButton("Delete", new Color(114, 114, 114), new Color(182, 182, 182), new Color(76, 175, 80), Color.white, new Color(56, 142, 60), new Color(139, 195, 74));
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// clearing the input panel text in TextField,
				// opening the search window,
				// and setting CURRENT_MODE to DELETE_MODE
				clearInfo(false, false);
				sw.open();
				CURRENT_MODE = DELETE_MODE;
			}
		});
		FunctPanel.add(btnDelete);

		btnTbReset = new SiButton("Column Resize", new Color(114, 114, 114), new Color(182, 182, 182), new Color(33, 150, 243), Color.white, new Color(25, 118, 210), new Color(3, 169, 244));
		FunctPanel.add(btnTbReset);
		btnTbReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// reset the JTable column width after a user adjust width of the columns
				for (int i = 0; i < 4; i++) {
					TbStudData.getColumnModel().getColumn(i).setPreferredWidth(216);
				}
			}
		});

		sw.getBtnQuery().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// the query button event in search window
				searchStudent();
			}
		});

		sw.getBtnCancel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// the cancel button event in search window
				// close the search window
				sw.close();

				// cancel the search operation, set the btnAdd button to the default
				btnAdd.setText("Add to the list");
				CURRENT_MODE = ADD_MODE;
			}
		});
	}

	// write the student data
	// table Row is the student selected which in the JTable position (row)
	// stuIdx is the student found index in the Manager.Studs list
	public void writeInfo(int tableRow, int stuIdx) {
		if (CURRENT_MODE != DELETE_MODE) {
			LbErrMsg.setForeground(Color.red);

			String sNum = TfStuNum.getText();
			if (!sNum.matches(STU_NUM_REGEX)) {
				LbErrMsg.setText("Student Number is not correct.");
				return;
			}

			String sName = TfName.getText();
			if (sName == null || sName.length() == 0) {
				LbErrMsg.setText("Name is not entered.");
				return;
			}

			JRadioButton jrbs = getSelbtnFrombtnGroup(btnGroupSport);
			int sSport = (jrbs == null) ? -1 : MainUI.SportStr(jrbs.getText());
			if (sSport == -1) {
				LbErrMsg.setText("Not select any sport.");
				return;
			}

			JRadioButton jrbc = getSelbtnFrombtnGroup(btnGroupCourse);
			int sCourse = (jrbc == null) ? -1 : MainUI.CourseStr(jrbc.getText());
			if (sCourse == -1) {
				LbErrMsg.setText("Not select any course.");
				return;
			}
			System.out.printf("%s, %s, %d, %d\n", sNum, sName, sSport, sCourse);

			if (CURRENT_MODE == ADD_MODE) {
				if (StudsManager.add(sNum, sName, sSport, sCourse)) {
					Student stu = StudsManager.getStuds().lastElement();
					StuTableModel stm = (StuTableModel) TbStudData.getModel();
					stm.insertData(stu);
					TbStudData.getSelectionModel().setSelectionInterval(stm.getRowCount() - 1, stm.getRowCount() - 1);
					TbStudData.scrollRectToVisible(TbStudData.getCellRect(stm.getRowCount() - 1, 0, true));

					LbErrMsg.setForeground(new Color(56, 142, 60));
					LbErrMsg.setText("Add a student to the list.");
				} else {
					LbErrMsg.setForeground(Color.red);
					LbErrMsg.setText("Could not add same \"Name\" or \"ID\".");
				}
			} else if (CURRENT_MODE == MODIFY_MODE) {
				// MODIFY_MODE, check if no change the student data found
				if (StudsManager.getStuds().get(stuIdx).fequals(sNum, sName, sSport, sCourse)) {
					LbErrMsg.setForeground(Color.red);
					LbErrMsg.setText("No change.");
					btnAdd.setText("Add to the list");
					CURRENT_MODE = ADD_MODE;
				} else {
					// modify the stundent data and update the information to Manger.Studs and the JTable
					if (StudsManager.edit(stuIdx, sNum, sName, sSport, sCourse)) {
						StuTableModel stm = (StuTableModel) TbStudData.getModel();
						stm.setAllValueAt(StudsManager.getStuds().get(stuIdx), tableRow);

						// highlight the selected student row in the JTable
						TbStudData.getSelectionModel().setSelectionInterval(tableRow, tableRow);
						TbStudData.scrollRectToVisible(TbStudData.getCellRect(tableRow, 0, true));

						LbErrMsg.setForeground(new Color(56, 142, 60));
						LbErrMsg.setText("Modified a student from the list.");
						btnAdd.setText("Add to the list");
						CURRENT_MODE = ADD_MODE;
					} else {
						LbErrMsg.setForeground(Color.red);
						LbErrMsg.setText("There is a same \"Name\" or \"ID\".");
					}
				}
			}
		} else {
			// DELETE_MODE, delete a student in the system from the Manager.Studs and JTable
			if (tableRow != -1) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Delete this student data?", "Warning", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					StuTableModel stm = (StuTableModel) TbStudData.getModel();
					stm.removeRow(tableRow);
					StudsManager.delete(stuIdx);
					clearInfo(false, false);
					LbErrMsg.setForeground(new Color(56, 142, 60));
					LbErrMsg.setText("Delete a student.");
				} else {
					LbErrMsg.setForeground(new Color(56, 142, 60));
					LbErrMsg.setText("Cancel deleting.");
				}

				btnAdd.setText("Add to the list");
				CURRENT_MODE = ADD_MODE;
			}
		}
	}

	public void searchStudent() {
		String s = sw.getTfQueryText().getText();
		int found = StudsManager.search(s, s);
		if (found == -1) {
			LbErrMsg.setForeground(Color.red);
			LbErrMsg.setText("Not found!");
			clearInfo(false, false);
			foundStuIdx = -1;
			foundStuTableRow = -1;
		} else {
			LbErrMsg.setForeground(new Color(56, 142, 60));
			LbErrMsg.setText("Found!");

			Student stu = StudsManager.getStuds().get(found);
			int row = getIdRowFromTable(stu.getId());
			setStudentInfo(stu);
			TbStudData.getSelectionModel().setSelectionInterval(row, row);
			TbStudData.scrollRectToVisible(TbStudData.getCellRect(row, 0, true));

			foundStuIdx = found;
			foundStuTableRow = row;

			// According to the CURRENT_MODE, btnAdd would be change the functional text
			switch (CURRENT_MODE) {
				case 0: // add
					btnAdd.setText("Add to the list");
					break;
				case 1: // modify
					sw.close();
					btnAdd.setText("Modify");
					break;
				case 2: // delete
					sw.close();
					btnAdd.setText("Delete");
					break;
			}
		}
	}

	// set a student information to the input panel
	public void setStudentInfo(Student stu) {
		TfStuNum.setText(stu.getNum());
		TfName.setText(stu.getName());
		setSelbtnFrombtnGroup(btnGroupSport, stu.getSport());
		setSelbtnFrombtnGroup(btnGroupCourse, stu.getCourse());
	}

	public void clearInfo(boolean msgVisible, boolean cmode) {
		if (cmode) {
			CURRENT_MODE = ADD_MODE;
		}
		btnAdd.setText("Add to the list");
		TfName.setText("");
		TfStuNum.setText("");
		btnGroupSport.clearSelection();
		btnGroupCourse.clearSelection();
		TbStudData.getSelectionModel().clearSelection();
		CLEAR_TRIGGER = !CLEAR_TRIGGER;

		if (msgVisible) {
			LbErrMsg.setForeground(Color.blue);
			LbErrMsg.setText("Clear input panel!");
		}
	}

	public static int SportStr(String s) {
		char t = s.charAt(0);
		switch (t) {
			case 'B':
				return 0;
			case 'T':
				return 1;
			case 'V':
				return 2;
			case 'S':
				return 3;
			default:
				return -1;
		}
	}

	public static int CourseStr(String s) {
		char t = s.charAt(0);
		switch (t) {
			case 'C':
				return 0;
			case 'P':
				return 1;
			case 'J':
				return 2;
			case 'S':
				return 3;
			default:
				return -1;
		}
	}

	public JRadioButton getSelbtnFrombtnGroup(ButtonGroup bg) {
		for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return (JRadioButton) button;
			}
		}
		return null;
	}

	// set selection of a radio button according to the item which is the index of a radio button
	public void setSelbtnFrombtnGroup(ButtonGroup bg, int item) {
		int i = 0;
		for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (item == i) {
				button.setSelected(true);
			}
			i++;
		}
	}

	public int getIdRowFromTable(int id) {
		for (int i = 0; i < TbStudData.getRowCount(); i++) {
			if (id == (int) TbStudData.getValueAt(i, 4))
				return i;
		}
		return -1;
	}

	public Manager getStudsManager() {
		return this.StudsManager;
	}

	public void setStudsManager(Manager studsManager) {
		this.StudsManager = studsManager;
	}

	public SearchWindow getSw() {
		return this.sw;
	}

	public void setSw(SearchWindow sw) {
		this.sw = sw;
	}
}
