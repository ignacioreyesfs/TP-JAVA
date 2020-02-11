package queMePongo.prenda;

import static org.junit.Assert.assertEquals;
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
import java.util.stream.Collectors;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Before;
import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaAccuweather;
import queMePongo.clima.ClimaOWM;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.TipoUsuario;
import queMePongo.exceptions.GuardarropaLlenoException;
import queMePongo.usuario.Usuario;

public class TestAtuendosClima {
	Usuario usuario;
	ClimaOWM mockClima;
	GeneradorAtuendos ga;
	Guardarropa guardarropa = new Guardarropa("Guardarropa");

	Prenda remeraAmarillaAlgodon;
	Prenda calzaTermicaNegroCuero;
	Prenda zapatillaCueroAzul;
	Prenda camperaNegraCuero;
	Prenda camperaRojaCuero;
	Prenda shortNegroPoliester;
	Prenda zapatosNegrosCuero;
	Prenda botasNegrasCuero;

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
		remeraAmarillaAlgodon = new PrendaBuilder().setNombre("Remera amarilla").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON)
				.setColorPrimario(new Color(255, 255, 255)).build();
		calzaTermicaNegroCuero = new PrendaBuilder().setNombre("Calza Termica").setTipoPrenda(TipoPrenda.CALZATERMICA).setMaterial(Material.SUPPLEX)
				.setColorPrimario(new Color(0, 0, 0)).build();
		zapatillaCueroAzul = new PrendaBuilder().setNombre("Zapatilla").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO)
				.setColorPrimario(new Color(34, 72, 128)).build();
		camperaNegraCuero = new PrendaBuilder().setNombre("Campera Negra").setTipoPrenda(TipoPrenda.CAMPERA).setMaterial(Material.CUERO)
				.setColorPrimario(new Color(255, 255, 255)).build();
		camperaRojaCuero = new PrendaBuilder().setNombre("Campera Roja").setTipoPrenda(TipoPrenda.CAMPERA).setMaterial(Material.CUERO)
				.setColorPrimario(new Color(255, 0, 0)).build();
		shortNegroPoliester = new PrendaBuilder().setNombre("Short negro").setTipoPrenda(TipoPrenda.SHORT).setMaterial(Material.POLIESTER)
				.setColorPrimario(new Color(255, 255, 255)).build();
		zapatosNegrosCuero = new PrendaBuilder().setNombre("Zapatos negros").setTipoPrenda(TipoPrenda.ZAPATOS).setMaterial(Material.CUERO)
				.setColorPrimario(new Color(255, 255, 255)).build();
		botasNegrasCuero = new PrendaBuilder().setNombre("Botas negras").setTipoPrenda(TipoPrenda.BOTAS).setMaterial(Material.CUERO)
				.setColorPrimario(new Color(255, 255, 255)).build();

		usuario.agregarGuardarropas(guardarropa);
		usuario.agregarPrenda(guardarropa, remeraAmarillaAlgodon);
		usuario.agregarPrenda(guardarropa, calzaTermicaNegroCuero);
		usuario.agregarPrenda(guardarropa, zapatillaCueroAzul);
		usuario.agregarPrenda(guardarropa, camperaNegraCuero);
		usuario.agregarPrenda(guardarropa, camperaRojaCuero);
		usuario.agregarPrenda(guardarropa, shortNegroPoliester);
		usuario.agregarPrenda(guardarropa, botasNegrasCuero);
		usuario.agregarPrenda(guardarropa, zapatosNegrosCuero);
	}

	@Test
	public void TestSugerirAtuendosSinAbrigoCuandoHaceCalor() {
		
		Set<Prenda> setAtuendoCorrectoA = new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, shortNegroPoliester, zapatillaCueroAzul));
		Set<Prenda> setAtuendoCorrectoB = new HashSet<>(Arrays.asList(remeraAmarillaAlgodon, shortNegroPoliester, zapatosNegrosCuero));
		Atuendo atuendoCorrectoA = new Atuendo(setAtuendoCorrectoA);
		Atuendo atuendoCorrectoB = new Atuendo(setAtuendoCorrectoB);
		Set<Atuendo> atuendosCorrectos = new HashSet<>();
		atuendosCorrectos.add(atuendoCorrectoA);
		atuendosCorrectos.add(atuendoCorrectoB);

		LinkedList<Atuendo> atuendosObtenidas = usuario.sugerirAtuendos(guardarropa);

		assertEquals(atuendosCorrectos.stream().map(atuendo -> atuendo.getPrendas()).collect(Collectors.toSet()),
				atuendosObtenidas.stream().map(atuendo -> atuendo.getPrendas()).collect(Collectors.toSet()));
	}
	
	@Test(expected = GuardarropaLlenoException.class)
	public void TestUsuarioGratuitoPoneDemasiadasPrendas() {
		Usuario usuarioTest = new Usuario(new Sensibilidad());
		usuarioTest.setTipo(TipoUsuario.GRATUITO);
		Guardarropa guardarropaTest = new Guardarropa("Guardarropa");
		usuarioTest.agregarGuardarropas(guardarropa);
		
		Prenda prendaMock1 = mock(Prenda.class);
		Prenda prendaMock2 = mock(Prenda.class);
		Prenda prendaMock3 = mock(Prenda.class);
		Prenda prendaMock4 = mock(Prenda.class);
		Prenda prendaMock5 = mock(Prenda.class);
		Prenda prendaMock6 = mock(Prenda.class);
		Prenda prendaMock7 = mock(Prenda.class);
		Prenda prendaMock8 = mock(Prenda.class);
		Prenda prendaMock9 = mock(Prenda.class);
		Prenda prendaMock10 = mock(Prenda.class);
		Prenda prendaMock11 = mock(Prenda.class);

		usuarioTest.agregarPrenda(guardarropaTest, prendaMock1);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock2);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock3);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock4);	
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock5);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock6);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock7);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock8);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock9);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock10);
		usuarioTest.agregarPrenda(guardarropaTest, prendaMock11);
	}

}