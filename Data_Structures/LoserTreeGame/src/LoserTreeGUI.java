import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoserTreeGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblA;
	private JLabel lblB;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;

	private int[] listOfA;
	private int[] listOfB;
	private int timesPressOfA;
	private int timesPressOfB;
	private int[] loserTreeList;
	private LoserTree loserTree;

	private JTextField FirstOutput;
	private JLabel lblFirstOutput;
	private JTextField SecondOutput;
	private JLabel lblSecondOutput;
	private static Random randNumber = new Random();
	private JLabel lblProcess;
	private JButton btnA;
	private JButton btnB;
	private JButton btnClear;
	private JTextField[] textFieldOfA;
	private JTextField[] textFieldOfB;
	private JScrollPane scrollPane;
	private JTextArea txtrOutputdisplay;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoserTreeGUI window = new LoserTreeGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoserTreeGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		randNumber.setSeed(randNumber.nextInt());
		loserTree = new LoserTree(8);
		listOfA = new int[5];
		listOfB = new int[5];
		loserTreeList = new int[8];

		frame = new JFrame();
		frame.setBounds(100, 100, 980, 468);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(6, 6, 653, 229);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setEditable(false);
		textField.setBounds(295, 6, 70, 28);
		panel.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(295, 46, 70, 28);
		panel.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(130, 95, 70, 28);
		panel.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(459, 95, 70, 28);
		panel.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(48, 120, 70, 28);
		panel.add(textField_4);

		textField_5 = new JTextField();
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setEditable(false);
		textField_5.setColumns(10);
		textField_5.setBounds(212, 120, 70, 28);
		panel.add(textField_5);

		textField_6 = new JTextField();
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setEditable(false);
		textField_6.setColumns(10);
		textField_6.setBounds(377, 120, 70, 28);
		panel.add(textField_6);

		textField_7 = new JTextField();
		textField_7.setHorizontalAlignment(SwingConstants.CENTER);
		textField_7.setEditable(false);
		textField_7.setColumns(10);
		textField_7.setBounds(541, 120, 70, 28);
		panel.add(textField_7);

		textField_8 = new JTextField();
		textField_8.setHorizontalAlignment(SwingConstants.CENTER);
		textField_8.setEditable(false);
		textField_8.setColumns(10);
		textField_8.setBounds(6, 160, 70, 28);
		panel.add(textField_8);

		textField_9 = new JTextField();
		textField_9.setHorizontalAlignment(SwingConstants.CENTER);
		textField_9.setEditable(false);
		textField_9.setColumns(10);
		textField_9.setBounds(88, 160, 70, 28);
		panel.add(textField_9);

		textField_10 = new JTextField();
		textField_10.setHorizontalAlignment(SwingConstants.CENTER);
		textField_10.setEditable(false);
		textField_10.setColumns(10);
		textField_10.setBounds(170, 160, 70, 28);
		panel.add(textField_10);

		textField_11 = new JTextField();
		textField_11.setHorizontalAlignment(SwingConstants.CENTER);
		textField_11.setEditable(false);
		textField_11.setColumns(10);
		textField_11.setBounds(252, 160, 70, 28);
		panel.add(textField_11);

		textField_12 = new JTextField();
		textField_12.setHorizontalAlignment(SwingConstants.CENTER);
		textField_12.setEditable(false);
		textField_12.setColumns(10);
		textField_12.setBounds(334, 160, 70, 28);
		panel.add(textField_12);

		textField_13 = new JTextField();
		textField_13.setHorizontalAlignment(SwingConstants.CENTER);
		textField_13.setEditable(false);
		textField_13.setColumns(10);
		textField_13.setBounds(416, 160, 70, 28);
		panel.add(textField_13);

		textField_14 = new JTextField();
		textField_14.setHorizontalAlignment(SwingConstants.CENTER);
		textField_14.setEditable(false);
		textField_14.setColumns(10);
		textField_14.setBounds(498, 160, 70, 28);
		panel.add(textField_14);

		textField_15 = new JTextField();
		textField_15.setHorizontalAlignment(SwingConstants.CENTER);
		textField_15.setEditable(false);
		textField_15.setColumns(10);
		textField_15.setBounds(580, 160, 70, 28);
		panel.add(textField_15);

		lblA = new JLabel("A");
		lblA.setHorizontalAlignment(SwingConstants.CENTER);
		lblA.setBounds(6, 189, 61, 16);
		panel.add(lblA);

		lblB = new JLabel("B");
		lblB.setHorizontalAlignment(SwingConstants.CENTER);
		lblB.setBounds(88, 189, 61, 16);
		panel.add(lblB);

		label = new JLabel("B");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(252, 189, 61, 16);
		panel.add(label);

		label_1 = new JLabel("A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(170, 189, 61, 16);
		panel.add(label_1);

		label_2 = new JLabel("B");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(416, 189, 61, 16);
		panel.add(label_2);

		label_3 = new JLabel("A");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(334, 189, 61, 16);
		panel.add(label_3);

		label_4 = new JLabel("A");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(498, 189, 61, 16);
		panel.add(label_4);

		label_5 = new JLabel("B");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(580, 189, 61, 16);
		panel.add(label_5);

		FirstOutput = new JTextField();
		FirstOutput.setHorizontalAlignment(SwingConstants.CENTER);
		FirstOutput.setEditable(false);
		FirstOutput.setColumns(10);
		FirstOutput.setBounds(113, 6, 70, 28);
		panel.add(FirstOutput);

		lblFirstOutput = new JLabel("First Output :");
		lblFirstOutput.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstOutput.setBounds(6, 12, 112, 16);
		panel.add(lblFirstOutput);

		SecondOutput = new JTextField();
		SecondOutput.setHorizontalAlignment(SwingConstants.CENTER);
		SecondOutput.setEditable(false);
		SecondOutput.setColumns(10);
		SecondOutput.setBounds(113, 40, 70, 28);
		panel.add(SecondOutput);

		lblSecondOutput = new JLabel("Second Output :");
		lblSecondOutput.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecondOutput.setBounds(6, 46, 112, 16);
		panel.add(lblSecondOutput);

		panel_1 = new JPanel();
		panel_1.setBounds(663, 6, 309, 229);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		lblProcess = new JLabel("Process");
		lblProcess.setBounds(6, 6, 61, 16);
		panel_1.add(lblProcess);

		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(0, 25, 309, 204);
		panel_1.add(scrollPane);

		txtrOutputdisplay = new JTextArea();
		txtrOutputdisplay.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		txtrOutputdisplay.setEditable(false);
		txtrOutputdisplay.setLineWrap(true);
		scrollPane.setViewportView(txtrOutputdisplay);

		panel_2 = new JPanel();
		panel_2.setBounds(6, 242, 966, 198);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		btnA = new JButton("A");
		btnA.setFont(new Font("Lucida Grande", Font.PLAIN, 70));
		btnA.setForeground(new Color(0, 0, 0));
		btnA.setBackground(Color.WHITE);
		btnA.setBounds(6, 6, 477, 93);
		panel_2.add(btnA);

		btnB = new JButton("B");
		btnB.setFont(new Font("Lucida Grande", Font.PLAIN, 70));
		btnB.setBackground(Color.WHITE);
		btnB.setBounds(483, 6, 477, 93);
		panel_2.add(btnB);

		btnClear = new JButton("Clear");
		btnClear.setFocusable(false);
		btnClear.setFont(new Font("Lucida Grande", Font.PLAIN, 70));
		btnClear.setBounds(6, 99, 954, 93);
		panel_2.add(btnClear);

		textFieldOfA = new JTextField[4];
		textFieldOfA[0] = textField_8;
		textFieldOfA[1] = textField_10;
		textFieldOfA[2] = textField_12;
		textFieldOfA[3] = textField_14;

		textFieldOfB = new JTextField[4];
		textFieldOfB[0] = textField_9;
		textFieldOfB[1] = textField_11;
		textFieldOfB[2] = textField_13;
		textFieldOfB[3] = textField_15;

		txtrOutputdisplay.append("Press A and B four times respectively\n");

		//	========================================================================	event listener start

		//		========================================================================	button A click event
		btnA.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnA.isEnabled()) {
					if (timesPressOfA < 5) {
						int numberFromA = randNumber.nextInt(100) + 1;

						txtrOutputdisplay.append("Press A produce number : " + numberFromA + "\n");

						if (timesPressOfA < 4)
							textFieldOfA[timesPressOfA].setText(String.format("%d", numberFromA));
						listOfA[timesPressOfA++] = numberFromA;
					}

					if (timesPressOfA == 4)
						btnA.setEnabled(false);

					if (timesPressOfA == 5) {
						btnA.setEnabled(false);
						int maxNumberIndex = findMaxNumIndex(loserTree.getList(), loserTree.getList()[0], 8, loserTree.getList().length);
						loserTree.newSort(maxNumberIndex, listOfA[4]);
						int currTF = (int) (maxNumberIndex - 8) / 2;
						textFieldOfA[currTF].setText(String.format("%d", listOfA[4]));
						textFieldOfA[currTF].setBackground(Color.cyan);
						printLoserTree(8, loserTree.getList(), 2);
					}

					if (timesPressOfB == 4 && timesPressOfA == 4)
						startGame();
				}
			}
		});

		//		========================================================================	button B click event
		btnB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnB.isEnabled()) {
					if (timesPressOfB < 5) {
						int numberFromB = randNumber.nextInt(100) + 1;

						txtrOutputdisplay.append("Press B produce number : " + numberFromB + "\n");

						if (timesPressOfB < 4)
							textFieldOfB[timesPressOfB].setText(String.format("%d", numberFromB));
						listOfB[timesPressOfB++] = numberFromB;
					}

					if (timesPressOfB == 4)
						btnB.setEnabled(false);

					if (timesPressOfB == 5) {
						btnB.setEnabled(false);
						int maxNumberIndex = findMaxNumIndex(loserTree.getList(), loserTree.getList()[0], 8, loserTree.getList().length);
						loserTree.newSort(maxNumberIndex, listOfB[4]);
						int currTF = (int) ((maxNumberIndex - 8) - 1) / 2;
						textFieldOfB[currTF].setText(String.format("%d", listOfB[4]));
						textFieldOfB[currTF].setBackground(Color.cyan);
						printLoserTree(8, loserTree.getList(), 2);
					}

					if (timesPressOfB == 4 && timesPressOfA == 4)
						startGame();
				}
			}
		});

		//		========================================================================	button clear click event
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//		clear output
				FirstOutput.setText("");
				SecondOutput.setText("");

				//		clear trace text area
				txtrOutputdisplay.setText("*****  Cleared  *****\n");
				txtrOutputdisplay.append("Press A and B four times respectively\n");

				//		clear textfield
				for (int i = 0; i < panel.getComponentCount(); i++) {
					if (panel.getComponent(i) instanceof JTextField) {
						JTextField clearTemp = (JTextField) panel.getComponent(i);
						clearTemp.setText("");
						clearTemp.setBackground(Color.white);
					}
				}

				//		clear array;
				timesPressOfA = 0;
				timesPressOfB = 0;

				//		button enable true
				btnA.setEnabled(true);
				btnB.setEnabled(true);

				//		update ui
				panel.updateUI();
				panel_1.updateUI();
				panel_2.updateUI();
			}
		});
		//	========================================================================	event listener end
	}

	private void sendAlertMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}

	private void startGame() {
		txtrOutputdisplay.append("*** Loser Tree has built ***\n");

		for (int i = 0; i < 4; i++) {
			loserTreeList[i * 2] = listOfA[i];
			loserTreeList[i * 2 + 1] = listOfB[i];
		}
		loserTree.constructLoserTree(loserTreeList);

		printLoserTree(8, loserTree.getList(), 1);

		int[] LTList = loserTree.getList();
		int maxNumberIndex = findMaxNumIndex(LTList, LTList[0], 8, LTList.length);
		int listIndex = maxNumberIndex - 8;

		//		from a is even number
		if (listIndex % 2 == 0) {
			sendAlertMessage("Please press A once.");
			btnA.setEnabled(true);
			btnA.grabFocus();
		} else {
			sendAlertMessage("Please press B once.");
			btnB.setEnabled(true);
			btnB.grabFocus();
		}
	}

	private void printLoserTree(int size, int[] list, int outputTh) {
		for (int i = 0; i < size; i++) {
			JTextField toTextField = (JTextField) panel.getComponent(i);
			toTextField.setText(String.format("%d", list[i]));
		}
		if (outputTh == 1) {
			FirstOutput.setText(String.format("%d", list[0]));
			txtrOutputdisplay.append("First output number of the max number is " + FirstOutput.getText() + "\n");
		} else if (outputTh == 2) {
			SecondOutput.setText(String.format("%d", list[0]));
			txtrOutputdisplay.append("Second output number of the max number is " + SecondOutput.getText() + "\n");
		}
	}

	private int findMaxNumIndex(int[] list, int target, int low, int high) {
		for (int i = low; i < high; i++) {
			if (list[i] == target)
				return i;
		}
		return -1;
	}
}
