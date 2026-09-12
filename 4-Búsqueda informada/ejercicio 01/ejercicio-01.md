# Ejercicio 1 — Comparar Greedy y A* en el mapa de Rumania - Jorge Gutiérrez

## Contexto

En el proyecto `Búsqueda informada/project` (AIMA cap. 3–4, Figuras 3.2 y 3.22)
se resuelve el problema de **encontrar una ruta** entre dos ciudades del mapa
carretero de Rumania. Dos algoritmos de búsqueda **informada** comparten el
mismo grafo, el mismo `RouteFindingProblem` y la misma heurística `h(n)`:

## Objetivo

Elegir una ruta distinta de Arad → Bucharest, inspeccionar `h(n)`, correr
Greedy y A*, y analizar diferencias de camino, costo, profundidad y nodos
expandidos a la luz de `g`, `h` y `f`.

## Reporte de resultados
### Greedy

| City        | g   | h   | f   |
|-------------|-----|-----|-----|
| `Oradea`    | 0   | 393 | 393 |
| `Sibiu`     | 151 | 271 | 422 |
| `Fagaras`   | 250 | 181 | 431 |
| `Bucharest` | 461 | 61  | 522 |
| `Urziceni`  | 546 | 0   | 546 |

### A* Search

| City             | g   | h   | f   |
|------------------|-----|-----|-----|
| `Oradea`         | 0   | 393 | 393 |
| `Sibiu`          | 151 | 271 | 422 |
| `Rimnicu Vilcea` | 231 | 231 | 462 |
| `Pitesti`        | 328 | 137 | 465 |
| `Bucharest`      | 429 | 61  | 490 |
| `Urziceni`       | 514 | 0   | 514 |

Greedy y A* devolvieron rutas distintas

Greedy evalúa únicamente el valor de la heurística h(n). Visualmente, la distancia en línea recta desde Fagaras a Urziceni
es menor que la de Rimnicu Vilcea, entonces elige Fagaras.
A* evalúa una función f(n) = g(n) + h(n), donde g(n) es el costo acumulado desde el inicio hasta el nodo actual. Se puede
leer como que se evalúa la distancia acumulada hasta aquí, y se le agrega el valor estimado de la línea recta faltante.
Aunque Rimnicu Vilcea parece estar un poco "más lejos" en línea recta, A* se ve que las carreteras reales por ese lado
(pasando por Pitesti) son más cortas.

¿A* encontró el camino de menos km? Si, fueron 514 km vs los 546 de Greedy ¿Greedy coincidió o se desvió? Se desvió,
con más distancia pero menos caminos.

¿Por qué Greedy puede devolver un camino más caro aunque h sea admisible? Porque Greedy no toma en cuenta el costo acumulado g(n).
A*
Oradea: f = 0 + 361 = 361
Sibiu: 151 + 328
Rimnicu Vilcea: f = 231 +  253
Pitesti: f = 328 + 159
Bucharest: f = 429  + 85
Urziceni: f = 514 + 0