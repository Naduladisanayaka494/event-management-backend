package evenhandler.even.handler.repository;

import evenhandler.even.handler.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    List<Attendee> findByEventId(Long eventId);
}
