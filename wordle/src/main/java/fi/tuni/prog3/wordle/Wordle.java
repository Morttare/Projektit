package fi.tuni.prog3.wordle;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX App based on the popular game Wordle
 */
public class Wordle extends Application {

    private boolean isFull;
    private boolean isOver;
    private int currentRow;
    private int currentButton;
    private int wordLength;
    private String wordToGuess;
    private String guessedLetters;
    private String wordDefinition;
    Text infoBox;
    Text definitionText;
    private List<List<Button>> buttonGrid;    
    private Map<Integer,Integer> sizes;
    private Scene scene;

    @Override
    public void start(Stage stage) {

        isFull = false;
        isOver = false;
        guessedLetters = "";
        currentRow = 0;
        currentButton = 0;
        buttonGrid = new ArrayList<>();        
        sizes = new HashMap<>();

        // Haetaan arvattava sana
        WordUtils.getWord(new WordUtils.WordCallback() {
            @Override
            public void onWordReady(String word, String[] definitions) {
                wordToGuess = word;
                wordDefinition = word + ": \n" + definitions[0];
                if (definitions[1] != null) {
                    wordDefinition += "\n" + definitions[1];
                }
            }

            // Virheen sattuessa käytetään oletusta
            @Override
            public void onWordError(String message) {
                wordToGuess = "mango";
                wordDefinition = "A tropical Asian fruit tree, Mangifera indica.\n"
                        + "The fruit of the mango tree.";
            }

        });

        fillSizes();
        wordLength = wordToGuess.length();
        int width = sizes.get(wordLength);
        GridPane grid = new GridPane();
        scene = new Scene(grid, width, 615);
        registerButtons(scene);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(5);
        stage.setScene(scene);
        stage.setTitle("Wordle");

        Button startNew = new Button("Start new game");
        startNew.setId("newGameBtn");
        startNew.setOnAction((event) -> {
            start(stage);
        });
        grid.add(startNew, 0, 0, 5, 1);

        definitionText = new Text("");
        infoBox = new Text("");
        definitionText.setText(wordDefinition);
        definitionText.setWrappingWidth(width-40);
        definitionText.setVisible(false);
        infoBox.setText("");
        grid.add(infoBox, 0, 1, 2, 2);
        grid.add(definitionText, 0, 9, 3, 3);

        setUpWords(grid);        

        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    // Testaamalla etsitty sopiva ikkunan leveys sanan pituuteen suhteutettuna
    private void fillSizes(){
        sizes.put(3, 200);
        sizes.put(4, 260);
        sizes.put(5, 320);
        sizes.put(6, 375);
        sizes.put(7, 430);        
    }
    
    // Luodaan arvausruudukko sanalle    
    private void setUpWords(GridPane grid) {        
        for (int i = 0; i < 6; i++) {
            List<Button> buttonRow = new ArrayList<>(wordLength);            

            for (int j = 0; j < wordLength; j++) {
                Button cell = new Button();
                cell.setDisable(true);
                cell.setText("");
                cell.setPrefSize(50,50);     
                cell.setMinSize(50,50);
                grid.add(cell, j+2, i+3, 1, 1);
                buttonRow.add(cell);                
            }
            buttonGrid.add(buttonRow);          
        }
    }

    // Päivitetään tietyn napin teksti
    private void updateButton(int row, int col, String text) {       
        Button button = buttonGrid.get(row).get(col);
        if (button != null) {
            button.setText(text);
        }
    }

    // Palautetaan tietyn rivin napit
    public List<Button> getRowButtons(int row) {
        return buttonGrid.get(row);
    }

    // Lisätään kuuntelija näppäinten painalluksille   
    // Kirjainnäppäimiä painettaessa lisätään kirjain arvattuun sanaan ja arvausruudukkoon    
    private void registerButtons(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (!isOver) {
                // Enteriä painettaessa tarkistetaan onko arvatussa sanassa             
                // tarpeeksi kirjaimia ja muodostavatko kirjaimet sanan
                if (key.getCode() == KeyCode.ENTER) {
                    if (isFull) {
                        WordUtils.isWord(guessedLetters, new WordUtils.WordCallback() {

                            // Arvauksen ollessa sana voidaan kutsua arvausmetodia
                            @Override
                            public void onWordReady(String word, String[] definitions) {
                                infoBox.setText("");
                                guessWord();
                            }

                            // Muussa tapauksessa ilmoitetaan virheestä
                            @Override
                            public void onWordError(String message) {
                                infoBox.setText(message);
                            }
                        });
                    } else {
                        infoBox.setText("Not enough letters!");
                    }
                } else if (key.getCode() == KeyCode.BACK_SPACE) {

                    // Poistonappia painettaessa vähennetään arvatuista kirjaimista viimeisin
                    // sekä päivitetään arvausruudukko, mikäli kirjaimia on arvattu
                    if (currentButton > 0) {
                        isFull = false;
                        guessedLetters = guessedLetters.substring(0, guessedLetters.length() - 1);
                        currentButton--;
                        updateButton(currentRow, currentButton, "");
                        infoBox.setText("");
                    }
                } else {
                    // Muussa tapauksessa lisätään kirjain arvattuun sanaan mikäli tilaa on
                    if (!isFull) {
                        String text = key.getText();
                        if (text.matches("[a-z]")){
                            guessedLetters += text;
                            onLetterGuessed(text.toUpperCase());
                            if (guessedLetters.length() == wordToGuess.length()) {
                                isFull = true;
                            }
                        }                        
                    }
                }
            }
        });
    }

    // Kirjaimen painalluksen jälkeen päivitetään arvausruudukko ja seuraavan kirjaimen paikka
    private void onLetterGuessed(String letter) {
        if (currentRow < 6) {
            updateButton(currentRow, currentButton, letter);
            currentButton++;
            scene.getRoot().requestFocus();
        }
    }

    // Käydään arvausruudukko läpi ja tarkistetaan onko sana edes osittain oikein
    // Lisäksi värjätään arvausruudukon rivi ja käytetyt näppäimet tulosten perusteella
    private void guessWord() {

        ArrayList<Integer> wipLetters = new ArrayList<>();
        StringBuilder correctLetters = new StringBuilder();
        StringBuilder foundLetters = new StringBuilder();
        List<Button> rowButtons = getRowButtons(currentRow);
        for (int i = 0; i < wordToGuess.length(); i++) {

            Button button = rowButtons.get(i);
            char x = guessedLetters.charAt(i);
            String color;

            // Mikäli kirjain on oikein, värjätään napit vihreäksi
            // Lisäksi pidetään kirjaa löytyneistä ja oikeista kirjaimista
            if (x == wordToGuess.charAt(i)) {
                color = "-fx-background-color: Green";
                correctLetters.append(x);
                foundLetters.append(x);

                // Mikäli sanan alussa esiintyy kirjain väärässä paikassa
                // ja myöhemmin oikeassa, pitää ensimmäisen esiintymän väri korjata
                long countWord = wordToGuess.chars().filter(c -> c == x).count();
                long countGuessed = foundLetters.chars().filter(c -> c == x).count();

                if (countWord < countGuessed) {
                    for (int j = 0; j < wipLetters.size(); j++) {
                        int index = wipLetters.get(j);
                        if (guessedLetters.charAt(index) == x) {
                            Button colorButton = rowButtons.get(index);
                            colorButton.setStyle("-fx-background-color: Gray");
                            wipLetters.remove(j);
                        }
                    }
                }

                // Mikäli kirjain sisältyy sanaan, tarkistetaan kuinka monta kertaa
            } else if (wordToGuess.contains(String.valueOf(x))) {
                long countWord = wordToGuess.chars().filter(c -> c == x).count();
                long countGuessed = foundLetters.chars().filter(c -> c == x).count();

                // Jos sanassa esiintyy jokin kirjain useammin kuin jo arvattu,
                // tulee kirjaimesta keltainen
                if (countWord > countGuessed) {
                    foundLetters.append(x);
                    wipLetters.add(i);
                    color = "-fx-background-color: Yellow";

                    // Mikäli arvauksessa on useampi kirjain kuin sanassa,
                    // tulee kirjaimesta harmaa
                } else {
                    color = "-fx-background-color: Gray";

                }

                // Ja jos kirjain ei esiinny sanassa, tulee siitä harmaa
            } else {
                color = "-fx-background-color: Gray";

            }
            // Värjätään kirjain arvausruudukosta
            button.setStyle(color);
        }

        // Jos sana on oikein, lopetetaan peli
        if (correctLetters.toString().equals(wordToGuess)) {
            infoBox.setText("You won!");
            gameOver();

            // Muutoin siirrytään seuraavalle riville
        } else if (currentRow < 5) {
            currentButton = 0;
            isFull = false;
            currentRow++;
            guessedLetters = "";

            // Paitsi jos rivit loppuivat, jolloin peli on hävitty
        } else {
            infoBox.setText("You lost, the word was " + wordToGuess);
            gameOver();
        }
    }

    // Pelin loputtua poistetaan näppäimet käytöstä
    // sekä paljastetaan sanan määritelmä
    private void gameOver() {
        definitionText.setVisible(true);
        isOver = true;
    }
}
