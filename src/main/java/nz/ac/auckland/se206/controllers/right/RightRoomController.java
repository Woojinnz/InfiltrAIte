package nz.ac.auckland.se206.controllers.right;

import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Sound;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Controller class for the room view. */
public class RightRoomController extends Commander {

  /** Enum for the answer to puzzles. */
  public enum NumberGroup {
    ANS1,
    ANS2,
    ANS3,
  }

  static NumberGroup answer;

  @FXML private Polygon riddle;
  @FXML private Polygon blackboard;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception if there is an error loading the chat view
   */
  public void initialize() throws Exception {

    // Initialise phone.
    super.initialize();
    objective.setText("You must find the clue!!");

    final NumberGroup[] answerGroup = NumberGroup.values();
    // Randomnly select a number group
    answer = answerGroup[new Random().nextInt(answerGroup.length)];

    setHoverEvents();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println(BlackBoardController.getKeypadAns());
    Sound.getInstance().playClickMinor();
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  /**
   * Handles the click event on the locker and either gives a dialogue response or swap scenes.
   *
   * @param event the mouse event
   * @throws Exception if there is an error loading the chat view
   */
  @FXML
  public void clickRiddle(MouseEvent event) throws Exception {
    Sound.getInstance().playClickMinor();
    if (GameState.cabinetRightIntelfound) {
      updateDialogue(Dialogue.ALREADYGOTLOCKER);
    } else {
      Polygon rectangle = (Polygon) event.getSource();
      Scene currentScene = rectangle.getScene();
      // Update the scene to the main room
      currentScene.setRoot(SceneManager.getuserInterface(AppUi.LOCKER));
    }
  }

  /**
   * Shows the blackboard this is connected to the answer for the keypad.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBlackBoard(MouseEvent event) throws IOException {
    Sound.getInstance().playClickMinor();
    Polygon poly = (Polygon) event.getSource();
    Scene currentScene = poly.getScene();
    // Update the scene to the blackboard
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.BLACKBOARD));
  }

  /**
   * Handles the hovering of rectangles.
   *
   * @param event the mouse
   */
  @FXML
  public void onHover(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(1);
  }

  /**
   * Handles the un-hovering of rectangles.
   *
   * @param event the mouse
   */
  @FXML
  public void onHoverExit(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(0);
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBook(MouseEvent event) {
    Sound.getInstance().playClickMinor();
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main game.
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.BOOKSHELF));
  }

  /** Handles hover events on objects in the room. */
  private void setHoverEvents() {
    setOpacityOnHover(riddle);
    setOpacityOnHover(blackboard);
  }

  /** When object in the room is hovered by user, opacity will increase to 0.5. */
  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }
}
