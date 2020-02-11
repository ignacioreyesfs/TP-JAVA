package queMePongo.prenda;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaAccuweather;
import queMePongo.clima.ClimaOWM;
import queMePongo.clima.ProveedorClima;
import queMePongo.evento.Evento;
import queMePongo.evento.EventoConFecha;
import queMePongo.evento.EventoRepetitivo;
import queMePongo.evento.Frecuencia;
import queMePongo.evento.RepositorioEventos;
import queMePongo.exceptions.ErrorConFechaException;
import queMePongo.exceptions.SinNotificacionAUsuarioException;
import queMePongo.notificaciones.MailConcreteObserver;
import queMePongo.notificaciones.NotificadorObserver;
import queMePongo.notificaciones.WhatsAppConcreteObserver;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EventosTest {
	Usuario usuario;
	ClimaOWM mockClima;
	GeneradorAtuendos ga;
	EventoConFecha evento;
	
	@Before
    public void setup() throws Exception {
    	
    	mockClima = Mockito.mock(ClimaOWM.class);
    	Mockito.when(mockClima.getTemperaturaFecha(any(LocalDateTime.class)))
    		.thenReturn(24.0);
    	ga = Mockito.spy(new GeneradorAtuendos());
    	Mockito.when(ga.crearProveedorClima()).thenReturn(mockClima);
    	usuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(usuario.crearGeneradorAtuendos()).thenReturn(ga);
	}
	
	
	/*@Test
	public void crearUnJobCorrectamente() throws SchedulerException {  	
        Prenda camisa = new PrendaBuilder().setNombre("camisa").setTipoPrenda(TipoPrenda.CAMISA).setMaterial(Material.SEDA)
                .setColorPrimario(new Color(255, 255, 255)).build();
        Prenda pantalon = new PrendaBuilder().setNombre("Pantalon").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.ALGODON)
                .setColorPrimario(new Color(0, 0, 0)).build();
        Prenda zapatos = new PrendaBuilder().setNombre("Zapatos").setTipoPrenda(TipoPrenda.ZAPATOS).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(0, 0, 0)).build();
        Guardarropa guardarropa = new Guardarropa("Guardarropa");
        guardarropa.agregarPrenda(camisa);
        guardarropa.agregarPrenda(pantalon);
        guardarropa.agregarPrenda(zapatos);

        Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));
        EventoConFecha evento = Mockito.spy(new EventoConFecha(usuario, "gala_party", LocalDateTime.now().plusHours(2), guardarropas));
        Mockito.when(evento.crearGeneradorAtuendos()).thenReturn(ga);
        JobEventoConFecha jobEvento = new JobEventoConFecha(evento);
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        RepositorioEventos organizadorEventos = Mockito.spy(RepositorioEventos.getInstance());
        Mockito.doNothing().when(organizadorEventos).cargarEvento(evento);
        
        Calendario calendario = new Calendario(scheduler, organizadorEventos);

        calendario.iniciarCalendario();
        calendario.cargarTarea(jobEvento);

        assertTrue(scheduler.checkExists(new JobKey("j_gala_party")));
        assertTrue(scheduler.checkExists(new TriggerKey("t_gala_party")));
        
        evento.buscarSugerencias();
        Set<Atuendo> atuendos = evento.getSugerencias().stream().map(sugerencia -> sugerencia.getAtuendo())
        							   .collect(Collectors.toSet());
        
        assertEquals(1, atuendos.size());
        assertTrue(atuendos.iterator().next().getPrendas().containsAll(Arrays.asList(camisa, pantalon, zapatos)));

        scheduler.shutdown(true);
    }*/

	@Test
	public void eventoRepetitivoGeneraEventosConFecha() throws SchedulerException {
		
		Frecuencia frecuenciaDiaria = Frecuencia.DIARIA;
		Guardarropa guardarropa = new Guardarropa("Guardarropa");
		Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));

		EventoRepetitivo evento = new EventoRepetitivo(usuario, "eventoPrueba", guardarropas, frecuenciaDiaria, LocalDateTime.now());

        RepositorioEventos organizadorEventos = RepositorioEventos.getInstance();

        assertTrue(organizadorEventos.getEventosPendientes().stream().anyMatch(eventoConFecha -> eventoConFecha.getNombre() == "eventoPrueba"));
        assertTrue(organizadorEventos.getEventosPendientes().stream().anyMatch(eventoConFecha -> eventoConFecha.getClass() == EventoConFecha.class));
	}

    @Test
    public void notificarUsuarioSugerenciasListas() {
        Evento mockEvento = mock(Evento.class);
        NotificadorObserver mockObserverMail = mock(MailConcreteObserver.class);
        NotificadorObserver mockObserverWhatsApp = mock(WhatsAppConcreteObserver.class);
        usuario.addObserver(mockObserverMail);
        usuario.addObserver(mockObserverWhatsApp);
        usuario.notificar(mockEvento);
        verify(mockObserverMail).notificarSugerenciasListas(usuario, mockEvento);
        verify(mockObserverMail).notificarSugerenciasListas(usuario, mockEvento);
    }
    @Test
    public void agregoUnObserverMail() {
        ClimaAccuweather climaAccuweatherMock = mock(ClimaAccuweather.class);
        when(climaAccuweatherMock.getTemperatura()).thenReturn(25.0);
        Evento mockEvento = mock(Evento.class);
        NotificadorObserver mockObserverMail = mock(MailConcreteObserver.class);
        NotificadorObserver mockObserverWhatsApp = mock(WhatsAppConcreteObserver.class);
        usuario.addObserver(mockObserverMail);
        usuario.notificar(mockEvento);
        verify(mockObserverMail).notificarSugerenciasListas(usuario, mockEvento);
        verifyZeroInteractions(mockObserverWhatsApp);
    }
    @Test(expected = SinNotificacionAUsuarioException.class)
    public void cuandoNoSePudoNotificarAlUsuarioPorNingunMedioDeberiaLanzarUnaException() {
        Evento evento = mock(Evento.class);
        NotificadorObserver mockObserverMail = mock(MailConcreteObserver.class);
        NotificadorObserver mockObserverWhatsApp = mock(WhatsAppConcreteObserver.class);
        usuario.addObserver(mockObserverMail);
        usuario.addObserver(mockObserverWhatsApp);
        doThrow(RuntimeException.class)
                .when(mockObserverMail)
                .notificarSugerenciasListas(usuario, evento);
        doThrow(RuntimeException.class)
                .when(mockObserverWhatsApp)
                .notificarSugerenciasListas(usuario, evento);
        usuario.notificar(evento);
    }
    
    @Test(expected = ErrorConFechaException.class)
    public void ingresarFechaAnteriorALaActualLanzaException() {
    	Set<Guardarropa> guardarropas = new HashSet<>();
    	EventoConFecha evento = new EventoConFecha(usuario, "gala_party", LocalDateTime.now().minusHours(2), guardarropas);
    }
    
    @Test
    public void elPedidoDeEventosEnUnRangoDe15DiasDebeGenerar2Eventos(){
		Frecuencia frecuenciaSemanal = Frecuencia.SEMANAL;
    	Guardarropa guardarropa = new Guardarropa("Guardarropa");
		Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));

    	EventoRepetitivo evento = new EventoRepetitivo(usuario, "Entrega de diplomas", guardarropas, frecuenciaSemanal, LocalDateTime.now());
    	
    	List<Evento> proximosEventos = evento.eventosEntreFechas(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusWeeks(2));
    	assertEquals(2,proximosEventos.size());
    	assertEquals(LocalDateTime.now().plusWeeks(1).toLocalDate(),proximosEventos.get(0).getFecha().toLocalDate());
    	assertEquals(LocalDateTime.now().plusWeeks(2).toLocalDate(),proximosEventos.get(1).getFecha().toLocalDate());

    }
}