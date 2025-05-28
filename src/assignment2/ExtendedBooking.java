package assignment2;

import java.io.Serializable;

public class ExtendedBooking extends Booking implements Serializable
{
	private static int nextId = 100;

    private int id;
    private double totalCost;
    private double depositPaid;
    private Offer offerUsed;
    
    public ExtendedBooking(Guest guest, int startDay, int endDay,
            double totalCost, double depositPaid, Offer offerUsed) {
		super(guest, startDay, endDay);
		this.id = nextId++;
		this.totalCost = totalCost;
		this.depositPaid = depositPaid;
		this.offerUsed = offerUsed;
}
    
	public int getId() {
        return id;
    }

    public double getRemainingToPay() {
        return totalCost - depositPaid;
    }
    
    public double getDepositPaid() {
		return depositPaid;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public Offer getOfferUsed() {
		return offerUsed;
	}

    public String getSummary() {
    	return "Booking #" + getId() + " for " + getGuest().getGuestName() +
 		       " - " + getRoom().getDescription() + ", Remaining: $" + getRemainingToPay();
 	}
}
