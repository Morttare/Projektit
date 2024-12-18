package fi.tuni.prog3.wordle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class WordUtils {
    
    private final static HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    
    private static String responseString = "";
    private static HttpResponse<String> response;
    
    // Käyttöliittymä API-kutsujen tulosten tarkasteluun
    public interface WordCallback{
        void onWordReady(String word, String[] definitions);
        void onWordError(String message);
    }

    // Metodi arvattavan sanan hakuun
    // joka myös tarkistaa sanan löytymisen sanakirja-apista
    public static void getWord(WordCallback callback){

        // Määritetään haettavan sanan pituus väliltä 3-7 kirjainta
        Random random = new Random();
        int numberOfLetters = 3 + random.nextInt(5);

        // Haetaan random word api:sta kyseisen pituinen sana
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://random-word-api.herokuapp.com/word?length=" + numberOfLetters))
                .build();
        
        try{
            // Erotetaan vastauksesta haluttu sana
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseString = response.body();            
            String word = responseString.substring(2, responseString.length() - 2);

            // jos sanakirja-API tunnistaa sanan
            // voidaan peli aloittaa, muussa tapauksessa
            // haetaan uusi sana
            isWord(word, new WordCallback() {
                @Override
                public void onWordReady(String word, String[] definitions) {
                    callback.onWordReady(word, definitions);
                }

                @Override
                public void onWordError(String message) {
                    getWord(callback);
                }
            });
        }
        catch(Exception e){
            // Virheen sattuessa ilmoitetaan käyttäjälle
            callback.onWordError("Error fetching a word, using default");
        }  
    }


    // Metodi sanan oikeellisuuden tarkistamista varten
    public static void isWord(String word, WordCallback callback){

        // Haetaan sanakirja-apista kyseistä sanaa sekä sen määritelmää
        // ja välitetään tulos eteenpäin
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/" + word))
                .build();
        
        try{
            
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseString = response.body();
            
            // Jos sanaa ei löydy, ensimmäinen sana vastauksesta on title
            String start = responseString.substring(2, 7);
            if ("title".equals(start)){
                throw new Exception();
            }
            String [] definitions = getDefinition(responseString);
            callback.onWordReady(word, definitions);
        }
        
        catch(Exception e){
            // Virheen sattuessa ilmoitetaan käyttäjälle
            callback.onWordError("Not a word!");
        }                
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Jos jokin menee pieleen, palautetaan null
        return null;
    }
}
