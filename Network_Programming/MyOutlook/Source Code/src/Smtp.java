import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Smtp {
	public static final int SMTP_PORT = 25;
	public static String encode;
	private String server;
	private String username;
	private String password;
	private Socket smtp;
	private BufferedReader smtpIn;
	private PrintWriter smtpOut;

	public Smtp(String server, String username, String password) {
		this.server = server;
		this.username = username;
		this.password = password;
		if (File.separator.equals("/")) {
			this.encode = "UTF-8";
		} else {
			this.encode = "BIG5";
		}
	}

	public boolean login() {
		try {
			smtp = new Socket(server, SMTP_PORT);
			smtpIn = new BufferedReader(new InputStreamReader(smtp.getInputStream(), this.encode));
			smtpOut = new PrintWriter(smtp.getOutputStream());

			// check if success to connecting
			response(220);
			return true;
		} catch (IOException e) {
			System.out.println("Smtp login ERROR");
			e.printStackTrace();
		}
		return false;
	}

	public void logout() {
		try {
			sendCmd("QUIT", 221);
			smtp.close();
		} catch (IOException e) {
			System.out.println("Smtp logout ERROR");
			e.printStackTrace();
		}
	}

	public void sendCmd(String cmd, int rcode) throws IOException {
		smtpOut.print(cmd + "\r\n");
		smtpOut.flush();
		System.out.println("Smtp send >> cmd: " + cmd);
		response(rcode);
	}

	public void response(int rcode) throws IOException {
		String response = smtpIn.readLine();

		if (response == null) {
			System.out.println("Smtp response is NULL");
		}

		System.out.println("Smtp response >> rcode: " + rcode);
		if (response != null && Integer.parseInt(response.substring(0, 3)) != rcode) {
			//			smtp.close();
			throw new RuntimeException(response);
		}
	}

	public boolean sendMail(String subject, String[] destEmail, String content) {
		try {
			// say hello
			String myHost = InetAddress.getLocalHost().getHostName();
			sendCmd("HELO " + myHost, 250);

			// auth login
			sendCmd("AUTH LOGIN", 334);

			// encode username and password
			String enUsername = Base64.encode(username.getBytes());
			String enPassword = Base64.encode(password.getBytes());

			// login
			sendCmd(enUsername, 334);
			sendCmd(enPassword, 235);

			// set sender
			sendCmd("MAIL FROM: " + username, 250);

			// set receivers
			for (int i = 0; i < destEmail.length; i++)
				sendCmd("RCPT TO: " + destEmail[i], 250);

			// start set email relative data
			sendCmd("DATA", 354);

			// set subject
			smtpOut.print("Subject: " + subject + "\r\n");
			smtpOut.print("Content-Type: text/plain; charset=" + encode + "\r\n");
			smtpOut.print("Content-Transfer-Encoding: base64\r\n");
			smtpOut.print("\r\n");
			smtpOut.print(Base64.encode(content.getBytes(encode)) + "\r\n");
			sendCmd("\r\n.", 250);
			return true;
		} catch (IOException e) {
			System.out.println("Smtp sendMail ERROR");
			e.printStackTrace();
		}
		return false;
	}

	public Socket getSmtp() {
		return smtp;
	}
}
