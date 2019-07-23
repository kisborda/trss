package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.trss.R;
import hu.bme.aut.trss.model.player.Player;
import hu.bme.aut.trss.model.player.PlayerManager;

/**
 * Játékosok hozzáadását megvalósító activity
 */
public class UserActivity extends AppCompatActivity {
    private RadioGroup rgPlayerCount;
    private RadioButton rbPlayerCount;
    private Integer playerCount;
    private Button btnStart, btnOkay;
    private EditText etPlayerOneName, etPlayerTwoName, etPlayerThreeName, etPlayerFourName;
    private List<LinearLayout> layoutList;
    private Player player1 = null;
    private Player player2 = null;
    private Player player3 = null;
    private Player player4 = null;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        LinearLayout llPlayerOne = findViewById(R.id.llPlayerOne);
        LinearLayout llPlayerTwo = findViewById(R.id.llPlayerTwo);
        LinearLayout llPlayerThree = findViewById(R.id.llPlayerThree);
        LinearLayout llPlayerFour = findViewById(R.id.llPlayerFour);

        layoutList = new ArrayList<>();
        layoutList.add(llPlayerOne);
        layoutList.add(llPlayerTwo);
        layoutList.add(llPlayerThree);
        layoutList.add(llPlayerFour);

        for (int i = 0; i < layoutList.size(); i++) {
            setViewAndChildrenEnabled(layoutList.get(i), false);
        }

        etPlayerOneName = findViewById(R.id.etPlayerOneName);
        etPlayerTwoName = findViewById(R.id.etPlayerTwoName);
        etPlayerThreeName = findViewById(R.id.etPlayerThreeName);
        etPlayerFourName = findViewById(R.id.etPlayerFourName);

        rgPlayerCount = findViewById(R.id.rgPlayerCount);

        btnStart = findViewById(R.id.btnStart);
        btnOkay = findViewById(R.id.btnOkay);
        btnStart.setEnabled(false);
        btnOkay.setEnabled(false);

        rgPlayerCount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbPlayerCount = findViewById(rgPlayerCount.getCheckedRadioButtonId());

                btnOkay.setEnabled(true);
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOkay.getText().toString().equals(getResources().getString(R.string.btnOkay_text_next))) {
                    btnOkay.setText(getResources().getString(R.string.back));
                    btnStart.setEnabled(true);

                    playerCount = Integer.parseInt(rbPlayerCount.getText().toString());

                    setViewAndChildrenEnabled(rgPlayerCount, false);
                    for (int i = 0; i < playerCount; i++) {
                        setViewAndChildrenEnabled(layoutList.get(i), true);
                    }
                } else {
                    btnOkay.setText(getResources().getString(R.string.btnOkay_text_next));

                    for (LinearLayout layout : layoutList) {
                        setViewAndChildrenEnabled(layout, false);
                    }

                    setViewAndChildrenEnabled(rgPlayerCount, true);
                    btnStart.setEnabled(false);
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1 = new Player(R.drawable.black_player_white, etPlayerOneName.getText().toString());
                player2 = new Player(R.drawable.blue_player_white, etPlayerTwoName.getText().toString());
                player3 = new Player(R.drawable.green_player_white, etPlayerThreeName.getText().toString());
                player4 = new Player(R.drawable.yellow_player_white, etPlayerFourName.getText().toString());

                playerList = new ArrayList<>();
                playerList.add(player1);
                playerList.add(player2);
                playerList.add(player3);
                playerList.add(player4);

                PlayerManager.players = new ArrayList<>();

                for (int i = 0; i < playerCount; i++) {
                    PlayerManager.players.add(playerList.get(i));
                }

                Intent intent = new Intent(UserActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * View-t és gyerekeit állítja a paraméternek megfelelően (enabled/disabled)
     *
     * @param view    Ezen a nézeten kell állítani
     * @param enabled Erre állítja át
     */
    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    setViewAndChildrenEnabled(child, enabled);
                }
            }
        }
    }
}
