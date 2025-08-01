package francesco.U5_W19_D5.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private int availableSeats;

    @ManyToOne
    private User organizer;

    private User creator;


    public User getCreator() {
        return creator;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }



    public String getLocation() {
        return location;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
}
