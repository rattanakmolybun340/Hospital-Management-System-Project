package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor Class
 * 
 * Represents a doctor in the hospital system.
 * Manages doctor information, availability schedule, and appointments.
 * 
 * Key Relationships:
 * - Doctor → Schedule (doctor's working availability)
 * - Doctor → ArrayList<Appointment> (doctor's scheduled appointments)
 * 
 * Access Modifiers:
 * - Private: name, specialist, hourlyRate, availability, appointments (encapsulated data)
 * - Private Static: doctorCount (static counter)
 * - Public: Getters, setters, appointment management methods (interface)
 */
public class Doctor {
    // Private fields - encapsulated data
    private String name;
    private String specialist;
    private double hourlyRate;
    private Schedule schedule;       // Doctor's working schedule
    private List<Appointment> appointments;    // Doctor's scheduled appointments
    
    // Private static field - tracks total doctors created
    private static int doctorCount = 0;

    /**
     * Constructor: Creates a new doctor with specialization and schedule
     * @param name Doctor's full name
     * @param specialist Doctor's medical specialty
     * @param hourlyRate Doctor's hourly rate (requires admin password to modify)
     * @param availability List of Schedule objects showing doctor's availability
     */
    public Doctor(String name, String specialist, double hourlyRate, Schedule schedule) {
        // Even the constructor should use the setters to ensure validation logic is applied immediately
        setName(name);
        setSpecialist(specialist);
        this.hourlyRate = hourlyRate;
        setSchedule(schedule);
        this.appointments = new ArrayList<>();  // Initialize empty appointment list
        doctorCount++;
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the doctor name
     * Condition: Prevents "anonymous" doctors or accidental empty strings
     * @param name Doctor's full name
     */
    public void setName(String name) {
        // Condition: Prevents "anonymous" doctors or accidental empty strings
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty. Setting to 'Unknown Doctor'.");
            this.name = "Unknown Doctor";
        } else {
            this.name = name;
        }
    }

    /**
     * Sets the doctor's medical specialty
     * Condition: Ensures the doctor has a valid department
     * @param specialist Medical specialty
     */
    public void setSpecialist(String specialist) {
        // Condition: Ensures the doctor has a valid department
        if (specialist == null || specialist.trim().isEmpty()) {
            System.out.println("Error: Specialist field required. Setting to 'General Practice'.");
            this.specialist = "General Practice";
        } else {
            this.specialist = specialist;
        }
    }

    /**
     * Sets the doctor's hourly rate (requires admin password)
     * Security Condition: Only allows modification with correct admin code
     * @param hourlyRate The new hourly rate
     * @param accessCode Admin password for authorization
     */
    public void setHourlyRate(double hourlyRate, String accessCode) {
        // Condition 1: Security check
        if (!accessCode.equals("ADMIN123")) {
            System.out.println("Access Denied: Incorrect password to modify salary.");
            return;
        }
        // Condition 2: Data Integrity (A doctor cannot pay the hospital to work)
        if (hourlyRate < 0) {
            System.out.println("Error: Rate cannot be negative.");
        } else {
            this.hourlyRate = hourlyRate;
        }
    }

    /**
     * Sets the doctor's availability schedule
     * Condition: A doctor must have at least an empty list to avoid NullPointerException
     * @param availability List of Schedule objects
     */
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            System.out.println("Warning: Doctor has no assigned schedule.");
        } 
        this.schedule = schedule;
    }

    // --- GETTERS (Accessors) with Business Logic ---

    /**
     * Gets the doctor name in professional format
     * Condition: Format the output for professional display
     * @return Doctor's name with "Dr." prefix
     */
    public String getName() {
        // Condition: Format the output for professional display
        if (this.name == null) return "No Name Assigned";
        return "Dr. " + this.name;
    }

    /**
     * Gets the doctor's specialty in standardized format
     * Condition: Logic to return a readable string
     * @return Specialty in uppercase
     */
    public String getSpecialist() {
        // Condition: Logic to return a readable string
        return this.specialist.toUpperCase(); 
    }

    /**
     * Gets the doctor's hourly rate
     * @return Hourly rate in dollars
     */
    public double getHourlyRate() {
        // Logic: You could add a condition here to check if the user is authorized 
        // to see the salary, otherwise return 0.0
        return hourlyRate;
    }

    /**
     * Gets the doctor's availability schedule
     * Condition: Logic to ensure we don't return a null list that might crash the app
     * @return List of Schedule objects
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the total count of doctors created
     * @return Static doctor count
     */
    public static int getDoctorCount() {
        return doctorCount;
    }

    // --- APPOINTMENT MANAGEMENT METHODS ---

    /**
     * Gets all appointments for this doctor
     * @return List of doctor's appointments
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);  // Return copy to prevent external modification
    }

    /**
     * Gets count of total appointments for this doctor
     * @return Number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active (booked) appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    /**
     * Checks if this doctor has any appointments
     * @return true if doctor has at least one appointment
     */
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    // --- DUPLICATE APPOINTMENT VALIDATION ---

    /**
     * Checks if doctor has a conflicting appointment at the given time slot
     * Business Logic: Prevents double-booking (doctor cannot see two patients at once)
     * @param timeSlot The time slot to check
     * @return true if doctor already has an appointment during this time slot
     */
    public boolean hasConflictingAppointment(TimeSlot timeSlot) {
        if (timeSlot == null) {
            return false;
        }
        
        // Check all active appointments for time conflicts
        for (Appointment apt : appointments) {
            if (!apt.isActive()) {
                continue; // Skip cancelled/completed appointments
            }
            
            TimeSlot existingSlot = apt.getTimeSlot();
            if (existingSlot == null) {
                continue;
            }
            
            // Same date and overlapping times = conflict
            if (existingSlot.getDate().equals(timeSlot.getDate())) {
                // Check if times overlap
                String existingStart = existingSlot.getStartRaw();
                String existingEnd = existingSlot.getEndRaw();
                String newStart = timeSlot.getStartRaw();
                String newEnd = timeSlot.getEndRaw();
                
                // Simple overlap check: if new appointment starts before existing ends
                if (newStart.compareTo(existingEnd) < 0 && 
                    newEnd.compareTo(existingStart) > 0) {
                    return true; // Conflict found
                }
            }
        }
        return false;
    }

    /**
     * Validates if an appointment can be booked for this doctor
     * Business Logic: Ensures appointment doesn't violate business rules
     * @param appointment The appointment to validate
     * @return true if appointment can be booked, false otherwise
     */
    public boolean canBookAppointment(Appointment appointment) {
        if (appointment == null || appointment.getTimeSlot() == null) {
            System.out.println("Error: Invalid appointment or time slot.");
            return false;
        }
        
        if (hasConflictingAppointment(appointment.getTimeSlot())) {
            System.out.println("Error: Doctor already has an appointment at this time.");
            return false;
        }
        
        if (!appointment.getTimeSlot().isAvailable()) {
            System.out.println("Error: Selected time slot is not available.");
            return false;
        }
        
        return true;
    }

    /**
     * Adds an appointment to this doctor's appointment list
     * Business Logic: Validates appointment before adding and prevents duplicates
     * @param appointment The appointment to add
     * @return true if appointment was added successfully
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            System.out.println("Error: Cannot add null appointment to doctor record.");
            return false;
        }
        
        if (!canBookAppointment(appointment)) {
            return false;
        }
        
        appointments.add(appointment);
        System.out.println("Appointment successfully booked with " + this.getName());
        return true;
    }

    /**
     * Removes an appointment from this doctor's list
     * @param appointment The appointment to remove
     * @return true if appointment was removed
     */
    public boolean removeAppointment(Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        return appointments.remove(appointment);
    }

    /**
     * Cancels an appointment for this doctor
     * Business Logic: Cancels the appointment and frees up the time slot
     * @param appointment The appointment to cancel
     * @return true if cancellation was successful
     */
    public boolean cancelAppointment(Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        
        if (appointment.cancel()) {
            System.out.println("Appointment cancelled successfully. Time slot is now available.");
            return true;
        }
        
        return false;
    }

    // --- STRING REPRESENTATION ---

    /**
     * Gets a detailed doctor profile summary
     * @return Doctor information formatted for display
     */
    public String getDoctorSummary() {
        return getName() +
               " | Specialty: " + getSpecialist() +
               " | Hourly Rate: $" + hourlyRate +
               " | Appointments: " + getAppointmentCount() +
               " | Available Slots: " + getAvailableSlotCount();
    }

    /**
     * Gets count of available time slots across all schedules
     * @return Number of available slots
     */
public int getAvailableSlotCount() {
        int count = 0;
        if (schedule != null && schedule.getSlots() != null) {
            for (TimeSlot slot : schedule.getSlots()) {
                if (slot.isAvailable()) {
                    count++;
                }
            }
        }
        return count;
    }
}