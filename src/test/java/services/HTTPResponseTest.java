package services;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.junit.jupiter.api.Test;
import services.exc.ServiceNotAvailableException;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HTTPResponseTest {

    @Test
    void getResponse() {
        String result = "";
        try {
            HTTPResponse respone = new HTTPResponse();
            result = respone.getResponse("http://www.omdbapi.com/?t=Infinity&apikey=63f68b84");
        } catch (ServiceNotAvailableException e) {

        } finally {
            assertTrue(result.contains("Infinity"));
        }
    }

    @Test
    void asyncResponse() {
        String response = "";
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        CompletableFuture<Response> f = asyncHttpClient
                .prepareGet("http://www.omdbapi.com/?t=Infinity&apikey=63f68b84")
                .execute()
                .toCompletableFuture();
        try {
            response = f.get().getResponseBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotEquals("", response);
    }

    @Test
    void feignClient() {

    }
}