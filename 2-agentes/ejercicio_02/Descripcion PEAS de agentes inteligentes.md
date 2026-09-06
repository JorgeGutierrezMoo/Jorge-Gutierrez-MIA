# Descripción PEAS de agentes inteligentes - Jorge Gutiérrez

```
Letra 	    Significado 	                  Pregunta guía
P 	    Performance (medida de desempeño) 	  ¿Cómo se evalúa el éxito del agente?
E 	    Environment (entorno) 	          ¿En qué mundo opera? ¿Quién más actúa ahí?
A 	    Actuators (actuadores) 	          ¿Qué acciones puede ejecutar?
S 	    Sensors (sensores) 	                  ¿Qué información puede percibir?
```
## Objetivo
Para cada una de las 8 aplicaciones listadas abajo, redacta una descripción PEAS completa y coherente.
Debes pensar como diseñador del agente: qué optimiza, dónde actúa, con qué puede mover o modificar el mundo, y qué puede observar.

## Aplicaciones a analizar
Describe PEAS para cada una de estas aplicaciones:

    Asistente virtual de voz (p. ej. Siri, Alexa o Google Assistant en un altavoz inteligente).
    Robot aspirador doméstico (p. ej. Roomba u otro robot que limpia pisos de un departamento).
    Sistema de recomendación de streaming (p. ej. Netflix o Spotify que sugiere películas o canciones).
    Vehículo autónomo en ciudad (conducción sin conductor en calles urbanas con tráfico y peatones).
    Agente de trading algorítmico en bolsa (compra y venta automática de acciones en mercados financieros).
    Sistema de diagnóstico médico asistido por IA (apoya a un médico a interpretar síntomas e imágenes clínicas).
    Dron de inspección de infraestructura (revisa grietas, corrosión o fugas en puentes, tuberías o líneas eléctricas).
    Agente jugador de ajedrez (programa que compite contra un humano u otro agente en partidas completas).


### 1. Asistente virtual de voz - Comandos para inventario por voz Nova Sonic.
- **Performance:**
  - Precisión con la que empieza a detectar voz.
  - Precisión con la que detecta las palabras escuchadas.
  - Baja velocidad de respuesta.
  - Precisión con la que reconoce los comandos específicos para su funcionamiento.
  - Baja reacción al ruido no causado por voz.
  - Nula reacción al ruido causado por sí mismo al hablar de vuelta.
- **Environment:**
  - Ruido generado por otras voces.
  - Ruido generado por sí mismo al hablar.
  - Voz del usuario.
- **Actuators:**
  - Altavoz integrado.
  - Pantalla (que dé información al usuario).
- **Sensors:** 
  - Micrófonos integrados.
  - Pantalla (que reciba input del usuario).
- Parcialmente observable: no se sabe toda la información de todos los artículos y lugares.
- No determinista: puede colarse ruido de fondo o el usuario podría decir un comando no reconocido o dar un comando que ejecute un resultado no desado.
- Secuencial: depende de acciones o comandos previos para decidir la siguiente acción.
- Dinámico: la cantidad y ubicación de los products siempre puede cambiar por cosas ajenas a la conversación actual.
- Discreto.

### 2. Robot aspirador doméstico
- **Performance:** 
  - Porcentaje de la habitación que deja limpio.
  - Si limpia las variedades comunes de basura.
  - Tiempo que se toma en realizar una limpieza de la habitación.
  - Tiempo que le toma en mapear toda la habitación para futura referencia.
  - Precisión con la que detecta objetos en colisión.
  - Variedad de superficies sobre las que puede actuar.
  - Autonomía de la batería.
  - Precisión y efectividad de los pasos para ir a su dock al terminar o necesitar carga.
- **Environment:** 
  - Habitación, casa, espacios cerrados.
  - Objetos en el camino como muebles, basura o seres vivos.
  - Superficies irregulares (losas rotas) o desnivel (subidas y bajadas).
- **Actuators:** 
  - Motores para las ruedas.
  - Motor/es para la escoba que tiene debajo.
  - Motor/es para succión de polvo y/o agua.
  - Bocina.
  - Pantalla o luces led.
- **Sensors:**
  - Sensor de choque para deliminar límite de la habitación y obstáculos encontrados.
  - Sensor para evitar la caída. Están situados al frente y el objetivo leer la distancia al suelo en un cambio de altura.
  - Cámara.
