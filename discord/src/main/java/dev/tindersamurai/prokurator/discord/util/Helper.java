package dev.tindersamurai.prokurator.discord.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.val;
import retrofit2.Response;

import java.io.IOException;

@Log
public class Helper {

    @Data public static class RateLimitMessage {
        private String message;
        private long retry_after;
        private boolean global;

        public static RateLimitMessage parse(byte[] bytes) throws IOException {
            return new ObjectMapper().readValue(bytes, RateLimitMessage.class);
        }
    }

    public static boolean onError(Response r, String msg, Class<?> clazz) throws IOException {
        if (r.code() == 429) {

            log.warning("Request limit exceeded [CODE 429] " + clazz.getName());
            if (r.errorBody() == null) {
                sleep(1000);
            }

            else {
                val err = RateLimitMessage.parse(r.errorBody().bytes());
                sleep(err.getRetry_after() + 100);
            }

            return true;
        }

        if (!r.isSuccessful()) {
            val err = (r.errorBody() == null ? "" : r.errorBody().string());
            System.out.println("body: " + r.body());
            System.out.println("error: " + err);
            throw new IOException(msg + ": " + err);
        }
        return false;
    }

    public static void sleep(long ms) throws IOException {
        log.warning("SLEEP: " + ms);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
