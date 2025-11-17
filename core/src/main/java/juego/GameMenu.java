package juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMenu extends Game {

    private static GameMenu instance;

    private SpriteBatch batch;
    private BitmapFont font;
    private int higherScore;

    public GameMenu() {
        instance = this;
    }

    public static GameMenu getInstance() {
        return instance;
    }

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHigherScore() {
        return higherScore;
    }

    public void setHigherScore(int higherScore) {
        this.higherScore = higherScore;
    }
}
