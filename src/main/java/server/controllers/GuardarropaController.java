package server.controllers;

import java.util.HashMap;
import java.util.Set;
import queMePongo.prenda.Guardarropa;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class GuardarropaController {
	public String show(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        Integer idUsuario = SessionService.getSessionId(req);
        Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario);
        viewModel.put("guardarropasUsuario", usuario.getGuardarropas());
        ModelAndView modelAndView = new ModelAndView(viewModel, "guardarropas.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
	
	public Set<Guardarropa> guardarropasUsuario(int idUsuario){
		return RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario).getGuardarropas();
	}
}
