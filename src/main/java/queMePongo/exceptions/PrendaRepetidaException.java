package queMePongo.exceptions;

public class PrendaRepetidaException extends RuntimeException{
	public PrendaRepetidaException(){
		super("Ya existe la prenda en otro guardarropas.");
	}
}
