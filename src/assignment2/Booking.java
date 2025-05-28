package assignment2;

import java.io.Serializable;

/**
 * Represents a hotel booking made by a guest.
 * A booking includes information about the guest, the start and end day of the stay,
 * and the room assigned to the booking.
 */
public class Booking implements Serializable
{
	private Guest guest;		// Who is the guest that this booking is for?
	private int startDay;		// The day of the first night that is included in booking
	private int endDay;			// The day of the last night that is included in booking (can be same as startDay)
	private Room room;			// One part of a bi-directional reference. The room will also have a reference back to this (when booked).

	/**
     * Constructs a new Booking for a specific guest over a date range.
     *
     * @param guest    the guest making the booking
     * @param startDay the day of the first night included in the booking
     * @param endDay   the day of the last night included in the booking
     */
    public Booking(Guest guest, int startDay, int endDay) {
        this.guest = guest;
        this.startDay = startDay;
        this.endDay = endDay;
        this.room = null;
    }

    /**
     * Associates a room with this booking.
     *
     * @param bookedRoom the room to associate with this booking
     */
    public void setRoom(Room bookedRoom) {
        this.room = bookedRoom;
    }

    /**
     * Returns the room associated with this booking.
     *
     * @return the booked room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the guest who made this booking.
     *
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Returns the starting day of the booking.
     *
     * @return the start day
     */
    public int getStartDay() {
        return startDay;
    }

    /**
     * Returns the ending day of the booking.
     *
     * @return the end day
     */
    public int getEndDay() {
        return endDay;
    }
}