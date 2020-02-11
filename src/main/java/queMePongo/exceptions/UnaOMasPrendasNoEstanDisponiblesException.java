package queMePongo.exceptions;

import java.util.stream.Collectors;

import queMePongo.prenda.Atuendo;

public class UnaOMasPrendasNoEstanDisponiblesException extends RuntimeException{
	public UnaOMasPrendasNoEstanDisponiblesException(Atuendo atuendo) {
		super("Prenda no disponible : " + String.join(", ", atuendo.getPrendas().stream().filter(prenda -> !prenda.estaDisponible())
				.map(prenda -> prenda.toString()).collect(Collectors.toList())));
	}
}

