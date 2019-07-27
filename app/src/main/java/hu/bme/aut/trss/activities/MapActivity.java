package hu.bme.aut.trss.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.trss.R;
import hu.bme.aut.trss.model.player.Player;
import hu.bme.aut.trss.model.player.PlayerManager;
import hu.bme.aut.trss.model.tile.Tile;

/**
 * Pálya
 */
public class MapActivity extends AppCompatActivity {
    private List<Tile> map = new ArrayList<>();
    private Object tag;
    private TextView tvPlayerName, tvTurn;
    private Tile activeTile = null;
    private boolean afterQuiz = false;
    private boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* TODO ehelyett kitalálni valami értelmesebb pályadesign-t */
        /* főleg úgy, hogy jelenleg a listában vízszintes sorrendből vannak benne a mezők,
         * a tényleges lépési sorrend függőleges irányú, ez zavaró lehet */
        List<Integer> reds = new ArrayList<>();
        List<Integer> pinks = new ArrayList<>();
        List<Integer> greenishes = new ArrayList<>();
        reds.add(8);
        reds.add(15);
        reds.add(18);
        reds.add(35);
        reds.add(40);
        pinks.add(2);
        pinks.add(21);
        pinks.add(24);
        pinks.add(28);
        pinks.add(39);
        greenishes.add(5);
        greenishes.add(17);
        greenishes.add(27);
        greenishes.add(32);
        greenishes.add(38);

        tvPlayerName = findViewById(R.id.tvActivePlayer);
        tvTurn = findViewById(R.id.tvTurn);

        Button btnNext = findViewById(R.id.btnNext);
        TableLayout playground = findViewById(R.id.map);

