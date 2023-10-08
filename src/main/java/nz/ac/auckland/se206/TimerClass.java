package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class TimerClass {

  private static volatile TimerClass instance;
  private static List<Text> timers = new ArrayList<>();

  public static List<Text> getTimers() {
    return timers;
  }

  public static void setTimers(List<Text> timers) {
    TimerClass.timers = timers;
  }

  public static void resetInstance() {
    instance = null;
    System.gc();
  }

  public static void add(Text timer) {
    timers.add(timer);
  }

  public static void initialize() {
    if (instance == null) {
      instance = new TimerClass();
    } else {
      System.out.println("Timer Exists");
    }
  }

  public static TimerClass getInstance() {
    if (instance == null) {
      System.out.println("NO TIMER EXISTS");
    }
    return instance;
  }

  private int timeLeft;
  private Timeline timeline;

  private TimerClass() {}

  public void start(int minutes) {
    // Set the correct amount of time for timer and start counting
    this.timeLeft = minutes * 60;
    for (Text timer : timers) {
      timer.setText(String.format("0%d:00", minutes));
    }

    this.timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    // Create a keyframe to decrease the timer every second.
    KeyFrame frame =
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              try {
                decreaseTimer();
              } catch (Exception e1) {

                e1.printStackTrace();
              }
            });
    timeline.getKeyFrames().add(frame);
    timeline.play();
  }

  public void stop() {
    for (Text timer : timers) {
      timer.setText("00:00");
    }
    timeline.stop();
  }

  private void decreaseTimer() throws Exception {
    timeLeft--;
    for (Text timer : timers) {
      String time = getTimeLeft();
      timer.setText(String.valueOf(time));
    }
    chatToPlayer(timeLeft);
    warn();
    checkLost();
  }

  public String getTimeLeft() {
    int minutes = timeLeft / 60;
    int seconds = timeLeft % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  // Method to add each rooms timer label to the timer.
  public void addTimer(Text timer) {
    timers.add(timer);
  }

  // Method to check if the game has been lost or not.
  private void checkLost() {
    if (timeLeft == 0) {
      timeline.stop();
      // Switch to lose screen.
      Scene currentScene = SceneManager.getCurrentSceneRoot().getScene();
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.END));
    }
  }

  // Method to warn player than time is running out.
  public void warn() {
    if (timeLeft == 30) {
      Task<Void> warn =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              try {
                // update commander dialogue to let player know they have been detected
                CommanderController.getInstance()
                    .updateDialogueBox(Dialogue.INTRUDERDETECED.toString());
                Sound.getInstance().playTTS();
              } catch (Exception e) {
                e.printStackTrace();
              }
              return null;
            }
          };
      // This will schedule the task to be run on a background thread
      new Thread(warn).start();
    }
  }

  // Method to create AI presence
  public void chatToPlayer(int timeLeft) throws Exception {

    String hint = null;

    // Determine the game state to get hints
    if (!GameState.isPasswordSolved && timeLeft == 60) {
      // Prompt the player abount solving the computer.
      hint = GptPromptEngineering.getComputerHint();
    } else if (GameState.isPasswordSolved && timeLeft == 60) {
      hint = GptPromptEngineering.getEscapeHint();
    } else if (GameState.isKeyFound.get()
        && !GameState.isKeyUsed.get()
        && timeLeft == 80) { // Give hint about the key if they have it.
      hint = GptPromptEngineering.getKeyHint();
    } else if (timeLeft == 80) { // Give player hint to focus on intel.
      hint = GptPromptEngineering.getIntelHint();
    }

    // Only Update GPT at certain times.
    if (timeLeft == 60 || timeLeft == 80) {
      CommanderController.getInstance().sendForUser(hint);
    }
  }
}
