package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Usuario;
import com.eggeducacion.libreria.excepciones.MiExcepcion;
import com.eggeducacion.libreria.servicio.RolServicio;
import com.eggeducacion.libreria.servicio.UsuarioServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private RolServicio rolServicio;
    
    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
    
        if (flashMap != null){
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }
        
        mav.addObject("usuarios", usuarioServicio.mostrarTodos());
        return mav;
    }
    
    @GetMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER-ADMIN')")
    public ModelAndView crear(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (flashMap != null){
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }
        
        mav.addObject("title", "Crear usuario");
        mav.addObject("action", "guardar");
        mav.addObject("roles", rolServicio.buscarTodos());
        
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER-ADMIN')")
    public ModelAndView modificar(@PathVariable Integer id, HttpServletRequest request, RedirectAttributes attributes){
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("usuario", flashMap.get("usuario"));
            } else {
                mav.addObject("usuario", usuarioServicio.buscarPorId(id));
            }
            
            mav.addObject("title", "Modificar usuario");
            mav.addObject("action", "modificar");
            mav.addObject("roles", rolServicio.buscarTodos());
        } catch (MiExcepcion e){
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/usuario");
        }
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Usuario usuario, RedirectAttributes attributes){
        RedirectView redirectView = new RedirectView("/usuario");
        
        try {
            //usuarioServicio.crear(usuario.getNombre(), usuario.getApellido(), usuario.getCorreo(), usuario.getClave(), usuario.getRol());
            usuarioServicio.crear(usuario);
            attributes.addFlashAttribute("exito", "La creación se ha realizado satisfactoriamente");
        } catch (MiExcepcion e){
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/usuario/crear");
        }
        
        return redirectView;
    }
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('SUPER-ADMIN')")
    public RedirectView modificar(@ModelAttribute Usuario usuario, RedirectAttributes attributes){
        RedirectView redirectView = new RedirectView("/usuario");
        
        try {
            usuarioServicio.modificar(usuario);
            attributes.addFlashAttribute("exito", "La modificación se ha realizado satisfactoriamente");
        } catch (MiExcepcion e){
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/usuario/editar" + usuario.getId());
        }
        
        return redirectView;
    }
    
    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable Integer id){
        usuarioServicio.habilitar(id);
        return new RedirectView("/usuario");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Integer id){
        usuarioServicio.eliminar(id);
        return new RedirectView("/usuario");
    }
}
