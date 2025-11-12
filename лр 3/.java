#**БИНАРНАЯ КУЧА** 
  public class BinaryMinHeap {
    private int[] heap;
    private int size;
    private int capacity;

    // Конструктор: задаём максимальную ёмкость кучи
    public BinaryMinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity];
        this.size = 0;
    }

    // Вспомогательные методы для вычисления индексов
    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }

    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Обмен элементов в массиве
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Восстановление свойства кучи снизу вверх (после вставки)
    private void heapifyUp(int index) {
        if (index > 0 && heap[parent(index)] > heap[index]) {
            swap(index, parent(index));
            heapifyUp(parent(index));
        }
    }

    // Восстановление свойства кучи сверху вниз (после извлечения)
    private void heapifyDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && heap[left] < heap[smallest]) {
            smallest = left;
        }
        if (right < size && heap[right] < heap[smallest]) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    // Вставка элемента
    public void insert(int value) {
        if (size >= capacity) {
            throw new IllegalStateException("Heap is full");
        }
        heap[size] = value;
        size++;
        heapifyUp(size - 1);
    }

    // Извлечение минимального элемента (корня)
    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return min;
    }

    // Просмотр минимального элемента (без извлечения)
    public int peek() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap[0];
    }

    // Проверка, пуста ли куча
    public boolean isEmpty() {
        return size == 0;
    }

    // Размер кучи
    public int size() {
        return size;
    }

    // Печать массива кучи (для отладки)
    public void printHeap() {
        System.out.print("Heap: ");
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }
}

  Пример реализации:
public class Main {
    public static void main(String[] args) {
        BinaryMinHeap heap = new BinaryMinHeap(10);

        heap.insert(10);
        heap.insert(5);
        heap.insert(20);
        heap.insert(1);
        heap.insert(15);

        heap.printHeap(); // Heap: 1 5 20 10 15

        System.out.println("Min: " + heap.extractMin()); // Min: 1
        System.out.println("New min: " + heap.peek());  // New min: 5

        heap.printHeap(); // Heap: 5 10 20 15
    }
}



#**БИНОМИНАЛЬНАЯ КУЧА**
import java.util.LinkedList;
import java.util.List;

// Узел биномиального дерева
class BinomialNode {
    int key;
    int degree; // степень (порядок) дерева
    BinomialNode parent;
    BinomialNode child;  // первый ребёнок
    BinomialNewton sibling; // следующий брат

    public BinomialNode(int key) {
        this.key = key;
        this.degree = 0;
        this.parent = null;
        this.child = null;
        this.sibling = null;
    }
}

// Биномиальная куча
public class BinomialHeap {
    private List<BinomialNode> roots; // список корней деревьев

    public BinomialHeap() {
        this.roots = new LinkedList<>();
    }

    // Вспомогательный метод: слияние двух биномиальных деревьев одинаковой степени
    private BinomialNode mergeTrees(BinomialNode a, BinomialNode b) {
        if (a.key > b.key) {
            // Делаем b родителем a
            BinomialNode temp = a;
            a = b;
            b = temp;
        }
        b.parent = a;
        b.sibling = a.child;
        a.child = b;
        a.degree++;
        return a;
    }

    // Слияние двух биномиальных куч (основной метод)
    public void merge(BinomialHeap other) {
        // Объединяем списки корней
        List<BinomialNode> newRoots = new LinkedList<>();
        List<BinomialNode> list1 = this.roots;
        List<BinomialNode> list2 = other.roots;

        var it1 = list1.iterator();
        var it2 = list2.iterator();

        BinomialNode curr1 = it1.hasNext() ? it1.next() : null;
        BinomialNode curr2 = it2.hasNext() ? it2.next() : null;

        while (curr1 != null || curr2 != null) {
            if (curr2 == null || (curr1 != null && curr1.degree <= curr2.degree)) {
                newRoots.add(curr1);
                curr1 = it1.hasNext() ? it1.next() : null;
            } else {
                newRoots.add(curr2);
                curr2 = it2.hasNext() ? it2.next() : null;
            }
        }

        // Очищаем исходные кучи
        this.roots.clear();
        other.roots.clear();

        // Проходим по новому списку и сливаем деревья одинаковой степени
        if (newRoots.isEmpty()) return;

        List<BinomialNode> merged = new LinkedList<>();
        BinomialNode prev = null;

        for (BinomialNode node : newRoots) {
            if (prev == null || prev.degree != node.degree) {
                merged.add(node);
                prev = node;
            } else {
                // Сливаем два дерева одинаковой степени
                BinomialNode mergedTree = mergeTrees(prev, node);
                merged.remove(prev);
                merged.add(mergedTree);
                prev = mergedTree;
            }
        }

        this.roots = merged;
    }

    // Вставка нового элемента
    public void insert(int key) {
        BinomialHeap tempHeap = new BinomialHeap();
        tempHeap.roots.add(new BinomialNode(key));
        this.merge(tempHeap);
    }

