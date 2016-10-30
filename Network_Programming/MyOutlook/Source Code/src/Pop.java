import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pop {
	public static final int POP_PORT = 110;
	public static String encode;
	private String server;
	private String username;
	private String password;
	private Socket popServer;
	private BufferedReader popIn;
	private PrintWriter popOut;

	public Pop(String server, String username, String password) {
		this.server = server;
		this.username = username;
		this.password = password;
		if (File.separator.equals("/")) {
			this.encode = "UTF-8";
		} else {
			this.encode = "BIG5";
		}
	}

	public boolean sendCmd(String cmd) throws IOException {
		popOut.print(cmd + "\r\n");
		popOut.flush();
		System.out.println("Pop sendCmd >> Cmd: " + cmd);
		return response();
	}

	public boolean response() throws IOException {
		String response = popIn.readLine();
		System.out.println("Pop sendCmd >> Response: " + response);
		if (response != null && !response.startsWith("+OK"))
			return false;
		return true;
	}

	public void doDelete(int number) {
		try {
			if (sendCmd("DELE " + number)) {
				System.out.println("Pop doDelete SUCCESS");
			} else {
				System.out.println("Pop doRetrmail FAILURE");
			}
		} catch (IOException e) {
			System.out.println("Pop doRetrmail ERROR");
			e.printStackTrace();
		}
	}

	public Mail doRetrmail(int number) {
		try {
			if (sendCmd("RETR " + number)) {
				Vector<String> mailContent = getLines();
				String subject = "無標題";
				String sender = "寄件者不詳";
				String content = "";
				String dateStr = "寄件日期不詳";
				int tfEncodeType = 0;
				for (int i = 0; i < mailContent.size(); i++) {
					String curStr = mailContent.get(i);
					System.out.println(curStr);
					int pos = -1;
					if ((pos = curStr.indexOf("Return-Path:")) >= 0) {
						sender = curStr.substring(pos + 12).trim();
					}
					if ((pos = curStr.indexOf("Subject:")) >= 0) {
						Pattern SUBJECT_REGEX = Pattern.compile("=\\?(?<encode>.+)\\?(?<m>[A-Za-z])\\?(?<content>.+)\\?=");
						subject = curStr.substring(pos + 8).replaceAll(" ", "");
						Matcher subjectRes = SUBJECT_REGEX.matcher(subject);
						if (subjectRes.matches()) {
							subjectRes.group(0);
							if (subjectRes.group(2).toLowerCase().equals("b")) {
								subject = new String(Base64.decode(subjectRes.group(3), subjectRes.group(1)), subjectRes.group(1));
							}
						}
						subject = subject.trim();
					}
					if ((pos = curStr.indexOf("Content-Transfer-Encoding:")) >= 0) {
						if (curStr.substring(pos + "Content-Transfer-Encoding:".length()).toLowerCase().indexOf("base64") >= 0)
							tfEncodeType = 1;
						else if (curStr.substring(pos + "Content-Transfer-Encoding:".length()).toLowerCase().indexOf("quoted-printable") >= 0)
							tfEncodeType = 2;
					}
					if ((pos = curStr.indexOf("Received:")) >= 0) {
						for (; i < mailContent.size(); i++) {
							System.out.println(mailContent.get(i));
							curStr = mailContent.get(i);
							if ((pos = curStr.indexOf(";")) >= 0) {
								dateStr = curStr.substring(pos + 1).trim();
								SimpleDateFormat sp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
								try {
									dateStr = MainUI.DATE_TIME_FORMAT.format(sp.parse(dateStr));
								} catch (ParseException e) {
									System.out.println("Pop parse email messsage date ERROR");
								}
								break;
							}
						}
					}
					if (curStr.length() == 0) {
						// mail content start
						for (i++; i < mailContent.size(); i++) {
							System.out.println(mailContent.get(i));
							switch (tfEncodeType) {
								case 1:
									content += new String(Base64.decode(mailContent.get(i), encode), encode) + "\n";
									break;
								case 2:
									content += new String(QuotedPrintable.decodeQuotedPrintable(mailContent.get(i).getBytes(encode)), encode) + "\n";
									break;
								default:
									content += mailContent.get(i) + "\n";
							}
						}
					}
				}
				return new Mail(sender, new Vector<String>(), subject, content, dateStr, Mail.STATUS.RECEIVE);
			} else {
				System.out.println("Pop doRetrmail FAILURE");
			}
		} catch (IOException e) {
			System.out.println("Pop doRetrmail ERROR");
			e.printStackTrace();
		}
		return null;
	}

	public Vector<String[]> doList() {
		try {
			if (sendCmd("LIST")) {
				Vector<String> list = getLines();
				Vector<String[]> listRes = new Vector<String[]>();
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
					listRes.addElement(list.get(i).split(" "));
				}
				return listRes;
			} else {
				System.out.println("Pop doList FAILURE");
				return null;
			}
		} catch (IOException e) {
			System.out.println("Pop doList ERROR");
			e.printStackTrace();
		}
		return null;
	}

	public Vector<String> getLines() throws IOException {
		Vector<String> result = new Vector<String>();
		String tmp;
		while (true) {
			tmp = popIn.readLine();
			if (".".equals(tmp))
				break;
			result.addElement(tmp);
		}
		return result;
	}

	public boolean login() {
		try {
			popServer = new Socket(server, POP_PORT);
			popIn = new BufferedReader(new InputStreamReader(popServer.getInputStream(), this.encode));
			popOut = new PrintWriter(popServer.getOutputStream());

			if (!response())
				return false;
			return sendCmd("USER " + username) && sendCmd("PASS " + password);
		} catch (IOException e) {
			System.out.println("Pop login ERROR");
			e.printStackTrace();
		}
		return false;
	}

	public void logout() {
		try {
			sendCmd("QUIT");
			popServer.close();
		} catch (IOException e) {
			System.out.println("Pop logout ERROR");
			e.printStackTrace();
		}
	}

	public Socket getPopServer() {
		return popServer;
	}
}