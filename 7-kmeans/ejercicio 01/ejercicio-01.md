# Ejercicio 1 — Separar los blobs y volver a elegir \(k\) - Jorge Gutiérrez

## Contexto

La notebook `Clustering K-medias/Notebooks/01 K-medias.ipynb` (capítulo de
Géron / Hands-On ML) entrena **k-means** de scikit-learn sobre nubes
gaussianas y termina con segmentación de color de una foto.

La parte central genera **5** blobs, ajusta `KMeans(n_clusters=5, ...)` y
después busca el \(k\) “bueno” con dos herramientas:

| Herramienta | Qué grafica | Lectura en la notebook original |
|---|---|---|
| **Codo** (inercia \(J\) vs \(k\)) | `inertias` para \(k = 1,\ldots,9\) | El codo está en **\(k = 4\)**, no en 5 |
| **Silueta** | `silhouette_score` para \(k = 2,\ldots,9\) | \(k = 4\) se ve muy bien; **\(k = 5\)** también |

Eso no es un bug: tres blobs de la izquierda están **casi pegados**
(`std = 0.1` y centros en \(x = -2.8\)). K-means (y el codo) los trata como
un solo grupo.

## Objetivo

Correr la notebook en Colab **tal como está**, anotar el \(k\) que sugieren
codo y silueta, **separar los 5 blobs** en el arreglo `blob_centers` (y, si
hace falta, `blob_std`) y volver a graficar. Debes ver si el codo y la
silueta se mueven hacia **\(k = 5\)**.

# Reporte

- En los datos de Géron, ¿por qué el codo “prefiere” (k = 4) si make_blobs usó 5 centros?
  El algoritmo K-Means, en su búsqueda por minimizar la inercia, tiende a fusionar clústeres que están muy cerca o se
  solapan significativamente. En este caso, era probable que el modelo considerara los tres clústeres en x=-2.8 como uno solo,
  o como parte de un clúster más grande que incluía también el de (-1.5, 2.3). Esta configuración hacía que el beneficio de añadir
  un quinto clúster (pasando de k=4 a k=5) en términos de reducción de inercia fuera marginal, lo que resultaba en un "codo" más 
  pronunciado en k=4.
- Con tus blobs separados, ¿el codo y la silueta coinciden en el mismo (k)? ¿Ese (k) es 5? Sí siguen coincidiendo en k = 4.
  Interpreto que ha de ser por el salto que es un valor de inercia mejor al que hay entre 3 y 4 por ejemplo. Yo prefiero que sea k = 5.
    
- Si el codo sigue en 4, ¿qué te falta mover (distancia entre centros vs. blob_std)?
    Si el codo de la inercia aún se inclina hacia k=4 a pesar de que el número real de clústeres es 5, esto sugiere que,
    aunque los blobs están más separados que en la configuración original de Géron, todavía no están lo suficientemente 
    distintos y compactos entre sí como para que la adición del quinto clúster produzca una caída de inercia tan drástica 
    que forme un codo claro en k=5. Para que el codo esté en K = 5 habría qué aumentar la distancia entre los cluster y disminuir
    la desviación estándar (blob_std) para que los cluster sean más "pequeños", es decir, menos dispersos.

  
| K-means    | Original           | Modificado         |
|------------|--------------------|--------------------|
| k3 inertia | 653.2167190021554  | 1055.6957689138324 |
| k8 inertia | 127.13141880461835 | 321.30517414282434 |
| k inertia  | 224.0743312251571  | 433.24908319473167 |

### Original
```
    Y
    ^
    |
3.5 +
    |
3.0 +   .                   O
    |        O
2.5 +   .
    |
2.0 + O       .
    |
1.5 + O . O
    |
1.0 + O       .
    |
0.5 +-----------------------> X
     -3.5  -2.5  -1.5  -0.5  0.5
```

blob_centers originales:
```
[[ 0.2,  2.3],
 [-1.5 ,  2.3],
 [-2.8,  1.8],
 [-2.8,  2.8],
 [-2.8,  1.3]]
 ```
blob_std original:
```[0.4, 0.3, 0.1, 0.1, 0.1]```


### Original
```
    Y
    ^
    |
3.5 +
    |        N
3.0 +   .                   N
    |
2.5 +   .
    |         N
2.0 + N
    |
1.5 + N
    |
1.0 +         N
    |
0.5 +-----------------------> X
     -3.5  -2.5  -1.5  -0.5  0.5
```
blob_centers nuevos:
```
[[ 0.2,  2.3],
 [-1.5 ,  3.0],
 [-2.8,  1.8],
 [-2.8,  2.8],
 [-2.0,  1.0]]
 ```
blob_std nuevo:``` [0.4, 0.3, 0.3, 0.3, 0.4]```

```O```: centros originales
```N```: centros nuevos
```.```: puntos que describen las puntos alrededor del centro.
