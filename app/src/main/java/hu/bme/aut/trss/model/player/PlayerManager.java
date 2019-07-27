package hu.bme.aut.trss.model.player;

import java.util.ArrayList;
import java.util.List;

/**
 * Játékosokat menedzseli, kinek a fordulója aktív, ki következik
 */
public class PlayerManager {
    public static List<Player> players;                     // összes játékos
    private static int activePlayerIndex = 0;               // a listában való indexe
    private static List<Player> finished;                   // célbaért játékosok
    public static boolean cheat = false;
    private static Integer turn = 1;

    /**
     * Aktív játékost adja meg.
     *
     * @return Aktív játékos
     */
    public static Player getActivePlayer() {
        return players.get(activePlayerIndex);
    }

    /**
     * Váltás a következő játékosra
     */
    public static void nextPlayer() {
        activePlayerIndex++;
        if (activePlayerIndex >= players.size()) {
            activePlayerIndex = 0;
            turn++;
        }
    }

    /**
     * Célbaérő játékos feljegyzése, és annak vizsgálata, hogy van-e még további játékos
     *
     * @param player A célbaérő játékos
     * @return Az összes játékos célba ért-e
     */
    public static boolean finished(Player player) {
        if (finished == null || finished.isEmpty()) {
            finished = new ArrayList<>();
        }

        finished.add(player);
        players.remove(player);

        if (activePlayerIndex == 0) {
            activePlayerIndex = players.size() - 1;
        } else {
            activePlayerIndex--;
        }
        return players.isEmpty();
    }

    /**
     * A végeredmény lista elkérése
     *
     * @return Lista a játékosok helyezéseinek megfelelő sorrendben
     */
    public static List<Player> getFinishers() {
        return finished;
    }

    public static String getTurn() {
        return turn.toString();
    }
}
