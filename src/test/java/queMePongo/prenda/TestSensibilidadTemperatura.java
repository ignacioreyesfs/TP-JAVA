package queMePongo.prenda;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaOWM;
import queMePongo.evento.EventoConFecha;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;

public class TestSensibilidadTemperatura {
	
	ClimaOWM mockClima;
    GeneradorAtuendos ga;
    Usuario usuario;
    EventoConFecha evento;
    
    @Before
    public void setup() throws Exception {
    	
    	mockClima = Mockito.mock(ClimaOWM.class);
    	Mockito.when(mockClima.getTemperaturaFecha(any(LocalDateTime.class)))
    		.thenReturn(20.0);
    	ga = Mockito.spy(new GeneradorAtuendos());
    	Mockito.when(ga.crearProveedorClima()).thenReturn(mockClima);
    	usuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(usuario.crearGeneradorAtuendos()).thenReturn(ga);
    }
    
	@Test
	public void testFrioConShortNoLoVuelveASugerir() {
		
        Prenda shortNailon = new PrendaBuilder().setNombre("Short").setTipoPrenda(TipoPrenda.SHORT).setMaterial(Material.POLIESTER)
                .setColorPrimario(new Color(255, 255, 255)).build();
        Prenda camisa = new PrendaBuilder().setNombre("Camisa").setTipoPrenda(TipoPrenda.CAMISA).setMaterial(Material.SEDA)
                .setColorPrimario(new Color(255, 255, 255)).build();
        Prenda zapatos = new PrendaBuilder().setNombre("Zapato").setTipoPrenda(TipoPrenda.ZAPATOS).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(0, 0, 0)).build();

        Guardarropa guardarropa = new Guardarropa("Guardarropa");
        guardarropa.agregarPrenda(shortNailon);
        guardarropa.agregarPrenda(camisa);
        guardarropa.agregarPrenda(zapatos);

        Set<Guardarropa> guardarropas = new HashSet<>(Arrays.asList(guardarropa));

        evento = Mockito.spy(new EventoConFecha(usuario, "gala_party", LocalDateTime.now().plusDays(1), guardarropas));
        Mockito.when(evento.crearGeneradorAtuendos()).thenReturn(ga);
        
        evento.buscarSugerencias();
		List<Sugerencia> sugerencias1 = evento.getSugerencias();
        
        assertEquals(1, sugerencias1.size());
        
        usuario.ajustarSensibilidad(Categoria.INFERIOR, NivelDeAbrigo.BAJO);

        evento.buscarSugerencias();
		List<Sugerencia> sugerencias2 = evento.getSugerencias();
        
        assertEquals(0, sugerencias2.size());
	}
}

