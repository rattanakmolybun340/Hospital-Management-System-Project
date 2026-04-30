package Model;

/**
 * Appointment Class
 * 
 * Represents a medical appointment in the hospital system.
 * Establishes relationships between Patient, Doctor, and TimeSlot.
 * 
 * Key Relationships:
 * - Appointment → Patient (who is being treated)
 * - Appointment → Doctor (who provides treatment)
 * - Appointment → TimeSlot (when the appointment occurs)
 * - Appointment → AppointmentStatus (current state of appointment)
 * 
 * Access Modifiers:
 * - Private: id, patient, doctor, timeSlot, status, fee (encapsulated data)
 * - Public: Constructor, getters, setters, validation methods (interface)
 */
public class Appointment {
    // Private fields - encapsulated data
    private String id;                          // Unique appointment reference
    private Patient patient;                    // Patient for this appointment
    private Doctor doctor;                      // Doctor assigned to this appointment
    private TimeSlot timeSlot;                  // When the appointment is scheduled
    private AppointmentStatus status;           // Current state of the appointment
    private double fee;                         // Cost of the appointment

    /**
     * Constructor: Creates an Appointment linking Patient, Doctor, and TimeSlot
     * @param id Unique appointment identifier
     * @param patient The patient for this appointment
     * @param doctor The doctor for this appointment
     * @param timeSlot The time slot for this appointment
     */
    public Appointment(String id, Patient patient, Doctor doctor, TimeSlot timeSlot) {
        setId(id);
        setPatient(patient);
        setDoctor(doctor);
        setTimeSlot(timeSlot);
        this.status = AppointmentStatus.BOOKED; // New appointments start as BOOKED
        // Fee is calculated based on the doctor's hourly rate at time of booking
        this.fee = (doctor != null) ? doctor.getHourlyRate() : 0.0;
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the appointment ID
     * @param id Unique identifier (typically set once at creation)
     */
    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            this.id = "GEN-" + System.currentTimeMillis(); // Auto-generate if invalid
        } else {
            this.id = id;
        }
    }

    /**
     * Sets the patient for this appointment
     * Logic: Ensures a valid patient is assigned
     * @param patient The patient object
     */
    public void setPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Error: Appointment must have a valid patient.");
        } else {
            this.patient = patient;
        }
    }

    /**
     * Sets the doctor for this appointment
     * Logic: Ensures a valid doctor is assigned
     * @param doctor The doctor object
     */
    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Error: Appointment must have a valid doctor.");
        } else {
            this.doctor = doctor;
        }
    }

    /**
     * Sets the TimeSlot for this appointment
     * Logic: Ensures a valid TimeSlot is assigned
     * Note: Availability is managed by HospitalSystem during booking process
     * @param timeSlot The TimeSlot object containing date and time
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            System.out.println("Error: Appointment must have a valid time slot.");
        } else {
            this.timeSlot = timeSlot;
            // Do NOT set availability here - let HospitalSystem manage it
        }
    }

    /**
     * Updates the appointment status
     * Logic: Allows transitioning between valid states
     * @param status The new AppointmentStatus
     */
    public void setStatus(AppointmentStatus status) {
        if (status == null) {
            System.out.println("Error: Status cannot be null.");
        } else {
            this.status = status;
            // If cancelling, make the TimeSlot available again
            if (status == AppointmentStatus.CANCELLED && timeSlot != null) {
                timeSlot.setAvailable(true);
            }
        }
    }

    // --- GETTERS (Accessors) ---

    /**
     * Gets the appointment ID in a formatted way
     * @return Formatted ID like "REF-12345"
     */
    public String getId() {
        return "REF-" + id.toUpperCase();
    }

    /**
     * Gets the raw appointment ID (without formatting)
     * @return The unformatted ID
     */
    public String getRawId() {
        return id;
    }

    /**
     * Gets the patient associated with this appointment
     * @return The Patient object
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Gets the doctor associated with this appointment
     * @return The Doctor object
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the TimeSlot for this appointment
     * @return The TimeSlot object
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Gets the date of this appointment
     * @return Date in YYYY-MM-DD format
     */
    public String getDate() {
        return (timeSlot != null) ? timeSlot.getDate() : "Not Set";
    }

    /**
     * Gets the time of this appointment
     * @return Time with formatting (e.g., "09:00 hrs (Scheduled)")
     */
    public String getTime() {
        return (timeSlot != null) ? timeSlot.getStart() + " (Scheduled)" : "Not Set";
    }

    /**
     * Gets the appointment status
     * @return The current AppointmentStatus
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Gets the status display value
     * @return Human-readable status string
     */
    public String getStatusDisplay() {
        return (status != null) ? status.getDisplayValue() : "Unknown";
    }

    /**
     * Gets the appointment fee with hospital tax
     * Logic: Automatically adds 10% hospital service tax to the doctor's rate
     * @return Total fee including tax
     */
    public double getFee() {
        return fee * 1.10;
    }

    /**
     * Gets the appointment fee without tax
     * @return Base fee from doctor's hourly rate
     */
    public double getBaseFee() {
        return fee;
    }

    // --- BUSINESS LOGIC METHODS ---

    /**
     * Checks if this appointment can be cancelled
     * Logic: Only BOOKED appointments can be cancelled
     * @return true if appointment can be cancelled
     */
    public boolean canBeCancelled() {
        return status == AppointmentStatus.BOOKED;
    }

    /**
     * Checks if this appointment can be rescheduled
     * Logic: Only BOOKED appointments can be rescheduled
     * @return true if appointment can be rescheduled
     */
    public boolean canBeRescheduled() {
        return status == AppointmentStatus.BOOKED;
    }

    /**
     * Checks if this appointment is still active (not cancelled or completed)
     * @return true if appointment is active
     */
    public boolean isActive() {
        return status == AppointmentStatus.BOOKED || status == AppointmentStatus.RESCHEDULED;
    }

    /**
     * Cancels this appointment
     * Logic: Updates status to CANCELLED and frees up the time slot
     * @return true if cancellation was successful
     */
    public boolean cancel() {
        if (canBeCancelled()) {
            setStatus(AppointmentStatus.CANCELLED);
            return true;
        }
        return false;
    }

    /**
     * Marks this appointment as completed
     * @return true if status update was successful
     */
    public boolean markAsCompleted() {
        if (status == AppointmentStatus.BOOKED) {
            setStatus(AppointmentStatus.COMPLETED);
            return true;
        }
        return false;
    }

    // --- STRING REPRESENTATION ---

    /**
     * Provides a detailed appointment summary
     * @return Formatted appointment receipt
     */
    @Override
    public String toString() {
        String patientName = (patient != null) ? patient.getName() : "Unknown Patient";
        String doctorName = (doctor != null) ? doctor.getName() : "Unknown Doctor";
        String appointmentDate = getDate();
        String appointmentTime = (timeSlot != null) ? timeSlot.getStartRaw() : "Not Set";
        String statusInfo = getStatusDisplay();
        
        return "Receipt " + getId() + 
               " | Patient: " + patientName + 
               " | Doctor: " + doctorName + 
               " | Date: " + appointmentDate +
               " | Time: " + appointmentTime +
               " | Status: " + statusInfo +
               " | Fee: $" + String.format("%.2f", getFee());
    }

    /**
     * Gets a concise appointment summary
     * @return Brief appointment details
     */
    public String getAppointmentSummary() {
        return getId() + " - " + getDate() + " at " + getTime() + 
               " with " + ((doctor != null) ? doctor.getName() : "Unknown") + 
               " [" + getStatusDisplay() + "]";
    }
}