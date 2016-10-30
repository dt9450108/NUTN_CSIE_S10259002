import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class MyClient {
	private Socket socket;

	public MyClient(String iaddr, int port) {
		try {
			this.socket = new Socket(InetAddress.getByName(iaddr), port);
			System.out.printf("Connect to Server:\"%s:%d\"\n\n", iaddr, port);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String send2Server(String numStr) {
		try {
			DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
			outstream.writeUTF(numStr);
			DataInputStream instream = new DataInputStream(socket.getInputStream());
			String messagein = instream.readUTF();
			return messagein;
		} catch (IOException e) {
			return null;
		}
	}

	public Socket getSocket() {
		return this.socket;
	}

	public static boolean isInt(String str) {
		return str.matches("^[-+]?\\d+$");
	}

	public static boolean isUInt(String str) {
		if (str.matches("^[-+]?\\d+$"))
			return (Integer.parseInt(str) >= 0);
		return false;
	}

	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("USAGE: java Client11_6 [iaddr] [port]");
			System.exit(1);
		}
		String iaddr = args[0];
		int port = Integer.parseInt(args[1]);
		Scanner jin = new Scanner(System.in);
		MyClient clientStart = new MyClient(iaddr, port);
		int size;
		int[] numbers = null;
		String inputStr;

		while (true) {
			System.out.println("┌───────────────────────────────────────────");
			System.out.println("|    Enter how many numbers you want to input: ");
			System.out.print("|    ");
			inputStr = jin.next();

			while (!isUInt(inputStr)) {
				System.out.println("|  Please enter positive integer: ");
				System.out.print("|  ");
				inputStr = jin.next();
			}

			size = Integer.parseInt(inputStr);
			if (size != 0) {
				numbers = new int[size];
				System.out.println("├───────────────────────────────────────────");
				System.out.println("|    Enter the number:");
				System.out.print("|    ");
				for (int i = 0; i < size; i++) {
					inputStr = jin.next();
					while (!isInt(inputStr)) {
						System.out.println("|  Please enter integer: ");
						System.out.print("|  ");
						inputStr = jin.next();
					}
					numbers[i] = Integer.parseInt(inputStr);
				}
				System.out.println("└───────────────────────────────────────────");

				String messageout = Arrays.toString(numbers);
				String returnStr = clientStart.send2Server(messageout);
				if (returnStr != null) {
					System.out.printf("\n%s\n\n", returnStr);
				}

				// delete dynamic 
				numbers = null;
			} else {
				System.out.println("┌───────────────────────────────────────────");
				System.out.println("|   You don't enter any number!");
				System.out.println("└───────────────────────────────────────────");
			}

			System.out.println("┌────────────────────────");
			System.out.println("|     Exit?(Yes=y, No=n)     ");
			System.out.println("├────────────────────────");
			System.out.print("|Action: ");

			char exit = jin.next().charAt(0);
			if (exit == 'Y' || exit == 'y') {
				System.out.println("|Status: Disconnected");
				System.out.println("└────────────────────────");
				try {
					clientStart.getSocket().close();
				} catch (IOException e) {
					;
				}
				clientStart.send2Server("[]");
				break;
			}
			System.out.println("└────────────────────────");
			System.out.println();
		}
		jin.close();
	}
}
