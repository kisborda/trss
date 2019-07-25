package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        List<TextView> tvList = new ArrayList<>();
        tvList.add(tvPlayer1);
        tvList.add(tvPlayer2);
        tvList.add(tvPlayer3);
        tvList.add(tvPlayer4);

        Intent intent = getIntent();
        int spec = intent.getIntExtra(getResources().getString(R.string.spec), R.drawable.white);
        String[] players = intent.getStringArrayExtra(getResources().getString(R.string.players));

        if (players != null) {
            for (int i = 0; i < players.length; i++) {
                tvList.get(i).setText(players[i]);
            }
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
            case 0:
                layout.setBackgroundResource(R.drawable.purple);
                tvSpecText.setText(getString(R.string.start_tile_details));
                break;
            case 41:
                layout.setBackgroundResource(R.drawable.purple);
                tvSpecText.setText(getString(R.string.finish_tile_details));
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
