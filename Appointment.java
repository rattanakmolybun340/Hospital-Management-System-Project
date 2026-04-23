public class Appointment {
    private String appointmentID;
    private Patient patient;
    private Schedule schedule;
    private String consultation;
    private double hours;
    private double fee;

    public Appointment(String appointmentID, Patient patient, Schedule schedule, String consultation, double hours) {
        setAppointmentID(appointmentID);
        setPatient(patient);
        setSchedule(schedule);
        setConsultation(consultation);
        setHours(hours);
        // Initial fee calculation based on doctor's current rate
        this.fee = hours * schedule.getDoctor().getHourlyRate();
    }

    public void setAppointmentID(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.appointmentID = id;
        }
    }

    public void setPatient(Patient patient) {
        if (patient != null) this.patient = patient;
    }

    public void setSchedule(Schedule schedule) {
        if (schedule != null) this.schedule = schedule;
    }

    public void setConsultation(String consultation) {
        if (consultation != null && !consultation.trim().isEmpty()) {
            this.consultation = consultation;
        }
    }

    public void setHours(double hours) {
        if (hours > 0) {
            this.hours = hours;
        }
    }

    // Manual fee adjustment
    public void setFee(double fee) {
        if (fee >= 0) {
            this.fee = fee;
        }
    }

    // Getters
    public String getAppointmentID() { return appointmentID; }
    public Patient getPatient() { return patient; }
    public Schedule getSchedule() { return schedule; }
    public String getConsultation() { return consultation; }
    public double getHours() { return hours; }
    public double getFee() { return fee; }

    public void display() {
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Patient: " + patient.getName());
        System.out.println("Doctor: " + schedule.getDoctor().getName());
        System.out.println("Consultation: " + consultation);
        System.out.println("Fee: $" + fee);
    }
}