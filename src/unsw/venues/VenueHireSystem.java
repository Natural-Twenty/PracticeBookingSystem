/**
 * Questions: tmp variables in venue?
 * How are venues going to be created?
 * How are changes handled? Cancel -> Request or request only.
 * 
 */
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
        // TODO Auto-generated constructor stub
        venues = new ArrayList<Venue>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

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

        // TODO Implement other commands
        case "change":
            String newID = json.getString("id");
            LocalDate newStart = LocalDate.parse(json.getString("start"));
            LocalDate newEnd = LocalDate.parse(json.getString("end"));
            int newSmall = json.getInt("small");
            int newMedium = json.getInt("medium");
            int newLarge = json.getInt("large");

            JSONObject newResult = change(newID, newStart, newEnd, newSmall, newMedium, newLarge);
        
            System.out.println(newResult.toString(2));
            break;

        case "list":
            String listVenue = json.getString("venue");

            JSONArray listResult = list(listVenue);

            System.out.println(listResult.toString(2));
            break;

        case "cancel":
            String cancelID = json.getString("id");

            cancel(cancelID);
            break;
        }
    }

    private void addRoom(String venue_str, String room, String size) {
        Venue venue = Venue.getVenue(this.venues, venue_str);
        venue.addRoom(room, size);
    }

    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {

        // TODO Process the request commmand
        for (Venue venue : venues) {
            ArrayList<Room> availableRooms = venue.roomAvailability(start, end, small, medium, large);
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

    public JSONObject change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();

        // TODO Process the change command
        
        return result;
    }

    public  JSONArray list(String venue) {
        // TODO Process the list command
        JSONArray list = new JSONArray();

        return list;
    }

    public void cancel(String id) {
        // TODO Process the cancel command
    }

    private JSONObject outputRejected() {
        JSONObject rejected = new JSONObject();
        rejected.put("status", "rejected");
        return rejected;
    }

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
