package Classes.Coordinator;

import java.io.Serializable;

public class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int magazynId;
    private int ksiazkaId;
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

    public int getKsiazkaId() {
        return ksiazkaId;
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

    public InventoryItem(int magazynId, int ksiazkaId, int ilosc, int rzad, int sektor, int polka, int miejsceNaPolce) {
        this.magazynId = magazynId;
        this.ksiazkaId = ksiazkaId;
        this.ilosc = ilosc;
        this.rzad = rzad;
        this.sektor = sektor;
        this.polka = polka;
        this.miejsceNaPolce = miejsceNaPolce;
    }
}
