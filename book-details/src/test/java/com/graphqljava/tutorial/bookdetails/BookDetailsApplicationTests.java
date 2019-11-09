package com.graphqljava.tutorial.bookdetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BookDetailsApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private String request = "" +
            "query {" +
            "bookById(id:\"book-1\"){" +
            "    name" +
            "    pageCount" +
            "    author {" +
            "      firstName" +
            "      lastName" +
            "    }" +
            "  }" +
            "}";

    private String expected = "" +
            "{" +
            "  \"data\": {" +
            "    \"bookById\": {" +
            "      \"name\": \"Harry Potter and the Philosopher's Stone\"," +
            "      \"pageCount\": 223," +
            "      \"author\": {" +
            "        \"firstName\": \"Joanne\"," +
            "        \"lastName\": \"Rowling\"" +
            "      }" +
            "    }" +
            "  }" +
            "}";

    @Test
    public void graphqlRequest() throws JsonProcessingException, JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode wrapper = objectMapper.createObjectNode();
        wrapper.put("query", request);
        String jsonQuery = objectMapper.writeValueAsString(wrapper);

        ResponseEntity<String> response = restTemplate.exchange(
                "/graphql", HttpMethod.POST, new HttpEntity<>(jsonQuery, httpHeaders), String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

}

