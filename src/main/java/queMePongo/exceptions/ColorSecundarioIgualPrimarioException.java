package queMePongo.exceptions;

public class ColorSecundarioIgualPrimarioException extends RuntimeException{
    public ColorSecundarioIgualPrimarioException() {
        super("El color secundario de una prenda no puede ser igual a su color primario.");
    }
}
