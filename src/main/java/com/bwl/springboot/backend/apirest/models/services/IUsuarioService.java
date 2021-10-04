package com.bwl.springboot.backend.apirest.models.services;

import com.bwl.springboot.backend.apirest.models.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IUsuarioService {

	Usuario findByUsername(String username);
	Page<Usuario> findAll(Pageable pageable);

	List<Usuario> findAll();

	Usuario save(Usuario usuario);
}
