package hospital.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for Hospital Appointment System
 * Defines methods that can be called remotely by clients
 */
public interface HospitalService extends Remote {

    /**
     * Retrieves list of available doctors
     * @return Array of available doctor names
     * @throws RemoteException if remote communication fails
     */
    String[] getAvailableDoctors() throws RemoteException;

    /**
     * Books an appointment with specified doctor for a patient
     * @param doctorName Name of the doctor
     * @param patientName Name of the patient
     * @return Confirmation message with appointment details
     * @throws RemoteException if remote communication fails
     */
    String bookAppointment(String doctorName, String patientName) throws RemoteException;

    /**
     * Checks if a specific doctor is available
     * @param doctorName Name of the doctor to check
     * @return true if doctor is available, false otherwise
     * @throws RemoteException if remote communication fails
     */
    boolean isDoctorAvailable(String doctorName) throws RemoteException;

    /**
     * Gets the next available appointment slot for a doctor
     * @param doctorName Name of the doctor
     * @return Next available time slot
     * @throws RemoteException if remote communication fails
     */
    String getNextAvailableSlot(String doctorName) throws RemoteException;
}