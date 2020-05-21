/*
 * Copyright (c) 2020 Byteflair
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
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
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
class FreeMarkerViewResolverSpecIT extends Specification {

    /**
     *   <!DOCTYPE html>
     <html lang="es">
     <head>
     <meta charset="utf-8"></meta>
     <meta name="unauthorized" content="true"></meta>
     <title>Modified</title>
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
            customTemplateLogin = [
                    content : 'PCFET0NUWVBFIGh0bWw+DQo8aHRtbCBsYW5nPSJlcyI+DQo8aGVhZD4NCjxtZXRhIGNoYXJzZXQ9InV0Zi04Ij48L21ldGE+DQo8bWV0YSBuYW1lPSJ1bmF1dGhvcml6ZWQiIGNvbnRlbnQ9InRydWUiPjwvbWV0YT4NCjx0aXRsZT5Nb2RpZmllZDwvdGl0bGU+DQo8c2NyaXB0IHNyYz0iLy9jb2RlLmpxdWVyeS5jb20vanF1ZXJ5LTEuMTAuMi5qcyI+PC9zY3JpcHQ+DQo8L2hlYWQ+DQo8Ym9keT4NCgk8ZGl2IGNsYXNzPSJjb250YWluZXIiPg0KCQk8ZGl2IGlkPSJsb2dpbkNvbnRlbnRzRGl2Ij4NCgkJCUxvZ2luIFRlc3QNCgkJCTxmb3JtIG1ldGhvZD0icG9zdCIgYWN0aW9uPSJsb2dpbiI+DQoJCQkJPGxhYmVsIGNsYXNzPSJoaWRkZW4tbGFiZWwiIGZvcj0idXNlcm5hbWUiPlVzZXJuYW1lPC9sYWJlbD4NCgkJCQk8aW5wdXQgaWQ9InVzZXJuYW1lIiB2YWx1ZT0iIiBuYW1lPSJ1c2VybmFtZSIgcGxhY2Vob2xkZXI9InVzZXJuYW1lIiA+PC9pbnB1dD4NCgkJCQk8bGFiZWwgY2xhc3M9ImhpZGRlbi1sYWJlbCIgZm9yPSJQYXNzd2QiPlBhc3N3b3JkPC9sYWJlbD4NCgkJCQk8aW5wdXQgaWQ9InBhc3N3b3JkIiB0eXBlPSJwYXNzd29yZCIgbmFtZT0icGFzc3dvcmQiIHBsYWNlaG9sZGVyPSJwYXNzd29yZCIgPjwvaW5wdXQ+DQoJCQkJPGlucHV0IHR5cGU9ImhpZGRlbiIgaWQ9ImNzcmZfdG9rZW4iIG5hbWU9IiR7X2NzcmYucGFyYW1ldGVyTmFtZX0iIHZhbHVlPSIke19jc3JmLnRva2VufSIvPg0KCQkJCTxpbnB1dCBpZD0ibG9naW4iIGNsYXNzPSJyYy1idXR0b24gcmMtYnV0dG9uLXN1Ym1pdCIgdHlwZT0ic3VibWl0IiB2YWx1ZT0iTG9nIGluIiBuYW1lPSJsb2dpbiI+PC9pbnB1dD4NCgkJCTwvZm9ybT4NCgkJPC9kaXY+DQoJPC9kaXY+DQo8L2JvZHk+DQo8L2h0bWw+',
                    encoding: 'UTF-8'
            ]

    @LocalServerPort
    int port

    @Shared
    def accessToken

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

    def createCustomTemplate(name, template) {
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(template)
                .put("http://localhost:" + port + "/custom-templates/" + name)
    }

    def deleteCustomTemplate(name) {
        given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .delete("http://localhost:" + port + "/custom-templates/" + name)
    }

    def "That renders default /login template (/resources/templates/)"() {
        when: ""

        // step 1 (simulate web navigation)
        WebDriver driver = new HtmlUnitDriver()

        driver.get("http://localhost:" + port + "/login")

        then: "Following elements exists (see login.ftl)"
        assert driver.findElement(By.tagName("title")).getAttribute("innerHtml").contains("Byteflair Authorization Server")
        assert !driver.findElements(By.tagName("legend")).isEmpty()
        assert driver.findElement(By.id("username")).isDisplayed()
        assert driver.findElement(By.id("password")).isDisplayed()
        assert driver.findElement(By.id("login")).isDisplayed()

        driver.quit()
    }

    def "That renders database LOGIN template "() {
        when: "Insert login custom template"

        createCustomTemplate("login", customTemplateLogin) // Automatically delete cache
        // step 1 (simulate web navigation)
        WebDriver driver = new HtmlUnitDriver()

        driver.get("http://localhost:" + port + "/login")

        then: "Following elements exists "
        /**
         * Ensure changes in rendered page
         */
        assert driver.findElement(By.tagName("title")).getAttribute("innerHtml").contains("Modified")
        assert driver.findElements(By.tagName("legend")).isEmpty()
        assert driver.findElement(By.id("username")).isDisplayed()
        assert driver.findElement(By.id("password")).isDisplayed()
        assert driver.findElement(By.id("login")).isDisplayed()

        driver.quit()
    }

    def "That can log in with database template when _CSRF exists in form "() {
        when: ""
        def user = "user"
        def password = "secret"

        // step 1 (simulate web navigation)
        WebDriver driver = new HtmlUnitDriver()

        driver.get("http://localhost:" + port + "/login")
        driver.findElement(By.id("username")).sendKeys(user)
        driver.findElement(By.id("password")).sendKeys(password)
        driver.findElement(By.id("login")).click()


        then: "Following elements exists "


        driver.quit()
    }

    def "That renders default /login template when delete database template"() {
        when: "Delete login custom template"

        deleteCustomTemplate("login") // Automatically delete cache
        // step 1 (simulate web navigation)
        WebDriver driver = new HtmlUnitDriver()

        driver.get("http://localhost:" + port + "/login")

        then: "Following elements exists (see login.ftl)"
        /**
         * Ensure changes in rendered page
         */
        assert driver.findElement(By.tagName("title")).getAttribute("innerHtml").contains("Byteflair Authorization Server")
        assert !driver.findElements(By.tagName("legend")).isEmpty()
        assert driver.findElement(By.id("username")).isDisplayed()
        assert driver.findElement(By.id("password")).isDisplayed()
        assert driver.findElement(By.id("login")).isDisplayed()

        driver.quit()
    }


}
