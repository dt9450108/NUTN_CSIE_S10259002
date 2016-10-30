import java.util.LinkedList;

public class SparseMatrix {
	private LinkedList<DummyNode> TLcorner;

	public SparseMatrix(int rowsNum, int colsNum) {
		TLcorner = new LinkedList<DummyNode>();

		TLcorner.add(new DummyNode(rowsNum, colsNum, false));

		for (int col = 1; col <= colsNum; col++)
			TLcorner.add(new DummyNode(col, 0, true));
	}

	//	public SparseMatrix(int rowsNum, int colsNum, int termsNum) {
	//		TLcorner = new LinkedList<DummyNode>();
	//
	//		TLcorner.add(new DummyNode(rowsNum, colsNum, termsNum, false));
	//
	//		for (int col = 1; col <= colsNum; col++)
	//			TLcorner.add(new DummyNode(col, 0, 0, true));
	//	}

	public LinkedList<DummyNode> getTLcorner() {
		return this.TLcorner;
	}

	public double getRCValue(int row, int col) {
		DummyNode currentDummyNode = getTLcorner().get(col);
		Node rowFound = findRowEntry(row, currentDummyNode.getColEntry());

		if (rowFound != null)
			return rowFound.getValue();
		return 0;
	}

	public Node getRCNode(int row, int col) {
		LinkedList<Node> currentColList = getTLcorner().get(col).getColEntry();
		Node targetNode = findRowEntry(row, currentColList);

		if (targetNode != null)
			return targetNode;
		return null;
	}

	public void setRCValue(int row, int col, double value) {
		int colsNum, rowsNum, currentTermsNum;
		colsNum = getTLcorner().getFirst().getColsNum();
		rowsNum = getTLcorner().getFirst().getRowsNum();
		currentTermsNum = getTLcorner().getFirst().getTermsNum();

		// the node [row, col]
		Node currentRowEntry = null;

		// if row and col in the size of the matrix
		if ((row > 0 && row <= rowsNum) && (col > 0 && col <= colsNum)) {
			// find node [row, col]
			currentRowEntry = findRowEntry(row, getTLcorner().get(col).getColEntry());

			if (value != 0) {
				// if [row, col] is not null, then change the value which has existed in [row, col]
				if (currentRowEntry != null) {
					currentRowEntry.setValue(value);
				} else {
					// if [row, col] not exist in the matrix, then add new node
					int indexOfPreviousOfNewNode = findIndexOfPreviousNode(row, getTLcorner().get(col).getColEntry());
					getTLcorner().get(col).getColEntry().add(indexOfPreviousOfNewNode + 1, new Node(row, col, value));

					//change number of nonzero terms
					getTLcorner().getFirst().setTermsNum(currentTermsNum + 1);
				}
			} else {
				//------------------------------------------------------------		ok
				if (currentRowEntry != null) {
					getTLcorner().get(col).getColEntry().remove(getTLcorner().get(col).getColEntry().indexOf(currentRowEntry));

					// because of value of [row, col] is zero, number of term subtract 1
					getTLcorner().getFirst().setTermsNum(currentTermsNum - 1);
				}
			}
		}
	}

	private Node findRowEntry(int row, LinkedList<Node> colEntry) {
		Node rowFound = null;
		int rowTerms = colEntry.size();

		for (int i = 0; i < rowTerms; i++) {
			rowFound = colEntry.get(i);

			if (rowFound.getRow() == row)
				return rowFound;
		}
		return null;
	}

	// find index of the previous node that is before the row
	private int findIndexOfPreviousNode(int row, LinkedList<Node> colEntry) {
		Node rowFound = null;
		int foundIndex = -1, i;

		for (i = 0; i < colEntry.size(); i++) {
			rowFound = colEntry.get(i);
			if (rowFound.getRow() > row) {
				foundIndex = i - 1;
				break;
			}
		}
		return foundIndex;
	}
}
