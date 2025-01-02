package evenhandler.even.handler.controller;

import evenhandler.even.handler.dto.AttendeeDTO;
import evenhandler.even.handler.dto.EventAnalyticsDTO;
import evenhandler.even.handler.dto.EventDTO;
import evenhandler.even.handler.services.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String tags) {
        List<EventDTO> events = eventService.getAllEvents(date, location, tags);
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<EventDTO> addEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.addEvent(eventDTO);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}/attendees")
    public ResponseEntity<List<AttendeeDTO>> getAttendees(@PathVariable Long eventId) {
        List<AttendeeDTO> attendees = eventService.getAttendees(eventId);
        return ResponseEntity.ok(attendees);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<Void> registerAttendee(@PathVariable Long eventId, @RequestBody AttendeeDTO attendeeDTO) {
        eventService.registerAttendee(eventId, attendeeDTO);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{eventId}/analytics")
    public ResponseEntity<EventAnalyticsDTO> getEventAnalytics(@PathVariable Long eventId) {
        EventAnalyticsDTO analyticsDTO = eventService.getEventAnalytics(eventId);
        return ResponseEntity.ok(analyticsDTO);
    }
}
