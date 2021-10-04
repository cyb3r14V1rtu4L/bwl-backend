package com.bwl.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwl.springboot.backend.apirest.models.entity.Task;
import com.bwl.springboot.backend.apirest.models.services.ITaskService;
import com.bwl.springboot.backend.apirest.models.services.IUploadFileService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class TaskRestController {

	@Autowired
	private ITaskService taskService;

	@Autowired
	private IUploadFileService uploadService;

	// private final Logger log = LoggerFactory.getLogger(TaskRestController.class);

	@GetMapping("/tasks")
	public List<Task> index() {
		return taskService.findAll();
	}

	@GetMapping("/tasks/status/{estatus}")
	public List<Task> estatus(@PathVariable Boolean estatus) {
		return taskService.findByEstatus(estatus);
	}

	@GetMapping("/tasks/page/{page}")
	public Page<Task> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return taskService.findAll(pageable);
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/tasks/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Task task = null;
		Map<String, Object> response = new HashMap<>();

		try {
			task = taskService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(task == null) {
			response.put("mensaje", "El task ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/tasks")
	public ResponseEntity<?> create(@Valid @RequestBody Task task, BindingResult result) {

		Task taskNew = null;
		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			taskNew = taskService.save(task);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El task ha sido creado con éxito!");
		response.put("task", taskNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/tasks/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Task task, BindingResult result, @PathVariable Long id) {

		Task taskActual = taskService.findById(id);

		Task taskUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (taskActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el task ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		response.put("mensaje", "El task ha sido actualizado con éxito!");
		response.put("task", taskUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			Task task = taskService.findById(id);
			taskService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el task de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El task eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	
}
