package queMePongo.exceptions;

public class ErrorSolicitarTemperaturaException extends RuntimeException {
	public ErrorSolicitarTemperaturaException(int statusCode) {
		super("Se ha producido un error con la API de AccuWeather. Error code: " + statusCode);
	}

	public ErrorSolicitarTemperaturaException(Throwable e) {
		super("No se pudo obtener la temperatura", e);
	}
}
