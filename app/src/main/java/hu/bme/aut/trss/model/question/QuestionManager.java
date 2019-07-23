package hu.bme.aut.trss.model.question;

import java.util.List;
import java.util.Random;
import java.util.Vector;

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

    public static void setQuestions(List<Question> questions) {
        QuestionManager.questions = questions;
    }

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

    /**
     * Vizsgálja, hogy vannak-e már kérdések betöltve fájlból
     *
     * @return true ha legalább az egyik listában vannak elemek, egyébként false
     */
    public static Boolean isLoaded() {
        return !questions.isEmpty() | !usedQuestions.isEmpty();
    }
}
