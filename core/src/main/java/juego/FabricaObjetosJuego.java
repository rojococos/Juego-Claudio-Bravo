package juego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public interface FabricaObjetosJuego {
    Balon crearBalonNormal(Texture textura, float velocidad);
    Balon crearBalonDificil(Texture textura, float velocidad);
    BalonZigZag crearBalonZigZag(Texture textura, float velocidad);
    Premio crearPremioVida(Texture textura, Sound sonido, float velocidad);
    Premio crearPremioPuntos(Texture textura, Sound sonido, float velocidad);
}
