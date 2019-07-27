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
import android.widget.Toast;

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
    private Button btnStart, btnTest;
    private EditText etPlayerOneName, etPlayerTwoName, etPlayerThreeName, etPlayerFourName;
    private List<EditText> etList;
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

        etList = new ArrayList<>();
        etList.add(etPlayerOneName);
        etList.add(etPlayerTwoName);
        etList.add(etPlayerThreeName);
        etList.add(etPlayerFourName);

        rgPlayerCount = findViewById(R.id.rgPlayerCount);

        btnStart = findViewById(R.id.btnStart);
        btnTest = findViewById(R.id.btnTest);
        btnStart.setEnabled(false);

        rgPlayerCount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbPlayerCount = findViewById(rgPlayerCount.getCheckedRadioButtonId());

                playerCount = Integer.parseInt(rbPlayerCount.getText().toString());

                for (int i = 0; i < layoutList.size(); i++) {
                    if (i < playerCount) {
                        setViewAndChildrenEnabled(layoutList.get(i), true);
                    } else {
                        setViewAndChildrenEnabled(layoutList.get(i), false);
                    }
                }
                btnStart.setEnabled(true);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerManager.cheat = true;
                Toast.makeText(UserActivity.this, getString(R.string.cheater_message), Toast.LENGTH_SHORT).show();
                btnTest.setEnabled(false);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sameName = false;

                player1 = new Player(R.drawable.black_player_white, etPlayerOneName.getText().toString());
                player2 = new Player(R.drawable.blue_player_white, etPlayerTwoName.getText().toString());
                player3 = new Player(R.drawable.green_player_white, etPlayerThreeName.getText().toString());
                player4 = new Player(R.drawable.yellow_player_white, etPlayerFourName.getText().toString());

                playerList = new ArrayList<>();
                playerList.add(player1);
                playerList.add(player2);
                playerList.add(player3);
                playerList.add(player4);

                for (int i = 0; i < playerList.size(); i++) {
                    Integer num = i + 1;
                    String name = getString(R.string.default_namebase) + num.toString();
                    if (etList.get(i).getText().toString().equals("") || etList.get(i).getText().toString().equals("\t") || etList.get(i).getText().toString().equals("\n")) {
                        etList.get(i).setText(name);
                        playerList.get(i).setName(name);
                    }
                    for (int j = i; j < playerList.size(); j++) {
                        if (i != j && playerList.get(i).getName().equals(playerList.get(j).getName())) {
                            sameName = true;
                        }
                    }
                }

                PlayerManager.players = new ArrayList<>();

                for (int i = 0; i < playerCount; i++) {
                    PlayerManager.players.add(playerList.get(i));
                }

                if (sameName) {
                    Toast.makeText(UserActivity.this, getString(R.string.unique_names_message), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(UserActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
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
