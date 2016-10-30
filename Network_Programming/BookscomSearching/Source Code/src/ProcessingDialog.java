import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class ProcessingDialog extends JDialog {
	private JProgressBar progressBar;
	private JPanel ProcessingPanel;
	private JLabel LbMsg;
	private SwingWorker<Void, Void> worker;

	public ProcessingDialog(String title) {
		super(null, title, ModalityType.APPLICATION_MODAL);
		ProcessingPanel = new JPanel();
		ProcessingPanel.setBounds(0, 0, 310, 70);
		ProcessingPanel.setLayout(null);

		LbMsg = new JLabel("搜尋中...");
		LbMsg.setBounds(5, 5, 300, 40);
		LbMsg.setHorizontalAlignment(SwingConstants.CENTER);
		LbMsg.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		ProcessingPanel.add(LbMsg);

		progressBar = new JProgressBar();
		progressBar.setBounds(5, 50, 310, 25);
		progressBar.setIndeterminate(true);
		progressBar.setBorder(BorderFactory.createLineBorder(new Color(56, 142, 60)));
		progressBar.setForeground(new Color(76, 175, 80));
		ProcessingPanel.add(progressBar);
		this.add(ProcessingPanel);
		this.setIconImage(MainUI.icon.getImage());
		this.setSize(320, 125);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	public void open() {
		this.setVisible(true);
	}

	public void close() {
		this.setVisible(false);
	}

	public SwingWorker<Void, Void> getWorker() {
		return worker;
	}

	public void setWorker(SwingWorker<Void, Void> worker) {
		this.worker = worker;
	}

	public void execute() {
		this.worker.execute();
		open();
	}
}
