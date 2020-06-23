package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;

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
     * Getter method to search through a list of venues for a specific venue
     * @param venues An ArrayList of containing Venue instances.
     * @param venue_str The name of the venue.
     * @return Returns the venue instance if found, otherwise return null.
     */
    public static Venue getVenue(ArrayList<Venue> venues, String venue_str) {
        for (Venue venue : venues) {
            if (venue.getName().equals(venue_str)) {
                return venue;
            }
        }
        return null;
    }
    /**
     * Adds a reservation to the reservation list in the instane.
     * @param reservation The reservation to be added.
     */
    private void addReservation(Reservation reservation) {
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
    public ArrayList<Room> roomAvailability(LocalDate start, LocalDate end, int small, int medium, int large) {
        ArrayList<Room> availableRooms = new ArrayList<Room>();
        int tmpSmall = small;
        int tmpMedium = medium;
        int tmpLarge = large;
        // Iterate for all rooms in the venue
        for (Room room : rooms) {
            ArrayList<Reservation> reservationsWithRoom = Reservation.searchReservation(reservations, room);
            switch(room.getSize()) {
                case "small":
                    if (tmpSmall > 0) {                    
                        if(reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {

                            availableRooms.add(room);
                            tmpSmall--;
                        }                    
                    }
                    break;

                case "medium":
                    if (medium > 0) {
                        if (reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {
                            
                            availableRooms.add(room);
                            tmpMedium--;
                        }
                    }
                    break;

                case "large":
                    if (medium > 0) {
                        if (reservationsWithRoom.size() == 0 ||
                            !Reservation.hasDateOverlap(reservationsWithRoom, start, end)) {
                                
                            availableRooms.add(room);
                            tmpLarge--;
                        }
                    break;
                    }
            }
        }
        // A request cannot be fulfilled if there are no rooms 
        if (tmpSmall > 0 || tmpMedium > 0 || tmpLarge > 0) {
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
        for (Room room : rooms) {
            reservation.addRoom(room);
        }
        this.addReservation(reservation);
        return reservation;
    }
}