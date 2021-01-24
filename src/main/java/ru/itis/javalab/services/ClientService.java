package ru.itis.javalab.services;

import ru.itis.javalab.models.Client;

import java.util.List;

public interface ClientService {
    void saveClient(Client client);
    List<Client> getAllClients();
    List<Client> getByData(String login);
    List<Client> getByUuid(String uuid);
    boolean containsUuid(String uuid);

    void deleteById(long userId);
}
