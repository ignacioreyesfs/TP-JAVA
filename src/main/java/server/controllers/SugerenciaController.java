package server.controllers;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import queMePongo.prenda.Atuendo;
import queMePongo.prenda.RepositorioSugerencias;
import queMePongo.prenda.Sugerencia;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SugerenciaController implements WithGlobalEntityManager, TransactionalOps {
  
    public String showSugerencias(Request req, Response res) {
        String eventoId = req.params(":idEvento");
        List<Sugerencia> sugerencias = RepositorioSugerencias.getInstance()
        													 .getSugerencias(eventoId)
        													 .stream()
        													 .filter(sugerencia -> !sugerencia.estaAceptada())
        													 .collect(Collectors.toList());
  
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("sugerencias", sugerencias);
        viewModel.put("idEvento", eventoId);
        
        ModelAndView modelAndView = new ModelAndView(viewModel, "sugerencias.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String showPrendas(Request req, Response res) {
        String atuendoId = req.params(":idAtuendo");
        Atuendo atuendo = RepositorioSugerencias.getInstance().getAtuendo(atuendoId);
        HashMap<String, Object> viewModel = new HashMap<>();
        String idEvento = req.params(":idEvento");
    	
        viewModel.put("idEvento", idEvento);
        viewModel.put("atuendo", atuendo);
        
        ModelAndView modelAndView = new ModelAndView(viewModel, "prendas.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String aceptarSugerencia(Request req, Response res) {
        Integer idUsuario = SessionService.getSessionId(req);
        Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario);
        String sugerenciaId = req.params(":idSugerencia");
        Sugerencia sugerencia = RepositorioSugerencias.getInstance().getSugerencia(sugerenciaId);
        
        withTransaction(() -> {
        	usuario.aceptarSugerencia(sugerencia);
        });

        res.redirect("/eventos");
        
        return "OK";
    }
}
