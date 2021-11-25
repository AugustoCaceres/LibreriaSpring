package com.eggeducacion.libreria.repositorio;

import com.eggeducacion.libreria.entidad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {
    
    
    
}
