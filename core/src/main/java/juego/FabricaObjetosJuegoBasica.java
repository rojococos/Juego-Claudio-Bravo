package juego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class FabricaObjetosJuegoBasica implements FabricaObjetosJuego {

    @Override
    public Balon crearBalonNormal(Texture textura, float velocidad) {
        return new Balon(textura, 1, velocidad, new EstrategiaColisionNormal());
    }

    @Override
    public Balon crearBalonDificil(Texture textura, float velocidad) {
        return new Balon(textura, 2, velocidad, new EstrategiaColisionDificil());
    }

    @Override
    public BalonZigZag crearBalonZigZag(Texture textura, float velocidad) {
        return new BalonZigZag(textura, velocidad);
    }

    @Override
    public Premio crearPremioVida(Texture textura, Sound sonido, float velocidad) {
        return new Premio(textura, sonido, 3, velocidad);
    }

    @Override
    public Premio crearPremioPuntos(Texture textura, Sound sonido, float velocidad) {
        return new Premio(textura, sonido, 4, velocidad);
    }
}
