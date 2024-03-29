package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hu.bme.aut.trss.R;
import hu.bme.aut.trss.model.player.PlayerManager;
import hu.bme.aut.trss.model.question.Question;
import hu.bme.aut.trss.model.question.QuestionManager;

/**
 * Kvíz
 */
public class QuizActivity extends AppCompatActivity {
    private Button btnA, btnB, btnC, btnD, btnDone;
    private Map<String, Button> buttonsMap = new HashMap<>();
    private Question question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        int tileResId = intent.getIntExtra(getResources().getString(R.string.tile), R.drawable.white);

        question = QuestionManager.getOneQuestion(tileResId);

        TextView tvQuestion = findViewById(R.id.tvQuestion);
        TextView tvPlayerName = findViewById(R.id.tvPlayerName);
        TextView tvTurn = findViewById(R.id.tvTurn);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnDone = findViewById(R.id.btnDone);
        btnDone.setEnabled(false);

        if (question == null) {
            Toast.makeText(QuizActivity.this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
        } else {
            buttonsMap.put(getString(R.string.A), btnA);
            buttonsMap.put(getString(R.string.B), btnB);
            buttonsMap.put(getString(R.string.C), btnC);
            buttonsMap.put(getString(R.string.D), btnD);

            if (PlayerManager.cheat) {
                Toast.makeText(QuizActivity.this, question.getAnswer(), Toast.LENGTH_SHORT).show();
            }

            tvPlayerName.setText(PlayerManager.getActivePlayer().getName());
            tvTurn.setText(PlayerManager.getTurn());
            tvQuestion.setText(question.getQuestion());

            if (tileResId == R.drawable.start_empty) {
                tvQuestion.setBackground(getResources().getDrawable(R.drawable.white, null));
            } else {
                tvQuestion.setBackground(getResources().getDrawable(tileResId, null));
            }

            btnA.setText(question.getA());
            btnB.setText(question.getB());
            btnC.setText(question.getC());
            btnD.setText(question.getD());

            btnA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionAnswered(btnA);
                }
            });
            btnB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionAnswered(btnB);
                }
            });
            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionAnswered(btnC);
                }
            });
            btnD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionAnswered(btnD);
                }
            });
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Azt ellenőrzi, hogy a lenyomott gomb a helyes válasz volt-e. A válaszgombokat deaktiválja, míg a visszagombot aktiválja.
     * Helyes/helytelen válaszokat színezi zöldre/pirosra.
     *
     * @param pressedButton a lenyomott gomb
     */
    private void questionAnswered(Button pressedButton) {
        String correctAnswer = question.getAnswer();

        for (Button tmp : buttonsMap.values()) {
            tmp.setEnabled(false);
        }
        btnDone.setEnabled(true);

        if (pressedButton.equals(buttonsMap.get(correctAnswer))) {
            PlayerManager.getActivePlayer().setCorrect(true);
            pressedButton.setTextColor(Color.GREEN);
        } else {
            PlayerManager.getActivePlayer().setCorrect(false);
            Toast.makeText(QuizActivity.this, getResources().getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show();
            pressedButton.setTextColor(Color.RED);
            buttonsMap.get(correctAnswer).setTextColor(Color.GREEN);
        }
    }

    @Override
    public void onBackPressed() {
        if (btnDone.isEnabled()) {
            Toast.makeText(this, getResources().getString(R.string.use_button_message), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.answer_question_message), Toast.LENGTH_SHORT).show();
        }
    }
}
