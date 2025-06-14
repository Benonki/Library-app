package Classes.Employee.Util;

import java.io.Serializable;
import java.util.Date;

public class NewBookData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tytul;
    private String isbn;
    private Date dataWydania;
    private String imieAutora;
    private String nazwiskoAutora;
    private String wydawnictwo;
    private String typOkladki;
    private String lokalizacja;
    private int egzemplarzId;
    private String status;

    public NewBookData(String tytul, String imieAutora, String nazwiskoAutora, String isbn,
                       Date dataWydania, String wydawnictwo, String typOkladki) {
        this.tytul = tytul;
        this.imieAutora = imieAutora;
        this.nazwiskoAutora = nazwiskoAutora;
        this.isbn = isbn;
        this.dataWydania = dataWydania;
        this.wydawnictwo = wydawnictwo;
        this.typOkladki = typOkladki;
    }

    public String getTytul() {
        return tytul;
    }

    public String getIsbn() {
        return isbn;
    }

    public Date getDataWydania() {
        return dataWydania;
    }

    public String getImieAutora() {
        return imieAutora;
    }

    public String getNazwiskoAutora() {
        return nazwiskoAutora;
    }

    public String getWydawnictwo() {
        return wydawnictwo;
    }

    public String getTypOkladki() { return typOkladki; }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public int getEgzemplarzId() { return egzemplarzId; }

    public void setEgzemplarzId(int egzemplarzId) { this.egzemplarzId = egzemplarzId; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}