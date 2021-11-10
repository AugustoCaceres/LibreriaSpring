package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.servicio.EditorialServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("editoriales");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (flashMap != null){
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }
        
        mav.addObject("editoriales", editorialServicio.obtenerEditoriales());
        return mav;
    } 
    
    @GetMapping("/crear")
    public ModelAndView crearEditorial(){
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear editorial");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView modificarEditorial(@PathVariable String id) throws Exception{
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", editorialServicio.obtenerEditorialPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes attributes) throws Exception{
        try {
            editorialServicio.ingresarEditorial(nombre);
            attributes.addFlashAttribute("exito", "Se ha ingresado la editorial con éxito!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/editoriales");
        }
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes attribute) throws Exception{
        try {
            editorialServicio.modificarEditorial(id, nombre);
            attribute.addFlashAttribute("exito", "Se ha modificado la editorial con éxito.");
        } catch (Exception e){
            attribute.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/editoriales");
        }
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) throws Exception{
        editorialServicio.bajaEditorial(id);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) throws Exception{
        editorialServicio.altaEditorial(id);
        return new RedirectView("/editoriales");
    }
}
