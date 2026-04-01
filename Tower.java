import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 * Clase principal que administra la torre de objetos apilables.
 */
public class Tower
{
    private Stack<StackItem> items;
    private boolean ok;
    private boolean visible;
    private int width;
    private int maxHeight;
    private TowerView view;

    /**
     * Crea una torre vacía.
     * @param width ancho de referencia de la torre
     * @param maxHeight altura máxima permitida
     */
    public Tower(int width, int maxHeight)
    {
        this.items     = new Stack<StackItem>();
        this.ok        = true;
        this.visible   = false;
        this.width     = width;
        this.maxHeight = maxHeight;
        this.view      = new TowerView();
    }

    /**
     * Crea una torre precargada con n tazas numeradas 1 a cups.
     * No incluye tapas. Requisito 10 (Ciclo 2).
     * @param cups número de tazas a crear
     */
    public Tower(int cups)
    {
        this(2 * cups - 1, cups * cups);

        for (int i = 1; i <= cups; i++) {
            pushCup(i);
        }
    }

    /**
     * Agrega una taza a la torre.
     * @param number número de la taza
     */
    public void pushCup(int number)
    {
        if (!isValidNumber(number) || containsCup(number)) {
            ok = false;
            showError("Cannot push cup " + number + ".");
            return;
        }

        items.push(new StackItem(new Cup(number), null));

        if (!fitsInTower()) {
            items.pop();
            ok = false;
            showError("Cup " + number + " exceeds the tower height.");
            return;
        }

        ok = true;
        refreshView();
    }

    /**
     * Elimina la taza ubicada arriba.
     */
    public void popCup()
    {
        if (items.isEmpty() || !items.peek().hasCup()) {
            ok = false;
            showError("Cannot pop cup.");
            return;
        }

        items.pop();
        ok = true;
        refreshView();
    }

    /**
     * Elimina una taza específica.
     * @param number número de la taza
     */
    public void removeCup(int number)
    {
        ok = removeItemByCupNumber(number);

        if (!ok) showError("Cup " + number + " not found.");

        refreshView();
    }

    /**
     * Agrega una tapa a la torre.
     * @param number número de la tapa
     */
    public void pushLid(int number)
    {
        if (!isValidNumber(number) || containsLid(number)) {
            ok = false;
            showError("Cannot push lid " + number + ".");
            return;
        }

        items.push(new StackItem(null, new Lid(number)));

        if (!fitsInTower()) {
            items.pop();
            ok = false;
            showError("Lid " + number + " exceeds the tower height.");
            return;
        }

        ok = true;
        refreshView();
    }

    /**
     * Elimina la tapa ubicada arriba.
     */
    public void popLid()
    {
        if (items.isEmpty() || !items.peek().hasLid()) {
            ok = false;
            showError("Cannot pop lid.");
            return;
        }

        items.pop();
        ok = true;
        refreshView();
    }

    /**
     * Elimina una tapa específica.
     * @param number número de la tapa
     */
    public void removeLid(int number)
    {
        ok = removeItemByLidNumber(number);

        if (!ok) showError("Lid " + number + " not found.");

        refreshView();
    }

    /**
     * Ordena la torre de mayor a menor número.
     */
    public void orderTower()
    {
        ArrayList<StackItem> ordered = new ArrayList<StackItem>(items);

        Collections.sort(ordered, new Comparator<StackItem>() {
            public int compare(StackItem first, StackItem second) {
                return second.getNumber() - first.getNumber();
            }
        });

        rebuildStackFromList(ordered);
        ok = fitsInTower();
        refreshView();
    }

    /**
     * Invierte el orden actual de la torre.
     */
    public void reverseTower()
    {
        Stack<StackItem> reversed = new Stack<StackItem>();

        while (!items.isEmpty()) {
            reversed.push(items.pop());
        }

        items = reversed;
        ok    = true;
        refreshView();
    }

    /**
     * Intercambia la posición de dos objetos en la torre.
     * Cada objeto se identifica con {"cup","4"} o {"lid","4"}.
     * Falla si alguno no existe o ambos están en el mismo slot.
     * Requisito 11 (Ciclo 2).
     * @param o1 identificador del primer objeto
     * @param o2 identificador del segundo objeto
     */
    public void swap(String[] o1, String[] o2)
    {
        ArrayList<StackItem> list = new ArrayList<StackItem>(items);

        int idx1 = findItemIndex(list, o1);
        int idx2 = findItemIndex(list, o2);

        if (idx1 == -1 || idx2 == -1) {
            ok = false;
            showError("One of the objects was not found.");
            return;
        }

        if (idx1 == idx2) {
            ok = false;
            showError("Both objects are in the same slot.");
            return;
        }

        StackItem temp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, temp);

