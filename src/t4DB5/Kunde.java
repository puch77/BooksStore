package t4DB5;

import java.time.LocalDate;

public class Kunde {
	private long id;
	private String name;
	private LocalDate datum;
	public Kunde(long id, String name, LocalDate datum) {
		super();
		this.id = id;
		this.name = name;
		this.datum = datum;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	
	
}
