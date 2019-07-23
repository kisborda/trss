package hu.bme.aut.trss.model.player;

import java.util.List;

/**
 * Játékosokat menedzseli, kinek a fordulója aktív, ki következik
 */
public class PlayerManager {
    public static List<Player> players;             // összes játékos
    private static int activePlayer = 0;            // a listában való indexe
    public static Player winner;
    //public static List<Player> finished;            // abban a sorrendben kerülnek majd a listába, ahogy célbaértek, egyelőre nincs használva, lehet teljesen kiváltja majd a winner tagváltozót
    public static boolean cheat = false;

    public static Player getActivePlayer() {
        return players.get(activePlayer);
    }

    public static Player getNextPlayer() {
        activePlayer++;
        if (activePlayer < players.size()) {
            return players.get(activePlayer);
        } else {
            activePlayer = 0;
            return players.get(players.size() - 1);
        }
    }

}
