package juego;

import com.badlogic.gdx.Gdx;

public class GameOverScreen extends PantallaBase {

    public GameOverScreen(final GameMenu game) {
        super(game);
    }

    @Override
    protected void dibujarContenido(float delta) {
        font.draw(batch, "GAME OVER", 100, 200);
        font.draw(batch, "Toca en cualquier lado para reiniciar", 100, 100);
    }

    @Override
    protected void manejarInput(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }
}
