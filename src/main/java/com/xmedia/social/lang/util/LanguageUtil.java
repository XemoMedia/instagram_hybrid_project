package com.xmedia.social.lang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jfasttext.JFastText;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LanguageUtil {

	private static final Logger logger = LoggerFactory.getLogger(LanguageUtil.class);
	private final JFastText fastText;

	public String detectLanguageIso(String text) {
		if (text == null || text.isBlank())
			return "Unknown";

		// FIX: Short English detection BEFORE using FastText
		if (isShortEnglish(text)) {
			return "English";
		}
		try {
			logger.debug("Attempting to detect language for text: {}", text);
			// fastText predict returns a list of predictions, we take the first one
			// The label format is __label__<lang_code>
			String predictedLabel = fastText.predict(text);
			String iso = predictedLabel.replace("__label__", "");
			logger.debug("Detected ISO: {} for text: {}", iso, text);

			if (iso != null && !iso.equals("Unknown"))
				return mapIsoToFullName(iso);
			return "Unknown";
		} catch (Exception e) {
			logger.error("Error detecting language with fastText: {}", e.getMessage(), e);
			return "Unknown";
		}
	}

	private String mapIsoToFullName(String iso) {
		if (iso == null)
			return "Unknown";
		return switch (iso) {
		case "en" -> "English";
		case "hi" -> "Hindi";
		case "bn" -> "Bengali";
		case "mr" -> "Marathi";
		case "gu" -> "Gujarati";
		case "pa" -> "Punjabi";
		case "ta" -> "Tamil";
		case "te" -> "Telugu";
		case "ur" -> "Urdu";
		case "es" -> "Spanish";
		case "kn" -> "Kannada";
		case "as" -> "Assamese";
		case "brx" -> "Bodo";
		case "doi" -> "Dogri";
		case "ks" -> "Kashmiri";
		case "kok" -> "Konkani";
		case "mai" -> "Maithili";
		case "ml" -> "Malayalam";
		case "mni" -> "Meitei";
		case "ne" -> "Nepali";
		case "or" -> "Odia";
		case "sa" -> "Sanskrit";
		case "sat" -> "Santali";
		case "sd" -> "Sindhi";
		default -> "Unknown";
		};
	}

	private boolean isShortEnglish(String text) {
		String cleaned = text.replaceAll("[^A-Za-z]", "").toLowerCase();

		return switch (cleaned) {
		case "hi", "hello", "hey", "hii", "hiii", "ok", "okay", "yo", "sup", "thx", "k", "kk", "Nice", "Super",
				"cool" ->
			true;
		default -> false;
		};
	}
}
