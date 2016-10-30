import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class StuTableCellRender extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 2L;

	public StuTableCellRender() {
		super();
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setBorder(null);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		if (column == 1) {
			table.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		} else {
			table.setFont(new Font("Time New Roman", Font.PLAIN, 16));
		}

		if (isSelected) {
			table.setSelectionBackground(new Color(76, 175, 80));
			table.setSelectionForeground(Color.white);
		} else {
			table.setSelectionBackground(Color.white);
			table.setSelectionForeground(Color.black);
		}

		table.setRowHeight(25);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
