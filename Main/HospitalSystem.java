package Main;

import Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * HospitalSystem Class
 * 
 * Central management system for the entire hospital.
 * Manages all patients, doctors, appointments, and schedules.
 * Provides centralized business logic for appointment booking, validation, and management.
 * 
 * Key Responsibilities:
 * 1. Manage Patient collection (ArrayList<Patient>)
 * 2. Manage Doctor collection (ArrayList<Doctor>)
 * 3. Manage Appointment collection (ArrayList<Appointment>)
 * 4. Validate and book appointments
 * 5. Prevent duplicate/conflicting appointments
 * 6. Track appointment history and status
 * 7. Provide system-wide reporting
 * 
 * Access Modifiers:
 * - Private: patients, doctors, appointments, appointmentMap, appointmentCounter (encapsulated data)
 * - Public: All management methods (interface for Main or UI components)
 */
public class HospitalSystem {
    // Hospital name constant
    private static final String HOSPITAL_NAME = "City General Hospital";
    
    // Private collections - encapsulated data
    private List<Patient> patients;              // All patients in the system
    private List<Doctor> doctors;                // All doctors in the system
    private List<Appointment> appointments;      // All appointments in the system
    private Map<String, Appointment> appointmentMap;  // Fast lookup by ID
    private int appointmentCounter;              // For generating unique appointment IDs

