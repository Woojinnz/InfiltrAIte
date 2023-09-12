package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Commander;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerClass;
import nz.ac.auckland.se206.TimerObserver;
import nz.ac.auckland.se206.controllers.SceneManager.AppUI;

/** Controller class for the room view. */
public class BookController extends Commander implements TimerObserver {

  @FXML private TextArea objective;
  @FXML private Text timer;

  @FXML private Rectangle book1;
  @FXML private Rectangle book2;
  @FXML private Rectangle book3;
  @FXML private Rectangle book4;
  @FXML private Rectangle book5;
  @FXML private ImageView actual1;
  @FXML private ImageView actual2;
  @FXML private ImageView actual3;
  @FXML private ImageView actual4;
  @FXML private ImageView actual5;
  @FXML private Button back;
  @FXML private Button goBack;
  @FXML private Label intel;
  @FXML private Text title1;
  @FXML private Text title2;
  @FXML private Text title3;
  @FXML private Text title4;
  @FXML private Text title5;

  @FXML private Text description1;
  @FXML private Text description2;
  @FXML private Text description3;
  @FXML private Text description4;
  @FXML private Text description5;

  @FXML private String currentBook;

  private List<Rectangle> bookButtons;

  private String bookID;
  private boolean goodBook = false;

  private ImageView book;
  private String bookContent;

  private Map<String, ImageView> bookMap = new HashMap<>();
  private Map<String, Boolean> content = new HashMap<>();
  private Map<String, Text> titleMap = new HashMap<>();
  private Map<String, Text> descriptionMap = new HashMap<>();
  private String goodTitle = "Where our intelligence is stored";
  private String goodDesc =
      "One of the intelligence can be found in the left room, make sure to use the sliders"
          + " properly!!\r\n"
          + "\r\n"
          + "% ^ $ & #";

  private Map<String, String> titleToDescription =
      new HashMap<>() {
        {
          put(
              "How to bake a Cake",
              "Simple Vanilla Cake Recipe\r\n"
                  + "\r\n"
                  + "Ingredients:\r\n"
                  + "\r\n"
                  + "1 cup sugar\r\n"
                  + "1/2 cup butter, softened\r\n"
                  + "2 eggs\r\n"
                  + "1 and 1/2 cups all-purpose flour\r\n"
                  + "1 and 3/4 tsp baking powder\r\n"
                  + "1/2 cup milk\r\n"
                  + "1 tsp vanilla extract\r\n"
                  + "Pinch of salt\r\n"
                  + "\r\n"
                  + "Instructions:\r\n"
                  + "Preheat the Oven: Set your oven to 350\u00B0F (175\u00B0C) and position a rack"
                  + " in the middle. Lightly grease a 9x9 inch baking pan or line it with parchment"
                  + " paper.\r\n");
          put(
              "The rain is never ending",
              "The small town of Riverview had seen rain before, but this was different."
                  + "\r\n"
                  + " Dark"
                  + " clouds loomed for weeks, turning days into an endless twilight."
                  + "\r\n"
                  + "Streets"
                  + " became rivers, and gardens, ponds."
                  + "\r\n"
                  + " People whispered about an ancient curse as"
                  + " they watched their umbrellas turn inside out."
                  + "\r\n"
                  + "Children played, imagining"
                  + " adventures on grand, floating cities. Mrs. Thompson, the oldest in town,"
                  + " smiled, remembering a similar storm from her youth."
                  + "\r\n"
                  + "\"It's nature's way of"
                  + " reminding us,\" she said, \"that sometimes, change needs time.\"");
          put(
              "Data Structures 5th Edition",
              "A heap is a specialized tree-based data structure that satisfies the heap property."
                  + " It can be visualized as a binary tree, and it finds its primary use in"
                  + " algorithms like heap sort and in data structures such as priority queues.\r\n"
                  + "\r\n"
                  + "There are two types of heaps based on the heap property:\r\n"
                  + "\r\n"
                  + "Max Heap \u2013 In a max heap, the largest element is found at the root.\r\n"
                  + "\r\n"
                  + "Min Heap \u2013 In a min heap, the smallest element is located at the root.");
          put(
              "10 Programming tips even your grandma knows",
              "1- Always Save Your Work\r\n"
                  + "\r\n"
                  + "2- Read the Error Messages\r\n"
                  + "\r\n"
                  + "3- Google is Your Friend\r\n"
                  + "\r\n"
                  + "4- Comments are For Humans\r\n"
                  + "\r\n"
                  + "5- Backup, Backup, Backup\r\n"
                  + "\r\n"
                  + "6- Keep Your Code Neat\r\n"
                  + "\r\n"
                  + "7- Start Simple\r\n"
                  + "\r\n"
                  + "8- Test Often\r\n"
                  + "\r\n"
                  + "9- Take Breaks\r\n"
                  + "\r\n"
                  + "10- Stay Updated");
        }
      };

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws Exception
   */
  public void initialize() throws Exception {
    intel.textProperty().bind(Bindings.concat("x", GameState.numOfIntel.asString()));

    // Initialise phone.
    super.initialize();
    objective.setText("Wonder which book can help me?");
    TimerClass.add(this);

    bookMap.put("book1", actual1);
    bookMap.put("book2", actual2);
    bookMap.put("book3", actual3);
    bookMap.put("book4", actual4);
    bookMap.put("book5", actual5);
    titleMap.put("book1", title1);
    titleMap.put("book2", title2);
    titleMap.put("book3", title3);
    titleMap.put("book4", title4);
    titleMap.put("book5", title5);
    descriptionMap.put("book1", description1);
    descriptionMap.put("book2", description2);
    descriptionMap.put("book3", description3);
    descriptionMap.put("book4", description4);
    descriptionMap.put("book5", description5);

    setupContent();
    bookButtons = Arrays.asList(book1, book2, book3, book4, book5);
  }

