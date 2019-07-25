package hu.bme.aut.trss.model.tile;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.trss.model.player.Player;

/**
 * Játékmezőt modellezi
 */
public class Tile {
    private int tag;                        // imageview tag-je
    private int resId;                      // resource id, R.drawable.[fájlnév]
    private ImageView imageView;            // ref a felületi elemre
    private List<Player> players;           // mezőn lévő játékosok
    private Tile nextTile;                  // következő mező

    public Tile(int id, int resId, ImageView iv) {
        this.tag = id;
        this.resId = resId;
        this.imageView = iv;
        players = new ArrayList<>();
    }

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public List<Player> getPlayers() {
        if (players.size() == 0) {
            return null;
        } else {
            return players;
        }
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Integer getTag() {
        return tag;
    }
}
