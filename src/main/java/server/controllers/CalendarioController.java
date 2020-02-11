package server.controllers;

import queMePongo.evento.RepositorioEventos;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;

public class CalendarioController {
    
    public String show(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        Integer idUsuario = SessionService.getSessionId(req);
        RepositorioEventos repositorioEventos = RepositorioEventos.getInstance();
        viewModel.put("eventos", repositorioEventos.buscarEventosPorUsuarioProximos(idUsuario));
        ModelAndView modelAndView = new ModelAndView(viewModel, "calendario.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
}
