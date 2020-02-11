package web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import queMePongo.evento.EventoConFecha;
import queMePongo.evento.RepositorioEventos;
import queMePongo.prenda.Guardarropa;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;
import server.controllers.CalendarioController;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class CalendarioControllerTest implements WithGlobalEntityManager, TransactionalOps {
    @InjectMocks
    private CalendarioController calendarioController;
    @Mock
    private Sensibilidad sensibilidad;
    @Mock
    private Usuario usuario;
    @Mock
    private Guardarropa guardarropa;
    @Mock
    private Set<Guardarropa> guardarropas;
    @Mock
    private RepositorioEventos repositorioEventos;
    @Mock
    private EventoConFecha unEvento;
    @Mock
    private EventoConFecha otroEvento;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        //calendarioController = new CalendarioController();
        //sensibilidad = new Sensibilidad();
        usuario = new Usuario(sensibilidad);
        //guardarropa = new Guardarropa();
        //Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));
        repositorioEventos = new RepositorioEventos();
        unEvento = new EventoConFecha(usuario, "unEvento", LocalDateTime.now().plusDays(20), null);
        otroEvento = new EventoConFecha(usuario, "otroEvento", LocalDateTime.now().plusDays(30),null);
        repositorioEventos.cargarEvento(unEvento);
        repositorioEventos.cargarEvento(otroEvento);
    }

    @Test
    public void persistoEventos() {
        HashMap<String, Object> viewModel = new HashMap<>();
        withTransaction(() -> {
            viewModel.put("unEvento", unEvento);
            viewModel.put("otroEvento", otroEvento);
        });

        assertEquals(repositorioEventos.buscarEventoPorNombre("unEvento"), viewModel.get("unEvento"));
        assertEquals(repositorioEventos.buscarEventoPorNombre("otroEvento"), viewModel.get("otroEvento"));
    }

    @After
    public void eliminoEventos() {
        withTransaction(() -> {
            repositorioEventos.eliminarEventosConNombre("unEvento");
            repositorioEventos.eliminarEventosConNombre("otroEvento");
        });
    }













}
