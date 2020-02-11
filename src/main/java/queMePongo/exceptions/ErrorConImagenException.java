package queMePongo.exceptions;

public class ErrorConImagenException extends RuntimeException {
	public ErrorConImagenException(){
		super("Hubo un error al pasar el archivo imagen a bytes[]");
	}
}
