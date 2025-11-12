   **БИНАРНАЯ КУЧА**
     
#include <iostream>
#include <vector>
#include <stdexcept>

class MaxHeap {
private:
    std::vector<int> heap;

    // Проталкивание элемента вверх (после вставки)
    void siftUp(int index) {
        if (index == 0) return;  // Корень — некуда подниматься

        int parent = (index - 1) / 2;
        if (heap[index] > heap[parent]) {
            std::swap(heap[index], heap[parent]);
            siftUp(parent);
        }
    }

    // Проталкивание элемента вниз (после извлечения)
    void siftDown(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int largest = index;

        // Находим наибольший среди текущего, левого и правого потомка
        if (left < heap.size() && heap[left] > heap[largest])
            largest = left;
        if (right < heap.size() && heap[right] > heap[largest])
            largest = right;

        // Если наибольший не текущий — меняем и продолжаем
        if (largest != index) {
            std::swap(heap[index], heap[largest]);
            siftDown(largest);
        }
    }

public:
    // Вставка элемента
    void insert(int value) {
        heap.push_back(value);
        siftUp(heap.size() - 1);
    }

    // Извлечение максимального элемента
    int extractMax() {
        if (heap.empty())
            throw std::runtime_error("Heap is empty");

        int maxValue = heap[0];
        heap[0] = heap.back();  // Переносим последний элемент в корень
        heap.pop_back();      // Удаляем последний

        if (!heap.empty())     // Если куча не опустела — восстанавливаем свойство
            siftDown(0);

        return maxValue;
    }

    // Просмотр максимального элемента (без извлечения)
    int getMax() const {
        if (heap.empty())
            throw std::runtime_error("Heap is empty");
        return heap[0];
    }

    // Проверка на пустоту
    bool isEmpty() const {
        return heap.empty();
    }

    // Размер кучи
    size_t size() const {
        return heap.size();
    }

    // Вывод всех элементов (для отладки)
    void print() const {
        for (int val : heap) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
    }
};

  Пример использования:
int main() {
    MaxHeap heap;

    // Вставляем элементы
    heap.insert(10);
    heap.insert(20);
    heap.insert(5);
    heap.insert(30);
    heap.insert(15);

    std::cout << "Куча: ";
    heap.print();  // 30 20 5 10 15

    std::cout << "Максимум: " << heap.getMax() << std::endl;  // 30

    std::cout << "Извлечён максимум: " << heap.extractMax() << std::endl;  // 30
    std::cout << "Новый максимум: " << heap.getMax() << std::endl;      // 20

    std::cout << "Куча после извлечения: ";
    heap.print();  // 20 15 5 10

    return 0;
}


**БИНОМИНАЛЬНАЯ КУЧА**
  
#include <iostream>
#include <vector>
#include <stdexcept>

// Узел биномиального дерева
template<typename T>
struct BinomialNode {
    T key;
    int degree;  // степень (количество детей)
    BinomialNode* parent;
    BinomialNode* child;   // первый ребёнок
    BinomialNode* sibling;  // следующий брат

    explicit BinomialNode(T k) : key(k), degree(0),
        parent(nullptr), child(nullptr), sibling(nullptr) {}
};

// Биномиальная куча
template<typename T>
class BinomialHeap {
private:
    BinomialNode<T>* head;  // голова списка корней

    // Слияние двух биномиальных деревьев одинаковой степени
    BinomialNode<T>* mergeTrees(BinomialNode<T>* t1, BinomialNode<T>* t2) {
        if (t1->key > t2->key) {
            std::swap(t1, t2);  // t1 получает меньший ключ
        }
        t2->parent = t1;
        t2->sibling = t1->child;
        t1->child = t2;
        t1->degree++;
        return t1;
    }

    // Консолидация кучи: объединение деревьев одинаковой степени
    void consolidate() {
        std::vector<BinomialNode<T>*> trees(64, nullptr);  // макс. степень ~64
        BinomialNode<T>* curr = head;
        head = nullptr;

        while (curr) {
            BinomialNode<T>* next = curr->sibling;
            curr->sibling = nullptr;  // отсоединяем от списка

            int d = curr->degree;
            while (trees[d]) {
                curr = mergeTrees(curr, trees[d]);
                trees[d] = nullptr;
                d++;
            }
            trees[d] = curr;
            curr = next;
        }

        // Собираем итоговый список корней
        for (auto& tree : trees) {
            if (tree) {
                tree->sibling = head;
                head = tree;
            }
        }
    }

public:
    BinomialHeap() : head(nullptr) {}

