package queMePongo.prenda;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioGuardarropa implements WithGlobalEntityManager {
	private static RepositorioGuardarropa instancia;

	public static RepositorioGuardarropa getInstance() {
		if (instancia == null) {
			instancia = new RepositorioGuardarropa();
		}
		return instancia;
	}
	
	public Guardarropa guardarGuardarropa(Guardarropa guardarropa) {
		if(guardarropa.getId() == null) {
			entityManager().persist(guardarropa);
		}else {
			guardarropa = entityManager().merge(guardarropa);
		}
		return guardarropa;
	}
	
	public void eliminarGuardarropa(Guardarropa guardarropa) {
    	if (entityManager().contains(guardarropa)) {
    		entityManager().remove(guardarropa);
        } else {
        	entityManager().remove(entityManager().merge(guardarropa));
        }
    }
	
	public Guardarropa buscarGuardarropaPorId(int id) {
		return entityManager().find(Guardarropa.class, id);
	}
	
}
