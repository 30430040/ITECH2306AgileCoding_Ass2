package assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class HotelBookingTests {

    private Hotel hotelObj;

    @BeforeEach
    public void setup() {
        hotelObj = new Hotel();
    }

    @Test
    public void testCreateGeneralGuestBooking() {
        GeneralGuest guest = new GeneralGuest("Test User", "123 St", "0412345678", "ID123");
        ExtendedBooking booking = hotelObj.addGeneralGuestBooking(guest, 5, 3, RoomType.BASIC_SINGLE_SUITE, 300, 150, null, false);

        assertNotNull(booking);
        assertEquals("Test User", booking.getGuest().getGuestName());
        assertEquals(3, booking.getEndDay() - booking.getStartDay() + 1);
        assertTrue(booking.getId() >= 100);
    }

    @Test
    public void testBookingIdIncrements() {
        GeneralGuest g1 = new GeneralGuest("G1", "A", "P", "ID1");
        GeneralGuest g2 = new GeneralGuest("G2", "A", "P", "ID2");

        ExtendedBooking b1 = hotelObj.addGeneralGuestBooking(g1, 6, 2, RoomType.BASIC_SINGLE_SUITE, 200, 100, null, false);
        ExtendedBooking b2 = hotelObj.addGeneralGuestBooking(g2, 9, 2, RoomType.BASIC_SINGLE_SUITE, 200, 100, null, false);

        assertNotEquals(b1.getId(), b2.getId());
        assertTrue(b2.getId() > b1.getId());
    }

    @Test
    public void testRoomIsBookedAfterBooking() {
        GeneralGuest guest = new GeneralGuest("Room Tester", "x", "y", "z");
        ExtendedBooking booking = hotelObj.addGeneralGuestBooking(guest, 10, 2, RoomType.BASIC_SINGLE_SUITE, 200, 100, null, false);

        assertNotNull(booking);
        Room room = booking.getRoom();
        assertFalse(room.isAvailable(10));
        assertFalse(room.isAvailable(11));
        assertTrue(room.isAvailable(12)); // after booking
    }

    @Test
    public void testCancelBookingBeforeStartWithRefund() {
        GeneralGuest guest = new GeneralGuest("Refund Guest", "x", "y", "z");
        ExtendedBooking booking = hotelObj.addGeneralGuestBooking(guest, 15, 3, RoomType.BASIC_SINGLE_SUITE, 300, 200, null, false);

        assertTrue(hotelObj.cancelBookingById(booking.getId(), 10)); // 5 days early

        Room room = booking.getRoom();
        assertTrue(room.isAvailable(15)); // should now be freed
    }

    @Test
    public void testRoomAvailabilityCheck() {
        GeneralGuest guest = new GeneralGuest("Availability Guest", "x", "y", "z");
        ExtendedBooking booking = hotelObj.addGeneralGuestBooking(guest, 25, 2, RoomType.BASIC_SINGLE_SUITE, 200, 100, null, false);

        List<Room> availableRooms = hotelObj.findAvailableRoom(RoomType.BASIC_SINGLE_SUITE, 25, 26);
        // Booking took one room, so total available should be less than before
        assertNotNull(availableRooms);
        assertTrue(availableRooms.size() < Hotel.TOTAL_ROOMS / 2);
    }
}
