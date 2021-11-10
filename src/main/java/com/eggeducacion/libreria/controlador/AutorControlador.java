package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.servicio.AutorServicio;
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
@RequestMapping("/autores")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("autores");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (flashMap != null){
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }
        
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
    public ModelAndView modificarAutor(@PathVariable String id) throws Exception{
        ModelAndView mav = new ModelAndView("autor-formulario");
        mav.addObject("autor", autorServicio.obtenerAutorPorId(id));
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes attributes) throws Exception{
        try {
            autorServicio.cargarAutor(nombre);
            attributes.addFlashAttribute("exito", "El autor se ha creado con éxito");
        } catch (Exception e){
            attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/autores");
        }
        return new RedirectView("/autores");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre, RedirectAttributes attributes) throws Exception{
        try {
            autorServicio.modificarAutor(id, nombre);
            attributes.addFlashAttribute("exito", "Se ha modificado al autor con éxito");
        } catch (Exception e){
            attributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/autores");
        }
        return new RedirectView("/autores");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) throws Exception{
        autorServicio.bajaAutor(id);
        return new RedirectView("/autores");
    }   
    
    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) throws Exception{
        autorServicio.altaAutor(id);
        return new RedirectView("/autores");
    }
}
