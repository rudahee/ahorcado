package com.ahorcado.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahorcado.model.entities.Jugador;
import com.ahorcado.model.entities.Partida;
import com.ahorcado.model.repositories.JugadorRepository;

@Service
public class JugadorService {

	@Autowired
	private JugadorRepository jugadorRepo;
	
	@Autowired
	private PartidaService partidaService;
	
	@Value("${server.address}") 
	String servidorIP;
	
	protected Logger logger;
	
	/**
	 * 
	 * Logger para avisar de las peticiones de login y registro
	 * 
	 */
	@PostConstruct
	private void initLogger() {
		logger = LoggerFactory.getLogger(getClass());
	}
	
	/**
	 * 
	 * Permite registrar a un usuario en el sistema. 
	 * 
	 * Si el jugador existe devuelve null, sino, devuelve el jugador.
	 * 
	 * @param String username
	 * @param String password
	 * 
	 * @return Jugador
	 */	
	public Jugador register(String username, String password) {
		Jugador jugador = null;
		
		if (jugadorRepo.existsJugadorByUsername(username)) {
			return jugador;
		} else {
			jugador = new Jugador(username, password);
			jugador.setActive(false);
			jugadorRepo.save(jugador);
			logger.info("REGISTER: User: "+ username + " | Password: "+ password);
			return jugador;			
		}
	}
	
	/**
	 * 
	 * Permite hacer login a un usuario en el sistema, permitiendo asignarle una IP y el estado activo
	 * 
	 * Si el jugador existe y el login es correcto devuelve el Jugador, sino, devuelve null.
	 * 
	 * @param String username
	 * @param String password
	 * @param String ip
	 * 
	 * @return Jugador
	 */	
	
	public Jugador login(String username, String password, String ip) {
		Jugador jugador = null;
		if (jugadorRepo.existsJugadorByUsername(username)) {
			jugador = jugadorRepo.findJugadorByUsername(username);
			jugador.setActive(true);
			jugador.setIp(ip);
			logger.info("LOGIN EXITOSO: User: "+ username + " | Password: "+ password);
			jugadorRepo.save(jugador);
			if (!jugador.getPassword().equals(password)) {
				jugador = null;
			}

		}
		return jugador;
	}

	/**
	 * 
	 * Permite empezar una partida. 
	 * 
	 * Si existe una partida la borra, y despues crea una y se la asigna al jugador.
	 * 
	 * @param Integer id
	 * 
	 * 
	 * @return Jugador
	 */	
	public Jugador startGame(Integer id) {		
		Jugador jugador = jugadorRepo.findById(id).get();
		if (jugador.getActive() && jugador.getIp() != servidorIP) {
			if (jugador.getPartida() != null && jugador.getActive() && jugador.getIp() != servidorIP) {
				partidaService.deleteGame(jugador.getPartida().getId());
				
			}				
			Partida partida = new Partida();
			partida = partidaService.initialize(partida);
			
			jugador.setPartida(partida);

		} else {
			logger.warn("INTENTO DE EMPEZAR UN JUEGO NO AUTORIZADO: "+ jugador);
		}
		
		jugadorRepo.save(jugador);
		
		return jugador;
	}
}
