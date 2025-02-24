package t4DB5;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BuchFX {

	private Buch modellBuch;
	private SimpleStringProperty isbn;
	private SimpleStringProperty titel;
	private SimpleStringProperty thema;
	private SimpleStringProperty author;
	private SimpleIntegerProperty jahr;
	private SimpleIntegerProperty preis;

	public BuchFX(Buch modellBuch) {
		super();
		this.modellBuch = modellBuch;
		isbn = new SimpleStringProperty(modellBuch.getiSBN());
		titel = new SimpleStringProperty(modellBuch.getTitel());
		thema = new SimpleStringProperty(modellBuch.getThema());
		author = new SimpleStringProperty(modellBuch.getAuthor());
		jahr = new SimpleIntegerProperty(modellBuch.getYear());
		preis = new SimpleIntegerProperty(modellBuch.getPrice());
	}

	public Buch getModellBuch() {
		return modellBuch;
	}

	public final SimpleStringProperty isbnProperty() {
		return this.isbn;
	}

	public final String getIsbn() {
		return this.isbnProperty().get();
	}

	public final void setIsbn(final String isbn) {
		this.isbnProperty().set(isbn);
		modellBuch.setiSBN(isbn);
	}

	public final SimpleStringProperty titelProperty() {
		return this.titel;
	}

	public final String getTitel() {
		return this.titelProperty().get();
	}

	public final void setTitel(final String titel) {
		this.titelProperty().set(titel);
		modellBuch.setTitel(titel);
	}

	public final SimpleStringProperty themaProperty() {
		return this.thema;
	}

	public final String getThema() {
		return this.themaProperty().get();
	}

	public final void setThema(final String thema) {
		this.themaProperty().set(thema);
		modellBuch.setThema(thema);
	}

	public final SimpleStringProperty authorProperty() {
		return this.author;
	}

	public final String getAuthor() {
		return this.authorProperty().get();
	}

	public final void setAuthor(final String author) {
		this.authorProperty().set(author);
		modellBuch.setAuthor(author);
	}

	public final SimpleIntegerProperty jahrProperty() {
		return this.jahr;
	}

	public final int getJahr() {
		return this.jahrProperty().get();
	}

	public final void setJahr(final int jahr) {
		this.jahrProperty().set(jahr);
		modellBuch.setYear(jahr);
	}

	public final SimpleIntegerProperty preisProperty() {
		return this.preis;
	}

	public final int getPreis() {
		return this.preisProperty().get();
	}

	public final void setPreis(final int preis) {
		this.preisProperty().set(preis);
		modellBuch.setPrice(preis);
	}

}
