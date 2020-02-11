package queMePongo.prenda;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;


import static org.junit.Assert.assertEquals;

import queMePongo.evento.RepositorioEventos;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Sensibilidad;
import queMePongo.usuario.Usuario;

public class TestGuardarropas{
	Guardarropa guardarropaId1;
	Usuario usuario;
	RepositorioUsuarios repositorioUsuarios;
	
	@Before
	public void init() {
		guardarropaId1 = new Guardarropa();
		usuario = new Usuario(new Sensibilidad());
		usuario.agregarGuardarropas(guardarropaId1);
		repositorioUsuarios = Mockito.spy(RepositorioUsuarios.getInstance());
		repositorioUsuarios.guardarUsuario(usuario);
	}
	
	@Test
	public void getGuardarropasByIdDebeDevolverUnGuardarropas() {
		Usuario usuarioBD = repositorioUsuarios.getUsuarios().stream().filter(u -> u.equals(usuario)).findFirst().orElse(null);
		//Guardarropa guardarropaById = usuarioBD.getGuardarropasById(1);
		//assertEquals(guardarropaId1,guardarropaById);
	}
}
