package t4DB5;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class KundeFX {

	private Kunde modellKunde;
	private SimpleLongProperty id;
	private SimpleStringProperty name;
	private SimpleObjectProperty<LocalDate> datum;
	private ArrayList<BuchFX> bestellungen = new ArrayList<>();

	public KundeFX(Kunde modellKunde) {
		super();
		this.modellKunde = modellKunde;
		id = new SimpleLongProperty(modellKunde.getId());
		name = new SimpleStringProperty(modellKunde.getName());
		datum = new SimpleObjectProperty<>(modellKunde.getDatum());
	}

	public ArrayList<BuchFX> getBestellungen() {
		return bestellungen;
	}

	public void setBestellungen(ArrayList<BuchFX> bestellungen) {
		this.bestellungen = bestellungen;
	}

	public Kunde getModellKunde() {
		return modellKunde;
	}

	public final SimpleLongProperty idProperty() {
		return this.id;
	}

	public final long getId() {
		return this.idProperty().get();
	}

/*	public final void setId(final long id) {
		this.idProperty().set(id);
	}*/

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	/*public final void setName(final String name) {
		this.nameProperty().set(name);
	}*/

	public final SimpleObjectProperty<LocalDate> datumProperty() {
		return this.datum;
	}

	public final LocalDate getDatum() {
		return this.datumProperty().get();
	}

	/*public final void setDatum(final LocalDate datum) {
		this.datumProperty().set(datum);
	}*/

}
