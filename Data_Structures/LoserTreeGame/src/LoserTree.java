import java.util.ArrayList;

public class LoserTree {
	private int k; // leaves of tree
	private int treeLevel; // level of the tree
	private int[] list; // index 0 for final winner number

	public LoserTree(int k) {
		setK(k);
		treeLevel = getLevel(k);
	}

	public void constructLoserTree(int[] initialNum) {
		int level = getTreeLevel();
		int totalWinnerTreeNodes = getMaxNodeOfTree(level);
		int compareNum = getK() - 1;
		int[] temp = new int[totalWinnerTreeNodes + 1];
		ArrayList<Integer> tempList = new ArrayList<>();

		for (int i = 0; i < temp.length; i++) {
			temp[i] = -1;
			tempList.add(-1);
		}

		int InputNumberSize = initialNum.length;

		// read number to leaves
		for (int i = InputNumberSize - 1; i >= 0; i--) {
			temp[totalWinnerTreeNodes - i] = initialNum[InputNumberSize - 1 - i];
			tempList.set(totalWinnerTreeNodes - i, initialNum[InputNumberSize - 1 - i]);
		}

		//	compare
		for (int i = compareNum; i > 0; i--) {
			if (tempList.get(i * 2) > tempList.get(i * 2 + 1)) {
				temp[i] = tempList.get(i * 2 + 1);
				tempList.set(i, tempList.get(i * 2));
			} else {
				temp[i] = tempList.get(i * 2);
				tempList.set(i, tempList.get(i * 2 + 1));
			}
		}
		temp[0] = tempList.get(1);

		//	set temp to list
		setList(temp);
	}

	public int getWinner() {
		return list[0];
	}

	public void newSort(int index, int value) {
		int larger = value;
		list[index] = value;

		for (int i = index / 2; i > 0; i /= 2) {
			if (larger < list[i]) {
				int tempValue = list[i];
				list[i] = larger;
				larger = tempValue;
			}
		}
		list[0] = larger;
	}

	public void setK(int k) {
		if (k > 0)
			this.k = k;
		else
			this.k = 0;
	}

	public int getK() {
		return this.k;
	}

	public void setList(int[] list) {
		this.list = list;
	}

	public int[] getList() {
		return this.list;
	}

	public void setTreeLevel(int treeLevel) {
		if (treeLevel > 0)
			this.treeLevel = treeLevel;
		else
			this.treeLevel = 0;
	}

	public int getTreeLevel() {
		return this.treeLevel;
	}

	//	get tree level
	private int getLevel(int leavesNum) {
		if (leavesNum > 0)
			return ((int) ((Math.ceil((Math.log10(leavesNum) / Math.log10(2.0)))) + 1));
		return 0;
	}

	//	get max number of nodes in level
	//	private int getMaxNodeOfLevel(int level) {
	//		if (level > 0)
	//			return (int) Math.pow(2.0, level - 1);
	//		return 0;
	//	}

	//	get max number of nodes in tree
	private int getMaxNodeOfTree(int level) {
		if (level > 0)
			return (((int) Math.pow(2.0, level)) - 1);
		return 0;
	}
}