    // Вставка ключа (создаём кучу из 1 узла и сливаем)
    void insert(T key) {
        BinomialHeap temp;
        temp.head = new BinomialNode<T>(key);
        merge(temp);
    }

    // Объединение с другой кучей
    void merge(BinomialHeap& other) {
        // Сшиваем два списка корней в один (по возрастанию degree)
        BinomialNode<T>* newHead = nullptr;
        BinomialNode<T>** curr = &newHead;
        BinomialNode<T>* h1 = head;
        BinomialNode<T>* h2 = other.head;

        while (h1 && h2) {
            if (h1->degree <= h2->degree) {
                *curr = h1;
                h1 = h1->sibling;
            } else {
                *curr = h2;
                h2 = h2->sibling;
            }
            curr = &((*curr)->sibling);
        }
        *curr = h1 ? h1 : h2;

        head = newHead;
        other.head = nullptr;  // обнуляем вторую кучу
        consolidate();         // объединяем деревья одинаковой степени
    }

    // Извлечение минимального ключа
    T extractMin() {
        if (!head) throw std::runtime_error("Heap is empty");

        // Находим корень с минимальным ключом
        BinomialNode<T>* prevMin = nullptr;
        BinomialNode<T>* minNode = head;
        BinomialNode<T>* curr = head;

        while (curr->sibling) {
            if (curr->sibling->key < minNode->key) {
                prevMin = curr;
                minNode = curr->sibling;
            }
            curr = curr->sibling;
        }

        // Удаляем minNode из списка корней
        if (prevMin) {
            prevMin->sibling = minNode->sibling;
        } else {
            head = minNode->sibling;
        }

        // Создаём новую кучу из детей minNode
        BinomialHeap childrenHeap;
        BinomialNode<T>* child = minNode->child;
        while (child) {
            BinomialNode<T>* next = child->sibling;
            child->parent = nullptr;
            child->sibling = childrenHeap.head;
            childrenHeap.head = child;
            child = next;
        }

        T minKey = minNode->key;
        delete minNode;

        // Сливаем детей обратно в основную кучу
        merge(childrenHeap);
        return minKey;
    }

    // Уменьшение ключа узла (предполагаем, что узел доступен)
    void decreaseKey(BinomialNode<T>* node, T newKey) {
        if (newKey > node->key) {
            throw std::invalid_argument("New key is greater than current key");
        }
        node->key = newKey;

        // «Поднимаем» узел вверх, пока родитель больше
        BinomialNode<T>* parent = node->parent;
        while (parent && node->key < parent->key) {
            // Меняем ключи (не перестраиваем указатели!)
            std::swap(node->key, parent->key);
            node = parent;
            parent = node->parent;
        }
    }

    // Проверка на пустоту
    bool isEmpty() const {
        return head == nullptr;
    }

    // Деструктор (очистка памяти)
    ~BinomialHeap() {
        clear();
    }

    void clear() {
        while (!isEmpty()) {
            extractMin();
        }
    }
};

  Пример использования:
int main() {
    BinomialHeap<int> heap;

    // Вставка элементов
    heap.insert(10);
    heap.insert(5);
    heap.insert(20);
    heap.insert(3);
    heap.insert(15);

    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 3
    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 5

    heap.insert(1);
    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 1

    return 0;
}


  
**КУЧА ФИБОНАЧЧИ**
  
#include <iostream>
#include <vector>
#include <stdexcept>
#include <climits>

// Узел кучи Фибоначчи
template<typename T>
struct FibNode {
    T key;
    int degree;           // количество детей
    bool marked;          // отмечен ли узел (для каскадного вырезания)
    FibNode* parent;      // родитель
    FibNode* child;       // первый ребёнок
    FibNode* left;        // левый сосед в циклическом списке
    FibNode* right;       // правый сосед в циклическом списке

    explicit FibNode(T k) : key(k), degree(0), marked(false),
        parent(nullptr), child(nullptr), left(this), right(this) {}
};

