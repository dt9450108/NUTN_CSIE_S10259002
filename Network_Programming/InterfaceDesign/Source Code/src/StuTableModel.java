import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class StuTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Student ID", "Name", "Sport", "Course", "ID" };
	private final Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, Integer.class };
	private Vector<Object[]> data;

	public StuTableModel(Vector<Student> Studs) {
		super();
		this.data = new Vector<Object[]>();
		for (int i = 0; i < Studs.size(); i++) {
			this.data.addElement(new Object[] { Studs.get(i).getNum(), Studs.get(i).getName(), MainUI.SPORTS[Studs.get(i).getSport()], MainUI.COURSE[Studs.get(i).getCourse()], Studs.get(i).getId() });
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
		Object[] o = (Object[]) this.data.get(row);

		switch (col) {
			case 0:
				return (String) o[col];
			case 1:
				return (String) o[col];
			case 2:
				return (String) o[col];
			case 3:
				return (String) o[col];
			case 4:
				return (int) o[col];
		}
		return new String();
	}

	public void setValueAt(Object value, int row, int col) {
		Object[] o = this.data.get(row);
		switch (col) {
			case 0:
				o[col] = (String) value;
			case 1:
				o[col] = (String) value;
			case 2:
				o[col] = (String) MainUI.SPORTS[(int) value];
			case 3:
				o[col] = (String) MainUI.COURSE[(int) value];
			case 4:
				o[col] = (int) value;
		}
		fireTableCellUpdated(row, col);
	}

	public void setAllValueAt(Object obj, int row) {
		Object[] o = this.data.get(row);
		Student s = (Student) obj;
		o[0] = (String) s.getNum();
		o[1] = (String) s.getName();
		o[2] = (String) MainUI.SPORTS[(int) s.getSport()];
		o[3] = (String) MainUI.COURSE[(int) s.getCourse()];
		o[4] = (int) s.getId();
		fireTableDataChanged();
	}

	public void insertData(Student stu) {
		System.out.printf("%s, %s, %s, %s, %d\n", stu.getNum(), stu.getName(), MainUI.SPORTS[stu.getSport()], MainUI.COURSE[stu.getCourse()], stu.getId());
		this.data.addElement(new Object[] { stu.getNum(), stu.getName(), MainUI.SPORTS[stu.getSport()], MainUI.COURSE[stu.getCourse()], stu.getId() });
		fireTableDataChanged();
	}

	public void removeRow(int row) {
		data.removeElementAt(row);
		fireTableDataChanged();
	}
}
