package com.eggeducacion.libreria.repositorio;

import com.eggeducacion.libreria.entidad.Autor;
import com.eggeducacion.libreria.entidad.Editorial;
import com.eggeducacion.libreria.entidad.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String>{

    @Modifying
    @Query("UPDATE Libro l SET l.isbn = :isbn, l.titulo = :titulo, l.anio = :anio, l.ejemplares = :ejemplares, l.ejemplaresPrestados = :ejemplaresPrestados,"
            + "l.autor = :autor, l.editorial = :editorial WHERE l.id = :id")
    void modificarLibro(@Param("id") String id, @Param("isbn") Long isbn, @Param("titulo") String titulo, @Param("anio") Integer anio, 
            @Param("ejemplares") Integer ejemplares, @Param("ejemplaresPrestados") Integer ejemplaresPrestados, @Param("autor") Autor autor,
                    @Param("editorial") Editorial editorial);
    
    @Modifying 
    @Query("UPDATE Libro l SET l.alta = :alta WHERE l.id = :id")
    void bajaLibro(@Param("id") String id, @Param("alta") boolean alta);
    
    @Modifying 
    @Query("UPDATE Libro l SET l.alta = :alta WHERE l.id = :id")
    void altaLibro(@Param("id") String id, @Param("alta") boolean alta);
}
