package com.byteflair.oauth.server

import groovy.util.logging.Slf4j
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given

/**
 * Created by calata on 22/11/16.
 */
@SpringBootTest(classes = ITConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application.yml"])
@Slf4j
@Stepwise
class AuthenticationCodeFlowSpecIT extends Specification {

    @LocalServerPort
    int port

    String client_id = "admin-client"
    String client_secret = "secret"
    String username = "admin"
    String password = "secret"

    def "That can authenticate client"() {
        //given: "An existing client"
        when: "The client logs in"

        // paso 1 (simulamos navegacion web)
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:" + port + "/oauth/authorize?response_type=code&client_id="
                + client_id + "&client_secret=" + client_secret + "&redirect_uri=http://byteflair.com/hola");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        String codeUrl = driver.getCurrentUrl();
        codeUrl = codeUrl.substring(codeUrl.indexOf("code="));

        // paso 2
        def response = given().auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=authorization_code&" + codeUrl + "&redirect_uri=http://byteflair.com/hola")

        then: "The client obtains an access token"

        response.then().log().all()
                .statusCode(200)

        def body = response.as(Map)
        assert body.get('access_token') != null

        driver.quit()
    }

    def "That can not authenticate unexisting client"() {
        given: "Invalid credentials"
        def client_id = "not_found"
        //given: "An existing client"
        when: "The client logs in"

        // paso 1 (simulamos navegacion web)
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:" + port + "/oauth/authorize?response_type=code&client_id="
                + client_id + "&client_secret=" + client_secret + "&redirect_uri=http://byteflair.com/hola");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        then: "Access denied due to bad credentials"

        // con selenium no podemos acceder al response para comprobar que se ha producido un error
        // si podemos acceder al body de la página, que muestra el error
        assert driver.pageSource.contains("error=\"invalid_client\"")
        driver.quit()

    }

    def "That can not authenticate invalid code"() {
        //given: "An existing client"
        when: "The client logs in"

        // paso 1 (simulamos navegacion web)
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:" + port + "/oauth/authorize?response_type=code&client_id="
                + client_id + "&client_secret=" + client_secret + "&redirect_uri=http://byteflair.com/hola");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        String codeUrl = "code=throw_error"
        // paso 2
        def response = given().auth().basic(client_id, client_secret)
                .post("http://localhost:" + port + "/oauth/token?grant_type=authorization_code&" + codeUrl + "&redirect_uri=http://byteflair.com/hola")

        then: "The client can not obtain access token"

        response.then().log().all()
                .statusCode(400)

        def body = response.as(Map)
        assert body.get('access_token') == null

        driver.quit()
    }

}
