package com.eggeducacion.libreria.repositorio;

import com.eggeducacion.libreria.entidad.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsUsuarioByCorreo(String correo);
    
    @Modifying
    @Query("UPDATE Usuario u SET u.alta = true WHERE u.id = :id")
    void habilitar(@Param ("id") Integer id);
    
}
