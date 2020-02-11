package queMePongo.exceptions;

public class ErrorConFechaException extends RuntimeException {
	public ErrorConFechaException() {
		super("La fecha y horario del evento debe ser posterior a la fecha y horario actual");
	}
}
