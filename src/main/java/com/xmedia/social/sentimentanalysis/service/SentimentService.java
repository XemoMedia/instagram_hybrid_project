package com.xmedia.social.sentimentanalysis.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.xmedia.social.sentimentanalysis.dto.SentimentAnalysisRequestDto;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for sentiment analysis using Stanford CoreNLP (similar to TextBlob in Python).
 * Uses machine learning-based NLP models for accurate sentiment analysis.
 */
@Service
@Slf4j
public class SentimentService {

  
    private static final Logger logger = LoggerFactory.getLogger(SentimentService.class);
    @Value("${python.sentiment.service.url}")
	private String pythonSentimentServiceUrl;

   
    /**
	 * Calls Python REST endpoint to analyze sentiment for comments and replies.
	 * 
	 * @param commentIds List of comment IDs to analyze
	 * @param repliedIds List of reply IDs to analyze
	 */
	public void callPythonSentimentAnalysisEndpoint(List<String> commentIds, List<String> repliedIds) {
		try {
			logger.info("Calling Python sentiment analysis endpoint for {} comments and {} replies", 
				commentIds.size(), repliedIds.size());
			
			// Create request DTO
			SentimentAnalysisRequestDto request = new SentimentAnalysisRequestDto(commentIds, repliedIds);
			
			// Create WebClient instance and make REST call to Python endpoint
			WebClient webClient = WebClient.builder().build();
			
			// Make REST call to Python endpoint using WebClient
			String response = webClient.post()
				.uri(pythonSentimentServiceUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(String.class)
				.block(); // Blocking call - convert Mono to synchronous
			
			logger.info("Successfully called Python sentiment analysis endpoint. Response: {}", response);
			
		} catch (Exception e) {
			logger.error("Failed to call Python sentiment analysis endpoint: {}", e.getMessage(), e);
			throw e;
		}
	}


    /**
     * Inner class to hold sentiment analysis result.
     */
    public static class SentimentAnalysisResult {
        private final String label;
        private final double polarity;

        public SentimentAnalysisResult(String label, double polarity) {
            this.label = label;
            this.polarity = polarity;
        }

        public String getLabel() {
            return label;
        }

        public double getPolarity() {
            return polarity;
        }
    }
}
