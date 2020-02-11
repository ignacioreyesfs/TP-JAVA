package queMePongo.prenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaOWM;
import queMePongo.exceptions.AccionIncorrectaException;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;

public class SugerenciasTest {
    Usuario usuario;
    Sugerencia sugerencia;
    Prenda remera;
    Prenda pantalon;
    Prenda zapatillas;
    Atuendo atuendo;
    ClimaOWM mockClima;
    GeneradorAtuendos ga;

    @Before
    public void setup() throws Exception {
    	
    	mockClima = Mockito.mock(ClimaOWM.class);
    	Mockito.when(mockClima.getTemperaturaFecha(any(LocalDateTime.class)))
    		.thenReturn(24.0);
    	ga = Mockito.spy(new GeneradorAtuendos());
    	Mockito.when(ga.crearProveedorClima()).thenReturn(mockClima);
    	usuario = Mockito.spy(new Usuario(new Sensibilidad()));
    	Mockito.when(usuario.crearGeneradorAtuendos()).thenReturn(ga);
    	
        remera = new PrendaBuilder().setNombre("Remera").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON)
                .setColorPrimario(new Color(255, 255, 255)).build();
        pantalon = new PrendaBuilder().setNombre("Pantalon").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(255, 255, 255)).build();
        zapatillas = new PrendaBuilder().setNombre("Zapatillas").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO)
                .setColorPrimario(new Color(34, 72, 128)).build();
        
        atuendo = new Atuendo(new HashSet<>(Arrays.asList(remera, pantalon, zapatillas)));

        sugerencia = new Sugerencia(atuendo);
    }
	
	  @Test(expected = AccionIncorrectaException.class) public void
	  cuandoSeEjecutaUnaAccionIncorrectaDeberiaLanzarUnaException() {
	  sugerencia.deshacerAccion(usuario); }
	  
	  @Test public void cuandoSeEjecutaUnaAccionDeberiaTenerEseEstado() {
	  sugerencia.ejecutarAccion(Accion.ACEPTAR, usuario);
	  usuario.addSugerenciaAHistory(sugerencia);
	  assertTrue(usuario.getEleccionesHistory().contains(sugerencia));
	  assertEquals(Estado.ACEPTADO, sugerencia.getEstado()); }
	  
	  @Test public void cuandoSeDeshaceUnaAccionDeberiaVolverAlEstadoPendiente() {
	  sugerencia.ejecutarAccion(Accion.RECHAZAR, usuario);
	  sugerencia.deshacerAccion(usuario);
	  usuario.addSugerenciaAHistory(sugerencia);
	  assertTrue(usuario.getEleccionesHistory().contains(sugerencia));
	  assertEquals(Estado.PENDIENTE, sugerencia.getEstado()); }
	  
	  @Test public void aumentaElCoeficienteEleccionCuandoAceptoSugerencia() {
	  assertEquals(0, remera.cantidadSelecciones(usuario)); assertEquals(0,
	  sugerencia.getAtuendo().coeficientePreferencia(usuario));
	  sugerencia.ejecutarAccion(Accion.ACEPTAR, usuario); assertEquals(1,
	  remera.cantidadSelecciones(usuario)); assertEquals(3,
	  sugerencia.getAtuendo().coeficientePreferencia(usuario)); }
	  
	  @Test public void siAceptoUnAtuendoCambiaSuCoeficiente() { Guardarropa
	  guardarropa = new Guardarropa("Guardarropa");
	  
	  guardarropa.agregarPrenda(remera); 
	  guardarropa.agregarPrenda(pantalon);
	  guardarropa.agregarPrenda(zapatillas);
	  
	  Sugerencia sugerencia = new Sugerencia(atuendo);
	  
	  sugerencia.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia.liberarPrendas(); 
	  sugerencia.ejecutarAccion(Accion.ACEPTAR,usuario); 
	  sugerencia.liberarPrendas();
	  
	  assertEquals(2, remera.cantidadSelecciones(usuario)); }
	  
	  @Test public void devuelveAtuendosPorOrdenDeCoeficienteEleccion() { Prenda
	  remera2 = new PrendaBuilder().setNombre("Remera2").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON)
	  	.setColorPrimario(new Color(255, 255, 0)).build(); 
	  Prenda pantalon2 = new PrendaBuilder().setNombre("Pantalon2").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.CUERO)
			  .setColorPrimario(new Color(255, 255, 0)).build(); 
	  Prenda zapatillas2 = new PrendaBuilder().setNombre("Zapatilla2").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO) 
			  .setColorPrimario(new Color(34, 72, 0)).build();
	  Prenda remera3 = new PrendaBuilder().setNombre("Remera3").setTipoPrenda(TipoPrenda.REMERA).setMaterial(Material.ALGODON) 
			  .setColorPrimario(new Color(255, 0, 0)).build(); 
	  Prenda pantalon3 = new PrendaBuilder().setNombre("Pantalon3").setTipoPrenda(TipoPrenda.PANTALON).setMaterial(Material.CUERO)
			  .setColorPrimario(new Color(255, 0, 0)).build(); 
	  Prenda zapatillas3 = new PrendaBuilder().setNombre("Zapatilla3").setTipoPrenda(TipoPrenda.ZAPATILLAS).setMaterial(Material.CUERO)
			  .setColorPrimario(new Color(34, 0, 0)).build();
	  
	  Guardarropa guardarropa = new Guardarropa("Guardarropa");
	  
	  guardarropa.agregarPrenda(remera); guardarropa.agregarPrenda(pantalon);
	  guardarropa.agregarPrenda(zapatillas); guardarropa.agregarPrenda(remera2);
	  guardarropa.agregarPrenda(pantalon2); guardarropa.agregarPrenda(zapatillas2);
	  guardarropa.agregarPrenda(remera3); guardarropa.agregarPrenda(pantalon3);
	  guardarropa.agregarPrenda(zapatillas3);
	  
	  Set<Prenda> prendas2 = new HashSet<>(Arrays.asList(remera2, pantalon2,
	  zapatillas2));
	  
	  Atuendo atuendo2 = new Atuendo(prendas2);
	  
	  Sugerencia sugerencia = new Sugerencia(atuendo);
	  Sugerencia sugerencia2 = new Sugerencia(atuendo2);
	  
	  sugerencia.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia.liberarPrendas(); 
	  sugerencia.ejecutarAccion(Accion.ACEPTAR,usuario);
	  sugerencia.liberarPrendas();
	  sugerencia2.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia2.liberarPrendas();
	  
	  LinkedList<Atuendo> atuendos = usuario.sugerirAtuendos(guardarropa);
	  
	  //El primer elemento es el de mayor coeficiente, atuendo
	  assertEquals(atuendo.getPrendas(), atuendos.get(0).getPrendas());
	  
	  //Si cambio los coeficientes el mas recomendado es atuendo2
	  sugerencia2.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia2.liberarPrendas(); 
	  sugerencia2.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia2.liberarPrendas();
	  sugerencia2.ejecutarAccion(Accion.ACEPTAR, usuario);
	  sugerencia2.liberarPrendas();
	  
	  atuendos = usuario.sugerirAtuendos(guardarropa);
	  
	  assertEquals(atuendo2.getPrendas(), atuendos.get(0).getPrendas());
	  
	  }
	 
}
