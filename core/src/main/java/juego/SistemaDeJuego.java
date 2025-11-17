package juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SistemaDeJuego {

    private Array<Colisionable> objetos;
    private Array<Rectangle> posiciones;
    private long ultimoTiempoCreacion;
    private long tiempoInicioJuego;

    private long idSonidoGolActual = 0;
    private long idSonidoPremioActual = 0;

    private final float VELOCIDAD_BASE = 200f;
    private final float INCREMENTO_POR_SEGUNDO = 5f;
    private final float VELOCIDAD_MAXIMA = 500f;

    private final float TIEMPO_BASE_CREACION = 0.8f;
    private final float DECREMENTO_POR_SEGUNDO = 0.03f;
    private final float TIEMPO_MINIMO_CREACION = 0.2f;

    private final float AUMENTO_PROB_PREMIO_POR_SEGUNDO = 0.5f;
    private final float PROB_PREMIO_BASE = 16f;
    private final float MAX_PROB_PREMIO = 40f;

    private final float PROB_BALON_NORMAL = 0.45f;
    private final float PROB_BALON_DIFICIL = 0.25f;
    private final float PROB_BALON_ZIGZAG = 0.30f;

    private final float MARGEN = 100f;
    private final float ANCHO = 800f;

    private Texture textBalonNormal;
    private Texture textBalonDificil;
    private Texture textPremioVida;
    private Texture textPremioPuntos;
    private Sound sonidoGol;
    private Sound sonidoPremio;
    private Music musica;

    private FabricaObjetosJuego fabrica;

    public SistemaDeJuego(Texture bn, Texture bd, Texture pv, Texture pp,
                          Sound sg, Sound sp, Music mu) {
        textBalonNormal = bn;
        textBalonDificil = bd;
        textPremioVida = pv;
        textPremioPuntos = pp;
        sonidoGol = sg;
        sonidoPremio = sp;
        musica = mu;
        fabrica = new FabricaObjetosJuegoBasica();
    }

    public void crear() {
        objetos = new Array<>();
        posiciones = new Array<>();
        tiempoInicioJuego = TimeUtils.nanoTime();
        crearObjeto();
        musica.setLooping(true);
        musica.setVolume(0.4f);
        musica.play();
    }

    private void reproducirGol() {
        if (idSonidoGolActual != 0) sonidoGol.stop(idSonidoGolActual);
        idSonidoGolActual = sonidoGol.play(0.85f);
    }

    private void reproducirPremio() {
        if (idSonidoPremioActual != 0) sonidoPremio.stop(idSonidoPremioActual);
        idSonidoPremioActual = sonidoPremio.play(0.95f);
    }

    private void crearObjeto() {
        Rectangle pos = new Rectangle();
        pos.width = 64;
        pos.height = 64;
        pos.x = MathUtils.random(MARGEN, ANCHO - pos.width - MARGEN);
        pos.y = 480;
        posiciones.add(pos);

        float t = (TimeUtils.nanoTime() - tiempoInicioJuego) / 1_000_000_000f;
        float vel = Math.min(VELOCIDAD_BASE + t * INCREMENTO_POR_SEGUNDO, VELOCIDAD_MAXIMA);

        float probPremio = Math.min(PROB_PREMIO_BASE + t * AUMENTO_PROB_PREMIO_POR_SEGUNDO, MAX_PROB_PREMIO);
        float r = MathUtils.random(0, 100);

        Colisionable obj;
        float velFinal;

        if (r <= probPremio) {
            velFinal = vel * 0.7f;
            if (r <= probPremio / 2f) {
                obj = fabrica.crearPremioVida(textPremioVida, sonidoPremio, velFinal);
            } else {
                obj = fabrica.crearPremioPuntos(textPremioPuntos, sonidoPremio, velFinal);
            }
        } else {
            float probBalones = 100 - probPremio;
            float limNormal = probBalones * PROB_BALON_NORMAL;
            float limDificil = limNormal + probBalones * PROB_BALON_DIFICIL;
            float rb = MathUtils.random(0, probBalones);

            if (rb <= limNormal) {
                velFinal = vel;
                obj = fabrica.crearBalonNormal(textBalonNormal, velFinal);
            } else if (rb <= limDificil) {
                velFinal = vel * 1.5f;
                obj = fabrica.crearBalonDificil(textBalonDificil, velFinal);
            } else {
                velFinal = vel * 1.2f;
                obj = fabrica.crearBalonZigZag(textBalonNormal, velFinal);
            }
        }

        ((EntidadMovil)obj).crear(pos.x, pos.y, pos.width, pos.height);
        objetos.add(obj);
        ultimoTiempoCreacion = TimeUtils.nanoTime();
    }

    public boolean actualizarMovimiento(ArqueroClaudioBravo arq) {
        float t = (TimeUtils.nanoTime() - tiempoInicioJuego) / 1_000_000_000f;
        float tc = Math.max(TIEMPO_BASE_CREACION - t * DECREMENTO_POR_SEGUNDO, TIEMPO_MINIMO_CREACION);
        long tcNanos = (long)(tc * 1_000_000_000L);

        if (TimeUtils.nanoTime() - ultimoTiempoCreacion > tcNanos) {
            crearObjeto();
        }

        for (int i = objetos.size - 1; i >= 0; i--) {
            Colisionable obj = objetos.get(i);
            EntidadMovil ent = (EntidadMovil)obj;
            ent.mover(Gdx.graphics.getDeltaTime());

            if (ent.getArea().y + 64 < 0) {
                if (obj instanceof Balon) {
                    arq.registrarGol();
                    reproducirGol();
                }
                objetos.removeIndex(i);
                posiciones.removeIndex(i);
                if (arq.getVidas() <= 0) return false;
                continue;
            }

            if (ent.getArea().overlaps(arq.getHitbox())) {
                obj.alColisionar(arq, ent.getArea(), arq.getHitbox());
                if (obj instanceof Premio) reproducirPremio();
                objetos.removeIndex(i);
                posiciones.removeIndex(i);
                if (arq.getVidas() <= 0) return false;
            }
        }

        return arq.getVidas() > 0;
    }

    public void actualizarDibujo(SpriteBatch batch) {
        for (Colisionable o : objetos) ((EntidadMovil)o).dibujar(batch);
    }

    public void destruir() {
        for (Colisionable o : objetos) ((EntidadMovil)o).dispose();
        textBalonNormal.dispose();
        textBalonDificil.dispose();
        textPremioVida.dispose();
        textPremioPuntos.dispose();
        sonidoGol.dispose();
        sonidoPremio.dispose();
        musica.dispose();
    }

    public void pausar() { musica.stop(); }
    public void continuar() { musica.play(); }
}