  private void setupContent() {
    // Select a random book to be the good book
    int randomNumber = new Random().nextInt(5) + 1;
    String randomBookId = "book" + randomNumber;
    content.put(randomBookId, true);

    List<String> availableBadTitles = new ArrayList<>(titleToDescription.keySet());

    List<String> allBooks =
        new ArrayList<>(Arrays.asList("book1", "book2", "book3", "book4", "book5"));
    allBooks.remove(randomBookId);

    Random random = new Random();
    for (String bookId : allBooks) {
      int randomIndex = random.nextInt(availableBadTitles.size());

      String selectedBadTitle = availableBadTitles.get(randomIndex);
      content.put(bookId, false);

      titleMap.get(bookId).setText(selectedBadTitle);
      descriptionMap.get(bookId).setText(titleToDescription.get(selectedBadTitle));

      availableBadTitles.remove(randomIndex);
    }
  }

  @Override
  public void timerStart() {
    TimerClass timerText = TimerClass.getInstance();
    timer.setText(timerText.getTimerLeft());
  }

  /** Handles the popup of books. */
  @FXML
  private void showBook() {
    back.setVisible(true);
    back.setDisable(false);
    book.setVisible(true);

    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(false);
    }

    if (content.get(bookID)) {
      titleMap.get(bookID).setStyle("-fx-font-weight: bold; -fx-font-size: 25px;");

      descriptionMap.get(bookID).setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

      titleMap.get(bookID).setText(goodTitle);
      descriptionMap.get(bookID).setText(goodDesc);

      titleMap.get(bookID).setVisible(true);
      descriptionMap.get(bookID).setVisible(true);
    } else {
      titleMap.get(bookID).setVisible(true);
      descriptionMap.get(bookID).setVisible(true);
    }
  }

  /**
   * Handles the return from popup of books.
   *
   * @param click
   */
  @FXML
  private void onGoBackShelf() {
    back.setVisible(false);
    back.setDisable(true);
    book.setVisible(false);

    for (Rectangle bookInShelf : bookButtons) {
      bookInShelf.setVisible(true);
    }
    for (int i = 1; i <= 5; i++) {
      titleMap.get("book" + i).setVisible(false);
      descriptionMap.get("book" + i).setVisible(false);
    }
  }

  /**
   * Handles the clicking of book type
   *
   * @param click the mouse event
   */
  @FXML
  public void checkBook(MouseEvent click) {

    Rectangle selectedBook = (Rectangle) click.getSource();
    bookID = selectedBook.getId();
    currentBook = bookID.toString();
    book = bookMap.get(bookID);

    showBook();
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
    currentScene.setRoot(SceneManager.getuserInterface(AppUI.RIGHT));
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Handles the hovering of rectangles
   *
   * @param event the mouse
   */
  @FXML
  public void onHover(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(1);
  }

  /**
   * Handles the un-hovering of rectangles
   *
   * @param event the mouse
   */
  @FXML
  public void onHoverExit(MouseEvent event) {
    Rectangle rectangle = (Rectangle) event.getSource();
    rectangle.setOpacity(0);
  }
}