- Parcialmente observable: no se sabe toda la información de todos los lugares de la casa o incluso el cuarto.
- No determinista: puede caminar una persona o mascota en su camino y de igual manera dejar caer algo que genere un obstáculo inesperado o que dañe el robot.
- Secuencial: luego de una sesión de limpieza el paso lógico, por ejemplo, sería ir al doc a recargarse. Lo mismo si ya detecta tener batería baja.
- Dinámico: una persona puede intervenir de mil maneras sobre el robot ya sea levantándolo o incluso por lluvia que pudiera generar barro en la basura que se está por limpiar.
- Continuo: similar al ejemplo del taxi, esto se da en un entorno real donde todo ocurre de manera continua.

### 3. Sistema de recomendación de streaming
- **Performance:** 
  - Métricas de interacción como clicks, ubicaciones (en la aplicación), tiempo de retención.
  - Precisión con la que las recomendaciones coinciden con los gustos del usuario.
  - Precisión con la que se sugiere nuevos títulos que sean del interés del usuario.
- **Environment:** 
  - Usuarios que sirven de referencia para cruzar con información del usuario actual y obtener una lista potencial de recomendaciones.
  - Series, películas, documentales, etc. Esto es lo que se le recomendará al usuario actual.
  - Usuario principal o actual a quien se le hará las recomendaciones.
- **Actuators:** 
  - Dispositivo donde corre la aplicación que contiene el sistema de recomendación.
  - UI/UX de dicha aplicación.
  - Notificaciones acerca de recomendaciones al usuario: notificaciones en la app, push, por sms y por email.
- **Sensors:** 
  - Méticas de interacción con las recomendaciones: clicks, vistas, tiempo de retención.
  - Chequeo de búsquedas realizadas por el usuario y su coincidencia con las recomendaciones realizadas.
- Parcialmente observable: la interacción solo puede ser registrada dentro de la aplicación. Aunque busque las recomendaciones fuera, no hay manera de saberlo.
- No determinista: el usuario podría dar dislike a todas las comedias lo que causaría que no se le recomienden, pero aún así, buscar por su cuenta o incluso prestar su cuenta a algún otro usurio.
- Secuencial: las recomendaciones afectan lo que a futuro verá en su feed, ya sea que las rechace o no.
- Dinámico: el catálogo multimedia cambia mucho, lo que puede cambiar los gustos del usuario y por ende las recomendaciones.
- Discreto: hay un ser de acciones que el sistema puede realizar. Además, también el catálogo de títulos es limitado.

### 4. Vehículo autónomo en ciudad
- **Performance:**
  - Precisión con la que se mantiene en la carretera.
  - Tiempo que toma en llegar a un destino.
  - Si tiene una muy buena efectividad en situaciones de alto tráfico.
  - Efectividad en ruta (ya sea medido por combustible utilizado, por tiempo o distancia).
- **Environment:** 
  - Carreteras.
  - Peatones.
  - Policías de tránsito.
  - Otros autos.
  - Conductores.
  - Indicadores viales.
  - Clima.
- **Actuators:**
- Controles del vehículo (clutch, freno, acelerador, palanca de cambios, volante y luces).
- **Sensors:** 
  - Cámaras.
  - Gps.
- Parcialmente observable: el vehículo solo sabe lo que tiene alrededor. No puede saber lo que hay en el lugar destino o si algún percance está por pasar.
- No determinista: la ciudad es impredecible.
- Secuencial: las decisiones anteriores afectan las siguientes, como con la ruta o si se perdió un retorno o salida.
- Dinámico: todo el entorno cambia constantemente.
- Discreto: como con el ejemplo del taxi, este es continuo pues es la realidad en donde discretizar los valores no es posible.

### 5. Agente de trading algorítmico en bolsa
- **Performance:** 
  - Precisión con la que se realizan transacciones de manera exitosa.
  - Alto porcentaje de ganancias.
  - Bajo porcentaje de pérdidas.
  - Precisión para identificar tiempo correcto para una transacción.
- **Environment:** 
  - Bolsa (o donde sea que se hagan las transacciones).
  - Usuarios (o el equivalente de quienes realizan las transacciones).
- **Actuators:** 
  - Acciones de una transacción (comprar, vender, retrasar o cancelar).
- **Sensors:** 
  - Balance (lo que se ha ganado y perdido en el tiempo).
  - Noticias que pueden afectar subida o caída de acciones.
  - Oferta y demanda.
- Parcialmente observable: no se puede saber todas las estrategias de los demás usuarios.
- No determinista: un entorno completamente afectado por el entorno social. La declaración de un político al otro lado del mundo puede afectar gravemente o como cuando Cristiano Ronaldo rechazó la Coca y prefirió agua purificada.
- Secuencial: las decisiones anteriores afectan las siguientes, como con cuánto dinero cuentas para invertir o si lo perdiste todo en el último movimiento.
- Dinámico: todo el entorno cambia constantemente.
- Discreto: bien podría ser discretizado porque las cantidades se puede expresar como centavos, pero las posibilidades son tantas alrededor que yo diría continuo ya que es directamente afectado por el mundo real.

