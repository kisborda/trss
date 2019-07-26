package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.trss.R;
import hu.bme.aut.trss.model.player.Player;
import hu.bme.aut.trss.model.player.PlayerManager;

/**
 * Eredményhirdetés
 */
public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Button btnMenu = findViewById(R.id.btnMenu);
        TextView tvWinner = findViewById(R.id.tvWinner);
        TextView tvFirst = findViewById(R.id.tvPlayer1);
        TextView tvSecond = findViewById(R.id.tvPlayer2);
        TextView tvThird = findViewById(R.id.tvPlayer3);
        TextView tvFourth = findViewById(R.id.tvPlayer4);

        List<Player> result = PlayerManager.getFinishers();

        tvWinner.setText(result.get(0).getName());

        tvFirst.setText(result.get(0).getName());
        tvFirst.setTextColor(result.get(0).getResId());

        if (result.size() > 1) {
            tvSecond.setText(result.get(1).getName());
            tvSecond.setTextColor(result.get(1).getResId());
        }
        if (result.size() > 2) {
            tvThird.setText(result.get(2).getName());
            tvThird.setTextColor(result.get(2).getResId());
        }
        if (result.size() > 3) {
            tvFourth.setText(result.get(3).getName());
            tvFourth.setTextColor(result.get(3).getResId());
        }


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        /* Nem lehet visszalépni! */
    }
}
