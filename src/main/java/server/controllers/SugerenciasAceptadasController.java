package server.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import queMePongo.evento.EventoConFecha;
import queMePongo.evento.RepositorioEventos;
import queMePongo.prenda.Categoria;
import queMePongo.prenda.NivelDeAbrigo;
import queMePongo.prenda.RepositorioGuardarropa;
import queMePongo.prenda.*;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SugerenciasAceptadasController implements WithGlobalEntityManager, TransactionalOps {
	public String show(Request req, Response res) {
		HashMap<String, Object> viewModel = new HashMap<>();
		ModelAndView modelAndView = new ModelAndView(viewModel, "sugerenciasAceptadas.hbs");
		Integer idUsuario = SessionService.getSessionId(req);
		Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario); 
		List<Sugerencia>sugerencias = usuario.getEleccionesSinCalificar();
		viewModel.put("sugerencias", sugerencias);
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
	
	public String calificar(Request req, Response res) {
		String calificacionCabeza = req.queryParams("calificacionCabeza");
		String calificacionCuello = req.queryParams("calificacionCuello");
		String calificacionSuperior = req.queryParams("calificacionSuperior");
		String calificacionInferior = req.queryParams("calificacionInferior");
		String calificacionManos = req.queryParams("calificacionManos");
		String calificacionCalzado = req.queryParams("calificacionCalzado");
		int idSugerencia = Integer.parseInt(req.queryParams("idSugerencia"));
		Integer idUsuario = SessionService.getSessionId(req);
		Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario);
		Sugerencia sugerencia = usuario.getEleccionesHistory().stream().filter(sug -> sug.getId() == idSugerencia)
																		.collect(Collectors.toList()).get(0);
		
		sugerencia.calificar();
		usuario.ajustarSensibilidad(Categoria.CABEZA, NivelDeAbrigo.valueOf(calificacionCabeza));
		usuario.ajustarSensibilidad(Categoria.CUELLO, NivelDeAbrigo.valueOf(calificacionCuello));
		usuario.ajustarSensibilidad(Categoria.SUPERIOR, NivelDeAbrigo.valueOf(calificacionSuperior));
		usuario.ajustarSensibilidad(Categoria.INFERIOR, NivelDeAbrigo.valueOf(calificacionInferior));
		usuario.ajustarSensibilidad(Categoria.MANOS, NivelDeAbrigo.valueOf(calificacionManos));
		usuario.ajustarSensibilidad(Categoria.CALZADO, NivelDeAbrigo.valueOf(calificacionCalzado));
		
		withTransaction(() -> {
			RepositorioUsuarios.getInstance().guardarUsuario(usuario);
		});
		
		res.redirect("/sugerencias/aceptadas");
		return "OK";
	}
}
