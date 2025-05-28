package assignment2;

public class WeekendGetawayOffer implements Offer 
{
	@Override
	public double applyDiscount(double originalCost, RoomType type, int[] days) {
        boolean allWeekend = true;
        for (int d : days) {
            if (d != 5 && d != 6) {
                allWeekend = false;
                break;
            }
        }
        if (type == RoomType.DELUXE_SUITE && allWeekend) {
            return originalCost - (23 * days.length); // $25→$2 breakfast discount
        }
        return originalCost;
    }
}
