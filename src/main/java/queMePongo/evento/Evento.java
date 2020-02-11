package queMePongo.evento;

import queMePongo.prenda.Atuendo;
import queMePongo.prenda.GeneradorAtuendos;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.Sugerencia;
import queMePongo.usuario.Usuario;
import javax.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Evento {

	@Id @GeneratedValue
	private Integer id;
	protected String nombre;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	protected Usuario usuario;
	@ManyToMany(cascade = CascadeType.PERSIST)
	protected Set<Guardarropa> guardarropas;
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "evento_id")
	@OrderColumn
	private List<Sugerencia> sugerencias = new LinkedList<Sugerencia>();
	protected LocalDateTime fecha;
	@Enumerated(EnumType.STRING)
	protected EstadoEvento estado;
	
    public abstract LocalDateTime getFechaEjecucion();
    public abstract void conseguirSugerencias();
    public abstract Boolean eventoYaTermino();
    public abstract List<Evento> eventosEntreFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
    public abstract void ejecutar();
    
	public List<Sugerencia> getSugerencias(){
		return this.sugerencias;
	}
	public String getNombre() {
		return nombre;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public Integer getId() {
		return id;
	}
	public LocalDateTime getFecha() { return fecha; }

	public void validarSugerenciasTemperatura() {
		if (!sugerencias.isEmpty() && !this.atuendosSonAdecuados()) {
			this.liberarSugerenciaAceptada();
			this.buscarSugerencias();
			usuario.notificarCambiosSugerencias(this);
		}
	}
	
	public void buscarSugerencias() {
		this.sugerencias = this.generarSugerencias();
	}
	
	public Boolean atuendosSonAdecuados() {
		return this.sugerencias == this.generarSugerencias();
	}
	
	private LinkedList<Sugerencia> generarSugerencias() {
		return this.generarAtuendos().stream()
				.map(atuendo -> new Sugerencia(atuendo))
				.collect(Collectors.toCollection(LinkedList::new));
	}

	public LinkedList<Atuendo> generarAtuendos() {
		GeneradorAtuendos generator = this.crearGeneradorAtuendos();
		return guardarropas.stream().map(g -> generator.sugerirAtuendosFecha(usuario, g, fecha))
				.flatMap(s -> s.stream())
				.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public void finalizar() {
		if (this.eventoYaTermino()) {
			this.liberarSugerenciaAceptada();
		}
	}
	
	public Boolean yaPasoLaFecha(LocalDateTime fecha) {
		return LocalDateTime.now().isAfter(fecha) 
				&& this.diferenciaHorasEsMayorA24(LocalDateTime.now(), fecha);
	}
	
	protected Boolean diferenciaHorasEsMayorA24(LocalDateTime fecha1, LocalDateTime fecha2) {
		Duration duration = Duration.between(fecha1, fecha2);
		long diferencia = duration.toHours();
		return diferencia >= 24;
	}
	
	protected Boolean diferenciaHorasEsMenorA24(LocalDateTime fecha1, LocalDateTime fecha2) {
		Duration duration = Duration.between(fecha1, fecha2);
		long diferencia = duration.toHours();
		return diferencia <= 24;
	}
	
	public Sugerencia getSugerenciaAceptada() {
		return this.sugerencias.stream().filter(sugerencia -> sugerencia.estaAceptada()).findFirst().orElse(null);
	}
	
	public void liberarSugerenciaAceptada() {
		if (this.getSugerenciaAceptada() != null)
			this.getSugerenciaAceptada().liberarPrendas();
	}
	
	public void desactivar() {
    	this.estado = EstadoEvento.DESACTIVADO;
    }

	//method for mock testing
	public GeneradorAtuendos crearGeneradorAtuendos() {
		return new GeneradorAtuendos();
	}

	

	
}
