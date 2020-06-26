package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A venue that contains a list of rooms within the venue and the reservations that it has. It also has a name.
 */
public class Venue {
    private String name;
    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;

    /**
     * Constructor method to create a venue.
     * The rooms and reservations ArrayList will intially be empty.
     * @param name The name of the venue
     */
    public Venue(String name) {
        this.name = name;
        this.rooms = new ArrayList<Room>();
        this.reservations = new ArrayList<Reservation>();
    }
    /**
     * Geter method to retrieve the name of the venue.
     * @return Returns the name of the venue as a String.
     */
    public String getName() {
        return name;
    }
    /**
     * Setter method to change the name off the venue.
     * @param name The new name of the venue
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Creates a new room with the parameters and adds it the
     * Room ArrayList in the instance.
     * @param name Name of the room
     * @param size Size of the room.
     */
    public void addRoom(String name, String size) {
        Room newRoom = new Room(this, name, size);
        rooms.add(newRoom);
    }
    /**
     * Removes a room from the ArrayList in the venue instance.
     * @param room The room to be removed.
     */
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    /**
     *  Getter method to retrieve an ArrayList of Room instances
     * @return An ArrayList of Room instances belonging to the venue
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    /**
     * Mmethod to search through a list of venues for a specific venue by name
     * @param venues An ArrayList of containing Venue instances.
     * @param venue_str The name of the venue as a String
     * @return Returns the venue instance if found, otherwise return null.
     */
    public static Venue searchVenue(ArrayList<Venue> venues, String venue_str) {
        // Empty arraylist given
        if (venues.size() == 0) {
            return null;
        }
        // Search through the arraylist of venue instances and check for one that matches the given name.
        for (Venue venue : venues) {
            if (venue.getName().equals(venue_str)) {
                // Venue name match
                return venue;
            }
        }
        // No venue with the given name found.
        return null;
    }

    public static Reservation searchReservation(ArrayList<Venue> venues, String id) {
        for (Venue venue : venues) {
            Reservation result = Reservation.searchReservation(venue.getReservation(), id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Getter method to retrieve an arraylist of reservation instances
     * @return An arraylist of reservation instances belonging to the venue
     */
    public ArrayList<Reservation> getReservation() {
        return reservations;
    }
    /**
     * Adds a reservation to the reservation list in the instane.
     * @param reservation The reservation to be added.
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
    


    /**
     * Checks the rooms in the venue for available rooms.
     * Available rooms are the first rooms that satisfy the request.
     * These rooms are then checked with the reservations to see if the room is reserved.
     * These rooms are added to a created ArrayList of Room's if they do no overlap the reservation dates.
     * Only adds rooms are needed. No excess rooms are added.
     * 
     * @param start Starting date of the request.
     * @param end Ending date of the request.
     * @param small Number of small rooms requested.
     * @param medium Number of medium rooms requested.
     * @param large Number of large rooms requested.
     * @return Returns an ArrayList of rooms that fulfil the request; if request cannot be fulfilled, return null.
     */
    private ArrayList<Room> roomAvailability(LocalDate start, LocalDate end, int small, int medium, int large) {
        ArrayList<Room> availableRooms = new ArrayList<Room>();
        // Iterate for all rooms in the venue
        for (Room room : rooms) {
            // Search the venue's reservations that contain the specified room.
            ArrayList<Reservation> reservationsWithRoom = Reservation.searchReservation(reservations, room);
            // Try to find rooms that fulfil the request.
            switch(room.getSize()) {
                case "small":
                    // Ignore if no small rooms are needed.
                    if (small > 0) {
                        // If there no reservations with the room or the reserved dates do not
                        // overlap, then add the room sinced it is available.
                        if(reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {

                            availableRooms.add(room);
                            small--;
                        }                    
                    }
                    break;

                case "medium":
                    // Ignore if no medium rooms are needed.
                    if (medium > 0) {
                        
                        if (reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {
                            
                            availableRooms.add(room);
                            medium--;
                        }
                    }
                    break;

                case "large":
                    // Ignore if no large rooms ar eneeded.
                    if (large > 0) {
                        if (reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {
                                
                            availableRooms.add(room);
                            large--;
                        }
                    break;
                    }
            }
        }
        // A request cannot be fulfilled if there are no rooms available in the venue
        if (small > 0 || medium > 0 || large > 0) {
        
            return null;
        } else {
            return availableRooms;
        }
    }
    /**
     * Creates a reservation with given parameters and 
     * adds it to this venue's ArrayList of Reservation's.
     * 
     * @param id Unique reservation ID.
     * @param rooms ArrayList of Rooms to be reserved.
     * @param start Starting date of the reservation.
     * @param end Ending date of the reservation.
     * @return Returns the created reservation.
     */
    public Reservation makeReservation(String id, ArrayList<Room> rooms, LocalDate start, LocalDate end) {
        Reservation reservation = new Reservation(this, id, start, end);
        // Add the given rooms to the reservation.
        for (Room room : rooms) {
            reservation.addRoom(room);
        }
        // Add the reservation to this venue's list of reservations
        this.addReservation(reservation);
        return reservation;
    }

    /**
     * Retreives the rooms that can fulfil the request.
     * @param start Requested start date
     * @param end Requested end date
     * @param small Requested number of small rooms
     * @param medium Requested number of medium rooms
     * @param large Requeted number of large rooms
     * @return An arraylist of Room instanes that can fulfil the request. Returns null if request cannot be fulfilled
     */
    public ArrayList<Room> getAvailableRooms(LocalDate start, LocalDate end,
            int small, int medium, int large) {
        // Prepare temporary variables since the function using them will change. We do not want to change the original small, medium and large parameters.
        int tmpSmall = small;
        int tmpMedium = medium;
        int tmpLarge = large;
        return roomAvailability(start, end, tmpSmall, tmpMedium, tmpLarge);
        
    }

    /**
     * Cancels a resrvation
     * @param reservation The reservation to cancel
     */
    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
}