### 6. Sistema de diagnóstico médico asistido por IA
- **Performance:**
  - Precisión con la que se realiza un diagnóstico.
  - Porcentaje de veces en que se realiza un diagnóstico correcto.
  - Rapidez con la que se realiza el diagnóstico.
  - Razón entre el diagnóstico y el tratamiento que lleve a un balance entre ser efectivo y no muy costoso.
- **Environment:** 
  - Hospitales o centros médicos.
  - Usuario o paciente.
  - Médicos o personal del área de la salud.
- **Actuators:** 
  - Dispositivo donde se ejecuta este sistema.
  - UI/UX del sistema ya que interactúa directamente con el usuario o paciente.
- **Sensors:**
  - Estudios realizados en el paciente.
  - Síntomas o entrada del paciente por medio de texto o voz.
- Parcialmente observable: no se puede conocer todo el contexto actual en el cuerpo del paciente o incluso en su mente si es un problema psiquiátrico.
- No determinista: un entorno completamente cambiante incluso por síntomas que el paciente haya decidido omitir.
- Secuencial: el tratamiento puede afectar el estado actual del cuerpo del usuario/paciente, de manera que se tiene qué adecuar al siguiente paso en la evolución de la enfermedad o síntma.
- Dinámico: todo el entorno cambia constantemente, y debido a cosas que el paciente no controle, como el clima.
- Discreto*: el conjunto de enfermedades o síntimas es finito y listable pero tiene valores a considerar* que son continuos, como lo puede ser medidas de presión, azúcar, temperatura o cantidades de otras sustancias en el cuerpo.

### 7. Dron de inspección de infraestructura
- **Performance:** 
  - Porcentaje de terreno cubierto.
  - Es pósible o no accesar a lugares difíciles y necesarios.
  - Es robusto o no para las condiciones bajo las que tiene qué operar (oscuridad, humedad, polvo, calor o goteras).
  - Autonomía de la batería.
- **Environment:** 
  - Tunel que supongo es de pieda, como para una carretera.
  - Tuberías (posiblemente con goteras), cablería o suciedad como polvo o telarañas.
  - Oscuridad
  - Humedad.
  - Polvo.
  - Sin acceso a internet.
- **Actuators:** 
  - Motor(es) para las hélices.
  - Motor(es) para las ruedas en caso de ser terrestre.
  - Luces LED.
  - Antena para emitir señal al receptor.
- **Sensors:** 
  - Cámaras (quizá con capacidad de ver otros espectros de luz).
  - Cámara térmica.
  - Sensor ultrasónico para evitar colisiones.
  - GPS.
  - Antena.
  - Giroscopio.
- Parcialmente observable: no se puede conocer todo entorno, para esto precisamente se necesita el dron.
- No determinista: un entorno físico está sujeto a cualquier cambio por clima o incluso un terremoto.
- Secuencial: similar al ejemplo del taxi, muchas cosas pueden afectar sin pista previa.
- Dinámico: todo el entorno cambia constantemente, y debido a cosas que el dron no controle, como el clima o algún animal viviendo en el tunel.
- Continuo: el espacio sobre el que anda es básicamente una carretera sin paviementar en donde hay muchos valores que hay qué tener en cuenta como ubicación, o inclinación.

### 8. Agente jugador de ajedrez
- **Performance:** 
  - Alto porcentaje de veces donde se gana.
  - Bajo tiempo en el que se gana.
  - Menor cantidad de movimientos para ganar.
  - Lo menos predecible posible
- **Environment:** 
  - Tablero.
  - Jugador contrario.
  - Piezas.
- **Actuators:** 
  - Cerebro (lo que calcula los movimientos).
  - Mano (lo que ejecuta el movimiento).
- **Sensors:** 
  - Ojos o cámara (lo que pueda ver el estado actual del tablero).
  - Memoria (poder recordar los movimientos realizados anteriormente).
  - Temporizador (dependiendo de la modalidad, se puede necesitar hacer un movimiento y ganar antes del tiempo estipulado).
- Completamente observable: en el tablero se tienen todas las variables y en donde están en todo momento durante el juego.
- Determinista: las posibles posiciones están a la vista, no existe un movimiento inesperado.
- Secuencial: el siguiente movimiento depende del anterior.
- Estático: el entorno sigue igual mientras piensa el agente, de hecho, para cambiar tiene qué esperar que el agente piense primero.
- Discreto: los estados y acciones tienen un número finito, numerable y esperable como el número de piezas posiciones y posibles movimientos de cada pieza dependiendo de dónde estén.