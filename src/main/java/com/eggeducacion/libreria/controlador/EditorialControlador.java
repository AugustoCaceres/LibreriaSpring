package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.servicio.EditorialServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(){
        ModelAndView mav = new ModelAndView("editoriales");
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
    public ModelAndView modificarEditorial(@PathVariable String id) throws ExcepcionServicio{
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", editorialServicio.obtenerEditorialPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) throws ExcepcionServicio{
        editorialServicio.ingresarEditorial(nombre);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre) throws ExcepcionServicio{
        editorialServicio.modificarEditorial(id, nombre);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/eliminar")
    public RedirectView eliminar(@RequestParam String id) throws ExcepcionServicio {
        editorialServicio.bajaEditorial(id);
        return new RedirectView("/editoriales");
    }
}
