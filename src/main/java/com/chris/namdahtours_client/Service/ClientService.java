package com.chris.namdahtours_client.Service;

import com.chris.namdahtours_client.Model.Client;
import com.chris.namdahtours_client.Repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * client service will handle
     * CRUD operation on client object
     * and fetch client(s)
     */

    //CREATE
    public String addClient(Client client) throws Exception {
        Boolean emailExists = clientRepository.existsByEmail(client.getEmail());
        if(emailExists){
            log.error("email already exists");
            throw new Exception("email already exists");
        }else {
            clientRepository.save(client);
            log.info("Client details saved");
            return "Client details saved";
        }
    }


    //GET CLIENT
    public Optional<Client> getClient(int id){
        log.info("getting client with id: ?", id);
       return clientRepository.findById(id);
    }


    //GET CLIENTS
    public List<Client> getAllClients(){
        log.info("getting a list of clients");
        return clientRepository.findAll();
    }


    //UPDATE
    public String updateClient(int id, String phone_number, String email) throws Exception {
        //get the client
        Client expectedClient = clientRepository.findById(id)
                .orElseThrow(()-> new Exception("client not found"));


        if(phone_number != null){
            expectedClient.setPhone_number(phone_number);
        }
        if(email != null){
            Boolean emailExists = clientRepository.existsByEmail(email);
            if (emailExists){
                log.error("client with email already exists");
                throw new Exception("Email exists");
            }else {
                expectedClient.setEmail(email);
            }

        }

        clientRepository.save(expectedClient);
        log.info("Client details updated");
        return "Client details updated";
    }


    //DELETE
    public String deleteClient(int id) throws InstanceNotFoundException {
        Boolean isClientPresent = clientRepository.existsById(id);

        if(!isClientPresent){
            log.error("client with id ? not found", id);
            throw new InstanceNotFoundException("client doesnt exist");
        }else {
            clientRepository.deleteById(id);
        }

        log.info("Client deleted");
        return "Client deleted";
    }

}
