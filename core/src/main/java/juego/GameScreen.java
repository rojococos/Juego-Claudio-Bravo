package juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen extends PantallaBase {

    private ArqueroClaudioBravo arquero;
    private SistemaDeJuego sistema;

    public GameScreen(GameMenu game) {
        super(game);

        Sound golSound = Gdx.audio.newSound(Gdx.files.internal("gol.wav"));
        Sound premioSound = Gdx.audio.newSound(Gdx.files.internal("premio.wav"));

        arquero = new ArqueroClaudioBravo(
                new Texture(Gdx.files.internal("claudio_bravo.png"))
        );

        Texture balonNormal = new Texture(Gdx.files.internal("balon_normal.png"));
        Texture balonDificil = new Texture(Gdx.files.internal("balon_dificil.png"));
        Texture premioVida = new Texture(Gdx.files.internal("vida_extra.png"));
        Texture premioPuntos = new Texture(Gdx.files.internal("puntos_extra.png"));

        Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("hinchada.mp3"));

        sistema = new SistemaDeJuego(
                balonNormal, balonDificil, premioVida, premioPuntos,
                golSound, premioSound, musicaFondo
        );

        arquero.crear(400 - 233/2f, 20, 233, 113);
        sistema.crear();
    }

    @Override
    protected void dibujarContenido(float delta) {
        font.draw(batch, "Atajadas: " + arquero.getAtajadas(), 5, 475);
        font.draw(batch, "Vidas: " + arquero.getVidas(), 200, 475);
        font.draw(batch, "Goles: " + arquero.getGolesRecibidos(), 400, 475);
        font.draw(batch, "HighScore: " + game.getHigherScore(), 600, 475);

        arquero.mover(delta);

        if (!sistema.actualizarMovimiento(arquero)) {
            if (game.getHigherScore() < arquero.getAtajadas()) {
                game.setHigherScore(arquero.getAtajadas());
            }
            game.setScreen(new GameOverScreen(game));
        }

        arquero.dibujar(batch);
        sistema.actualizarDibujo(batch);
    }

    @Override
    protected void manejarInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause();
        }
    }

    @Override
    public void pause() {
        sistema.pausar();
        game.setScreen(new PausaScreen(game, this));
    }

    @Override
    public void dispose() {
        arquero.destruir();
        sistema.destruir();
    }
}
