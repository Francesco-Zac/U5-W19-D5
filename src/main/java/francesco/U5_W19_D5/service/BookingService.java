package francesco.U5_W19_D5.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import francesco.U5_W19_D5.model.Booking;
import francesco.U5_W19_D5.model.User;
import francesco.U5_W19_D5.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
