# Ejercicio 1 — Comparar BFS, UCS, DFS, DLS e IDS en el mapa de Rumania - Jorge Gutiérrez

## reporte

- Acerca de BFS Y UCS:
  - BFS es una cola convencional FIFO para explorar por niveles de profundidad.
  - UCS usa una cola de prioridad. No es LIFO, ni FIFO.
  - BFS encuentra el camino con la menor cantidad de pasos.
  - UCS encuentra el camino más barato, sin tomar en cuenta la cantidad de pasos.
  - BFS trata todas las conexiones con el mismo peso.
  - UCS procesa pesos variables (distancias o tiempo).
  - BFS es útil cuando se busca encontrar la ruta con menos movimientos o pasos.
  - UVS es útil cuando las conexiones en el grafo tienen costos varios y se necesita el menor costo (tiempo, distancia o consumo que se traduce en dinero).
  - BFS es un caso especial de UCS donde cada arista tiene un costo 1.

BFS encontró el camino con menos pasos o caminos en este caso.
UCS encontró el camino con menos kilómetros pero más pasos.

 - Acerca de IDS y BFS: Coinciden en la profundidad (número de carreteras o pasos), con 4. La diferencia entre ambos métodos radica en la manera en que la respuesta es obtenida ya que IDS es una pila LIFO.

- ¿Qué pasó con DLS en el límite bajo frente al límite suficiente? Con límite 2 no encontró Urziceni, como tampoco lo hubiera hecho con 3, 0 y 1. Con límite 4 se cuentra a Urziceni siguiendo: Sibiu → Fagaras → Fagaras → Bucharest.

- ¿Por qué DFS puede devolver un camino más largo aunque el grafo sea el mismo? Para DFS no importa el número de pasos ya que se toma una decisión y esta se sigue hasta que ocurre un error o logra éxito. Si DFS inicia en Oradea y elige seguir con Zerid (y además, no falla, como en este caso), tomará ese camino y no volverá a Sibiu hasta que esta ruta falle enteramente.

- ¿Con qué --limit DLS pasó de cutoff a solución, y cómo se relaciona eso con la profundidad del camino de BFS/IDS? 
Fue con limit = 4.
Por la manera en que BFS explora el grado, dice que su meta más cercana está a n aristas (4 en este caso).
IDS ejecuta continuamente DLS pero incrementando el límite. IDS se detiene cuando el limit = n.
DLS deja de arrojar cutoff y devuelve 4, el cual es el mismo número solución que dio BFS y a su vez es el valor de éxito que devuelve IDS.