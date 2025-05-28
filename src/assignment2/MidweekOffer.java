package assignment2;

public class MidweekOffer implements Offer
{
	@Override
    public double applyDiscount(double originalCost, RoomType type, int[] days) {
        if ((type == RoomType.BASIC_SINGLE_SUITE || type == RoomType.DELUXE_SUITE) && days.length == 3) {
            for (int d : days) {
                if (d == 5 || d == 6) return originalCost; // Friday/Saturday night
            }
            return originalCost * 0.8; // 20% off
        }
        return originalCost;
    }

}
