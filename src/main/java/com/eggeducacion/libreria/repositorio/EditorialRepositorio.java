package com.eggeducacion.libreria.repositorio;

import com.eggeducacion.libreria.entidad.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{

    @Modifying
    @Query("UPDATE Editorial e SET e.nombre = :nombre WHERE e.id = :id")
    void modificarEditorial(@Param("id") String id, @Param("nombre") String nombre);

    @Modifying 
    @Query("UPDATE Editorial e SET e.alta = :alta WHERE e.id = :id")
    void bajaEditorial(@Param("id") String id, @Param("alta")boolean alta);

    @Modifying 
    @Query("UPDATE Editorial e SET e.alta = :alta WHERE e.id = :id")
    void altaEditorial(@Param("id") String id, @Param("alta") boolean alta);
}
