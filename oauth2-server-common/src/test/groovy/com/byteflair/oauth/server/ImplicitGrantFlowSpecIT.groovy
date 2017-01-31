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

/**
 * Created by calata on 22/11/16.
 */
@SpringBootTest(classes = ITConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application.yml"])
@Slf4j
@Stepwise
class ImplicitGrantFlowSpecIT extends Specification {

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

        driver.get("http://localhost:" + port + "/oauth/authorize?response_type=token&scope=read write trust&redirect_uri=https://byteflair.com/hola&client_id=" + client_id);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        String token = driver.getCurrentUrl();

        then: "The client obtains an access token"

        assert token.contains("access_token")

        driver.quit()
    }

    def "That can not authenticate unexisting client"() {
        given: "Invalid credentials"
        def client_id = "not_found"
        //given: "An existing client"
        when: "The client logs in"

        // paso 1 (simulamos navegacion web)
        WebDriver driver = new HtmlUnitDriver();

        driver.get("http://localhost:" + port + "/oauth/authorize?response_type=token&scope=read write trust&redirect_uri=https://byteflair.com/hola&client_id=" + client_id);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        then: "Access denied due to bad credentials"

        // con selenium no podemos acceder al response para comprobar que se ha producido un error
        // si podemos acceder al body de la página, que muestra el error
        assert driver.pageSource.contains("error=\"invalid_client\"")
        driver.quit()

    }

}
