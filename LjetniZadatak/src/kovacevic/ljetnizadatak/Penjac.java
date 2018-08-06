package kovacevic.ljetnizadatak;

public class Penjac {
	private int sifra;
	private String ime;
	private String prezime;
	private String rezultat;

	public int getSifra() {
		return sifra;
	}

	public void setSifra(int sifra) {
		this.sifra = sifra;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getRezultat() {
		return rezultat;
	}

	public void setRezultat(String rezultat) {
		this.rezultat = rezultat;
	}

	@Override
	public String toString() {
		return (getPrezime() + " " + getIme());

	}
}
