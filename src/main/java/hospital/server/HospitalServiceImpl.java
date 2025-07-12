package hospital.server;

import hospital.interfaces.HospitalService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implementation of HospitalService interface
 * Contains business logic for hospital appointment system
 */
public class HospitalServiceImpl extends UnicastRemoteObject implements HospitalService {

    // Data structures to store hospital information
    private final List<String> availableDoctors;
    private final Map<String, List<String>> doctorAppointments;
    private final Map<String, String> doctorSpecializations;
    private final DateTimeFormatter timeFormatter;

    /**
     * Constructor initializes dummy data for the hospital system
     */
    public HospitalServiceImpl() throws RemoteException {
        super();

        // Initialize available doctors
        availableDoctors = new ArrayList<>();
        availableDoctors.add("Dr. Sarah Wanjiku - Cardiologist");
        availableDoctors.add("Dr. James Kiprotich - Pediatrician");
        availableDoctors.add("Dr. Amina Hassan - Dermatologist");
        availableDoctors.add("Dr. Peter Mwangi - General Medicine");
        availableDoctors.add("Dr. Grace Achieng - Gynecologist");

        // Initialize appointment tracking
        doctorAppointments = new HashMap<>();
        for (String doctor : availableDoctors) {
            doctorAppointments.put(doctor, new ArrayList<>());
        }

        // Initialize doctor specializations
        doctorSpecializations = new HashMap<>();
        doctorSpecializations.put("Dr. Sarah Wanjiku - Cardiologist", "Heart and cardiovascular conditions");
        doctorSpecializations.put("Dr. James Kiprotich - Pediatrician", "Children's health and development");
        doctorSpecializations.put("Dr. Amina Hassan - Dermatologist", "Skin, hair, and nail conditions");
        doctorSpecializations.put("Dr. Peter Mwangi - General Medicine", "General health consultations");
        doctorSpecializations.put("Dr. Grace Achieng - Gynecologist", "Women's reproductive health");

        timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("HospitalService implementation initialized successfully!");
        System.out.println("Available doctors: " + availableDoctors.size());
    }

    @Override
    public String[] getAvailableDoctors() throws RemoteException {
        System.out.println("Client requested available doctors list");
        return availableDoctors.toArray(new String[0]);
    }

    @Override
    public String bookAppointment(String doctorName, String patientName) throws RemoteException {
        System.out.println("Booking request - Doctor: " + doctorName + ", Patient: " + patientName);

        // Validate input parameters
        if (patientName == null || patientName.trim().isEmpty()) {
            return "Error: Patient name cannot be empty";
        }

        if (doctorName == null || !availableDoctors.contains(doctorName)) {
            return "Error: Doctor '" + doctorName + "' is not available";
        }

        // Generate appointment details
        String appointmentTime = getNextAvailableSlot(doctorName);
        String appointmentId = generateAppointmentId();

        // Record the appointment
        List<String> appointments = doctorAppointments.get(doctorName);
        String appointmentRecord = String.format("Patient: %s, Time: %s, ID: %s",
                patientName, appointmentTime, appointmentId);
        appointments.add(appointmentRecord);

        // Create confirmation message
        String confirmation = String.format(
                "✅ APPOINTMENT CONFIRMED\n" +
                        "Patient: %s\n" +
                        "Doctor: %s\n" +
                        "Appointment Time: %s\n" +
                        "Appointment ID: %s\n" +
                        "Specialization: %s\n" +
                        "Location: MetroCare Hospital, Nairobi\n" +
                        "Please arrive 15 minutes early.",
                patientName, doctorName, appointmentTime, appointmentId,
                doctorSpecializations.get(doctorName)
        );

        System.out.println("Appointment booked successfully: " + appointmentId);
        return confirmation;
    }

    @Override
    public boolean isDoctorAvailable(String doctorName) throws RemoteException {
        boolean available = availableDoctors.contains(doctorName);
        System.out.println("Availability check for " + doctorName + ": " + available);
        return available;
    }

    @Override
    public String getNextAvailableSlot(String doctorName) throws RemoteException {
        // Simulate next available slot (in real system, this would check actual schedule)
        LocalDateTime nextSlot = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);

        // Add some randomness to appointment times
        Random random = new Random();
        nextSlot = nextSlot.plusHours(random.nextInt(8)); // 9 AM to 5 PM
        nextSlot = nextSlot.plusMinutes(random.nextInt(4) * 15); // 15-minute intervals

        return nextSlot.format(timeFormatter);
    }

    /**
     * Generates a unique appointment ID
     */
    private String generateAppointmentId() {
        return "MCH" + System.currentTimeMillis() % 100000;
    }

    /**
     * Gets appointment statistics (bonus method for demonstration)
     */
    public void printAppointmentStats() {
        System.out.println("\n=== APPOINTMENT STATISTICS ===");
        for (String doctor : availableDoctors) {
            List<String> appointments = doctorAppointments.get(doctor);
            System.out.println(doctor + ": " + appointments.size() + " appointments");
        }
        System.out.println("===============================\n");
    }
}