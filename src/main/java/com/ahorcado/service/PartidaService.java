package com.ahorcado.service;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ahorcado.model.entities.Partida;
import com.ahorcado.model.repositories.JugadorRepository;
import com.ahorcado.model.repositories.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository partidaRepo;
	@Autowired
	private JugadorRepository jugadorRepo;
	
	@Value("${server.address}") 
	String servidorIP;
	
    private int lengthWord;
    
	/**
	 * 
	 * Permite inicializar una partida con los datos basicos. 
	 * 
	 * Obtiene una palabra de una API REST (Por ello no hay otra forma de introducir palabras, 
	 * como se pide en un requisito basico, ya que esto es un requisito avanzado para lo mismo)
	 * 
	 * Si llamas a este metodo cuando existe una partida, esa partida se sustituira por una nueva.
	 * 
	 * @param Partida p
	 * 
	 * 
	 * @return Partida
	 */	
    public Partida initialize(Partida p) {
    	// Obtener palabra.
    	RestTemplate restWord = new RestTemplate();
    	String word = restWord.getForEntity("http://random-word-api.herokuapp.com/word", String.class).getBody().toUpperCase();
  
    	// le asignamos la palabra secreta a la instancia de Palabra
    	p.setWordToWin(word.substring(2, word.length()-2));
    	lengthWord = p.getSecretWord().length();

    	char[] publicWord = new char[lengthWord];
        
    	for (int i = 0; i < publicWord.length ; i++) {
    		// 0x002E = '-'
    		publicWord[i] = '-';
    	}

    	p.setPublicWord(publicWord);
    	return p;
    }
    
	/**
	 * 
	 * Permite hacer el intento de una letra. 
	 * 
	 * Comprueba que estes loggeado, que sea una ip valida (distinta de la del servidor) y que sea una letra.
	 * 
	 * @param Integer pId
	 * @param String letter
	 * 
	 * 
	 * @return Partida
	 */
    public Partida attemptLetter(Integer pId, String letter) throws UnknownHostException {
    	letter = letter.toUpperCase();
    	Partida partida = partidaRepo.findById(pId).get();    	
    	char[] publicWord = partida.getPublicWord();
    	Integer pos = 0;
    	Boolean acierto = false;

    	partida.setAttemptedLetters(partida.getAttemptedLetters() + letter);

    	//Si no se cumplen todas las condiciones devolvera null, sino, devuelve la partida.
    	if (this.isLogged(pId) && this.isValidIp(pId) && letter.length() == 1) {
	    	
    		//Recorre la palabra y sustituye las letras acertadas, si existen.
	    	while (pos != -1) {
	    		pos = partida.getSecretWord().indexOf(letter, pos);
	    		
	    		if (pos != -1) {
	    			publicWord[pos] = letter.charAt(0);
	    			pos++;
	    			acierto = true;
	    		}
	    	}
	    	
	    	//Si no existen aciertos, resta un intento.
	    	if (!acierto) {
	    		partida.setRemainingAttempts(partida.getRemainingAttempts() - 1);
	    	}
	    	
	    	partida.setPublicWord(publicWord);
	    	partidaRepo.save(partida);
	    	
	    	return partida;
    	} else {
    		return null;
    	}
    }

    /**
	 * 
	 * Permite hacer el intento de una palabra. 
	 * 
	 * Comprueba que estes loggeado, que sea una ip valida (distinta de la del servidor).
	 * 
	 * @param Integer pId
	 * @param String word
	 * 
	 * 
	 * @return Partida
	 */
	public Partida attemptWord(Integer pId, String word) throws UnknownHostException {
    	Partida p = partidaRepo.findById(pId).get();
    	
    	//Si no se cumplen todas las condiciones devolvera null, sino, devuelve la partida.
    	if (this.isLogged(pId) && this.isValidIp(pId)) {
	    	
    		// si aciertas la palabra, devuelve null
    		if (word.equals(p.getSecretWord())) {
	    		jugadorRepo.findById(p.getJugadores().get(0).getId()).get().setPartida(null);
	    		partidaRepo.deleteById(pId);
	    		
	    		p = null;
  
    		//Si no la aciertas, te resta un intento
	    	}	else {
	    		p.setRemainingAttempts(p.getRemainingAttempts() - 1);
	    		partidaRepo.save(p);
	    	}
	    		return p;
    	} else {
    		return null;
    	}
	}

	/**
	 * 
	 * Ver el estado de la partida. 
	 * 
	 * Comprueba que estes loggeado, se puede ver el estado desde el propio servidor.
	 * 
	 * @param Integer pId
	 * 
	 * 
	 * @return Partida
	 */
	public Partida getStatus(Integer pId) {
		//Si no existe devuelve null.
		if (partidaRepo.existsById(pId)) {
			if (partidaRepo.findById(pId).get().getJugadores().get(0).getActive())
				return partidaRepo.findById(pId).get();
			else
				return null;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * Comprueba que estes loggeado.
	 * 
	 * 
	 * @param Integer pId
	 * 
	 * 
	 * @return Boolean
	 */
    private Boolean isLogged(Integer pId) {
		Partida p = partidaRepo.findById(pId).get();
		
    	return p.getJugadores().get(0).getActive();
	}

    /**
	 * 
	 * Comprueba que desde que ip haces una peticion.
	 * 
	 * Para realizar esta peticion, obtengo la ip desde la que el jugador se ha loggeado
	 * y la ip del servidor, desde el server.address haciendo un @Value(${server.address});
	 * 
	 * @param Integer pId
	 * 
	 * 
	 * @return Boolean
	 */
	private boolean isValidIp(Integer pId) throws UnknownHostException {
		String jugadorIP = partidaRepo.findById(pId).get().getJugadores().get(0).getIp();
				
		if (!jugadorIP.equals(servidorIP))
			return false;
		else
			return true;
	}
	
	/**
	 * 
	 * Borra una partida.
	 * 
	 * 
	 * @param Integer pId
	 * 
	 */
    public void deleteGame(Integer pId) {
    	partidaRepo.delete(partidaRepo.findById(pId).get());
    }
}
