public class Schedule {
    private Doctor doctor;
    private String timeSlot;
    private String room;
    private boolean isAvailable;

    public Schedule(Doctor doctor, String timeSlot, String room, boolean isAvailable) {
        setDoctor(doctor);
        setTimeSlot(timeSlot);
        setRoom(room);
        setAvailable(isAvailable);
    }

    public void setDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctor = doctor;
        }
    }

    public void setTimeSlot(String timeSlot) {
        if (timeSlot != null && !timeSlot.trim().isEmpty()) {
            this.timeSlot = timeSlot;
        }
    }

    public void setRoom(String room) {
        if (room != null && !room.trim().isEmpty()) {
            this.room = room;
        }
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // Getters
    public Doctor getDoctor() { return doctor; }
    public String getTimeSlot() { return timeSlot; }
    public String getRoom() { return room; }
    public boolean isAvailable() { return isAvailable; }
}