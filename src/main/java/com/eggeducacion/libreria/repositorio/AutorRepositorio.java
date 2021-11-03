package com.eggeducacion.libreria.repositorio;

import com.eggeducacion.libreria.entidad.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {
        
//    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
//    List<Autor> buscarAutoresPorNombre(@Param("nombre") String nombre);
    
    @Modifying
    @Query("UPDATE Autor a SET a.nombre = :nombre WHERE a.id = :id")
    void modificarAutor(@Param("id")String id, @Param("nombre") String nombre);
    
    @Modifying 
    @Query("UPDATE Autor a SET a.alta = :alta WHERE a.id = :id")
    void bajaAutor(@Param("id")String id, @Param("alta") boolean alta);
    
    @Modifying 
    @Query("UPDATE Autor a SET a.alta = :alta WHERE a.id = :id")
    void altaAutor(@Param("id") String id, @Param("alta") boolean alta);
}
