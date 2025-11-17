package juego;

import com.badlogic.gdx.math.Rectangle;

public interface EstrategiaColisionBalon {
    void alColisionar(ArqueroClaudioBravo arquero, Rectangle ballArea, Rectangle arqueroHitbox);
}
