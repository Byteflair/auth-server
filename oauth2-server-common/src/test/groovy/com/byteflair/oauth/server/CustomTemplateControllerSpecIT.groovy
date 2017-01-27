package com.byteflair.oauth.server

import com.byteflair.oauth.server.boundary.CustomTemplateResource
import com.byteflair.oauth.server.domain.CustomTemplate
import groovy.util.logging.Slf4j
import io.restassured.http.ContentType
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given
/**
 * Created by calata on 26/01/17.
 */
@SpringBootTest(classes = ITConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application.yml"])
@Slf4j
@Stepwise
class CustomTemplateControllerSpecIT extends Specification {


    /** Custom Template content (decoded)
     * <!DOCTYPE html>
     <html lang="es">
         <head>
             <meta charset="utf-8"></meta>
             <meta name="unauthorized" content="true"></meta>
             <title>Byteflair Authorization Server</title>
             <script src="//code.jquery.com/jquery-1.10.2.js"></script>
         </head>
         <body>
             <div class="container">
                 <div id="loginContentsDiv">
                     Login Test
                     <form method="post" action="login">
                         <label class="hidden-label" for="username">Username</label>
                         <input id="username" value="" name="username" placeholder="username" ></input>
                         <label class="hidden-label" for="Passwd">Password</label>
                         <input id="password" type="password" name="password" placeholder="password" ></input>
                         <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                         <input id="login" class="rc-button rc-button-submit" type="submit" value="Log in" name="login"></input>
                     </form>
                 </div>
             </div>
         </body>
     </html>
     */
    @Shared
    customTemplate = [
            content   : 'PCFET0NUWVBFIGh0bWw+DQo8aHRtbCBsYW5nPSJlcyI+DQo8aGVhZD4NCjxtZXRhIGNoYXJzZXQ9InV0Zi04Ij48L21ldGE+DQo8bWV0YSBuYW1lPSJ1bmF1dGhvcml6ZWQiIGNvbnRlbnQ9InRydWUiPjwvbWV0YT4NCjx0aXRsZT5CeXRlZmxhaXIgQXV0aG9yaXphdGlvbiBTZXJ2ZXI8L3RpdGxlPg0KPHNjcmlwdCBzcmM9Ii8vY29kZS5qcXVlcnkuY29tL2pxdWVyeS0xLjEwLjIuanMiPjwvc2NyaXB0Pg0KPC9oZWFkPg0KPGJvZHk+DQoJPGRpdiBjbGFzcz0iY29udGFpbmVyIj4NCgkJPGRpdiBpZD0ibG9naW5Db250ZW50c0RpdiI+DQoJCQlMb2dpbiBUZXN0DQoJCQk8Zm9ybSBtZXRob2Q9InBvc3QiIGFjdGlvbj0ibG9naW4iPg0KCQkJCTxsYWJlbCBjbGFzcz0iaGlkZGVuLWxhYmVsIiBmb3I9InVzZXJuYW1lIj5Vc2VybmFtZTwvbGFiZWw+DQoJCQkJPGlucHV0IGlkPSJ1c2VybmFtZSIgdmFsdWU9IiIgbmFtZT0idXNlcm5hbWUiIHBsYWNlaG9sZGVyPSJ1c2VybmFtZSIgPjwvaW5wdXQ+DQoJCQkJPGxhYmVsIGNsYXNzPSJoaWRkZW4tbGFiZWwiIGZvcj0iUGFzc3dkIj5QYXNzd29yZDwvbGFiZWw+DQoJCQkJPGlucHV0IGlkPSJwYXNzd29yZCIgdHlwZT0icGFzc3dvcmQiIG5hbWU9InBhc3N3b3JkIiBwbGFjZWhvbGRlcj0icGFzc3dvcmQiID48L2lucHV0Pg0KCQkJCTxpbnB1dCB0eXBlPSJoaWRkZW4iIGlkPSJjc3JmX3Rva2VuIiBuYW1lPSIke19jc3JmLnBhcmFtZXRlck5hbWV9IiB2YWx1ZT0iJHtfY3NyZi50b2tlbn0iLz4NCgkJCQk8aW5wdXQgaWQ9ImxvZ2luIiBjbGFzcz0icmMtYnV0dG9uIHJjLWJ1dHRvbi1zdWJtaXQiIHR5cGU9InN1Ym1pdCIgdmFsdWU9IkxvZyBpbiIgbmFtZT0ibG9naW4iPjwvaW5wdXQ+DQoJCQk8L2Zvcm0+DQoJCTwvZGl2Pg0KCTwvZGl2Pg0KPC9ib2R5Pg0KPC9odG1sPg==',
            encoding  : 'UTF-8'
    ]

    @Shared
    modifiedCustomTemplate = [
            content   :'PCFET0NUWVBFIGh0bWw+DQo8aHRtbCBsYW5nPSJlcyI+DQo8aGVhZD4NCjxtZXRhIGNoYXJzZXQ9InV0Zi04Ij48L21ldGE+DQo8bWV0YSBuYW1lPSJ1bmF1dGhvcml6ZWQiIGNvbnRlbnQ9InRydWUiPjwvbWV0YT4NCjx0aXRsZT5Nb2RpZmllZDwvdGl0bGU+DQo8c2NyaXB0IHNyYz0iLy9jb2RlLmpxdWVyeS5jb20vanF1ZXJ5LTEuMTAuMi5qcyI+PC9zY3JpcHQ+DQo8L2hlYWQ+DQo8Ym9keT4NCgk8ZGl2IGNsYXNzPSJjb250YWluZXIiPg0KCQk8ZGl2IGlkPSJsb2dpbkNvbnRlbnRzRGl2Ij4NCgkJCUxvZ2luIFRlc3QNCgkJCTxmb3JtIG1ldGhvZD0icG9zdCIgYWN0aW9uPSJsb2dpbiI+DQoJCQkJPGxhYmVsIGNsYXNzPSJoaWRkZW4tbGFiZWwiIGZvcj0idXNlcm5hbWUiPlVzZXJuYW1lPC9sYWJlbD4NCgkJCQk8aW5wdXQgaWQ9InVzZXJuYW1lIiB2YWx1ZT0iIiBuYW1lPSJ1c2VybmFtZSIgcGxhY2Vob2xkZXI9InVzZXJuYW1lIiA+PC9pbnB1dD4NCgkJCQk8bGFiZWwgY2xhc3M9ImhpZGRlbi1sYWJlbCIgZm9yPSJQYXNzd2QiPlBhc3N3b3JkPC9sYWJlbD4NCgkJCQk8aW5wdXQgaWQ9InBhc3N3b3JkIiB0eXBlPSJwYXNzd29yZCIgbmFtZT0icGFzc3dvcmQiIHBsYWNlaG9sZGVyPSJwYXNzd29yZCIgPjwvaW5wdXQ+DQoJCQkJPGlucHV0IHR5cGU9ImhpZGRlbiIgaWQ9ImNzcmZfdG9rZW4iIG5hbWU9IiR7X2NzcmYucGFyYW1ldGVyTmFtZX0iIHZhbHVlPSIke19jc3JmLnRva2VufSIvPg0KCQkJCTxpbnB1dCBpZD0ibG9naW4iIGNsYXNzPSJyYy1idXR0b24gcmMtYnV0dG9uLXN1Ym1pdCIgdHlwZT0ic3VibWl0IiB2YWx1ZT0iTG9nIGluIiBuYW1lPSJsb2dpbiI+PC9pbnB1dD4NCgkJCTwvZm9ybT4NCgkJPC9kaXY+DQoJPC9kaXY+DQo8L2JvZHk+DQo8L2h0bWw+',
            encoding  : 'UTF-8'
        ]

    @LocalServerPort
    int port

    @Shared
    def accessToken;
    @Shared
    def content;

    String client_id = "admin-client"
    String client_secret = "secret"


    def "Authenticate"() {
        //given: "An existing client"
        when: "The client logs in"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=client_credentials")
        def body = response.as(Map)
        accessToken = body.get('access_token')
        then: "The client obtains an access token"
        response.then().log().all()
                .statusCode(200)

    }

    def "That can create login"() {
        when: "Send PUT request to update custom-template login"
        def templateName = "login"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(customTemplate)
                .put("http://localhost:" + port + "/custom-templates/"+templateName)

        def body = response.as(CustomTemplateResource)
        content = body.getContent()

        then: "Custom template is created correctly and returns"
        response.then().log().all()
                .statusCode(201)

        assert body.getName().equalsIgnoreCase(CustomTemplate.TemplateName.valueOf(templateName.toUpperCase()).toString())
        //assert body.getContent().equals(Base64.getDecoder().decode(customTemplate['content']))
        assert body.getEncoding().equals(customTemplate['encoding'])
        assert body.getLast_modified() != null

    }

    def "That can GET existent custom-template login"() {
        when: "Send GET request to update custom-template login"
        def templateName = "login"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(customTemplate)
                .put("http://localhost:" + port + "/custom-templates/"+templateName)
        then: "Custom template is retrieved"
        response.then().log().all()
                .statusCode(201)

        def body = response.as(CustomTemplateResource)

        assert body.getName().equalsIgnoreCase(CustomTemplate.TemplateName.valueOf(templateName.toUpperCase()).toString())
        //assert body.getContent().equals(Base64.getDecoder().decode(customTemplate['content']))
        assert body.getEncoding().equals(customTemplate['encoding'])
        assert body.getLast_modified() != null
    }

    def "That sucessive PUT requets overrides last custom template"() {
        when: "Send PUT request to update custom-template login"
        def templateName = "login"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(modifiedCustomTemplate)
                .put("http://localhost:" + port + "/custom-templates/"+templateName)
        then: "Custom template is created correctly and returns"
        response.then().log().all()
                .statusCode(201)

        def body = response.as(CustomTemplateResource)

        assert !body.getContent().equals(content)

    }

    def "That can delete custom-template login"() {
        when: "Send DELETE request to update custom-template login"
        def templateName = "login"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .delete("http://localhost:" + port + "/custom-templates/"+templateName)
        then: "Custom template is deleted"
        response.then().log().all()
                .statusCode(200)
    }

    def "That can't create non existent TemplateName"() {
        //given: "An existing client"
        when: "Send PUT request to update custom-template asdf"
        def templateName = "asdf"
        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(customTemplate)
                .put("http://localhost:" + port + "/custom-templates/"+templateName)
        then: "Custom template is created correctly and returns"
        response.then().log().all()
                .statusCode(500)

    }

    def "That can't create with invalid encoding"() {
        //given: "An existing client"
        when: "Send PUT request with invalid encoding"

        def templateName = "login"
        def customTemplate2 = customTemplate.getClass().newInstance(customTemplate)
        customTemplate2['encoding'] = 'asdf'

        def response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(customTemplate2)
                .put("http://localhost:" + port + "/custom-templates/"+templateName)
        then: "Custom template is created correctly and returns"
        response.then().log().all()
                .statusCode(500)
    }
}
