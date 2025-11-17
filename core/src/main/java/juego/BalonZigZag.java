package juego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class BalonZigZag extends Balon {

    private final float MIN_TIEMPO_ZIG = 0.2f;
    private final float MAX_TIEMPO_ZAG = 0.6f;
    private final float MIN_VELOCIDAD_LATERAL = 200f;
    private final float MAX_VELOCIDAD_LATERAL = 500f;

    private float tiempoDesdeUltimoCambio;
    private float tiempoProximoCambio;
    private float direccionLateral;
    private float velocidadLateralActual;

    private final float MARGEN = 100f;
    private final float ANCHO = 800f;

    public BalonZigZag(Texture textura, float velocidad) {
        super(textura, 6, velocidad, new EstrategiaColisionNormal());
        direccionLateral = MathUtils.randomBoolean() ? 1 : -1;
        reiniciar();
    }

    private void reiniciar() {
        tiempoProximoCambio = MathUtils.random(MIN_TIEMPO_ZIG, MAX_TIEMPO_ZAG);
        velocidadLateralActual = MathUtils.random(MIN_VELOCIDAD_LATERAL, MAX_VELOCIDAD_LATERAL);
        tiempoDesdeUltimoCambio = 0;
    }

    @Override
    public void mover(float delta) {
        area.y -= velocidad * delta;

        tiempoDesdeUltimoCambio += delta;
        if (tiempoDesdeUltimoCambio >= tiempoProximoCambio) {
            direccionLateral *= -1;
            reiniciar();
        }

        area.x += velocidadLateralActual * direccionLateral * delta;

        if (area.x < MARGEN) {
            area.x = MARGEN;
            direccionLateral = 1;
            reiniciar();
        }
        if (area.x > ANCHO - area.width - MARGEN) {
            area.x = ANCHO - area.width - MARGEN;
            direccionLateral = -1;
            reiniciar();
        }
    }
}
