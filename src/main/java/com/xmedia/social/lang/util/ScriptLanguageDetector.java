package com.xmedia.social.lang.util;

public class ScriptLanguageDetector {
	/**
	 * Detects common Indian languages by Unicode block and returns ISO code
	 * (2-letter when possible)
	 */
	/**
	 * Detects the primary Indian language of a text based on Unicode blocks.
	 * Returns the full language name.
	 */
	public static String detectIndianLanguageScript(String text) {
		if (text == null || text.isBlank())
			return null;

		for (int i = 0; i < text.length(); i++) {
			int cp = text.codePointAt(i);
			Character.UnicodeBlock block = Character.UnicodeBlock.of(cp);

			if (block == Character.UnicodeBlock.DEVANAGARI) {
				return "Hindi / Marathi / Sanskrit / Nepali / Dogri / Konkani";
			}
			if (block == Character.UnicodeBlock.BENGALI) {
				return "Bengali / Assamese / Manipuri (Bengali script)";
			}
			if (block == Character.UnicodeBlock.GUJARATI) {
				return "Gujarati";
			}
			if (block == Character.UnicodeBlock.GURMUKHI) {
				return "Punjabi";
			}
			if (block == Character.UnicodeBlock.TELUGU) {
				return "Telugu";
			}
			if (block == Character.UnicodeBlock.TAMIL) {
				return "Tamil";
			}
			if (block == Character.UnicodeBlock.MALAYALAM) {
				return "Malayalam";
			}
			if (block == Character.UnicodeBlock.KANNADA) {
				return "Kannada";
			}
			if (block == Character.UnicodeBlock.ORIYA) {
				return "Odia";
			}
			if (block == Character.UnicodeBlock.ARABIC) {
				return "Urdu / Kashmiri / Sindhi";
			}
			if (block == Character.UnicodeBlock.MEETEI_MAYEK) {
				return "Manipuri (Meetei Mayek)";
			}
			if (block == Character.UnicodeBlock.OL_CHIKI) {
				return "Santali (Ol Chiki)";
			}
		}

		return "Unknown"; // Unknown or unsupported script
	}
}