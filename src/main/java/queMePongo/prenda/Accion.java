package queMePongo.prenda;

import queMePongo.exceptions.UnaOMasPrendasNoEstanDisponiblesException;
import queMePongo.usuario.Usuario;

public enum Accion {
	ACEPTAR{
		@Override
	    public void ejecutar(Sugerencia sugerencia, Usuario usuario) {
	    	if (sugerencia.getAtuendo().estaDisponible()) {
	            sugerencia.setEstado(Estado.ACEPTADO);
	            sugerencia.ocuparPrendas();
	            sugerencia.ajustarCoeficientePreferencia(usuario, 1);
	    	}
	    	else
	    		throw new UnaOMasPrendasNoEstanDisponiblesException(sugerencia.getAtuendo());
	    }

	    @Override
	    public void deshacer(Sugerencia sugerencia, Usuario usuario) {
	    	sugerencia.setEstado(Estado.PENDIENTE);
	    	sugerencia.liberarPrendas();
	    	sugerencia.ajustarCoeficientePreferencia(usuario, -1);
	    }
	},
	RECHAZAR{
		@Override
	    public void ejecutar(Sugerencia sugerencia, Usuario usuario) {
	        sugerencia.setEstado(Estado.RECHAZADO);
	    }

	    @Override
	    public void deshacer(Sugerencia sugerencia, Usuario usuario) {
	        sugerencia.setEstado(Estado.PENDIENTE);
	    }
	};
	
	public abstract void ejecutar(Sugerencia sugerencia, Usuario usuario);
	public abstract void deshacer(Sugerencia sugerencia, Usuario usuario);
}