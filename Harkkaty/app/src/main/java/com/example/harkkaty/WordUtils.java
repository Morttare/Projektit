package com.example.harkkaty;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;

public class WordUtils {

    // Käyttöliittymä API-kutsujen tulosten tarkasteluun
    public interface WordCallback{
        void onWordReady(String word, String[] definitions);
        void onWordError(String message);
    }

    // Metodi arvattavan sanan hakuun
    // joka myös tarkistaa sanan löytymisen sanakirja-apista
    public static void getWord(Context context, WordCallback callback){

        // Määritetään haettavan sanan pituus väliltä 3-7 kirjainta
        Random random = new Random();
        int numberOfLetters = 3 + random.nextInt(5);

        // Haetaan random word api:sta kyseisen pituinen sana
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://random-word-api.herokuapp.com/word?length=" + numberOfLetters;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Haettu sana ilmoitetaan eteenpäin
                    String word = response.substring(2, response.length() - 2);
                    Log.d("softa", word);

                    // jos sanakirja-API tunnistaa sanan
                    // voidaan peli aloittaa, muussa tapauksessa
                    // haetaan uusi sana
                    isWord(context, word, new WordCallback() {
                        @Override
                        public void onWordReady(String word, String[] definitions) {
                            Log.d("softa", "Word is " + word);
                            callback.onWordReady(word, definitions);
                        }

                        @Override
                        public void onWordError(String message) {
                            Log.d("softa", "Haetaan uus sana");
                            getWord(context, callback);
                        }
                    });
                },
                error -> {
                    // Virheen sattuessa ilmoitetaan käyttäjälle
                    callback.onWordError("Error fetching a word, using default");
                });

        queue.add(request);
    }


    // Metodi sanan oikeellisuuden tarkistamista varten
    public static void isWord(Context context, String word, WordCallback callback){

        // Haetaan sanakirja-apista kyseistä sanaa sekä sen määritelmää
        // ja välitetään tulos eteenpäin
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    String[] definitions = getDefinition(response);
                    callback.onWordReady(word, definitions);
                },
                error -> {
                    // Virheen sattuessa ilmoitetaan käyttäjälle
                    callback.onWordError("Not a word!");
                });

        queue.add(request);
    }

    // Haetaan sanakirja-API:n JSON-vastauksesta
    // määritelmä(t) sanalle
    public static String[] getDefinition(String jsonString) {
        String[] twoDefinitions = new String[2];
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Jäsennetään JSON-merkkijono
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // Käydään sanalista läpi
            for (JsonNode wordNode : rootNode) {
                // Haetaan merkitys
                JsonNode meanings = wordNode.path("meanings");

                if (meanings.isArray()) {
                    for (JsonNode meaning : meanings) {
                        // Haetaan määritelmälista
                        JsonNode definitions = meaning.path("definitions");

                        if (definitions.isArray() && !definitions.isEmpty()) {
                            // Haetaan ensimmäinen määritelmä
                            twoDefinitions[0] = definitions.get(0).path("definition").asText();
                            if (definitions.size() > 1) {
                                // Haetaan toinen määritelmä jos sellainen löytyy
                                twoDefinitions[1] = definitions.get(1).path("definition").asText();
                            }
                            return twoDefinitions;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Jos jokin menee pieleen, palautetaan null
        return null;
    }
}
