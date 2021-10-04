package com.bwl.springboot.backend.apirest.models.services;

import com.bwl.springboot.backend.apirest.models.dao.ITaskDao;
import com.bwl.springboot.backend.apirest.models.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	private ITaskDao taskDao;

	@Override
	@Transactional(readOnly = true)
	public List<Task> findAll() {
		return (List<Task>) taskDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Task> findAll(Pageable pageable) {
		return taskDao.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Task findById(Long id) {
		return taskDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Task save(Task task) {
		return taskDao.save(task);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		taskDao.deleteById(id);
	}

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByEstatus(Boolean estatus) {
        return taskDao.findByEstatus(estatus);
    }

}
