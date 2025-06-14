package Classes.Employee.Util;

import java.io.Serializable;
import java.util.Date;

public class LibraryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int egzemplarzId;
    private String tytul;
    private String autor;
    private String status;
    private Date dataWydania;
    private String lokalizacja;
    private String wydawnictwo;
    private String typOkladki;
    private String isbn;

    public LibraryItem(int egzemplarzId, String tytul, String autor, String status,
                       Date dataWydania, String lokalizacja, String wydawnictwo, String typOkladki, String isbn) {
        this.egzemplarzId = egzemplarzId;
        this.tytul = tytul;
        this.autor = autor;
        this.status = status;
        this.dataWydania = dataWydania;
        this.lokalizacja = lokalizacja;
        this.wydawnictwo = wydawnictwo;
        this.typOkladki = typOkladki;
        this.isbn = isbn;
    }

    public int getEgzemplarzId() {
        return egzemplarzId;
    }

    public String getTytul() {
        return tytul;
    }

    public String getAutor() {
        return autor;
    }

    public String getStatus() {
        return status;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public String getWydawnictwo() {
        return wydawnictwo;
    }

    public String getTypOkladki() {
        return typOkladki;
    }

    public Date getDataWydania() { return dataWydania; }

    public String getIsbn() { return isbn; }
}