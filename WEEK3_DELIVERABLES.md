# Week 3 Deliverable Documentation
## Hospital Management System - Access Modifiers & Class Relationships

---

## 1. ACCESS MODIFIER REVIEW

### Summary Table

| Class | Fields | Access | Methods | Access | Static Counter |
|-------|--------|--------|---------|--------|-----------------|
| `AppointmentStatus` | Enum (displayValue) | Private | getDisplayValue() | Public | N/A |
| `TimeSlot` | date, start, end, isAvailable | Private | getters/setters | Public | N/A |
| `Appointment` | id, patient, doctor, timeSlot, status, fee | Private | getters/setters/business logic | Public | N/A |
| `Patient` | name, age, gender, dob, phone, appointments | Private | getters/setters/appointment mgmt | Public | patientCount (Private Static) |
| `Doctor` | name, specialist, hourlyRate, availability, appointments | Private | getters/setters/appointment mgmt | Public | doctorCount (Private Static) |
| `Schedule` | day, slots, startTime, endTime, isAvailable | Private | getters/setters | Public | N/A |
| `HospitalSystem` | patients, doctors, appointments, appointmentMap, counter | Private | All management methods | Public | N/A |

---

## 2. DETAILED CLASS DESCRIPTIONS

### AppointmentStatus (Enum)
**Purpose:** Defines appointment lifecycle states
**Access Modifiers:**
- `displayValue`: **private** - encapsulated display text
- `getDisplayValue()`: **public** - allows access to status name

**Valid States:**
```
BOOKED      → Appointment confirmed, scheduled
CANCELLED   → Appointment cancelled, time slot freed
COMPLETED   → Appointment finished
RESCHEDULED → Appointment moved to different time
```

---

### TimeSlot
**Purpose:** Represents a specific time interval on a date with availability tracking

**Private Fields:**
- `date` (String) - YYYY-MM-DD format, immutable after validation
- `start` (String) - HH:MM format, immutable after validation
- `end` (String) - HH:MM format, immutable after validation
- `isAvailable` (boolean) - tracks booking status

**Public Methods:**
- Constructor: `TimeSlot(String date, String start, String end)`
- Getters: `getDate()`, `getStart()`, `getEnd()`, `isAvailable()`, `getSlotDetails()`
- Setters: `setDate()`, `setStart()`, `setEnd()`, `setAvailable()`
- Helper: `getStartRaw()`, `getEndRaw()` (for time comparisons)

**Key Logic:**
- Validates date format (YYYY-MM-DD)
- Validates time format (HH:MM)
- Marks unavailable when appointment is booked
- Marks available again when appointment is cancelled

---

### Appointment
**Purpose:** Links Patient, Doctor, and TimeSlot; manages appointment lifecycle

**Private Fields:**
- `id` (String) - unique appointment reference
- `patient` (Patient) - cannot be null or changed
- `doctor` (Doctor) - cannot be null or changed
- `timeSlot` (TimeSlot) - contains date and time
- `status` (AppointmentStatus) - BOOKED, CANCELLED, COMPLETED, RESCHEDULED
- `fee` (double) - captured at booking time

**Public Methods:**
- Constructor: `Appointment(String id, Patient patient, Doctor doctor, TimeSlot timeSlot)`
- Status: `getStatus()`, `setStatus()`, `getStatusDisplay()`
- Validation: `canBeCancelled()`, `canBeRescheduled()`, `isActive()`
- Operations: `cancel()`, `markAsCompleted()`
- Getters: `getId()`, `getPatient()`, `getDoctor()`, `getTimeSlot()`, `getDate()`, `getTime()`, `getFee()`
- Display: `toString()`, `getAppointmentSummary()`

