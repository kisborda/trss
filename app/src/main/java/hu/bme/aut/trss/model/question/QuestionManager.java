package hu.bme.aut.trss.model.question;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import hu.bme.aut.trss.R;

/* TODO QuestionManager osztályt felülvizsgálni:
                használt/nem használt kérdésekkel kezdeni valamit, jelenleg hibaforrás
                isLoaded függvény: mikor kell fájlból olvasni?
*/

/**
 * A kérdéseket tartja számon
 */
public class QuestionManager {
    private static List<Question> questions = new Vector<>();
    private static List<Question> usedQuestions = new Vector<>();

    /**
     * A következő kérdést választja ki
     *
     * @return Random sorsolt kérdés
     */
    public static Question getOneQuestion() {
        // azt is le kell kezelni ha az összes kérdés fel lett használva
        // mivan ha üres a lista?

        Random rand = new Random();
        int idx = rand.nextInt(questions.size());

        usedQuestions.add(questions.remove(idx));
        return usedQuestions.get(usedQuestions.size() - 1);
    }

    public static void loadQuestions(Resources resources) {
        if (!QuestionManager.isLoaded() && resources != null) {

            InputStream inStream = resources.openRawResource(R.raw.loim);
            InputStreamReader inputStreamReader = new InputStreamReader(inStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    questions.add(new Question(line.split(resources.getString(R.string.separator_in_file))));
                }
            } catch (IOException e) {
                Log.i(resources.getString(R.string.log_tag), e.toString());
            }
        }
    }

    /**
     * Vizsgálja, hogy vannak-e már kérdések betöltve fájlból
     *
     * @return true ha legalább az egyik listában vannak elemek, egyébként false
     */
    private static Boolean isLoaded() {
        return !questions.isEmpty() | !usedQuestions.isEmpty();
    }
}
