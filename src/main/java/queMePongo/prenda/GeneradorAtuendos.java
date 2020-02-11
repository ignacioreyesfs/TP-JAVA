package queMePongo.prenda;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;

import queMePongo.clima.ClimaOWM;
import queMePongo.clima.ProveedorClima;
import queMePongo.usuario.Usuario;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;

public class GeneradorAtuendos {
		
	public LinkedList<Atuendo> sugerirAtuendosFecha(Usuario usuario, Guardarropa guardarropa, LocalDateTime fecha) {
		ProveedorClima proveedorClima = this.crearProveedorClima();
		LinkedList<Atuendo> atuendos = sugerirAtuendosValidos(guardarropa)
									.stream()
									.filter(atuendo -> atuendo.adecuadoTemperatura(usuario, proveedorClima.getTemperaturaFecha(fecha)))
									.collect(Collectors.toCollection(LinkedList::new));
		
		atuendos.sort(Comparator.comparingInt((Atuendo a) -> a.coeficientePreferencia(usuario)).reversed());
		return atuendos;
	}

	public Set<Atuendo> sugerirAtuendosValidos(Guardarropa guardarropa) {
		Predicate<Atuendo> noRepiteTiposDePrenda = atuendo -> {
			long cantidadTiposDePrendaDistintos = atuendo.getPrendas().stream().map(prenda -> prenda.getTipoPrenda()).distinct().count();
			return cantidadTiposDePrendaDistintos == atuendo.getPrendas().size();
		};

		Predicate<Atuendo> tienePrendasNecesarias = atuendo -> {
			List<Categoria> categoriasObligatorias = Arrays.asList(Categoria.SUPERIOR, Categoria.INFERIOR,
					Categoria.CALZADO);

			return atuendo.getPrendas().stream().map(prenda -> prenda.getTipoPrenda().getCategoria()).collect(Collectors.toList())
					.containsAll(categoriasObligatorias) && atuendo.getPrendas().stream().anyMatch(prenda -> prenda.esSuperiorBasica());
		};

		Predicate<Atuendo> tieneSuperposicionCorrecta = atuendo -> atuendo.getPrendas().stream().collect(groupingBy(Prenda::getCategoria, counting()))
				.entrySet().stream()
				.filter(map -> map.getValue() > 1)
				.allMatch(map -> map.getKey().admiteSuperposicion());

		Predicate<Atuendo> estaDisponible = atuendo -> atuendo.estaDisponible();

		Predicate<Atuendo> esAtuendoValido = noRepiteTiposDePrenda.and(tienePrendasNecesarias).and(tieneSuperposicionCorrecta).and(estaDisponible);

		return Sets.powerSet(guardarropa.getPrendas()).stream().map(Atuendo::new).filter(esAtuendoValido).collect(Collectors.toSet());
	}
	
	//method for mock testing
	public ProveedorClima crearProveedorClima() {
		return new ClimaOWM();
	}
}