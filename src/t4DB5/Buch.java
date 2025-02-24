package t4DB5;

import java.time.Year;

public class Buch {
	private String iSBN;
	private String titel;
	private String thema;
	private String author;
	private int year;
	private int price;

	public Buch(String iSBN, String titel, String thema, String author, int year, int price) {
		super();
		this.iSBN = iSBN;
		this.titel = titel;
		this.thema = thema;
		this.author = author;
		this.year = year;
		this.price = price;
	}

	public Buch() {
		year = Year.now().getValue();
	}

	@Override
	public String toString() {
		return "Buch [iSBN=" + iSBN + ", titel=" + titel + ", thema=" + thema + ", author=" + author + ", year=" + year
				+ ", price=" + price + "]";
	}

	public String getiSBN() {
		return iSBN;
	}

	public void setiSBN(String iSBN) {
		this.iSBN = iSBN;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getThema() {
		return thema;
	}

	public void setThema(String thema) {
		this.thema = thema;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
