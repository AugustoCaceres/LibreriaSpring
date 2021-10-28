package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.excepcion.ExcepcionServicio;
import com.eggeducacion.libreria.repositorio.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio repositorio;
    
    @Transactional
    public void ingresarEditorial(String nombre) throws ExcepcionServicio{
        Editorial editorial = new Editorial();
        validarDatos(nombre);
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        repositorio.save(editorial);
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws ExcepcionServicio {
        validarDatos(nombre);
        Optional<Editorial> e = repositorio.findById(id);
        if (e.isPresent()){
            Editorial editorial = e.get();
            editorial.setNombre(nombre);
            repositorio.save(editorial);
        } else {
            throw new ExcepcionServicio("Esa editorial no se encuentra en la base de datos.");
        }
    }
    
    @Transactional
    public void bajaEditorial(String id) throws ExcepcionServicio {
        
        Optional<Editorial> e = repositorio.findById(id);
        if (e.isPresent()){
            Editorial editorial = e.get();
            editorial.setAlta(false);
            repositorio.save(editorial);
        } else {
            throw new ExcepcionServicio("Esa editorial no se encuentra en la base de datos.");
        }
    }
    
    @Transactional(readOnly=true)
    public List<Editorial> obtenerEditoriales(){
        return repositorio.findAll();
    }
    
    @Transactional(readOnly=true)
    public Editorial obtenerEditorialPorId(String id) throws ExcepcionServicio{
        Optional<Editorial> e = repositorio.findById(id);
        if (e.isPresent()){
            Editorial editorial = e.get();
            return editorial;
        } else {
            throw new ExcepcionServicio("Esa editorial no está en la base de datos.");
        }
    }
    
    private void validarDatos(String nombre) throws ExcepcionServicio {
        if (nombre == null || nombre.isEmpty()){
            throw new ExcepcionServicio("El nombre ingresado es inválido.");
        }
    }
}
