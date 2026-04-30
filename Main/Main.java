package Main;

import java.util.ArrayList;
import java.util.List;
import Model.Appointment;
import Model.AppointmentStatus;
import Model.Doctor;
import Model.Patient;
import Model.Schedule;
import Model.TimeSlot;


/**
 * Main Class - Hospital Management System Entry Point
 * 
 * Demonstrates the hospital system with:
 * 1. Patient registration
 * 2. Doctor registration with schedules
 * 3. TimeSlot creation with dates
 * 4. Appointment booking with conflict validation
 * 5. Appointment status management (booked, cancelled, completed)
 * 6. System-wide reporting
 * 
 * Week 3 Deliverables Achieved:
 * ✓ AppointmentStatus enum for appointment states
 * ✓ TimeSlot with date and availability tracking
 * ✓ Appointment uses TimeSlot instead of String date/time
 * ✓ Patient has ArrayList<Appointment> for history
 * ✓ Doctor prevents duplicate appointments (conflict validation)
 * ✓ HospitalSystem manages all patients, doctors, appointments
 * ✓ Clear class relationships documented
 * ✓ Access modifiers clearly defined in all classes
 */
public class Main {
    public static void main(String[] args) {
        // Initialize the Hospital System - central management class
        HospitalSystem hospital = new HospitalSystem();
        System.out.println("Welcome to " + HospitalSystem.getHospitalName());
        System.out.println("Initializing Hospital Management System...\n");

        // ============================================
        // 1. CREATE PATIENTS
        // ============================================
        System.out.println("--- REGISTERING PATIENTS ---");
        Patient patient1 = new Patient("John Doe", 25, "Male", "1999-01-01", "01234567890");
        Patient patient2 = new Patient("Jane Smith", 30, "Female", "1994-05-15", "09876543210");
        Patient patient3 = new Patient("Michael Johnson", 45, "Male", "1979-03-20", "08765432109");

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);
        hospital.registerPatient(patient3);

        // ============================================
        // 2. CREATE TIME SLOTS WITH DATES
        // ============================================
        System.out.println("\n--- CREATING TIME SLOTS ---");
        
        // Monday slots
        TimeSlot monday_09_10 = new TimeSlot("2024-01-08", "09:00", "10:00");
        TimeSlot monday_10_11 = new TimeSlot("2024-01-08", "10:00", "11:00");
        TimeSlot monday_11_12 = new TimeSlot("2024-01-08", "11:00", "12:00");
        TimeSlot monday_14_15 = new TimeSlot("2024-01-08", "14:00", "15:00");

        // Tuesday slots
        TimeSlot tuesday_09_10 = new TimeSlot("2024-01-09", "09:00", "10:00");
        TimeSlot tuesday_10_11 = new TimeSlot("2024-01-09", "10:00", "11:00");

        List<TimeSlot> mondaySlots = new ArrayList<>();
        mondaySlots.add(monday_09_10);
        mondaySlots.add(monday_10_11);
        mondaySlots.add(monday_11_12);
        mondaySlots.add(monday_14_15);

        List<TimeSlot> tuesdaySlots = new ArrayList<>();
        tuesdaySlots.add(tuesday_09_10);
        tuesdaySlots.add(tuesday_10_11);

        System.out.println("Time slots created with proper date tracking");

        // ============================================
        // 3. CREATE SCHEDULES WITH TIME SLOTS
        // ============================================
        System.out.println("\n--- CREATING DOCTOR SCHEDULES ---");
        
        Schedule mondaySchedule = new Schedule("Monday", mondaySlots, "09:00", "17:00", true);
        Schedule tuesdaySchedule = new Schedule("Tuesday", tuesdaySlots, "09:00", "17:00", true);

        List<Schedule> doctorAvailability = new ArrayList<>();
        doctorAvailability.add(mondaySchedule);
        doctorAvailability.add(tuesdaySchedule);

        System.out.println("Schedules created with time slot associations");

        // ============================================
        // 4. CREATE DOCTORS WITH SCHEDULES
        // ============================================
        System.out.println("\n--- REGISTERING DOCTORS ---");
        
        Doctor doctor1 = new Doctor("Gregory House", "Diagnostic Medicine", 150.0, doctorAvailability);
        Doctor doctor2 = new Doctor("Lisa Cuddy", "Internal Medicine", 160.0, doctorAvailability);
        Doctor doctor3 = new Doctor("Robert Chase", "Cardiology", 180.0, doctorAvailability);

        hospital.registerDoctor(doctor1);
        hospital.registerDoctor(doctor2);
        hospital.registerDoctor(doctor3);

        // Test security: Try to change hourly rate with WRONG password
        System.out.println("\nTesting security - Attempting rate change with wrong password:");
        doctor1.setHourlyRate(400.0, "Hello123");

        // Test security: Change hourly rate with CORRECT password
        System.out.println("Testing security - Changing rate with correct password:");
        doctor1.setHourlyRate(200.0, "ADMIN123");

        // ============================================
        // 5. BOOK APPOINTMENTS WITH VALIDATION
        // ============================================
        System.out.println("\n--- BOOKING APPOINTMENTS ---");
        
