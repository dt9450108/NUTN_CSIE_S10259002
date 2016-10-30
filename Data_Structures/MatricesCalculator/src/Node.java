public class Node {
	private int row;
	private int col;
	private double value;

	public Node(int row, int col, double value) {
		setRow(row);
		setCol(col);
		setValue(value);
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public double getValue() {
		return this.value;
	}
}
