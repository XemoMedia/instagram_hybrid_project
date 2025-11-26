package com.xmedia.social.base.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.jfasttext.JFastText;

@Configuration
public class FastTextConfig {

	@Bean
	public JFastText jFastText() {
		JFastText fastText = new JFastText();

		// Load model safely from resources
		String modelPath = "models/lid.176.ftz";
		try (InputStream modelStream = getClass().getClassLoader().getResourceAsStream(modelPath)) {
			if (modelStream == null) {
				throw new IllegalArgumentException("FastText model file not found in resources/" + modelPath);
			}

			// Create a temporary file to load the model
			Path tempModelFile = Files.createTempFile("fasttext-model-", ".ftz");
			Files.copy(modelStream, tempModelFile, StandardCopyOption.REPLACE_EXISTING);

			fastText.loadModel(tempModelFile.toAbsolutePath().toString());

			// Delete the temporary file when the application exits
			tempModelFile.toFile().deleteOnExit();

		} catch (IOException e) {
			throw new RuntimeException("Failed to load FastText model", e);
		}
		return fastText;
	}
}
