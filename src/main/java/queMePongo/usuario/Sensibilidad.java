package queMePongo.usuario;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import queMePongo.prenda.Categoria;
import queMePongo.prenda.NivelDeAbrigo;

@Entity
public class Sensibilidad {
	@Id @GeneratedValue
	private Integer id;
	@ElementCollection
	@MapKeyEnumerated(EnumType.STRING)
	@CollectionTable(name = "sensibilidad_coeficiente", joinColumns = {@JoinColumn(name = "sensibilidad_id")})
    @Column(name = "coeficiente")
    @MapKeyColumn(name = "categoria")
	private Map<Categoria, Double> mapSensibilidad = new HashMap<>();

	public Sensibilidad(){
		mapSensibilidad.put(Categoria.SUPERIOR, 1.0);
		mapSensibilidad.put(Categoria.INFERIOR, 1.0);
		mapSensibilidad.put(Categoria.CALZADO, 1.0);
		mapSensibilidad.put(Categoria.ACCESORIO, 1.0);
		mapSensibilidad.put(Categoria.CABEZA, 1.0);
		mapSensibilidad.put(Categoria.CUELLO, 1.0);
		mapSensibilidad.put(Categoria.MANOS, 1.0);
	}
	
	public void ajustarPorcentajeExtraSensibilidad(Categoria categoria, NivelDeAbrigo nivelDeAbrigo) {
		double oldValue = mapSensibilidad.get(categoria);
		double newValue = oldValue + nivelDeAbrigo.ajustePorcentajeAbrigoExtra();
		mapSensibilidad.put(categoria, newValue);
	}
	
	public double getPorcentajeExtraSensibilidad(Categoria categoria) {
		return mapSensibilidad.get(categoria);
	}
	
	public Integer getId() {
		return id;
	}
}
