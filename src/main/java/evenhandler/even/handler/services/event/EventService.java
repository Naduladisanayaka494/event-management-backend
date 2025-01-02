package evenhandler.even.handler.services.event;

import evenhandler.even.handler.dto.AttendeeDTO;
import evenhandler.even.handler.dto.EventAnalyticsDTO;
import evenhandler.even.handler.dto.EventDTO;
import evenhandler.even.handler.entity.Attendee;
import evenhandler.even.handler.entity.Event;
import evenhandler.even.handler.repository.AttendeeRepository;
import evenhandler.even.handler.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AttendeeRepository attendeeRepository;

    public List<EventDTO> getAllEvents(String date, String location, String tags) {
        // Add filtering logic here
        return eventRepository.findAll().stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setDescription(event.getDescription());
            dto.setDate(event.getDate());
            dto.setLocation(event.getLocation());
            dto.setCreatedBy(event.getCreatedBy());
            dto.setCapacity(event.getCapacity());
            dto.setRemainingCapacity(event.getRemainingCapacity());
            dto.setTags(event.getTags());
            return dto;
        }).toList();
    }

    public EventDTO addEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setCreatedBy(eventDTO.getCreatedBy());
        event.setCapacity(eventDTO.getCapacity());
        event.setRemainingCapacity(eventDTO.getCapacity()); // Initially set to capacity
        event.setTags(eventDTO.getTags());

        Event savedEvent = eventRepository.save(event);

        eventDTO.setId(savedEvent.getId());
        return eventDTO;
    }

    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }

        Event event = eventOpt.get();
        if (event.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot update past events");
        }

        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setCreatedBy(eventDTO.getCreatedBy());
        event.setCapacity(eventDTO.getCapacity());
        event.setRemainingCapacity(eventDTO.getRemainingCapacity());
        event.setTags(eventDTO.getTags());

        Event updatedEvent = eventRepository.save(event);

        eventDTO.setId(updatedEvent.getId());
        return eventDTO;
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }

        Event event = eventOpt.get();
        eventRepository.delete(event);
    }

    public List<AttendeeDTO> getAttendees(Long eventId) {
        List<Attendee> attendees = attendeeRepository.findByEventId(eventId);
        return attendees.stream().map(attendee -> {
            AttendeeDTO dto = new AttendeeDTO();
            dto.setId(attendee.getId());
            dto.setName(attendee.getName());
            dto.setEmail(attendee.getEmail());
            return dto;
        }).toList();
    }

    public void registerAttendee(Long eventId, AttendeeDTO attendeeDTO) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }

        Event event = eventOpt.get();
        if (event.getRemainingCapacity() <= 0) {
            throw new IllegalArgumentException("No remaining capacity");
        }

        Attendee attendee = new Attendee();
        attendee.setName(attendeeDTO.getName());
        attendee.setEmail(attendeeDTO.getEmail());
        attendee.setEvent(event);

        attendeeRepository.save(attendee);

        event.setRemainingCapacity(event.getRemainingCapacity() - 1);
        eventRepository.save(event);
    }

    public EventAnalyticsDTO getEventAnalytics(Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }

        Event event = eventOpt.get();
        EventAnalyticsDTO analyticsDTO = new EventAnalyticsDTO();
        analyticsDTO.setTotalAttendees(attendeeRepository.findByEventId(eventId).size());
        analyticsDTO.setCapacityUtilization((double) analyticsDTO.getTotalAttendees() / event.getCapacity() * 100000);
        return analyticsDTO;
    }
}
