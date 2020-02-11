package server.controllers;

import java.awt.Color;
import java.util.HashMap;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import queMePongo.builders.PrendaBuilder;
import queMePongo.prenda.Guardarropa;
import queMePongo.prenda.Material;
import queMePongo.prenda.Prenda;
import queMePongo.prenda.RepositorioGuardarropa;
import queMePongo.prenda.TipoPrenda;
import queMePongo.usuario.RepositorioUsuarios;
import queMePongo.usuario.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class PrendaController implements WithGlobalEntityManager, TransactionalOps {

    public String show(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        int idGuardarropas = Integer.parseInt(req.params(":idGuardarropa"));
        int idPrenda= Integer.parseInt(req.params(":idPrenda"));
        Guardarropa guardarropas = RepositorioGuardarropa.getInstance().buscarGuardarropaPorId(idGuardarropas);
        Prenda prenda = obtenerPrenda(guardarropas,idPrenda);
        viewModel.put("prenda", prenda);
        ModelAndView modelAndView = new ModelAndView(viewModel, "vistaPrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String crear(Request req, Response res) {
    	PrendaBuilder builder = new PrendaBuilder();
    	TipoPrenda tipoPrenda = TipoPrenda.valueOf(req.cookie("tipoPrenda"));
    	builder.setTipoPrenda(tipoPrenda);
    	builder.setNombre(req.cookie("nombrePrenda"));
    	Material materialPrenda = Material.valueOf(req.cookie("materialPrenda"));
    	builder.setMaterial(materialPrenda);
    	builder.setImagen(req.cookie("imagenPrenda"));
    	Color colorPrimario = Color.decode(req.cookie("colorPrimario"));
    	builder.setColorPrimario(colorPrimario);
		Color colorSecundario = null;

    	try {
    		colorSecundario = Color.decode(req.cookie("colorSecundario"));
    	}
    	catch (NullPointerException e) {}
    	catch (NumberFormatException e) {}
    	
    	builder.setColorSecundario(colorSecundario);
    	
    	Prenda prenda = builder.build();
        int idGuardarropa = Integer.parseInt(req.params(":idGuardarropa"));
        Guardarropa guardarropa = RepositorioGuardarropa.getInstance().buscarGuardarropaPorId(idGuardarropa);
        Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorId(1);
        usuario.agregarPrenda(guardarropa, prenda);
        
        withTransaction(() ->{
    		RepositorioUsuarios.getInstance().guardarUsuario(usuario);
        });

        res = eliminarCookiesPrenda(res);
    	        
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("mensaje", "Su prenda fue agregada al guardarropas.");
        
        res.redirect("/guardarropas");
        return "OK";
    }

    private Prenda obtenerPrenda(Guardarropa guardarropa, int idPrenda) {
    	return guardarropa.getPrendaById(idPrenda);
    } 
    
    private Response eliminarCookiesPrenda(Response res) {
        res.removeCookie("tipoPrenda");
    	res.removeCookie("nombrePrenda");
        res.removeCookie("materialPrenda");
        res.removeCookie("imagenPrenda");
        res.removeCookie("coloresPrenda");
        return res;
    }
}
