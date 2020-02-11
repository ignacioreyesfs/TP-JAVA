package queMePongo.builders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import queMePongo.exceptions.FaltanDatosException;
import queMePongo.prenda.Material;
import queMePongo.prenda.Prenda;
import queMePongo.prenda.TipoPrenda;

public class ValidadorExistencia {
	TipoPrenda tipoPrenda;
	Material material;
	Color colorPrimario;
	
	public ValidadorExistencia(TipoPrenda tipoPrenda, Material material, Color colorPrimario) {
		this.tipoPrenda = tipoPrenda;
		this.material = material;
		this.colorPrimario = colorPrimario;
	}
	
	public void validar() {
		validarDatosObligatorios();
	}

	private void validarDatosObligatorios() {
		List<String> mensajesError = new ArrayList<>();
		
		if (this.tipoPrenda == null) {
			mensajesError.add("Debe especificar el tipo de prenda");
		}
		
		if (this.material == null) {
			mensajesError.add("Debe especificar el material");
		}
		
		if (this.colorPrimario == null) {
			mensajesError.add("Debe especificar el color primario");
		}
		
        
        if (mensajesError.size() > 0) throw new FaltanDatosException(String.join("\n", mensajesError));
	}

}
