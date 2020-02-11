package queMePongo.clima;

import net.aksingh.owmjapis.core.OWM;
import queMePongo.exceptions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import net.aksingh.owmjapis.api.APIException;

public class OpenWeatherMap {
	
	private OWM owm = new OWM("3d8400ed6a740b8d6684b988a5d0b4d9");
	
	public double getTemperaturaBsAs() {
		try {
			return owm.currentWeatherByCityId(3435910).getMainData().getTemp();
		} catch (APIException e) {
			e.printStackTrace();
			throw new ErrorSolicitarTemperaturaException(e);
		}
	}
	public Double getTemperaturaFecha(LocalDateTime fecha) {
		try {
			return owm.hourlyWeatherForecastByCityId(3435910).getDataList().get(this.calcularIndice(fecha)).component2().component1();
		} catch (APIException e) {
			e.printStackTrace();
			throw new ErrorSolicitarTemperaturaException(e);
		}
	}
	private int calcularIndice(LocalDateTime fecha) {
		Instant instanteFecha = fecha.toInstant(ZoneOffset.UTC);
		Instant instanteAhora = LocalDateTime.now().toInstant(ZoneOffset.UTC);
		Date fechaDate = Date.from(instanteFecha);
		Date ahoraDate = Date.from(instanteAhora);
		return (this.diasEntre(ahoraDate, fechaDate) * 8) - 1;
	}
	
	public int diasEntre(Date d1, Date d2){
		int resultado = (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		return resultado + 1;
	}
}
