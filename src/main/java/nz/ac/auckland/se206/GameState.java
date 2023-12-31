package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.controllers.CommanderController;
import nz.ac.auckland.se206.controllers.left.RadioController;
import nz.ac.auckland.se206.controllers.main.ComputerController;
import nz.ac.auckland.se206.controllers.main.MainRoomController;
import nz.ac.auckland.se206.controllers.right.BlackBoardController;
import nz.ac.auckland.se206.controllers.right.BookController;
import nz.ac.auckland.se206.controllers.right.LockerController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Represents the state of the game. */
public class GameState {

  private static final Set<String> riddleSet = new HashSet<>();

  /** Indicates the answer to slider puzzle for current game. */
  public static char[] sliderAnswer;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key to the cabinet has been found. */
  public static BooleanProperty isKeyFound = new SimpleBooleanProperty(false);

  /** Indicates whether the key to the cabinet has been used. */
  public static BooleanProperty isKeyUsed = new SimpleBooleanProperty(false);

  /** Indicates whether the keypad has had the correct digits input. */
  public static BooleanProperty isKeypadSolved = new SimpleBooleanProperty(false);

  /** Indicates whether game has been won or not. */
  public static BooleanProperty isGameWon = new SimpleBooleanProperty(false);

  /** Indicates the difficulty level of the game, 1 for EASY, 2 for MEDIUM and 3 for HARD. */
  public static IntegerProperty difficulty = new SimpleIntegerProperty();

  /** Indicates amount of intelligence gathered. */
  public static SimpleIntegerProperty numOfIntel = new SimpleIntegerProperty(0);

  /** Indeicates the number of hints allowed. */
  public static SimpleIntegerProperty numHints = new SimpleIntegerProperty(0);

  /** Indicates whether the player has found intel in the right room cabinet. */
  public static boolean cabinetRightIntelfound = false;

  /** Indicates whether the player has found intel in the middle room cabinet. */
  public static boolean cabinetMiddleIntelfound = false;

  /** Indicates whether the player has solved communication puzzle. */
  public static boolean isSlidersSolved = false;

  /** Indicates whether player has pressed the mute button to mute TTS. */
  public static BooleanProperty isMuted = new SimpleBooleanProperty(false);

  /**
   * Indicates whether the player has solved the password to the computer, giving them access to the
   * keypad.
   */
  public static boolean isPasswordSolved = false;

  /** Indicates the answer to the left room riddle for the current game. */
  public static String puzzleWord = "";

  /** Indicates the answer to the computer riddle for the current game. */
  public static String mainRiddleAnswer = "";

  /** Indicates the last numbers of the year for the current game. */
  public static SimpleIntegerProperty lastNumbers = new SimpleIntegerProperty(0);

  /** Indicates if player is on end Screen. */
  public static BooleanProperty isEndScreen = new SimpleBooleanProperty(false);

  private static Random random = new Random();

  // Create riddle answers for drawer.
  static {

    // // Create riddle set for computer.
    riddleSet.add("book");
    riddleSet.add("locker");
    riddleSet.add("blackboard");
    riddleSet.add("key");
    // Add more words.

    GameState.mainRiddleAnswer = getRandomWord(riddleSet);
    System.out.println(GameState.mainRiddleAnswer);

    // setUp listeners to check if game is won or not.
    setupWinListeners();
    // Generate random year for left room puzzle.
    generateYear();
    sliderAnswer = setSliders();
  }

  /** Method to create random riddle for current game. */
  public static String getRandomWord(Set<String> set) {
    // Create an ArrayList to hold the keys (words)
    ArrayList<String> keys = new ArrayList<>(set);

    // Get a random index from 0 to size-1
    int randomIndex = random.nextInt(keys.size());

    // Update the answer to the riddle for current game, and return.
    String riddle = keys.get(randomIndex);
    return riddle;
  }

