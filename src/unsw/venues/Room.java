package unsw.venues;

import java.util.ArrayList;

/**
 * A room belonging to a venue. It has a name and a size.
 */
public class Room {
    private Venue venue;
    private String name;
    private String size;
    
    /**
     * Constructor method to create a room
     * @param venue The venue the room belongs to
     * @param name The name of the room
     * @param size The size of the room
     */

    public Room(Venue venue, String name, String size) {
        this.name = name;
        this.size = size;
        this.venue = venue;
    }
    /**
     * Getter method to retreive the venue class
     * @return Returns a venue of class Venue
     */
    public Venue getVenue() {
        return venue;
    }
    /**
     * Getter method to retrieve the name of the room
     * @return Returns the room name as a String
     */
    public String getName() {
        return name;
    }
    /**
     * Setter method to change the room name
     * @param name The new name of the room
     */
    // Room names can change if it is really needed 
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Getter method to retrieve the room size. Current there are 3 sizes:
     * small, medium and large.
     * @return Returns the room size as a String
     */
    public String getSize() {
        return size;
    }
    /**
     * Setter method to change the room size.
     * @param size The new size of the room.
     */
    // Room size can change (renovations or other reasons)
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Checks an ArrayList of Room instances whether there is at least one of the provided room within that arraylist
     * @param rooms An arraylist of Room instances to check through
     * @param room The room we want to check for
     * @return True if the arraylist contains the given room. False if not.
     */
    public static boolean containsRoom(ArrayList<Room> rooms, Room room) {
        return rooms.contains(room);
    }



}