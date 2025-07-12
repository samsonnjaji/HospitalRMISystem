package hospital.server;

import hospital.interfaces.HospitalService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server application that starts the RMI registry and registers the hospital service
 */
public class HospitalServer {

    public static void main(String[] args) {
        try {
            System.out.println("Starting MetroCare Hospital RMI Server...");

            // Create and start RMI registry on port 1099
            System.out.println("Creating RMI Registry on port 1099...");
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry created successfully!");

            // Create the hospital service implementation
            System.out.println("Initializing Hospital Service...");
            HospitalServiceImpl hospitalService = new HospitalServiceImpl();

            // Register the service with a name in the RMI registry
            String serviceName = "HospitalService";
            Naming.rebind("rmi://localhost:1099/" + serviceName, hospitalService);

            System.out.println("✅ Hospital Service registered successfully!");
            System.out.println("Service Name: " + serviceName);
            System.out.println("Service URL: rmi://localhost:1099/" + serviceName);
            System.out.println("\n🏥 MetroCare Hospital RMI Server is running...");
            System.out.println("Waiting for client connections...");
            System.out.println("Press Ctrl+C to stop the server.");

            // Keep the server running
            while (true) {
                Thread.sleep(5000);

                // Optional: Print periodic status
                if (hospitalService != null) {
                    hospitalService.printAppointmentStats();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}