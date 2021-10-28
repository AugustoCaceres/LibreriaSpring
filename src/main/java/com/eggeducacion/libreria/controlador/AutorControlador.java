package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.servicio.AutorServicio;
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
@RequestMapping("/autores")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(){
        ModelAndView mav = new ModelAndView("autores");
        mav.addObject("autores", autorServicio.obtenerAutores());
        return mav;
    } 
    
    @GetMapping("/crear")
    public ModelAndView crearAutor(){
        ModelAndView mav = new ModelAndView("autor-formulario");
        mav.addObject("autor", new Autor());
        mav.addObject("title", "Crear autor");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView modificarAutor(@PathVariable String id) throws ExcepcionServicio{
        ModelAndView mav = new ModelAndView("autor-formulario");
        mav.addObject("autor", autorServicio.obtenerAutorPorId(id));
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) throws ExcepcionServicio{
        autorServicio.cargarAutor(nombre);
        return new RedirectView("/autores");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre) throws ExcepcionServicio{
        autorServicio.modificarAutor(id, nombre);
        return new RedirectView("/autores");
    }
    
    @PostMapping("/eliminar")
    public RedirectView eliminar(@RequestParam String id) throws ExcepcionServicio {
        autorServicio.bajaAutor(id);
        return new RedirectView("/autores");
    }    
}
