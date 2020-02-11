package queMePongo.exceptions;

public class SinNotificacionAUsuarioException extends RuntimeException {
    public SinNotificacionAUsuarioException(){
        super("No se pudo notificar al usuario por ningún medio");
    }
}