    // Поиск узла с минимальным ключом
    public BinomialNode findMin() {
        if (roots.isEmpty()) return null;

        BinomialNode minNode = roots.get(0);
        for (BinomialNode node : roots) {
            if (node.key < minNode.key) {
                minNode = node;
            }
        }
        return minNode;
    }

    // Извлечение минимального элемента
    public int extractMin() {
        if (roots.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        // Находим дерево с минимальным корнем
        BinomialNode minNode = findMin();
        roots.remove(minNode);

        // Создаём новую кучу из детей minNode (они образуют биномиальную кучу)
        BinomialHeap childrenHeap = new BinomialHeap();
        BinomialNode child = minNode.child;
        while (child != null) {
            child.parent = null;
            childrenHeap.roots.add(child);
            child = child.sibling;
        }

        // Сливаем оставшуюся кучу с кучей из детей
        this.merge(childrenHeap);

        return minNode.key;
    }

    // Проверка, пуста ли куча
    public boolean isEmpty() {
        return roots.isEmpty();
    }

    // Печать всех корней (для отладки)
    public void printRoots() {
        System.out.print("Roots: ");
        for (BinomialNode node : roots) {
            System.out.print(node.key + "(deg=" + node.degree + ") ");
        }
        System.out.println();
    }
}

    Пример реализации:
public class Main {
    public static void main(String[] args) {
        BinomialHeap heap = new BinomialHeap();

        heap.insert(10);
        heap.insert(5);
        heap.insert(20);
        heap.insert(1);
        heap.insert(15);

        heap.printRoots(); // Roots: 1(deg=2) 5(deg=0)

        System.out.println("Min: " + heap.extractMin()); // Min: 1
        System.out.println("New min: " + heap.findMin().key); // New min: 5

        heap.printRoots(); // Roots: 5(deg=1) 10(deg=0) 15(deg=0)
    }
}


#**КУЧА ФИБОНАЧЧИ**
import java.util.*;

public class FibonacciHeap {
    private FibonacciNode minNode;
    private int size;

    public FibonacciHeap() {
        minNode = null;
        size = 0;
    }

    // Вставка элемента
    public void insert(int key) {
        FibonacciNode node = new FibonacciNode(key);
        if (minNode == null) {
            minNode = node;
        } else {
            addNodeToRootList(node);
            if (node.key < minNode.key) {
                minNode = node;
            }
        }
        size++;
    }

    // Поиск минимума (O(1))
    public int findMin() {
        if (minNode == null) throw new IllegalStateException("Heap is empty");
        return minNode.key;
    }

    // Извлечение минимума (O(log n) амортизировано)
    public int extractMin() {
        if (minNode == null) throw new IllegalStateException("Heap is empty");

        FibonacciNode z = minNode;
        int minKey = z.key;

        // Добавляем детей z в корневой список
        if (z.child != null) {
            FibonacciNode child = z.child;
            do {
                child.parent = null;
                child = child.right;
            } while (child != z.child);

            mergeLists(z.child, minNode);
        }

        // Удаляем z из корневого списка
        if (z == z.right) {
            minNode = null;
        } else {
            z.left.right = z.right;
            z.right.left = z.left;
            minNode = z.right;
            consolidate();
        }

        size--;
        return minKey;
    }

    // Слияние двух куч (O(1))
    public void merge(FibonacciHeap other) {
        if (other.minNode == null) return;
        if (this.minNode == null) {
            this.minNode = other.minNode;
            this.size = other.size;
        } else {
            mergeLists(other.minNode, this.minNode);
            if (other.minNode.key < this.minNode.key) {
                this.minNode = other.minNode;
            }
            this.size += other.size;
        }
        other.minNode = null;
        other.size = 0;
    }

    // Уменьшение ключа
    public void decreaseKey(FibonacciNode x, int newKey) {
        if (newKey > x.key) 
            throw new IllegalArgumentException("New key > current key");

        x.key = newKey;
        FibonacciNode y = x.parent;

        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadeCut(y);
        }

        if (x.key < minNode.key) minNode = x;
    }

    // Удаление узла
    public void delete(FibonacciNode x) {
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }

    // Вспомогательные методы
    private void addNodeToRootList(FibonacciNode node) {
        node.right = minNode.right;
        minNode.right.left = node;
        node.left = minNode;
        minNode.right = node;
    }

    private void mergeLists(FibonacciNode list1, FibonacciNode list2) {
        FibonacciNode temp1 = list1.right;
        FibonacciNode temp2 = list2.right;

        list1.right = temp2;
        temp2.left = list1;
        list2.right = temp1;
        temp1.left = list2;
    }

