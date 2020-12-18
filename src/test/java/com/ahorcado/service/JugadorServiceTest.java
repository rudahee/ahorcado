package com.ahorcado.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.ahorcado.model.entities.Jugador;
import com.ahorcado.model.entities.Partida;
import com.ahorcado.model.repositories.JugadorRepository;

class JugadorServiceTest {

	private JugadorService sut;
	
	private JugadorRepository jugadorRepoMock;
	private Jugador jugadorMock;
	private Logger loggerMock;
	private PartidaService partidaServiceMock;
	
	@BeforeEach
	public void init() {
		sut = new JugadorService();
		
		jugadorRepoMock = Mockito.mock(JugadorRepository.class);
		jugadorMock = Mockito.mock(Jugador.class);
		loggerMock = Mockito.mock(Logger.class);
		partidaServiceMock = Mockito.mock(PartidaService.class);
		
		sut.setJugadorRepo(jugadorRepoMock);
		sut.setLogger(loggerMock);
		sut.setPartidaService(partidaServiceMock);

	}
	
	
	@Test
	public void registerTest() {
		Mockito.when(jugadorRepoMock.save(jugadorMock)).thenReturn(jugadorMock);
		assert(sut.register("sut1", "1234") != null);
		assert(sut.register("", "1234") == null);
		assert(sut.register("sut1", "") == null);
	}

	@Test
	public void loginTest() {
		Jugador jugadorPreLogin = new Jugador("sut1", "1234");
		
		jugadorPreLogin.setIp("10.0.0.1");

		Mockito.when(jugadorRepoMock.existsJugadorByUsername("sut1")).thenReturn(true);
		Mockito.when(jugadorRepoMock.findJugadorByUsername("sut1")).thenReturn(jugadorPreLogin);
		Mockito.when(jugadorRepoMock.save(jugadorMock)).thenReturn(jugadorMock);
		
		Jugador jugadorPostLogin = sut.login(
				jugadorPreLogin.getUsername(), 
				jugadorPreLogin.getPassword(), 
				jugadorPreLogin.getIp()
			);

		
		assert(jugadorPostLogin.getActive().equals(true));
		assert(jugadorPostLogin.getIp().equals(jugadorPreLogin.getIp()));
		assert(jugadorPostLogin.getUsername().equals(jugadorPreLogin.getUsername()));
		assert(jugadorPostLogin.getPassword().equals(jugadorPreLogin.getPassword()));
	}
	
	@Test
	public void startGameTest() {
		// No funciona!!
		
		Jugador jugador = new Jugador("sut1", "1234");
		Partida partida = new Partida();
		
		jugador.setId(1);
		jugador.setIp("10.0.0.1");
		jugador.setActive(true);
		partida.setId(1);

		Optional<Jugador> jugadorOpt = Optional.of(jugador);
		
		Mockito.when(jugadorRepoMock.findById(jugador.getId())).thenReturn(jugadorOpt);
		Mockito.when(partidaServiceMock.initialize(partida)).thenReturn(partida);
		System.out.println(sut.startGame(jugador.getId()).toString());
		
		
		//assert(sut.startGame(jugador.getId()).getPartida() != null);
	}
	
}
