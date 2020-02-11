package queMePongo.prenda;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.Color;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaAccuweather;
import queMePongo.clima.ClimaOWM;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.TipoUsuario;
import queMePongo.usuario.Usuario;

public class TestGenerarAtuendos {
	ClimaOWM mockClima;
	Usuario usuario;
	GeneradorAtuendos ga;
	Prenda remeraAmarillaAlgodon;
	Prenda remeraRojaPoliester;
	Prenda pantalonNegroAlgodon;
	Prenda pantalonNegroCuero;
	Prenda zapatillaCueroAzul;
	Prenda lentesPlasticoNegro;

	Set<Atuendo> sugerenciasGuardarropa1 = new HashSet<>();

	@Before
	public void init() {
		
		mockClima = Mockito.mock(ClimaOWM.class);
    	Mockito.when(mockClima.getTemperaturaFecha(any(LocalDateTime.class)))
    		.thenReturn(24.0);
    	ga = Mockito.spy(new GeneradorAtuendos());
    	Mockito.when(ga.crearProveedorClima()).thenReturn(mockClima);
    	usuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(usuario.crearGeneradorAtuendos()).thenReturn(ga);
    	
		usuario.setTipo(TipoUsuario.PREMIUM);
		remeraAmarillaAlgodon = new PrendaBuilder().setNombre("Remera amarilla").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON).setColorPrimario(new Color(255, 255, 255)).build();
		remeraRojaPoliester = new PrendaBuilder().setNombre("Remera roja").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.POLIESTER).setColorPrimario(new Color(255, 0, 0)).build();
		pantalonNegroAlgodon = new PrendaBuilder().setNombre("Pantalon negro").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.ALGODON).setColorPrimario(new Color(0, 0, 0)).build();
		pantalonNegroCuero = new PrendaBuilder().setNombre("Pantalon de cuero").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.CUERO).setColorPrimario(new Color(0, 0, 0)).build();
		zapatillaCueroAzul = new PrendaBuilder().setNombre("Zapatilla cuero").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO).setColorPrimario(new Color(34, 72, 128)).build();
		lentesPlasticoNegro = new PrendaBuilder().setNombre("Lentes de plastico").setTipoPrenda(TipoPrenda.LENTES).setMaterial(Material.PLASTICO).setColorPrimario(new Color(0, 0, 0)).build();

		Atuendo atuendo1Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, pantalonNegroAlgodon, zapatillaCueroAzul, lentesPlasticoNegro)));
		Atuendo atuendo2Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, pantalonNegroCuero, zapatillaCueroAzul, lentesPlasticoNegro)));
		Atuendo atuendo3Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraRojaPoliester, pantalonNegroAlgodon, zapatillaCueroAzul, lentesPlasticoNegro)));
		Atuendo atuendo4Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraRojaPoliester, pantalonNegroCuero, zapatillaCueroAzul, lentesPlasticoNegro)));
		Atuendo atuendo5Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, pantalonNegroAlgodon, zapatillaCueroAzul)));
		Atuendo atuendo6Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, pantalonNegroCuero, zapatillaCueroAzul)));
		Atuendo atuendo7Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraRojaPoliester, pantalonNegroAlgodon, zapatillaCueroAzul)));
		Atuendo atuendo8Guardarropa1 = new Atuendo(new HashSet<>(Arrays.asList(remeraRojaPoliester, pantalonNegroCuero, zapatillaCueroAzul)));

		sugerenciasGuardarropa1.add(atuendo1Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo2Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo3Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo4Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo5Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo6Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo7Guardarropa1);
		sugerenciasGuardarropa1.add(atuendo8Guardarropa1);
	}

	@Test
	public void TestGenerarAtuendosMuestraTodasLasCombinaciones() {
		Guardarropa guardarropa = new Guardarropa("Guardarropa");
		usuario.agregarGuardarropas(guardarropa);
		usuario.agregarPrenda(guardarropa, remeraAmarillaAlgodon);
		usuario.agregarPrenda(guardarropa, remeraRojaPoliester);
		usuario.agregarPrenda(guardarropa, pantalonNegroCuero);
		usuario.agregarPrenda(guardarropa, pantalonNegroAlgodon);
		usuario.agregarPrenda(guardarropa, zapatillaCueroAzul);
		usuario.agregarPrenda(guardarropa, lentesPlasticoNegro);

		LinkedList<Atuendo> atuendos = usuario.sugerirAtuendos(guardarropa);

		assertEquals(8, atuendos.size());
		assertEquals(atuendos.stream().map(atuendo -> atuendo.getPrendas()).collect(Collectors.toSet())
				,sugerenciasGuardarropa1.stream().map(atuendo -> atuendo.getPrendas()).collect(Collectors.toSet()));
	}

}