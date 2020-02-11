package queMePongo.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum Frecuencia {
	ANUAL{
 
		@Override
		public LocalDateTime proximaEjecucion(LocalDateTime ultimaFechaEjecucion) {
			return ultimaFechaEjecucion.plusYears(1);
		}

	},
	
	MENSUAL{
		
		@Override
		public LocalDateTime proximaEjecucion(LocalDateTime ultimaFechaEjecucion) {
			return ultimaFechaEjecucion.plusMonths(1);
		}
	},
	
	SEMANAL{

		@Override
		public LocalDateTime proximaEjecucion(LocalDateTime ultimaFechaEjecucion) {
			return ultimaFechaEjecucion.plusWeeks(1);
		}

	},
	
	DIARIA{

		@Override
		public LocalDateTime proximaEjecucion(LocalDateTime ultimaFechaEjecucion) {
			return ultimaFechaEjecucion.plusDays(1);
		}
	};
	
	public abstract LocalDateTime proximaEjecucion(LocalDateTime ultimaFechaEjecucion);
	
	public List<LocalDateTime> fechasEjecucion(LocalDateTime ultimaFechaEjecucion, LocalDateTime fechaHasta) {
		List<LocalDateTime> fechasEjecucion = new ArrayList<>();
		LocalDateTime proximaFechaEjecucion = this.proximaEjecucion(ultimaFechaEjecucion);
		while(proximaFechaEjecucion.isBefore(fechaHasta)) {
			fechasEjecucion.add(proximaFechaEjecucion);
			proximaFechaEjecucion = this.proximaEjecucion(proximaFechaEjecucion);
		}
		return fechasEjecucion;
	}
}
