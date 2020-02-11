package queMePongo.usuario;

import queMePongo.exceptions.GuardarropaLlenoException;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.Prenda;

public enum TipoUsuario {
	GRATUITO{
		private final int limitePrendasPorGuardarropa = 10;
	    
	    public void agregarPrenda(Guardarropa guardarropa, Prenda prenda) {
	    	this.validarLimite(guardarropa);
	    	guardarropa.agregarPrenda(prenda);
	    }
	    
	    private void validarLimite(Guardarropa guardarropa) {
	    	if(guardarropa.cantidadPrendas() == this.getLimitePrendas()) {
	    		throw new GuardarropaLlenoException();
	    	}
	    }
	    
	    private int getLimitePrendas() {
	    	return limitePrendasPorGuardarropa;
	    }
	},
	PREMIUM{
		public void agregarPrenda(Guardarropa guardarropa, Prenda prenda) {
	    	guardarropa.agregarPrenda(prenda);
	    }
	};
	public abstract void agregarPrenda(Guardarropa guardarropa, Prenda prenda);
}