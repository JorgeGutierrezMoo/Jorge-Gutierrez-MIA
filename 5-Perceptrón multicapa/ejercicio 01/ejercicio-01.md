# Ejercicio 1 — Más capas en el perceptrón multicapa (Iris) - Jorge Gutiérrez

## Contexto

En `Perceptrón multicapa/Notebooks/` hay dos notebooks que resuelven el mismo
problema: clasificar las **3 especies** del conjunto **Iris** (4 atributos:
sépalo/pétalo en largo y ancho). Ambas redes son un MLP con activación
**sigmoide**, error **MSE**, **SGD** con \(\eta = 0.03\) y **500 épocas**.

| Notebook | Cómo está implementada | Topología inicial |
|---|---|---|
| `01 Multilayer perceptron.ipynb` | A mano (NumPy): forward, error y backprop | \(4 \times 3 \times 3\) |
| `02 Keras - multilayer perceptron - iris.ipynb` | Keras / TensorFlow (`Sequential`) | \(4 \times 3 \times 3\) |

En la notebook 01, \(4 \times 3 \times 3\) significa: **4** entradas, **una**
capa oculta de **3** neuronas y **3** neuronas de salida (una por clase). En
Keras es lo mismo: dos `Dense(3)` (la primera con `input_shape=(4,)`).

En este ejercicio **sí vas a modificar código**, pero no el de
`Perceptrón multicapa/project/`. Trabajas **en Colab**, sobre **copias** de las
dos notebooks.

## Objetivo

Correr ambas notebooks en **Google Colab** con la arquitectura original,
**agregar dos capas** a cada red, volver a entrenar y **comparar** qué cambia
(curva de error/pérdida, velocidad, calidad de la clasificación).

## Reporte de resultados

| Implementation                             | Topology                          | Initial Error/loss (Epoch 0) | Final Error/loss (Epoch 500) |
|--------------------------------------------|-----------------------------------|------------------------------|------------------------------|
| `Numpy Multilayer perceptron original`     | 4 x 3 x 3                         | 0.710034                     | 0.060168                     |
| `Numpy Multilayer perceptron modificado`   | 4 x 3 x 3 x 3 x 3                         | 0.785299                     | 0.160621                     |
| `Keras - multilayer perceptron original`   | 4 x 3 x 3   | 0.28054457902908325          | 0.20460690557956696          | 
| `Keras - multilayer perceptron modificado` | 4 x 3 x 3 x 3 x 3   | 0.23745620250701904                             | 0.22127123177051544                             |

- ¿Bajar más el error al añadir dos capas, o se estancó / empeoró? ¿Igual en NumPy y en Keras? lo empeoró de en ambos casos
pero se notó más en NumPy de acuerdo a los datos registrados en la tabla de arriba.
- ¿Las curvas de la notebook manual y de Keras se parecen con la misma topología? Si no, ¿qué diferencias de implementación
podrían explicarlo (orden de los datos, inicialización, vectorización, etc.)? No se parecen con la misma topología.
  En ambos casos del NumPy parece asentarse(y volverse constante) el error antes de descender de nuevo.
  Em ambos casos del Keras se parece pero en el 4 x 3 x 3 x 3 x 3 tiene una zona donde disminuye más rápido el error.
  Una de las razones puede ser que en NumPy se toma una flow, se calcula el error y se actualizan los datos. Luego de esto
  se va a la siguiente flow. Ahora, en Keras se toman batches de 32 muestras, se toma el promedio antes de hacer un ajuste a los pesos y
  por ende la curva es más suave.
- Con sigmoides apiladas y MSE, ¿tiene sentido que una red más profunda no aprenda mejor en Iris? Relaciónalo con lo que viste en las gráficas.
  Sí, tiene sentido debido al Desvanecimiento del Gradiente. Cuando la retropropagación sucede, el error sale de la última capa
  y debe viajar hasta el inicio para poder indicar a las primeras capas cuánto cambiar. Ahora, La función sigmoide tiene una trampa: su
  derivada matemática máxima (su capacidad de transmitir la fuerza del error) es de solo 0.25.Al encadenar 4 capas de sigmoides consecutivas (las 3 ocultas más la de salida),
  al multiplicar esas fuerzas. Entonces el error de la salida ahora es tan pequeña que el cambio que hacen las capas iniciales es apenas notable.