/**
 * Representa una tapa de la torre.
 */
public class Lid
{
    private int number;
    private int height;
    private int width;
    private String color;
    private static final String[] COLORS = {
    "blue", "red", "green", "yellow", "magenta", "black"
    };

    /**
     * Crea una tapa con su número.
     * @param number número de la tapa
     */
    public Lid(int number)
    {
        this.number = number;
        this.height = 1;
        this.width  = (2 * number) - 1;
        this.color  = COLORS[(number - 1) % COLORS.length];
    }

    /**
     * Retorna el número de la tapa.
     * @return número de la tapa
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * Retorna la altura de la tapa.
     * @return altura de la tapa
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Retorna el ancho de la tapa.
     * @return ancho de la tapa
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Retorna el color de la tapa.
     * @return color de la tapa
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Cambia el color de la tapa.
     * @param color nuevo color
     */
    public void setColor(String color)
    {
        this.color = color;
    }
}