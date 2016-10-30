import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		Socket tmp = null;
		boolean listening = true;
		Date currentDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
		int clientCounts = 0;

		try {
			serverSocket = new ServerSocket(52345);
			System.out.println("┌────────────────────────────");
			System.out.println("│          Server Started         ");
			System.out.println("├────────────────────────────");
			System.out.printf("│Port: %5d\n", serverSocket.getLocalPort());
			System.out.printf("│Time: %s\n", df.format(currentDate.getTime()));
			System.out.println("└────────────────────────────\n");
		} catch (IOException e) {
			System.out.println("┌────────────────────────────");
			System.out.println("│           Server Error         ");
			System.out.println("├────────────────────────────");
			System.out.println("│Could Not Listen on Port \"52345\"");
			System.out.printf("│Time: %s\n", df.format(currentDate.getTime()));
			System.out.println("└────────────────────────────\n");
			System.exit(-1);
		}
		while (listening) {
			if ((tmp = serverSocket.accept()) != null) {
				new MyServerThread(tmp, ++clientCounts).start();
			}
		}

		serverSocket.close();
	}
}
