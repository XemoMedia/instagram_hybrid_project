package com.xmedia.social.lang.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

@Configuration
public class LanguageConfig {

	@Bean
	public LanguageDetector languageDetector() {
		return LanguageDetectorBuilder.fromLanguages(
				// Indian languages supported by Lingua
				Language.HINDI, Language.BENGALI, Language.TAMIL, Language.GUJARATI, Language.PUNJABI, Language.TELUGU,
				Language.MARATHI, Language.URDU,
				// Popular foreign languages
				Language.ENGLISH, Language.SPANISH, Language.FRENCH, Language.GERMAN, Language.CHINESE,
				Language.JAPANESE, Language.KOREAN, Language.ARABIC, Language.RUSSIAN, Language.ITALIAN,
				Language.PORTUGUESE).build();
	}
}
