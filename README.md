# Sugeridor de prendas

Trabajo practico desarrollado en la materia anual Diseño de Sistemas que consiste en una aplicación web desarrollada en  Java, html y jquery, utilizando maven, hibernate y deployanda con heroku.
QueMePongo es una aplicación web que, basandose en el/los guardarropas del usuario, recomienda los atuendos ideales para el evento al que se debe asistir; para ello tiene en cuenta la formalidad del evento, el clima del día y las preferencias del usuario.

## Funcionalidades

 - Cargar prendas a un guardarropas.
 - Cargar un evento, indicando la fecha del mismo.
	 - 24hs antes del evento (utilizando asincronismo), la aplicación genera las posibles combinaciones de prendas (atuendos) ideales para la ocasión, basandose en la temperatura a la hora del evento y las elecciones pasadas del usuario.
 - Calificar un atuendo utilizado.
	 - Un atuendo consiste en un conjunto de prendas con distintas capas (por ejemplo, un buzo es de capa 2) dependiendo la temperatura del día. Es obligatorio que cada atuendo conste de una parte superior, inferior y calzado, pero opcional el uso de accesorios.
	 - Calificar un atuendo consiste en indicar si el mismo le resulto adecuado para la temperatura del día. De esta manera, el algoritmo va ajustando la sensibilidad del usuario al frio/calor.


