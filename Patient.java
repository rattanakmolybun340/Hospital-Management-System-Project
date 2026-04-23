public class Patient {
    private String name;
    private String id;
    private int age;
    private String phoneNumber;

    public Patient(String name, String id, int age, String phoneNumber) {
        setName(name);
        setId(id);
        setAge(age);
        setPhoneNumber(phoneNumber);
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.id = id;
        }
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        // Basic check for non-empty string; could be expanded to a specific format
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.phoneNumber = phoneNumber;
        }
    }

    // Getters
    public String getName() { return name; }
    public String getId() { return id; }
    public int getAge() { return age; }
    public String getPhoneNumber() { return phoneNumber; }
}