package juego;

import com.badlogic.gdx.Gdx;

public class MainMenuScreen extends PantallaBase {

    public MainMenuScreen(GameMenu game) {
        super(game);
    }

    @Override
    protected void dibujarContenido(float delta) {
        font.getData().setScale(2);
        font.draw(batch, "Bienvenido a Claudio Bravo Atajadas!!!", 100, camera.viewportHeight/2 + 50);

        font.getData().setScale(1.5f);
        font.draw(batch, "Controles:", 100, camera.viewportHeight/2 - 50);
        font.draw(batch, "A / D: Mover", 100, camera.viewportHeight/2 - 90);
        font.draw(batch, "SPACE: Dash", 100, camera.viewportHeight/2 - 130);
        font.draw(batch, "ESC / P: Pausa", 100, camera.viewportHeight/2 - 170);

        font.getData().setScale(2);
        font.draw(batch, "Toca para comenzar!", 100, camera.viewportHeight/2 - 250);
    }

    @Override
    protected void manejarInput(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }
}
