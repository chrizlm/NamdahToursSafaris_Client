package com.chris.namdahtours_client.Service;

import com.chris.namdahtours_client.Model.Client;
import com.chris.namdahtours_client.Repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;

    private ClientService clientServiceUnderTest;

    @BeforeEach
    void setUp() {
        clientServiceUnderTest = new ClientService(clientRepository);
    }

    @Test
    void addClient() throws Exception {
        //given
        String email = "chris@gmail.com";
        Client client = new Client(
                1,
                "chris",
                "0712515497",
                email,
                1234567
        );

        given(clientRepository.existsByEmail(email)).willReturn(false);

        //when
        clientServiceUnderTest.addClient(client);

        //then
        ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(argumentCaptor.capture());

        Client capturedClient = argumentCaptor.getValue();

        assertThat(capturedClient).isEqualTo(client);
    }

    @Test
    void cannotAddClientExistingEmail() throws Exception {
        //given
        String email = "chris@gmail.com";
        Client client = new Client(
                1,
                "chris",
                "0712515497",
                email,
                1234567
        );

        given(clientRepository.existsByEmail(email)).willReturn(true);

        //when
        //then

        assertThatThrownBy(() -> clientServiceUnderTest.addClient(client))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("email already exists");

        verify(clientRepository, never()).save(client);

    }

    @Test
    void getClient() {
        //given
        int id = 1;
        Client client = new Client(
                id,
                "chris",
                "071251497",
                "chris@gmail.com",
                1234567
        );

        given(clientRepository.findById(id)).willReturn(Optional.of(client));

        //when
        Optional<Client> expectedClient = clientServiceUnderTest.getClient(id);

        //then
        assertThat(expectedClient).isEqualTo(Optional.of(client));
    }

    @Test
    void getAllClients() {
        //given
        //when
        clientServiceUnderTest.getAllClients();
        //then
        verify(clientRepository).findAll();
    }

    @Test
    void updateClient() throws Exception {
        //given
        String phone_number = "0712515497";
        String phone_number_new = "0101515497";
        String email = "chris@gmail.com";
        String email_new = "lm@gmail.com";
        int id = 1;

        Client client = new Client(
                id,
                "chris",
                phone_number,
                email,
                1234567
        );

        given(clientRepository.findById(id)).willReturn(Optional.of(client));

        //when
        clientServiceUnderTest.updateClient(id, phone_number_new, email_new);

        //then
        Optional<Client> expectedClient = clientRepository.findById(id);

        assertThat(phone_number_new).isEqualTo(expectedClient.get().getPhone_number());
        assertThat(email_new).isEqualTo(expectedClient.get().getEmail());
        assertThat(client).isNotEqualTo(expectedClient);
    }



    @Test
    void willThrowExceptionWhenUpdating() throws Exception {
        //given
        String phone_number = "0712515497";
        String phone_number_new = "0101515497";
        String email = "chris@gmail.com";
        String email_new = "lm@gmail.com";
        int id = 1;

        Client client = new Client(
                id,
                "chris",
                phone_number,
                email,
                1234567
        );

        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        given(clientRepository.existsByEmail(email_new)).willReturn(true);

        //when
        //then

        assertThatThrownBy(() ->clientServiceUnderTest.updateClient(id, phone_number_new, email_new))
        .isInstanceOf(Exception.class).hasMessageContaining("Email exists");
    }

    @Test
    void deleteClient() throws InstanceNotFoundException {
        //given
        int id = 1;
        given(clientRepository.existsById(id)).willReturn(true);

        //when
        clientServiceUnderTest.deleteClient(id);
        //then
        verify(clientRepository).deleteById(id);
    }

    @Test
    void willThrowExceptionWhenDeletingClient() throws InstanceNotFoundException {
        //given
        int id = 1;
        given(clientRepository.existsById(id)).willReturn(false);

        //when
        //then

        assertThatThrownBy(()->clientServiceUnderTest.deleteClient(id))
        .isInstanceOf(InstanceNotFoundException.class).hasMessageContaining("client doesnt exist");
    }
}