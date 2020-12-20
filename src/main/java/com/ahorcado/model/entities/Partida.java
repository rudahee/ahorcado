package com.ahorcado.model.entities;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class Partida implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	@JsonIgnore
	private String secretWord; 
	
    private char[] publicWord;
    
	private Integer remainingAttempts;
	
	private String attemptedLetters;
	
    @OneToMany(mappedBy="partida")
    @JsonIgnore
	private List<Jugador> jugadores;

	public Partida() {
		super();
		this.remainingAttempts = 8;
		this.secretWord = "springtoolsuite"; 
		this.attemptedLetters = "";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSecretWord() {
		return secretWord;
	}
	public void setWordToWin(String secretWord) {
		this.secretWord = secretWord;
	}
	public char[] getPublicWord() {
		return publicWord;
	}
	public void setPublicWord(char[] publicWord) {
		this.publicWord = publicWord;
	}
	public Integer getRemainingAttempts() {
		return remainingAttempts;
	}
	public void setRemainingAttempts(Integer attempts) {
		this.remainingAttempts = attempts;
	}
	public String getAttemptedLetters() {
		return attemptedLetters;
	}
	public void setAttemptedLetters(String attemptedLetters) {
		this.attemptedLetters = attemptedLetters;
	}
	public List<Jugador> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}
	public void addJugador(Jugador jugador) {
		this.jugadores.add(jugador);
	}

}
