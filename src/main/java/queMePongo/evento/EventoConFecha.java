package queMePongo.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import queMePongo.exceptions.ErrorConFechaException;
import queMePongo.prenda.Guardarropa;
import queMePongo.usuario.Usuario;

@Entity
public class EventoConFecha extends Evento {
	public EventoConFecha() {}

	public EventoConFecha(Usuario usuario, String nombre, LocalDateTime fecha, Set<Guardarropa> guardarropas) {
        this.nombre = nombre;
        this.validarFecha(fecha);
        this.fecha = fecha;
        this.guardarropas = guardarropas;
        this.usuario = usuario;
        this.estado = EstadoEvento.ACTIVADO;
    }

	@Override
	public Boolean eventoYaTermino() {
		return this.yaPasoLaFecha(fecha);
	}

	@Override
	public void conseguirSugerencias() {
		this.buscarSugerencias();
	}

	@Override
	public LocalDateTime getFechaEjecucion() {
		return fecha.minusDays(1);
	}
	
	public void validarFecha(LocalDateTime fecha) {
		if(fecha.isBefore(LocalDateTime.now().plusMinutes(10)))
			throw new ErrorConFechaException();
	}

	@Override
	public List<Evento> eventosEntreFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		List<Evento> eventosEntreFechas = new ArrayList<>();
		if(fecha.isAfter(fechaDesde) && fecha.isBefore(fechaHasta))
			eventosEntreFechas.add(this);
		return eventosEntreFechas;
	}

	@Override
	public void ejecutar() {
		if(this.esMomentoDeEjecutar()) {
			estado.buscarSugerencias(this);
			this.estado = EstadoEvento.DESACTIVADO;
			usuario.notificar(this);
		}
	}

	private boolean esMomentoDeEjecutar() {
		return LocalDateTime.now().isBefore(fecha)
				&& this.diferenciaHorasEsMenorA24(LocalDateTime.now(), fecha);
	}
}
