package com.bwl.springboot.backend.apirest.models.dao;

import com.bwl.springboot.backend.apirest.models.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITaskDao extends JpaRepository<Task, Long>{

    List <Task>findByEstatus(Boolean estatus);
}
