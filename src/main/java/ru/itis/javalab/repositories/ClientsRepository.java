package ru.itis.javalab.repositories;

import ru.itis.javalab.models.Client;

import java.util.List;

public interface ClientsRepository extends CrudRepository<Client>{
    List<Client> findByUuid(String uuid);
    List<Client> findByData(String login);
}
