package queMePongo.exceptions;

public class SetVacioException extends RuntimeException{
	public SetVacioException() {
		super("El conjunto ingresado no posee elementos.");
	}
}
