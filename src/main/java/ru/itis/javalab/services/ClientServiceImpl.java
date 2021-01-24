package ru.itis.javalab.services;

import ru.itis.javalab.models.Client;
import ru.itis.javalab.repositories.ClientsRepository;

import java.util.List;

public class ClientServiceImpl implements ClientService {

    private ClientsRepository clientsRepository;

    public ClientServiceImpl(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @Override
    public void saveClient(Client client) {
        clientsRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    @Override
    public List<Client> getByData(String login) {
        return clientsRepository.findByData(login);
    }

    @Override
    public List<Client> getByUuid(String uuid) {
        return clientsRepository.findByUuid(uuid);
    }

    @Override
    public boolean containsUuid(String uuid) {
        return clientsRepository.findByUuid(uuid) != null;
    }

    @Override
    public void deleteById(long userId) {
        clientsRepository.findById(userId)
                .ifPresent(
                        user -> {
                            user.setIsDeleted(true);
                            clientsRepository.update(user);
                        }
                );
    }
}
