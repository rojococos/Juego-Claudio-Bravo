package juego;

import com.badlogic.gdx.Gdx;

public class PausaScreen extends PantallaBase {

    private GameScreen juego;

    public PausaScreen(GameMenu game, GameScreen juego) {
        super(game);
        this.juego = juego;
    }

    @Override
    protected float getColorB() { return 1f; }

    @Override
    protected float getColorA() { return 0.5f; }

    @Override
    protected void dibujarContenido(float delta) {
        font.draw(batch, "Juego en Pausa", 100, 150);
        font.draw(batch, "Toca para continuar", 100, 100);
    }

    @Override
    protected void manejarInput(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(juego);
        }
    }
}
