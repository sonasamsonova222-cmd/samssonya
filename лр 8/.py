# Чтение входных данных с клавиатуры
items_input = input("Введите размеры предметов через запятую (например, 7,5,3,8,2,6,4,9): ")
container_size_input = input("Введите размер контейнера (например, 10): ")

# Преобразование входных данных
items = list(map(int, items_input.split(',')))
container_size = int(container_size_input)

# Функция для вывода содержимого контейнеров
def print_bins(bins):
    for i, b in enumerate(bins):
        print(f"Контейнер {i+1}: {b} (заполнено: {sum(b)}/{container_size})")

# Стратегия First Fit
def first_fit(items, container_size):
    bins = []
    for item in items:
        placed = False
        for b in bins:
            if sum(b) + item <= container_size:
                b.append(item)
                placed = True
                break
        if not placed:
            bins.append([item])
    return bins

# Стратегия First Fit Decreasing
def first_fit_decreasing(items, container_size):
    sorted_items = sorted(items, reverse=True)
    return first_fit(sorted_items, container_size)

# Стратегия Best Fit
def best_fit(items, container_size):
    bins = []
    for item in items:
        best_bin_index = -1
        min_remaining = container_size + 1
        for i, b in enumerate(bins):
            remaining = container_size - sum(b)
            if item <= remaining < min_remaining:
                best_bin_index = i
                min_remaining = remaining
        if best_bin_index == -1:
            bins.append([item])
        else:
            bins[best_bin_index].append(item)
    return bins

# Выполнение стратегий
bins_ff = first_fit(items, container_size)
bins_ffd = first_fit_decreasing(items, container_size)
bins_bf = best_fit(items, container_size)

# Вывод результатов
print("\nРезультаты стратегии First Fit:")
print_bins(bins_ff)

print("\nРезультаты стратегии First Fit Decreasing:")
print_bins(bins_ffd)

print("\nРезультаты стратегии Best Fit:")
print_bins(bins_bf)

# Сравнение количества контейнеров
print("\nСравнение количества контейнеров:")
print(f"First Fit: {len(bins_ff)}")
print(f"First Fit Decreasing: {len(bins_ffd)}")
print(f"Best Fit: {len(bins_bf)}")


РЕЗУЛЬТАТ ВЫПОЛНЕНИЯ:
Результаты стратегии First Fit:
Контейнер 1: [7, 3] (заполнено: 10/10)
Контейнер 2: [5, 2] (заполнено: 7/10)
Контейнер 3: [8] (заполнено: 8/10)
Контейнер 4: [6, 4] (заполнено: 10/10)
Контейнер 5: [9] (заполнено: 9/10)

Результаты стратегии First Fit Decreasing:
Контейнер 1: [9] (заполнено: 9/10)
Контейнер 2: [8, 2] (заполнено: 10/10)
Контейнер 3: [7, 3] (заполнено: 10/10)
Контейнер 4: [6, 4] (заполнено: 10/10)
Контейнер 5: [5] (заполнено: 5/10)

Результаты стратегии Best Fit:
Контейнер 1: [7, 3] (заполнено: 10/10)
Контейнер 2: [5] (заполнено: 5/10)
Контейнер 3: [8, 2] (заполнено: 10/10)
Контейнер 4: [6, 4] (заполнено: 10/10)
Контейнер 5: [9] (заполнено: 9/10)

Сравнение количества контейнеров:
First Fit: 5
First Fit Decreasing: 5
Best Fit: 5
