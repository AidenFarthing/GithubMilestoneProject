package com.sparta.graphql.happypath;

import com.sparta.graphql.TestBase;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReadRepo extends TestBase {

    @Test
    void readRepoReturnsExpectedData() throws IOException {
        String query = readQuery("ReadRepo.graphql");
        Map<String, Object> variables = Map.of(
                "owner", OWNER,
                "name", REPO
        );

        Response response = executeQuery(query, "ReadRepo", variables);
        assertThat(response.statusCode(), is(200));
    }

}
