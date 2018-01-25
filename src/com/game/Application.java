package com.game;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;


// versions.
// 1.1 add ability to shuffle cards up to one's wish instead of random
//
public class Application {

    public static void main(String[] args) {

        try {
        FileHandler fileHandler = new FileHandler("log.txt");
        fileHandler.setLevel(Level.FINEST);
        Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException e) {

        }

        GameManager manager = new GameManager();

    }

}
