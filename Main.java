public class Main {
    public static void main(String[] args) {
        // 1. Create Doctor with an hourly rate
        Doctor doctor = new Doctor("Dr. Smith", 45, "Cardiology", 100.0);

        // 2. Setup Patient and Schedule
        Patient patient = new Patient("John Doe", "P101", 25, "555-1234");
        Schedule schedule = new Schedule(doctor, "Morning", "Room 101", true);

        // 3. Create Appointment (Initial fee will be 2 * 100 = 200)
        Appointment app = new Appointment("APP-001", patient, schedule, "Heart Check", 2.0);
        
        // 4. Update Fee manually (e.g., adding a special equipment charge)
        app.setFee(250.0);

        app.display();
    }
}
