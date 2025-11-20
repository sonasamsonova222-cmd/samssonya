###10 ВАРИАНТ 
Колония муравьёв (ACO) для TSP

import random
import numpy as np

def build_path(pheromone, dist):
    n = len(dist)
    path = []
    current = random.randint(0, n - 1)
    path.append(current)
    
    visited = [False] * n
    visited[current] = True
    
    for _ in range(n - 1):
        next_city = select_next_city(current, visited, pheromone, dist)
        path.append(next_city)
        visited[next_city] = True
        current = next_city
    
    return path

def select_next_city(current, visited, pheromone, dist, alpha=1, beta=2):
    n = len(dist)
    unvisited = [j for j in range(n) if not visited[j]]
    
    if not unvisited:
        return current
    
    # Вычисляем вероятности перехода
    probabilities = []
    total = 0.0
    
    for j in unvisited:
        pheromone_factor = pheromone[current][j]  alpha  # Исправлено:  вместо *
        distance_factor = (1.0 / dist[current][j])  beta  # Исправлено:  вместо *
        prob = pheromone_factor * distance_factor
        probabilities.append(prob)
        total += prob
    
    # Нормализуем вероятности
    if total == 0:
        # Если все вероятности нулевые, выбираем случайно
        return random.choice(unvisited)
    
    probabilities = [p / total for p in probabilities]
    
    # Выбираем город по вероятностям
    next_city = np.random.choice(unvisited, p=probabilities)
    return next_city

def path_length(path, dist):
    length = 0
    for i in range(len(path) - 1):
        length += dist[path[i]][path[i + 1]]  # Исправлено: [] вместо ()
    # Возвращаемся в начальный город
    length += dist[path[-1]][path[0]]
    return length

def update_pheromones(pheromone, all_paths, dist, rho=0.1, Q=1.0):
    n = len(pheromone)
    # Испаряем феромоны
    for i in range(n):
        for j in range(n):
            pheromone[i][j] *= (1 - rho)
    
    # Добавляем феромоны по пройденным путям
    for path, length in all_paths:
        if length == 0:
            continue
        delta = Q / length
        for i in range(len(path) - 1):
            u, v = path[i], path[i + 1]
            pheromone[u][v] += delta
            pheromone[v][u] += delta  # симметричная матрица
        # Возвращаемся в старт
        u, v = path[-1], path[0]
        pheromone[u][v] += delta
        pheromone[v][u] += delta

def aco_tsp(dist, n_ants, n_iter):
    n = len(dist)
    pheromone = [[1.0 for _ in range(n)] for _ in range(n)]
    best_path = None
    best_length = float('inf')
    
    for iter in range(n_iter):
        all_paths = []
        for ant in range(n_ants):
            path = build_path(pheromone, dist)
            length = path_length(path, dist)
            all_paths.append((path, length))
            
            if length < best_length:
                best_length = length
                best_path = path
        
        update_pheromones(pheromone, all_paths, dist)
    
    return best_path, best_length

print("Введите количество городов:")
n = int(input())

print(f"Введите матрицу расстояний {n}x{n} (построчно, числа через пробел):")
dist = []
for i in range(n):
    row = list(map(float, input().split()))
    if len(row) != n:
        raise ValueError(f"В строке {i+1} должно быть {n} чисел!")
    dist.append(row)

print("Введите количество муравьёв:")
n_ants = int(input())

print("Введите количество итераций:")
n_iter = int(input())

best_path, best_length = aco_tsp(dist, n_ants, n_iter)

print("\nЛучший найденный маршрут:")
print(" -> ".join(map(str, best_path)) + " -> " + str(best_path[0]))
print(f"Длина маршрута: {best_length}")


## Пример использования:
Введите количество городов:
4
Введите матрицу расстояний 4x4 (построчно, числа через пробел):
0 10 15 20
10 0 35 25
15 35 0 30
20 25 30 0
Введите количество муравьёв:
10
Введите количество итераций:
100

Лучший найденный маршрут:
3 -> 2 -> 0 -> 1 -> 3
Длина маршрута: 80.0
