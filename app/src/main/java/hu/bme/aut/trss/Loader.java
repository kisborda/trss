package hu.bme.aut.trss;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import hu.bme.aut.trss.data.question.Question;
import hu.bme.aut.trss.data.question.QuestionManager;

public class Loader {
    /**
     * Kérdéseket olvassa be fájlból és betölti a QuestionManager-be.
     *
     * @param resources fájlok eléréséhez szükséges
     */
    public static void readQuestions(Resources resources) {
        if (!QuestionManager.isLoaded() && resources != null) {
            List<Question> questions = new Vector<>();

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

            QuestionManager.setQuestions(questions);
        }
    }

    /* ez esetleg csinálhatna majd kiírást is ha rakok bele mentés opciót */
}
