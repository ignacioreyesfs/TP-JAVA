package server.controllers;

import com.google.common.hash.Hashing;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

public class LoginController {
    private RepositorioUsuarios repositorio;
    private static final String LOGIN_CONNECTION_ERROR = "Error de conexión, pruebe más tarde";
    private static final String LOGIN_INVALID = "Usuario y/o contraseña inválida";

    public LoginController() {}

    public String show(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("login", "login");
        ModelAndView modelAndView = new ModelAndView(viewModel, "login.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String login(Request req, Response res) {
        String user = req.queryParams("username");
        String password = req.queryParams("password");

        try {
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            Optional<Usuario> userRecovery = RepositorioUsuarios.getInstance().getUsuario(user, password);
            if (userRecovery.isPresent()) {
                req.session(true);
                SessionService.setSessionId(req, userRecovery.get().getId());
                res.redirect("/");
                return null;
            } else {
                return getModelAndViewError(user, password, LOGIN_INVALID);
            }
        } catch (Exception e) {
        	password = "";
            return getModelAndViewError(user, password, LOGIN_CONNECTION_ERROR);
        }
    }

    private String getModelAndViewError(String user, String password, String message) {
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("isNotCorrect", true);
        viewModel.put("message", message);
        viewModel.put("user", user);
        viewModel.put("password", password);
        ModelAndView modelAndView = new ModelAndView(viewModel, "login.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public Object logout(Request req, Response res) {
        SessionService.removeSessionId(req);
        res.redirect("/");
        return null;
    }

    //used for tests
    public LoginController(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }
}
