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
import hu.bme.aut.trss.data.player.Player;
import hu.bme.aut.trss.data.player.PlayerManager;
import hu.bme.aut.trss.data.tile.Tile;

/**
 * Pálya
 */
public class MapActivity extends AppCompatActivity {
    private List<Tile> map = new ArrayList<>();
    private Object tag;
    private TextView tvPlayerName;
    private Tile activeTile = null;
    private Player activePlayer = null;
    private boolean tile = false;

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

        Button btnNext = findViewById(R.id.btnNext);
        TableLayout playground = findViewById(R.id.map);

        for (int i = 0; i < playground.getChildCount(); i++) {
            TableRow tableRow = (TableRow) playground.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                ImageView imageView = (ImageView) tableRow.getChildAt(j);

                if (!imageView.getTag().toString().equals(getString(R.string.tile_separator))) {
                    if (!tableRow.getChildAt(j).getTag().toString().equals(getString(R.string.startTile_tag)) && !tableRow.getChildAt(j).getTag().toString().equals(getString(R.string.finishTile_tag))) {
                        imageView.setOnTouchListener(touchListener);
                        imageView.setOnClickListener(clickListener);
                    }

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

        // ehelyett mindre mehetne egy setspec, vagy megoldani, hogy ne legyen szükség erre a két sorra sem
        map.get(0).setSpec(getStartPicture(map.get(0)));
        map.get(5).setSpec(R.drawable.finish_empty);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // teszteléshez...
        // map.get(25).setSpec(R.drawable.finish_empty);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (Tile tile : map) {
            tile.setNextTile(selectTile(tile.getTag() + 1));

            tile.getImageView().setImageResource(tile.getSpec());
        }

        if (activeTile == null) {
            activeTile = map.get(0);

            for (Player player : PlayerManager.players) {
                player.setCurrentTile(activeTile);
                activeTile.addPlayer(player);
            }
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTile = PlayerManager.getActivePlayer().getCurrentTile();

                Intent intent = new Intent(MapActivity.this, QuizActivity.class);
                intent.putExtra(getResources().getString(R.string.tile), activeTile.getSpec());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (tile) {                             // ha csak a részletes nézetből jön vissza, akkor nem kell csinálni semmit sem
            tile = false;
        } else {
            /* van következő mező, ahova lehet lépni */
            if (activeTile.getNextTile() != null) {
                int spec = activeTile.getSpec();

                /* helyes válasz */
                if (PlayerManager.getActivePlayer().isCorrect()) {
                    if (spec == R.drawable.greenish) {
                        activeTile.removePlayer(PlayerManager.getActivePlayer());

                        setImageForActiveTile();

                        for (int i = 0; i < 3; i++) {
                            if (activeTile.getNextTile() != null) {                             // elvileg nem lehetne null Tile sehol, de nem merem kivenni, hátha elnéztem valamit
                                activeTile = activeTile.getNextTile();
                            }
                        }
                    } else {
                        activeTile.removePlayer(PlayerManager.getActivePlayer());

                        setImageForActiveTile();

                        activeTile = activeTile.getNextTile();
                    }
                    if (activeTile.getImageView().getTag() == getString(R.string.finishTile_tag)) {
                        PlayerManager.winner = activePlayer;

                        /* TODO finish-t rendesen megcsinálni,
                            jelenleg az első célbaérő játékossal végetér az egész játék */

                        Intent intent = new Intent(MapActivity.this, EndActivity.class);
                        startActivity(intent);
                        MapActivity.this.finish();
                    } else {
                        stepper(PlayerManager.getActivePlayer(), activeTile);

                        setImageForActiveTile();
                    }
                }       /* helytelen válasz */ else {
                    activeTile.removePlayer(PlayerManager.getActivePlayer());
                    setImageForActiveTile();
                    if (spec == R.drawable.pink) {
                        activeTile = selectTile(activeTile.getTag() - 1);
                    }
                    if (spec == R.drawable.red) {
                        activeTile = selectTile(Integer.parseInt(getString(R.string.startTile_tag)));
                    }
                    stepper(PlayerManager.getActivePlayer(), activeTile);
                    setImageForActiveTile();
                }
            }

            if (activePlayer != null) {
                PlayerManager.getNextPlayer().setCorrect(false);
            } else {
                activePlayer = PlayerManager.getActivePlayer();
            }
        }

        map.get(0).getImageView().setImageResource(getStartPicture(map.get(0)));
        tvPlayerName.setText(PlayerManager.getActivePlayer().getName());
    }

    @Override
    public void onBackPressed() {
        // nincs ennél jobb?
        Snackbar.make(findViewById(R.id.layout), getResources().getString(R.string.quit_message), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.quit), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setActionTextColor(Color.RED).show();
    }

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

    // TODO getStartPicture()-t valahogy beolvasztani a setImageForActiveTile() függvénybe
    private int getStartPicture(Tile tile) {
        if (tile.getPlayers() == null) {
            return R.drawable.start_empty;
        } else {
            switch (tile.getPlayers().size()) {
                case 1:
                    return R.drawable.start_1p;
                case 2:
                    return R.drawable.start_2p;
                case 3:
                    return R.drawable.start_3p;
                default:
                    return R.drawable.start_4p;
            }
        }
    }

    /**
     * Lépés után a mező "újrarajzolása", megfelelő játékosokkal stb
     */
    private void setImageForActiveTile() {
        if (activeTile.getPlayers() == null) {
            activeTile.getImageView().setImageResource(activeTile.getSpec());
        } else {
            switch (activeTile.getSpec()) {
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
                        default:
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
                        default:
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
                        default:
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
                        default:
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
                            activeTile.getImageView().setImageResource(R.drawable.start_2p);
                            break;
                        case 3:
                            activeTile.getImageView().setImageResource(R.drawable.start_3p);
                            break;
                        case 4:
                            activeTile.getImageView().setImageResource(R.drawable.start_4p);
                            break;
                        default:
                            activeTile.getImageView().setImageResource(R.drawable.start_1p);
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

            Tile clickedTile = map.get(1);

            for (Tile tile : map) {
                if (tile.getTag().equals(ID)) {
                    clickedTile = tile;
                }
            }

            Log.i(getResources().getString(R.string.log_tag), "klikkelt mező " + tag);

            if (clickedTile.getNextTile() != null) {
                Log.i(getResources().getString(R.string.log_tag), "következő mező " + clickedTile.getNextTile().getTag());

                Intent intent = new Intent(MapActivity.this, TileActivity.class);
                intent.putExtra(getResources().getString(R.string.spec), clickedTile.getSpec());
                String[] plyrs = new String[4];
                if (clickedTile.getPlayers() == null) {
                    for (int i = 0; i < 4; i++) {
                        plyrs[i] = null;
                    }
                } else {
                    for (int i = 0; i < clickedTile.getPlayers().size(); i++) {
                        plyrs[i] = clickedTile.getPlayers().get(i).getName();
                    }
                }
                intent.putExtra(getResources().getString(R.string.players), plyrs);
                tile = true;
                startActivity(intent);
            }
        }
    };
}
