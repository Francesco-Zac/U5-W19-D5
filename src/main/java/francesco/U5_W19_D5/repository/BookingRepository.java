package francesco.U5_W19_D5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import francesco.U5_W19_D5.model.Booking;
import java.util.List;
import francesco.U5_W19_D5.model.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
