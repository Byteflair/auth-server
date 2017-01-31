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

    @RequestMapping(method = RequestMethod.POST, value = "/clients")
    public BaseClientDetails createNewClient(@RequestBody BaseClientDetails clientDetails) {
        return clientDetailsService.createClientDetails(clientDetails);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/clients/{id}")
    public BaseClientDetails getClientDetails(@PathVariable(value = "id") String id){
        return clientDetailsService.getClientDetails(id);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/clients")
    public List<BaseClientDetails> getAllClientDetails() {
        return clientDetailsService.getAllClientDetails();
    }

}
