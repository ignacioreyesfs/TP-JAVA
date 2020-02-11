package queMePongo.usuario;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioUsuarios implements WithGlobalEntityManager {

	private Set<Usuario> usuarios = new HashSet<>();
	private static RepositorioUsuarios instancia;

	public static RepositorioUsuarios getInstance() {
		if (instancia == null) {
			instancia = new RepositorioUsuarios();
		}
		return instancia;
	}

	public Usuario guardarUsuario(Usuario usuario) {
		if(usuario.getId() == null) {
			entityManager().persist(usuario);
		}else {
			usuario = entityManager().merge(usuario);
		}
		return usuario;
	}
	
	public void eliminarUsuario(Usuario usuario) {
    	if (entityManager().contains(usuario)) {
    		entityManager().remove(usuario);
        } else {
        	entityManager().remove(entityManager().merge(usuario));
        }
    }

	public Usuario buscarUsuarioPorId(int id) {
		return entityManager().find(Usuario.class, id);
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public Optional<Usuario> getUsuario(String user, String password) {
		return entityManager()
				.createQuery("from Usuario where user = :user and password = :password", Usuario.class)
				.setParameter("user", user)
                .setParameter("password", password).getResultList()
                .stream().findFirst();
	}
}
