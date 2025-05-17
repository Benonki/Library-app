package Classes.Coordinator.Util;

import java.io.Serializable;

public class BookOrder implements Serializable {

    private String title;
    private int bookID;
    private int amount;

    public BookOrder(int amount, String title, int bookID) {
        this.amount = amount;
        this.title = title;
        this.bookID = bookID;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
