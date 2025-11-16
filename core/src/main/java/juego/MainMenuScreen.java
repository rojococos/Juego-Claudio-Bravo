package juego;

import com.badlogic.gdx.Gdx;

public class MainMenuScreen extends PantallaBase {

    public MainMenuScreen(final GameMenu game) {
        super(game);
    }

    @Override
    protected void dibujarContenido(float delta) {
        font.getData().setScale(2f, 2f);
        font.draw(batch, "Bienvenido a Claudio Bravo Atajadas!!! ", 100, camera.viewportHeight / 2f + 50f);

        font.getData().setScale(1.5f, 1.5f);
        font.draw(batch, "Controles:", 100, camera.viewportHeight / 2f - 50f);
        font.draw(batch, "A / D: Mover", 100, camera.viewportHeight / 2f - 90f);
        font.draw(batch, "SPACE: Dash de velocidad", 100, camera.viewportHeight / 2f - 130f);
        font.draw(batch, "ESC / P: Pausa", 100, camera.viewportHeight / 2f - 170f);

        font.getData().setScale(2f, 2f);
        font.draw(batch, "Toca en cualquier lugar para comenzar!", 100, camera.viewportHeight / 2f - 250f);
    }

    @Override
    protected void manejarInput(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }
}
