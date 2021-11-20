package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Usuario;
import com.eggeducacion.libreria.excepciones.MiExcepcion;
import com.eggeducacion.libreria.servicio.UsuarioServicio;
import java.security.Principal;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/home")
    public ModelAndView inicio(){
        return new ModelAndView("index");
    }
    
    @GetMapping("/login")
    public ModelAndView login(@RequestParam (required = false) String error, @RequestParam (required = false) String logout, Principal principal){
        ModelAndView mav = new ModelAndView("login");
        
        if (error != null){
            mav.addObject("error", "Usuario o contraseña inválida.");
        }
        
        if (logout != null){
            mav.addObject("logout", "Has salido correctamente!");
        }
        
        if (principal != null){
            mav.addObject("redirect:/");
        }
        
        return mav;
    }
    
    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal){
        ModelAndView mav = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (principal != null){
            mav.setViewName("redirect:/home");
        }
        
        if (flashMap != null){
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }
        return mav;
    }
    
    @PostMapping("/registro")
    public RedirectView signup(@ModelAttribute Usuario usuario, HttpServletRequest request, RedirectAttributes attributes){
        RedirectView redirectView = new RedirectView("/login");
        
        try {
            usuarioServicio.crear(usuario);
            request.login(usuario.getCorreo(), usuario.getClave());
        } catch (MiExcepcion e){
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/signup");
        } catch(ServletException e){
            attributes.addFlashAttribute("error", "Error al realizar el autologin");
        }
        return redirectView;
    }
}
