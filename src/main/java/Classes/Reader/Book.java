package Classes.Reader;

import java.time.LocalDate;

public class Book {
    private final int id;
    private final String title;
    private final String author;
    private final LocalDate reservationDate;
    private final LocalDate pickupDate;

    public Book(int id, String title, String author) {
        this(id, title, author, null, null);
    }

    public Book(int id, String title, String author, LocalDate reservationDate, LocalDate pickupDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.reservationDate = reservationDate;
        this.pickupDate = pickupDate;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDate getReservationDate() { return reservationDate; }
    public LocalDate getPickupDate() { return pickupDate; }
}
