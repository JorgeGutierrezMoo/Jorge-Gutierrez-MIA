# Reporte - Jorge Gutiérrez

```
4 | .  .  .  .
3 | G  W  P  .
2 | P  .  P  .
1 | >  .  .  .
    1  2  3  4
```
```
G: oro
W: wumpus vivo
w: wumpus muerto
P: pit
>: agente viendo hacia el este (derecha).
```

Al correr el programa con los agentes correspondientes, el resultado fue como sigue:
- python 02_simple_reflex_agent.py --config config/mi_cueva_4x4.yaml:
  - dio vueltas en su lugar, similar a como había sucedido con la otra configuración. En este caso 	no avanzó ni una casilla ya que tenía un Pit a su izquierda todo el tiempo. 200 pasos.
  - moviendo de lugar los pits y oro, siempre que se encontró con un pit empezó a dar vueltas y hasta que se acabaron sus pasos.
- python 03_model_based_agent.py  --config config/mi_cueva_4x4.yaml
  - Se quedó dando vueltas en su lugar hasta alcanzar el límite de 200 pasos.
  - 
- python 04_goal_based_agent.py   --config config/mi_cueva_4x4.yaml
  - Se quedó dando vueltas en su lugar hasta alcanzar el límite de 200 pasos.
- python 05_utility_based_agent.py --config config/mi_cueva_4x4.yaml
  - Se logró en 27 pasos, en los que vale la pena mencionar que no fue la mejor ruta.
- python 06_learning_agent.py --episodes 1500 --config config/mi_cueva_4x4.yaml
  - No se logró el objetivo utilizando los 1500 episodes.
  - Fui elevando los episodes hasta 1,000,000 y funcionó.
  - Fui bajando los episodes y quedé en 700,000 para encontrar el oro.

El hecho que los agentes Simple reflex, Model based y Goal based hayan tenido el mismo comportamiento
lo atribuyo a cómo el Agente tiene un Pit inmediatamente a su izquierda. Además, el utility parece solo ir
iterando sobre las filas primero.

Respecto de la variante dificil de mi cueva:

```
4 | P  P  P  G 
3 | .  .  .  W 
2 | .  .  .  . 
1 | >  .  .  . 
    1  2  3  4
```

El único agente que logró obtener un resultado positivo fue el Utility based.
El Learning agent pasó hasta por 60,000,000 episodes y no se logró nada más que escalar sin tomar el oro ni realizar otra acción.