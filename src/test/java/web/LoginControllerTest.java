package web;

import com.google.common.hash.Hashing;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.controllers.LoginController;
import spark.Request;
import spark.Response;
import spark.Session;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LoginControllerTest {
    @Mock
    private Request request;
    @Mock
    private Response response;
    @Mock
    private RepositorioUsuarios repositorioUsuarios;
    @Mock
    private Usuario userRecovery;
    @InjectMocks
    private LoginController loginController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        loginController = new LoginController(repositorioUsuarios);
    }

    @Test
    public void cuandoElLoginEsCorrectoEntoncesSeRedirecciona() {
        when(request.queryParams("username")).thenReturn("dds");
        when(request.queryParams("password")).thenReturn("dds");
        String password = Hashing.sha256().hashString("dds", StandardCharsets.UTF_8).toString();
        when(repositorioUsuarios.getUsuario("dds", password)).thenReturn(Optional.of(userRecovery));
        when(request.session()).thenReturn(mock(Session.class));

        loginController.login(request, response);

        verify(response).redirect("/");
    }

    @Test
    public void cuandoElLoginEsIncorrectoEntoncesNoSeRedirecciona() {
        when(request.queryParams("username")).thenReturn("dds");
        when(request.queryParams("password")).thenReturn("dds");
        when(repositorioUsuarios.getUsuario("dds", "dds")).thenReturn(Optional.empty());

        loginController.login(request, response);

        verifyZeroInteractions(response);
    }
}
