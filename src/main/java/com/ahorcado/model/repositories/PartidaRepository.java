package com.ahorcado.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahorcado.model.entities.Partida;


@Repository
public interface PartidaRepository extends CrudRepository<Partida, Integer>{

}
