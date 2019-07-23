package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import hu.bme.aut.trss.R;

/**
 * Mező részletező nézete
 */
public class TileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);

        LinearLayout layout = findViewById(R.id.llBackground);
        TextView tvSpecText = findViewById(R.id.tvSpecText);
        TextView tvPlayer1 = findViewById(R.id.tvPlayer1);
        TextView tvPlayer2 = findViewById(R.id.tvPlayer2);
        TextView tvPlayer3 = findViewById(R.id.tvPlayer3);
        TextView tvPlayer4 = findViewById(R.id.tvPlayer4);
        Button btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        int spec = intent.getIntExtra(getResources().getString(R.string.spec), 0);
        String[] players = intent.getStringArrayExtra(getResources().getString(R.string.players));

        if (players[0] != null) {
            tvPlayer1.setText(players[0]);
        }
        if (players[1] != null) {
            tvPlayer2.setText(players[1]);
        }
        if (players[2] != null) {
            tvPlayer3.setText(players[2]);
        }
        if (players[3] != null) {
            tvPlayer4.setText(players[3]);
        }

        switch (spec) {
            case R.drawable.pink:
                layout.setBackgroundResource(R.drawable.pink);
                tvSpecText.setText(getResources().getString(R.string.pink_details));
                break;
            case R.drawable.greenish:
                layout.setBackgroundResource(R.drawable.greenish);
                tvSpecText.setText(getResources().getString(R.string.greenish_details));
                break;
            case R.drawable.red:
                layout.setBackgroundResource(R.drawable.red);
                tvSpecText.setText(getResources().getString(R.string.red_details));
                tvSpecText.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                layout.setBackgroundResource(R.drawable.white);
                tvSpecText.setText(getResources().getString(R.string.white_details));
                break;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
