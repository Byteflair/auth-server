/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
