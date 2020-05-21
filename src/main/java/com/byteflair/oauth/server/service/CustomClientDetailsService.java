/*
 * Copyright (c) 2020 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.byteflair.oauth.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by calata on 17/01/17.
 */
@Service
public class CustomClientDetailsService {

    @Value("${api.client.access_token_validity}")
    private Integer defaultAccessTokenValidity;
    @Value("${api.client.refresh_token_validity}")
    private Integer defaultRefreshTokenValidity;

    @Autowired
    @Qualifier("jdbcClientDetailsService")
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public BaseClientDetails createClientDetails(BaseClientDetails clientDetails) {

        BaseClientDetails clientDet;
        Assert.notNull(clientDetails);
        Assert.hasText(clientDetails.getClientId(), "client_id must be informed");
        Assert.hasText(clientDetails.getClientSecret(), "client_secret must be informed");
        Assert.isTrue(!clientDetails.getScope().isEmpty(), "at least one scope must be informed");

        if (clientDetails.getAuthorities() == null || clientDetails.getAuthorities().isEmpty()) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRUSTED_CLIENT");
            clientDetails.setAuthorities(Arrays.asList(authority));
        }

        if (clientDetails.getAuthorizedGrantTypes() == null || clientDetails.getAuthorizedGrantTypes().isEmpty()) {
            clientDetails.setAuthorizedGrantTypes(Arrays.asList("client_credentials"));
        }

        if (clientDetails.getAccessTokenValiditySeconds() == null) {
            clientDetails.setAccessTokenValiditySeconds(defaultAccessTokenValidity);
        }

        if (clientDetails.getRefreshTokenValiditySeconds() == null) {
            clientDetails.setRefreshTokenValiditySeconds(defaultRefreshTokenValidity);
        }

        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        jdbcClientDetailsService.addClientDetails(clientDetails);

        clientDet = getClientDetails(clientDetails.getClientId());

        return clientDet;
    }

    public BaseClientDetails getClientDetails(String id) {
        BaseClientDetails clientDetails;

        Assert.hasText(id, "id must be informed");

        clientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(id);
        clientDetails.setClientSecret("*****");

        return clientDetails;
    }

    public List<BaseClientDetails> getAllClientDetails() {

        List<BaseClientDetails> baseClientDetails = new ArrayList<>();
        List<ClientDetails> clientDetailsList = jdbcClientDetailsService.listClientDetails();

        for (ClientDetails clientDetails : clientDetailsList) {
            BaseClientDetails baseClient = (BaseClientDetails) clientDetails;
            baseClient.setClientSecret("*****");
            baseClientDetails.add(baseClient);
        }

        return baseClientDetails;
    }
}
