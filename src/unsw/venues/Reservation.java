package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * A reservation that contains information on the venue where the reservation takes place, the unique id, list of rooms, starting and end date.
 */


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
     * Getter method to retrieve the start date in String form.
     * @return The start date in String form.
     */
    public String getStartString() {
        return getStart().toString();
    }
    /**
     * Getter method to retrieve ending date
     * @return Returns the ending date of the reservation
     */
    public LocalDate getEnd() {
        return end;
    }

    /**
     * Getter method to retrieve the end date in String form.
     * 
     * @return The end date in String form.
     */
    public String getEndString() {
        return getEnd().toString();
    }
    /**
     * Getter method to retreieve venue name
     * @return Returns the venue name as a String
     */
    public String getVenueName() {
        return venue.getName();
    }

    /**
     * Checks if the given id is equal to the reservation's id
     * @param id Unique id to be compared with.
     * @return True if the id is equal. Otherwise, return false.
     */
    private boolean IDequals(String id) {
        return this.id.equals(id);
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
            // No overlap condition logic
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

    /**
     * An overloaded method. Searches through an ArrayList of reservations according to input. This particular method searches for a reservation matching an id.
     * @param reservations An ArrayList of Reservation instances
     * @param id Unique id of a reservation to search for.
     * @return Returns the reservation instance matching the id. if none is found, retuen null.
     */
    public static Reservation searchReservation(ArrayList<Reservation> reservations, String id) {
        for (Reservation reservation : reservations) {
            if (reservation.IDequals(id)) {
                // reservation found
                return reservation;
            }
        }
        // id not found in this venue's reservation list.
        return null;
    }

    /**
     * An overloaded method. This particular method searches through reservations and finds reservations that contain the given room.
     * @param reservations ArrayList of Reservation instances
     * @param room Room instance to search reservations by.
     * @return An ArrayList of Reservation instanes that contain the toom.
     */
    
    public static ArrayList<Reservation> searchReservation(ArrayList<Reservation> reservations, Room room) {
        ArrayList<Reservation> result = new ArrayList<Reservation>();
        for (Reservation reservation : reservations) {
            if (Room.containsRoom(reservation.getRooms(), room)) {
                result.add(reservation);
            }
        }
        return result;
    }

    /**
     * Sorting method that sorts reservations by date (earliest to latest). It creates a new ArrayList of Reservation instances and destroys the old one.
     * @param reservations An ArrayList of Reservation instances.
     * @return A new ArrayList of Reservation instances sorted by date.
     */
    public static ArrayList<Reservation> sortByDate(ArrayList<Reservation> reservations) {
        ArrayList<Reservation> result = new ArrayList<Reservation>();
        // If given list only contains 1 instance, simple return since no sorting is needed.
        if (reservations.size() == 1) {
            return reservations;
        }
        // Store the original size of the reservation arraylist 
        int size = reservations.size();
        // Continue to operate until size of new arraylist matches the original. That way, we know it contains all the original instances but is now sorted.
        while (result.size() < size) {
            // Set earliest date to first as a placeholder.
            Reservation min = reservations.get(0);
            // Last element reached, no comparisons needed so add to new arraylist and remove from the original and return.
            if (reservations.size() == 1) {
                    result.add(min);
                    reservations.remove(min);
                    return result;
            }
            // Search for the earliest date and store it
            for (Reservation reservation : reservations) {
                if (reservation.dateIsBefore(min.getStart())) {
                    min = reservation;
                }
            }
            // Add the stored date to new arraylist and remove it from the original.
            result.add(min);
            reservations.remove(min);
        }
        return result;
    }
    
    /**
     * Checks whether the reservation's date is before the given date.
     * @param start The date to check.
     * @return True if the reservation's date is before the given date. Otherwise, false.
     */
    private boolean dateIsBefore(LocalDate start) {
        return this.start.isBefore(start);
    }

}