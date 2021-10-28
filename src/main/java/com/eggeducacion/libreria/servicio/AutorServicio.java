package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.repositorio.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio repositorio;
    
    @Transactional
    public void cargarAutor(String nombre) throws ExcepcionServicio {
        validarDatos(nombre);
        
        Autor autor = new Autor();
        
        autor.setNombre(nombre);
        autor.setAlta(true);
        repositorio.save(autor);
    }
    
    @Transactional
    public void modificarAutor(String id, String nombre) throws ExcepcionServicio{
        validarDatos(nombre);
        
        Optional<Autor> a = repositorio.findById(id);
        if (a.isPresent()){
            Autor autor = a.get();
            autor.setNombre(nombre);
            repositorio.save(autor);
        } else {
            throw new ExcepcionServicio("No se encuentra ese autor en la base de datos");
        } 
    }
            
    
    @Transactional
    public void bajaAutor(String id) throws ExcepcionServicio {
        Optional<Autor> a = repositorio.findById(id);
        if (a.isPresent()){
            Autor autor = a.get();
            if(autor.getAlta().equals(false)){
                throw new ExcepcionServicio("Ese autor ya estaba dado de baja.");
            }
            autor.setAlta(false);
            repositorio.save(autor);
        } else {
            throw new ExcepcionServicio("Ese autor no se encuentra en la base de datos.");
        }
    }
    
    @Transactional(readOnly=true)
    public List<Autor> obtenerAutores(){
        return repositorio.findAll();
    }
    
    @Transactional(readOnly=true)
    public Autor obtenerAutorPorId(String id) throws ExcepcionServicio{
        Optional<Autor> a = repositorio.findById(id);
        if(a.isPresent()){
            Autor autor = a.get();
            return autor;
        } else {
            throw new ExcepcionServicio("Ese autor no se encuentra en la base de datos.");
        }
    }
    
    private void validarDatos(String nombre) throws ExcepcionServicio {
        if (nombre == null || nombre.isEmpty()){
            throw new ExcepcionServicio("Debe ingresar un nombre válido.");
        }
    }
}
