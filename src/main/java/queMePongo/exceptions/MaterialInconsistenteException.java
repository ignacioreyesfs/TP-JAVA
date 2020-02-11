package queMePongo.exceptions;

public class MaterialInconsistenteException extends RuntimeException {
	public MaterialInconsistenteException() {
		super("El material es inconsistente con el tipo de prenda.");
	}
}