package queMePongo.evento;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import queMePongo.prenda.Guardarropa;
import queMePongo.usuario.Usuario;

@Entity
public class EventoRepetitivo extends Evento {

	private LocalDateTime fechaDeInicio;
	@ElementCollection(targetClass = Month.class)
	private Set<Month> meses = new HashSet<Month>();
	@ElementCollection(targetClass = DayOfWeek.class)
	private Set<DayOfWeek> diasSemana = new HashSet<DayOfWeek>();
	public LocalDateTime ultimaFechaEjecucion;
    @Enumerated(EnumType.STRING)
	private Frecuencia frecuencia;

    public EventoRepetitivo() {

	}
	
	public EventoRepetitivo(Usuario usuario, String nombre, Set<Guardarropa> guardarropas, Frecuencia frecuencia, LocalDateTime fechaDeInicio) {
        this.usuario = usuario;
		this.nombre = nombre;
        this.guardarropas = guardarropas;
        this.frecuencia = frecuencia;
        this.setUltimaFechaEjecucion(LocalDateTime.now());
        this.estado = EstadoEvento.ACTIVADO;
        this.setUltimaFechaEjecucion(fechaDeInicio);
        this.configurarEventoRepetitivo();
    }
	
	private void configurarEventoRepetitivo() {
		RepositorioEventos.getInstance().cargarEvento(this);
		this.ejecutar();
	}
	
	public void setDiaDeInicio(LocalDateTime nuevaFecha) {
        fechaDeInicio = nuevaFecha; 
    }

	//Este metodo ya quedo de mas
	@Override
	public Boolean eventoYaTermino() {
		return null;}

	//Este metodo ya quedo de mas
	@Override
	public void conseguirSugerencias() {}

	private void setUltimaFechaEjecucion(LocalDateTime ultimaFechaEjecucion) {
		this.ultimaFechaEjecucion = ultimaFechaEjecucion;
	}

	@Override
	public LocalDateTime getFechaEjecucion() {
		return frecuencia.proximaEjecucion(ultimaFechaEjecucion);
	}

	@Override
	public List<Evento> eventosEntreFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		return frecuencia.fechasEjecucion(this.ultimaFechaEjecucion, fechaHasta)
								.stream().map(fecha -> new EventoConFecha(usuario, nombre, fecha, guardarropas))
								.collect(Collectors.toList());
		
	}
	
	private EventoConFecha generarEventoProximo() {
		return new EventoConFecha(usuario, nombre, this.getFechaEjecucion(), guardarropas);
		}


	@Override
	public void ejecutar() {
		EventoConFecha proximoEvento = this.generarEventoProximo();
		RepositorioEventos.getInstance().cargarEvento(proximoEvento);
		this.setUltimaFechaEjecucion(LocalDateTime.now());
	}
}