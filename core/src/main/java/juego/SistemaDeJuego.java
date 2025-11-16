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

    private final float MARGEN_LATERAL = 100f;
    private final float ANCHO_PANTALLA = 800f;

    private Texture balonNormal;
    private Texture balonDificil;
    private Texture premioVida;
    private Texture premioPuntos;
    private Sound sonidoGol;
    private Sound sonidoPremio;
    private Music musicaFondo;

    private FabricaObjetosJuego fabrica;

    public SistemaDeJuego(Texture balonNormal, Texture balonDificil,
                          Texture premioVida, Texture premioPuntos,
                          Sound golSound, Sound premioSound,
                          Music musica) {
        this.balonNormal = balonNormal;
        this.balonDificil = balonDificil;
        this.premioVida = premioVida;
        this.premioPuntos = premioPuntos;
        this.sonidoGol = golSound;
        this.sonidoPremio = premioSound;
        this.musicaFondo = musica;
        this.fabrica = new FabricaObjetosJuegoBasica();
    }

    public void crear() {
        objetos = new Array<>();
        posiciones = new Array<>();

        tiempoInicioJuego = TimeUtils.nanoTime();

        crearObjeto();
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.4f);
        musicaFondo.play();
    }

    private void reproducirSonidoGol() {
        if (idSonidoGolActual != 0) sonidoGol.stop(idSonidoGolActual);
        idSonidoGolActual = sonidoGol.play(0.85f);
    }

    private void reproducirSonidoPremio() {
        if (idSonidoPremioActual != 0) sonidoPremio.stop(idSonidoPremioActual);
        idSonidoPremioActual = sonidoPremio.play(0.95f);
    }

    private void crearObjeto() {
        Rectangle posicion = new Rectangle();
        posicion.width = 64f;
        posicion.height = 64f;
        posicion.x = MathUtils.random(MARGEN_LATERAL, ANCHO_PANTALLA - posicion.width - MARGEN_LATERAL);
        posicion.y = 480f;
        posiciones.add(posicion);

        float tiempoTranscurrido = (TimeUtils.nanoTime() - tiempoInicioJuego) / 1_000_000_000f;
        float velocidadAumentada = VELOCIDAD_BASE + tiempoTranscurrido * INCREMENTO_POR_SEGUNDO;
        float velocidadBaseActual = Math.min(velocidadAumentada, VELOCIDAD_MAXIMA);
        float velocidadFinal;

        float aumentoPremio = tiempoTranscurrido * AUMENTO_PROB_PREMIO_POR_SEGUNDO;
        float probTotalPremios = Math.min(PROB_PREMIO_BASE + aumentoPremio, MAX_PROB_PREMIO);

        float randomRange = MathUtils.random(0f, 100f);
        Colisionable objeto;

        final float MULTIPLICADOR_ESPECIAL = 1.2f;

        if (randomRange <= probTotalPremios) {
            float probVida = probTotalPremios / 2f;
            velocidadFinal = velocidadBaseActual * 0.7f;

            if (randomRange <= probVida) {
                objeto = fabrica.crearPremioVida(premioVida, sonidoPremio, velocidadFinal);
            } else {
                objeto = fabrica.crearPremioPuntos(premioPuntos, sonidoPremio, velocidadFinal);
            }
        } else {
            float probBalon = 100f - probTotalPremios;

            float acumuladoNormal = probBalon * PROB_BALON_NORMAL;
            float acumuladoDificil = acumuladoNormal + probBalon * PROB_BALON_DIFICIL;

            float balonRange = MathUtils.random(0f, probBalon);

            if (balonRange <= acumuladoNormal) {
                velocidadFinal = velocidadBaseActual;
                objeto = fabrica.crearBalonNormal(balonNormal, velocidadFinal);
            } else if (balonRange <= acumuladoDificil) {
                velocidadFinal = velocidadBaseActual * 1.5f;
                objeto = fabrica.crearBalonDificil(balonDificil, velocidadFinal);
            } else {
                velocidadFinal = velocidadBaseActual * MULTIPLICADOR_ESPECIAL;
                objeto = fabrica.crearBalonZigZag(balonNormal, velocidadFinal);
            }
        }

        ((EntidadMovil) objeto).crear(posicion.x, posicion.y, posicion.width, posicion.height);
        objetos.add(objeto);
        ultimoTiempoCreacion = TimeUtils.nanoTime();
    }

    public boolean actualizarMovimiento(ArqueroClaudioBravo arquero) {
        float tiempoTranscurrido = (TimeUtils.nanoTime() - tiempoInicioJuego) / 1_000_000_000f;

        float tiempoEntreCreacionAumentado = TIEMPO_BASE_CREACION - tiempoTranscurrido * DECREMENTO_POR_SEGUNDO;
        float tiempoEntreCreacion = Math.max(tiempoEntreCreacionAumentado, TIEMPO_MINIMO_CREACION);
        long tiempoEntreCreacionNanos = (long) (tiempoEntreCreacion * 1_000_000_000L);

        if (TimeUtils.nanoTime() - ultimoTiempoCreacion > tiempoEntreCreacionNanos) {
            crearObjeto();
        }

        for (int i = objetos.size - 1; i >= 0; i--) {
            Colisionable objeto = objetos.get(i);
            EntidadMovil entidad = (EntidadMovil) objeto;

            entidad.mover(Gdx.graphics.getDeltaTime());

            if (entidad.getArea().y + 64f < 0f) {
                if (objeto instanceof Balon) {
                    arquero.registrarGol();
                    reproducirSonidoGol();
                }
                objetos.removeIndex(i);
                posiciones.removeIndex(i);
                if (arquero.getVidas() <= 0) return false;
                continue;
            }

            if (entidad.getArea().overlaps(arquero.getHitbox())) {
                objeto.alColisionar(arquero, entidad.getArea(), arquero.getHitbox());
                if (objeto instanceof Premio) reproducirSonidoPremio();

                objetos.removeIndex(i);
                posiciones.removeIndex(i);
                if (arquero.getVidas() <= 0) return false;
            }
        }

        return arquero.getVidas() > 0;
    }

    public void actualizarDibujo(SpriteBatch batch) {
        for (Colisionable objeto : objetos) {
            ((EntidadMovil) objeto).dibujar(batch);
        }
    }

    public void destruir() {
        for (Colisionable objeto : objetos) {
            ((EntidadMovil) objeto).dispose();
        }
        objetos.clear();
        posiciones.clear();

        balonNormal.dispose();
        balonDificil.dispose();
        premioVida.dispose();
        premioPuntos.dispose();

        sonidoGol.dispose();
        sonidoPremio.dispose();
        musicaFondo.dispose();
    }

    public void pausar() {
        musicaFondo.stop();
    }

    public void continuar() {
        musicaFondo.play();
    }
}
