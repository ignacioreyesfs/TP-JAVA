package queMePongo.clima;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class ClimaOWM extends ProveedorClima {

	@Override
	public double getTemperatura() {
		return kelvinToCelsius(new OpenWeatherMap().getTemperaturaBsAs());
	}

	@Override
	public double getTemperaturaFecha(LocalDateTime fecha) {
		return kelvinToCelsius(new OpenWeatherMap().getTemperaturaFecha(fecha));
	}

	private double kelvinToCelsius(double tempKelvin) {
		return tempKelvin - 273;
	}

}