        rebuildStackFromList(list);
        ok = true;
        refreshView();
    }

    /**
     * Fusiona cada taza sola con su tapa sola del mismo número.
     * La tapa queda absorbida: la altura baja 1 cm por par fusionado.
     * Requisito 12 (Ciclo 2).
     */
    public void cover()
    {
        ArrayList<StackItem> list  = new ArrayList<StackItem>(items);
        boolean              found = true;

        while (found) {
            found = false;
            int i = 0;

            while (i < list.size() && !found) {
                if (list.get(i).hasOnlyCup()) {
                    int cupNum = list.get(i).getCupNumber();
                    int j = 0;

                    while (j < list.size() && !found) {
                        if (j != i
                                && list.get(j).hasOnlyLid()
                                && list.get(j).getLidNumber() == cupNum) {
                            list.get(i).setLid(list.get(j).getLid());
                            list.remove(j);
                            found = true;
                        }
                        j++;
                    }
                }
                i++;
            }
        }

        rebuildStackFromList(list);
        ok = true;
        refreshView();
    }

    /**
     * Retorna la altura total de la torre.
     * @return altura total
     */
    public int height()
    {
        int total = 0;

        for (StackItem item : items) {
            total += item.getHeight();
        }

        return total;
    }

    /**
     * Retorna los números de las tazas tapadas.
     * @return arreglo con números de tazas tapadas
     */
    public int[] liddedCups()
    {
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        for (StackItem item : items) {
            if (item.isLiddedCup()) {
                numbers.add(Integer.valueOf(item.getCupNumber()));
            }
        }

        Collections.sort(numbers);

        int[] result = new int[numbers.size()];

        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i).intValue();
        }

        return result;
    }

    /**
     * Retorna la información de los elementos apilados.
     * @return matriz con tipo y número
     */
    public String[][] stackingItems()
    {
        ArrayList<String[]> info = new ArrayList<String[]>();

        for (StackItem item : items) {
            if (item.hasCup()) {
                info.add(new String[]{"cup", String.valueOf(item.getCupNumber())});
            }

            if (item.hasLid()) {
                info.add(new String[]{"lid", String.valueOf(item.getLidNumber())});
            }
        }

        String[][] result = new String[info.size()][2];

        for (int i = 0; i < info.size(); i++) {
            result[i][0] = info.get(i)[0];
            result[i][1] = info.get(i)[1];
        }

        return result;
    }

    /**
     * Busca un intercambio que al ejecutarse y llamar cover()
     * reduzca la altura de la torre en al menos 1 cm.
     * Retorna null si no existe ningún intercambio útil.
     * Requisito 13 (Ciclo 2).
     * @return {{"tipo","n"},{"tipo","n"}} o null
     */
    public String[][] swapToReduce()
    {
        ArrayList<StackItem> list = new ArrayList<StackItem>(items);

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).hasOnlyCup()) continue;

            int cupNumber = list.get(i).getCupNumber();

            for (int j = 0; j < list.size(); j++) {
                if (j == i || !list.get(j).hasOnlyLid()) continue;
                if (list.get(j).getLidNumber() != cupNumber) continue;

                if (j == i + 1) {
                    return new String[][]{
                        {"cup", String.valueOf(cupNumber)},
                        {"lid", String.valueOf(cupNumber)}
                    };
                }

                int aboveCup = i + 1;

                if (aboveCup < list.size() && aboveCup != j) {
                    return new String[][]{
                        itemIdentifier(list.get(aboveCup)),
                        {"lid", String.valueOf(cupNumber)}
                    };
                }

                return new String[][]{
                    {"cup", String.valueOf(cupNumber)},
                    {"lid", String.valueOf(cupNumber)}
                };
            }
        }

        return null;
    }

    /**
     * Hace visible la torre.
     */
    public void makeVisible()
    {
        visible = true;
        refreshView();
    }

    /**
     * Hace invisible la torre.
     */
    public void makeInvisible()
    {
        visible = false;
        view.hide();
    }

    /**
     * Cierra la torre visualmente.
     */
    public void exit()
    {
        visible = false;
        view.hide();
    }

    /**
     * Informa si la última operación fue exitosa.
     * @return true si salió bien, false si falló
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Retorna una copia de los elementos de la torre.
     * @return lista con los elementos actuales
     */
    public ArrayList<StackItem> getItems()
    {
        return new ArrayList<StackItem>(items);
    }

    /**
     * Valida si el número es correcto.
     * @param number número a validar
     * @return true si es positivo
     */
    private boolean isValidNumber(int number)
    {
        return number > 0;
    }

    /**
     * Verifica si ya existe una taza.
     * @param number número de la taza
     * @return true si ya existe
     */
    private boolean containsCup(int number)
    {
        for (StackItem item : items) {
            if (item.hasCup() && item.getCupNumber() == number) return true;
        }
        return false;
    }

    /**
     * Verifica si ya existe una tapa.
     * @param number número de la tapa
     * @return true si ya existe
     */
    private boolean containsLid(int number)
    {
        for (StackItem item : items) {
            if (item.hasLid() && item.getLidNumber() == number) return true;
        }
        return false;
    }

    /**
     * Elimina un elemento por número de taza.
     * @param number número de la taza
     * @return true si se eliminó
     */
    private boolean removeItemByCupNumber(int number)
    {
        Stack<StackItem> aux     = new Stack<StackItem>();
        boolean          removed = false;

        while (!items.isEmpty()) {
            StackItem top = items.pop();

            if (!removed && top.hasCup() && top.getCupNumber() == number) {
                removed = true;
            } else {
                aux.push(top);
            }
        }

        while (!aux.isEmpty()) items.push(aux.pop());

        return removed;
    }

    /**
     * Elimina un elemento por número de tapa.
     * @param number número de la tapa
     * @return true si se eliminó
     */
    private boolean removeItemByLidNumber(int number)
    {
        Stack<StackItem> aux     = new Stack<StackItem>();
        boolean          removed = false;

        while (!items.isEmpty()) {
            StackItem top = items.pop();

            if (!removed && top.hasLid() && top.getLidNumber() == number) {
                removed = true;
            } else {
                aux.push(top);
            }
        }

        while (!aux.isEmpty()) items.push(aux.pop());

        return removed;
    }

    /**
     * Verifica si la torre cabe en la altura máxima.
     * @return true si cabe
     */
    private boolean fitsInTower()
    {
        return height() <= maxHeight;
    }

    /**
     * Reconstruye la pila desde una lista.
     * @param ordered lista ordenada
     */
    private void rebuildStackFromList(ArrayList<StackItem> ordered)
    {
        items.clear();

        for (int i = 0; i < ordered.size(); i++) {
            items.push(ordered.get(i));
        }
    }

    /**
     * Retorna el índice del slot que contiene el objeto identificado.
     * Usado por swap() para encontrar las posiciones a intercambiar.
     * @param list copia actual de la torre
     * @param id   arreglo {tipo, número}
     * @return índice del slot, o -1 si no se encontró
     */
    private int findItemIndex(ArrayList<StackItem> list, String[] id)
    {
        if (id == null || id.length < 2) return -1;

        String type   = id[0];
        int    number = Integer.parseInt(id[1]);

        for (int i = 0; i < list.size(); i++) {
            StackItem item = list.get(i);

            if ("cup".equals(type) && item.hasCup()
                    && item.getCupNumber() == number) return i;

            if ("lid".equals(type) && item.hasLid()
                    && item.getLidNumber() == number) return i;
        }

        return -1;
    }

    /**
     * Retorna el identificador del objeto dominante en un slot.
     * Las tazas tienen prioridad sobre las tapas.
     * @param item slot a identificar
     * @return arreglo {tipo, número}
     */
    private String[] itemIdentifier(StackItem item)
    {
        if (item.hasCup()) {
            return new String[]{"cup", String.valueOf(item.getCupNumber())};
        }
        return new String[]{"lid", String.valueOf(item.getLidNumber())};
    }

    /**
     * Actualiza la vista si la torre está visible.
     */
    private void refreshView()
    {
        if (visible) view.show(items, width, maxHeight);
    }

    /**
     * Muestra un mensaje de error con JOptionPane solo si la torre está visible.
     * En modo invisible el error se ignora silenciosamente.
     * @param message texto del error a mostrar
     */
    private void showError(String message)
    {
        if (visible) {
            JOptionPane.showMessageDialog(null, message,
                "Tower error", JOptionPane.ERROR_MESSAGE);
        }
    }
}