// Куча Фибоначчи
template<typename T>
class FibonacciHeap {
private:
    FibNode<T>* min;     // указатель на узел с минимальным ключом
    int n;              // количество узлов в куче

    // Добавление узла в корневой список (циклический)
    void addToRootList(FibNode<T>* x) {
        if (!min) {
            min = x;
        } else {
            x->left = min;
            x->right = min->right;
            min->right->left = x;
            min->right = x;
            if (x->key < min->key) {
                min = x;
            }
        }
    }

    // Удаление узла из корневого списка
    void removeFromRootList(FibNode<T>* x) {
        x->left->right = x->right;
        x->right->left = x->left;
    }

    // Слияние корневых списков двух куч
    void mergeRootLists(FibNode<T>* h1, FibNode<T>* h2) {
        if (!h1 || !h2) return;
        FibNode<T>* temp = h1->right;
        h1->right = h2->right;
        h2->right->left = h1;
        h2->right = temp;
        temp->left = h2;
    }

    // Консолидация кучи (объединение деревьев одинаковой степени)
    void consolidate() {
        const int D = 64;  // макс. степень ~log(n)
        std::vector<FibNode<T>*> A(D, nullptr);

        std::vector<FibNode<T>*> roots;
        for (FibNode<T>* w = min; w; w = w->right) {
            roots.push_back(w);
            if (w->right == w) break;  // единственный узел
        }

        for (auto w : roots) {
            FibNode<T>* x = w;
            int d = x->degree;
            while (A[d]) {
                FibNode<T>* y = A[d];
                if (x->key > y->key) std::swap(x, y);
                link(y, x);
                A[d] = nullptr;
                d++;
            }
            A[d] = x;
        }

        min = nullptr;
        for (int i = 0; i < D; ++i) {
            if (A[i]) {
                addToRootList(A[i]);
            }
        }
    }

    // Присоединение дерева y к дереву x (y становится ребёнком x)
    void link(FibNode<T>* y, FibNode<T>* x) {
        removeFromRootList(y);
        y->parent = x;
        y->child = x->child;
        if (x->child) {
            y->child->left = y;
        }
        x->child = y;
        y->left = y->right = y;
        x->degree++;
        y->marked = false;
    }

    // Каскадное вырезание узла z
    void cascadingCut(FibNode<T>* z) {
        FibNode<T>* p = z->parent;
        if (p) {
            if (!z->marked) {
                z->marked = true;
            } else {
                cut(z, p);
                cascadingCut(p);
            }
        }
    }

    // Вырезание узла z из поддерева родителя p
    void cut(FibNode<T>* z, FibNode<T>* p) {
        // Удаляем z из списка детей p
        if (p->child == z) {
            p->child = z->right;
        }
        if (z->right != z) {
            z->left->right = z->right;
            z->right->left = z->left;
        }
        p->degree--;

        // Добавляем z в корневой список
        z->parent = nullptr;
        z->marked = false;
        addToRootList(z);
    }

public:
    FibonacciHeap() : min(nullptr), n(0) {}

    // Вставка ключа
    FibNode<T>* insert(T key) {
        FibNode<T>* x = new FibNode<T>(key);
        addToRootList(x);
        n++;
        return x;  // возвращаем узел для decreaseKey
    }

    // Извлечение минимального ключа
    T extractMin() {
        if (!min) throw std::runtime_error("Heap is empty");

        FibNode<T>* z = min;
        // Добавляем детей z в корневой список
        if (z->child) {
            FibNode<T>* child = z->child;
            do {
                child->parent = nullptr;
                child = child->right;
            } while (child != z->child);

            mergeRootLists(min, z->child);
        }

        // Удаляем z из корневого списка
        removeFromRootList(z);
        if (z == z->right) {  // последний узел
            min = nullptr;
        } else {
            min = z->right;
            consolidate();
        }

        T minKey = z->key;
        delete z;
        n--;
        return minKey;
    }

