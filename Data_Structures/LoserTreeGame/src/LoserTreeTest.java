public class LoserTreeTest {
	public static void main(String[] args) {
		LoserTree test = new LoserTree(8);
		int[] number = { 1, 2, 3, 4, 5, 6, 7, 8 };
		test.constructLoserTree(number);

		int size = test.getList().length;
		int[] temp = test.getList();
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(temp[i] + " ");
		}

		test.newSort(15, 9);
		temp = test.getList();
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(temp[i] + " ");
		}
	}
}
