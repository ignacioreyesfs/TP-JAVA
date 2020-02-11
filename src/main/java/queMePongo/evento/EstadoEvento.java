package queMePongo.evento;

import queMePongo.evento.Evento;

public enum EstadoEvento {
	ACTIVADO{
		@Override
		public void buscarSugerencias(Evento evento) {
			evento.conseguirSugerencias();
		}
	},
	DESACTIVADO{
		@Override
		public void buscarSugerencias(Evento evento) {}
	};
	
    public abstract void buscarSugerencias(Evento evento);
}
