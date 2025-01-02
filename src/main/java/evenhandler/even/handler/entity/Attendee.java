package evenhandler.even.handler.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Attendee ")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    private String email;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // Getters and setters
}
