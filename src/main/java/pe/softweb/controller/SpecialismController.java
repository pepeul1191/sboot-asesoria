package pe.softweb.controller;

import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pe.softweb.config.Database;
import pe.softweb.model.Specialism;
import pe.softweb.helper.SpecialismHelper;

@RestController
@RequestMapping(
  value = "/specialism"
)
public class SpecialismController extends ApplicationController
{
  
  @RequestMapping(
    value = {"/list", "/listar"}, 
    method = RequestMethod.GET,
    produces={"text/html; charset=utf-8"}  
  )
  public ResponseEntity<String> list() 
  {
    String response = "";
    HttpStatus status = HttpStatus.OK;
    Database db = new Database();
    try {
      db.open();
      response = Specialism.findAll().toJson(true).toString();
    }catch (Exception e) {
      e.printStackTrace();
      JSONArray error = new JSONArray();
      error.put("Se ha producido un error en listar las especialidades");
      error.put(e.toString());
      response = error.toString();
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } finally {
      if(db.getDb().hasConnection()){
        db.close();
      }
    }
    return new ResponseEntity<>(response, status);
  }

  @RequestMapping(
    value = {"/", ""}, 
    method = RequestMethod.GET,
    produces={"text/html; charset=utf-8"}  
  )
  public ModelAndView index() 
  {
    Database db = new Database();
    List<Specialism> specialisms = null;
    try {
      db.open();
      specialisms = Specialism.findAll();
    }catch (Exception e) {
      e.printStackTrace();
    }
    // locals
    SpecialismHelper helper = new SpecialismHelper(this.constants);
    final var locals = new HashMap<String, Object>();
    locals.put("constants", this.constants);
    locals.put("csss", helper.indexCSS());
    locals.put("jss", helper.indexJS());
    locals.put("specialisms", specialisms);
    /*
    for (Specialism specialism : specialisms) {
      System.out.println(specialism.get("id"));
    }
    */
    System.out.println("1 +++++++++++++++++++++++++++++++++");
    for (Specialism specialism : specialisms) {
      System.out.println(specialism);
    }
    System.out.println("2 +++++++++++++++++++++++++++++++++");
    locals.put("title", "Lista de Especialides");
    // view
    ModelAndView model =  new ModelAndView("specialism/index", locals);
    model.setStatus(HttpStatus.ACCEPTED);
    return model;
  }
}