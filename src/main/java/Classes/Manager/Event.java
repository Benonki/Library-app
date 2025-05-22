package Classes.Manager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Serializable {
    private int id;
    private String theme;
    private LocalDate date;
    private LocalTime time;
    private String place;

    public Event(int id, String theme, LocalDate date, LocalTime time, String place) {
        this.id = id;
        this.theme = theme != null ? theme : "";
        this.date = date != null ? date : LocalDate.now();
        this.time = time != null ? time : LocalTime.MIDNIGHT;
        this.place = place != null ? place : "";
    }

    public int getId() { return id; }
    public String getTheme() { return theme; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getPlace() { return place; }

    public void setId(int id) { this.id = id; }
    public void setTheme(String theme) { this.theme = theme; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setPlace(String place) { this.place = place; }
}