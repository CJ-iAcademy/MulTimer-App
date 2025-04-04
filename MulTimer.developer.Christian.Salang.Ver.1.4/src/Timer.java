import javax.swing.JLabel;

public class Timer extends Thread {
    // Timer attributes
    int hours, minutes, seconds; // countdown time
    String message; // Custom message
    volatile boolean isRunning = true; // Flag to check if the timer is active
    int initialHours, initialMinutes, initialSeconds; // Stores initial values for reset
    private JLabel timerLabel; // GUI label to update countdown

    // Constructor to initialize timer values
    Timer(int hours, int minutes, int seconds, String message, JLabel timerLabel) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.message = message;
        this.initialHours = hours;
        this.initialMinutes = minutes;
        this.initialSeconds = seconds;
        this.timerLabel = timerLabel;
    }

    @Override
    public void run() {
        // Loop runs while the timer has time left and isRunning is true
        while (isRunning && (hours > 0 || minutes > 0 || seconds > 0)) {
            try {
                timerLabel.setText(String.format("%02d:%02d:%02d - %s", hours, minutes, seconds, message));
                Thread.sleep(1000); // Wait for 1 second
                decrementTime(); // Decrease the time values
            } catch (InterruptedException e) {
                break; // Exit loop if thread is interrupted
            }

            if (isRunning) {
                GUI.AlertTheUserThatTheTimerHasEnded(message); // Call alert when timer ends
            }
        }
    }

    // decrement the timer values
    private void decrementTime() {
        if (seconds > 0) {
            seconds--;
        } else if (minutes > 0) {
            minutes--;
            seconds = 59;
        } else if (hours > 0) {
            hours--;
            minutes = 59;
            seconds = 59;
        }
    }

    // Stops the timer and interrupts the thread
    public void stopTimer() {
        isRunning = false;
        this.interrupt();
    }

    // Resets the timer to its initial values
    public void resetTimer() {
        stopTimer();
        hours = initialHours;
        minutes = initialMinutes;
        seconds = initialSeconds;
        timerLabel.setText(String.format("%02d:%02d:%02d | %s", hours, minutes, seconds, message));
    }
}