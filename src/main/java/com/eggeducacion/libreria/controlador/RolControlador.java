package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.servicio.RolServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/rol")
public class RolControlador {
    
    @Autowired
    private RolServicio rolServicio;
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("roles");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (flashMap != null){
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }
        
        mav.addObject("roles", rolServicio.buscarTodos());
        return mav;
    } 
    
    @GetMapping("/crear")
    @PreAuthorize("hasRole('SUPER-ADMIN')")
    public ModelAndView crear() {
        return new ModelAndView("rol-formulario");
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) {
        rolServicio.crear(nombre);
        return new RedirectView("/home");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Integer id){
        rolServicio.eliminar(id);
        return new RedirectView("/rol");
    }
}
