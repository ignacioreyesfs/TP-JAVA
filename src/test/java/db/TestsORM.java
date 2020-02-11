package db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaOWM;
import queMePongo.evento.Evento;
import queMePongo.evento.EventoConFecha;
import queMePongo.evento.RepositorioEventos;
import queMePongo.prenda.Accion;
import queMePongo.prenda.Atuendo;
import queMePongo.prenda.GeneradorAtuendos;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.Material;
import queMePongo.prenda.Prenda;
import queMePongo.prenda.Sugerencia;
import queMePongo.prenda.TipoPrenda;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.TipoUsuario;
import queMePongo.usuario.Usuario;

public class TestsORM extends AbstractPersistenceTest implements WithGlobalEntityManager {
	private Usuario usuario;
	private Sensibilidad sensibilidad;
	private Guardarropa guardarropa;
	private Prenda remeraRoja;
	private Prenda pantalonNegro;
	private Prenda zapatillaAzulYBlanca;

	@Before
	public void init() throws Exception {
		sensibilidad = new Sensibilidad();
		usuario = new Usuario(sensibilidad);
    	
		usuario.setTipo(TipoUsuario.GRATUITO);
		guardarropa = new Guardarropa("Guardarropa");
		remeraRoja = new Prenda("remera roja", TipoPrenda.REMERA, Material.ALGODON, Color.RED, Color.RED, null);
		pantalonNegro = new Prenda("pantalon negro", TipoPrenda.PANTALON, Material.CUERO, Color.BLACK, Color.BLACK, null);
		zapatillaAzulYBlanca = new Prenda("zapatilla azul y blanca", TipoPrenda.ZAPATILLAS, Material.CUERO, Color.BLUE, Color.WHITE,null);
		usuario.agregarGuardarropas(guardarropa);
		usuario.agregarPrenda(guardarropa, remeraRoja);
		usuario.agregarPrenda(guardarropa, pantalonNegro);
		usuario.agregarPrenda(guardarropa, zapatillaAzulYBlanca);
		RepositorioUsuarios.getInstance().guardarUsuario(usuario);
	}

	@Test
	public void usuarioYGuardarropaPersisten() throws Exception {

		Usuario usuarioPersistido = RepositorioUsuarios.getInstance().buscarUsuarioPorId(usuario.getId());

		assertEquals(usuario, usuarioPersistido);
		
		assertTrue(usuarioPersistido.getGuardarropas().contains(guardarropa));
	}

	@Test
	public void eliminarPrendasAGuardarropa() throws Exception {
		
		Usuario usuarioPersistido = RepositorioUsuarios.getInstance().buscarUsuarioPorId(usuario.getId());

		guardarropa.eliminarPrenda(remeraRoja);
		
		usuarioPersistido = RepositorioUsuarios.getInstance().guardarUsuario(usuarioPersistido);
		
		assertTrue(usuarioPersistido.getGuardarropas().stream().filter(guardarropa -> guardarropa.getPrendas().contains(remeraRoja))
				.collect(Collectors.toList()).isEmpty());
	}

	@Test
	public void persistirEventoYDespuesEliminarlo() throws Exception {

		Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));
		EventoConFecha evento = new EventoConFecha(usuario, "gala_party", LocalDateTime.now().plusHours(2),
				guardarropas);

		RepositorioEventos.getInstance().guardarEvento(evento);
		Evento eventoPersistido = RepositorioEventos.getInstance().buscarEventoPorId(evento.getId());

		assertEquals(evento, eventoPersistido);

		RepositorioEventos.getInstance().eliminarEvento(eventoPersistido);

		Evento eventoNulo = RepositorioEventos.getInstance().buscarEventoPorId(evento.getId());

		assertEquals(null, eventoNulo);
	}
}
