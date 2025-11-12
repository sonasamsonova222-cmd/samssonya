 #**БИНАРНАЯ КУЧА**
import heapq

max_heap = []
heapq.heappush(max_heap, -10)  # храним -10
heapq.heappush(max_heap, -5)   # храним -5
heapq.heappush(max_heap, -20)  # храним -20

# Извлекаем максимум (берём минус от минимального в куче)
max_val = -heapq.heappop(max_heap)
print(max_val)  # 20


 #**БИНОМИНАЛЬНАЯ КУЧА**
class BinomialHeapNode:
    def __init__(self, key):
        self.key = key
        self.children = []  # список дочерних узлов
        self.parent = None
        self.degree = 0  # ранг (число детей)

class BinomialHeap:
    def __init__(self):
        self.roots = []  # список корней биномиальных деревьев

    def merge_trees(self, t1, t2):
        """Сливает два дерева одинакового ранга (t1.key <= t2.key)"""
        if t1.key > t2.key:
            t1, t2 = t2, t1
        t2.parent = t1
        t1.children.append(t2)
        t1.degree += 1
        return t1

    def union(self, other):
        """Объединяет две биномиальные кучи"""
        # Сшиваем корни в один список, сортированный по degree
        combined = sorted(self.roots + other.roots, key=lambda x: x.degree)
        if not combined:
            return

        i = 0
        while i < len(combined) - 1:
            if combined[i].degree == combined[i + 1].degree:
                # Сливаем деревья одинакового ранга
                merged = self.merge_trees(combined[i], combined[i + 1])
                combined[i] = merged
                combined.pop(i + 1)
            else:
                i += 1
        self.roots = combined

    def insert(self, key):
        """Добавляет элемент"""
        new_heap = BinomialHeap()
        new_heap.roots.append(BinomialHeapNode(key))
        self.union(new_heap)

    def get_min(self):
        """Возвращает минимальный ключ (без удаления)"""
        if not self.roots:
            raise ValueError("Heap is empty")
        return min(node.key for node in self.roots)

    def extract_min(self):
        """Удаляет и возвращает минимальный элемент"""
        if not self.roots:
            raise ValueError("Heap is empty")

        # Находим дерево с минимальным корнем
        min_node = min(self.roots, key=lambda x: x.key)
        self.roots.remove(min_node)

        # Создаём новую кучу из детей min_node
        new_heap = BinomialHeap()
        for child in min_node.children:
            child.parent = None
            new_heap.roots.append(child)

        # Объединяем оставшуюся кучу с новой
        self.union(new_heap)
        return min_node.key

# Пример использования
bh = BinomialHeap()
bh.insert(10)
bh.insert(5)
bh.insert(20)
print(bh.get_min())      # 5
print(bh.extract_min())  # 5
print(bh.get_min())      # 10


 #**КУЧА ФИБОНАЧЧИ**
import math

class FibonacciHeapNode:
    def __init__(self, key):
        self.key = key
        self.degree = 0
        self.parent = None
        self.child = None
        self.mark = False
        # Двусвязный круговой список: left ↔ right
        self.left = self
        self.right = self


