/**
 * Representa un elemento apilado en la torre.
 * Puede contener una taza, una tapa o ambas.
 */
public class StackItem
{
    private Cup cup;
    private Lid lid;

    /**
     * Crea un elemento apilado.
     * @param cup taza del elemento
     * @param lid tapa del elemento
     */
    public StackItem(Cup cup, Lid lid)
    {
        this.cup = cup;
        this.lid = lid;
    }

    /**
     * Retorna la taza del elemento.
     * @return taza asociada
     */
    public Cup getCup()
    {
        return cup;
    }

    /**
     * Retorna la tapa del elemento.
     * @return tapa asociada
     */
    public Lid getLid()
    {
        return lid;
    }

    /**
     * Cambia la taza del elemento.
     * @param cup nueva taza
     */
    public void setCup(Cup cup)
    {
        this.cup = cup;
    }

    /**
     * Cambia la tapa del elemento.
     * @param lid nueva tapa
     */
    public void setLid(Lid lid)
    {
        this.lid = lid;
    }

    /**
     * Indica si el elemento tiene taza.
     * @return true si tiene taza
     */
    public boolean hasCup()
    {
        return cup != null;
    }

    /**
     * Indica si el elemento tiene tapa.
     * @return true si tiene tapa
     */
    public boolean hasLid()
    {
        return lid != null;
    }

    /**
     * Indica si el elemento tiene solo taza.
     * @return true si solo tiene taza
     */
    public boolean hasOnlyCup()
    {
        return cup != null && lid == null;
    }

    /**
     * Indica si el elemento tiene solo tapa.
     * @return true si solo tiene tapa
     */
    public boolean hasOnlyLid()
    {
        return cup == null && lid != null;
    }

    /**
     * Indica si el elemento representa una taza tapada.
     * @return true si tiene taza y tapa
     */
    public boolean isLiddedCup()
    {
        return cup != null && lid != null;
    }

    /**
     * Retorna el número del elemento.
     * Si tiene taza usa el número de la taza, si no el de la tapa.
     * @return número del elemento
     */
    public int getNumber()
    {
        if (cup != null) {
            return cup.getNumber();
        }
        return lid.getNumber();
    }

    /**
     * Retorna el número de la taza.
     * @return número de la taza o -1 si no existe
     */
    public int getCupNumber()
    {
        if (cup != null) {
            return cup.getNumber();
        }
        return -1;
    }

    /**
     * Retorna el número de la tapa.
     * @return número de la tapa o -1 si no existe
     */
    public int getLidNumber()
    {
        if (lid != null) {
            return lid.getNumber();
        }
        return -1;
    }

    /**
     * Retorna la altura total del elemento.
     * @return altura total
     */
    public int getHeight()
    {
        if (cup != null) return cup.getHeight();
        if (lid != null) return lid.getHeight();
        return 0;
    }   

    /**
     * Retorna el ancho de dibujo del elemento.
     * @return ancho de dibujo
     */
    public int getWidth()
    {
        if (cup != null) {
            return cup.getWidth();
        }

        if (lid != null) {
            return lid.getWidth();
        }

        return 0;
    }

    /**
     * Retorna el color del elemento.
     * @return color del elemento
     */
    public String getColor()
    {
        if (cup != null) {
            return cup.getColor();
        }

        if (lid != null) {
            return lid.getColor();
        }

        return "black";
    }
}