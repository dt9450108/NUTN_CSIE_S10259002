import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class BookTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "書名", "價錢(NT)", "作者", "出版社", "出版日期" };
	private final Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, String.class };
	private Vector<Object[]> data;

	public BookTableModel() {
		super();
		this.data = new Vector<Object[]>();
	}

	public BookTableModel(Vector<Book> Books) {
		super();
		this.data = new Vector<Object[]>();
		for (int i = 0; i < Books.size(); i++) {
			this.data.addElement(new Object[] { Books.get(i).getName(), Books.get(i).getPriceStr(), Books.get(i).getAuthor(), Books.get(i).getPublisher(), Books.get(i).getPublishDateStr() });
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
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

	public Class getColumnClass(int col) {
		return columnTypes[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		try {
			Object[] o = (Object[]) this.data.get(row);
			switch (col) {
				case 0: // name: String
					return (String) o[col];
				case 1: // price: int
					return (String) o[col];
				case 2: // author: String
					return (String) o[col];
				case 3: // publisher: String
					return (String) o[col];
				case 4: // publishTime: Date
					return (String) o[col];
			}
		} catch (Exception e) {
			System.out.println("TableModel getValueAt ERROR");
		}
		return new String();
	}

	public void setValueAt(Object value, int row, int col) {
		Object[] o = this.data.get(row);
		switch (col) {
			case 0: // name: String
				o[col] = (String) value;
			case 1: // price: int
				o[col] = (String) value;
			case 2: // author: String
				o[col] = (String) value;
			case 3: // publisher: String
				o[col] = (String) value;
			case 4: // publishTime: Date
				o[col] = (String) value;
		}
		fireTableCellUpdated(row, col);
	}

	public void setAllValueAt(Object obj, int row) {
		Object[] o = this.data.get(row);
		Book s = (Book) obj;
		o[0] = s.getName();
		o[1] = s.getPriceStr();
		o[2] = s.getAuthor();
		o[3] = s.getPublisher();
		o[4] = s.getPublishDateStr();
		fireTableDataChanged();
	}

	public void insertData(Book book) {
		this.data.addElement(new Object[] { book.getName(), book.getPriceStr(), book.getAuthor(), book.getPublisher(), book.getPublishDateStr() });
		fireTableDataChanged();
	}

	public void insertData(Vector<Book> Books) {
		for (int i = 0; i < Books.size(); i++)
			this.data.addElement(new Object[] { Books.get(i).getName(), Books.get(i).getPriceStr(), Books.get(i).getAuthor(), Books.get(i).getPublisher(), Books.get(i).getPublishDateStr() });
		fireTableDataChanged();
	}

	public void removeRow(int row) {
		data.removeElementAt(row);
		fireTableDataChanged();
	}

	public void removeAll() {
		data.clear();
		fireTableDataChanged();
	}
}
