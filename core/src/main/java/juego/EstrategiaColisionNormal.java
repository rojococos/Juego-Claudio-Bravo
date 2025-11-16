package juego;

import com.badlogic.gdx.math.Rectangle;

public class EstrategiaColisionNormal implements EstrategiaColisionBalon {
    @Override
    public void alColisionar(ArqueroClaudioBravo arquero, Rectangle ballArea, Rectangle arqueroHitbox) {
        arquero.atajar();
    }
}
