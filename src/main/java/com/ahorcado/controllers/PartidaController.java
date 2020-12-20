package com.ahorcado.controllers;

import java.net.UnknownHostException;

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

import com.ahorcado.model.entities.Partida;
import com.ahorcado.service.HistoricService;
import com.ahorcado.service.PartidaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(path = "/game")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;
	@Autowired
	private HistoricService historicoService;
	
	private ObjectMapper mapper = new ObjectMapper();	

	@PostMapping("/{pId}")
	public ResponseEntity<?> attemptLetter(@RequestBody String data, @PathVariable Integer pId) throws JsonProcessingException {
		HttpStatus status;
		Partida partida = null;
		JsonNode body = mapper.readTree(new String("{}"));

		try {
			partida = partidaService.attemptLetter(pId, data);
			status = HttpStatus.OK;
			
			if (partida.getRemainingAttempts() <= 0) {
				
				((ObjectNode) body).put("winner", false);
				((ObjectNode) body).put("deletedGame", true);
				((ObjectNode) body).put("word", partida.getSecretWord());
				partidaService.deleteGame(pId);
			}
			
		} catch (Exception e) {
			
			status = HttpStatus.CONFLICT;
			
		}
		
		((ObjectNode) body).put("Game:", mapper.writeValueAsString(partida));

		historicoService.addEntryLetter(partida, data);
		
		return ResponseEntity.status(status).body(body);
	}
	
	@PutMapping("/{pId}")
	public ResponseEntity<?> attemptWord(@RequestBody String data, @PathVariable Integer pId) throws JsonMappingException, JsonProcessingException, UnknownHostException {		
		HttpStatus status;
		Partida partida = null;
		String secretWord = partidaService.getStatus(pId).getSecretWord();
		JsonNode body = mapper.readTree(new String("{}"));
		
		data = data.toUpperCase();
		
		partida = partidaService.attemptWord(pId, data);

		if (partida == null) {
			
			((ObjectNode) body).put("winner", true);
			((ObjectNode) body).put("deletedGame", true);
			((ObjectNode) body).put("word", secretWord);
			status = HttpStatus.OK;
			
		} else if (partida.getRemainingAttempts() <= 0) {
			
			((ObjectNode) body).put("winner", false);
			((ObjectNode) body).put("deletedGame", true);
			((ObjectNode) body).put("word", partida.getSecretWord());
			partidaService.deleteGame(pId);
			status = HttpStatus.OK;
			
		} else {
			
			((ObjectNode) body).put("winner", false);
			status = HttpStatus.CONFLICT;			
		}
		
		((ObjectNode) body).put("Game:", mapper.writeValueAsString(partida));
		
		historicoService.addEntryWord(partida, data);
		
		return ResponseEntity.status(status).body(body);
	}
	
	@GetMapping("/{pId}")
	public ResponseEntity<?> getStatus(@PathVariable Integer pId) {
		HttpStatus status;
		Partida partida = null;
		try {
			
			partida = partidaService.getStatus(pId);
			status = HttpStatus.OK;
			
		} catch (Exception e) {
			
			status = HttpStatus.CONFLICT;
			
		}
		
		return ResponseEntity.status(status).body(partida);
	}
}
