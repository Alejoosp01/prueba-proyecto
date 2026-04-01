/**
 * Representa una taza de la torre.
 */
public class Cup
{
    private int number;
    private int height;
    private int width;
    private String color;
    private static final String[] COLORS = {
    "blue", "red", "green", "yellow", "magenta", "black"
    };


    /**
     * Crea una taza con su número.
     * @param number número de la taza
     */

    public Cup(int number)
    {
        this.number = number;
        this.height = (2 * number) - 1;
        this.width  = (2 * number) - 1;
        this.color  = COLORS[(number - 1) % COLORS.length];
    }

    /**
     * Retorna el número de la taza.
     * @return número de la taza
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * Retorna la altura de la taza.
     * @return altura de la taza
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Retorna el ancho de la taza.
     * @return ancho de la taza
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Retorna el color de la taza.
     * @return color de la taza
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Cambia el color de la taza.
     * @param color nuevo color
     */
    public void setColor(String color)
    {
        this.color = color;
    }
    
    /**
     * Retorna el color de fondo interior de la taza (siempre blanco
     * para mostrar el hueco donde caben tazas más pequeñas).
     * @return "white"
     */
    public String getInnerColor()
    {
        return "white";
    }
    
    /**
     * Retorna el grosor de las paredes en píxeles de escala.
     * @return 3
     */
    public int getWallThickness()
    {
        return 3;
    }
    }