**Key Logic:**
- Fee is locked at booking time (captures doctor's rate)
- Only BOOKED appointments can be cancelled/rescheduled
- Cancellation automatically frees the TimeSlot
- TimeSlot marked unavailable when appointment booked
- TimeSlot marked available when appointment cancelled

---

### Patient
**Purpose:** Represents a patient with demographics and appointment history

**Private Fields:**
- `name` (String) - min 2 characters
- `age` (int) - 0-150 range validation
- `gender` (String) - "Male", "Female", "Other"
- `date_of_birth` (String) - YYYY-MM-DD format
- `phone` (String) - masked for privacy, min 10 digits
- `appointments` (List<Appointment>) - **NEW** - patient's appointment history

**Private Static Field:**
- `patientCount` (int) - total patients created (incremented in constructor)

**Public Methods:**
- Constructor: `Patient(String name, int age, String gender, String dob, String phone)`
- Personal Info: `getName()`, `getAge()`, `getGender()`, `getDate_of_birth()`, `getPhone()`
- Setters: `setName()`, `setAge()`, `setGender()`, `setDate_of_birth()`, `setPhone()`
- **Appointment Management:**
  - `addAppointment(Appointment)` - adds to history
  - `removeAppointment(Appointment)` - removes from history
  - `getAppointments()` - returns copy of list
  - `getAppointmentCount()` - total appointments
  - `getActiveAppointmentCount()` - non-cancelled appointments
  - `hasAppointments()` - boolean check
  - `getLastAppointment()` - most recent appointment
- Static: `getPatientCount()`
- Display: `getPatientSummary()`

**Key Relationships:**
- **Patient → ArrayList<Appointment>** (one patient can have many appointments)

---

### Doctor
**Purpose:** Represents a doctor with specialization, schedule, and appointment management

**Private Fields:**
- `name` (String) - doctor's full name
- `specialist` (String) - medical specialty
- `hourlyRate` (double) - requires ADMIN123 password to modify
- `availability` (List<Schedule>) - doctor's working schedules
- `appointments` (List<Appointment>) - **NEW** - doctor's booked appointments

**Private Static Field:**
- `doctorCount` (int) - total doctors created (incremented in constructor)

**Public Methods:**
- Constructor: `Doctor(String name, String specialist, double rate, List<Schedule> availability)`
- Personal Info: `getName()`, `getSpecialist()`, `getHourlyRate()`
- Schedule: `getAvailability()`, `setAvailability()`
- Setters: `setName()`, `setSpecialist()`, `setHourlyRate(String, String password)`
- **Appointment Management:**
  - `getAppointments()` - returns copy of list
  - `getAppointmentCount()` - total appointments
  - `getActiveAppointmentCount()` - active appointments
  - `hasAppointments()` - boolean check
  - `addAppointment(Appointment)` - adds with validation
  - `removeAppointment(Appointment)` - removes from list
  - `cancelAppointment(Appointment)` - cancels with status update
- **Validation & Conflict Detection:**
  - `hasConflictingAppointment(TimeSlot)` - **NEW** - checks for double-booking
  - `canBookAppointment(Appointment)` - validates before booking
- Static: `getDoctorCount()`
- Display: `getDoctorSummary()`, `getAvailableSlotCount()`

**Key Relationships:**
- **Doctor → List<Schedule>** (doctor works multiple days/times)
- **Doctor → ArrayList<Appointment>** (one doctor can have many appointments)
- **Duplicate Prevention:** `hasConflictingAppointment()` validates same-date, overlapping-time prevention

---

### Schedule
**Purpose:** Represents a doctor's working schedule for a specific day with time slots

**Private Fields:**
- `day` (String) - "Monday", "Tuesday", etc. (standardized)
- `slots` (List<TimeSlot>) - all available time slots for this day
- `startTime` (String) - shift start (e.g., "09:00")
- `endTime` (String) - shift end (e.g., "17:00")
- `isAvailable` (boolean) - false if doctor is on holiday/leave

**Public Methods:**
- Constructor: `Schedule(String day, List<TimeSlot> slots, String start, String end, boolean available)`
- Getters: `getDay()`, `getSlots()`, `isAvailable()`, `getShiftDuration()`
- Setters: `setDay()`, `setSlots()`, `setStartTime()`, `setEndTime()`, `setAvailable()`

**Key Logic:**
- Returns empty slot list if `isAvailable` is false (holiday/leave)
- Enforces endTime >= startTime

**Key Relationships:**
- **Schedule → ArrayList<TimeSlot>** (one schedule contains many time slots)

---

### HospitalSystem
**Purpose:** Central management class for entire hospital operations

**Private Fields:**
- `patients` (List<Patient>) - all registered patients
- `doctors` (List<Doctor>) - all registered doctors
- `appointments` (List<Appointment>) - all system appointments
- `appointmentMap` (Map<String, Appointment>) - fast lookup by ID
- `appointmentCounter` (int) - generates unique appointment IDs

**Private Static Constant:**
- `HOSPITAL_NAME` (String) = "City General Hospital"

**Public Methods:**

**Patient Management:**
- `registerPatient(Patient)` - adds new patient
- `removePatient(Patient)` - removes patient
- `getAllPatients()` - returns copy of patient list
- `findPatientByName(String)` - searches by name
- `getPatientCount()` - total patients

**Doctor Management:**
- `registerDoctor(Doctor)` - adds new doctor
- `removeDoctor(Doctor)` - removes doctor
- `getAllDoctors()` - returns copy of doctor list
- `findDoctorByName(String)` - searches by name
- `findDoctorsBySpecialty(String)` - searches by medical specialty
- `getDoctorCount()` - total doctors

**Appointment Management:**
- `bookAppointment(Patient, Doctor, TimeSlot)` - **validates conflicts before booking**
- `cancelAppointment(Appointment)` - cancels with status update
- `completeAppointment(Appointment)` - marks as completed
- `findAppointmentById(String)` - lookup by ID
- `getAllAppointments()` - returns copy of appointment list
- `getPatientAppointments(Patient)` - appointments for patient
- `getDoctorAppointments(Doctor)` - appointments for doctor
- `getAppointmentCount()` - total appointments
- `getActiveAppointmentCount()` - active (not cancelled) appointments

**Reporting:**
- `generateSystemReport()` - formatted system statistics
- `displayAllPatients()` - prints all patients
- `displayAllDoctors()` - prints all doctors
- `displayAllAppointments()` - prints all appointments
- `getHospitalName()` - returns hospital name

**Key Features:**
- Prevents double-booking (validates doctor availability)
- Manages appointment lifecycle (BOOKED → CANCELLED/COMPLETED)
- Maintains appointment history for patients and doctors
- Automatic ID generation for appointments

---

## 3. CLASS RELATIONSHIPS (UML Diagram)

```
┌─────────────────────────────────────────────────┐
│         HospitalSystem (Central Hub)            │
├─────────────────────────────────────────────────┤
│ - patients: ArrayList<Patient>                  │
│ - doctors: ArrayList<Doctor>                    │
│ - appointments: ArrayList<Appointment>          │
│ - appointmentMap: Map<String, Appointment>      │
│ - appointmentCounter: int                       │
├─────────────────────────────────────────────────┤
│ + bookAppointment()                             │
│ + cancelAppointment()                           │
│ + findPatientByName()                           │
│ + findDoctorByName()                            │
│ + ... (management methods)                      │
└─────────────────────────────────────────────────┘
    │                       │                    │
    │ manages             │ manages            │ manages
    ▼                     ▼                    ▼
┌─────────────┐    ┌─────────────┐    ┌──────────────┐
│   Patient   │    │   Doctor    │    │Appointment   │
├─────────────┤    ├─────────────┤    ├──────────────┤
│ - name      │    │ - name      │    │ - id         │
│ - age       │    │ - specialist│    │ - patient    │────┐
│ - phone     │    │ - hourlyRate│    │ - doctor     │────┼──> Patient
│ - appointments───┤ - availability   │ - timeSlot   │────┼──> Doctor
└─────────────┘    │ - appointments   │ - status     │────┼──> TimeSlot
                   ├─────────────┤    │ - fee        │    │
                   │ + addAppointment()            │    │
                   │ + hasConflictingAppointment() │    └──> AppointmentStatus
                   └─────────────┘    └──────────────┘
                       │
                       │ contains
                       ▼
                   ┌─────────────┐
                   │  Schedule   │
                   ├─────────────┤
                   │ - day       │
                   │ - slots     │
                   │ - startTime │
                   │ - endTime   │
                   └─────────────┘
                       │
                       │ contains
                       ▼
                   ┌─────────────┐
                   │  TimeSlot   │
                   ├─────────────┤
                   │ - date      │
                   │ - start     │
                   │ - end       │
                   │ - isAvailable
                   └─────────────┘
```

---

## 4. REQUIRED CLASS RELATIONSHIPS (Week 3)

✓ **Patient → ArrayList<Appointment>** 
- Stores appointment history
- Implemented in Patient class

✓ **Doctor → Schedule** 
- Doctor has List<Schedule> for availability
- Implemented in Doctor class

✓ **Schedule → ArrayList<TimeSlot>** 
- Schedule contains multiple TimeSlots
- Implemented in Schedule class

✓ **TimeSlot → date, time, availability** 
- Each slot has date, start, end, isAvailable
- Implemented in TimeSlot class

✓ **Appointment → Patient** 
- Links to specific patient
- Implemented in Appointment class

✓ **Appointment → Doctor** 
- Links to specific doctor
- Implemented in Appointment class

✓ **Appointment → TimeSlot** 
- Replaces String date/time with TimeSlot object
- Implemented in Appointment class

✓ **HospitalSystem → ArrayList<Patient>** 
- Manages all patients
- Implemented in HospitalSystem class

✓ **HospitalSystem → ArrayList<Doctor>** 
- Manages all doctors
- Implemented in HospitalSystem class

✓ **HospitalSystem → ArrayList<Appointment>** 
- Manages all appointments
- Implemented in HospitalSystem class

---

## 5. DUPLICATE APPOINTMENT VALIDATION

### Implementation in Doctor class:

```java
public boolean hasConflictingAppointment(TimeSlot timeSlot) {
    // Checks if doctor already has appointment during this time slot
    // Prevents: One doctor having two appointments at same time
    // Returns: true if conflict found, false if available
}

public boolean canBookAppointment(Appointment appointment) {
    // Validates appointment before booking
    // Checks: No conflicts, slot available, valid objects
}

public boolean addAppointment(Appointment appointment) {
    // Adds appointment with validation
    // Prevents double-booking automatically
}
```

### Validation Flow:
1. `HospitalSystem.bookAppointment()` calls `doctor.canBookAppointment()`
2. `canBookAppointment()` calls `hasConflictingAppointment()`
3. If conflict detected → appointment rejected
4. If valid → appointment added, TimeSlot marked unavailable

---

## 6. APPOINTMENT STATUS MANAGEMENT

### Status Lifecycle:

```
┌─────────────────────────────────────┐
│                                     │
│   NEW APPOINTMENT                   │
│         │                           │
│         ▼                           │
│   BOOKED (default status)           │
│    │          │                     │
│    │          │                     │
│    │          ├─→ COMPLETED (markAsCompleted)
│    │          │                     │
│    └──────────→ CANCELLED (cancel)  │
│               │                     │
│               ▼                     │
│          RESCHEDULED (future)       │
│                                     │
└─────────────────────────────────────┘
```

### Status Methods:
- `getStatus()` - returns current AppointmentStatus
- `setStatus(AppointmentStatus)` - updates status
- `cancel()` - changes to CANCELLED, frees TimeSlot
- `markAsCompleted()` - changes to COMPLETED
- `isActive()` - returns true if BOOKED or RESCHEDULED
- `canBeCancelled()` - returns true if BOOKED

---

## 7. SUMMARY OF WEEK 3 IMPROVEMENTS

✓ **Removed String date/time dependency**
- Now using TimeSlot with proper date and time tracking

✓ **Added appointment status tracking**
- AppointmentStatus enum with BOOKED, CANCELLED, COMPLETED, RESCHEDULED

✓ **Implemented duplicate appointment prevention**
- Doctor class validates no overlapping appointments at same time

✓ **Patient appointment history**
- ArrayList<Appointment> in Patient for tracking all appointments

✓ **Central management system**
- HospitalSystem class manages all patients, doctors, appointments

✓ **Clear access modifiers**
- All fields private, necessary methods public
- Static counters properly encapsulated

✓ **Comprehensive documentation**
- Class relationships clearly documented
- Business logic explained in comments
- Example usage in Main.java

---

## 8. TESTING THE SYSTEM

Run `Main.java` to see:
1. Patient registration
2. Doctor registration with schedules
3. TimeSlot creation with dates
4. Appointment booking with validation
5. Duplicate appointment prevention (demonstrations)
6. Appointment status changes
7. System reporting with statistics
8. Appointment history for patients and doctors

