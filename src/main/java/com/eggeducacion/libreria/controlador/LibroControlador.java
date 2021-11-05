package com.eggeducacion.libreria.controlador;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.entidad.Libro;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.servicio.AutorServicio;
import com.eggeducacion.libreria.servicio.EditorialServicio;
import com.eggeducacion.libreria.servicio.LibroServicio;
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
@RequestMapping("/libros")
public class LibroControlador {
    
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping
    public ModelAndView mostrarLibros(){
        ModelAndView mav = new ModelAndView("libros");
        mav.addObject("libros", libroServicio.obtenerLibros());
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearLibro(){
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", new Libro());
        mav.addObject("autores", autorServicio.obtenerAutores()); //esto tiene que coincidir con lo que va en las llaves del th:each 
        mav.addObject("editoriales", editorialServicio.obtenerEditoriales()); //esto lo mismo pero con las editoriales
        mav.addObject("title", "Crear libro");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView modificarLibro(@PathVariable String id) throws ExcepcionServicio {
        ModelAndView mav = new ModelAndView("libro-formulario");
        mav.addObject("libro", libroServicio.obtenerLibroPorId(id));
        mav.addObject("autores", autorServicio.obtenerAutores());
        mav.addObject("editoriales", editorialServicio.obtenerEditoriales());
        mav.addObject("title", "Editar libro");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, 
            @RequestParam Integer ejemplaresPrestados, @RequestParam("autor") String autorId, 
            @RequestParam("editorial") String editorialId)throws ExcepcionServicio{
        libroServicio.ingresarLibro(isbn, titulo, anio, ejemplares, ejemplaresPrestados, autorId, editorialId);
        return new RedirectView("/libros");
    }
    
    @PostMapping("/modificar")
    public RedirectView editar(@RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, 
            @RequestParam Integer anio, @RequestParam Integer ejemplares,
            @RequestParam Integer ejemplaresPrestados, @RequestParam ("autor") String autorId, @RequestParam ("editorial") String editorialId// 
    ) 
            throws ExcepcionServicio {
        libroServicio.modificarLibro(id, isbn, titulo,  anio, ejemplares, ejemplaresPrestados, autorId, editorialId
        );
        return new RedirectView("/libros");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) throws ExcepcionServicio {
        libroServicio.bajaLibro(id);
        return new RedirectView("/libros");
    }
    
    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) throws ExcepcionServicio {
        libroServicio.altaLibro(id);
        return new RedirectView("/libros");
    }
    
}
