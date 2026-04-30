package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Patient Class
 * 
 * Represents a patient in the hospital system.
 * Stores patient demographics and maintains appointment history.
 * 
 * Key Relationships:
 * - Patient → ArrayList<Appointment> (appointment history)
 * 
 * Access Modifiers:
 * - Private: name, age, gender, date_of_birth, phone, appointments (encapsulated data)
 * - Private Static: patientCount (static counter)
 * - Public: Getters, setters, appointment management methods (interface)
 */
public class Patient {
    // Private fields - encapsulated data
    private String name;
    private int age;
    private String gender;
    private String date_of_birth;               // Format: YYYY-MM-DD
    private String phone;
    private List<Appointment> appointments;     // Patient's appointment history
    
    // Private static field - tracks total patients created
    private static int patientCount = 0;

    /**
     * Constructor: Creates a new patient with basic information
     * @param name Patient's full name
     * @param age Patient's age
     * @param gender Patient's gender
     * @param date_of_birth Patient's date of birth (YYYY-MM-DD)
     * @param phone Patient's contact phone number
     */
    public Patient(String name, int age, String gender, String date_of_birth, String phone) {
        // Always call setters in the constructor to trigger validation logic
        setName(name);
        setAge(age);
        setGender(gender);
        setDate_of_birth(date_of_birth);
        setPhone(phone);
        this.appointments = new ArrayList<>();  // Initialize empty appointment list
        patientCount++;
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the patient name
     * Logic: A patient name cannot be empty or too short
     * @param name The patient's full name
     */
    public void setName(String name) {
        // Logic: A patient name cannot be a number or empty.
        if (name == null || name.trim().length() < 2) {
            this.name = "Invalid Name";
        } else {
            this.name = name;
        }
    }

    /**
     * Sets the patient age
     * Logic: Age must be within realistic hospital bounds
     * @param age The patient's age
     */
    public void setAge(int age) {
        // Logic: Age cannot be negative, and logically in a hospital, 
        // someone over 150 is likely a data entry error.
        if (age < 0 || age > 150) {
            System.out.println("Error: Age is invalid. Setting to 0.");
            this.age = 0;
        } else {
            this.age = age;
        }
    }

    /**
     * Sets the patient gender
     * Logic: Restrict to specific medical categories
     * @param gender The patient's gender
     */
    public void setGender(String gender) {
        // Logic: Restrict to specific medical categories
        String g = gender.toLowerCase();
        if (g.equals("male") || g.equals("female") || g.equals("other")) {
            this.gender = gender;
        } else {
            this.gender = "Unknown";
        }
    }

    /**
     * Sets the patient date of birth
     * Logic: Ensures valid date format
     * @param date_of_birth The date of birth (YYYY-MM-DD format)
     */
    public void setDate_of_birth(String date_of_birth) {
        // Logic: Basic check to ensure it's not an empty string
        // (In a real app, you would check if the date is in the future)
        if (date_of_birth == null || !date_of_birth.contains("-")) {
            this.date_of_birth = "0000-00-00";
        } else {
            this.date_of_birth = date_of_birth;
        }
    }

    /**
     * Sets the patient phone number
     * Logic: Validates phone number length
     * @param phone The patient's phone number
     */
    public void setPhone(String phone) {
        // Logic: A phone number should usually have at least 10 digits
        if (phone != null && phone.replaceAll("[^0-9]", "").length() >= 10) {
            this.phone = phone;
        } else {
            this.phone = "Invalid Phone Number";
        }
    }

    // --- GETTERS (Accessors) with Business Logic ---

    /**
     * Gets the patient name
     * @return The patient's full name
     */
    public String getName() {
        // Logic: Return the name as entered
        return name;
    }

    /**
     * Gets the patient age
     * @return The patient's age
     */
    public int getAge() {
        // Logic: If the patient is a minor (under 18), we could trigger a warning
        if (age < 18) {
            System.out.println("[Note: Patient is a minor]");
        }
        return age;
    }

    /**
     * Gets the patient gender in standardized format
     * @return The gender formatted as first letter capital
     */
    public String getGender() {
        // Logic: Return a standardized format
        return gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
    }

    /**
     * Gets the patient date of birth
     * @return Date of birth formatted for display
     */
    public String getDate_of_birth() {
        return "DOB: " + date_of_birth;
    }

    /**
     * Gets the patient phone number (masked for privacy)
     * @return Phone number with first digits masked
     */
    public String getPhone() {
        // Logic: Mask the phone number for privacy (Security logic)
        // Show only the last 4 digits: *******1234
        if (phone.length() > 4) {
            return "*******" + phone.substring(phone.length() - 4);
        }
        return phone;
    }

    /**
     * Gets the total count of patients created
     * @return Static patient count
     */
    public static int getPatientCount() {
        return patientCount;
    }

    // --- APPOINTMENT MANAGEMENT METHODS ---

    /**
     * Adds an appointment to this patient's appointment history
     * @param appointment The appointment to add
     * @return true if appointment was added successfully
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            System.out.println("Error: Cannot add null appointment to patient record.");
            return false;
        }
        appointments.add(appointment);
        return true;
    }

    /**
     * Removes an appointment from this patient's history
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
     * Gets all appointments for this patient
     * @return List of all appointments
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);  // Return copy to prevent external modification
    }

    /**
     * Gets count of total appointments for this patient
     * @return Number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active (not cancelled) appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    /**
     * Checks if patient has any appointments
     * @return true if patient has at least one appointment
     */
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    /**
     * Gets the most recent appointment for this patient
     * @return The most recent appointment or null if none exist
     */
    public Appointment getLastAppointment() {
        if (appointments.isEmpty()) {
            return null;
        }
        return appointments.get(appointments.size() - 1);
    }

    // --- STRING REPRESENTATION ---

    /**
     * Gets a detailed patient profile summary
     * @return Patient information formatted for display
     */
    public String getPatientSummary() {
        return "Patient: " + name + 
               " | Age: " + age + 
               " | Gender: " + getGender() +
               " | DOB: " + date_of_birth +
               " | Phone: " + getPhone() +
               " | Appointments: " + getAppointmentCount();
    }
}