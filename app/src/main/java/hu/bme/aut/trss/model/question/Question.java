package hu.bme.aut.trss.model.question;

/**
 * Kérdést modellezi
 * kérdés nehézsége, kategóriája nincs kihasználva
 */
public class Question {
    private Integer difficulty;     // nehézség
    private String question;        // kérdés szövege
    private String a;               // válaszlehetőségek
    private String b;
    private String c;
    private String d;
    private String answer;          // helyes válasz betűjele (A/B/C/D)
    private String category;        // kategória

    public Question(String[] line) {
        this.difficulty = Integer.parseInt(line[0]);
        this.question = line[1];
        this.a = line[2];
        this.b = line[3];
        this.c = line[4];
        this.d = line[5];
        this.answer = line[6];
        this.category = line[7];
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }
}
