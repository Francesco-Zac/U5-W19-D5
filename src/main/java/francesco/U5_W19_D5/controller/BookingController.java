package francesco.U5_W19_D5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import francesco.U5_W19_D5.model.Booking;
import francesco.U5_W19_D5.model.Event;
import francesco.U5_W19_D5.model.User;
import francesco.U5_W19_D5.service.BookingService;
import francesco.U5_W19_D5.service.EventService;
import francesco.U5_W19_D5.service.UserService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @PostMapping("/book/{eventId}")
    public String createBooking(@PathVariable Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Event> eventOpt = eventService.findById(eventId);

        if (eventOpt.isEmpty()) {
            return "Evento non trovato";
        }
        if (user.isEmpty()) {
            return "Utente non autenticato";
        }

        Event event = eventOpt.get();

        if (event.getAvailableSeats() <= 0) {
            return "Nessun posto disponibile";
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventService.saveEvent(event);

        Booking booking = new Booking();
        booking.setUser(user.get());
        booking.setEvent(event);
        bookingService.saveBooking(booking);

        return "Prenotazione effettuata";
    }

    @GetMapping("/my")
    public List<Booking> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return bookingService.findByUser(user.get());
    }

    @DeleteMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Booking> bookingOpt = bookingService.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            return "Prenotazione non trovata";
        }
        if (user.isEmpty()) {
            return "Utente non autenticato";
        }

        Booking booking = bookingOpt.get();
        if (!booking.getUser().getId().equals(user.get().getId())) {
            return "Non puoi cancellare questa prenotazione";
        }

        Event event = booking.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventService.saveEvent(event);

        bookingService.deleteById(bookingId);
        return "Prenotazione cancellata";
    }
}