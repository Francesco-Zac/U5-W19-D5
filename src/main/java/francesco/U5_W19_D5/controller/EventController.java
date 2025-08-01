package francesco.U5_W19_D5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> creator = userService.findByUsername(username);

        if (creator.isEmpty() || !creator.get().getRole().equals("ORGANIZER")) {
            return "Solo gli organizzatori possono creare eventi";
        }

        event.setOrganizer(creator.get());
        eventService.saveEvent(event);
        return "Evento creato";
    }

    @PutMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @RequestBody Event event) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Event> existing = eventService.findById(id);

        if (existing.isEmpty()) {
            return "Evento non trovato";
        }
        if (user.isEmpty()) {
            return "Utente non autenticato";
        }

        Event e = existing.get();
        if (!e.getOrganizer().getId().equals(user.get().getId())) {
            return "Non puoi modificare questo evento";
        }

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Event> existing = eventService.findById(id);

        if (existing.isEmpty()) {
            return "Evento non trovato";
        }
        if (user.isEmpty()) {
            return "Utente non autenticato";
        }

        Event e = existing.get();
        if (!e.getOrganizer().getId().equals(user.get().getId())) {
            return "Non puoi eliminare questo evento";
        }

        eventService.deleteEvent(id);
        return "Evento eliminato";
    }
}