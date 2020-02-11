package server.controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;

public class HomeController{
    public String home(Request req, Response res) {
        HashMap<String, Object> viewModel = new HashMap<>();
        viewModel.put("home", "home");
        ModelAndView modelAndView = new ModelAndView(viewModel, "home.hbs");
        return new HandlebarsTemplateEngine().render(modelAndView);
    }
}
