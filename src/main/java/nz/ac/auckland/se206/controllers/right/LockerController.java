package nz.ac.auckland.se206.controllers.right;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.left.LeftRoomController;

/** Controller class for the room view. */
public class LockerController extends Commander {

  @FXML private Label first;
  @FXML private Label second;
  @FXML private Label third;
  @FXML private Label fourth;
  @FXML private ImageView intelFile;
  @FXML private ImageView roomimage;
  @FXML private Button checkAns;
  @FXML private Button goBack;

  private int one;
  private int two;
  private int three;
  private int four;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    super.initialize();
    one = two = three = four = 0;
    objective.setText("Whats the correct combination?");
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene currentScene = rectangle.getScene();
    // Update the scene to the main room
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  /**
   * Checks the answer
   *
   * @param event
   * @throws Exception
   */
  @FXML
  public void onCheckAns(MouseEvent event) throws Exception {

    if (!GameState.cabinetIntelfound) {
      int answer = LeftRoomController.year;
      int userAnswer = (one * 1000 + two * 100 + three * 10 + four);
      if (answer == userAnswer) {
        updateDialogue(Dialogue.CORRECTYEAR);
        intelFile.setDisable(false);
        intelFile.setVisible(true);
        roomimage.setVisible(false);
        checkAns.setVisible(false);
        goBack.setVisible(false);
        showIntel();

      } else {
        updateDialogue(Dialogue.WRONGYEAR);
      }
    }
  }

  private void showIntel() {
    intelFile.setOnMouseClicked(
        event -> {
          if (!GameState.cabinetIntelfound)
            GameState.numOfIntel.set(GameState.numOfIntel.get() + 1);
          Scene currentScene = input.getScene();
          GameState.cabinetIntelfound = true;
          currentScene.setRoot(SceneManager.getuserInterface(AppUi.RIGHT));
        });
  }

  /**
   * Handles the increase event.
   *
   * @param event the mouse event
   */
  @FXML
  public void increase(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    switch (rect.getId()) {
      case "upOne":
        if (one == 9) {
          one = 0;
        } else {
          one++;
        }
        first.setText(String.valueOf(one));
        break;

      case "upTwo":
        if (two == 9) {
          two = 0;
        } else {
          two++;
        }
        second.setText(String.valueOf(two));
        break;

      case "upThree":
        if (three == 9) {
          three = 0;
        } else {
          three++;
        }
        third.setText(String.valueOf(three));
        break;

      case "upFour":
        if (four == 9) {
          four = 0;
        } else {
          four++;
        }
        fourth.setText(String.valueOf(four));
        break;
      default:
        break;
    }
  }

  /**
   * Handles the decrease event.
   *
   * @param event the mouse event
   */
  @FXML
  public void decrease(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    switch (rect.getId()) {
      case "downOne":
        if (one == 0) {
          one = 9;
        } else {
          one--;
        }
        first.setText(String.valueOf(one));
        break;

      case "downTwo":
        if (two == 0) {
          two = 9;
        } else {
          two--;
        }
        second.setText(String.valueOf(two));
        break;

      case "downThree":
        if (three == 0) {
          three = 9;
        } else {
          three--;
        }
        third.setText(String.valueOf(three));
        break;

      case "downFour":
        if (four == 0) {
          four = 9;
        } else {
          four--;
        }
        fourth.setText(String.valueOf(four));
        break;
      default:
        break;
    }
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
