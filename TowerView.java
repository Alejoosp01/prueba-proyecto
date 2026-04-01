import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Se encarga de dibujar la torre.
 * Cada taza se dibuja como una U con grosor de pared proporcional
 * a su tamaño para que las tazas pequeñas sean visibles dentro.
 * Las tapas sueltas se dibujan como barras horizontales delgadas.
 * La columna izquierda muestra marcas de centímetro sin números.
 */
public class TowerView
{
    private ArrayList<Rectangle> shapes;

    /**
     * Crea la vista de la torre.
     */
    public TowerView()
    {
        shapes = new ArrayList<Rectangle>();
    }

    /**
     * Muestra la torre en el canvas.
     * Ordena por ancho de mayor a menor para que las grandes
     * rodeen visualmente a las pequeñas.
     * @param items     elementos de la torre
     * @param width     ancho de referencia en cm
     * @param maxHeight altura máxima en cm
     */
    public void show(Stack<StackItem> items, int width, int maxHeight) {
        hide();
    
        int baseX = 120;
        int baseY = 270;
        int scale = 18;
    
        dibujarRegleta(baseX, baseY, scale, maxHeight);
    
        ArrayList<StackItem> ordenados = new ArrayList<StackItem>(items);
        int[] nivelesBase = new int[ordenados.size()];
    
        for (int i = 0; i < ordenados.size(); i++) {
            StackItem item = ordenados.get(i);
            int itemW = item.getWidth();
            int itemH = item.getHeight();
            int maxApoyoY = 0;
    
            for (int j = 0; j < i; j++) {
                StackItem prevItem = ordenados.get(j);
                int prevW = prevItem.getWidth();
                int prevH = prevItem.getHeight();
                int prevBaseY = nivelesBase[j];
                int apoyoCalculado = 0;
                boolean prevEsCerrada = prevItem.isLiddedCup() || (prevItem.hasLid() && !prevItem.hasCup());
    
                if (prevEsCerrada) {
                    apoyoCalculado = prevBaseY + prevH;
                } else {
                    if (itemW <= prevW - 2) {
                        apoyoCalculado = prevBaseY + 1;
                    } else {
                        apoyoCalculado = prevBaseY + prevH;
                    }
                }
    
                if (apoyoCalculado > maxApoyoY) {
                    maxApoyoY = apoyoCalculado;
                }
            }
    
            nivelesBase[i] = maxApoyoY;
            int itemHeightPixels = itemH * scale;
            int itemWidthPixels  = itemW * scale;
            int moveH = baseX - 70 + (width * scale / 2) - (itemWidthPixels / 2);
            int moveV = baseY - (maxApoyoY * scale) - itemHeightPixels;
            String color = item.getColor();
    
            if (item.isLiddedCup()) {
                dibujarTazaTapada(moveH, moveV, itemWidthPixels, itemHeightPixels, color);
            } else if (item.hasCup()) {
                dibujarTaza(moveH, moveV, itemWidthPixels, itemHeightPixels, color);
            } else if (item.hasLid()) {
                int alturaTapa = itemHeightPixels > 0 ? itemHeightPixels : scale; 
                dibujarTapa(moveH, moveV, itemWidthPixels, alturaTapa, color);
            }
        }
    }


    /**
     * Oculta la torre del canvas.
     */
    public void hide()
    {
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).makeInvisible();
        }
        shapes.clear();
    }

    /**
     * Dibuja la regleta de centímetros a la izquierda de la torre.
     * Cada 5 cm la marca es más larga. Sin números.
     * @param baseX     posición horizontal base
     * @param baseY     posición vertical base
     * @param scale     píxeles por centímetro
     * @param maxHeight altura máxima en cm
     */
    private void dibujarRegleta(int baseX, int baseY, int scale, int maxHeight)
    {
        for (int cm = 1; cm <= maxHeight; cm++) {
            int tickW = (cm % 5 == 0) ? 8 : 4;
            int moveH = baseX - 70 - tickW - 4;
            int moveV = baseY - (cm * scale);

            crearBloque(moveH, moveV, tickW, 1, "black");
        }
    }

    /**
     * Dibuja una taza abierta como forma de U.
     * El grosor de pared es proporcional al ancho (w/8) para que
     * las tazas más pequeñas sean visibles dentro de las más grandes.
     * @param moveH  valor para moveHorizontal
     * @param moveV  valor para moveVertical del borde superior
     * @param w      ancho total en píxeles
     * @param h      altura total en píxeles
     * @param color  color de las paredes y base
     */
    private void dibujarTaza(int moveH, int moveV, int w, int h, String color)
    {
        int wall = Math.max(4, w / 8);

        // Pared izquierda
        crearBloque(moveH, moveV, wall, h, color);

        // Pared derecha
        crearBloque(moveH + w - wall, moveV, wall, h, color);

        // Base
        crearBloque(moveH, moveV + h - wall, w, wall, color);
    }

    /**
     * Dibuja una taza tapada: U cerrada con barra de tapa arriba.
     * @param moveH  valor para moveHorizontal
     * @param moveV  valor para moveVertical
     * @param w      ancho total en píxeles
     * @param h      altura total en píxeles
     * @param color  color de la taza y la tapa
     */
    private void dibujarTazaTapada(int moveH, int moveV, int w, int h, String color)
    {
        dibujarTaza(moveH, moveV, w, h, color);

        int wall = Math.max(4, w / 8);

        // Barra de tapa cerrando la parte superior de la U
        crearBloque(moveH, moveV, w, wall + 1, color);
    }

    /**
     * Dibuja una tapa suelta como barra horizontal delgada.
     * @param moveH  valor para moveHorizontal
     * @param moveV  valor para moveVertical
     * @param w      ancho total en píxeles
     * @param scale  altura de la barra en píxeles
     * @param color  color de la tapa
     */
    private void dibujarTapa(int moveH, int moveV, int w, int scale, String color)
    {
        crearBloque(moveH, moveV, w, scale, color);
    }

    /**
     * Crea un rectángulo y lo registra en la lista de shapes.
     * @param moveH  valor para moveHorizontal
     * @param moveV  valor para moveVertical
     * @param width  ancho en píxeles
     * @param height alto en píxeles
     * @param color  color del rectángulo
     */
    private void crearBloque(int moveH, int moveV, int width, int height, String color)
    {
        if (width <= 0 || height <= 0) return;

        Rectangle r = new Rectangle();
        r.changeSize(height, width);
        r.moveHorizontal(moveH);
        r.moveVertical(moveV);
        r.changeColor(color);
        r.makeVisible();
        shapes.add(r);
    }
}