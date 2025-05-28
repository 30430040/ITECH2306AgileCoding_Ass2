package assignment2;

import java.util.*;
import java.io.*;

public class Hotel implements Serializable
{
    public static final int TOTAL_ROOMS = 21;
    private Room[] allRooms;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private HashMap<String, Business> businesses = new HashMap<>();

    /** Creates the new hotel. It makes several rooms of each type. */
    public Hotel() {
    	int i = 0;
    	int offset = 1;
        allRooms = new Room[TOTAL_ROOMS];
        // Initialize rooms with some example data
        while (offset < (TOTAL_ROOMS/2)+1) {
            allRooms[i] = new Room(100 + offset, RoomType.BASIC_SINGLE_SUITE);
            i++;
            offset++;
        }
        
        offset = 1;
        while (offset < (TOTAL_ROOMS/3)+1) {
            allRooms[i] = new Room(200 + offset, RoomType.DELUXE_SUITE);
            i++;
            offset++;
        }
        
        offset = 1;
        while (i < TOTAL_ROOMS) {
        	allRooms[i] = new Room(300 + offset, RoomType.EXECUTIVE_SUITE);
            i++;
            offset++;
        }

    }

    /** Returns a list of all the rooms of a specific type. */
    public ArrayList<Room> getRoomsOfType(RoomType roomType) {
        ArrayList<Room> rooms = new ArrayList<>();
        for (Room room : allRooms) {
            if (room.getRoomType() == roomType) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    /** Returns a safe copy of all Rooms of this Hotel. */
    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> rooms = new ArrayList<Room>();
        for (Room room : allRooms) {
            rooms.add(room);
        }
        return rooms;
    }

    /** Returns a list of potential rooms that meet the criteria. */
    public ArrayList<Room> findAvailableRoom(RoomType roomType, int startDay, int endDay) {
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room room : getRoomsOfType(roomType)) {
            boolean available = true;
            for (int day = startDay; day <= endDay; day++) {
                if (!room.isAvailable(day)) {
                    available = false;
                    break;
                }
            }
            if (available) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    /** Finds the first booking for the guest whose name is supplied */
    public Booking findBooking(String guest) {
        for (int day = 1; day <= Room.MAX_DAYS; day++) {
            for (Room room : allRooms) {
                Booking booking = room.getBookingForDay(day);
                if (booking != null && booking.getGuest().equals(guest)) {
                    return booking;
                }
            }
        }
        return null;
    }

    /** Useful method to determine if a given day's night leads into a weekend day's morning (or not). 
     * @param day The day number to be checked.
     * @return true if the specified day's night leads into a weekend day's morning 
     */
    public boolean isWeekend(int day)
    {
    	switch (day) {
    		case 5:			// The 5th of the month, is a Friday, which leads into Saturday morning
    		case 6:			// The 6th of the month, is a Saturday, which leads into Sunday morning
    		case 12:
    		case 13:
    		case 19:
    		case 20:
    		case 26:
    		case 27:
    			return true;
    		default:
    			return false;
    	}
    }
    
    private boolean isAvailable(Room room, int startDay, int nights) {
        int endDay = startDay + nights - 1;
        for (int day = startDay; day <= endDay; day++) {
            if (!room.isAvailable(day)) {
                return false;
            }
        }
        return true;
    }
    
    public ExtendedBooking addGeneralGuestBooking(GeneralGuest guest, int startDate, int nights, RoomType type, double cost, double deposit, Offer offer, boolean breakfast) {
        int endDate = startDate + nights - 1;

        for (Room room : allRooms) {
            if (room.getRoomType() == type && isAvailable(room, startDate, nights)) {
                ExtendedBooking booking = new ExtendedBooking(guest, startDate, endDate, cost, deposit, offer);
                booking.setRoom(room);
                room.recordBooking(booking);
                bookings.add(booking);
                return booking;
            }
        }
        return null;
    }


   public double calculateCost(RoomType type, int startDay, int nights, boolean includeBreakfast) {
	    double total = 0;
	    for (int i = 0; i < nights; i++) {
	        int dayOfWeek = (startDay + i) % 7; // 0 = Sunday
	        switch (type) {
	            case BASIC_SINGLE_SUITE:
	                total += (dayOfWeek == 5 || dayOfWeek == 6) ? 125 : 105;
	                break;
	            case DELUXE_SUITE:
	                total += (dayOfWeek == 5 || dayOfWeek == 6) ? 170 : 141;
	                break;
	            case EXECUTIVE_SUITE:
	                total += 220;
	                break;
	        }
	        if (includeBreakfast) {
	            total += 25;
	        }
	    }
	    return total;
	}

   public Business getOrCreateBusiness(String name, String contactPerson) {
	    if (!businesses.containsKey(name)) {
	        businesses.put(name, new Business(name, contactPerson));
	    }
	    return businesses.get(name);
	}

   
   public ExtendedBooking addBusinessGuestBooking(BusinessGuest guest, int startDate, int nights, RoomType type, double cost, double deposit, Offer offer, boolean breakfast) {
	    int endDate = startDate + nights - 1;

	    for (Room room : allRooms) {
	        if (room.getRoomType() == type && isAvailable(room, startDate, nights)) {
	            ExtendedBooking booking = new ExtendedBooking(guest, startDate, endDate, cost, deposit, offer);
	            booking.setRoom(room);
	            room.recordBooking(booking);
	            bookings.add(booking);
	            guest.getBusinessName().addEmployee(guest);
	            return booking;
	        }
	    }
	    return null;
	}

   public void printBusinessBookingSummary() {
	    System.out.println("==== Business Booking Summary ====");

	    for (Business business : businesses.values()) {
	        System.out.println("Business: " + business.getName());
	        System.out.println("Contact: " + business.getContactPerson());

	        ArrayList<BusinessGuest> employees = business.getEmployees();
	        boolean hasBookings = false;

	        for (BusinessGuest guest : employees) {
	            for (Booking booking : bookings) {
	                if (booking instanceof ExtendedBooking &&
	                    ((ExtendedBooking) booking).getGuest() instanceof BusinessGuest &&
	                    ((ExtendedBooking) booking).getGuest().equals(guest)) {

	                    ExtendedBooking ext = (ExtendedBooking) booking;
	                    Room room = ext.getRoom();

	                    System.out.printf("  - Booking #%d: %s, Room %d [%s], Days %d–%d, Remaining: $%.2f%n",
	                    	    ext.getId(),
	                    	    guest.getGuestName(),
	                    	    room.getRoomNumber(),
	                    	    room.getRoomType(),
	                    	    ext.getStartDay(),
	                    	    ext.getEndDay(),
	                    	    ext.getRemainingToPay()
	                    	);
	                    hasBookings = true;
	                }
	            }
	        }

	        if (!hasBookings) {
	            System.out.println("  No bookings yet.");
	        }

	        System.out.println();
	    }
	}
   
   public void printBookingsForGuest(String name) {
	    System.out.println("=== Bookings for Guest: " + name + " ===");
	    boolean found = false;

	    for (Booking booking : bookings) {
	        String guestName = booking.getGuest().getGuestName();
	        if (guestName.equalsIgnoreCase(name)) {
	            int id = (booking instanceof ExtendedBooking) ? ((ExtendedBooking) booking).getId() : -1;
	            Room room = booking.getRoom();
	            System.out.printf("- Booking ID: %s, Room %d [%s], Days %d to %d%n",
	                (id != -1 ? id : "N/A"),
	                room.getRoomNumber(),
	                room.getRoomType(),
	                booking.getStartDay(),
	                booking.getEndDay()
	            );
	            found = true;
	        }
	    }

	    if (!found) {
	        System.out.println("No bookings found for guest: " + name);
	    }
	}
   
   public boolean cancelBookingById(int bookingId, int currentDay) {
	    Booking toCancel = null;

	    for (Booking booking : bookings) {
	        if (booking instanceof ExtendedBooking) {
	            ExtendedBooking ext = (ExtendedBooking) booking;
	            if (ext.getId() == bookingId) {
	                toCancel = ext;
	                break;
	            }
	        }
	    }

	    if (toCancel == null) {
	        System.out.println("Booking ID not found.");
	        return false;
	    }

	    ExtendedBooking booking = (ExtendedBooking) toCancel;

	    // Refund logic
	    double refund = 0.0;
	    if (currentDay < booking.getStartDay()) {
	        if (booking.getStartDay() - currentDay > 1) {
	            refund = booking.getDepositPaid() - 20;
	            System.out.printf("Booking cancelled. Refund due: $%.2f (Cancellation fee: $20)%n", refund);
	        } else {
	            System.out.println("Booking cancelled. No refund (less than 1 day before start).");
	        }
	    } else {
	        System.out.println("Booking cancelled on/after start day. No refund.");
	    }

	    // Remove booking from room and hotel list
	    booking.getRoom().deleteBooking(booking);
	    bookings.remove(booking);
	    return true;
	}

   public void printDetailedBusinessSummary() {
	    System.out.println("====== Detailed Business Booking Summary ======\n");

	    for (Business business : businesses.values()) {
	        System.out.println("Business: " + business.getName());
	        System.out.println("Contact: " + business.getContactPerson());

	        ArrayList<BusinessGuest> employees = business.getEmployees();
	        boolean hasBookings = false;

	        for (BusinessGuest guest : employees) {
	            for (Booking booking : bookings) {
	                if (booking instanceof ExtendedBooking &&
	                    ((ExtendedBooking) booking).getGuest().equals(guest)) {

	                    ExtendedBooking ext = (ExtendedBooking) booking;
	                    Room room = ext.getRoom();

	                    System.out.printf("  - Employee: %s (Member #%d)\n", guest.getGuestName(), guest.getMemberNumber());
	                    System.out.printf("    Room: %d [%s], Stay: Days %d–%d, Booking ID: %d\n",
	                            room.getRoomNumber(), room.getRoomType(),
	                            ext.getStartDay(), ext.getEndDay(), ext.getId());
	                    System.out.printf("    Paid: $%.2f, Outstanding: $%.2f\n\n",
	                            ext.getDepositPaid(), ext.getRemainingToPay());

	                    hasBookings = true;
	                }
	            }
	        }

	        if (!hasBookings) {
	            System.out.println("  No bookings yet.");
	        }

	        System.out.println("--------------------------------------------------\n");
	    }
	}

   public void saveToFile(String filename) {
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
	        out.writeObject(this);
	        System.out.println("Hotel data saved to " + filename);
	    } catch (IOException e) {
	        System.out.println("Error saving hotel data: " + e.getMessage());
	    }
	}

	public static Hotel loadFromFile(String filename) {
	    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
	        Hotel hotel = (Hotel) in.readObject();
	        System.out.println("Hotel data loaded from " + filename);
	        return hotel;
	    } catch (IOException | ClassNotFoundException e) {
	        System.out.println("Error loading hotel data: " + e.getMessage());
	        return new Hotel(); 
	    }
	}
	
