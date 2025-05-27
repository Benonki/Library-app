package Classes.Employee.Util;

import java.io.Serializable;

public class LibraryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ksiazkaId;
    private String tytul;
    private String autor;
    private int ilosc;

    public LibraryItem(int ksiazkaId, String tytul, String autor, int ilosc) {
        this.ksiazkaId = ksiazkaId;
        this.tytul = tytul;
        this.autor = autor;
        this.ilosc = ilosc;
    }

    public int getKsiazkaId() {
        return ksiazkaId;
    }

    public String getTytul() {
        return tytul;
    }

    public String getAutor() {
        return autor;
    }

    public int getIlosc() {
        return ilosc;
    }
}