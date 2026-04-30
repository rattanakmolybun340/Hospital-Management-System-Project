package Model;

/**
 * AppointmentStatus Enum
 * 
 * Represents the possible states of an appointment throughout its lifecycle.
 * This ensures consistent status tracking and prevents invalid state transitions.
 */
public enum AppointmentStatus {
    /**
     * BOOKED: Appointment has been successfully scheduled and confirmed.
     * Patient and Doctor have committed to this appointment time.
     */
    BOOKED("Booked"),
    
    /**
     * CANCELLED: Appointment has been cancelled by patient or doctor.
     * The time slot becomes available again for other patients.
     */
    CANCELLED("Cancelled"),
    
    /**
     * COMPLETED: Appointment has been completed.
     * Patient has seen the doctor and treatment/consultation is finished.
     */
    COMPLETED("Completed"),
    
    /**
     * RESCHEDULED: Appointment was cancelled and rescheduled to a different time.
     * This represents the transition state during rescheduling.
     */
    RESCHEDULED("Rescheduled");
    
    // Display value for status
    private final String displayValue;
    
    /**
     * Constructor for AppointmentStatus
     * @param displayValue Human-readable status name
     */
    AppointmentStatus(String displayValue) {
        this.displayValue = displayValue;
    }
    
    /**
     * Gets the display value of the status
     * @return Human-readable status string
     */
    public String getDisplayValue() {
        return displayValue;
    }
}
