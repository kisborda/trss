package hu.bme.aut.trss.data.tile;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.trss.data.player.Player;

/**
 * Játékmezőt modellezi
 */
public class Tile {
    private int tag;                        // imageview tag-je
    private int spec;                       // resource id, R.drawable.[fájlnév]
    private ImageView imageView;            // ref a felületi elemre
    private List<Player> players;           // mezőn lévő játékosok
    private Tile nextTile;                  // következő mező

    public Tile(int id, int spec, ImageView iv) {
        this.tag = id;
        this.spec = spec;
        this.imageView = iv;
        players = new ArrayList<>();
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public int getSpec() {
        return spec;
    }

    public void setSpec(int spec) {
        this.spec = spec;
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

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public Integer getTag() {
        return tag;
    }
}
