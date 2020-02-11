package queMePongo.prenda;

import java.awt.Color;
import org.junit.Test;
import queMePongo.builders.PrendaBuilder;
import queMePongo.exceptions.*;
import static org.junit.Assert.*;

public class TestCrearPrenda {

	@Test(expected = FaltanDatosException.class)
	public void testCrearPrendaSinColorPrimario() {
		new PrendaBuilder()
				.setNombre("Pantalon de cuero")
				.setTipoPrenda(TipoPrenda.PANTALON)
				.setMaterial(Material.CUERO)
				.build();
	}

	@Test
	public void testCrearPrendaOK(){
		Color blanco = new Color(255,255,255);
		Color negro = new Color(0,0,0);

		Prenda prenda = new PrendaBuilder()
				.setNombre("Remera negra y blanca")
				.setTipoPrenda(TipoPrenda.REMERA)
				.setMaterial(Material.ALGODON)
				.setColorPrimario(blanco)
				.setColorSecundario(negro)
				.build();

		assertEquals(TipoPrenda.REMERA, prenda.getTipoPrenda());
		assertEquals(Material.ALGODON, prenda.getMaterial());
		assertEquals(Categoria.SUPERIOR, prenda.getTipoPrenda().getCategoria());
		assertEquals(blanco, prenda.getColorPrimario());
		assertEquals(negro, prenda.getColorSecundario());
	}

	@Test(expected = ColorSecundarioIgualPrimarioException.class)
	public void testColorSecundarioIgualAPrimario() {
		Color blanco = new Color(255,255,255);

		new PrendaBuilder()
				.setNombre("Remera blanca")
				.setTipoPrenda(TipoPrenda.REMERA)
				.setMaterial(Material.ALGODON)
				.setColorPrimario(blanco)
				.setColorSecundario(blanco)
				.build();
	}
	
	@Test
	public void testColorSecundarioPuedeSerNull() {
		Prenda prenda = new PrendaBuilder()
			.setNombre("remerita")
			.setTipoPrenda(TipoPrenda.REMERA)
			.setMaterial(Material.ALGODON)
			.setColorPrimario(Color.BLACK)
			.setColorSecundario(null)
			.build();

		assertEquals(null, prenda.getColorSecundario());
	}


}
