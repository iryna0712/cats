package com;

import com.game.Client;

import java.io.IOException;

public class GameSecurityManager {
    private static GameSecurityManager instance;

    private GameSecurityManager() {}

    public static GameSecurityManager getInstance() {
        if (instance == null) {
            instance = new GameSecurityManager();
        }
        return instance;
    }

    public boolean kickClient(Client client) {
        boolean result = true;
        client.sendMessage("BAD BOY");
        try {
            client.disconnect(true);
        } catch (IOException e) {
            result = false;
             e.printStackTrace();
        }

        return result;
    }
}
