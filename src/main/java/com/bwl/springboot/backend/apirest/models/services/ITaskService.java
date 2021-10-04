package com.bwl.springboot.backend.apirest.models.services;

import com.bwl.springboot.backend.apirest.models.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITaskService {

	public List<Task> findAll();
	
	public Page<Task> findAll(Pageable pageable);

	public List<Task> findByEstatus(Boolean estatus);

	public Task findById(Long id);

	public Task save(Task task);
	
	public void delete(Long id);
}
