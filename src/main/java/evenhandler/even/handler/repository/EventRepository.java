package evenhandler.even.handler.repository;

import evenhandler.even.handler.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
