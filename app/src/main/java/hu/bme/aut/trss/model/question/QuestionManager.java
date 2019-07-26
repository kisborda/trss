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

/**
 * A kérdéseket tartja számon
 */
public class QuestionManager {
    private static List<Question> easyQuestions = new Vector<>();
    private static List<Question> difficultQuestions = new Vector<>();
    private static List<Question> usedEasyQuestions = new Vector<>();
    private static List<Question> usedDifficultQuestions = new Vector<>();

    /**
     * A következő kérdést választja ki a kapott mező alapján.
     * Szerencse mező esetén nehezebb kérdés, egyébként könnyebb.
     *
     * @param resId Ehhez a mező típushoz tartozik a kérdés
     * @return Random sorsolt kérdés
     */
    public static Question getOneQuestion(int resId) {
        if (!isLoaded()) {
            return null;
        }

        Random rand = new Random();

        if (resId == R.drawable.greenish) {
            if (difficultQuestions.isEmpty()) {
                difficultQuestions = usedDifficultQuestions;
                usedDifficultQuestions.clear();
            }

            int idx = rand.nextInt(difficultQuestions.size());

            usedDifficultQuestions.add(difficultQuestions.remove(idx));
            return usedDifficultQuestions.get(usedDifficultQuestions.size() - 1);
        } else {
            if (easyQuestions.isEmpty()) {
                easyQuestions = usedEasyQuestions;
                usedEasyQuestions.clear();
            }

            int idx = rand.nextInt(easyQuestions.size());

            usedEasyQuestions.add(easyQuestions.remove(idx));
            return usedEasyQuestions.get(usedEasyQuestions.size() - 1);
        }
    }

    /**
     * Betölti fájlból a kérdéseket
     *
     * @param resources Erőforrások eléréséhez szükséges
     */
    public static void loadQuestions(Resources resources) {
        if (!QuestionManager.isLoaded() && resources != null) {

            InputStream inStream = resources.openRawResource(R.raw.loim);
            InputStreamReader inputStreamReader = new InputStreamReader(inStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    Question question = new Question(line.split(resources.getString(R.string.separator_in_file)));
                    if (question.getDifficulty() < 10) {
                        easyQuestions.add(question);
                    } else {
                        difficultQuestions.add(question);
                    }
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
        return !easyQuestions.isEmpty() | !usedEasyQuestions.isEmpty() | !difficultQuestions.isEmpty() | !usedDifficultQuestions.isEmpty();
    }
}
