package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.entidad.Libro;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.repositorio.AutorRepositorio;
import com.eggeducacion.libreria.repositorio.EditorialRepositorio;
import com.eggeducacion.libreria.repositorio.LibroRepositorio;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio repositorio;
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void ingresarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String autorId, String editorialId) throws ExcepcionServicio{
        
        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados);
        
        //Autor autor = autorServicio.obtenerAutorPorId(autorId); --> esto no funcionaba 
        
        //Editorial editorial = editorialServicio.obtenerEditorialPorId(autorId); --> esto no funcionaba 
        
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(libro.getEjemplares()-libro.getEjemplaresPrestados());
        libro.setAutor(autorRepositorio.findById(autorId).orElse(null)); //--> con esto se arregló lo que no funcionaba 
        libro.setEditorial(editorialRepositorio.findById(editorialId).orElse(null)); //--> con esto se arregló lo que no funcionaba 
        libro.setAlta(true);
        repositorio.save(libro);      
    }
    
    @Transactional
    public void modificarLibro (String id,  Long isbn, String titulo,  Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            String autorId, String editorialId)throws ExcepcionServicio{
        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados);

        Optional<Libro> l = repositorio.findById(id);
        
        if(l.isPresent()){
            Libro libro = l.get();
            
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(libro.getEjemplares()-libro.getEjemplaresPrestados());
            libro.setAutor(autorRepositorio.findById(autorId).orElse(null));
            libro.setEditorial(editorialRepositorio.findById(editorialId).orElse(null));
            
            repositorio.save(libro);
        } else {
            throw new ExcepcionServicio("Ese libro no se encuentra en la base de datos.");
        }
    }
    
    @Transactional
    public void bajaLibro(String id) throws ExcepcionServicio{
        Optional<Libro> l = repositorio.findById(id);
        
        if(l.isPresent()){
            Libro libro = l.get();
            libro.setAlta(Boolean.FALSE);
            
            repositorio.save(libro);
        } else {
            throw new ExcepcionServicio("Ese libro no se encuentra en la base de datos.");
        }
    }
    
    @Transactional
    public void altaLibro(String id) throws ExcepcionServicio{
        Optional<Libro> l = repositorio.findById(id);
        
        if(l.isPresent()){
            Libro libro = l.get();
            libro.setAlta(Boolean.TRUE);
            
            repositorio.save(libro);
        } else {
            throw new ExcepcionServicio("Ese libro no se encuentra en la base de datos.");
        }
    }
    
    @Transactional(readOnly=true)
    public List<Libro> obtenerLibros(){
        return repositorio.findAll();
    }
    
    @Transactional(readOnly=true)
    public Libro obtenerLibroPorId(String id) throws ExcepcionServicio{
          return repositorio.findById(id).orElse(null);

//        Optional<Libro> l = repositorio.findById(id);
//        
//        if(l.isPresent()){
//            Libro libro = l.get();
//            return libro;
//        } else {
//            throw new ExcepcionServicio("Ese libro no se encuentra en la base de datos.");
//        }
    }
    
    public void validarDatos(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados) throws ExcepcionServicio {
        Calendar c = Calendar.getInstance();
        int anioActual = c.get(Calendar.YEAR);
        
        if (isbn == null){
            throw new ExcepcionServicio("Debe ingresar un isbn valido.");
        }
        
        if(titulo == null || titulo.isEmpty()){
            throw new ExcepcionServicio("Debe ingresar un titulo válido.");
        }
        
        if(anio > anioActual || anio == null){
            throw new ExcepcionServicio("Debe ingresar un año válido.");
        }
        
        if(ejemplares < 0){
            throw new ExcepcionServicio("Debe ingresar cantidad válida de ajemplares.");
        }
        
        if(ejemplaresPrestados > ejemplares){
            throw new ExcepcionServicio("La cantidad de ejemplares prestados no puede ser mayor que la ejemplares totales.");
        }
        
    }
}
