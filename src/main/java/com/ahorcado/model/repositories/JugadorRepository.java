package com.ahorcado.model.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahorcado.model.entities.Jugador;

@Repository
public interface JugadorRepository extends CrudRepository<Jugador, Integer>{

	public boolean existsJugadorByUsername(String username);
	public Jugador findJugadorByUsername(String username);

}
