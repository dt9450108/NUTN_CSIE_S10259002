import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MailTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "選擇", "標題", "寄件人", "日期", "mail id" };
	private final Class[] columnTypes = new Class[] { Boolean.class, String.class, String.class, String.class, Integer.class };
	private Vector<Object[]> data;

	public MailTableModel() {
		super();
		this.data = new Vector<Object[]>();
	}

	public MailTableModel(Vector<Mail> mails) {
		super();
		this.data = new Vector<Object[]>();
		for (int i = 0; i < mails.size(); i++) {
			this.data.addElement(new Object[] { false, mails.get(i).getSubject(), mails.get(i).getSender(), mails.get(i).getDate(), mails.get(i).getId() });
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return true;
		return false;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return columnTypes[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object[] o = (Object[]) this.data.get(row);

		switch (col) {
			case 0: // is selected : boolean
				return (boolean) o[col];
			case 1: // subject : String
				return (String) o[col];
			case 2: // sender: String
				return (String) o[col];
			case 3: // date: String
				return (String) o[col];
			case 4: // id: integer
				return (int) o[col];
		}
		return new Object();
	}

	public void setValueAt(Object value, int row, int col) {
		this.data.get(row)[col] = value;
		fireTableCellUpdated(row, col);
	}

	public void setAllValueAt(Object obj, int row) {
		Object[] o = this.data.get(row);
		Mail mail = (Mail) obj;
		o[0] = false;
		o[1] = (String) mail.getSubject();
		o[2] = (String) mail.getSender();
		o[3] = (String) mail.getDate();
		o[4] = (int) mail.getId();
		fireTableDataChanged();
	}

	public void insertData(Mail mail) {
		//		System.out.printf("%b, %s, %s, %s, %d\n", false, mail.getSubject(), mail.getSender(), mail.getDate(), mail.getId());
		this.data.addElement(new Object[] { false, mail.getSubject(), mail.getSender(), mail.getDate(), mail.getId() });
		fireTableDataChanged();
	}

	public void removeRow(int row) {
		data.removeElementAt(row);
		fireTableDataChanged();
	}

	public void removeAll() {
		data.removeAllElements();
		fireTableDataChanged();
	}
}
