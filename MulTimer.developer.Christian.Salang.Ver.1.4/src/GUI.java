import java.awt.*;
import javax.swing.*;

public class GUI {
    private JFrame mainFrame; // Main window
    private JPanel[] timerPanels = new JPanel[5]; // Panels for each timer slot
    JLabel[] timerLabels = new JLabel[5]; // Labels to display time
    private int selectedTimer = -1; // Index of currently selected timer
    private TController controller; // Removes direct instantiation

    public GUI() {
        controller = new TController(this); 

        // Set up main window
        mainFrame = new JFrame("MulTimer App");
        mainFrame.setSize(600, 430);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(new Color(33, 33, 33));
        ImageIcon icon = new ImageIcon("MulTimerLogo.png");
        mainFrame.setIconImage(icon.getImage());

        // Control panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setBounds(-9, 20, 600, 35);
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(new Color(33, 33, 33));
        mainFrame.add(controlPanel);

        // Buttons for controlling timers
        String[] buttonLabels = {"Add Timer", "Remove Timer", "Start Timer", "Reset Timer"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            styleButton(button);
            button.addActionListener(e -> handleButtonClick(label));
            controlPanel.add(button);
        }

        // Creating UI for each timer slot
        for (int i = 0; i < 5; i++) {
            timerPanels[i] = new JPanel();
            timerPanels[i].setBounds(17, 80 + (i * 60), 550, 55);
            timerPanels[i].setBackground(new Color(50, 50, 50));
            timerPanels[i].setLayout(new FlowLayout());
            timerLabels[i] = new JLabel("00:00:00");
            timerLabels[i].setForeground(Color.WHITE);
            timerPanels[i].add(timerLabels[i]);

            int index = i;
            JButton pickButton = new JButton("Select");
            styleButton(pickButton);
            pickButton.addActionListener(e -> selectedTimer = index);
            timerPanels[i].add(pickButton);

            mainFrame.add(timerPanels[i]);
        }
        mainFrame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(90, 0, 160));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "Add Timer": addTimer(); break;
            case "Remove Timer": removeTimer(); break;
            case "Start Timer": startTimer(); break;
            case "Reset Timer": resetTimer(); break;
        }
    }

    private void addTimer() {
        if (controller.allTimers.size() >= 5) return;
    
        // Outer container to ensure background consistency
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setBackground(new Color(45, 45, 45));
    
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setLayout(new FlowLayout());
    
        JSpinner hours = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner minutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JSpinner seconds = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JTextField message = new JTextField(10);
    
        JLabel hoursLabel = new JLabel("Hours:");
        JLabel minutesLabel = new JLabel("Minutes:");
        JLabel secondsLabel = new JLabel("Seconds:");
        JLabel messageLabel = new JLabel("Message:");
    
        // Set foreground color to white
        for (JLabel label : new JLabel[]{hoursLabel, minutesLabel, secondsLabel, messageLabel}) {
            label.setForeground(Color.WHITE);
        }
    
        // Set dark background and white text for inputs
        for (JSpinner spinner : new JSpinner[]{hours, minutes, seconds}) {
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                editor.getComponent(0).setBackground(new Color(70, 70, 70));
                editor.getComponent(0).setForeground(Color.WHITE);
            }
        }
    
        message.setBackground(new Color(70, 70, 70));
        message.setForeground(Color.WHITE);
    
        // Add components to panel
        panel.add(hoursLabel);
        panel.add(hours);
        panel.add(minutesLabel);
        panel.add(minutes);
        panel.add(secondsLabel);
        panel.add(seconds);
        panel.add(messageLabel);
        panel.add(message);
    
        outerPanel.add(panel);
    
        // Apply dark theme to JOptionPane
        UIManager.put("OptionPane.background", new Color(45, 45, 45));
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    
        int result = JOptionPane.showConfirmDialog(null, outerPanel, "Set Timer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.newTimer((int) hours.getValue(), (int) minutes.getValue(), (int) seconds.getValue(), message.getText(), timerLabels[controller.allTimers.size()]);
            updateUI();
        }
    
        // Reset UI properties after dialog closes
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }
    

    private void removeTimer() {
        if (selectedTimer != -1) {
            controller.deleteTimer(selectedTimer);
            selectedTimer = -1;
            updateUI();
        }
    }

    private void startTimer() {
        if (selectedTimer != -1) {
            controller.startTimer(selectedTimer);
        }
    }

    private void resetTimer() {
        if (selectedTimer != -1) {
            controller.resetTimer(selectedTimer);
            updateUI();
        }
    }

    private void updateUI() {
        for (int i = 0; i < 5; i++) {
            if (i < controller.allTimers.size()) {
                TimerObject t = controller.allTimers.get(i);
                timerLabels[i].setText(String.format("%02d:%02d:%02d | %s", t.getHours(), t.getMinutes(), t.getSeconds(), t.getMessage()));
            } else {
                timerLabels[i].setText("00:00:00");
            }
        }
    }

    public JLabel getTimerLabel(int index) {
        return (index >= 0 && index < timerLabels.length) ? timerLabels[index] : null;
    }

    public static void AlertTheUserThatTheTimerHasEnded(String custom_message) {
        SwingUtilities.invokeLater(() -> {
            // Create a new frame for the alert
            JFrame alertFrame = new JFrame("Timer Alert");
            alertFrame.setSize(350, 180);
            alertFrame.setResizable(false);
            alertFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            alertFrame.setLocationRelativeTo(null);
            alertFrame.getContentPane().setBackground(new Color(33, 33, 33));
            
            // Create a panel with BoxLayout for vertical alignment
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(33, 33, 33));
    
            // Create a label with larger text
            JLabel label = new JLabel(custom_message, SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            // Add spacing
            panel.add(Box.createVerticalStrut(30));
            panel.add(label);
            panel.add(Box.createVerticalStrut(40));
    
            // "OK" Button centered
            JButton okButton = new JButton("OK");
            okButton.setBackground(new Color(90, 0, 160));
            okButton.setForeground(Color.WHITE);
            okButton.setFont(new Font("Arial", Font.BOLD, 16));
            okButton.setFocusPainted(false);
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            okButton.addActionListener(e -> alertFrame.dispose());
    
            panel.add(okButton);
            panel.add(Box.createVerticalStrut(10));
    
            alertFrame.add(panel);
            alertFrame.setVisible(true);
    
            SoundPlayer.playSound("notif_ringtone.wav");
        });
    }    
}