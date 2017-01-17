package com.byteflair.oauth.server.controllers;

import com.byteflair.oauth.server.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by calata on 10/11/16.
 */
@RestController
public class ClientController {

    @Autowired
    private CustomClientDetailsService clientDetailsService;

    @RequestMapping(method = RequestMethod.POST, value = "/client")
    public BaseClientDetails createNewClient(@RequestBody BaseClientDetails clientDetails) {

        BaseClientDetails createdClientDetail = clientDetailsService.createClientDetails(clientDetails);

        return createdClientDetail;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/client/{id}")
    public BaseClientDetails getClientDetails(@PathVariable(value = "id") String id){

        BaseClientDetails clientDetails = clientDetailsService.getClientDetails(id);

        return clientDetails;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/client")
    public List<BaseClientDetails> getAllClientDetails() {
        List<BaseClientDetails> baseClientDetails = clientDetailsService.getAllClientDetails();

        return baseClientDetails;
    }

    /*@RequestMapping(method = RequestMethod.DELETE, value = "/client/{id}")
    public void deleteClient(@PathVariable(value = "id") String id) {

        jdbcClientDetailsService.removeClientDetails(id);
    }*/
}
