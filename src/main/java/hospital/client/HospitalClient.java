package hospital.client;

import hospital.interfaces.HospitalService;
import java.rmi.Naming;
import java.util.Scanner;

/**
 * Client application for MetroCare Hospital appointment booking system
 * Connects to remote hospital service via RMI
 */
public class HospitalClient {

    private static HospitalService hospitalService;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        try {
            // Connect to the remote hospital service
            System.out.println("🏥 Welcome to MetroCare Hospital Appointment System");
            System.out.println("Connecting to hospital server...");

            String serverURL = "rmi://localhost:1099/HospitalService";
            hospitalService = (HospitalService) Naming.lookup(serverURL);

            System.out.println("✅ Connected to hospital server successfully!\n");

            // Main application loop
            boolean running = true;
            while (running) {
                running = showMainMenu();
            }

        } catch (Exception e) {
            System.err.println("❌ Error connecting to hospital server: " + e.getMessage());
            System.err.println("Please ensure the server is running on localhost:1099");
        } finally {
            scanner.close();
        }
    }

    /**
     * Displays the main menu and handles user choice
     */
    private static boolean showMainMenu() {
        try {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("        METROCARE HOSPITAL SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. View Available Doctors");
            System.out.println("2. Book an Appointment");
            System.out.println("3. Check Doctor Availability");
            System.out.println("4. Get Next Available Slot");
            System.out.println("5. Exit");
            System.out.println("=".repeat(50));
            System.out.print("Please select an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAvailableDoctors();
                    break;
                case 2:
                    bookAppointment();
                    break;
                case 3:
                    checkDoctorAvailability();
                    break;
                case 4:
                    getNextAvailableSlot();
                    break;
                case 5:
                    System.out.println("\nThank you for using MetroCare Hospital System!");
                    System.out.println("Have a great day! 🌟");
                    return false;
                default:
                    System.out.println("❌ Invalid option. Please select 1-5.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            scanner.nextLine(); // Clear invalid input
        }

        return true;
    }

    /**
     * Displays list of available doctors
     */
    private static void viewAvailableDoctors() {
        try {
            System.out.println("\n📋 Available Doctors at MetroCare Hospital:");
            System.out.println("-".repeat(60));

            String[] doctors = hospitalService.getAvailableDoctors();

            for (int i = 0; i < doctors.length; i++) {
                System.out.printf("%d. %s\n", i + 1, doctors[i]);
            }

            System.out.println("-".repeat(60));
            System.out.printf("Total doctors available: %d\n", doctors.length);

        } catch (Exception e) {
            System.err.println("❌ Error retrieving doctors: " + e.getMessage());
        }
    }

    /**
     * Handles appointment booking process
     */
    private static void bookAppointment() {
        try {
            System.out.println("\n📅 Book an Appointment");
            System.out.println("-".repeat(30));

            // First show available doctors
            String[] doctors = hospitalService.getAvailableDoctors();
            System.out.println("Available doctors:");
            for (int i = 0; i < doctors.length; i++) {
                System.out.printf("%d. %s\n", i + 1, doctors[i]);
            }

            // Get doctor selection
            System.out.print("\nSelect doctor (enter number 1-" + doctors.length + "): ");
            int doctorChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (doctorChoice < 1 || doctorChoice > doctors.length) {
                System.out.println("❌ Invalid doctor selection.");
                return;
            }

            String selectedDoctor = doctors[doctorChoice - 1];

            // Get patient name
            System.out.print("Enter patient name: ");
            String patientName = scanner.nextLine().trim();

            if (patientName.isEmpty()) {
                System.out.println("❌ Patient name cannot be empty.");
                return;
            }

            // Confirm booking details
            System.out.println("\n📋 Booking Summary:");
            System.out.println("Doctor: " + selectedDoctor);
            System.out.println("Patient: " + patientName);
            System.out.print("Confirm booking? (y/n): ");

            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                System.out.println("❌ Booking cancelled.");
                return;
            }

            // Make the appointment
            System.out.println("\n⏳ Processing appointment...");
            String result = hospitalService.bookAppointment(selectedDoctor, patientName);

            System.out.println("\n" + "=".repeat(60));
            System.out.println(result);
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("❌ Error booking appointment: " + e.getMessage());
            scanner.nextLine(); // Clear any invalid input
        }
    }

    /**
     * Checks if a specific doctor is available
     */
    private static void checkDoctorAvailability() {
        try {
            System.out.println("\n🔍 Check Doctor Availability");
            System.out.println("-".repeat(35));

            System.out.print("Enter doctor name to check: ");
            String doctorName = scanner.nextLine().trim();

            if (doctorName.isEmpty()) {
                System.out.println("❌ Doctor name cannot be empty.");
                return;
            }

            boolean available = hospitalService.isDoctorAvailable(doctorName);

            if (available) {
                System.out.println("✅ " + doctorName + " is available for appointments.");
            } else {
                System.out.println("❌ " + doctorName + " is not available.");
                System.out.println("Please check the available doctors list for correct names.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error checking availability: " + e.getMessage());
        }
    }

    /**
     * Gets the next available appointment slot for a doctor
     */
    private static void getNextAvailableSlot() {
        try {
            System.out.println("\n🕒 Get Next Available Slot");
            System.out.println("-".repeat(35));

            // Show available doctors first
            String[] doctors = hospitalService.getAvailableDoctors();
            System.out.println("Available doctors:");
            for (int i = 0; i < doctors.length; i++) {
                System.out.printf("%d. %s\n", i + 1, doctors[i]);
            }

            System.out.print("\nSelect doctor (enter number 1-" + doctors.length + "): ");
            int doctorChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (doctorChoice < 1 || doctorChoice > doctors.length) {
                System.out.println("❌ Invalid doctor selection.");
                return;
            }

            String selectedDoctor = doctors[doctorChoice - 1];
            String nextSlot = hospitalService.getNextAvailableSlot(selectedDoctor);

            System.out.println("\n📅 Next available appointment slot:");
            System.out.println("Doctor: " + selectedDoctor);
            System.out.println("Available time: " + nextSlot);

        } catch (Exception e) {
            System.err.println("❌ Error getting available slot: " + e.getMessage());
            scanner.nextLine(); // Clear any invalid input
        }
    }
}