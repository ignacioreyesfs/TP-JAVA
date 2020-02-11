package queMePongo.exceptions;

public class GuardarropaLlenoException extends RuntimeException {
    public GuardarropaLlenoException() {
        super("La capacidad maxima de este guardarropa ha sido alcanzada.");
    }
}