        for (int i = 0; i < playground.getChildCount(); i++) {
            TableRow tableRow = (TableRow) playground.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                ImageView imageView = (ImageView) tableRow.getChildAt(j);

                if (!imageView.getTag().toString().equals(getString(R.string.tile_separator))) {
                    imageView.setOnTouchListener(touchListener);
                    imageView.setOnClickListener(clickListener);

                    boolean add = true;
                    int id = Integer.parseInt(imageView.getTag().toString());
                    if (reds.contains(id)) {
                        add = false;
                        map.add(new Tile(id, R.drawable.red, imageView));
                    }
                    if (greenishes.contains(id)) {
                        add = false;
                        map.add(new Tile(id, R.drawable.greenish, imageView));
                    }
                    if (pinks.contains(id)) {
                        add = false;
                        map.add(new Tile(id, R.drawable.pink, imageView));
                    }
                    if (add) {
                        map.add(new Tile(id, R.drawable.white, imageView));
                    }
                }
            }
        }

        for (Tile tile : map) {
            tile.setNextTile(selectTile(tile.getTag() + 1));
            tile.getImageView().setImageResource(tile.getResId());
        }

        if (activeTile == null) {
            activeTile = map.get(0);

            for (Player player : PlayerManager.players) {
                stepper(player, activeTile);
            }
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTile = PlayerManager.getActivePlayer().getCurrentTile();
                afterQuiz = true;

                Intent intent = new Intent(MapActivity.this, QuizActivity.class);
                intent.putExtra(getResources().getString(R.string.tile), activeTile.getResId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        activeTile = PlayerManager.getActivePlayer().getCurrentTile();

        /* csak akkor, ha QuizActivity-ről jövünk vissza */
        if (afterQuiz) {
            afterQuiz = false;

            /* van következő mező, ahova lehet lépni */
            if (activeTile.getNextTile() != null) {
                int spec = activeTile.getResId();

                /* helyes válasz */
                if (PlayerManager.getActivePlayer().isCorrect()) {
                    activeTile.removePlayer(PlayerManager.getActivePlayer());

                    setImageForActiveTile();

                    if (spec == R.drawable.greenish) {
                        for (int i = 0; i < 3; i++) {
                            activeTile = activeTile.getNextTile();
                        }
                    } else {
                        activeTile = activeTile.getNextTile();
                    }

                    stepper(PlayerManager.getActivePlayer(), activeTile);

                    setImageForActiveTile();

                    if (activeTile.getImageView().getTag() == getString(R.string.finishTile_tag)) {
                        if (PlayerManager.finished(PlayerManager.getActivePlayer())) {
                            end = true;
                        }
                    }
                } else {        /* helytelen válasz */
                    activeTile.removePlayer(PlayerManager.getActivePlayer());
                    setImageForActiveTile();

                    switch (spec) {
                        case R.drawable.pink:
                            activeTile = selectTile(activeTile.getTag() - 1);
                            break;
                        case R.drawable.red:
                            activeTile = selectTile(Integer.parseInt(getString(R.string.startTile_tag)));
                            break;
                        case R.drawable.greenish:
                            activeTile = activeTile.getNextTile();
                            break;
                    }

                    stepper(PlayerManager.getActivePlayer(), activeTile);
                    setImageForActiveTile();
                }
            }

            if (!PlayerManager.players.isEmpty()) {
                PlayerManager.nextPlayer();
                activeTile = PlayerManager.getActivePlayer().getCurrentTile();
            }
        }

        if (end) {
            Intent intent = new Intent(MapActivity.this, EndActivity.class);
            startActivity(intent);
        } else {
            map.get(0).getImageView().setImageResource(getStartOrFinishPicture(map.get(0)));
            map.get(5).getImageView().setImageResource(getStartOrFinishPicture(map.get(5)));
            tvPlayerName.setText(PlayerManager.getActivePlayer().getName());
            tvTurn.setText(PlayerManager.getTurn());
        }


        /////////////////////////////////////////////////////////////////////////////////////////////
        /* teszteléshez hasznos */
        /*if (PlayerManager.players != null) {
            for (Player player : PlayerManager.players) {
                String msg = player.getName() + "- mezőtag: " + player.getCurrentTile().getTag().toString();
                Log.i(getString(R.string.log_tag), msg);
            }
        }
        if (PlayerManager.getFinishers() != null) {
            for (Player player : PlayerManager.getFinishers()) {
                String msg = player.getName() + "- mezőtag: " + player.getCurrentTile().getTag().toString();
                Log.i(getString(R.string.log_tag), msg);
            }
        }*/
        /////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(findViewById(R.id.layout), getResources().getString(R.string.quit_message), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.quit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).setActionTextColor(Color.RED).show();
    }

    /**
     * Játékost lépteti mezőre
     *
     * @param whom  őt kell léptetni
     * @param where erre a mezőre
     */
    private void stepper(Player whom, Tile where) {
        where.addPlayer(whom);
        whom.setCurrentTile(where);
    }

    /**
     * Paraméterben megadott tag-gel rendelkező Tile-t választja ki.
     * Ha a tag nagyobb, akkor valószínűleg a finish utáni mezőt keresne, ami nem létezik,
     * ebben az esetben a finish mezőt adja vissza, egyébként pedig ha megtalálja, akkor a kért Tile-t
     *
     * @param tag keresett Tile tag-jének értéke
     * @return kért Tile, vagy finish Tile
     */
    private Tile selectTile(Integer tag) {
        Tile ret = map.get(5);              // ha minden igaz, ez a finish

        if (tag >= map.size()) {
            return ret;
        }

        for (Tile tile : map) {
            if (tile.getTag().equals(tag)) {
                ret = tile;
            }
        }
        return ret;
    }

    /**
     * A kezdő és cél mezők képeinek kiválasztása, a játékosok számának függvényében.
     *
     * @param startOrFinishTile Mező, amelyhez kell a kép
     * @return A megfelelő kép erőforrás id-ja
     */
    private int getStartOrFinishPicture(Tile startOrFinishTile) {
        if (startOrFinishTile.getTag().toString().equals(getString(R.string.startTile_tag))) {
            switch (startOrFinishTile.getPlayers().size()) {
                case 1:
                    return R.drawable.start_1p;
                case 2:
                    return R.drawable.start_2p;
                case 3:
                    return R.drawable.start_3p;
                case 4:
                    return R.drawable.start_4p;
                default:
                    return R.drawable.start_empty;
            }
        } else {
            switch (startOrFinishTile.getPlayers().size()) {
                case 1:
                    return R.drawable.finish_1p;
                case 2:
                    return R.drawable.finish_2p;
                case 3:
                    return R.drawable.finish_3p;
                case 4:
                    return R.drawable.finish_4p;
                default:
                    return R.drawable.finish_empty;
            }
        }
    }

    /**
     * Lépés után a mező "újrarajzolása", megfelelő játékosokkal stb
     */
    private void setImageForActiveTile() {
        if (activeTile.getPlayers() == null || activeTile.getPlayers().isEmpty()) {
            activeTile.getImageView().setImageResource(activeTile.getResId());
        } else {
            switch (activeTile.getResId()) {
                case R.drawable.white:
                    switch (activeTile.getPlayers().size()) {
                        case 2:
                            activeTile.getImageView().setImageResource(R.drawable.white_2p);
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(R.drawable.white_3p);
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(R.drawable.white_4p);
                            break;
                        case 1:
                            switch (activeTile.getPlayers().get(0).getResId()) {
                                case R.drawable.black_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.black_player_white);
                                    break;
                                case R.drawable.blue_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.blue_player_white);
                                    break;
                                case R.drawable.green_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.green_player_white);
                                    break;
                                case R.drawable.yellow_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.yellow_player_white);
                                    break;
                            }
                            break;
                    }
                    break;
                case R.drawable.greenish:
                    switch (activeTile.getPlayers().size()) {
                        case 2:
                            activeTile.getImageView().setImageResource(R.drawable.greenish_2p);
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(R.drawable.greenish_3p);
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(R.drawable.greenish_4p);
                            break;
                        case 1:
                            switch (activeTile.getPlayers().get(0).getResId()) {
                                case R.drawable.black_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.black_player_greenish);
                                    break;
                                case R.drawable.blue_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.blue_player_greenish);
                                    break;
                                case R.drawable.green_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.green_player_greenish);
                                    break;
                                case R.drawable.yellow_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.yellow_player_greenish);
                                    break;
                            }
                            break;
                    }
                    break;
                case R.drawable.pink:
                    switch (activeTile.getPlayers().size()) {
                        case 2:
                            activeTile.getImageView().setImageResource(R.drawable.pink_2p);
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(R.drawable.pink_3p);
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(R.drawable.pink_4p);
                            break;
                        case 1:
                            switch (activeTile.getPlayers().get(0).getResId()) {
                                case R.drawable.black_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.black_player_pink);
                                    break;
                                case R.drawable.blue_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.blue_player_pink);
                                    break;
                                case R.drawable.green_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.green_player_pink);
                                    break;
                                case R.drawable.yellow_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.yellow_player_pink);
                                    break;
                            }
                            break;
                    }
                    break;
                case R.drawable.red:
                    switch (activeTile.getPlayers().size()) {
                        case 2:
                            activeTile.getImageView().setImageResource(R.drawable.red_2p);
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(R.drawable.red_3p);
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(R.drawable.red_4p);
                            break;
                        case 1:
                            switch (activeTile.getPlayers().get(0).getResId()) {
                                case R.drawable.black_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.black_player_red);
                                    break;
                                case R.drawable.blue_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.blue_player_red);
                                    break;
                                case R.drawable.green_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.green_player_red);
                                    break;
                                case R.drawable.yellow_player_white:
                                    activeTile.getImageView().setImageResource(R.drawable.yellow_player_red);
                                    break;
                            }
                            break;
                    }
                    break;
                default:
                    switch (activeTile.getPlayers().size()) {
                        case 2:
                            activeTile.getImageView().setImageResource(getStartOrFinishPicture(activeTile));
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(getStartOrFinishPicture(activeTile));
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(getStartOrFinishPicture(activeTile));
                            break;
                        default:
                            activeTile.getImageView().setImageResource(getStartOrFinishPicture(activeTile));
                            break;
                    }
                    break;
            }
        }
    }

    // the purpose of the touch listener is just to store the touch X,Y coordinates
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                tag = v.getTag();
            }
            // let the touch event pass on to whoever needs it
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayerManager.getActivePlayer().setCorrect(false);

            int ID = Integer.parseInt(tag.toString());

            Tile clickedTile = map.get(0);

            for (Tile tile : map) {
                if (tile.getTag().equals(ID)) {
                    clickedTile = tile;
                }
            }

            //Log.i(getResources().getString(R.string.log_tag), "klikkelt mező " + tag);
            //Log.i(getResources().getString(R.string.log_tag), "következő mező " + clickedTile.getNextTile().getTag());

            Intent intent = new Intent(MapActivity.this, TileActivity.class);
            if (clickedTile.getTag().toString().equals(getString(R.string.startTile_tag)) || clickedTile.getTag().toString().equals(getString(R.string.finishTile_tag))) {
                intent.putExtra(getResources().getString(R.string.spec), clickedTile.getTag());
            } else {
                intent.putExtra(getResources().getString(R.string.spec), clickedTile.getResId());
            }
            String[] plyrs = null;
            if (clickedTile.getPlayers() != null) {
                plyrs = new String[clickedTile.getPlayers().size()];
                for (int i = 0; i < clickedTile.getPlayers().size(); i++) {
                    plyrs[i] = clickedTile.getPlayers().get(i).getName();
                }
            }
            intent.putExtra(getResources().getString(R.string.players), plyrs);
            startActivity(intent);
        }
    };
}
