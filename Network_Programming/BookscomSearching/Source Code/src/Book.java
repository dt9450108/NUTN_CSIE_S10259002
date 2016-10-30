import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
	public static final SimpleDateFormat BOOK_PUBLISH_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private String name;
	private int price;
	private String author;
	private String publisher;
	private Date publishDate;

	public Book(String name, int price, String author, String publisher, String publishDateStr) throws ParseException {
		this.name = name;
		this.price = price;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = Book.BOOK_PUBLISH_FORMAT.parse(publishDateStr);
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getPriceStr() {
		return String.format("%d", this.price);
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public String getPublishDateStr() {
		return Book.BOOK_PUBLISH_FORMAT.format(publishDate);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setPublishDate(String publishDateStr) throws ParseException {
		this.publishDate = Book.BOOK_PUBLISH_FORMAT.parse(publishDateStr);
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s, %s, %s", name, getPriceStr(), author, publisher, getPublishDateStr());
	}
}
