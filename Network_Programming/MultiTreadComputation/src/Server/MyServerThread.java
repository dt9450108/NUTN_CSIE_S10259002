import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerThread extends Thread {
	private Socket socket;
	private int id;

	public MyServerThread(Socket socket, int id) {
		super("My Server Thread");
		this.socket = socket;
		this.id = id;
	}

	public void run() {
		Date currentDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
		try {
			while (true) {
				String NumbersStr = null;
				DataInputStream instream = new DataInputStream(this.socket.getInputStream());

				NumbersStr = instream.readUTF();
				if (NumbersStr.equals("[]")) {
					this.socket.close();
					System.out.println("┌────────────────────────────");
					System.out.printf("│          Client ID: %-5d\n", this.id);
					System.out.println("├────────────────────────────");
					System.out.printf("│Port: %5d\n", socket.getPort());
					System.out.printf("│Time: %s\n", df.format(currentDate.getTime()));
					System.out.println("│Status: Disconnected");
					System.out.println("└────────────────────────────\n");
					break;
				}

				System.out.println("┌────────────────────────────");
				System.out.printf("│          Client ID: %-5d\n", this.id);
				System.out.println("├────────────────────────────");
				System.out.printf("│Port: %5d\n", socket.getPort());
				System.out.printf("│Time: %s\n", df.format(currentDate.getTime()));
				System.out.println("│Status: Connected");
				System.out.println("└────────────────────────────");
				System.out.printf("Request: %s\n", NumbersStr);
				System.out.println("└────────────────────────────\n");

				int[] numbers = StringToIntArray(NumbersStr);
				int max = getMax(numbers);
				int min = getMin(numbers);
				String reStr = "";

				reStr += String.format("╭───────────────────\n");
				reStr += String.format("│Minimum: %-6d\n", min);
				reStr += String.format("├───────────────────\n");
				reStr += String.format("│Maximum: %-6d\n", max);
				reStr += String.format("╰───────────────────");

				DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
				outstream.writeUTF(reStr);
			}
		} catch (IOException e) {
			try {
				this.socket.close();
			} catch (IOException e2) {
				;
			}
			System.out.println("┌────────────────────────────");
			System.out.printf("│          Client ID: %-5d\n", this.id);
			System.out.println("├────────────────────────────");
			System.out.printf("│Port: %5d\n", socket.getPort());
			System.out.printf("│Time: %s\n", df.format(currentDate.getTime()));
			System.out.println("│Status: Disconnected");
			System.out.println("└────────────────────────────\n");
		}
	}

	public int[] StringToIntArray(String str) {
		String[] items = str.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		int[] results = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			results[i] = Integer.parseInt(items[i].trim());
		}
		return results;
	}

	public int getMax(int[] myArray) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < myArray.length; i++) {
			if (myArray[i] > max) {
				max = myArray[i];
			}
		}
		return max;
	}

	public int getMin(int[] myArray) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < myArray.length; i++) {
			if (myArray[i] < min) {
				min = myArray[i];
			}
		}
		return min;
	}
}
