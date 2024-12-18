package com.example.WordleApp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WordArrayAdapter extends RecyclerView.Adapter<WordArrayAdapter.WordViewHolder> {

    private final int wordLength;
    private final int numberOfRows;
    private final List<List<Button>> buttonGrid;  // 2D-lista napeista
    private final List<List<String>> buttonTexts;  // 2D-lista nappien teksteistä
    private final Context context;

    // Alustetaan luokan kentät konstruktorissa
    public WordArrayAdapter(Context context, int wordLength, int numberOfRows) {
        this.context = context;
        this.wordLength = wordLength;
        this.numberOfRows = numberOfRows;
        this.buttonGrid = new ArrayList<>(numberOfRows);
        this.buttonTexts = new ArrayList<>(numberOfRows);

        // Alustetaan nappien tekstit
        for (int i = 0; i < numberOfRows; i++) {
            List<Button> buttonRow = new ArrayList<>(wordLength);
            List<String> textRow = new ArrayList<>(wordLength);
            for (int j = 0; j < wordLength; j++) {
                buttonRow.add(null);  // Alustetaan null-napeilla
                textRow.add("");  // Alustetaan tyhjällä merkkijonolla
            }
            buttonGrid.add(buttonRow);
            buttonTexts.add(textRow);
        }
    }

    // Luodaan näkymä ja palautetaan WordViewHolder-luokan olio
    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    // Asetetaan napit ViewHolderiin
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.setButtons(wordLength, position, buttonTexts.get(position));
    }

    // Palautetaan rivien lukumäärä
    @Override
    public int getItemCount() {
        return numberOfRows;
    }

    // Palautetaan tietyn rivin napit
    public List<Button> getRowButtons(int row) {
        return buttonGrid.get(row);
    }

    // Päivitetään tietyn napin teksti ja päivitetään näkymä
    public void updateButton(int row, int col, String text) {
        buttonTexts.get(row).set(col, text);
        Button button = buttonGrid.get(row).get(col);
        if (button != null) {
            button.setText(text);
        }
        notifyItemChanged(row);
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout buttonContainer;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonContainer = itemView.findViewById(R.id.buttonContainer);
        }

        // Poistetaan kaikki vanhat napit ja lisätään uudet tilalle
        public void setButtons(int wordLength, int rowIndex, List<String> buttonText) {
            buttonContainer.removeAllViews();  // Vanhojen nappien poisto
            List<Button> rowButtons = new ArrayList<>(wordLength);

            // Luodaan tarvittava määrä nappeja yhdelle riville
            // ja lisätään ne 2D-listaan
            for (int i = 0; i < wordLength; i++) {
                Button button = new Button(context);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        dpToPx(50),
                        dpToPx(50)
                ));
                button.setText(buttonText.get(i));
                button.setTextSize(25);
                button.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.kirjaimet);
                button.setBackground(drawable);
                buttonContainer.addView(button);

                rowButtons.add(button);
            }
            buttonGrid.set(rowIndex, rowButtons);
        }

        // Apumetodi dp:n muuttamisessa pikseleiksi
        private int dpToPx(int dp) {
            float density = context.getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
}