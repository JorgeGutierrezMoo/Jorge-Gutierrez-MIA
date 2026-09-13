# Ejercicio 1 — Cambiar la imagen de predicción en YOLO - Jorge Gutiérrez

## Contexto

La notebook `Visión computacional/Notebooks/13 YOLO ultralytics.ipynb` es un
tutorial corto de **YOLOv8** (paquete Ultralytics) pensado para **Google
Colab**. Hace tres cosas:

1. Instala `ultralytics` y comprueba el entorno.
2. Corre inferencia por CLI sobre la foto de muestra `zidane.jpg`.
3. Carga `yolov8n.pt`, entrena **3 épocas** en `coco128` y predice
   `bus.jpg`.

YOLO detecta objetos de las clases **COCO** (persona, auto, bus, corbata,
etc.) y dibuja cajas. En este ejercicio **sí vas a modificar código**, pero
solo un cambio pequeño y visible: **la imagen sobre la que predice**.

No toques `Visión computacional/project/`. Trabajas en **Colab**, sobre una
**copia** de la notebook.

## Objetivo

Correr la notebook en Colab **tal como está**, sustituir las dos imágenes de
muestra por **una imagen tuya** (la misma en ambas predicciones) y comparar
qué objetos detecta YOLO en la foto original frente a la tuya.

## Reporte de resultados

- ¿Qué clases detectó YOLO en las fotos de Ultralytics y cuáles en la tuya?
  En zidane.jpg fueron humanos y la corbata. En bus.jpg fue el autobus, humanos y la señal de alto del bus.
  Ahora, en mi imagen detectó los vasos, la mesa y los vasos de vino (no era vino, pero detectó los vasos).
- ¿Algún objeto evidente de tu foto no salió etiquetado? ¿Por qué podría pasar (clase que no está en COCO, objeto chico,
  recorte, umbral de confianza)?
  No se detecto el plato de comida y ninguno de los alimentos en el plato. Definitivamente es debido a que las clases no
  están en COCO. Otros objetos más difíciles de identificar, como la mesa misma, fueron reconocidos. Específicamente,
  los vasos que están cortados y las copas de vino también se reconocieron de buena manera.
- ¿La predicción de la celda CLI y la de model(...) coinciden sobre tu misma imagen?
  Sí, coincidieron de manera exacta.

Mi imagen de prueba es una fotografía de una comida donde se tiene un plato repleto de diferentes tipos de alimentos, que,
por cierto, no están en COCO. Además, hay varios vasos alrededor, entre los que unos contenían agua pero fueron identificados
como de vino, quizá por la forma.
Todo esto se tiene sobre una mesa convencional de manera, la cual a pesar de no verse completamente fue bien identificada.