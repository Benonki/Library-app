package Classes.Coordinator.Util;

import java.io.Serializable;

public class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int magazynId;
    private String tytul;
    private int ilosc;
    private int rzad;
    private int sektor;
    private int polka;
    private int miejsceNaPolce;

    public int getSektor() {
        return sektor;
    }

    public int getMagazynId() {
        return magazynId;
    }

    public String getTytul() {
        return tytul;
    }

    public int getIlosc() {
        return ilosc;
    }

    public int getRzad() {
        return rzad;
    }

    public int getPolka() {
        return polka;
    }

    public int getMiejsceNaPolce() {
        return miejsceNaPolce;
    }

    public InventoryItem(int magazynId, String tytul, int ilosc, int rzad, int sektor, int polka, int miejsceNaPolce) {
        this.magazynId = magazynId;
        this.tytul = tytul;
        this.ilosc = ilosc;
        this.rzad = rzad;
        this.sektor = sektor;
        this.polka = polka;
        this.miejsceNaPolce = miejsceNaPolce;
    }
}
