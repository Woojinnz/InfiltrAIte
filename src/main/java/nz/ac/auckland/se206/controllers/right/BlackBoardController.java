package nz.ac.auckland.se206.controllers.right;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class BlackBoardController extends Commander {

  private static int ans;
  public static BlackBoardController instance;

  public BlackBoardController() {
    instance = this;
  }

  public static BlackBoardController getInstance() {
    return instance;
  }

  /**
   * Returns the correct answer for keypad
   *
   * @return Correct answer for keypad
   */
  public static int getKeypadAns() {
    return ans;
  }

  @FXML private Label mon;
  @FXML private Label tues;
  @FXML private Label wed;
  @FXML private Label th;
  @FXML private Label fr;
  @FXML private Label sat;
  @FXML private Label sun;

  private int monday;
  private int tuesday;
  private int wednesday;
  private int thursday;
  private int friday;
  private int saturday;
  private int sunday;
  private int day;
  private LocalDate currentDate;

  private Map<Integer, Integer> hashmap = new HashMap<>();

  public static Map<Integer, Integer> randomizeNumbers() {
    Random random = new Random();

    Map<Integer, Integer> resultMap = new HashMap<>();

    resultMap.put(1, 100 + random.nextInt(900));
    resultMap.put(2, 100 + random.nextInt(900));
    resultMap.put(3, 100 + random.nextInt(900));
    resultMap.put(4, 100 + random.nextInt(900));
    resultMap.put(5, 100 + random.nextInt(900));
    resultMap.put(6, 100 + random.nextInt(900));
    resultMap.put(7, 100 + random.nextInt(900));

    return resultMap;
  }

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    Font.loadFont(getClass().getResourceAsStream("/fonts/Chalkduster.ttf"), 12);

    super.initialize();
    objective.setText("Hmm this seems pretty important");

    findDate();
  }

  private void findDate() {

    // Get the system date, then get the current day of the week.
    currentDate = LocalDate.now();
    day = currentDate.getDayOfWeek().getValue();

    hashmap = randomizeNumbers();

    monday = hashmap.get(1);
    tuesday = hashmap.get(2);
    wednesday = hashmap.get(3);
    thursday = hashmap.get(4);
    friday = hashmap.get(5);
    saturday = hashmap.get(6);
    sunday = hashmap.get(7);

    // match font with image font so it is less obvious to player.
    mon.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    tues.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    wed.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    th.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    fr.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    sat.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");
    sun.setStyle("-fx-font-family: 'Chalkduster'; -fx-font-size: 12px;");

    // Update the blackboard.
    mon.setText(Integer.toString(monday));
    tues.setText(Integer.toString(tuesday));
    wed.setText(Integer.toString(wednesday));
    th.setText(Integer.toString(thursday));
    fr.setText(Integer.toString(friday));
    sat.setText(Integer.toString(saturday));
    sun.setText(Integer.toString(sunday));

    ans = hashmap.get(day);
  }

  public void refreshBoard() {
    randomizeNumbers();
    findDate();
  }

  /**
   * Handles the return event
   *
   * @param event the mouse event
   */
  @FXML
  public void onGoBack(MouseEvent event) {
    Button rectangle = (Button) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the right room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
  }
}
