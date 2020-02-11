package queMePongo.builders;

import queMePongo.exceptions.MaterialInconsistenteException;
import queMePongo.prenda.Material;
import queMePongo.prenda.TipoPrenda;

public class ValidadorMaterial {
	TipoPrenda tipoPrenda;
	Material material;

	public ValidadorMaterial(TipoPrenda tipoPrenda, Material material) {
		this.tipoPrenda = tipoPrenda;
		this.material = material;
	}

	public void validar() {
		validarConsistenciaDelMaterial();
	}
	
	private void validarConsistenciaDelMaterial() {
        if (!this.tipoPrenda.puedeSerDeMaterial(this.material)) throw new MaterialInconsistenteException();
	}

}
