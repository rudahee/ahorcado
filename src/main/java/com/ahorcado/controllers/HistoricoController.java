package com.ahorcado.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahorcado.model.entities.Historico;
import com.ahorcado.service.HistoricService;

@RestController
@RequestMapping(path = "/historico")
public class HistoricoController {
	
	@Autowired
	HistoricService service;
	
	@GetMapping("")
	public ResponseEntity<?> getHistorico() {
		List<Historico> historico = service.getHistorico();
	
		return ResponseEntity.status(HttpStatus.OK).body(historico);
	}
}
