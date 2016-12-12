/*
 * Copyright (c) 2016 Byteflair
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.byteflair.oauth.server

import groovy.util.logging.Slf4j
import io.restassured.http.ContentType
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given

/**
 * Created by calata on 21/11/16.
 */

@SpringBootTest(classes = ITConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ['spring.profiles.active=dev',
        'spring.jpa.database=MYSQL',
        'spring.jpa.show-sql=true',
        'spring.jpa.generate-ddl=false',
        'spring.datasource.initialize=true',
        'spring.datasource.host=localhost',
        'spring.datasource.port=3306',
        'spring.datasource.dbname=oauth_db',
        'spring.datasource.username=oauth_server',
        'spring.datasource.password=password',
        'spring.datasource.platform=mysql',
        'spring.datasource.url=jdbc:mysql://${spring.datasource.host}:${spring.datasource.port}/${spring.datasource.dbname}?autoReconnect=true&useUnicode=true&characterEncoding=utf8&noAccessToProcedureBodies=true&protocol=tcp',
        'spring.datasource.driver-class-name=com.mysql.jdbc.Driver',
        'spring.data.rest.returnBodyOnCreate=true',
        'spring.data.rest.returnBodyOnupdate=true',
        'server.contextPath=/',
        'keystore.path=target/keystore.jks',
        'keystore.password=password',
        'keystore.key.alias=dev_oauth_jwt_key',
        'keystore.key.password=password',
        'logging.config=classpath:logback-development.xml',
        'logging.level.com.byteflair.oauth.server=DEBUG',
        'logging.level.org.springframework.security=DEBUG'])
@Slf4j
@Stepwise
class PasswordFlowSpecIT extends Specification {
    @LocalServerPort
    int port

    String username = "admin"
    String password = "secret"
    String client_id = "mercury"
    String client_secret = "secret"

    def "That can authenticate user"() {
        when: "The user logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password&username=" + username + "&password=" + password)
        then: "The user obtains an access token"
        response.then().log().all()
                .statusCode(200)

        def body = response.as(Map)
        assert body.get('access_token') != null
        assert body.get('email') != null
        assert body.get('details') != null

    }

    def "That can authenticate client with x-www-form-urlencoded"() {
        //given: "An existing client"
        when: "The client logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.URLENC)
                .auth().basic(client_id, client_secret)
                .body("grant_type=password&scope=trust&username=" + username + "&password=" + password)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password")
        then: "The client obtains an access token"
        response.then().log().all()
                .statusCode(200)

        def body = response.as(Map)
        assert body.get('access_token') != null
        assert body.get('email') != null
        assert body.get('details') != null

    }

    def "That can not authenticate unexisting client"() {
        given: "Invalid credentials"
        def username = "not_found"
        def password = "whatever"
        when: "The cllient logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password&username=" + username + "&password=" + password)
        then: "Access denied"
        response.then().log().all()
                .statusCode(400)
    }

    def "That can not authenticate invalid credentials"() {
        given: "Invalid credentials"
        def password = "whatever"
        when: "The cllient logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password&username=" + username + "&password=" + password)
        then: "The client obtains an access token"
        response.then().log().all()
                .statusCode(400)
    }
}
