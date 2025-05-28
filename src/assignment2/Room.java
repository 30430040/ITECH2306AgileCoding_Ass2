package assignment2;

import java.io.Serializable;

/** Represents a room in the hotel. */
public class Room implements Serializable
{
    public static final int MAX_DAYS = 30;
    private int roomNumber;
    private RoomType roomType;
    private Booking[] bookings;

    /** Creates a new room for the Hotel, to be of a specific type.*/
    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.bookings = new Booking[MAX_DAYS];
    }

    /** Get the unique room-number of this Room. */
    public int getRoomNumber() {
        return roomNumber;
    }

    /** Determine the type of room this is. */
    public RoomType getRoomType() {
        return roomType;
    }
    
    /** Returns a string description of the type of room. */
    public String getDescription()
    {
    	switch (roomType)
    	{
    		case BASIC_SINGLE_SUITE:
    			return "Basic Single Suite";
			case DELUXE_SUITE:
				return "Deluxe Suite";
			case EXECUTIVE_SUITE:
				return "Executive Suite";
			default:
				return "Unknown Room Type";
    	}
    }

    /** Report whether the room is available for the specified day. */
    public boolean isAvailable(int day) {
        return bookings[day - 1] == null;
    }

    /** See what booking is recorded (if any) for the specified day. */
    public Booking getBookingForDay(int day) {
        return bookings[day - 1];
    }

    /** Records a booking in the allocated room. */
    public boolean recordBooking(Booking booking) {
    	// Confirm that the room is available for each day of the booking (if not, cancel).
        for (int day = booking.getStartDay(); day <= booking.getEndDay(); day++) {
            if (!isAvailable(day)) {
                return false;
            }
        }
        // Now, reserve each day of the booking, for the new booking by setting it in our array.
        for (int day = booking.getStartDay(); day <= booking.getEndDay(); day++) {
            bookings[day - 1] = booking;
        }
        
     // establish the other direction of a bi-directional link from the booking back to us.
        booking.setRoom(this);    
        return true;
    }

    /** Deletes the booking that is associated with a specified day, but also removes for all other days that the same booking encompassed. */
    /*public boolean deleteBooking(int day) {
        Booking booking = getBookingForDay(day);
        if (booking == null) {
            return false;
        }
        return deleteBooking(booking);		// Jump to next version, to actually delete...
    }

    /** Deletes a booking from this room (all days which it covered, are now made available). */
    /*public boolean deleteBooking(Booking booking) {
        if (booking == null) {
            return false;
        }
        for (int d = booking.getStartDay(); d <= booking.getEndDay(); d++) {
            bookings[d - 1] = null;
        }
        booking.setRoom(null);    // disestablish the bi-directional link
        return true;
    }*/
    
    public void deleteBooking(Booking booking) {
        for (int day = booking.getStartDay(); day <= booking.getEndDay(); day++) {
            if (bookings[day - 1] == booking) {
                bookings[day - 1] = null;
            }
        }
    }


}
