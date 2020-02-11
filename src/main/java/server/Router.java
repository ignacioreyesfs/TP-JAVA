package server;

import server.controllers.HomeController;
import server.controllers.LoginController;
import server.controllers.SugerenciasAceptadasController;
import server.controllers.EventoController;
import server.controllers.GuardarropaController;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import server.controllers.*;
import spark.Spark;

public class Router implements WithGlobalEntityManager{
    static Router _instance;

    private Router() {

    }

    public static Router instance() {
        if(_instance == null) {
            _instance = new Router();
        }
        return _instance;
    }

    public void configurar() {
        
    	Spark.staticFileLocation("/public");
        
    	//Home
    	HomeController homeController = new HomeController();
    	Spark.get("/", homeController::home);
        
        //Login
        LoginController loginController = new LoginController();
        Spark.get("/login", loginController::show);
        Spark.post("/login", loginController::login);
       
        //Calificaciones
        SugerenciasAceptadasController sugerenciasAceptadas = new SugerenciasAceptadasController();
        Spark.get("/sugerencias/aceptadas", sugerenciasAceptadas::show);
        Spark.post("/sugerencias/aceptadas", sugerenciasAceptadas::calificar);
        
        //Eventos
        Spark.delete("/login", loginController::logout);
        EventoController eventoController = new EventoController();
        CalendarioController calendarioController = new CalendarioController();
        SugerenciaController sugerenciaController = new SugerenciaController();
        
        Spark.get("/eventos/:idEvento/sugerencias", sugerenciaController::showSugerencias);
        Spark.get("/eventos/:idEvento/sugerencias/:idSugerencia", sugerenciaController::aceptarSugerencia);
        Spark.get("/eventos/:idEvento/sugerencias/:idSugerencia/atuendos/:idAtuendo", sugerenciaController::showPrendas);
        Spark.get("/eventos/nuevoEvento", eventoController::show);
        Spark.get("/eventos", eventoController::listarEventos);
        Spark.get("/eventos/proximos", calendarioController::show);
        
        Spark.post("/eventos", eventoController::crear);
        
        //Guardarropas
        GuardarropaController guardarropaController = new GuardarropaController();
        Spark.get("/guardarropas", guardarropaController::show);

        PrendaController prendaController = new PrendaController();
        Spark.post("/guardarropas/:idGuardarropa/prendas", prendaController::crear);
        
        PrendaBuilderController prendaBuilderController = new PrendaBuilderController();
        Spark.get("/guardarropas/:idGuardarropa/prendas/tipo", prendaBuilderController::crearPrendaBuilder);
        Spark.get("/guardarropas/:idGuardarropa/prendas/nombre", prendaBuilderController::definirTipoPrenda);
        Spark.get("/guardarropas/:idGuardarropa/prendas/material", prendaBuilderController::definirNombrePrenda);
        Spark.get("/guardarropas/:idGuardarropa/prendas/imagen", prendaBuilderController::definirMaterialPrenda);
        Spark.get("/guardarropas/:idGuardarropa/prendas/colores", prendaBuilderController::definirImagenPrenda);
        Spark.get("/guardarropas/:idGuardarropa/prendas/confirmacion", prendaBuilderController::definirColoresPrenda);
        
        Spark.after((req, res) -> {
        	entityManager().clear();
        });

        Spark.before((req, res) -> {
            if (req.pathInfo().equals("/login")) {
                return;
            }

            if (SessionService.getSessionId(req) == null) {
                res.redirect("/login");
            }
        });
    }
}
