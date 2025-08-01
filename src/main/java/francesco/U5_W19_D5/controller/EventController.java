package francesco.U5_W19_D5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import francesco.U5_W19_D5.model.Event;
import francesco.U5_W19_D5.model.User;
import francesco.U5_W19_D5.service.EventService;
import francesco.U5_W19_D5.service.UserService;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Event> getEvents() {
        return eventService.findAll();
    }

    @PostMapping("/create")
    public String createEvent(@RequestBody Event event) {
        if (event.getCreator() == null || event.getCreator().getId() == null) {
            return "Evento senza creatore non valido";
        }

        Optional<User> creator = userService.findById(event.getCreator().getId());
        if (creator.isEmpty() || !creator.get().getRole().equals("ORGANIZER")) {
            return "Creatore non valido o non organizzatore";
        }

        eventService.saveEvent(event);
        return "Evento creato";
    }

    @PutMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Optional<Event> existing = eventService.findById(id);
        if (existing.isEmpty()) {
            return "Evento non trovato";
        }

        Event e = existing.get();
        e.setTitle(event.getTitle());
        e.setDescription(event.getDescription());
        e.setDate(event.getDate());
        e.setLocation(event.getLocation());
        e.setAvailableSeats(event.getAvailableSeats());

        eventService.saveEvent(e);
        return "Evento aggiornato";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        Optional<Event> existing = eventService.findById(id);
        if (existing.isEmpty()) {
            return "Evento non trovato";
        }
        eventService.deleteEvent(id);
        return "Evento eliminato";
    }

    @PostMapping("/book/{eventId}/user/{userId}")
    public String bookSeat(@PathVariable Long eventId, @PathVariable Long userId) {
        Optional<Event> eventOpt = eventService.findById(eventId);
        Optional<User> userOpt = userService.findById(userId);

        if (eventOpt.isEmpty()) {
            return "Evento non trovato";
        }
        if (userOpt.isEmpty()) {
            return "Utente non trovato";
        }

        Event event = eventOpt.get();

        if (event.getAvailableSeats() <= 0) {
            return "Nessun posto disponibile";
        }

        boolean booked = eventService.bookSeat(event, userOpt.get());
        if (booked) {
            return "Prenotazione effettuata";
        } else {
            return "Prenotazione fallita";
        }
    }
}
