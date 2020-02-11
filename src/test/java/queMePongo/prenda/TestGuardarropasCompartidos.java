package queMePongo.prenda;

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaOWM;
import queMePongo.exceptions.UnaOMasPrendasNoEstanDisponiblesException;
import queMePongo.persistencia.ColorConverter;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;

public class TestGuardarropasCompartidos {
	Usuario unUsuario;
	Usuario otroUsuario;
	Guardarropa guardarropaCompartido;
	Atuendo unAtuendo;
	Atuendo atuendoDisponible;
	ClimaOWM mockClima;
	GeneradorAtuendos ga;

	@Before
	public void init() {
		
		mockClima = Mockito.mock(ClimaOWM.class);
    	Mockito.when(mockClima.getTemperaturaFecha(any(LocalDateTime.class)))
    		.thenReturn(24.0);
    	ga = Mockito.spy(new GeneradorAtuendos());
    	Mockito.when(ga.crearProveedorClima()).thenReturn(mockClima);
    	unUsuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(unUsuario.crearGeneradorAtuendos()).thenReturn(ga);
    	otroUsuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(otroUsuario.crearGeneradorAtuendos()).thenReturn(ga);
    	
		guardarropaCompartido = new Guardarropa("Guardarropa");
		unUsuario.agregarGuardarropas(guardarropaCompartido);
				
        Prenda remeraRoja = new PrendaBuilder().setNombre("Remera roja").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON)
                .setColorPrimario(new Color(255, 0, 0)).build();
        Prenda pantalonNegro = new PrendaBuilder().setNombre("Pantalon negro").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(0, 0, 0)).build();
        Prenda zapatillasNegras = new PrendaBuilder().setNombre("Zapatilla negra").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(0, 0, 0)).build();
             
        Prenda remeraAmarilla = new PrendaBuilder().setNombre("Remera amarilla").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON)
                .setColorPrimario(new Color(255, 255, 0)).build();
        Prenda pantalonGris = new PrendaBuilder().setNombre("Pantalon gris").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.ALGODON)
                .setColorPrimario(new Color(155, 155, 155)).build();
        Prenda zapatillasAmarillas = new PrendaBuilder().setNombre("Zapatilla amarilla").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(255, 255, 0)).build();
        
        guardarropaCompartido.agregarPrenda(remeraRoja);
        guardarropaCompartido.agregarPrenda(pantalonNegro);
        guardarropaCompartido.agregarPrenda(zapatillasNegras);
        guardarropaCompartido.agregarPrenda(remeraAmarilla);
        guardarropaCompartido.agregarPrenda(pantalonGris);
        guardarropaCompartido.agregarPrenda(zapatillasAmarillas);
        
        unAtuendo = new Atuendo(new HashSet<>(Arrays.asList(remeraRoja, pantalonNegro, zapatillasNegras)));
        atuendoDisponible = new Atuendo(new HashSet<>(Arrays.asList(remeraAmarilla, pantalonGris, zapatillasAmarillas)));
	}

	@Test
	public void TestSugerirUnicamenteLosAtuendosConTodasSusPrendasDisponibles() {
		LinkedList<Atuendo> sugerenciaConTodosLasPrendasDisponibles = unUsuario.sugerirAtuendos(guardarropaCompartido);

		assertTrue(sugerenciaConTodosLasPrendasDisponibles.size() == 8);

        Sugerencia unaSugerencia = new Sugerencia(unAtuendo);
		unUsuario.aceptarSugerencia(unaSugerencia);
		LinkedList<Atuendo> sugerenciaCon3PrendasNoDisponibles = otroUsuario.sugerirAtuendos(guardarropaCompartido);
		assertTrue(sugerenciaCon3PrendasNoDisponibles.size() == 1);
		assertTrue(sugerenciaCon3PrendasNoDisponibles.iterator().next().getPrendas().containsAll(atuendoDisponible.getPrendas()));
	}

	@Test
	public void TestCompartirGuardarropasAgregaGuardarropaAUsuario() {
		assertFalse(otroUsuario.getGuardarropas().contains(guardarropaCompartido));
		assertTrue(unUsuario.getGuardarropas().contains(guardarropaCompartido));
		
		guardarropaCompartido.compartirCon(otroUsuario);

		assertTrue(otroUsuario.getGuardarropas().contains(guardarropaCompartido));
	}

	@Test (expected = UnaOMasPrendasNoEstanDisponiblesException.class)
	public void TestAccionAceptarUnAtuendoNoDisponibleLanzaExcepcion() {
		Sugerencia sugerenciaDisponible = new Sugerencia(atuendoDisponible);
		
		unUsuario.aceptarSugerencia(sugerenciaDisponible);        
		otroUsuario.aceptarSugerencia(sugerenciaDisponible);
	}

	@Test
	public void TestAceptarUnaSugerenciaCambiaElEstadoDeLasPrendasANoDisponible() {
		assertTrue(atuendoDisponible.getPrendas().stream().allMatch(prenda -> prenda.estaDisponible()));

		Sugerencia sugerenciaDisponible = new Sugerencia(atuendoDisponible);
		unUsuario.aceptarSugerencia(sugerenciaDisponible);
				
		assertTrue(atuendoDisponible.getPrendas().stream().noneMatch(prenda -> prenda.estaDisponible()));
	}

}