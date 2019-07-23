package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hu.bme.aut.trss.R;
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
        TextView tvPlayer1 = findViewById(R.id.tvPlayer1);
        TextView tvPlayer2 = findViewById(R.id.tvPlayer2);
        TextView tvPlayer3 = findViewById(R.id.tvPlayer3);
        TextView tvPlayer4 = findViewById(R.id.tvPlayer4);

        tvWinner.setText(PlayerManager.winner.getName());

        tvPlayer1.setText(PlayerManager.players.get(0).getName());
        tvPlayer1.setTextColor(getResources().getColor(R.color.black));

        if (PlayerManager.players.size() > 1) {
            if (PlayerManager.players.get(1) != null) {
                tvPlayer2.setText(PlayerManager.players.get(1).getName());
                tvPlayer2.setTextColor(getResources().getColor(R.color.blue));
            }
            if (PlayerManager.players.get(2) != null) {
                tvPlayer3.setText(PlayerManager.players.get(2).getName());
                tvPlayer3.setTextColor(getResources().getColor(R.color.green));
            }
            if (PlayerManager.players.get(3) != null) {
                tvPlayer4.setText(PlayerManager.players.get(3).getName());
                tvPlayer4.setTextColor(getResources().getColor(R.color.yellow));
            }
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /* Nem lehet visszalépni! */
    }
}
