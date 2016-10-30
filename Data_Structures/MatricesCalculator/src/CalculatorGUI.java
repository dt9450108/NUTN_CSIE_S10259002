import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class CalculatorGUI {
	private SparseMatrix firstMatrix;
	private SparseMatrix secondMatrix;

	private JFrame frame;
	private JPanel PanelHeader;
	private JLabel LabelFirstMatrixText;
	private JLabel LabelSecondMatrixText;
	private JTextField TextBoxFirstMatrixRows; // First matrix rows of the text box input
	private JTextField TextBoxFirstMatrixCols;
	private JTextField TextBoxSecondMatrixRows;
	private JTextField TextBoxSecondMatrixCols;
	private JLabel LabelCalulateResult;
	private JButton BtnFirstMatrixInputValue;
	private JButton BtnSecondMatrixInputValue;
	private JLabel LabelFirstSaveCondition;// save condition icon and label
	private JLabel LabelSecondSaveCondition;
	private ImageIcon IconSaved = new ImageIcon(CalculatorGUI.class.getResource("/saved.png"));
	private ImageIcon IconUnaved = new ImageIcon(CalculatorGUI.class.getResource("/unsaved.png"));

	private JPanel PanelMiddleDisplayResult;

	private JPanel PanelFooterOperation;
	private JPanel SubPanelNumberBtns;
	private JButton BtnNumber_1;
	private JButton BtnNumber_2;
	private JButton BtnNumber_3;
	private JButton BtnNumber_4;
	private JButton BtnNumber_5;
	private JButton BtnNumber_6;
	private JButton BtnNumber_7;
	private JButton BtnNumber_8;
	private JButton BtnNumber_9;
	private JButton Btn_dot;
	private JButton BtnNumber_0;
	private JButton Btn_readInMatrix;
	private JPanel PanelClearAndDelete;
	private JButton Btn_clear;
	private JButton Btn_delete;
	private JPanel PanelMatrixOperationBtns;
	private JButton BtnOp_merase;
	private JButton BtnOp_madd;
	private JButton BtnOp_mmult;
	private JButton BtnOp_mtranspose;
	private JPanel PanelFirstMatrixInput;
	private JPanel PanelSecondMatrixInput;
	private JPanel PanelResultMatrixOutput;
	private JPanel PanelFooterContent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorGUI window = new CalculatorGUI();
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
	public CalculatorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1163, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		PanelHeader = new JPanel();
		PanelHeader.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		PanelHeader.setBounds(6, 6, 1151, 45);
		frame.getContentPane().add(PanelHeader);
		PanelHeader.setLayout(null);

		//		First matrix size of the text box input
		TextBoxFirstMatrixRows = new JTextField();
		TextBoxFirstMatrixRows.setBounds(84, 10, 40, 25);
		PanelHeader.add(TextBoxFirstMatrixRows);
		TextBoxFirstMatrixRows.setColumns(10);

		TextBoxFirstMatrixCols = new JTextField();
		TextBoxFirstMatrixCols.setColumns(10);
		TextBoxFirstMatrixCols.setBounds(142, 10, 40, 25);
		PanelHeader.add(TextBoxFirstMatrixCols);

		TextBoxSecondMatrixRows = new JTextField();
		TextBoxSecondMatrixRows.setColumns(10);
		TextBoxSecondMatrixRows.setBounds(468, 10, 40, 25);
		PanelHeader.add(TextBoxSecondMatrixRows);

		TextBoxSecondMatrixCols = new JTextField();
		TextBoxSecondMatrixCols.setColumns(10);
		TextBoxSecondMatrixCols.setBounds(527, 10, 40, 25);
		PanelHeader.add(TextBoxSecondMatrixCols);

		BtnFirstMatrixInputValue = new JButton("確定");
		BtnFirstMatrixInputValue.setFocusable(false);
		BtnFirstMatrixInputValue.setBounds(201, 10, 90, 25);
		PanelHeader.add(BtnFirstMatrixInputValue);

		BtnSecondMatrixInputValue = new JButton("確定");
		BtnSecondMatrixInputValue.setFocusable(false);
		BtnSecondMatrixInputValue.setBounds(587, 10, 90, 25);
		PanelHeader.add(BtnSecondMatrixInputValue);

		LabelFirstMatrixText = new JLabel("矩陣大小：        列        行");
		LabelFirstMatrixText.setFont(new Font("Arial Black", Font.PLAIN, 16));
		LabelFirstMatrixText.setBounds(6, 10, 265, 25);
		PanelHeader.add(LabelFirstMatrixText);

		LabelSecondMatrixText = new JLabel("矩陣大小：        列        行");
		LabelSecondMatrixText.setFont(new Font("Arial Black", Font.PLAIN, 16));
		LabelSecondMatrixText.setBounds(390, 10, 265, 25);
		PanelHeader.add(LabelSecondMatrixText);

		LabelCalulateResult = new JLabel("計算結果");
		LabelCalulateResult.setFont(new Font("Arial Black", Font.PLAIN, 16));
		LabelCalulateResult.setBounds(771, 9, 71, 25);
		PanelHeader.add(LabelCalulateResult);

		//		==============================================================================		Label for save condition
		LabelFirstSaveCondition = new JLabel();
		LabelFirstSaveCondition.setBounds(290, 2, 40, 40);
		LabelFirstSaveCondition.setIcon(IconUnaved);
		PanelHeader.add(LabelFirstSaveCondition);

		LabelSecondSaveCondition = new JLabel();
		LabelSecondSaveCondition.setBounds(679, 2, 40, 40);
		LabelSecondSaveCondition.setIcon(IconUnaved);
		PanelHeader.add(LabelSecondSaveCondition);

		PanelMiddleDisplayResult = new JPanel();
		PanelMiddleDisplayResult.setBounds(6, 57, 1151, 319);
		frame.getContentPane().add(PanelMiddleDisplayResult);
		PanelMiddleDisplayResult.setLayout(new GridLayout(1, 3, 0, 0));

		PanelFirstMatrixInput = new JPanel();
		PanelFirstMatrixInput.setSize(PanelMiddleDisplayResult.getWidth() / 3, 300);
		PanelMiddleDisplayResult.add(PanelFirstMatrixInput);

		PanelSecondMatrixInput = new JPanel();
		PanelSecondMatrixInput.setSize(PanelMiddleDisplayResult.getWidth() / 3, 300);
		PanelMiddleDisplayResult.add(PanelSecondMatrixInput);

		PanelResultMatrixOutput = new JPanel();
		PanelResultMatrixOutput.setSize(PanelMiddleDisplayResult.getWidth() / 3, 300);
		PanelMiddleDisplayResult.add(PanelResultMatrixOutput);

		PanelFooterOperation = new JPanel();
		PanelFooterOperation.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		PanelFooterOperation.setBounds(6, 388, 1151, 184);
		frame.getContentPane().add(PanelFooterOperation);
		PanelFooterOperation.setLayout(null);

		PanelFooterContent = new JPanel();
		PanelFooterContent.setBounds(404, 6, 343, 172);
		PanelFooterOperation.add(PanelFooterContent);
		PanelFooterContent.setLayout(null);

		SubPanelNumberBtns = new JPanel();
		SubPanelNumberBtns.setBounds(0, 0, 215, 172);
		PanelFooterContent.add(SubPanelNumberBtns);
		SubPanelNumberBtns.setLayout(new GridLayout(4, 3, 0, 0));

		BtnNumber_1 = new JButton("1");
		BtnNumber_1.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_1);

		BtnNumber_2 = new JButton("2");
		BtnNumber_2.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_2);

		BtnNumber_3 = new JButton("3");
		BtnNumber_3.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_3);

		BtnNumber_4 = new JButton("4");
		BtnNumber_4.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_4);

		BtnNumber_5 = new JButton("5");
		BtnNumber_5.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_5);

		BtnNumber_6 = new JButton("6");
		BtnNumber_6.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_6);

		BtnNumber_7 = new JButton("7");
		BtnNumber_7.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_7);

		BtnNumber_8 = new JButton("8");
		BtnNumber_8.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_8);

		BtnNumber_9 = new JButton("9");
		BtnNumber_9.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_9);

		Btn_dot = new JButton(".");
		Btn_dot.setFocusable(false);
		SubPanelNumberBtns.add(Btn_dot);

		BtnNumber_0 = new JButton("0");
		BtnNumber_0.setFocusable(false);
		SubPanelNumberBtns.add(BtnNumber_0);

		Btn_readInMatrix = new JButton("Save");
		BtnNumber_0.setFocusable(false);
		SubPanelNumberBtns.add(Btn_readInMatrix);

		PanelClearAndDelete = new JPanel();
		PanelClearAndDelete.setBounds(215, 0, 63, 172);
		PanelFooterContent.add(PanelClearAndDelete);
		PanelClearAndDelete.setBorder(new MatteBorder(0, 1, 0, 1, (Color) new Color(0, 0, 0)));
		PanelClearAndDelete.setLayout(new GridLayout(0, 1, 0, 0));

		Btn_clear = new JButton("清除");
		Btn_clear.setFocusable(false);
		PanelClearAndDelete.add(Btn_clear);

		Btn_delete = new JButton("←");
		Btn_delete.setFocusable(false);
		PanelClearAndDelete.add(Btn_delete);

		PanelMatrixOperationBtns = new JPanel();
		PanelMatrixOperationBtns.setBounds(279, 0, 63, 172);
		PanelFooterContent.add(PanelMatrixOperationBtns);
		PanelMatrixOperationBtns.setLayout(new GridLayout(4, 1, 0, 0));

		BtnOp_merase = new JButton("清除矩陣");
		BtnOp_merase.setFocusable(false);
		BtnOp_merase.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		PanelMatrixOperationBtns.add(BtnOp_merase);

		BtnOp_madd = new JButton("+");
		BtnOp_madd.setFocusable(false);
		PanelMatrixOperationBtns.add(BtnOp_madd);

		BtnOp_mmult = new JButton("*");
		BtnOp_mmult.setFocusable(false);
		PanelMatrixOperationBtns.add(BtnOp_mmult);

		BtnOp_mtranspose = new JButton("T");
		BtnOp_mtranspose.setFocusable(false);
		PanelMatrixOperationBtns.add(BtnOp_mtranspose);
		//		===============================================================================================		event listener start

		//		=======================================================			button number 1 listener
		BtnNumber_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "1");
				}
			}
		});

		//		=======================================================			button number 2 listener
		BtnNumber_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "2");
				}
			}
		});

		//		=======================================================			button number 3 listener
		BtnNumber_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "3");
				}
			}
		});

		//		=======================================================			button number 4 listener
		BtnNumber_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "4");
				}
			}
		});

		//		=======================================================			button number 5 listener
		BtnNumber_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "5");
				}
			}
		});

		//		=======================================================			button number 6 listener
		BtnNumber_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "6");
				}
			}
		});

		//		=======================================================			button number 7 listener
		BtnNumber_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "7");
				}
			}
		});

		//		=======================================================			button number 8 listener
		BtnNumber_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "8");
				}
			}
		});

		//		=======================================================			button number 9 listener
		BtnNumber_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "9");
				}
			}
		});

		//		=======================================================			button dot listener
		Btn_dot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					String currentStr = currentTextField.getText();
					if (!currentStr.contains("."))
						BtnForPressNumber(currentTextField, ".");
					else if (currentStr.contains(".") && currentTextField.getHighlighter().getHighlights().length > 0) {
						int start = currentTextField.getHighlighter().getHighlights()[0].getStartOffset();
						int end = currentTextField.getHighlighter().getHighlights()[0].getEndOffset();
						String c = currentStr.substring(start, end);
						if (c.contains("."))
							BtnForPressNumber(currentTextField, ".");
					}
				}
			}
		});

		//		=======================================================			button number 0 listener
		BtnNumber_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					BtnForPressNumber(currentTextField, "0");
				}
			}
		});

		//		=======================================================			button clear listener
		Btn_clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					if (!currentTextField.getText().isEmpty())
						currentTextField.setText("");
				}
			}
		});

		//		=======================================================			button delete listener
		Btn_delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (frame.getFocusOwner() instanceof JTextField) {
					JTextField currentTextField = (JTextField) frame.getFocusOwner();
					boolean bool = true;

					if (currentTextField.getHighlighter().getHighlights().length > 0) {
						int startOffset = currentTextField.getHighlighter().getHighlights()[0].getStartOffset();
						int endOffset = currentTextField.getHighlighter().getHighlights()[0].getEndOffset();
						String oldStr = currentTextField.getText();
						String newStr = oldStr.substring(0, startOffset) + oldStr.substring(endOffset);
						currentTextField.setText(newStr);
						currentTextField.setCaretPosition(startOffset);
						bool = false;
					}

					if (!currentTextField.getText().isEmpty() && bool) {
						String currentStr = currentTextField.getText();
						int currentCaretPos = currentTextField.getCaretPosition();

						String newStr = currentStr.substring(0, currentCaretPos - 1) + currentStr.substring(currentCaretPos, currentStr.length());
						currentTextField.setText(newStr);
						currentTextField.setCaretPosition(currentCaretPos - 1);
					}
				}
			}
		});

		//		=======================================================			button first matrix input value listener
		BtnFirstMatrixInputValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowsNumber = 0, colsNumber = 0;
				boolean error = false;

				if (PanelFirstMatrixInput.getComponentCount() > 0)
					merase(firstMatrix, PanelFirstMatrixInput, 1);
				else {

					if (TextBoxFirstMatrixRows.getText().isEmpty()) {
						sendAlertMessage("第一個矩陣「列」未輸入");
						TextBoxFirstMatrixRows.grabFocus();
						error = true;
					} else if (!isDoubleNumber(TextBoxFirstMatrixRows.getText())) {
						sendAlertMessage("第一個矩陣「列」必須為「數字」");
						TextBoxFirstMatrixRows.grabFocus();
						error = true;
					} else if (!isIntegerNumber(TextBoxFirstMatrixRows.getText())) {
						sendAlertMessage("第一個矩陣「列」必須為「整數」");
						TextBoxFirstMatrixRows.grabFocus();
						error = true;
					} else {
						if (TextBoxFirstMatrixCols.getText().isEmpty()) {
							sendAlertMessage("第一個矩陣「行」未輸入");
							TextBoxFirstMatrixCols.grabFocus();
							error = true;
						} else if (!isDoubleNumber(TextBoxFirstMatrixCols.getText())) {
							sendAlertMessage("第一個矩陣「行」必須為「數字」");
							TextBoxFirstMatrixCols.grabFocus();
							error = true;
						} else if (!isIntegerNumber(TextBoxFirstMatrixCols.getText())) {
							sendAlertMessage("第一個矩陣「行」必須為「整數」");
							TextBoxFirstMatrixCols.grabFocus();
							error = true;
						}
					}

					if (!error) {
						rowsNumber = Integer.parseInt(TextBoxFirstMatrixRows.getText());
						colsNumber = Integer.parseInt(TextBoxFirstMatrixCols.getText());
						if (rowsNumber > 0 && colsNumber > 0)
							InsertTextFieldToPanel(rowsNumber, colsNumber, PanelFirstMatrixInput, true);
						else if (rowsNumber <= 0) {
							sendAlertMessage("「列」數必須是「大於零的整數」");
							TextBoxFirstMatrixRows.grabFocus();
						} else if (colsNumber <= 0) {
							sendAlertMessage("「行」數必須是「大於零的整數」");
							TextBoxFirstMatrixCols.grabFocus();
						}
					}
				}
			}
		});

		//		=======================================================			button second matrix input value listener
		BtnSecondMatrixInputValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowsNumber = 0, colsNumber = 0;
				boolean error = false;

				if (PanelSecondMatrixInput.getComponentCount() > 0)
					merase(secondMatrix, PanelSecondMatrixInput, 2);
				else {

					if (TextBoxSecondMatrixRows.getText().isEmpty()) {
						sendAlertMessage("第二個矩陣「列」未輸入");
						TextBoxSecondMatrixRows.grabFocus();
						error = true;
					} else if (!isDoubleNumber(TextBoxSecondMatrixRows.getText())) {
						sendAlertMessage("第二個矩陣「列」必須為「數字」");
						TextBoxSecondMatrixRows.grabFocus();
						error = true;
					} else if (!isIntegerNumber(TextBoxSecondMatrixRows.getText())) {
						sendAlertMessage("第二個矩陣「列」必須為「整數」");
						TextBoxSecondMatrixRows.grabFocus();
						error = true;
					} else {
						if (TextBoxSecondMatrixCols.getText().isEmpty()) {
							sendAlertMessage("第二個矩陣「行」未輸入");
							TextBoxSecondMatrixCols.grabFocus();
							error = true;
						} else if (!isDoubleNumber(TextBoxSecondMatrixCols.getText())) {
							sendAlertMessage("第二個矩陣「行」必須為「數字」");
							TextBoxSecondMatrixCols.grabFocus();
							error = true;
						} else if (!isIntegerNumber(TextBoxSecondMatrixCols.getText())) {
							sendAlertMessage("第二個矩陣「行」必須為「整數」");
							TextBoxSecondMatrixCols.grabFocus();
							error = true;
						}
					}

					if (!error) {
						rowsNumber = Integer.parseInt(TextBoxSecondMatrixRows.getText());
						colsNumber = Integer.parseInt(TextBoxSecondMatrixCols.getText());
						if (rowsNumber > 0 && colsNumber > 0)
							InsertTextFieldToPanel(rowsNumber, colsNumber, PanelSecondMatrixInput, true);
						else if (rowsNumber <= 0) {
							sendAlertMessage("「列」數必須是「大於零的整數」");
							TextBoxSecondMatrixRows.grabFocus();
						} else if (colsNumber <= 0) {
							sendAlertMessage("「行」數必須是「大於零的整數」");
							TextBoxSecondMatrixCols.grabFocus();
						}
					}
				}
			}
		});

		//		=======================================================			button readInMatrix listener		save matrix
		Btn_readInMatrix.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveMatrix(true, true);
			}
		});

		//		=======================================================			button erase matrix listener
		BtnOp_merase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] optionChoice = { "取消", "第二個矩陣", "第一個矩陣" }; // 0 1 2
				int choice = JOptionPane.showOptionDialog(null, "選擇所要清除的矩陣", "清除矩陣", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionChoice, "取消");

				switch (choice) {
					case 1: // second matrix
						if (secondMatrix != null)
							merase(secondMatrix, PanelSecondMatrixInput, 2);
						break;
					case 2: // first matrix
						if (firstMatrix != null)
							merase(firstMatrix, PanelFirstMatrixInput, 1);
						break;
					default:

				}
			}
		});

		//		=======================================================			button add matrix listener
		BtnOp_madd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (firstMatrix != null && secondMatrix != null) {
					SparseMatrix outputMatrix = madd(firstMatrix, secondMatrix);

					if (outputMatrix != null) {
						mwrite(outputMatrix, PanelResultMatrixOutput);
					}
				} else if (firstMatrix == null && secondMatrix == null) {
					sendAlertMessage("請儲存矩陣");
				} else if (firstMatrix == null) {
					sendAlertMessage("第一個矩陣尚未儲存");
				} else if (secondMatrix == null) {
					sendAlertMessage("第二個矩陣尚未儲存");
				}
			}
		});

		//		=======================================================			button multiply matrix listener
		BtnOp_mmult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (firstMatrix != null & secondMatrix != null) {
					SparseMatrix outputMatrix = mmult(firstMatrix, secondMatrix);

					if (outputMatrix != null)
						mwrite(outputMatrix, PanelResultMatrixOutput);
				} else if (firstMatrix == null && secondMatrix == null) {
					sendAlertMessage("請儲存矩陣");
				} else if (firstMatrix == null) {
					sendAlertMessage("第一個矩陣尚未儲存");
				} else if (secondMatrix == null) {
					sendAlertMessage("第二個矩陣尚未儲存");
				}
			}
		});

		//		=======================================================			button transpose matrix listener
		BtnOp_mtranspose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] optionDialogChoice = { "取消", "第二個矩陣", "第一個矩陣" };
				int choiceResult = JOptionPane.showOptionDialog(null, "選擇要轉置的矩陣", "轉置矩陣", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionDialogChoice, "取消");

				switch (choiceResult) {
					case 1: //		second matrix
						saveMatrix(false, true);
						if (secondMatrix != null) {
							SparseMatrix oldSecondMatrix = secondMatrix;
							secondMatrix = mtranspose(oldSecondMatrix);
							if (secondMatrix != null) {
								mwrite(secondMatrix, PanelSecondMatrixInput);
								TextBoxSecondMatrixRows.setText(String.format("%s", secondMatrix.getTLcorner().getFirst().getRowsNum()));
								TextBoxSecondMatrixCols.setText(String.format("%s", secondMatrix.getTLcorner().getFirst().getColsNum()));
								sendAlertMessage("第二個矩陣已經「轉置」");
							}
						} else {
							sendAlertMessage("第二個矩陣尚未儲存");
						}
						break;
					case 2: //		first matrix
						saveMatrix(true, false);
						if (firstMatrix != null) {
							SparseMatrix oldFirstMatrix = firstMatrix;
							firstMatrix = mtranspose(oldFirstMatrix);
							if (firstMatrix != null) {
								mwrite(firstMatrix, PanelFirstMatrixInput);
								TextBoxFirstMatrixRows.setText(String.format("%s", firstMatrix.getTLcorner().getFirst().getRowsNum()));
								TextBoxFirstMatrixCols.setText(String.format("%s", firstMatrix.getTLcorner().getFirst().getColsNum()));
								sendAlertMessage("第一個矩陣已經「轉置」");
							}
						} else {
							sendAlertMessage("第一個矩陣尚未儲存");
						}
						break;
					default: //do nothing
				}
			}
		});

		//		===============================================================================================		event listener end
	}//			===============================================================================================		initialize end

	private void InsertTextFieldToPanel(int rows, int cols, JPanel thisPanel, boolean isEditable) {
		//		if this panel has any component, if true, then remove all components
		if (thisPanel.getComponentCount() > 0) {
			thisPanel.removeAll();
			thisPanel.revalidate();
		}

		thisPanel.setLayout(new GridLayout(rows, cols));

		//		add number of textfield that all need in panel
		JTextField InsertTextField;
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++) {
				InsertTextField = new JTextField(10);
				InsertTextField.setEditable(isEditable);
				InsertTextField.setHorizontalAlignment(SwingConstants.CENTER);
				thisPanel.add(InsertTextField);
			}
		thisPanel.revalidate();
	}

	private void BtnForPressNumber(JTextField thisTextField, String number) {
		boolean bool = true;
		if (thisTextField.getHighlighter().getHighlights().length > 0) {
			int startOffset = thisTextField.getHighlighter().getHighlights()[0].getStartOffset();
			int EndOffset = thisTextField.getHighlighter().getHighlights()[0].getEndOffset();
			String oldStr = thisTextField.getText();
			String newStr = oldStr.substring(0, startOffset) + number + oldStr.substring(EndOffset);
			thisTextField.setText(newStr);
			thisTextField.setCaretPosition(startOffset + 1);
			bool = false;
		}

		if (bool) {
			int currentCaretPos = thisTextField.getCaretPosition();
			String oldStr = thisTextField.getText();
			String newStr = oldStr.substring(0, currentCaretPos) + number + oldStr.substring(currentCaretPos);
			thisTextField.setText(newStr);
			thisTextField.setCaretPosition(currentCaretPos + 1);
		}
	}

	private void sendAlertMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}

	//		Operation for matrix
	//		public static void mread(int rows, int cols, JPanel panel, SparseMatrix matrix) {
	private SparseMatrix mread(int rows, int cols, JPanel panel) {
		SparseMatrix matrix = new SparseMatrix(rows, cols);

		int nthTextField = 0;
		double inputValue = 0;
		JTextField currentTextField;

		for (int row = 1; row <= rows; row++)
			for (int col = 1; col <= cols; col++) {
				currentTextField = (JTextField) panel.getComponent(nthTextField);

				try {
					if (!currentTextField.getText().isEmpty())
						inputValue = Double.parseDouble(currentTextField.getText());
					else
						inputValue = 0;
				} catch (NumberFormatException numberFormatException) {
					sendAlertMessage(String.format("第[%d, %d]元素，請輸入數字", row, col));
					return null;
				}

				matrix.setRCValue(row, col, inputValue);
				nthTextField++;
			}
		return matrix;
	}

	private void mwrite(SparseMatrix resultMatrix, JPanel whichPanel) {
		int rowsNumber = resultMatrix.getTLcorner().getFirst().getRowsNum();
		int colsNumber = resultMatrix.getTLcorner().getFirst().getColsNum();

		if (whichPanel.getComponentCount() > 0) {
			whichPanel.removeAll();
			whichPanel.revalidate();
		}
		InsertTextFieldToPanel(rowsNumber, colsNumber, whichPanel, false);

		JTextField currentTextField;
		int nthTextField = 0;
		for (int row = 1; row <= rowsNumber; row++)
			for (int col = 1; col <= colsNumber; col++) {
				currentTextField = (JTextField) whichPanel.getComponent(nthTextField++);
				if (resultMatrix.getRCValue(row, col) == 0.0)
					currentTextField.setText("0");
				else if (resultMatrix.getRCValue(row, col) == (int) resultMatrix.getRCValue(row, col))
					currentTextField.setText(String.format("%d", (int) resultMatrix.getRCValue(row, col)));
				else
					currentTextField.setText(String.format("%f", resultMatrix.getRCValue(row, col)));
			}
		whichPanel.revalidate();

	}

	private void merase(SparseMatrix eraMatrix, JPanel eraPanel, int whichMatrix) {
		eraMatrix = null;

		eraPanel.removeAll();
		eraPanel.updateUI();

		PanelResultMatrixOutput.removeAll();
		PanelResultMatrixOutput.updateUI();

		if (whichMatrix == 1) {
			TextBoxFirstMatrixRows.setText("");
			TextBoxFirstMatrixCols.setText("");
			LabelFirstSaveCondition.setIcon(IconUnaved);
		} else if (whichMatrix == 2) {
			TextBoxSecondMatrixRows.setText("");
			TextBoxSecondMatrixCols.setText("");
			LabelSecondSaveCondition.setIcon(IconUnaved);
		}

	}

	private SparseMatrix madd(SparseMatrix first, SparseMatrix second) {
		int firstMatrixRows = first.getTLcorner().getFirst().getRowsNum();
		int firstMatrixCols = first.getTLcorner().getFirst().getColsNum();
		int secondMatrixRows = second.getTLcorner().getFirst().getRowsNum();
		int secondMatrixCols = second.getTLcorner().getFirst().getColsNum();
		SparseMatrix newMatrix = null;

		if (firstMatrixRows == secondMatrixRows && firstMatrixCols == secondMatrixCols) {
			newMatrix = new SparseMatrix(firstMatrixRows, firstMatrixCols);

			for (int row = 1; row <= firstMatrixRows; row++)
				for (int col = 1; col <= firstMatrixCols; col++) {
					newMatrix.setRCValue(row, col, first.getRCValue(row, col) + second.getRCValue(row, col));
				}
			return newMatrix;
		} else {
			sendAlertMessage("輸入的矩陣大小不同，無法相加，請重新輸入");
			return null;
		}
	}

	private SparseMatrix mmult(SparseMatrix first, SparseMatrix second) {
		int firstMatrixRows = first.getTLcorner().getFirst().getRowsNum();
		int firstMatrixCols = first.getTLcorner().getFirst().getColsNum();
		int secondMatrixRows = second.getTLcorner().getFirst().getRowsNum();
		int secondMatrixCols = second.getTLcorner().getFirst().getColsNum();
		SparseMatrix newMatrix = null;

		if (firstMatrixCols == secondMatrixRows) {
			newMatrix = new SparseMatrix(firstMatrixRows, secondMatrixCols);

			double newValue = 0;
			for (int row = 1; row <= firstMatrixRows; row++) {
				for (int col = 1; col <= secondMatrixCols; col++) {
					newValue = 0;
					for (int k = 1; k <= firstMatrixCols; k++)
						newValue += (first.getRCValue(row, k) * second.getRCValue(k, col));
					newMatrix.setRCValue(row, col, newValue);
				}
			}
		} else {
			sendAlertMessage("第一個矩陣的行與第二個矩陣的列不同，無法相乘，請重新輸入");
			TextBoxFirstMatrixCols.grabFocus();
		}
		return newMatrix;
	}

	private SparseMatrix mtranspose(SparseMatrix matrix) {
		int matrixRows = matrix.getTLcorner().getFirst().getRowsNum();
		int matrixCols = matrix.getTLcorner().getFirst().getColsNum();
		SparseMatrix newMatrix = new SparseMatrix(matrixCols, matrixRows);

		for (int row = 1; row <= matrixCols; row++)
			for (int col = 1; col <= matrixRows; col++)
				newMatrix.setRCValue(row, col, matrix.getRCValue(col, row));

		return newMatrix;
	}

	private Boolean isIntegerNumber(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}

	private Boolean isDoubleNumber(String number) {
		try {
			Double.parseDouble(number);
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}

	private void saveMatrix(boolean saveFirst, boolean saveSecond) {
		if (!isIntegerNumber(TextBoxFirstMatrixRows.getText()) && saveFirst) {
			sendAlertMessage("請輸入第一個矩陣的列數（需為整數）");
			TextBoxFirstMatrixRows.grabFocus();
		} else if (!isIntegerNumber(TextBoxFirstMatrixCols.getText()) && saveFirst) {
			sendAlertMessage("請輸入第一個矩陣的行數（需為整數）");
			TextBoxFirstMatrixCols.grabFocus();
		} else if (PanelFirstMatrixInput.getComponentCount() <= 0 && saveFirst) {
			sendAlertMessage("請按第一個矩陣的確定，並輸入數值");
		} else if (!isIntegerNumber(TextBoxSecondMatrixRows.getText()) && saveSecond) {
			sendAlertMessage("請輸入第二個矩陣的列數（需為整數）");
			TextBoxSecondMatrixRows.grabFocus();
		} else if (!isIntegerNumber(TextBoxSecondMatrixCols.getText()) && saveSecond) {
			sendAlertMessage("請輸入第二個矩陣的行數（需為整數）");
			TextBoxSecondMatrixCols.grabFocus();
		} else if (PanelSecondMatrixInput.getComponentCount() <= 0 && saveSecond) {
			sendAlertMessage("請按第二個矩陣的確定，並輸入數值");
		} else {
			//		save first matrix
			if (firstMatrix == null && saveFirst) {
				int firstRowsNumber = Integer.parseInt(TextBoxFirstMatrixRows.getText());
				int firstColsNumber = Integer.parseInt(TextBoxFirstMatrixCols.getText());
				firstMatrix = mread(firstRowsNumber, firstColsNumber, PanelFirstMatrixInput);
				if (firstMatrix != null)
					LabelFirstSaveCondition.setIcon(IconSaved);
			}

			// save second matrix
			if (secondMatrix == null && saveSecond) {
				int SecondRowsNumber = Integer.parseInt(TextBoxSecondMatrixRows.getText());
				int SecondColsNumber = Integer.parseInt(TextBoxSecondMatrixCols.getText());
				secondMatrix = mread(SecondRowsNumber, SecondColsNumber, PanelSecondMatrixInput);
				if (secondMatrix != null)
					LabelSecondSaveCondition.setIcon(IconSaved);
			}
		}
	}
}
