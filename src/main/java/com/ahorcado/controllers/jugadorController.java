package com.ahorcado.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahorcado.model.entities.Jugador;
import com.ahorcado.service.JugadorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(path = "/user")
public class jugadorController {

	private ObjectMapper mapper = new ObjectMapper();	

	@Autowired
	private JugadorService jugadorService;
	
	@GetMapping("")
	public ResponseEntity<?> login(@RequestBody Jugador data, HttpServletRequest request) throws JsonProcessingException {
		Jugador jugador = new Jugador();
		JsonNode body = mapper.readTree(new String("{}"));
		HttpStatus status;
		
		try {
			jugador = jugadorService.login(data.getUsername(), data.getPassword(), request.getRemoteAddr());
			status = HttpStatus.OK;
			
			if (jugador == null) {
				((ObjectNode) body).put("logged", false);
			} else {
				((ObjectNode) body).put("logged", true);
			}
			
		} catch (Exception e) {
		
			status = HttpStatus.CONFLICT;
			((ObjectNode) body).put("logged", false);
		
		}
		
		((ObjectNode) body).put("Player", mapper.writeValueAsString(jugador));
		
		return ResponseEntity.status(status).body(body);
	}
	
	@PostMapping("")
	public ResponseEntity<?> register(@RequestBody Jugador data) throws JsonProcessingException {
		HttpStatus status;
		JsonNode body = mapper.readTree(new String("{}"));
		Jugador jugador = null;
		try {
			jugador = jugadorService.register(data.getUsername(), data.getPassword());
			status = HttpStatus.OK;
		
			if (jugador == null) {
				((ObjectNode) body).put("registered", false);
			} else {
				jugador.setActive(false);
				((ObjectNode) body).put("registered", true);
			}
			
		} catch (Exception e) {
			status = HttpStatus.CONFLICT;
			((ObjectNode) body).put("registered", false);
		}
		
		((ObjectNode) body).put("Player", mapper.writeValueAsString(jugador));

		return ResponseEntity.status(status).body(body);
	}

	@PutMapping("/{jId}")
	public ResponseEntity<?> start(@PathVariable Integer jId) throws JsonProcessingException {
		Jugador jugador = new Jugador();
		JsonNode body = mapper.readTree(new String("{}"));
		HttpStatus status;
		
		try {
			jugador = jugadorService.startGame(jId);
			status = HttpStatus.OK;
		
			if (jugador == null || !jugador.getActive()) {
				((ObjectNode) body).put("started", false);
			} else {
				((ObjectNode) body).put("started", true);
			}
			
		} catch (Exception e) {
			status = HttpStatus.CONFLICT;
			((ObjectNode) body).put("started", false);
		}
		
		((ObjectNode) body).put("Game:", mapper.writeValueAsString(jugador.getPartida()));
		
		return ResponseEntity.status(status).body(body);
	}
	
}
