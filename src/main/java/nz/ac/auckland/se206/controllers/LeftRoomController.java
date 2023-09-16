package nz.ac.auckland.se206.controllers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.Dialogue;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class LeftRoomController extends Commander implements TimerObserver {

  private enum Object {
    COMMS,
    DRAWER,
    PAINT,
    PAINT1,
    PAINT2,
    DOOR,
    DESK,
    NEWS,
    BOT,
    MID,
    TOP
  }

  public static int year;

  @FXML private TextArea objective;
  @FXML private ImageView room;
  @FXML private Rectangle popUpBackGround;
  @FXML private Button back, decrypt;
  @FXML
  private Rectangle communications, drawer, painting, painting1, topDrawer, midDrawer, botDrawer;
  @FXML private Polygon painting2, door, desk, newspaper;
  @FXML private ImageView poster, poster1, poster2, comms, comms1, tear, drawer1;
  @FXML private Pane sliderPane;
  @FXML private Slider slider, slider1, slider2, slider3, slider4, slider5;
  @FXML private Pane passcodePane, riddlePane, riddleDrawer;
  @FXML private Label label, label1, label2, label3, label4, label5;
  @FXML private Label lastDigits, intel;
  @FXML private ImageView paper;
  @FXML private TextArea riddle;
  @FXML private ImageView intelligence;
  @FXML private TextArea riddleBox;
  @FXML private Button check;

  /** The key in the inventory box. It is currently set to visible. */
  @FXML private ImageView key;

  private Map<Shape, Object> objects;
  private List<ImageView> visiblePopups;
  private List<Slider> sliders;
  private List<Label> passcode;
  private char[] code;
  private char[] answer = GameState.setSliders();
  private Map<Integer, Character> sliderMap;
  private int lastNumbers;
  private String riddleCode;
  private boolean isDialogueUpdated = false;
  private boolean isIntelCollected = false;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {

    System.out.println(GameState.getRandomWord());

    super.initialize();
    objective.setText("This is the LEFT ROOM");

    createRoom();
    setPopups();
    setHoverEvents();
    setSliders();
    createSliderMap();
    generateYear();

    TimerClass.add(this);
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  private void clickDoor() {
    Scene currentScene = door.getScene();
    currentScene.setRoot(SceneManager.getuserInterface(AppUi.MAIN));
  }

  private void createRoom() {
    objects = new HashMap<>();
    objects.put(communications, Object.COMMS);
    objects.put(drawer, Object.DRAWER);
    objects.put(painting, Object.PAINT);
    objects.put(painting1, Object.PAINT1);
    objects.put(painting2, Object.PAINT2);
    objects.put(door, Object.DOOR);
    objects.put(newspaper, Object.NEWS);
    objects.put(desk, Object.DESK);
    objects.put(topDrawer, Object.TOP);
    objects.put(midDrawer, Object.MID);
    objects.put(botDrawer, Object.BOT);
  }

  private void setHoverEvents() {
    setOpacityOnHover(door);
    setOpacityOnHover(painting);
    setOpacityOnHover(painting1);
    setOpacityOnHover(painting2);
    setOpacityOnHover(newspaper);
    setOpacityOnHover(drawer);
    setOpacityOnHover(communications);
    setOpacityOnHover(desk);
    setOpacityOnHover(topDrawer);
    setOpacityOnHover(midDrawer);
    setOpacityOnHover(botDrawer);
  }

  private void setOpacityOnHover(Shape shape) {
    shape.setOnMouseEntered(event -> shape.setOpacity(0.5));
    shape.setOnMouseExited(event -> shape.setOpacity(0));
  }

  private void showPopup(ImageView popup) {
    popup.setVisible(true);
    popUpBackGround.setVisible(true);
    visiblePopups.add(popup);
    back.setVisible(true);
  }

  private void toggleSliders(Boolean flag) {
    passcodePane.setVisible(flag);
    sliderPane.setVisible(flag);
  }

  private void toggleYear(Boolean flag) {
    this.lastDigits.setVisible(flag);
  }

  private void setPopups() {
    visiblePopups = new ArrayList<>();
    popUpBackGround.setVisible(false);
    back.setVisible(false);
    decrypt.setVisible(false);
    riddleCode = generateEncrypted(100);
    riddle.appendText(riddleCode);
    riddlePane.setVisible(false);
    riddle.setWrapText(true);
    intelligence.setVisible(false);
    drawer1.setVisible(false);
    openCabinet(false);
    riddleDrawer.setVisible(false);

    // Individual popup items.
    poster.setVisible(false);
    poster1.setVisible(false);
    poster2.setVisible(false);
    comms.setVisible(false);
    comms1.setVisible(false);
    tear.setVisible(false);
    lastDigits.setVisible(false);

    back.setOnAction(
        event -> {
          for (ImageView popup : visiblePopups) {
            popup.setVisible(false);
          }
          visiblePopups.clear();
          back.setVisible(false);
          popUpBackGround.setVisible(false);
          // Disables sliders & last digits.
          toggleSliders(false);
          toggleYear(false);
          openCabinet(false);
          riddlePane.setVisible(false);
          decrypt.setVisible(false);
          comms1.setVisible(false);
          intelligence.setVisible(false);
          riddleDrawer.setVisible(false);
        });

    decrypt.setOnAction(
        event -> {
          // send the encrypted message to GPT.
          System.out.println("TEST PRESS");
          String dialogue = Dialogue.FOUNDENCRYPTED.toString() + riddleCode;
          try {
            CommanderController.getInstance().onSendMessage(event, dialogue);

            // Create a Timeline to wait for 10 seconds
            Timeline timeline =
                new Timeline(
                    new KeyFrame(
                        Duration.millis(10000),
                        ae -> {
                          try {
                            CommanderController.getInstance()
                                .sendForUser(
                                    GptPromptEngineering.getRiddleWithGivenWord(
                                        GameState.getRandomWord()));
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                        }));

            // Play the Timeline
            timeline.play();

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    check.setOnAction(
        event -> {
          String attempt = riddleBox.getText();
          String message;

          // If the user inputs the correct answer, then unlock the drawer.
          if (attempt.toLowerCase().contains(GameState.riddleAnswer)) {
            riddleDrawer.setVisible(false);
            // Update game state.
            GameState.isRiddleResolved = true;
            message = Dialogue.DRAWERUNLOCK.toString();
          } else {
            message = Dialogue.INCORRECT.toString();
          }
          // Update the dialogue Correctly.
          try {
            CommanderController.getInstance().updateDialogueBox(message);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void generateYear() {

    Random random = new Random();
    lastNumbers = random.nextInt(41) + 20; // Generates number between 20 and 60

    // Update the year passcode: 19XX
    LeftRoomController.year = 1900 + lastNumbers;
    // Update the tear piece to show the last digits.
    lastDigits.setText(String.valueOf(lastNumbers));
  }

  private void setSliders() {
    toggleSliders(false);
    code = new char[6];
    sliders = List.of(slider, slider1, slider2, slider3, slider4, slider5);
    passcode = List.of(label, label1, label2, label3, label4, label5);

    IntStream.range(0, sliders.size()).forEach(i -> setSlider(sliders.get(i), passcode.get(i), i));
  }

  private void createSliderMap() {
    sliderMap = new HashMap<>();
    sliderMap.put(0, '!');
    sliderMap.put(1, '+');
    sliderMap.put(2, '-');
    sliderMap.put(3, '*');
    sliderMap.put(4, '&');
    sliderMap.put(5, '^');
    sliderMap.put(6, '%');
    sliderMap.put(7, '$');
    sliderMap.put(8, '#');
    sliderMap.put(9, '@');
    sliderMap.put(10, '?');
  }

  // Helper function for sliders.
  private void setSlider(Slider s, Label digit, int index) {
    s.setMajorTickUnit(1);
    s.setMinorTickCount(0);
    s.setBlockIncrement(1);
    s.setSnapToTicks(true);

    s.valueProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int intValue = newValue.intValue();
                s.setValue(intValue); // Update the slider value to the nearest integer

                // Get the respective character from slider map.
                char codeValue = sliderMap.get(intValue);

                // Update the actual code array.
                code[index] = codeValue;
                // Update respective label.
                digit.setText(String.valueOf(codeValue));

                try {
                  checkSlidersSolved();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
  }

  private void checkSlidersSolved() throws Exception {
    if (Arrays.equals(code, answer) && !isDialogueUpdated) {
      isDialogueUpdated = true;
      // Update game state and show sine wave.
      GameState.isSlidersSolved = true;
      comms1.setVisible(true);
      // Disable slider game.
      for (Slider slider : sliders) {
        slider.setDisable(true);
      }

      CommanderController.getInstance().updateDialogueBox(Dialogue.CABINETUNLOCK.toString());
    }
  }

  private void openCabinet(Boolean flag) {
    topDrawer.setVisible(flag);
    midDrawer.setVisible(flag);
    botDrawer.setVisible(flag);
  }

  @FXML
  public void onClick(MouseEvent event) throws Exception {

    Shape clickedObject = (Shape) event.getSource();
    Object type = objects.get(clickedObject);

    // Add more items that can be clicked on.
    switch (type) {
      case DOOR:
        clickDoor();
        break;
      case PAINT1:
        showPopup(poster1);
        break;
      case PAINT:
        showPopup(poster);
        break;
      case NEWS:
        showPopup(poster2);
        break;
      case COMMS:
        showPopup(comms);
        if (GameState.isSlidersSolved) {
          showPopup(comms1);
        }
        toggleSliders(true);
        break;
      case DESK:
        showPopup(tear);
        toggleYear(true);
        break;
      case DRAWER:
        showPopup(drawer1);
        openCabinet(true);
        break;
      case TOP:
        if (GameState.isSlidersSolved && !isIntelCollected) {
          showPopup(intelligence);
        } else if (!GameState.isSlidersSolved) {
          CommanderController.getInstance().updateDialogueBox(Dialogue.CABINETLOCK.toString());
        } else {
          CommanderController.getInstance().updateDialogueBox(Dialogue.EMPTY.toString());
        }
        break;
      case MID:
        riddlePane.setVisible(true);
        decrypt.setVisible(true);
        break;
      case BOT:
        if (GameState.isRiddleResolved) {
          GameState.isKeyFound.set(true);
          CommanderController.getInstance().updateDialogueBox(Dialogue.KEYFOUND.toString());
        } else {
          riddleDrawer.setVisible(true);
        }
        break;
      default:
        break;
    }
  }

  @FXML
  public void onCollect(MouseEvent event) {
    // return back to main room.
    openCabinet(false);
    drawer1.setVisible(false);
    back.setVisible(false);
    popUpBackGround.setVisible(false);
    intelligence.setVisible(false);

    // Update text rollout.
    try {
      CommanderController.getInstance().updateDialogueBox(Dialogue.INTELFOUND.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Update game states.
    isIntelCollected = true;
    GameState.numOfIntel.set(GameState.numOfIntel.get() + 1);
  }

  private String generateEncrypted(int length) {

    String asciiChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    SecureRandom random = new SecureRandom();
    StringBuilder encryptedText = new StringBuilder();
    int count = 0;
    for (int i = 0; i < length; i++) {
      if (count > 0 && random.nextInt(10) < 2) { // 20% chance to insert a space
        encryptedText.append(' ');
        count = 0;
        continue;
      }
      int index = random.nextInt(asciiChars.length());
      encryptedText.append(asciiChars.charAt(index));
      count++;
    }
    return encryptedText.toString();
  }
}
