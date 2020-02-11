package queMePongo.prenda;

import java.awt.Color;
import org.junit.Test;
import queMePongo.builders.PrendaBuilder;
import queMePongo.clima.ClimaAccuweather;
import queMePongo.exceptions.PrendaRepetidaException;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.TipoUsuario;
import queMePongo.usuario.Usuario;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestPrendasRepetidas {
	ClimaAccuweather climaAccuweatherMock = mock(ClimaAccuweather.class);
	private Usuario user = new Usuario(new Sensibilidad());
	private Guardarropa guardarropa1 = new Guardarropa("Guardarropa");
	private Guardarropa guardarropa2 = new Guardarropa("Guardarropa");
	private Prenda remera;

	@Test(expected = PrendaRepetidaException.class)
	public void Test() {
		when(climaAccuweatherMock.getTemperatura()).thenReturn(20.0);
		user.agregarGuardarropas(guardarropa1);
		user.agregarGuardarropas(guardarropa2);
		user.setTipo(TipoUsuario.PREMIUM);
		remera = new PrendaBuilder()
				.setNombre("Remera")
				.setTipoPrenda(TipoPrenda.REMERA)
				.setMaterial(Material.ALGODON)
				.setColorPrimario(new Color(0,0,0))
				.build();

		user.agregarPrenda(guardarropa1, remera);
		user.agregarPrenda(guardarropa2, remera);
	}
}
