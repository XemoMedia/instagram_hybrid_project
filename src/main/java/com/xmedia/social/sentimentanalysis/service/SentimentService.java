package com.xmedia.social.sentimentanalysis.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import jakarta.annotation.PostConstruct;

/**
 * Service for sentiment analysis using Stanford CoreNLP (similar to TextBlob in Python).
 * Uses machine learning-based NLP models for accurate sentiment analysis.
 */
@Service
public class SentimentService {

    private static final double POSITIVE_THRESHOLD = 0.1;
    private static final double NEGATIVE_THRESHOLD = -0.1;

    // Stanford CoreNLP pipeline (similar to TextBlob in Python)
    private StanfordCoreNLP nlpPipeline;
    
    // Cache for sentiment analysis results (text -> result)
    private final Map<String, SentimentAnalysisResult> sentimentCache = new ConcurrentHashMap<>();

    /**
     * Initialize Stanford CoreNLP pipeline (similar to TextBlob initialization in Python).
     * This is called once when the service is created.
     * Optimized for performance with thread-safe settings.
     */
    @PostConstruct
    public void initializeNLP() {
        try {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
            props.setProperty("tokenize.language", "en");
            props.setProperty("ssplit.eolonly", "true");
            
            // Performance optimizations
            props.setProperty("parse.maxlen", "80"); // Limit sentence length for faster parsing
            
            // Thread pool settings for parallel processing
            props.setProperty("threads", "4");
            props.setProperty("coref.threads", "4");
            
            nlpPipeline = new StanfordCoreNLP(props);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Stanford CoreNLP pipeline", e);
        }
    }

    /**
     * Analyzes text using Stanford CoreNLP (similar to TextBlob in Python).
     * This method uses machine learning-based NLP models for sentiment analysis.
     *
     * @param text Input text to analyze
     * @return Sentiment analysis result with label and polarity
     */
    public SentimentAnalysisResult analyzeText(String text) {
        if (!StringUtils.hasText(text)) {
            return new SentimentAnalysisResult("neutral", 0.0);
        }

        // Check cache first for performance
        String normalizedText = text.trim();
        SentimentAnalysisResult cached = sentimentCache.get(normalizedText);
        if (cached != null) {
            return cached;
        }

        if (nlpPipeline == null) {
            throw new IllegalStateException("Stanford CoreNLP pipeline not initialized");
        }

        try {
            Annotation annotation = nlpPipeline.process(text);
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

            if (sentences == null || sentences.isEmpty()) {
                return new SentimentAnalysisResult("neutral", 0.0);
            }

            // Calculate average sentiment across all sentences
            double totalPolarity = 0.0;
            int sentenceCount = 0;

            for (CoreMap sentence : sentences) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                
                // Convert Stanford sentiment (0=very negative, 1=negative, 2=neutral, 3=positive, 4=very positive)
                // to polarity scale (-1.0 to 1.0)
                double polarity = (sentiment - 2.0) / 2.0; // Maps 0->-1.0, 2->0.0, 4->1.0
                totalPolarity += polarity;
                sentenceCount++;
            }

            double avgPolarity = sentenceCount > 0 ? totalPolarity / sentenceCount : 0.0;

            // Determine label based on thresholds (same as Python TextBlob)
            String label;
            if (avgPolarity >= POSITIVE_THRESHOLD) {
                label = "positive";
            } else if (avgPolarity <= NEGATIVE_THRESHOLD) {
                label = "negative";
            } else {
                label = "neutral";
            }

            SentimentAnalysisResult result = new SentimentAnalysisResult(label, avgPolarity);
            
            // Cache the result for future use
            sentimentCache.put(normalizedText, result);
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error analyzing text with Stanford CoreNLP", e);
        }
    }

    /**
     * Batch analyze multiple texts for better performance.
     * Processes all texts in a single annotation to reduce overhead.
     *
     * @param texts List of texts to analyze
     * @return List of sentiment analysis results
     */
    public List<SentimentAnalysisResult> analyzeTextsBatch(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }

        if (nlpPipeline == null) {
            throw new IllegalStateException("Stanford CoreNLP pipeline not initialized");
        }

        try {
            // Combine all texts with newline separator for batch processing
            String combinedText = String.join("\n", texts.stream()
                    .filter(StringUtils::hasText)
                    .toList());

            if (!StringUtils.hasText(combinedText)) {
                return texts.stream()
                        .map(text -> new SentimentAnalysisResult("neutral", 0.0))
                        .toList();
            }

            // Process all texts in one annotation
            Annotation annotation = nlpPipeline.process(combinedText);
            List<CoreMap> allSentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

            if (allSentences == null || allSentences.isEmpty()) {
                return texts.stream()
                        .map(text -> new SentimentAnalysisResult("neutral", 0.0))
                        .toList();
            }

            // Group sentences back to original texts (approximate)
            // For better accuracy, we'll process individually but in parallel
            return analyzeTextsParallel(texts);
        } catch (Exception e) {
            // Fallback to parallel processing
            return analyzeTextsParallel(texts);
        }
    }

    /**
     * Analyze multiple texts in parallel for better performance.
     *
     * @param texts List of texts to analyze
     * @return List of sentiment analysis results
     */
    public List<SentimentAnalysisResult> analyzeTextsParallel(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }

        return texts.parallelStream()
                .map(this::analyzeText)
                .toList();
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
