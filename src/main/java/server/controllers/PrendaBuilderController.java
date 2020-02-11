package server.controllers;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import queMePongo.builders.ValidadorColores;
import queMePongo.exceptions.ColorSecundarioIgualPrimarioException;
import queMePongo.prenda.Material;
import queMePongo.prenda.TipoPrenda;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class PrendaBuilderController {
    public String crearPrendaBuilder(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
    	viewModel.put("tiposPrenda", TipoPrenda.values());
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
        ModelAndView modelAndView = new ModelAndView(viewModel, "tipoPrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
    
    public String definirTipoPrenda(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
    	String tipoPrenda = req.queryParams("tipoPrenda");
    	res.cookie("tipoPrenda", tipoPrenda);
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
        ModelAndView modelAndView = new ModelAndView(viewModel, "nombrePrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
    
    public String definirNombrePrenda(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
    	String nombrePrenda = req.queryParams("nombrePrenda");
    	res.cookie("nombrePrenda", nombrePrenda);
    	String tipoPrenda = req.cookie("tipoPrenda");
       	List<Material> materialesPrenda = materialesPosibles(tipoPrenda);
    	viewModel.put("materialesPrenda", materialesPrenda);
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
        ModelAndView modelAndView = new ModelAndView(viewModel, "materialPrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }

    public String definirMaterialPrenda(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
    	String materialPrenda = req.queryParams("materialPrenda");
    	res.cookie("materialPrenda", materialPrenda);
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
    	ModelAndView modelAndView = new ModelAndView(viewModel, "imagenPrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
    
    public String definirImagenPrenda(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
    	String imagenPrenda = req.queryParams("imagenPrenda");
    	res.cookie("imagenPrenda", imagenPrenda);
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
    	viewModel.put("checkbox",false);
    	ModelAndView modelAndView = new ModelAndView(viewModel, "coloresPrenda.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
    
    public String definirColoresPrenda(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        String idGuardarropa = req.params(":idGuardarropa");
    	viewModel.put("idGuardarropa", idGuardarropa);
    	String colorPrimario = req.queryParams("color-primario");
    	String colorSecundario = req.queryParams("color-secundario");
    	boolean esColorUnico = Boolean.valueOf(req.queryParams("input-color-unico"));

    	if (!esColorUnico) {
        	try {
               	new ValidadorColores(Color.decode(colorPrimario), Color.decode(colorSecundario)).validar();
        	}    	
        	catch (ColorSecundarioIgualPrimarioException e) {
        		viewModel.put("error","El color primario debe ser distinto al secundario.");
        		viewModel.put("colorPrimario", colorPrimario);
        		viewModel.put("colorSecundario", colorSecundario);
            	ModelAndView modelAndView = new ModelAndView(viewModel, "coloresPrenda.hbs");

                return new HandlebarsTemplateEngine().render(modelAndView);
        	}
    	}
    	else
    	{
    		colorSecundario=null;
    	}
    	
    	res.cookie("colorPrimario", colorPrimario);
    	res.cookie("colorSecundario", colorSecundario);
    	viewModel.put("tipoPrenda", req.cookie("tipoPrenda"));
    	viewModel.put("nombrePrenda", req.cookie("nombrePrenda"));
    	viewModel.put("materialPrenda", req.cookie("materialPrenda"));
    	viewModel.put("imagenPrenda", req.cookie("imagenPrenda"));
    	viewModel.put("colorPrimario", req.cookie("colorPrimario"));
    	viewModel.put("colorSecundario", req.cookie("colorSecundario"));
    	ModelAndView modelAndView = new ModelAndView(viewModel, "confirmacionPrenda.hbs");

        return new HandlebarsTemplateEngine().render(modelAndView);
    }
    
    private List<Material> materialesPosibles(String tipoPrenda){
    	List<Material> materiales = Arrays.asList(Material.values()); 
    	TipoPrenda tipoPrendaSeleccionado = TipoPrenda.valueOf(tipoPrenda);
    	return materiales.stream().filter(material -> tipoPrendaSeleccionado.puedeSerDeMaterial(material)).collect(Collectors.toList());
    }
}
