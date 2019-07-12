package dev.tindersamurai.prokurator.discord.client;


import dev.tindersamurai.prokurator.discord.client.util.BaseURL;
import lombok.NonNull;
import lombok.val;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface DiscordClientFactory {

	static <T> T createClient(@NonNull Class<T> repository) {
		val log = Logger.getLogger(DiscordClientFactory.class.getName());
		log.log(Level.CONFIG, "createClient: " + repository.getName());

		val annotation = repository.getDeclaredAnnotation(BaseURL.class);
		if (annotation == null)
			throw new NullPointerException("Repository should have annotation " + BaseURL.class.getName());

		val retrofit = new Retrofit.Builder()
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient.Builder().build())
				.baseUrl(annotation.value())
				.build();
		return retrofit.create(repository);
	}
}
