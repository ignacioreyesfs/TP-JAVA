package server.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import queMePongo.evento.Evento;
import queMePongo.evento.EventoConFecha;
import queMePongo.evento.RepositorioEventos;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.RepositorioGuardarropa;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import server.SessionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class EventoController implements WithGlobalEntityManager, TransactionalOps  {

	public String show(Request req, Response res) {
		Integer idUsuario = SessionService.getSessionId(req);
		Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario);
		String fechaHoy = LocalDateTime.now().withSecond(0).withNano(0).toString();
		HashMap<String, Object> viewModel = new HashMap<>();
		viewModel.put("guardarropas", usuario.getGuardarropas());
		viewModel.put("fechaMin", fechaHoy);
		ModelAndView modelAndView = new ModelAndView(viewModel, "eventos.hbs");
		return new HandlebarsTemplateEngine().render(modelAndView);
	}

	public String crear(Request req, Response res) {
		String nombre = req.queryParams("nombreElegido");
		String fecha = req.queryParams("fecha");
		String[] guardarropasSeleccionados = req.queryParamsValues("seleccionarGuardarropas");
		Integer idUsuario = SessionService.getSessionId(req);
		Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(idUsuario);
		Set<Guardarropa> guardarropasSolicitados = this.buscarGuardarropa(guardarropasSeleccionados);
		EventoConFecha nuevoEvento = new EventoConFecha(usuario, nombre, LocalDateTime.parse(fecha), guardarropasSolicitados);
		
		withTransaction(() -> {
			RepositorioEventos.getInstance().guardarEvento(nuevoEvento);
		});
				
		res.redirect("/eventos");

		return "OK";
	}

	private Set<Guardarropa> buscarGuardarropa(String[] guardarropasSeleccionados) {
		List<String> elegidos = Arrays.asList(guardarropasSeleccionados);
		List<Integer> idGuardarropas = elegidos.stream().map(Integer::parseInt).collect(Collectors.toList());
		Set<Guardarropa> seleccionados = idGuardarropas.stream()
												 .map(guardarropa -> RepositorioGuardarropa.getInstance().buscarGuardarropaPorId(guardarropa))
												 .collect(Collectors.toSet());
		
		return seleccionados;
	}

	public String listarEventos(Request req, Response res) {
		Integer idUsuario = SessionService.getSessionId(req);
        List<Evento> eventos = RepositorioEventos.getInstance().buscarEventosPorUsuarioProximos(idUsuario);
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("eventos", eventos);
        ModelAndView modelAndView = new ModelAndView(viewModel, "eventos-list.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
}
