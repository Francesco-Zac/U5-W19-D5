package francesco.U5_W19_D5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import francesco.U5_W19_D5.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
