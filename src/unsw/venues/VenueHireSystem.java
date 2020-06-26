
package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {
    private ArrayList<Venue> venues;
    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
        
        venues = new ArrayList<Venue>();
    }
    /**
     * Processes a command in a JSONObject according to the command key word
     * @param json A JSONObject containing the command and relevant keys with the command.
     */
    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {
        // Convert keys to suitable parameters relevant method. No output required.
        // Adds a room. If no venue exists with the name, make a new one.
        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;
        // Convert keys to suitable parameters for relevant method. Output as a JSONObject.
        // Request a reservation.
        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        // Convert keys to suitable paramters for relevant method. Output as JSONObject.
        // Change a request.
        case "change":
            String currentID = json.getString("id");
            LocalDate newStart = LocalDate.parse(json.getString("start"));
            LocalDate newEnd = LocalDate.parse(json.getString("end"));
            int newSmall = json.getInt("small");
            int newMedium = json.getInt("medium");
            int newLarge = json.getInt("large");

            JSONObject newResult = change(currentID, newStart, newEnd, newSmall, newMedium, newLarge);
        
            System.out.println(newResult.toString(2));
            break;
        // Convert keys to suitable paramters for relevant method. Output as JSONArray.
        // List the occupancy of the rooms in a venue.
        case "list":
            String listVenue = json.getString("venue");

            JSONArray listResult = list(listVenue);

            System.out.println(listResult.toString(2));
            break;

        // Convert key to suitable parameter for relevant method. No output required.
        // Cancel a reservation.
        case "cancel":
            String cancelID = json.getString("id");

            cancel(cancelID);
            break;
        }
    }

    /**
     * Adds a room to the specified venue with a room name and size. If no venue with the given
     * name exists, make a new one.
     * @param venue_str Venue name as a String to add the room to
     * @param room Name of the room as a String
     * @param size Size of the room as a String
     */
    private void addRoom(String venue_str, String room, String size) {

        Venue venue = Venue.searchVenue(this.venues, venue_str);
        // If no venue is found, create a new venue and add it to the system
        if (venue == null) {
            venue = new Venue(venue_str);
            venues.add(venue);
        }
        // Add the room the venue
        venue.addRoom(room, size);
    }

    /**
     * Request a reservation with the specified conditions.
     * @param id Unique ID of the reservation
     * @param start Requested start date
     * @param end Requested end date
     * @param small Requested number of small rooms
     * @param medium Requested number of medium rooms
     * @param large Request number of large rooms
     * @return Returns a JSONObject that states whether the request was successful or rejected
     */
    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        // Search through each venue
        for (Venue venue : venues) {
            ArrayList<Room> availableRooms = venue.getAvailableRooms(start, end, small, medium, large);
            // Check if available rooms can fulfil request
            if (availableRooms != null) {
                // Request can be fulfiled.
                Reservation reservation = venue.makeReservation(id, availableRooms, start, end);
                return outputSuccess(reservation);
            }
        }
        // Looked through all venues but cannot fulful request
        return outputRejected();
    }
        
            
    /**
     * Change a reservation with new conditions. Similar to making a request. Successful changes will remove old
     * reservation and request a new one with the given conditions.
     * @param id Unique id of the reservation to change
     * @param start Starting date to change to
     * @param end Ending date to change to
     * @param small New number of small rooms
     * @param medium New number of medium rooms
     * @param large New number of large rooms
     * @return A JSONObject that states whether the change was successful or rejected
     */
    public JSONObject change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();
        /* According to specs, requests and changes are fulfilled as follows:
        *  1. Each venue is checked (in order of definition of input) to determine whether it
        *     can satisfy all requested rooms.
        *  2. If so, the first available rooms (in order of input) are assigned to the reservation.
        */
        // Search for the reservation id.
        Reservation tmpReservation = Venue.searchReservation(venues, id);

        for (Venue venue : venues) {
            
            // See if change can be fulfilled.
            ArrayList<Room> availableRooms = venue.getAvailableRooms(start, end, small, medium, large);
            if (availableRooms != null) {
                
                // Already checked that rooms are available so this should generate
                // a success.
                
                result = request(id, start, end, small, medium, large);
                // Change can be fulfilled so cancel old reservation and
                // request with new details
                tmpReservation.getVenue().cancelReservation(tmpReservation);
                return result;
            }
        }
        // Change cannot be fulfilled so reject it
        return outputRejected();
    }

    /**
     * Lists the occupancy of each room in a given venue.
     * @param venue The venue as a String to generate a list for.
     * @return A JSONArray that contains the list.
     */
    public  JSONArray list(String venue) {
        
        JSONArray list = new JSONArray();
        // Spec states that there will be no invalid inputs
        Venue resultVenue = Venue.searchVenue(venues, venue);
        // Find reservations through each room
        for (Room room : resultVenue.getRooms()) {
            
            JSONObject JSONroom = generateJSONRoom(room);
            JSONArray JSONReservations = generateJSONReservations(Reservation.searchReservation(resultVenue.getReservation(), room));
            JSONroom.put("reservations", JSONReservations);
            list.put(JSONroom);
        }
        return list;
    }

    /**
     * Cancel a reservation by specified id.
     * @param id Unique id of the reservation to cancel
     */
    public void cancel(String id) {
        // Search through each venue for the reservation
        for (Venue venue : venues) {
            Reservation reservation = Reservation.searchReservation(venue.getReservation(), id);
            if (reservation != null) {
                // Reservation found, cancel it.
                venue.cancelReservation(reservation);
            }
        }
        
    }
    /**
     * Creates a JSONObject to display a rejected message for a failed request or change
     * @return A JSONObject containing the rejected message
     */
    private JSONObject outputRejected() {
        JSONObject rejected = new JSONObject();
        rejected.put("status", "rejected");
        return rejected;
    }

    /**
     * Cretes a JSONObject to display a successful for a request or change that can be fulfilled.
     * It contains information on venue where the reservation takes place and the rooms.
     * @param reservation The reservation that was successfully made.
     * @return A JSONObject that contains the relevant information.
     */
    private JSONObject outputSuccess(Reservation reservation) {
        JSONObject success = new JSONObject();
        success.put("venue", reservation.getVenueName());
        JSONArray rooms = new JSONArray();
        for (Room room : reservation.getRooms()) {
            rooms.put(room.getName());
        }
        success.put("rooms", rooms);
        success.put("status", "success");
        return success;
    }

    /**
     * Creates a JSONObject that stores relevant information about a room.
     * @param room A Room instance to create a JSONObject for
     * @return A JSONObject containing information on the room.
     */
    private JSONObject generateJSONRoom(Room room) {
        JSONObject roomJSON = new JSONObject();
        // Add room name
        roomJSON.put("room", room.getName());
        return roomJSON;
    }

    /**
     * Creates a JSONArray that stores relevant information on a list of reservations
     * @param reservations An ArrayList of Reservation instances
     * @return A JSONArray containing information on the reservations.
     */
    private JSONArray generateJSONReservations(ArrayList<Reservation> reservations) {
        
        JSONArray result = new JSONArray();
        // Sort reservations by date
        ArrayList<Reservation> sortedReservations = Reservation.sortByDate(reservations);
        // Add reservation id, start date and end date each as a JSONObject, then add the
        // JSONObjects to the JSONArray
        for (Reservation reservation : sortedReservations) {
            JSONObject JSONReservation = new JSONObject();
            JSONReservation.put("id", reservation.getID());
            JSONReservation.put("start", reservation.getStartString());
            JSONReservation.put("end", reservation.getEndString());
            result.put(JSONReservation);
            
        }
        return result;
    }

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
