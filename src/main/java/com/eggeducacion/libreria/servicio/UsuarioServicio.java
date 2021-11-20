package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Usuario;
import com.eggeducacion.libreria.excepciones.MiExcepcion;
import com.eggeducacion.libreria.repositorio.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    private final String MENSAJE = "El usuario ingresado no existe";
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Transactional
    public void crear(Usuario dto) throws MiExcepcion {
        if (usuarioRepositorio.existsUsuarioByCorreo(dto.getCorreo())){
            throw new MiExcepcion("Ya existe un usuario con ese correo");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuario.setAlta(true);
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void modificar(Usuario dto) throws MiExcepcion{
        Usuario usuario = usuarioRepositorio.findById(dto.getId()).orElseThrow(() -> new MiExcepcion(String.format(MENSAJE, dto.getId())));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> mostrarTodos(){
        return usuarioRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) throws MiExcepcion {
        return usuarioRepositorio.findById(id).orElseThrow(() -> new MiExcepcion(String.format(MENSAJE, id)));
    }
    
    @Transactional
    public void habilitar(Integer id){
        usuarioRepositorio.habilitar(id);
    }
    
    @Transactional
    public void eliminar(Integer id){
        usuarioRepositorio.deleteById(id);
    }
    
    @Override
    public UserDetails loadUserByUsername (String correo) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepositorio.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException(String.format(MENSAJE, correo)));
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.emptyList());
    }
}