    // Уменьшение ключа узла x
    void decreaseKey(FibNode<T>* x, T k) {
        if (k > x->key) {
            throw std::invalid_argument("New key is greater than current key");
        }
        x->key = k;
        FibNode<T>* y = x->parent;

        if (y && x->key < y->key) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x->key < min->key) {
            min = x;
        }
    }

    // Объединение с другой кучей
    void merge(FibonacciHeap& other) {
        mergeRootLists(min, other.min);
        if (!min || (other.min && other.min->key < min->key)) {
            min = other.min;
        }
        n += other.n;
        other.min = nullptr;
        other.n = 0;
    }

    // Проверка на пустоту
    bool isEmpty() const {
        return min == nullptr;
    }

    // Количество элементов
    int size() const {
        return n;
    }

    // Деструктор (очистка памяти)
    ~FibonacciHeap() {
        clear();
    }

    void clear() {
        while (!isEmpty()) {
            extractMin();
        }
    }
};


   Пример использования:
int main() {
    FibonacciHeap<int> heap;

    // Вставка элементов
    auto n1 = heap.insert(10);
    auto n2 = heap.insert(5);
    auto n3 = heap.insert(20);
    auto n4 = heap.insert(3);
    auto n5 = heap.insert(15);

    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 3
    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 5

    heap.decreaseKey(n3, 1);  // уменьшаем 20 до 1
    std::cout << "Minimum: " << heap.extractMin() << std::endl;  // 1

    return 0;
}

**ХЭШ-ТАБЛИЦЫ**
  
#include <iostream>
#include <vector>
#include <list>
#include <utility>

template<typename Key, typename Value>
class HashTable {
private:
    std::vector<std::list<std::pair<Key, Value>>> table;
    size_t capacity;
    size_t size;

    // Простая хеш‑функция (для строк и числовых типов)
    size_t hash(const Key& key) const {
        return std::hash<Key>{}(key) % capacity;
    }

    // Увеличение размера и перехеширование
    void rehash() {
        size_t newCapacity = capacity * 2;
        std::vector<std::list<std::pair<Key, Value>>> newTable(newCapacity);

        for (const auto& bucket : table) {
            for (const auto& pair : bucket) {
                size_t index = std::hash<Key>{}(pair.first) % newCapacity;
                newTable[index].push_back(pair);
            }
        }

        table = std::move(newTable);
        capacity = newCapacity;
    }

public:
    explicit HashTable(size_t initialCapacity = 8)
        : capacity(initialCapacity), size(0) {
        table.resize(capacity);
    }

    // Вставка пары (ключ, значение)
    void insert(const Key& key, const Value& value) {
        size_t index = hash(key);

        // Проверяем, есть ли ключ в цепочке
        for (auto& pair : table[index]) {
            if (pair.first == key) {
                pair.second = value;  // обновляем значение
                return;
            }
        }

        // Добавляем новый элемент
        table[index].push_back({key, value});
        size++;

        // Если коэффициент заполнения > 0.7, перехешируем
        if (size > capacity * 0.7) {
            rehash();
        }
    }

    // Поиск значения по ключу
    bool find(const Key& key, Value& value) const {
        size_t index = hash(key);
        for (const auto& pair : table[index]) {
            if (pair.first == key) {
                value = pair.second;
                return true;
            }
        }
        return false;
    }

    // Удаление по ключу
    bool remove(const Key& key) {
        size_t index = hash(key);
        auto& bucket = table[index];

        for (auto it = bucket.begin(); it != bucket.end(); ++it) {
            if (it->first == key) {
                bucket.erase(it);
                size--;
                return true;
            }
        }
        return false;
    }

    // Проверка наличия ключа
    bool contains(const Key& key) const {
        Value dummy;
        return find(key, dummy);
    }

    // Размер таблицы
    size_t getSize() const { return size; }
    size_t getCapacity() const { return capacity; }

    // Очистка
    void clear() {
        table.clear();
        table.resize(capacity);
        size = 0;
    }
};

  Пример использования:
int main() {
    HashTable<std::string, int> ht;

    // Вставка данных
    ht.insert("apple", 10);
    ht.insert("banana", 20);
    ht.insert("cherry", 30);

    // Поиск
    int value;
    if (ht.find("apple", value)) {
        std::cout << "apple: " << value << std::endl;
    }

    // Обновление
    ht.insert("apple", 15);  // перезапишет старое значение

    // Удаление
    if (ht.remove("banana")) {
        std::cout << "banana removed" << std::endl;
    }

    // Проверка наличия
    if (ht.contains("cherry")) {
        std::cout << "cherry exists" << std::endl;
    }

    std::cout << "Size: " << ht.getSize() << std::endl;

    return 0;
}
