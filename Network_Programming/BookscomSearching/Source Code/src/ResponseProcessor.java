import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Vector;

public class ResponseProcessor {
	private Vector<Book> books;
	private FileWriter fwout;
	private FileWriter fwBook;
	private int BookLimit = 0;

	public ResponseProcessor() {
		books = new Vector<Book>();
	}

	public ResponseProcessor(BufferedReader in, int BookLimit) throws UnsupportedEncodingException, IOException, ParseException {
		books = new Vector<Book>();
		this.BookLimit = BookLimit;
		process(in);
	}

	public void openf() throws IOException {
		fwout = new FileWriter("response.txt", false);
		fwBook = new FileWriter("book.txt", false);
	}

	public void closef() throws IOException {
		fwout.close();
		fwBook.close();
	}

	public void process(BufferedReader in) throws UnsupportedEncodingException, IOException, ParseException {
		int s, e, price = 0;
		boolean nameFetched = false, priceFetched = false;
		String line, name = "", authors = "", publisher = "", publishDateStr = "";

		for (int i = 0; i < 100; i++) {
			// skip unnecessary comparison
			line = in.readLine();
			fwout.write(line + "\r\n");
			fwout.flush();
		}

		while ((line = in.readLine()) != null) {
			if (books.size() < this.BookLimit) {
				if (line.indexOf("rel=\"mid_name\"") != -1) {
					// book name
					if ((s = line.indexOf("title=\"")) != -1) {
						e = line.indexOf("\"", s + 7);
						name = line.substring(s + 7, e).replace("&nbsp;", "").replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&quot;", "\"").replace("&apos;", "'").replace("&cent;", "￠").replace("&pound;", "£").replace("&yen;", "¥").replace("&euro;", "€").replace("&sect;", "§").replace("&copy;", "©").replace("&reg;", "®").replace("&trade;", "™").replace("&times;", "×").replace("&divide;", "÷").replace("&rsquo;", "’");
						nameFetched = true;
					}
				} else if (line.indexOf("rel=\"go_author\"") != -1) {
					// book author
					if ((s = line.indexOf("title=\"")) != -1) {
						e = line.indexOf("\"", s + 7);
						authors += line.substring(s + 7, e) + " ";
					}
				} else if ((s = line.indexOf("，出版日期：")) != -1) {
					// book published date
					s += 6;
					e = line.toLowerCase().indexOf("<br>", s);
					publishDateStr = line.substring(s, e);
				} else if (line.indexOf("rel=\"mid_publish\"") != -1) {
					// book publisher
					if ((s = line.indexOf("title=\"")) != -1) {
						e = line.indexOf("\"", s + 7);
						publisher += line.substring(s + 7, e) + " ";
					}
				} else if (line.indexOf("優惠價：") != -1) {
					// book price
					if ((s = line.lastIndexOf("<b>")) != -1) {
						priceFetched = true;
						e = line.indexOf("</b>", s);
						price = Integer.parseInt(line.substring(s + 3, e));
					}
				}
				if (nameFetched && priceFetched) {
					books.addElement(new Book(name, price, authors, publisher, publishDateStr));
					fwBook.write(name + "\r\n");
					fwBook.flush();
					nameFetched = false;
					priceFetched = false;
					authors = "";
					publisher = "";
				}
			}
			fwout.write(line + "\r\n");
			fwout.flush();
		}
		in.close();
	}

	public Vector<Book> getBooks() {
		return books;
	}

	public int getBookLimit() {
		return BookLimit;
	}

	public void setBooks(Vector<Book> books) {
		this.books = books;
	}

	public void setBookLimit(int bookLimit) {
		BookLimit = bookLimit;
	}
}
