# **задача: реализовать Deep Q-Network для обучения агента играть в игру.**
**Требования:**
DQN архитектура с target network
Experience replay buffer
Epsilon-greedy exploration
Визуализация обучения

**Код-заготовка (Python):**
     
  #
  import tensorflow as tf
  
import collections

class DQNAgent:

def __init__(self, state_size, action_size, learning_rate=0.001):

self.state_size = state_size

self.action_size = action_size

self.learning_rate = learning_rate

#TODO: Создать Q-network и target network

self.q_network = self._build_q_network()

self.target_network = self._build_q_network()

#TODO: Инициализировать replay buffer

self.replay_buffer = collections.deque(maxlen=2000)

#Hyperparameters

self.gamma = 0.99 # discount factor

self.epsilon = 1.0 # exploration rate

self.epsilon_decay = 0.995

self.epsilon_min = 0.01

def _build_q_network(self):

#TODO: Построить Q-network

#Входные: state (state_size,)

#Выходные: Q-values для каждого action (action_size,)

#Архитектура:

#- Dense(64, activation='relu')

#- Dense(64, activation='relu')

#- Dense(action_size)

pass

def remember(self, state, action, reward, next_state, done):

#TODO: Сохранить опыт в replay buffer

self.replay_buffer.append((state, action, reward, next_state, done))

def act(self, state, training=True):

#TODO: Выбрать action используя epsilon-greedy

if training and np.random.rand() <= self.epsilon:

#Случайный action (exploration)

return np.random.randint(self.action_size)

#Greedy action (exploitation)

q_values = self.q_network(state[np.newaxis]).numpy()

return np.argmax(q_values)

def replay(self, batch_size):

#TODO: Обучить на batch из replay buffer

if len(self.replay_buffer) < batch_size:

return

#Случайно выбрать batch

batch = random.sample(self.replay_buffer, batch_size)

states, actions, rewards, next_states, dones = zip(*batch)

states = tf.convert_to_tensor(states)

actions = tf.convert_to_tensor(actions)

rewards = tf.convert_to_tensor(rewards, dtype=tf.float32)

next_states = tf.convert_to_tensor(next_states)

dones = tf.convert_to_tensor(dones, dtype=tf.float32)

#TODO: Вычислить target Q-values используя target network

#target = reward + gamma * max(target_network(next_state)) * (1 - done)

pass

def update_target_network(self):

#TODO: Обновить target network весами из q_network

self.target_network.set_weights(self.q_network.get_weights())

def train(self, env, episodes=100, batch_size=32, update_frequency=5):

#TODO: Основной цикл обучения

#for episode in range(episodes):

#state = env.reset()

#done = False

#while not done:

#action = self.act(state)

#next_state, reward, done = env.step(action)

#self.remember(state, action, reward, next_state, done)

#self.replay(batch_size)

#state = next_state

#if episode % update_frequency == 0:

#self.update_target_network()

#if self.epsilon > self.epsilon_min:

#self.epsilon *= self.epsilon_decay

pass

**Что нужно дополнить:**
1. Q-network архитектуру
2. Replay buffer
66
3. Epsilon-greedy стратегию
4. Replay function для обучения на batch
5. Target network обновление
6. Основной цикл обучения
7. Визуализацию learning curves


# Алгоритм работы НС по блокам
1. Инициализация
Задаются размеры входа и выхода сети: число параметров состояния игры (state_size) и число возможных действий (action_size).​

Создаются две одинаковые сети: Q‑network (основная, обучаемая) и target‑network (целевая, копия основной, обновляется реже).​

Инициализируется replay‑buffer (deque), куда будут записываться переходы (s,a,r,s′,done), и задаются гиперпараметры: коэффициент скидки 

2. Архитектура Q‑network
Входной слой принимает вектор состояния размерности state_size.​

Далее идут несколько полносвязных слоёв (например, 64 нейрона с ReLU), которые приближяют функцию Q(s,a;θ).​

Выходной слой имеет action_size нейронов без активации: каждое значение — оценка Q для соответствующего действия в данном состоянии.​

3. Выбор действий (epsilon‑greedy блок)
В начале обучения ϵ велико, поэтому с вероятностью ϵ агент выбирает случайное действие (exploration).​

С вероятностью 1−ϵ он подаёт текущее состояние в Q‑network и выбирает действие с максимальным Q‑значением (exploitation).​

После каждого эпизода ϵ умножается на коэффициент ϵ decay, но не опускается ниже ϵ min, поэтому агент постепенно переходит от исследования к использованию выученной стратегии.​

4. Сбор опыта и replay‑buffer
Агент выполняет выбранное действие в среде, получает новое состояние, награду и флаг завершения эпизода done.​

Кортеж (state,action,reward,next_state,done) записывается в replay‑buffer (очередь фиксированного размера, «старый» опыт вытесняется).​

Благодаря этому обучающий набор перемешивается, а корреляция между соседними шагами уменьшается, что стабилизирует градиентный спуск.​

5. Обучение на мини‑батчах (training блок)
Если в буфере достаточно элементов, случайно выбирается мини‑батч переходов заданного размера.​

Для каждого перехода вычисляется целевое значение:

если done = True, Q target=r;

Иначе Q target=r+γmax a ′ Q target_network (s ′,a ′ ).


Q‑network выдаёт предсказанные значения Q(s,a;θ); берутся только значения для реально совершённых действий в батче.​

Считается loss (например, MSE) между вектором Q target
  и предсказанными значениями, затем по этому лоссу с помощью оптимизатора (Adam) обновляются веса Q‑network.​

6. Target‑network и стабильность обучения
Target‑network не обучается напрямую на каждом шаге, а периодически копирует веса из Q‑network (каждые N эпизодов или шагов).​

При вычислении целевого Q target
  используется именно target‑network, поэтому целевые значения меняются более плавно и обучение становится устойчивее.​

Таким образом разрывается «обратная связь», когда сеть пытается подогнаться к целям, которые сама же непрерывно меняет.​

7. Метрики: награда, ошибка, точность
За каждый эпизод считается суммарная награда — это основной показатель, насколько хорошо агент играет в игру.​

Средний loss по эпизоду показывает, насколько сильно текущие предсказания Q отличаются от целевых Q target;
его уменьшение обычно сигнализирует о стабилизации обучения.​

«Точность» можно определить как долю шагов, где выбранное агентом действие совпало с greedy‑действием 
argmaxQ(s,a); рост этой величины говорит о всё более уверенном использовании выученной стратегии.

# Контрольный вопрос 18
**18. Как Affinity Propagation автоматически определяет количество кластеров?**

Affinity Propagation автоматически определяет количество кластеров через итеративный обмен сообщениями "ответственность" (responsibility) и "доступность" (availability) между парами точек данных. Каждая точка оценивает, насколько она подходит в качестве эксемпляра (центра кластера) для других, на основе матрицы сходства и параметра предпочтения (preference). В итоге наиболее подходящие эксемпляры выделяются как центры, а их число emerges естественно из структуры данных без предварительного задания.