    /**
     * Constructor: Initializes the hospital system with empty collections
     */
    public HospitalSystem() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.appointmentMap = new HashMap<>();
        this.appointmentCounter = 0;
    }

    // --- PATIENT MANAGEMENT ---

    /**
     * Registers a new patient in the system
     * @param patient The patient to register
     * @return true if patient was registered successfully
     */
    public boolean registerPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Error: Cannot register null patient.");
            return false;
        }
        if (patients.contains(patient)) {
            System.out.println("Error: Patient already registered.");
            return false;
        }
        patients.add(patient);
        System.out.println("Patient registered: " + patient.getName());
        return true;
    }

    /**
     * Removes a patient from the system
     * @param patient The patient to remove
     * @return true if patient was removed
     */
    public boolean removePatient(Patient patient) {
        if (patient == null || !patients.contains(patient)) {
            return false;
        }
        patients.remove(patient);
        return true;
    }

    /**
     * Gets all patients in the system
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);  // Return copy
    }

    /**
     * Gets a patient by name
     * @param name Patient's name
     * @return Patient object or null if not found
     */
    public Patient findPatientByName(String name) {
        if (name == null) {
            return null;
        }
        for (Patient p : patients) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets total number of patients
     * @return Patient count
     */
    public int getPatientCount() {
        return patients.size();
    }

    // --- DOCTOR MANAGEMENT ---

    /**
     * Registers a new doctor in the system
     * @param doctor The doctor to register
     * @return true if doctor was registered successfully
     */
    public boolean registerDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Error: Cannot register null doctor.");
            return false;
        }
        if (doctors.contains(doctor)) {
            System.out.println("Error: Doctor already registered.");
            return false;
        }
        doctors.add(doctor);
        System.out.println("Doctor registered: " + doctor.getName());
        return true;
    }

    /**
     * Removes a doctor from the system
     * @param doctor The doctor to remove
     * @return true if doctor was removed
     */
    public boolean removeDoctor(Doctor doctor) {
        if (doctor == null || !doctors.contains(doctor)) {
            return false;
        }
        doctors.remove(doctor);
        return true;
    }

    /**
     * Gets all doctors in the system
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);  // Return copy
    }

    /**
     * Gets a doctor by name
     * @param name Doctor's name
     * @return Doctor object or null if not found
     */
    public Doctor findDoctorByName(String name) {
        if (name == null) {
            return null;
        }
        for (Doctor d : doctors) {
            // Handle the "Dr. " prefix in the getter
            String doctorNameFormatted = d.getName(); // Returns "Dr. Name"
            String doctorNameRaw = doctorNameFormatted.substring(4); // Remove "Dr. "
            if (doctorNameRaw.equalsIgnoreCase(name) || doctorNameFormatted.equalsIgnoreCase("Dr. " + name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Gets doctors by specialty
     * @param specialty Medical specialty
     * @return List of doctors with that specialty
     */
    public List<Doctor> findDoctorsBySpecialty(String specialty) {
        List<Doctor> matchingDoctors = new ArrayList<>();
        if (specialty == null) {
            return matchingDoctors;
        }
        for (Doctor d : doctors) {
            if (d.getSpecialist().equalsIgnoreCase(specialty.toUpperCase())) {
                matchingDoctors.add(d);
            }
        }
        return matchingDoctors;
    }

    /**
     * Gets total number of doctors
     * @return Doctor count
     */
    public int getDoctorCount() {
        return doctors.size();
    }

    // --- APPOINTMENT MANAGEMENT ---

    /**
     * Books a new appointment in the system
     * Business Logic: Validates and prevents duplicate appointments
     * @param patient The patient
     * @param doctor The doctor
     * @param timeSlot The time slot
     * @return The created Appointment object or null if booking failed
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, TimeSlot timeSlot) {
        // Validate inputs
        if (patient == null || doctor == null || timeSlot == null) {
            System.out.println("Error: Invalid patient, doctor, or time slot.");
            return null;
        }

        // Check if time slot is available
        if (!timeSlot.isAvailable()) {
            System.out.println("Error: Selected time slot is already booked.");
            return null;
        }

        // Check for conflicting appointments with the doctor
        if (doctor.hasConflictingAppointment(timeSlot)) {
            System.out.println("Error: Doctor is already booked at this time.");
            return null;
        }

        // Create the appointment
        appointmentCounter++;
        String appointmentId = String.format("%05d", appointmentCounter);
        Appointment appointment = new Appointment(appointmentId, patient, doctor, timeSlot);

        // Add to doctor's schedule
        if (!doctor.addAppointment(appointment)) {
            System.out.println("Error: Failed to add appointment to doctor's schedule.");
            return null;
        }

        // Add to patient's appointment history
        if (!patient.addAppointment(appointment)) {
            // Rollback if patient add fails
            doctor.removeAppointment(appointment);
            System.out.println("Error: Failed to add appointment to patient record.");
            return null;
        }

        // Add to system collections
        appointments.add(appointment);
        appointmentMap.put(appointmentId, appointment);

        // Mark time slot as unavailable only AFTER successful booking
        timeSlot.setAvailable(false);

        return appointment;
    }

    /**
     * Cancels an existing appointment
     * @param appointment The appointment to cancel
     * @return true if cancellation was successful
     */
    public boolean cancelAppointment(Appointment appointment) {
        if (appointment == null || !appointments.contains(appointment)) {
            System.out.println("Error: Appointment not found in system.");
            return false;
        }

        if (!appointment.canBeCancelled()) {
            System.out.println("Error: Appointment cannot be cancelled in its current state.");
            return false;
        }

        // Cancel the appointment (which also frees the time slot)
        appointment.cancel();

        System.out.println("Appointment " + appointment.getId() + " has been cancelled.");
        return true;
    }

    /**
     * Marks an appointment as completed
     * @param appointment The appointment to complete
     * @return true if marking as completed was successful
     */
    public boolean completeAppointment(Appointment appointment) {
        if (appointment == null || !appointments.contains(appointment)) {
            System.out.println("Error: Appointment not found in system.");
            return false;
        }

        if (appointment.markAsCompleted()) {
            System.out.println("Appointment " + appointment.getId() + " marked as completed.");
            return true;
        }
        return false;
    }

    /**
     * Gets an appointment by ID
     * @param appointmentId The appointment ID
     * @return Appointment object or null if not found
     */
    public Appointment findAppointmentById(String appointmentId) {
        if (appointmentId == null) {
            return null;
        }
        return appointmentMap.get(appointmentId);
    }

    /**
     * Gets all appointments in the system
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);  // Return copy
    }

    /**
     * Gets appointments for a specific patient
     * @param patient The patient
     * @return List of patient's appointments
     */
    public List<Appointment> getPatientAppointments(Patient patient) {
        if (patient == null) {
            return new ArrayList<>();
        }
        return patient.getAppointments();
    }

    /**
     * Gets appointments for a specific doctor
     * @param doctor The doctor
     * @return List of doctor's appointments
     */
    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        if (doctor == null) {
            return new ArrayList<>();
        }
        return doctor.getAppointments();
    }

    /**
     * Gets total number of appointments
     * @return Appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    // --- REPORTING AND STATISTICS ---

    /**
     * Generates a system status report
     * @return Formatted report string
     */
    public String generateSystemReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n========================================\n");
        report.append(HOSPITAL_NAME + " - SYSTEM REPORT\n");
        report.append("========================================\n");
        report.append("Total Patients: ").append(getPatientCount()).append("\n");
        report.append("Total Doctors: ").append(getDoctorCount()).append("\n");
        report.append("Total Appointments: ").append(getAppointmentCount()).append("\n");
        report.append("Active Appointments: ").append(getActiveAppointmentCount()).append("\n");
        report.append("========================================\n");
        return report.toString();
    }

    /**
     * Displays all patients in the system
     */
    public void displayAllPatients() {
        System.out.println("\n--- PATIENTS ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        for (Patient p : patients) {
            System.out.println(p.getPatientSummary());
        }
    }

    /**
     * Displays all doctors in the system
     */
    public void displayAllDoctors() {
        System.out.println("\n--- DOCTORS ---");
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }
        for (Doctor d : doctors) {
            System.out.println(d.getDoctorSummary());
        }
    }

    /**
     * Displays all appointments in the system
     */
    public void displayAllAppointments() {
        System.out.println("\n--- APPOINTMENTS ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.");
            return;
        }
        for (Appointment a : appointments) {
            System.out.println(a.getAppointmentSummary());
        }
    }

    /**
     * Gets the hospital name
     * @return Hospital name
     */
    public static String getHospitalName() {
        return HOSPITAL_NAME;
    }
}
