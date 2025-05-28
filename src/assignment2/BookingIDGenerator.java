package assignment2;

/**
 * Utility class responsible for generating unique booking IDs.
 * This class maintains a static counter that increments with each ID request.
 */
public class BookingIDGenerator {

    private static int nextId = 100; 

    /**
     * Returns the next unique booking ID and increments the internal counter.
     *
     * @return the next unique booking ID
     */
    public static int getNextId() {
        return nextId++;
    }
}