    private void consolidate() {
        int D = (int) (Math.log(size) / Math.log(1.618)) + 1;
        List<FibonacciNode> A = new ArrayList<>(D + 1);
        for (int i = 0; i <= D; i++) A.add(null);

        List<FibonacciNode> roots = getRootList();
        for (FibonacciNode w : roots) {
            int d = w.degree;
            while (A.get(d) != null) {
                FibonacciNode y = A.get(d);
                if (w.key > y.key) {
                    FibonacciNode temp = w;
                    w = y;
                    y = temp;
                }
                link(y, w);
                A.set(d, null);
                d++;
            }
            A.set(d, w);
        }

        minNode = null;
        for (FibonacciNode node : A) {
            if (node != null) {
                if (minNode == null) {
                    minNode = node;
                    node.left = node;
                    node.right = node;
                } else {
                    addNodeToRootList(node);
                    if (node.key < minNode.key) minNode = node;
                }
            }
        }
    }

    private void link(FibonacciNode y, FibonacciNode x) {
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.right = x.child.right;
            x.child.right.left = y;
            y.left = x.child;
            x.child.right = y;
        }
        x.degree++;
        y.marked = false;
    }

    private void cut(FibonacciNode x, FibonacciNode y) {
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        if (y.child == x) y.child = x.right;
        if (y.degree == 0) y.child = null;

        addNodeToRootList(x);
        x.parent = null;
        x.marked = false;
    }

    private void cascadeCut(FibonacciNode y) {
        FibonacciNode z = y.parent;
        if (z != null) {
            if (!y.marked) {
                y.marked = true;
            } else {
                cut(y, z);
                cascadeCut(z);
            }
        }
    }

    private List<FibonacciNode> getRootList() {
        List<FibonacciNode> list = new ArrayList<>();
        if (minNode == null) return list;

        FibonacciNode current = minNode;
        do {
            list.add(current);
            current = current.right;
        } while (current != minNode);
        return list;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}

   Пример реализации:
public class Main {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();

        // Вставка элементов
        heap.insert(10);
        heap.insert(5);
        heap.insert(15);
        heap.insert(3);

        System.out.println("Текущий минимум: " + heap.findMin()); // 3

        // Извлекаем минимум
        System.out.println("Извлечён: " + heap.extractMin()); // 3

                  
#**ХЭШ-ТАБЛИЦЫ**
import java.util.Objects;

public class HashTable<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Object[] keys;
    private Object[] values;
    private int size;
    private int capacity;

    public HashTable() {
        this.capacity = INITIAL_CAPACITY;
        this.keys = new Object[capacity];
        this.values = new Object[capacity];
        this.size = 0;
    }

    // Хэш‑функция
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Поиск индекса для ключа (с пробированием)
    private int findIndex(K key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                return index;
            }
            index = (index + 1) % capacity;
        }
        return index; // свободная ячейка
    }

    // Проверка заполненности и расширение
    private void ensureCapacity() {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    // Удвоение размера и перехеширование
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Object[] oldKeys = keys;
        Object[] oldValues = values;

        keys = new Object[capacity];
        values = new Object[capacity];
        size = 0;

        // Перехешируем все элементы
        for (int i = 0; i < oldCapacity; i++) {
            if (oldKeys[i] != null) {
                put((K) oldKeys[i], (V) oldValues[i]);
            }
        }
    }

    // Добавление пары ключ‑значение
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        ensureCapacity();

        int index = findIndex(key);
        if (keys[index] == null) {
            size++;
        }
        keys[index] = key;
        values[index] = value;
    }

    // Получение значения по ключу
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = findIndex(key);
        if (keys[index] != null && keys[index].equals(key)) {
            return (V) values[index];
        }
        return null; // не найдено
    }

    // Проверка наличия ключа
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = findIndex(key);
        return keys[index] != null && keys[index].equals(key);
    }

    // Удаление ключа
    public boolean remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = findIndex(key);
        if (keys[index] == null || !keys[index].equals(key)) {
            return false; // не найдено
        }

        keys[index] = null;
        values[index] = null;

        // Перехешируем последующие элементы
        int next = (index + 1) % capacity;
        while (keys[next] != null) {
            K nextKey = (K) keys[next];
            V nextValue = (V) values[next];

            keys[next] = null;
            values[next] = null;
            size--;

            put(nextKey, nextValue); // перевставляем
            next = (next + 1) % capacity;
        }

        size--;
        return true;
    }

    // Размер таблицы
    public int size() {
        return size;
    }

    // Проверка на пустоту
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка таблицы
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            keys[i] = null;
            values[i] = null;
        }
        size = 0;
    }
}

     Пример реализации:
  public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> table = new HashTable<>();

        // Добавление элементов
        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);

        // Получение значений
        System.out.println(table.get("apple"));   // 10
        System.out.println(table.get("banana"));  // 20

        // Проверка наличия
        System.out.println(table.containsKey("cherry")); // true


        // Удаление
        table.remove("banana");
        System.out.println(table.get("banana")); // null

        // Размер
        System.out.println(table.size()); // 2
    }
}
