package hu.bme.aut.trss.data.player;

import hu.bme.aut.trss.data.tile.Tile;

/**
 * A játékost modellezi.
 */
public class Player {
    private int resId;                          // avatar resource id
    private String name;                        // játékos neve
    private Boolean correct = false;            // legutóbbi válaszának helyessége
    private Tile currentTile = null;            // ezen a mezőn áll

    public Player(int resId, String name) {
        this.resId = resId;
        this.name = name;
    }

    public Integer getResId() {
        return resId;
    }

    public String getName() {
        return name;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile nextTile) {
        this.currentTile = nextTile;
    }
}
