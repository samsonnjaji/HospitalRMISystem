package hospital.client;

import hospital.interfaces.HospitalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.rmi.Naming;

/**
 * COMPACT GUI Client - ALL BUTTONS VISIBLE, FITS ALL SCREENS
 */
public class HospitalGUIClient extends JFrame {

    private HospitalService hospitalService;
    private JTextArea outputArea;
    private JComboBox<String> doctorComboBox;
    private JTextField patientNameField;
    private JButton connectButton, refreshButton, bookButton, checkButton, slotButton;
    private JLabel statusLabel;
    private boolean connected = false;

    // MUTED, PROFESSIONAL COLOR PALETTE
    private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color SUCCESS_GREEN = new Color(34, 139, 34);
    private static final Color ACCENT_PURPLE = new Color(126, 58, 242);
    private static final Color WARNING_ORANGE = new Color(245, 158, 11);
    private static final Color DANGER_RED = new Color(185, 28, 28);
    private static final Color DARK_GRAY = new Color(55, 65, 81);
    private static final Color LIGHT_GRAY = new Color(249, 250, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(31, 41, 55);

    public HospitalGUIClient() {
        initializeCompactGUI();
    }

    private void initializeCompactGUI() {
        setTitle("MetroCare Hospital - Appointment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // SMALLER SIZE - FITS ALL SCREENS
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Add custom hospital icon
        setIconImage(createHospitalIcon());

        setupSimpleComponents();
        layoutSimpleComponents();
        attachListeners();

        setVisible(true);
    }

    /**
     * Create custom hospital cross icon
     */
    private Image createHospitalIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw hospital cross
        g2d.setColor(PRIMARY_BLUE);
        g2d.fillRoundRect(2, 2, 28, 28, 6, 6);

        g2d.setColor(WHITE);
        // Vertical bar of cross
        g2d.fillRect(14, 8, 4, 16);
        // Horizontal bar of cross
        g2d.fillRect(10, 14, 12, 4);

        g2d.dispose();
        return icon;
    }

    private void setupSimpleComponents() {
        // Modern connection button - NO EMOJIS
        connectButton = createModernButton("Connect to Server", PRIMARY_BLUE);
        statusLabel = new JLabel("Not Connected");
        statusLabel.setForeground(DANGER_RED);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Modern doctor components
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setPreferredSize(new Dimension(200, 28));
        doctorComboBox.setMinimumSize(new Dimension(200, 28));
        doctorComboBox.setMaximumSize(new Dimension(200, 28));
        doctorComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        doctorComboBox.setEnabled(false);

        refreshButton = createModernButton("Refresh Doctors", SUCCESS_GREEN);
        refreshButton.setEnabled(false);

        // Modern patient field
        patientNameField = new JTextField();
        patientNameField.setPreferredSize(new Dimension(200, 28));
        patientNameField.setMinimumSize(new Dimension(200, 28));
        patientNameField.setMaximumSize(new Dimension(200, 28));
        patientNameField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        patientNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        patientNameField.setEnabled(false);

        // Modern action buttons - NO EMOJIS, CLEAR NAMES
        bookButton = createModernButton("Book Appointment", ACCENT_PURPLE);
        checkButton = createModernButton("Check Availability", WARNING_ORANGE);
        slotButton = createModernButton("Get Next Slot", PRIMARY_BLUE);

        // Initially disable all action buttons
        bookButton.setEnabled(false);
        checkButton.setEnabled(false);
        slotButton.setEnabled(false);

        // Modern output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        outputArea.setBackground(WHITE);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputArea.setText("MetroCare Hospital System\n" +
                "================================\n\n" +
                "Welcome to our appointment system!\n" +
                "Click 'Connect to Server' to begin\n\n");
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setPreferredSize(new Dimension(190, 32));
        button.setMinimumSize(new Dimension(190, 32));
        button.setMaximumSize(new Dimension(190, 32));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Modern hover effect
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = color;
            Color hoverColor = color.brighter();

            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor);
                }
            }
        });

        return button;
    }

    private void layoutSimpleComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GRAY);

        // Modern header with gradient feel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_GRAY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("MetroCare Hospital");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.EAST);

        // Main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left control panel - FIXED WIDTH TO PREVENT SHIFTING
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        leftPanel.setPreferredSize(new Dimension(240, 0));
        leftPanel.setMinimumSize(new Dimension(240, 0));
        leftPanel.setMaximumSize(new Dimension(240, Integer.MAX_VALUE));

        // Add components with modern labels - NO EMOJIS
        leftPanel.add(createModernLabel("Connection"));
        leftPanel.add(Box.createVerticalStrut(8));
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(connectButton);
        leftPanel.add(Box.createVerticalStrut(15));

        leftPanel.add(createModernLabel("Doctors"));
        leftPanel.add(Box.createVerticalStrut(8));
        doctorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(doctorComboBox);
        leftPanel.add(Box.createVerticalStrut(8));
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(refreshButton);
        leftPanel.add(Box.createVerticalStrut(15));

        leftPanel.add(createModernLabel("Patient Name"));
        leftPanel.add(Box.createVerticalStrut(8));
        patientNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(patientNameField);
        leftPanel.add(Box.createVerticalStrut(15));

        leftPanel.add(createModernLabel("Actions"));
        leftPanel.add(Box.createVerticalStrut(8));
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(bookButton);
        leftPanel.add(Box.createVerticalStrut(6));
        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(checkButton);
        leftPanel.add(Box.createVerticalStrut(6));
        slotButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(slotButton);

        // Right output panel with modern styling
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel outputLabel = createModernLabel("System Output & Activity Log");
        outputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rightPanel.add(outputLabel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void attachListeners() {
        connectButton.addActionListener(e -> connectToServer());
        refreshButton.addActionListener(e -> refreshDoctors());
        bookButton.addActionListener(e -> bookAppointment());
        checkButton.addActionListener(e -> checkAvailability());
        slotButton.addActionListener(e -> getNextSlot());
    }

    private void connectToServer() {
        try {
            appendOutput("Connecting to server...\n");
            String serverURL = "rmi://localhost:1099/HospitalService";
            hospitalService = (HospitalService) Naming.lookup(serverURL);

            connected = true;
            statusLabel.setText("Connected");
            statusLabel.setForeground(SUCCESS_GREEN);
            connectButton.setText("Connected");
            connectButton.setBackground(SUCCESS_GREEN);
            connectButton.setEnabled(false);

            refreshButton.setEnabled(true);
            doctorComboBox.setEnabled(true);
            patientNameField.setEnabled(true);
            bookButton.setEnabled(true);
            checkButton.setEnabled(true);
            slotButton.setEnabled(true);

            appendOutput("Connected successfully!\n");
            appendOutput("All premium features are now available.\n\n");
            refreshDoctors();

        } catch (Exception e) {
            appendOutput("Connection failed: " + e.getMessage() + "\n");
            appendOutput("Make sure server is running on localhost:1099\n\n");
        }
    }

    private void refreshDoctors() {
        if (!connected) return;
        try {
            appendOutput("Loading doctors from hospital database...\n");
            String[] doctors = hospitalService.getAvailableDoctors();

            doctorComboBox.removeAllItems();
            doctorComboBox.addItem("-- Select Doctor --");
            for (String doctor : doctors) {
                doctorComboBox.addItem(doctor);
            }

            appendOutput("Successfully loaded " + doctors.length + " available doctors:\n");
            appendOutput("==========================================\n");
            for (int i = 0; i < doctors.length; i++) {
                appendOutput((i + 1) + ". " + doctors[i] + "\n");
            }
            appendOutput("==========================================\n");
            appendOutput("Please select a doctor from the dropdown menu.\n\n");

        } catch (Exception e) {
            appendOutput("Error loading doctors: " + e.getMessage() + "\n\n");
        }
    }

    private void bookAppointment() {
        if (!connected) return;

        String doctor = (String) doctorComboBox.getSelectedItem();
        String patient = patientNameField.getText().trim();

        if (doctor == null || doctor.equals("-- Select Doctor --")) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        if (patient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter patient name.");
            return;
        }

        try {
            appendOutput("Booking appointment...\n");
            String result = hospitalService.bookAppointment(doctor, patient);
            appendOutput("APPOINTMENT CONFIRMED!\n");
            appendOutput("=======================================\n");
            appendOutput(result + "\n");
            appendOutput("=======================================\n\n");

            patientNameField.setText("");
            doctorComboBox.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
        } catch (Exception e) {
            appendOutput("Booking failed: " + e.getMessage() + "\n\n");
        }
    }

    private void checkAvailability() {
        if (!connected) return;

        String doctor = (String) doctorComboBox.getSelectedItem();
        if (doctor == null || doctor.equals("-- Select Doctor --")) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        try {
            boolean available = hospitalService.isDoctorAvailable(doctor);
            appendOutput("Availability check: " + doctor + " is " +
                    (available ? "AVAILABLE" : "NOT AVAILABLE") + "\n\n");
        } catch (Exception e) {
            appendOutput("Error: " + e.getMessage() + "\n\n");
        }
    }

    private void getNextSlot() {
        if (!connected) return;

        String doctor = (String) doctorComboBox.getSelectedItem();
        if (doctor == null || doctor.equals("-- Select Doctor --")) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        try {
            String slot = hospitalService.getNextAvailableSlot(doctor);
            appendOutput("Next available slot for " + doctor + ": " + slot + "\n\n");
        } catch (Exception e) {
            appendOutput("Error: " + e.getMessage() + "\n\n");
        }
    }

    private void appendOutput(String text) {
        outputArea.append(text);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalGUIClient());
    }
}