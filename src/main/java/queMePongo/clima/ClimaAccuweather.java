package queMePongo.clima;

import org.apache.http.impl.client.HttpClients;
import java.time.LocalDateTime;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import queMePongo.exceptions.*;
import javax.persistence.Entity;

@Entity
public class ClimaAccuweather extends ProveedorClima {
	private static String API_KEY = "ysSCQlAKEHwplTewbjS5i6hOFuo8h0Xh";
	private String urlForecast = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/7894?apikey=" + API_KEY;
	private String urlTemperaturaAhora = "http://dataservice.accuweather.com/currentconditions/v1/7894?apikey=" + API_KEY;

	@Override
	public double getTemperatura() {
		return this.fetchTemperatura(0, urlTemperaturaAhora);
	}

	@Override
	public double getTemperaturaFecha(LocalDateTime fecha) {
		return this.fetchTemperatura(3, urlForecast);
	}
	
	private double fetchTemperatura(int indice, String url) {

		double temperatura = 0.0;
		
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);
		HttpResponse response;

		try {
			response = httpClient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				throw new ErrorSolicitarTemperaturaException(statusCode);
			}

			String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONArray jsonArray = new JSONArray(jsonString);
			if(url == urlTemperaturaAhora) {
				temperatura = Double.parseDouble(jsonArray.getJSONObject(indice).getJSONObject("Temperature").getJSONObject("Metric").get("Value").toString());
			}
			else {
				temperatura = this.FahrenheitACelsius(Double.parseDouble(jsonArray.getJSONObject(indice).getJSONObject("Temperature").get("Value").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSolicitarTemperaturaException(e);
		}

		return temperatura;
	}
	
	private double FahrenheitACelsius(double temperatura) {
		return Math.ceil((temperatura - 32) * (5d/9d));
	}
}
