package com.eggeducacion.libreria.servicio;

import com.eggeducacion.libreria.entidad.Rol;
import com.eggeducacion.libreria.entidad.Usuario;
import com.eggeducacion.libreria.excepciones.MiExcepcion;
import com.eggeducacion.libreria.repositorio.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    private final String MENSAJE = "El usuario ingresado no existe";
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
//    @Transactional
//    public void crear(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, @RequestParam String clave, @RequestParam Rol rol) throws MiExcepcion {
//        try {
//            if (usuarioRepositorio.existsUsuarioByCorreo(correo)){
//            throw new MiExcepcion("Ya existe un usuario con ese correo");
//        }
//        Usuario usuario = new Usuario();
//        usuario.setNombre(nombre);
//        usuario.setApellido(apellido);
//        usuario.setCorreo(correo);
//        usuario.setClave(encoder.encode(clave));
//        usuario.setRol(rol);
//        usuario.setAlta(true);
//        usuarioRepositorio.save(usuario);
//        } catch (MiExcepcion e) {
//            throw e;
//        }
//    }
    
    @Transactional
    public void crear(Usuario dto) throws MiExcepcion {
        try {
            if (usuarioRepositorio.existsUsuarioByCorreo(dto.getCorreo())){
            throw new MiExcepcion("Ya existe un usuario con ese correo");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuario.setRol(dto.getRol());
        usuario.setAlta(true);
        usuarioRepositorio.save(usuario);
        } catch (MiExcepcion e) {
            throw e;
        }
    }
    
    @Transactional
    public void modificar(Usuario dto) throws MiExcepcion{
        Usuario usuario = usuarioRepositorio.findById(dto.getId()).orElseThrow(() -> new MiExcepcion(String.format(MENSAJE, dto.getId())));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuario.setRol(dto.getRol());
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
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());
        
        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }
}
