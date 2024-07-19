package com.example.harkkaty;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isFull;
    private int currentRow;
    private int currentButton;
    private int wordLength;
    private String wordToGuess;
    private String guessedLetters;
    private String wordDefinition;
    private static final String TAG = "softa";
    private Button enterButton;
    private Button delButton;
    private TextView definitionTextView;
    private WordArrayAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        isFull = false;
        guessedLetters = "";
        currentRow = 0;
        currentButton = 0;
        context = this;

        definitionTextView = findViewById(R.id.defView);
        definitionTextView.setVisibility(INVISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Haetaan arvattava sana ja sen määritelmä(t)
        WordUtils.getWord(context, new WordUtils.WordCallback() {
            @Override
            public void onWordReady(String word, String[] definitions) {
                wordToGuess = word;
                wordDefinition = word + ": \n" + definitions[0];
                if (definitions[1] != null){
                    wordDefinition += "\n" + definitions[1];
                }
                startGame();
            }

            // Virheen sattuessa käytetään oletusta
            @Override
            public void onWordError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                wordToGuess = "mango";
                wordDefinition = "A tropical Asian fruit tree, Mangifera indica.\n" +
                        "The fruit of the mango tree.";
                startGame();
            }
        });
    }

    // Sanan haun jälkeen voidaan luoda adapteri ja arvausruudukko
    // sekä rekisteröidä kirjaimet
    private void startGame(){
        registerButtons(true);
        wordLength = wordToGuess.length();
        adapter = new WordArrayAdapter(context, wordLength, 6);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: " + wordToGuess);
    }

    // Käydään kaikki näppäimet läpi ja lisätään painalluksen toiminnallisuus
    // Lisätään myös enter ja delete napit
    // Kirjainnäppäimiä painettaessa lisätään kirjain arvattuun sanaan ja arvausruudukkoon
    // Lisäksi pelin loppuessa voidaan kutsua uudestaan, jotta saadaan poistettua kaikki käytöstä
    private void registerButtons(Boolean enabled){
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            Button button = findViewById(getResources().getIdentifier("" + alphabet, "id", getPackageName()));
            button.setEnabled(enabled);
            button.setOnClickListener(view -> {
                if (!isFull) {
                    guessedLetters += button.getText().toString().toLowerCase();
                    onLetterGuessed(button.getText().toString());
                    if (guessedLetters.length() == wordToGuess.length()){
                        isFull = true;
                    }
                }
            });
        }

        // Enteriä painettaessa tarkistetaan onko arvatussa sanassa tarpeeksi kirjaimia
        // ja muodostavatko kirjaimet sanan
        enterButton = findViewById(R.id.enter);
        enterButton.setOnClickListener(view -> {
            if (isFull) {
                WordUtils.isWord(context, guessedLetters, new WordUtils.WordCallback() {

                    // Arvauksen ollessa sana voidaan kutsua arvausmetodia
                    @Override
                    public void onWordReady(String word, String[] definitions) {
                        guessWord();
                    }

                    // Muussa tapauksessa ilmoitetaan virheestä
                    @Override
                    public void onWordError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(context, "Not enough letters!", Toast.LENGTH_SHORT).show();
            }
        });

        // Poistonappia painettaessa vähennetään arvatuista kirjaimista viimeisin
        // sekä päivitetään arvausruudukko, mikäli kirjaimia on arvattu
        delButton = findViewById(R.id.del);
        delButton.setOnClickListener(view -> {
            if (currentButton > 0) {
                isFull = false;
                guessedLetters = guessedLetters.substring(0, guessedLetters.length() - 1);
                currentButton--;
                adapter.updateButton(currentRow, currentButton, "");
            }
        });
    }

    // Kirjaimen painalluksen jälkeen päivitetään arvausruudukko ja seuraavan kirjaimen paikka
    private void onLetterGuessed(String letter) {
        Log.d(TAG, "onLetterGuessed: " + letter);
        if (currentRow < 6) {
            adapter.updateButton(currentRow, currentButton, letter);
            currentButton++;
        }
    }

    // Käydään arvausruudukko läpi ja tarkistetaan onko sana edes osittain oikein
    // Lisäksi värjätään arvausruudukon rivi ja käytetyt näppäimet tulosten perusteella
    private void guessWord() {

        ArrayList<Integer> wipLetters = new ArrayList<>();
        StringBuilder correctLetters = new StringBuilder();
        StringBuilder foundLetters = new StringBuilder();
        List<Button> rowButtons = adapter.getRowButtons(currentRow);
        for (int i = 0; i < wordToGuess.length(); i++){

            Button button = rowButtons.get(i);
            char x = guessedLetters.charAt(i);
            Button letterButton = findViewById(getResources().getIdentifier("" + x, "id", getPackageName()));
            int color;

            // Mikäli kirjain on oikein, värjätään napit vihreäksi
            // Lisäksi pidetään kirjaa löytyneistä ja oikeista kirjaimista
            if (x == wordToGuess.charAt(i)){
                color = ContextCompat.getColor(context, R.color.correct);
                letterButton.setBackgroundColor(color);
                correctLetters.append(x);
                foundLetters.append(x);

                // Mikäli sanan alussa esiintyy kirjain väärässä paikassa
                // ja myöhemmin oikeassa, pitää ensimmäisen esiintymän väri korjata
                long countWord = wordToGuess.chars().filter(c -> c == x).count();
                long countGuessed = foundLetters.chars().filter(c -> c == x).count();

                if (countWord < countGuessed){
                    for (int j = 0; j < wipLetters.size(); j++){
                        int index = wipLetters.get(j);
                        if (guessedLetters.charAt(index) == x){
                            Button colorButton = rowButtons.get(index);
                            colorButton.setBackgroundColor(ContextCompat.getColor(context, R.color.used));
                            wipLetters.remove(j);
                        }
                    }
                }

              // Mikäli kirjain sisältyy sanaan, tarkistetaan kuinka monta kertaa
            } else if (wordToGuess.contains(String.valueOf(x))){
                long countWord = wordToGuess.chars().filter(c -> c == x).count();
                long countGuessed = foundLetters.chars().filter(c -> c == x).count();

                // Jos sanassa esiintyy jokin kirjain useammin kuin jo arvattu,
                // tulee kirjaimesta keltainen
                if (countWord > countGuessed){
                    foundLetters.append(x);
                    wipLetters.add(i);
                    color = ContextCompat.getColor(context, R.color.found);
                    letterButton.setBackgroundColor(color);


                  // Mikäli arvauksessa on useampi kirjain kuin sanassa,
                  // tulee kirjaimesta harmaa
                } else {
                    color = ContextCompat.getColor(context, R.color.used);

                }

              // Ja jos kirjain ei esiinny sanassa, tulee siitä harmaa
            } else {
                color = ContextCompat.getColor(context, R.color.used);
                letterButton.setBackgroundColor(color);

            }
            // Värjätään myös kirjain arvausruudukosta
            button.setBackgroundColor(color);
        }

        // Jos sana on oikein, lopetetaan peli
        if (correctLetters.toString().equals(wordToGuess)) {
            Toast.makeText(context, "You won!", Toast.LENGTH_SHORT).show();
            gameOver();

          // Muutoin siirrytään seuraavalle riville
        } else if (currentRow < 5) {
            currentButton = 0;
            isFull = false;
            currentRow++;
            guessedLetters = "";

          // Paitsi jos rivit loppuivat, jolloin peli on hävitty
        } else {
            Toast.makeText(context, "You lost, the word was " + wordToGuess, Toast.LENGTH_LONG).show();
            gameOver();
        }
    }

    // Pelin loputtua poistetaan näppäimet käytöstä
    // sekä paljastetaan sanan määritelmä
    private void gameOver(){
        enterButton.setEnabled(false);
        delButton.setEnabled(false);
        registerButtons(false);
        definitionTextView.setText(wordDefinition);
        definitionTextView.setVisibility(VISIBLE);
    }

}