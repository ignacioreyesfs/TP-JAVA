package queMePongo.usuario;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import queMePongo.evento.Evento;
import queMePongo.exceptions.PrendaRepetidaException;
import queMePongo.exceptions.SinNotificacionAUsuarioException;
import queMePongo.notificaciones.NotificadorObserver;
import queMePongo.prenda.Accion;
import queMePongo.prenda.Atuendo;
import queMePongo.prenda.Categoria;
import queMePongo.prenda.GeneradorAtuendos;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.NivelDeAbrigo;
import queMePongo.prenda.Prenda;
import queMePongo.prenda.Sugerencia;
import javax.persistence.*;

@Entity
public class Usuario {
	@Id @GeneratedValue
	private Integer id;
	private String user;

	private String password;
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo = TipoUsuario.GRATUITO;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<Guardarropa> guardarropas = new HashSet<>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "usuario_id")
	private List<Sugerencia> eleccionesHistory = new ArrayList<>();
	private String mail;
	private String telefono;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<NotificadorObserver> observers = new ArrayList<>();
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Sensibilidad sensibilidad;
	@Transient
	private Integer intentos;
	
	public Usuario() {}
	
	public Usuario(Sensibilidad sensibilidad) {
		this.sensibilidad = sensibilidad;
	}

	public Integer getId() {
		return id;
	}
	public String getUser() { return this.user; }
	public String getPassword() { return this.password; }
	public Set<Guardarropa> getGuardarropas() {
		return guardarropas;
	}
	public List<Sugerencia> getEleccionesHistory() {
		return eleccionesHistory;
	}
	
	public Guardarropa getGuardarropasById(int id) {
		return this.getGuardarropas().stream().filter(g -> g.getId() == id).findFirst().orElse(null);
	}

	public String getMail() { return mail; }

	public void addObserver(NotificadorObserver observer) { observers.add(observer); }

	public void removeObserver(NotificadorObserver observer) {
		observers.remove(observer);
	}

	public void agregarGuardarropas(Guardarropa guardarropa) {
		guardarropas.add(guardarropa);
	}

	public void agregarPrenda(Guardarropa guardarropa, Prenda prenda) {
		this.validarIngresoPrenda(prenda);
		tipo.agregarPrenda(guardarropa, prenda);
	}

	private void validarIngresoPrenda(Prenda prenda) {
		if (this.tienePrenda(prenda))
			throw new PrendaRepetidaException();
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public void setUser(String user) { this.user = user; }

	public void setPassword(String password) { this.password = password; }

	private boolean tienePrenda(Prenda prenda) {
		return guardarropas.stream().anyMatch(guardarropa -> guardarropa.contienePrenda(prenda));
	}

	public void aceptarSugerencia(Sugerencia sugerencia) {
		sugerencia.ejecutarAccion(Accion.ACEPTAR, this);
		this.eleccionesHistory.add(sugerencia);
	}

	public void addSugerenciaAHistory(Sugerencia sugerencia) {
		this.eleccionesHistory.add(sugerencia);
	}

	public void notificar(Evento evento) {
		intentos = 0;
		observers.forEach(lambdaWrapper(observer -> observer.notificarSugerenciasListas(this, evento)));

		if (intentos == observers.size()) throw new SinNotificacionAUsuarioException();
	}

	public void notificarCambiosSugerencias(Evento evento) {
		intentos = 0;
		observers.forEach(lambdaWrapper(observer -> observer.notificarCambiosSugerencias(this, evento)));

		if (intentos == observers.size()) throw new SinNotificacionAUsuarioException();
	}

	private Consumer<NotificadorObserver> lambdaWrapper(Consumer<NotificadorObserver> consumer) {
		return i -> {
			try {
				consumer.accept(i);
			} catch (Exception e) {
				intentos++;
			}
		};
	}

	public void ajustarSensibilidad(Categoria categoria, NivelDeAbrigo nivelDeAbrigo) {
		sensibilidad.ajustarPorcentajeExtraSensibilidad(categoria, nivelDeAbrigo);
	}

	public Sensibilidad getSensibilidad() {
		return sensibilidad;
	}

	public double getPorcentajeExtraSensibilidad(Categoria categoria) {
		return sensibilidad.getPorcentajeExtraSensibilidad(categoria);
	}

	public LinkedList<Atuendo> sugerirAtuendos(Guardarropa guardarropa) {
		return this.crearGeneradorAtuendos().sugerirAtuendosFecha(this, guardarropa, LocalDateTime.now());
	}
	//method for mock testing
	public GeneradorAtuendos crearGeneradorAtuendos() {
		return new GeneradorAtuendos();
	}
	
	public void eliminarGuardarropa(Guardarropa guardarropa) {
		this.guardarropas.remove(guardarropa);
	}
	
	public List<Sugerencia> getEleccionesSinCalificar() {
		return this.eleccionesHistory.stream().
				filter(sugerencia -> !sugerencia.getCalificado()).collect(Collectors.toList());
	}
}