class FibonacciHeap:
    def __init__(self):
        self.min_node = None   # Указатель на узел с минимумом
        self.num_nodes = 0    # Число узлов в куче

    def is_empty(self):
        return self.min_node is None

    def insert(self, key):
        """Вставка за O(1)"""
        node = FibonacciHeapNode(key)
        if self.min_node is None:
            self.min_node = node
        else:
            # Вставляем node в корневой список (между min_node и min_node.right)
            node.left = self.min_node
            node.right = self.min_node.right
            self.min_node.right.left = node
            self.min_node.right = node
            # Обновляем min_node, если новый ключ меньше
            if node.key < self.min_node.key:
                self.min_node = node
        self.num_nodes += 1
        return node  # Возвращаем узел для decrease_key/delete


    def minimum(self):
        """Возврат минимума за O(1)"""
        if self.min_node is None:
            return None
        return self.min_node.key

    def merge(self, other_heap):
        """Слияние двух куч за O(1)"""
        if other_heap.is_empty():
            return
        if self.is_empty():
            self.min_node = other_heap.min_node
        else:
            # Сшиваем корневые списки
            a, b = self.min_node, other_heap.min_node
            a_right, b_left = a.right, b.left
            a.right = b
            b.left = a
            b_left.right = a_right
            a_right.left = b_left
            # Обновляем min_node
            if b.key < a.key:
                self.min_node = b
        self.num_nodes += other_heap.num_nodes

    def _link(self, parent, child):
        """Перемещает child в дети parent (используется в consolidate)"""
        # Удаляем child из корневого списка
        child.left.right = child.right
        child.right.left = child.left
        # Добавляем child к детям parent
        child.parent = parent
        if parent.child is None:
            parent.child = child
            child.left = child
            child.right = child
        else:
            child.left = parent.child
            child.right = parent.child.right
            parent.child.right.left = child
            parent.child.right = child
        parent.degree += 1
        child.mark = False

    def _consolidate(self):
        """Объединяет деревья одинаковой степени (вызывается из extract_min)"""
        if self.min_node is None:
            return
        # Максимальная степень дерева: log₂(n)
        max_degree = int(math.log(self.num_nodes) * 2) + 1
        degree_table = [None] * max_degree  # degree_table[d] — дерево степени d

        # Собираем все корневые узлы
        nodes = []
        current = self.min_node
        while True:
            nodes.append(current)
            current = current.right
            if current == self.min_node:
                break

        for node in nodes:
            d = node.degree
            while degree_table[d] is not None:
                other = degree_table[d]
                if node.key > other.key:
                    node, other = other, node  # node — меньший корень
                self._link(node, other)
                degree_table[d] = None
                d += 1
            degree_table[d] = node

        # Перестраиваем корневой список из degree_table
        self.min_node = None
        for tree in degree_table:
            if tree is not None:
                if self.min_node is None:
                    self.min_node = tree
                    tree.left = tree
                    tree.right = tree
                else:
                    # Вставляем tree в корневой список
                    tree.left = self.min_node
                    tree.right = self.min_node.right
                    self.min_node.right.left = tree
                    self.min_node.right = tree
                    if tree.key < self.min_node.key:
                        self.min_node = tree

    def extract_min(self):
        """Извлечение минимума за O(log n) (амортиз.)"""
        if self.min_node is None:
            raise ValueError("extract_min: куча пуста")

        z = self.min_node

        # 1. Добавляем детей z в корневой список
        if z.child is not None:
            child = z.child
            children = []
            while True:
                children.append(child)
                child = child.right
                if child == z.child:
                    break
            for child_node in children:
                # Отсоединяем от родителя и убираем mark
                child_node.parent = None
                child_node.mark = False
                # Вставляем в корневой список
                child_node.left = z
                child_node.right = z.right
                z.right.left = child_node
                z.left.right = child_node

        # 2. Удаляем z из корневого списка
        if z == z.right:
            self.min_node = None  # Куча стала пустой
        else:
            z.left.right = z.right
            z.right.left = z.left
            self.min_node = z.right  # Временный min — следующий
            self._consolidate()     # Объединяем деревья

        self.num_nodes -= 1
        return z.key

    def _cut(self, node, parent):
        """Отрезает node от parent и помещает в корневой список"""
        if node == node.right:
            parent.child = None
        else:
            node.left.right = node.right
            node.right.left = node.left
            if node == parent.child:
                parent.child = node.right
        parent.degree -= 1

        node.parent = None
        node.mark = False

        # Вставляем node в корневой список
        node.left = self.min_node
        node.right = self.min_node.right
        self.min_node.right.left = node
        self.min_node.right = node

    def _cascading_cut(self, node):
        """Каскадное отрезание вверх по родителям"""
        parent = node.parent
        if parent is not None:
            if not node.mark:
                node.mark = True
            else:
                self._cut(node, parent)
                self._cascading_cut(parent)

    def decrease_key(self, node, new_key):
        """Уменьшение ключа за O(1) (амортиз.)"""
        if new_key > node.key:
            raise ValueError("new_key > current key")
        node.key = new_key
        parent = node.parent

        if parent is not None and node.key < parent.key:
            self._cut(node, parent)
            self._cascading_cut(parent)

        if node.key < self.min_node.key:
            self.min




#**ХЭШ-ТАБЛИЦА**
class HashTable:
    def __init__(self, size=10):
        self.size = size
        self.table = [[] for _ in range(size)]  # Список списков для цепочек

    def _hash(self, key):
        """Простая хэш‑функция (сумма кодов символов по модулю размера)."""
        return sum(ord(c) for c in str(key)) % self.size

    def insert(self, key, value):
        """Добавить пару ключ‑значение."""
        index = self._hash(key)
        bucket = self.table[index]

        # Проверяем, есть ли ключ в цепочке
        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key, value)  # Обновляем значение
                return
        bucket.append((key, value))  # Добавляем новую пару

    def search(self, key):
        """Найти значение по ключу."""
        index = self._hash(key)
        bucket = self.table[index]

        for k, v in bucket:
            if k == key:
                return v
        raise KeyError(key)  # Ключ не найден

    def delete(self, key):
        """Удалить пару по ключу."""
        index = self._hash(key)
        bucket = self.table[index]

        for i, (k, v) in enumerate(bucket):
            if k == key:
                del bucket[i]
                return
        raise KeyError(key)  # Ключ не найден

    def __contains__(self, key):
        """Проверить наличие ключа (для оператора `in`)."""
        try:
            self.search(key)
            return True
        except KeyError:
            return False

    def __str__(self):
        """Строковое представление для отладки."""
        items = []
        for bucket in self.table:
            for k, v in bucket:
                items.append(f"{k}: {v}")
        return "{" + ", ".join(items) + "}"

# Пример использования
ht.insert("name", "Alice")
ht.insert("age", 25)
ht.insert("city", "Moscow")
...
print(ht)  # {name: Alice, age: 26}
