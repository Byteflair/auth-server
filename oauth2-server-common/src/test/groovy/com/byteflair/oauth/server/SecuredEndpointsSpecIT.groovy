package com.byteflair.oauth.server

import groovy.util.logging.Slf4j
import io.restassured.http.ContentType
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given

/**
 * Created by calata on 20/01/17.
 */
@SpringBootTest(classes = ITConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application.yml"])
@Slf4j
@Stepwise
public class SecuredEndpointsSpecIT extends Specification {

    @LocalServerPort
    int port

    @Shared
            client2 = [
                    client_id             : 'test2',
                    client_secret         : 'secret',
                    scope                 : 'trust',
                    authorized_grant_types: 'authorization_code,password,refresh_token,implicit,client_credentials',
                    authorities           : 'ROLE_TRUSTED_CLIENT',
                    access_token_validity : 900,
                    refresh_token_validity: 43200,
                    detail1               : 'detail1',
                    detail2               : 'detail2',
                    autoapprove           : 'true'
            ]

    String username_user = "user"
    String username_admin = "admin"
    String password = "secret"
    String client_id = "admin-client"
    String client_secret = "secret"

    @Shared
    def String access_token = ""

    def "Authenticate with client_id ROLE ADMIN"() {
        when: "The client_id logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=client_credentials")
        def body = response.as(Map)
        access_token = body.get('access_token')

        then: "The client obtains an access token"
        response.then().log().all()
                .statusCode(200)
    }

    def Object commonRequest(uri) {
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .get("http://localhost:" + port + uri)

        return response
    }

    def Object checkResponseNotNullAndStatusOK(response) {
        response.then().log().all()
                .statusCode(200)

        def body = response.as(Map)
        assert body != null
        return body
    }

    def void checkResponseAndStatusForbidden(response) {
        response.then().log().all()
                .statusCode(403)

        def body = response.as(Map)
        assert body['error'] == 'access_denied'
    }

    @Ignore
    def "That can access / with ROLE_ADMIN"() {
        when: "Client request /"
        def response = commonRequest("/")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /users with ROLE_ADMIN"() {
        when: "Client request /users"
        def response = commonRequest("/users")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /user-states with client ROLE_ADMIN"() {
        when: "Client request /user-states"
        def response = commonRequest("/user-states")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /user-details with client ROLE_ADMIN"() {
        when: "Client request /user-details"
        def response = commonRequest("/user-details")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /roles with client ROLE_ADMIN"() {
        when: "Client request /roles"
        def response = commonRequest("/roles")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /groups with client ROLE_ADMIN"() {
        when: "Client request /groups"
        def response = commonRequest("/groups")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /profile with client ROLE_ADMIN"() {
        when: "Client request /profile"
        def response = commonRequest("/profile")
        then: "The client receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /custom-templates with client ROLE_ADMIN"() {
        when: "Client request /custom-templates"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .get("http://localhost:" + port + "/custom-templates/login") // login is one of existent names
        then: "The client receives OK (database is empty so body is null)"
        response.then().log().all()
                .statusCode(200)
    }


    def createNewClient() {
        //given: "An existing client"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .body(client2)
                .post("http://localhost:" + port + "/clients")
        response.then().log().all()
                .statusCode(200)
    }


    def "Authenticate with client_id without ROLE ADMIN"() {
        when: "Create new client and logs in"
        createNewClient()
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client2.client_id, client2.client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=client_credentials")

        def body = response.as(Map)
        access_token = body.get('access_token')

        then: "The client obtains an access token"
        response.then().log().all()
                .statusCode(200)

    }

    def "That can't access /users without ROLE_ADMIN"() {
        when: "Client request /users"
        def response = commonRequest("/users")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /user-states without ROLE_ADMIN"() {
        when: "Client request /user-states"
        def response = commonRequest("/user-states")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /user-details without ROLE_ADMIN"() {
        when: "Client request /user-details"
        def response = commonRequest("/user-details")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /roles without ROLE_ADMIN"() {
        when: "Client request /roles"
        def response = commonRequest("/roles")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /groups without ROLE_ADMIN"() {
        when: "Client request /groups"
        def response = commonRequest("/groups")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /profile without ROLE_ADMIN"() {
        when: "Client request /profile"
        def response = commonRequest("/profile")
        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That can't access /custom-templates without ROLE_ADMIN"() {
        when: "Client request /custom-templates"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .get("http://localhost:" + port + "/custom-templates/login") // login is one of existent names

        then: "The client receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "Authenticate with user with ROLE ADMIN"() {
        when: "User logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password&username=" + username_admin + "&password=" + password)

        def body = response.as(Map)
        access_token = body.get('access_token')
        then: "The user obtains an access token"
        response.then().log().all()
                .statusCode(200)


    }

    def "That can access /users with user ROLE_ADMIN"() {
        when: "User request /users"
        def response = commonRequest("/users")
        then: "User receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /user-states with user ROLE_ADMIN"() {
        when: "User request /user-states"
        def response = commonRequest("/user-states")
        then: "User receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /user-details with user ROLE_ADMIN"() {
        when: "User request /user-details"
        def response = commonRequest("/user-details")
        then: "User receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /roles with user ROLE_ADMIN"() {
        when: "User request /roles"
        def response = commonRequest("/roles")
        then: "Usert receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /groups with user ROLE_ADMIN"() {
        when: "User request /groups"
        def response = commonRequest("/groups")
        then: "User receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /profile with user ROLE_ADMIN"() {
        when: "User request /profile"
        def response = commonRequest("/profile")
        then: "User receives response"
        checkResponseNotNullAndStatusOK(response)
    }

    def "That can access /custom-templates with user ROLE_ADMIN"() {
        when: "User request /custom-templates"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .get("http://localhost:" + port + "/custom-templates/login") // login is one of existent names
        then: "User receives response"
        then: "The client receives OK (database is empty so body is null)"
        response.then().log().all()
                .statusCode(200)
    }

    def "Authenticate with user without ROLE ADMIN"() {
        when: "User logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=password&username=" + username_user + "&password=" + password)

        def body = response.as(Map)
        access_token = body.get('access_token')

        then: "The user obtains an access token"
        response.then().log().all()
                .statusCode(200)
    }

    def "That user can't access /users without ROLE_ADMIN"() {
        when: "User request /users"
        def response = commonRequest("/users")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That user can't access /user-states without ROLE_ADMIN"() {
        when: "User request /user-states"
        def response = commonRequest("/user-states")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That user can't access /user-details without ROLE_ADMIN"() {
        when: "User request /user-details"
        def response = commonRequest("/user-details")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That user can't access /roles without ROLE_ADMIN"() {
        when: "User request /roles"
        def response = commonRequest("/roles")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That user can't access /groups without ROLE_ADMIN"() {
        when: "User request /groups"
        def response = commonRequest("/groups")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }


    def "That user can't access /profile without ROLE_ADMIN"() {
        when: "User request /profile"
        def response = commonRequest("/profile")
        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }

    def "That user can't access /custom-templates without ROLE_ADMIN"() {
        when: "Client request /custom-templates"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(access_token)
                .get("http://localhost:" + port + "/custom-templates/login") // login is one of existent names

        then: "User receives response"
        checkResponseAndStatusForbidden(response)
    }


}

