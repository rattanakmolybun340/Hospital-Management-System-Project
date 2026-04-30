package Model;

/**
 * TimeSlot Class
 * 
 * Represents a specific time slot on a given day with availability tracking.
 * Each TimeSlot has:
 * - date: The date of the slot (YYYY-MM-DD format)
 * - start time: When the slot starts (HH:MM format)
 * - end time: When the slot ends (HH:MM format)
 * - availability: Whether the slot is available for booking
 * 
 * Access Modifiers:
 * - Private: date, start, end, isAvailable (data encapsulation)
 * - Public: Constructor, getters, setters (interface for other classes)
 */
public class TimeSlot {
    // Private fields - encapsulated data
    private String date;        // Format: YYYY-MM-DD
    private String start;       // Format: HH:MM
    private String end;         // Format: HH:MM
    private boolean isAvailable; // Tracks if this slot can be booked

    /**
     * Constructor: Creates a TimeSlot with date and time range
     * @param date The date of this slot (YYYY-MM-DD format)
     * @param start The start time (HH:MM format)
     * @param end The end time (HH:MM format)
     */
    public TimeSlot(String date, String start, String end) {
        setDate(date);
        setStart(start);
        setEnd(end);
        this.isAvailable = true; // New slots are available by default
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the date for this time slot
     * Logic: Ensures date is in YYYY-MM-DD format
     * @param date The date (YYYY-MM-DD format)
     */
    public void setDate(String date) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Warning: Invalid date format. Expected YYYY-MM-DD");
            this.date = "0000-00-00";
        } else {
            this.date = date;
        }
    }

    /**
     * Sets the start time for this slot
     * Logic: Basic validation to ensure time isn't empty
     * @param start The start time (HH:MM format)
     */
    public void setStart(String start) {
        if (start == null || start.isEmpty() || !start.matches("\\d{2}:\\d{2}")) {
            System.out.println("Warning: Invalid start time format. Expected HH:MM");
            this.start = "00:00";
        } else {
            this.start = start;
        }
    }

    /**
     * Sets the end time for this slot
     * Logic: Basic validation
     * @param end The end time (HH:MM format)
     */
    public void setEnd(String end) {
        if (end == null || end.isEmpty() || !end.matches("\\d{2}:\\d{2}")) {
            System.out.println("Warning: Invalid end time format. Expected HH:MM");
            this.end = "00:00";
        } else {
            this.end = end;
        }
    }

    /**
     * Sets the availability of this time slot
     * Logic: Prevents double-booking by marking slots as unavailable when booked
     * @param isAvailable Whether this slot is available for booking
     */
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // --- GETTERS (Accessors) ---

    /**
     * Gets the date of this time slot
     * @return The date in YYYY-MM-DD format
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the start time with formatting
     * @return The start time formatted for display
     */
    public String getStart() {
        return start + " hrs";
    }

    /**
     * Gets the start time without formatting (raw value)
     * Useful for time comparisons and validations
     * @return The raw start time (HH:MM format)
     */
    public String getStartRaw() {
        return start;
    }

    /**
     * Gets the end time with formatting
     * @return The end time formatted for display
     */
    public String getEnd() {
        return end + " hrs";
    }

    /**
     * Gets the end time without formatting (raw value)
     * @return The raw end time (HH:MM format)
     */
    public String getEndRaw() {
        return end;
    }

    /**
     * Checks if this time slot is available for booking
     * @return true if available, false if already booked
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Gets a complete description of this time slot
     * @return String representation: "Date - StartTime to EndTime (Status)"
     */
    public String getSlotDetails() {
        String status = isAvailable ? "Available" : "Booked";
        return date + " | " + start + " - " + end + " | " + status;
    }
}