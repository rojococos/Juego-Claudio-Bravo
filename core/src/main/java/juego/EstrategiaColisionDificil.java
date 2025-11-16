package juego;

import com.badlogic.gdx.math.Rectangle;

public class EstrategiaColisionDificil implements EstrategiaColisionBalon {
    @Override
    public void alColisionar(ArqueroClaudioBravo arquero, Rectangle ballArea, Rectangle arqueroHitbox) {
        float rightHalfStartX = arqueroHitbox.x + arqueroHitbox.width / 2f;
        float topHalfStartY = arqueroHitbox.y + arqueroHitbox.height / 2f;

        Rectangle topRightQuarter = new Rectangle(
                rightHalfStartX,
                topHalfStartY,
                arqueroHitbox.width / 2f,
                arqueroHitbox.height / 2f
        );

        if (ballArea.overlaps(topRightQuarter)) {
            arquero.atajar();
        } else {
            arquero.penalizarVida();
        }
    }
}
