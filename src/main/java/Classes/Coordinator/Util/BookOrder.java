package Classes.Coordinator.Util;

import java.io.Serializable;

public class BookOrder implements Serializable {

    private String Title;
    private int amount;

    public BookOrder(int amount, String title) {
        this.amount = amount;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
