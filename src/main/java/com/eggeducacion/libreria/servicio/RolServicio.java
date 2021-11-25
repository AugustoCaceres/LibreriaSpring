package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Rol;
import com.eggeducacion.libreria.repositorio.RolRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicio {
    
    @Autowired
    private RolRepositorio rolRepositorio;
    
    @Transactional
    public void crear(String nombre) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rolRepositorio.save(rol);
    }
    
    @Transactional(readOnly = true)
    public List<Rol> buscarTodos() {
        return rolRepositorio.findAll();
    }
    
    @Transactional
    public void eliminar(Integer id){
        rolRepositorio.deleteById(id);
    }
    
}