	public void processAgencyBookings(String filename) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        int nights = Integer.parseInt(reader.readLine().trim()); // shared value

	        String guestName;
	        int success = 0, failed = 0;

	        while ((guestName = reader.readLine()) != null) {
	            try {
	                int startDay = Integer.parseInt(reader.readLine().trim());
	                int endDay = Integer.parseInt(reader.readLine().trim()); // can ignore if start + nights - 1
	                String roomTypeStr = reader.readLine().trim().toUpperCase();
	                double deposit = Double.parseDouble(reader.readLine().trim());

	                RoomType roomType = switch (roomTypeStr) {
	                    case "BASIC" -> RoomType.BASIC_SINGLE_SUITE;
	                    case "DELUXE" -> RoomType.DELUXE_SUITE;
	                    case "EXECUTIVE" -> RoomType.EXECUTIVE_SUITE;
	                    default -> throw new IllegalArgumentException("Invalid room type: " + roomTypeStr);
	                };

	                boolean breakfast = false; // default — not provided in file

	                // Create dummy guest
	                Guest guest = new GeneralGuest(guestName, "N/A", "N/A", "N/A");

	                // Determine applicable offer
	                Offer offer = null;
	                if ((roomType == RoomType.BASIC_SINGLE_SUITE || roomType == RoomType.DELUXE_SUITE) && nights == 3) {
	                    offer = new MidweekOffer();
	                } else if (roomType == RoomType.DELUXE_SUITE && (nights == 2 || nights == 3)) {
	                    boolean allWeekend = true;
	                    for (int i = 0; i < nights; i++) {
	                        int dayOfWeek = (startDay + i - 1) % 7;
	                        if (dayOfWeek != 5 && dayOfWeek != 6) {
	                            allWeekend = false;
	                            break;
	                        }
	                    }
	                    if (allWeekend) offer = new WeekendGetawayOffer();
	                }

	                double cost = calculateCost(roomType, startDay, nights, breakfast);
	                if (offer != null) {
	                    int[] stayDays = new int[nights];
	                    for (int i = 0; i < nights; i++) stayDays[i] = (startDay + i - 1) % 7;
	                    cost = offer.applyDiscount(cost, roomType, stayDays);
	                }

	                if (deposit < 0.5 * cost || deposit > cost) {
	                    throw new IllegalArgumentException("Invalid deposit amount for " + guestName);
	                }

	                ExtendedBooking booking = addGeneralGuestBooking((GeneralGuest) guest, startDay, nights, roomType, cost, deposit, offer, breakfast);

	                if (booking != null) {
	                    System.out.printf("✔ Booking confirmed for %s (Booking ID: %d)%n", guestName, booking.getId());
	                    success++;
	                } else {
	                    System.out.printf("✖ No available room for %s%n", guestName);
	                    failed++;
	                }

	            } catch (Exception e) {
	                System.out.println("✖ Error processing guest: " + guestName + " → " + e.getMessage());
	                failed++;
	            }
	        }

	        System.out.printf("%nBatch Import Summary: %d successful, %d failed%n", success, failed);

	    } catch (IOException e) {
	        System.out.println("File read error: " + e.getMessage());
	    }
	}


}
