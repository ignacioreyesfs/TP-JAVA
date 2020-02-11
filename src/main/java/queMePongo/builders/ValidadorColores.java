package queMePongo.builders;

import java.awt.Color;

import queMePongo.exceptions.ColorSecundarioIgualPrimarioException;

public class ValidadorColores {
	
	Color colorPrimario;
	Color colorSecundario;
	
	public ValidadorColores(Color colorPrimario, Color colorSecundario) {
		this.colorPrimario = colorPrimario;
		this.colorSecundario = colorSecundario;
	}
	
	public void validar() {
		if(colorSecundario != null)
			validarColoresDiferentes();
	}
	
	private void validarColoresDiferentes() {
        if (this.colorPrimario.equals(this.colorSecundario)) throw new ColorSecundarioIgualPrimarioException();
	}
}
