public class Doctor {
    private String name;
    private int age;
    private String specialist;
    private double hourlyRate;

    public Doctor(String name, int age, String specialist, double hourlyRate) {
        setName(name);
        setAge(age);
        setSpecialist(specialist);
        setHourlyRate(hourlyRate);
    }

    // Setters with validation
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setAge(int age) {
        if (age > 0 && age < 120) {
            this.age = age;
        }
    }

    public void setSpecialist(String specialist) {
        if (specialist != null && !specialist.trim().isEmpty()) {
            this.specialist = specialist;
        }
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate >= 0) {
            this.hourlyRate = hourlyRate;
        }
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSpecialist() { return specialist; }
    public double getHourlyRate() { return hourlyRate; }
}