  /** Method to create random slider combination for the current game. */
  public static char[] setSliders() {
    // Create an array of chars to hold the slider answer
    char[] answer = new char[6];
    Random random = new Random();
    // Possible char[] list of answer;
    char[] symbols = {'+', '-', '*', '^', '%', '$', '#', '@', '?'};

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(symbols.length);
      answer[i] = symbols[index];
    }
    sliderAnswer = answer;
    System.out.println(sliderAnswer);
    return sliderAnswer;
  }

  /**
   * Reset all states in all rooms when user has replayed.
   *
   * @throws Exception if the reset fails
   */
  public static void resetGame() throws Exception {
    resetGameState();
    resetTimer();
    resetCommander();
    resetMainRoom();
    resetLeftRoom();
    resetRightRoom();
  }

  /** Add listeners to isKeyPadSolved and numOfIntel to update isGameWon. */
  private static void setupWinListeners() {
    // Listen for changes to isKeyPadSolved
    isKeypadSolved.addListener((observable, oldValue, newValue) -> checkIsGameWon());

    // Listen for changes to numOfIntel
    numOfIntel.addListener((observable, oldValue, newValue) -> checkIsGameWon());
  }

  // Method to update if game has been won
  private static void checkIsGameWon() {
    isGameWon.set(isKeypadSolved.get() && numOfIntel.get() >= 1);
  }

  // Method to generate a new Year number for the current game.
  private static void generateYear() {
    lastNumbers.set(random.nextInt(41) + 20); // Generates number between 20 and 60
  }

  /** Reset all game state variables to default values for when the player restarts the game. */
  private static void resetGameState() {
    // Reset all game state variables to default values for when the player restarts the game.

    isRiddleResolved = false;
    isKeyFound.set(false);
    isKeyUsed.set(false);
    isKeypadSolved.set(false);
    isGameWon.set(false);

    difficulty.set(0);
    numOfIntel.set(0);
    numHints.set(0);

    cabinetRightIntelfound = false;
    cabinetMiddleIntelfound = false;
    isSlidersSolved = false;
    isPasswordSolved = false;

    mainRiddleAnswer = getRandomWord(riddleSet);
    sliderAnswer = setSliders();
    lastNumbers.set(random.nextInt(41) + 20);
    isEndScreen.set(false);
  }

  /** Reset timer when user has pressed replay. */
  private static void resetTimer() {
    // get the list of timers before resetting.
    List<Text> timers = TimerClass.getTimers();
    // Reset the timer.
    TimerClass.resetInstance();
    // Create a new Timer.
    TimerClass.initialize();
    // Update new Timer with room labels.
    TimerClass.setTimers(timers);
  }

  /**
   * Reset commander and all states related.
   *
   * @throws Exception if the reset fails
   */
  private static void resetCommander() throws Exception {

    CommanderController instance = CommanderController.getInstance();

    // Clear the notes and phones of previous game.
    instance.clearNotes();
    instance.clearPhones();
    instance.clearInput();

    List<TextArea> dialogues = instance.getDialogues();
    List<TextArea> inputAreas = instance.getInputAreas();
    List<ListView<ChatMessage>> phoneScreens = instance.getPhoneScreens();

    // Update the new Commander controller with the list of dialogues and phonescreens.
    CommanderController.getInstance().setDialogues(dialogues);
    CommanderController.getInstance().setPhoneScreens(phoneScreens);
    CommanderController.getInstance().setInputAreas(inputAreas);
    CommanderController.getInstance().displayStartHint();
  }

  /**
   * Reset main room and all states related.
   *
   * @throws ApiProxyException if the reset fails
   */
  private static void resetMainRoom() throws ApiProxyException {
    // change computer riddle for next user playthrough
    ComputerController.getInstance()
        .runGpt(
            new ChatMessage(
                "system", GptPromptEngineering.getPasswordRiddle(GameState.mainRiddleAnswer)));

    // Reset main room back to original imageview
    MainRoomController.getInstance().resetRoom();
  }

  /** Reset left room and all states related. */
  private static void resetLeftRoom() {
    RadioController.getInstance().setSliders();
  }

  /** Reset right room and all states related. */
  private static void resetRightRoom() {
    // Reset BlackBoard Numbers
    BlackBoardController.getInstance().refreshBoard();

    // Reset Book order in bookshelf
    BookController.getInstance().resetFont();
    BookController.getInstance().setupContent();

    // Reset Right Room Locker
    LockerController.getInstance().resetRoom();
  }
}
