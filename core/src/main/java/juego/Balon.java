package juego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Balon extends EntidadMovil implements Colisionable {

    private int tipo;
    private EstrategiaColisionBalon estrategia;

    public Balon(Texture textura, int tipo, float velocidad, EstrategiaColisionBalon estrategia) {
        super(textura, velocidad);
        this.tipo = tipo;
        this.estrategia = estrategia;
    }

    @Override
    public void mover(float delta) {
        area.y -= velocidad * delta;
    }

    @Override
    public void alColisionar(ArqueroClaudioBravo arquero, Rectangle ballArea, Rectangle arqueroHitbox) {
        estrategia.alColisionar(arquero, ballArea, arqueroHitbox);
    }

    @Override
    public int getTipo() {
        return tipo;
    }
}
