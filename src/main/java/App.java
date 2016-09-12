import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class App {
  public static void main(String[] args) {
    String layout = "templates/layout.vtl";
    staticFileLocation("/public");

    ChangeMachine ChMachine = new ChangeMachine();

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/changemachine", (request, response) -> {
      Float inputTotal = Float.parseFloat(request.queryParams("inputamount"));
      Map<String, Object> model = new HashMap<String, Object>();

      if (ChMachine.getTotalcash() >= inputTotal) {
        model.put("template", "templates/changemachine.vtl");
        model.put("inputamount", String.format("%.2f", inputTotal));
        model.put("changeamount", ChMachine.makeChange(inputTotal));
        return new ModelAndView(model, layout);
      } else {
        model.put("template", "templates/not-enough.vtl");
        model.put("inputamount", request.queryParams("inputamount"));
        model.put("machineamount", String.format("%.2f", ChMachine.getTotalcash()));
        return new ModelAndView(model, layout);
      }
    }, new VelocityTemplateEngine());
  }
}