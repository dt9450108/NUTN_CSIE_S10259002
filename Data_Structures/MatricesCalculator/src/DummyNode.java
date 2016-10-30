import java.util.LinkedList;

public class DummyNode {
	private int rowsNum;
	private int colsNum;
	private int termsNum;
	private LinkedList<Node> colEntry;

	public DummyNode(int rowsNum, int colsNum, boolean produceColEntry) {
		setRowsNum(rowsNum);
		setColsNum(colsNum);
		//		setTermsNum(termsNum);
		if (produceColEntry)
			colEntry = new LinkedList<Node>();
	}

	//	public DummyNode(int rowsNum, int colsNum, int termsNum, boolean produceColEntry) {
	//		setRowsNum(rowsNum);
	//		setColsNum(colsNum);
	//		setTermsNum(termsNum);
	//		if (produceColEntry)
	//			colEntry = new LinkedList<Node>();
	//	}

	public void setRowsNum(int rowsNum) {
		this.rowsNum = rowsNum;
	}

	public void setColsNum(int colsNum) {
		this.colsNum = colsNum;
	}

	public void setTermsNum(int termsNum) {
		this.termsNum = termsNum;
	}

	public int getRowsNum() {
		return this.rowsNum;
	}

	public int getColsNum() {
		return this.colsNum;
	}

	public int getTermsNum() {
		return this.termsNum;
	}

	public LinkedList<Node> getColEntry() {
		return colEntry;
	}
}
