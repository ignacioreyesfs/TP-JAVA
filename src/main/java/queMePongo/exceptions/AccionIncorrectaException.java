package queMePongo.exceptions;

public class AccionIncorrectaException extends RuntimeException {
    public AccionIncorrectaException() {
        super("El usuario a seleccionado una acción incorrecta");
    }
}
