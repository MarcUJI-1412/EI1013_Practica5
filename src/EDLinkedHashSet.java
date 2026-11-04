import java.util.*;

public class EDLinkedHashSet<T> implements Set<T> {

    private class Node {
        T data;
        Node hnext = null;   // siguiente nodo en la cadena de la cubeta de la tabla
        Node prev = null;    // anterior en la lista de orden de inserción
        Node next = null;    // siguiente en la lista de orden de inserción
    }

    static final private int DEFAULT_CAPACITY = 11;
    static final private double DEFAULT_LOAD_FACTOR = 2.0;

    Node[] table;
    Node header;
    int size = 0;
    int rehashThreshold;
    double loadFactor;

    // ------------------------------------------------------------------
    // Métodos que se dan implementados
    // ------------------------------------------------------------------

    public EDLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /** Posición de la tabla en la que dispersa item (admite null). */
    private int hash(Object item) {
        if (item == null)
            return 0;
        return (item.hashCode() & Integer.MAX_VALUE) % table.length;
    }

    /** Compara dos elementos teniendo en cuenta que alguno puede ser null. */
    private boolean equalsNull(Object a, Object b) {
        if (a == null)
            return b == null;
        return a.equals(b);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Recorre directamente la lista de orden desde el nodo cabecera. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node n = header.next;
        while (n != header) {
            sb.append(n.data);
            n = n.next;
            if (n != header)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (T e : this)
            result[i++] = e;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size)
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (T e : this)
            a[i++] = (E) e;
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T e : c)
            if (add(e))
                changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c)
            if (remove(e))
                changed = true;
        return changed;
    }

    // ------------------------------------------------------------------
    // Ejercicio 1
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public EDLinkedHashSet(int capacity, double loadFactor) {
        if (capacity <= 0 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }

        table = new EDLinkedHashSet.Node[capacity];
        this.loadFactor = loadFactor;
        header = new Node();
        header.next = header;
        header.prev = header;

        rehashThreshold = (int) (this.loadFactor * capacity);
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // ------------------------------------------------------------------

    private void linkLast(Node n) {
        header.prev.next = n;
        n.prev = header.prev;
        header.prev = n;
        n.next = header;
    }

    private void unlink(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // ------------------------------------------------------------------

    private Node findNode(Object item) {
        int index = hash(item);
        Node nodo_cubeta = table[index];
        while (nodo_cubeta != null) {
            if (equalsNull(nodo_cubeta.data, item)) {
                return nodo_cubeta;
            }
            nodo_cubeta = nodo_cubeta.hnext;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // ------------------------------------------------------------------

    @Override
    public boolean contains(Object item) {
        return findNode(item) != null;
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // ------------------------------------------------------------------

    @Override
    public boolean add(T item) {
        if (contains(item)) {
            return false;
        }

        // Creamos el nodo
        Node nodo_insertar = new Node();
        nodo_insertar.data = item;

        // Insertamos el nodo en la cubeta
        int index = hash(item);
        nodo_insertar.hnext = table[index];
        table[index] = nodo_insertar;

        // Insertamos el nodo en la lista de orden
        linkLast(nodo_insertar);
        size++;

        rehash();

        return true;
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private void rehash() {
        if (!(size > rehashThreshold)) {
            return;
        }

        rehashThreshold *= 2;
        Node orden_antiguo = header;

        // Reiniciamos los valores de la lista
        table = new EDLinkedHashSet.Node[size * 2 + 1];
        header = new Node();
        header.next = header;
        header.prev = header;
        size = 0;

        Node recorre = orden_antiguo.next;
        while (recorre != orden_antiguo) {
            add(recorre.data);
            recorre = recorre.next;
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // ------------------------------------------------------------------

    @Override
    public boolean remove(Object item) {
        Node nodo = findNode(item);

        if (nodo == null) {
            return false;
        }

        int index = hash(item);
        Node nodo_cubeta = table[index];
        while (nodo_cubeta.hnext != nodo && nodo_cubeta.hnext != null) {
            nodo_cubeta = nodo_cubeta.hnext;
        }
        nodo_cubeta.hnext = nodo.hnext;
        unlink(nodo);
        size--;
        return true;
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // ------------------------------------------------------------------

    @Override
    public void clear() {
        //TODO Ejercicio 8
        throw new UnsupportedOperationException("Ejercicio 8: por implementar");
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // ------------------------------------------------------------------

    private class EDIterator implements Iterator<T> {
        Node current = header.next;   // nodo que devolverá next
        Node lastReturned = null;     // último nodo devuelto por next

        @Override
        public boolean hasNext() {
            //TODO Ejercicio 9
            throw new UnsupportedOperationException("Ejercicio 9: por implementar");
        }

        @Override
        public T next() {
            //TODO Ejercicio 9
            //Debe actualizar lastReturned para que lo use remove
            throw new UnsupportedOperationException("Ejercicio 9: por implementar");
        }

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            EDLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new EDIterator();
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // ------------------------------------------------------------------

    @Override
    public boolean containsAll(Collection<?> c) {
        //TODO Ejercicio 10
        throw new UnsupportedOperationException("Ejercicio 10: por implementar");
    }

    // ------------------------------------------------------------------
    // Ejercicio 11
    // ------------------------------------------------------------------
    @Override
    public boolean retainAll(Collection<?> c) {
        //TODO Ejercicio 11
        throw new UnsupportedOperationException("Ejercicio 11: por implementar");
    }
}
