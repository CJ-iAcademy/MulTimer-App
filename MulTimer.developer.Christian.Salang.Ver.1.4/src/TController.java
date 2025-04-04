import java.util.ArrayList;
import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;

public class TController {
    public ArrayList<TimerObject> allTimers = new ArrayList<>();
    private GUI gui;

    public TController(GUI gui) {
        this.gui = gui;
    }

    public void newTimer(int hours, int minutes, int seconds, String message, JLabel label) {
        TimerObject timer = new TimerObject(hours, minutes, seconds, message, label);
        allTimers.add(timer);
    }

    public void deleteTimer(int index) {
        if (index >= 0 && index < allTimers.size()) {
            allTimers.get(index).stopTimer();
            allTimers.remove(index);

            for (int i = index; i < allTimers.size(); i++) {
                allTimers.get(i).setLabel(gui.getTimerLabel(i));
            }
        }
    }

    public void startTimer(int index) {
        if (index >= 0 && index < allTimers.size()) {
            allTimers.get(index).start();
        }
    }

    public void resetTimer(int index) {
        if (index >= 0 && index < allTimers.size()) {
            allTimers.get(index).reset();
        }
    }
}

class TimerObject {
    private int hours, minutes, seconds;
    private final int initialHours, initialMinutes, initialSeconds; // Store original values
    private String message;
    private JLabel label;
    private Timer timer;
    private boolean running = false;

    public TimerObject(int hours, int minutes, int seconds, String message, JLabel label) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.initialHours = hours;  // Save original values
        this.initialMinutes = minutes;
        this.initialSeconds = seconds;
        this.message = message;
        this.label = label;
        updateLabel();
    }

    public void start() {
        if (running) return;
        running = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (seconds > 0) {
                    seconds--;
                } else if (minutes > 0) {
                    minutes--;
                    seconds = 59;
                } else if (hours > 0) {
                    hours--;
                    minutes = 59;
                    seconds = 59;
                } else {
                    stopTimer();
                    GUI.AlertTheUserThatTheTimerHasEnded(message); // Call alert when timer ends
                }
                updateLabel();
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        running = false;
    }

    public void reset() {
        stopTimer();
        hours = initialHours;  // Restore original values
        minutes = initialMinutes;
        seconds = initialSeconds;
        updateLabel();
    }

    private void updateLabel() {
        label.setText(String.format("%02d:%02d:%02d | %s", hours, minutes, seconds, message));
    }

    public void setLabel(JLabel newLabel) {
        this.label = newLabel;
        updateLabel();
    }

    // Getter methods
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }
    public int getSeconds() { return seconds; }
    public String getMessage() { return message; }
}
