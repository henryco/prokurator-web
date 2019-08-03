package dev.tindersamurai.prokurator.discord;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import dev.tindersamurai.prokurator.discord.util.BaseURL;
import lombok.NonNull;
import lombok.val;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface DiscordClientFactory {

	static <T> T createClient(@NonNull Class<T> repository) {
		val gson = new GsonBuilder().setLenient().create();
		return createClient(repository, GsonConverterFactory.create(gson));
	}

	static <T> T createClient(@NonNull Class<T> repository, ObjectMapper objectMapper) {
		return createClient(repository, JacksonConverterFactory.create(objectMapper));
	}

	static <T> T createClient(@NonNull Class<T> repository, Converter.Factory factory) {
		val log = Logger.getLogger(DiscordClientFactory.class.getName());
		log.log(Level.CONFIG, "createClient: " + repository.getName());

		val annotation = repository.getDeclaredAnnotation(BaseURL.class);
		if (annotation == null)
			throw new NullPointerException("Repository should have annotation " + BaseURL.class.getName());

		val retrofit = new Retrofit.Builder()
				.addConverterFactory(factory)
				.client(new OkHttpClient.Builder().build())
				.baseUrl(annotation.value())
				.build();

		return retrofit.create(repository);
	}
}
