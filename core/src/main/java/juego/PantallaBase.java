package juego;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class PantallaBase implements Screen {

    protected final GameMenu game;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected OrthographicCamera camera;

    public PantallaBase(GameMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 480);
    }

    protected float getColorR() {
        return 0f;
    }

    protected float getColorG() {
        return 0f;
    }

    protected float getColorB() {
        return 0.2f;
    }

    protected float getColorA() {
        return 1f;
    }

    protected abstract void dibujarContenido(float delta);

    protected void manejarInput(float delta) {
    }

    @Override
    public final void render(float delta) {
        ScreenUtils.clear(getColorR(), getColorG(), getColorB(), getColorA());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        dibujarContenido(delta);
        batch.end();
        manejarInput(delta);
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
