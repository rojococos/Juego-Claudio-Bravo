package juego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class BalonZigZag extends Balon {

    private final float MIN_TIEMPO_ZIG = 0.2f;
    private final float MAX_TIEMPO_ZAG = 0.6f;
    private final float MIN_VELOCIDAD_LATERAL = 200f;
    private final float MAX_VELOCIDAD_LATERAL = 500f;

    private float tiempoInicio;
    private float direccionLateral;
    private float tiempoDesdeUltimoCambio;
    private float tiempoProximoCambio;
    private float velocidadLateralActual;
    private final float MARGEN_LATERAL = 100f;
    private final float ANCHO_PANTALLA = 800f;

    public BalonZigZag(Texture textura, float velocidad) {
        super(textura, 6, velocidad, new EstrategiaColisionNormal());
        this.tiempoInicio = TimeUtils.nanoTime();
        this.tiempoDesdeUltimoCambio = 0f;
        this.direccionLateral = MathUtils.randomBoolean() ? 1f : -1f;
        reiniciarTemporizador();
    }

    private void reiniciarTemporizador() {
        tiempoProximoCambio = MathUtils.random(MIN_TIEMPO_ZIG, MAX_TIEMPO_ZAG);
        velocidadLateralActual = MathUtils.random(MIN_VELOCIDAD_LATERAL, MAX_VELOCIDAD_LATERAL);
    }

    @Override
    public void mover(float delta) {
        area.y -= velocidad * delta;

        tiempoDesdeUltimoCambio += delta;

        if (tiempoDesdeUltimoCambio >= tiempoProximoCambio) {
            direccionLateral *= -1f;
            cambiarDireccion();
        }

        area.x += velocidadLateralActual * direccionLateral * delta;

        boolean tocaIzq = area.x < MARGEN_LATERAL;
        boolean tocaDer = area.x > ANCHO_PANTALLA - area.width - MARGEN_LATERAL;

        if (tocaIzq || tocaDer) {
            if (tocaIzq) {
                area.x = MARGEN_LATERAL;
                if (direccionLateral < 0f) {
                    direccionLateral = 1f;
                    cambiarDireccion();
                }
            } else {
                area.x = ANCHO_PANTALLA - area.width - MARGEN_LATERAL;
                if (direccionLateral > 0f) {
                    direccionLateral = -1f;
                    cambiarDireccion();
                }
            }
        }
    }

    private void cambiarDireccion() {
        tiempoDesdeUltimoCambio = 0f;
        reiniciarTemporizador();
    }
}
