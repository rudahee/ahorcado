package com.ahorcado.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahorcado.model.entities.Historico;

@Repository
public interface HistoricoRepository extends CrudRepository<Historico, Integer> {


}
