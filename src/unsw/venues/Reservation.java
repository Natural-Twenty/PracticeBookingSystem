package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;



public class Reservation {
    private Venue venue;
    private String id;
    private ArrayList<Room> rooms;
    private LocalDate start;
    private LocalDate end;
    /**
     * Constructor method to create a reservation.
     * @param venue The venue where the reservation belongs to
     * @param id Unique reservation ID
     * @param start Starting date of the reservation. Note: Dates are inclusive.
     * @param end Ending date of the rservation. Note: Dates are inclusive.
     */
    public Reservation(Venue venue, String id, LocalDate start, LocalDate end) {
        this.venue = venue;
        this.id = id;
        this.rooms = new ArrayList<Room>();
        this.start = start;
        this.end = end;
    }
    /**
     * Getter method to retrieve the Venue class which the reservation belongs to.
     * 
     * @return Returns a venue of class Venue.
     */
    public Venue getVenue() {
        return venue;
    }
    /**
     * Getter method to retrieve the unique ID of the reservaiton
     * @return Returns ID as type String
     */
    public String getID() {
        return id;
    }
    /**
     * Getter method to retrieve an ArrayList of rooms from the reservation
     * @return Returns an ArrayList of class Room
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    /**
     * Adds a room to the reservation
     * @param room A room to be added to the reservation
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }
    /**
     * Getter method to retrieve the starting date
     * @return Returns the starting date of the reservation
     */
    public LocalDate getStart() {
        return start;
    }
    /**
     * Getter method to retrieve ending date
     * @return Returns the ending date of the reservation
     */
    public LocalDate getEnd() {
        return end;
    }
    /**
     * Getter method to retreieve venue name
     * @return Returns the venue name as a String
     */
    public String getVenueName() {
        return venue.getName();
    }

    /**
     * Searches a given Reservations ArrayList and returns a Reservation ArrayList with reservations
     * containing the given room.
     * @param reservations An ArrayList of type Reservations
     * @param room A room of type room you want to search for
     * @return Returns an ArrayList of type Reservations containing the specified room
     */
    // Within each reservation, look through the rooms ArrayList for a room matching the
    // given room. If there is a match, add it to the newly created ArrayList of Reserveration.
    public static ArrayList<Reservation> searchReservation(ArrayList<Reservation> reservations, Room room) {
        ArrayList<Reservation> resultReservation = new ArrayList<Reservation>();
        for (Reservation reservation : reservations) {
            for (Room reservedRoom : reservation.getRooms()) {
                if (reservedRoom.getName().equals(room.getName())) {
                    resultReservation.add(reservation);
                }
            }
        }
        return resultReservation;
    }
    /**
     * Checks if there is a date overlap between the requested time and the room's reservation
     * times. Return is boolean. Note: dates are inclusive so overlap occurs if a reserved date equals a requeted date.
     * @param reservations ArrayList of type Reservation
     * @param start Requested start date.
     * @param end Requested end date.
     * @return 
     */
    // Date DOES NOT overlap if requeted start and end dates are either before reserved start dates
    // OR after reserved end dates. 
    public static boolean hasDateOverlap(ArrayList<Reservation> reservations, LocalDate start, LocalDate end) {
        for (Reservation reservation : reservations) {
            if ((start.isBefore(reservation.getStart()) && end.isBefore(reservation.getStart())) ||
                (start.isAfter(reservation.getEnd()) && end.isAfter(reservation.getEnd()))) {
                // No overlap so continue
                continue;
            } else {
                // Overlap encountered.
                return true;
            }
        }
        // Iterated through all reservations without overlap.
        return false;
    }
}