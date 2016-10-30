import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * 
 * @author Huang Shih-Hao
 * @class SearchWindow
 * @properties frame: a new search window frame. TfQueryText: the target been
 *             wanted to search.
 *
 */
public class SearchWindow {
	private JFrame frame;
	private JPanel panel;
	private JLabel LbIcon;
	private JLabel LbSearchHint;
	private JTextField TfQueryText;
	private SiButton btnQuery;
	private SiButton btnCancel;

	public SearchWindow() {
		frame = new JFrame("搜尋...");
		URL url = SearchWindow.class.getResource("/icon.png");
		URL url2 = SearchWindow.class.getResource("/search64.png");
		ImageIcon icon = new ImageIcon(url);
		ImageIcon icon2 = new ImageIcon(url2);
		frame.setIconImage(icon.getImage());
		frame.setSize(440, 220);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TfQueryText.setText("");
			}
		});

		panel = new JPanel();
		panel.setBackground(Color.decode("#ecf0f1"));
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(46, 12, 440, 191);
		panel.setLayout(null);

		LbIcon = new JLabel("");
		LbIcon.setBounds(24, 18, 64, 64);
		LbIcon.setHorizontalAlignment(SwingConstants.CENTER);

		LbIcon.setIcon(icon2);
		panel.add(LbIcon);

		TfQueryText = new JTextField();
		TfQueryText.setBounds(120, 80, 280, 30);
		TfQueryText.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		panel.add(TfQueryText);
		TfQueryText.setColumns(10);

		btnQuery = new SiButton("Search", Color.decode("#727272"), Color.decode("#B6B6B6"), Color.decode("#607D8B"), Color.decode("#FFFFFF"), Color.decode("#455A64"), Color.decode("#9E9E9E"));
		btnQuery.setBounds(200, 130, 90, 30);
		panel.add(btnQuery);

		btnCancel = new SiButton("Cancel", Color.decode("#727272"), Color.decode("#B6B6B6"), Color.decode("#F44336"), Color.decode("#FFFFFF"), Color.decode("#D32F2F"), Color.decode("#E57373"));
		btnCancel.setBounds(310, 130, 90, 30);
		panel.add(btnCancel);

		LbSearchHint = new JLabel("<html>輸入「信件標題」或「寄件者」<br>（只顯示搜尋到的第一封）：</html>");
		LbSearchHint.setBounds(120, 18, 280, 60);
		LbSearchHint.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		panel.add(LbSearchHint);

		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		TfQueryText.requestFocus();
	}

	public SiButton getBtnQuery() {
		return btnQuery;
	}

	public SiButton getBtnCancel() {
		return btnCancel;
	}

	public JTextField getTfQueryText() {
		return TfQueryText;
	}

	// open the search window
	public void open() {
		frame.setVisible(true);
		TfQueryText.setText("");
		// focus the search window
		TfQueryText.requestFocus();
	}

	// close the search window
	public void close() {
		frame.setVisible(false);
	}
}
