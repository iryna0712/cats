package java.game;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Application {

    public static void main(String[] args) {
        GameManager manager = new GameManager();

        Logger.getGlobal().setLevel(Level.FINEST);
    }

}
