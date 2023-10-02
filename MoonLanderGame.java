package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;


    @Override
    public void initialize() {

        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }


    private void createGame() {

        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1_000;
    }


    private void drawScene() {

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellColor(x, y, Color.BLACK);
            }
        }
        landscape.draw(this);
        rocket.draw(this);
    }


    private void createGameObjects() {

        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }


    @Override
    public void onTurn(int step) {

        if (score > 0) {
            score--;
        }

        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();
    }


    @Override
    public void setCellColor(int x, int y, Color color) {

        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            super.setCellColor(x, y, color);
        }
    }


    @Override
    public void onKeyPress(Key key) {

        if (key == Key.SPACE && isGameStopped) {
            createGame();
        }

        if (key == Key.UP) {
            isUpPressed = true;
        }

        if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        }

        if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        }
    }


    @Override
    public void onKeyReleased(Key key) {

        if (key == Key.UP) {
            isUpPressed = false;
        }

        if (key == Key.LEFT) {
            isLeftPressed = false;
        }

        if (key == Key.RIGHT) {
            isRightPressed = false;
        }
    }


    private void check() {

        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped())) {
            gameOver();
        }

        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        }
    }


    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "YOU WIN", Color.GREEN, 70);
        stopTurnTimer();
    }


    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "YOU LOSE", Color.RED, 70);
        stopTurnTimer();
        score = 0;
    }
}