        // Appointment 1: Patient 1 with Doctor 1 on Monday 09:00-10:00
        System.out.println("\nBooking: Patient1 with Doctor1 on Monday 09:00");
        Appointment app1 = hospital.bookAppointment(patient1, doctor1, monday_09_10);
        if (app1 != null) {
            System.out.println(app1.toString());
        }

        // Appointment 2: Patient 2 with Doctor 1 on Monday 10:00-11:00
        System.out.println("\nBooking: Patient2 with Doctor1 on Monday 10:00");
        Appointment app2 = hospital.bookAppointment(patient2, doctor1, monday_10_11);
        if (app2 != null) {
            System.out.println(app2.toString());
        }

        // Appointment 3: Patient 3 with Doctor 2 on Monday 09:00-10:00
        System.out.println("\nBooking: Patient3 with Doctor2 on Monday 09:00");
        Appointment app3 = hospital.bookAppointment(patient3, doctor2, monday_09_10);
        if (app3 != null) {
            System.out.println(app3.toString());
        }

        // ============================================
        // 6. TEST DUPLICATE APPOINTMENT VALIDATION
        // ============================================
        System.out.println("\n--- TESTING DUPLICATE APPOINTMENT PREVENTION ---");
        
        // Try to book doctor1 at overlapping time (should fail)
        System.out.println("\nAttempting to book Doctor1 at same Monday 09:00 time (should fail):");
        Appointment failedApp = hospital.bookAppointment(patient2, doctor1, monday_09_10);
        if (failedApp == null) {
            System.out.println("✓ Duplicate appointment correctly prevented!");
        }

        // Try to book the same time slot twice (should fail)
        System.out.println("\nAttempting to book already reserved Monday 09:00 slot (should fail):");
        Appointment failedApp2 = hospital.bookAppointment(patient1, doctor2, monday_09_10);
        if (failedApp2 == null) {
            System.out.println("✓ Double-booking correctly prevented!");
        }

        // ============================================
        // 7. TEST APPOINTMENT STATUS MANAGEMENT
        // ============================================
        System.out.println("\n--- TESTING APPOINTMENT STATUS MANAGEMENT ---");
        
        if (app1 != null) {
            System.out.println("\nAppointment 1 Status: " + app1.getStatusDisplay());
            
            // Cancel the appointment
            System.out.println("Cancelling appointment 1...");
            hospital.cancelAppointment(app1);
            System.out.println("Appointment 1 New Status: " + app1.getStatusDisplay());
            System.out.println("Time slot availability after cancellation: " + monday_09_10.isAvailable());
            
            // Try to cancel again (should fail)
            System.out.println("\nAttempting to cancel already cancelled appointment (should fail):");
            boolean cancelResult = hospital.cancelAppointment(app1);
            if (!cancelResult) {
                System.out.println("✓ Cannot cancel non-BOOKED appointment!");
            }
        }

        // ============================================
        // 8. DISPLAY SYSTEM REPORT
        // ============================================
        System.out.println(hospital.generateSystemReport());

        System.out.println("--- PATIENTS REGISTERED ---");
        hospital.displayAllPatients();

        System.out.println("\n--- DOCTORS REGISTERED ---");
        hospital.displayAllDoctors();

        System.out.println("\n--- ALL APPOINTMENTS ---");
        hospital.displayAllAppointments();

        // ============================================
        // 9. DISPLAY APPOINTMENT HISTORY
        // ============================================
        System.out.println("\n--- PATIENT APPOINTMENT HISTORY ---");
        System.out.println("\nPatient 2 (" + patient2.getName() + ") Appointments:");
        List<Appointment> patient2Appointments = hospital.getPatientAppointments(patient2);
        for (Appointment apt : patient2Appointments) {
            System.out.println("  " + apt.getAppointmentSummary());
        }

        System.out.println("\nDoctor 1 (" + doctor1.getName() + ") Appointments:");
        List<Appointment> doctor1Appointments = hospital.getDoctorAppointments(doctor1);
        for (Appointment apt : doctor1Appointments) {
            System.out.println("  " + apt.getAppointmentSummary());
        }

        // ============================================
        // 10. DISPLAY SUMMARY STATISTICS
        // ============================================
        System.out.println("\n========================================");
        System.out.println("SUMMARY STATISTICS");
        System.out.println("========================================");
        System.out.println("Total Patients: " + Patient.getPatientCount());
        System.out.println("Total Doctors: " + Doctor.getDoctorCount());
        System.out.println("Total System Appointments: " + hospital.getAppointmentCount());
        System.out.println("Active Appointments: " + hospital.getActiveAppointmentCount());
        System.out.println("\nPatient2 Total Appointments: " + patient2.getAppointmentCount());
        System.out.println("Patient2 Active Appointments: " + patient2.getActiveAppointmentCount());
        System.out.println("\nDoctor1 Total Appointments: " + doctor1.getAppointmentCount());
        System.out.println("Doctor1 Active Appointments: " + doctor1.getActiveAppointmentCount());
        System.out.println("========================================\n");

        System.out.println("Hospital System initialized successfully!");
    }
}

