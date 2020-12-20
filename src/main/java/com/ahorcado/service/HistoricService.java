package com.ahorcado.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahorcado.enums.Evento;
import com.ahorcado.model.entities.Historico;
import com.ahorcado.model.entities.Jugador;
import com.ahorcado.model.entities.Partida;
import com.ahorcado.model.repositories.HistoricoRepository;

@Service
public class HistoricService {

	@Autowired
	private HistoricoRepository repository;
	
	public void addEntryLogin(Jugador jugador) {
		Historico historico = new Historico();
		historico.setEvento(Evento.LOGIN);
		historico.setHistorico("User: " + jugador.getUsername() + " IP:" + jugador.getIp());
		historico.setTime(LocalDateTime.now());
		
		repository.save(historico);
	}
	public void addEntryRegister(Jugador jugador) {
		Historico historico = new Historico();
		historico.setEvento(Evento.REGISTRO);
		historico.setHistorico("User: " + jugador.getUsername());
		historico.setTime(LocalDateTime.now());
		
		repository.save(historico);
	}
	
	public void addEntryStartGame(Jugador jugador) {
		Historico historico = new Historico();
		historico.setEvento(Evento.EMPEZAR);
		historico.setHistorico("User: " + jugador.getUsername() + " Partida ID: " + jugador.getPartida().getId());
		historico.setTime(LocalDateTime.now());

		repository.save(historico);
	}
	
	public void addEntryLetter(Partida partida, String letra) {
		Historico historico = new Historico();
		historico.setEvento(Evento.LETRA);
		historico.setHistorico("User: " + partida.getJugadores().get(0).getUsername() + " Partida ID: " + partida.getId() +
				" Letra: " + letra + " Intentos: " + partida.getRemainingAttempts());
		historico.setTime(LocalDateTime.now());

		repository.save(historico);
	}
	
	public void addEntryWord(Partida partida, String word) {
		Historico historico = new Historico();
		historico.setEvento(Evento.PALABRA);
		historico.setHistorico("User: " + partida.getJugadores().get(0).getUsername() + " Partida ID: " + partida.getId() +
				" Letra: " + word + " Palabra secreta:" + partida.getSecretWord() + " Intentos: " + partida.getRemainingAttempts());
		historico.setTime(LocalDateTime.now());

		repository.save(historico);
	}
	
	public List<Historico> getHistorico() {
		return (List<Historico>) repository.findAll();
	}
	
	
}
