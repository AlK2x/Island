import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] a;

    public RandomizedQueue() {
        N = 0;
        a = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(a, 0, temp, 0, N);
        a = temp;
    }

    private void exch(Item[] a, int v, int w) {
        Item t = a[v];
        a[v] = a[w];
        a[w] = t;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (N == a.length) resize(2 * a.length);
        a[N++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(N);
        Item item = a[index];
        exch(a, N - 1, index);
        a[N - 1] = null;
        N--;
        if (N > 0 && N == a.length / 4) resize(a.length / 2);
        return item;

    }

    public Item getItem(int i) {
        return this.a[i];
    }

    public Item sample() {
        if (N == 0) throw new NoSuchElementException();
        if (N == 1) return a[0];
        return a[StdRandom.uniform(N)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueuesIterator<Item>(this.a);
    }

    private class RandomizedQueuesIterator<Item> implements Iterator<Item> {
        private int i = 0;
        private Item[] item;

        RandomizedQueuesIterator(Item[] item) {
            this.item = (Item[]) new Object[N];
            System.arraycopy(item, 0, this.item, 0, N);
            StdRandom.setSeed(System.currentTimeMillis());
            StdRandom.shuffle(this.item);
        }

        public boolean hasNext() {
            return i < item.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return item[i++];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        Iterator itr1 = q.iterator();
        Iterator itr2 = q.iterator();
        while (itr1.hasNext()) {
            Object element = itr1.next();
            StdOut.print(element + " ");
        }
        StdOut.println();
        while (itr2.hasNext()) {
            Object element = itr2.next();
            StdOut.print(element + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}
