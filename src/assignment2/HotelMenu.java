package assignment2;

import java.util.*;

/** This is a use-case handler class to provide mechanisms to operate the program, through a console-based menu. */
public class HotelMenu {
    private Hotel hotel;
    private Scanner scanner;

    public HotelMenu() {
        hotel = new Hotel();
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n1. Make General Guest Booking");
            System.out.println("2. Delete booking");
            System.out.println("3. Check room status for a day");
            System.out.println("4. Create a business guest booking");
            System.out.println("0. Exit\n");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    makeGeneralGuestBooking();
                    break;
                case 2:
                    deleteBooking();
                    break;
                case 3:
                	checkRoomStatusForDay();
                	break;
                case 4:
                    makeBusinessGuestBooking();
                    break;
                case 0:
                    System.out.println("Exiting. Thank you so much for using this.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createBooking() {
    	System.out.println("Creating a new booking...");
    	
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();
        Guest guest = new GeneralGuest(guestName, "unknown", "unknown", "unknown");
        System.out.print("Enter start day (1-30): ");
        int startDay = scanner.nextInt();
        System.out.print("Enter end day (1-30): ");
        int endDay = scanner.nextInt();
        scanner.nextLine();

        if (startDay < 1 || endDay > 30 || endDay <= startDay) {
            System.out.println("Invalid days. Please try again.");
            return;
        }

        int option;
        do {
	        System.out.println("Select room type:");
	        System.out.println("1. Basic Single Suite");
	        System.out.println("2. Deluxe Suite");
	        System.out.println("3. Executive Suite");
	        option = scanner.nextInt();
	        scanner.nextLine();

	        if (option < 1 || option > 3) {
	            System.out.println("Invalid room type. Please try again.");
	        }
        } while (option < 1 || option > 3);

        RoomType roomType;
        switch (option) {
        	case 1:
        		roomType = RoomType.BASIC_SINGLE_SUITE;
        		break;
        	case 2:
        		roomType = RoomType.DELUXE_SUITE;
        		break;
        	default:
        		roomType = RoomType.EXECUTIVE_SUITE;
        		break;
        }
        
        ArrayList<Room> availableRooms = hotel.findAvailableRoom(roomType, startDay, endDay);

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for the selected type and dates. Sorry.");
            return;
        }
        Room room = availableRooms.get(0);
        
        System.out.println("Room " + room.getRoomNumber() + " is available. Confirm booking? (y/n)");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            Booking booking = new Booking(guest, startDay, endDay);
            if (room.recordBooking(booking)) {
                System.out.println("Booking successful!");
            } else {
                System.out.println("Booking failed.");
            }
        } else {
            System.out.println("Booking aborted.");
        }
    }

    private void deleteBooking() {
        System.out.print("Enter name of guest whose booking is to be cancelled: ");
        String guest = scanner.nextLine();
        Booking booking = hotel.findBooking(guest);

        if (booking == null) {
            System.out.println("No booking found for that guest.");
            return;
        } else {
        	booking.getRoom().deleteBooking(booking);
        }
    }
    
    private void checkRoomStatusForDay() {
        System.out.print("Enter day (1-30): ");
        int day = scanner.nextInt();
        scanner.nextLine();

        if (day < 1 || day > 30) {
            System.out.println("Invalid day. Please try again.");
            return;
        }

        System.out.println("Room status for day " + day + ":");
        for (Room room : hotel.getAllRooms()) {
            Booking booking = room.getBookingForDay(day);
            if (booking == null) {
                System.out.println("Room " + room.getRoomNumber() + " ["+ room.getDescription() + "] is available.");
            } else {
            	String guestName = booking.getGuest().getGuestName();
            	String roomDesc = room.getDescription();
            	int roomNumber = room.getRoomNumber();
            	int start = booking.getStartDay();
            	int end = booking.getEndDay();
            	int id = (booking instanceof ExtendedBooking) ? ((ExtendedBooking) booking).getId() : -1;

            	System.out.printf("Room %d [%s] is booked by %s from day %d to day %d (Booking ID: %s)%n",
            	    roomNumber, roomDesc, guestName, start, end, (id != -1 ? id : "N/A"));
            }
        }
    }

    private void makeGeneralGuestBooking() {
        System.out.print("Enter guest name: ");
        String name = scanner.nextLine();
        System.out.print("Enter guest address: ");
        String address = scanner.nextLine();
        System.out.print("Enter guest phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter guest ID (passport or license): ");
        String id = scanner.nextLine();

        Guest guest = new GeneralGuest(name, address, phone, id);

        System.out.print("Enter start day (1-30): ");
        int startDay = scanner.nextInt();
        System.out.print("Enter number of nights: ");
        int nights = scanner.nextInt();
        scanner.nextLine(); // flush

        System.out.print("Enter room type (BASIC, DELUXE, EXECUTIVE): ");
        String typeInput = scanner.nextLine().toUpperCase();
        RoomType type;
        switch (typeInput) {
            case "BASIC": type = RoomType.BASIC_SINGLE_SUITE; break;
            case "DELUXE": type = RoomType.DELUXE_SUITE; break;
            case "EXECUTIVE": type = RoomType.EXECUTIVE_SUITE; break;
            default:
                System.out.println("Invalid room type.");
                return;
        }
        
        System.out.print("Include breakfast? (yes/no): ");
        boolean breakfast = scanner.nextLine().equalsIgnoreCase("yes");

        Offer offer = null;
        int[] stayDays = new int[nights];
        for (int i = 0; i < nights; i++) {
            stayDays[i] = (startDay + i - 1) % 7; 
        }
        if ((type == RoomType.BASIC_SINGLE_SUITE || type == RoomType.DELUXE_SUITE) && nights == 3) {
            System.out.print("Apply Midweek Offer (20% off)? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                offer = new MidweekOffer();
            }
        } else if (type == RoomType.DELUXE_SUITE && isWeekendOnly(startDay, nights)) {
            System.out.print("Apply Weekend Getaway Offer ($2 breakfasts)? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                offer = new WeekendGetawayOffer();
            }
        }

        double cost = hotel.calculateCost(type, startDay, nights, breakfast);
        if (offer != null) {
            cost = offer.applyDiscount(cost, type, stayDays);
        }

        System.out.printf("Total cost: $%.2f%n", cost);
        System.out.print("Enter deposit amount (minimum 50%): ");
        double deposit = scanner.nextDouble();
        scanner.nextLine();

        if (deposit < cost * 0.5 || deposit > cost) {
            System.out.println("Invalid deposit amount. Must be at least 50% and no more than total.");
            return;
        }

        ExtendedBooking booked = hotel.addGeneralGuestBooking((GeneralGuest) guest, startDay, nights, type, cost, deposit, offer, breakfast);
        if (booked != null) {
            System.out.println("Booking confirmed! ID: " + booked.getId());
        } else {
            System.out.println("No available room of selected type.");
        }
    }

    private boolean isWeekendOnly(int start, int nights) {
        for (int i = 0; i < nights; i++) {
            int dayOfWeek = (start + i) % 7; // assume 0=Sunday
            if (dayOfWeek != 5 && dayOfWeek != 6) return false;
        }
        return true;
    }
    
    private void makeBusinessGuestBooking() {
        System.out.print("Enter business name: ");
        String businessName = scanner.nextLine();
        System.out.print("Enter business contact person: ");
        String contactPerson = scanner.nextLine();
        Business business = hotel.getOrCreateBusiness(businessName, contactPerson);

        System.out.print("Enter guest name: ");
        String name = scanner.nextLine();
        System.out.print("Enter guest phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter member number: ");
        int memberNumber = scanner.nextInt();
        scanner.nextLine();

        Guest guest = new BusinessGuest(name, phone, memberNumber, business);

        System.out.print("Enter start day (1-30): ");
        int startDay = scanner.nextInt();
        System.out.print("Enter number of nights: ");
        int nights = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter room type (BASIC, DELUXE, EXECUTIVE): ");
        String typeInput = scanner.nextLine().toUpperCase();
        RoomType type;
        switch (typeInput) {
            case "BASIC": type = RoomType.BASIC_SINGLE_SUITE; break;
            case "DELUXE": type = RoomType.DELUXE_SUITE; break;
            case "EXECUTIVE": type = RoomType.EXECUTIVE_SUITE; break;
            default:
                System.out.println("Invalid room type.");
                return;
        }

        System.out.print("Include breakfast? (yes/no): ");
        boolean breakfast = scanner.nextLine().equalsIgnoreCase("yes");

        Offer offer = null;
        if ((type == RoomType.BASIC_SINGLE_SUITE || type == RoomType.DELUXE_SUITE) && nights == 3) {
            System.out.print("Apply Midweek Offer (20% off)? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                offer = new MidweekOffer();
            }
        } else if (type == RoomType.DELUXE_SUITE && isWeekendOnly(startDay, nights)) {
            System.out.print("Apply Weekend Getaway Offer ($2 breakfasts)? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                offer = new WeekendGetawayOffer();
            }
        }

        double cost = hotel.calculateCost(type, startDay, nights, breakfast);
        if (offer != null) {
            int[] stayDays = new int[nights];
            for (int i = 0; i < nights; i++) stayDays[i] = (startDay + i) % 7;
            cost = offer.applyDiscount(cost, type, stayDays);
        }

        System.out.printf("Total cost: $%.2f%n", cost);
        System.out.print("Enter deposit amount (minimum 50%): ");
        double deposit = scanner.nextDouble();
        scanner.nextLine();

        if (deposit < cost * 0.5 || deposit > cost) {
            System.out.println("Invalid deposit amount. Must be at least 50% and no more than total.");
            return;
        }

        ExtendedBooking booked = hotel.addBusinessGuestBooking((BusinessGuest) guest, startDay, nights, type, cost, deposit, offer, breakfast);
        if (booked != null) {
            System.out.println("Business guest booking confirmed! ID: " + booked.getId());
        } else {
            System.out.println("No available room of selected type.");
        }
    }

    // Program Commencement point
    public static void main(String[] args) {
        HotelMenu menu = new HotelMenu();
        menu.displayMenu();
    }
}
