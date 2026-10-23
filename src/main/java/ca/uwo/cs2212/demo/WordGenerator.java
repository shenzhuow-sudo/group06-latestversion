package ca.uwo.cs2212.demo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Responsible for generating words used during gameplay.
 * <br><br>
 * The {@code WordGenerator} selects words based on the given {@link Level}.
 * It uses a {@link Random} instance to randomly choose words from the level’s
 * word list, ensuring variability during typing sessions. This class does not
 * store any game state and is designed to be stateless aside from its internal
 * random generator.
 *
 *
 * @version 2.0
 * @author Robert Niu
 * @author Yangyue Qin
 */
public class WordGenerator {
    private static final int MAX_RECENT_HISTORY = 8;

    /**
     * Random number generator used to select words.
     */
    private final Random random;
    private final Map<Integer, Deque<String>> recentWordsByLevel;

    /**
     * Constructs a WordGenerator.
     */
    public WordGenerator() {
        this.random = new Random();
        this.recentWordsByLevel = new HashMap<>();
    }

    /**
     * Generates a list of random words from the given level.
     *
     * @param level the level providing the word list
     * @param count number of words to generate
     * @return randomly selected words, or an empty list if unavailable
     */
    public List<String> generateWords(Level level, int count) {
        List<String> generated = new ArrayList<>();
        if (level == null || count <= 0) {
            return generated;
        }

        List<String> words = sanitizeWords(level.getWordList());
        if (words == null || words.isEmpty()) {
            return generated;
        }

        List<String> filteredWords = filterWordsByDifficulty(words, level.getDifficulty());

        if (filteredWords.isEmpty()){
            filteredWords = words;
        }

        List<String> pool = new ArrayList<>(filteredWords);
        Collections.shuffle(pool, random);

        Deque<String> recentWords = recentWordsByLevel.computeIfAbsent(level.getLevelId(), ignored -> new ArrayDeque<>());
        Set<String> recentWordSet = new HashSet<>(recentWords);
        List<String> preferredPool = new ArrayList<>();
        List<String> fallbackPool = new ArrayList<>();

        for (String word : pool) {
            if (recentWordSet.contains(word)) {
                fallbackPool.add(word);
            } else {
                preferredPool.add(word);
            }
        }

        addUniqueWords(generated, preferredPool, count);
        addUniqueWords(generated, fallbackPool, count);

        while (generated.size() < count && !filteredWords.isEmpty()) {
            String candidate = filteredWords.get(random.nextInt(filteredWords.size()));
            if (generated.isEmpty() || !generated.get(generated.size() - 1).equals(candidate) || filteredWords.size() == 1) {
                generated.add(candidate);
            }
        }

        rememberGeneratedWords(level.getLevelId(), generated, filteredWords.size());
        return generated;
    }

    private List<String> sanitizeWords(List<String> words) {
        List<String> sanitized = new ArrayList<>();
        if (words == null) {
            return sanitized;
        }
        for (String word : words) {
            if (word != null && !word.isBlank()) {
                sanitized.add(word);
            }
        }
        return sanitized;
    }

    private void addUniqueWords(List<String> generated, List<String> pool, int targetCount) {
        for (String word : pool) {
            if (generated.size() >= targetCount) {
                return;
            }
            generated.add(word);
        }
    }

    private void rememberGeneratedWords(int levelId, List<String> generatedWords, int availableWordCount) {
        if (generatedWords.isEmpty() || availableWordCount <= 1) {
            return;
        }

        Deque<String> recentWords = recentWordsByLevel.computeIfAbsent(levelId, ignored -> new ArrayDeque<>());
        int historyLimit = Math.max(1, Math.min(MAX_RECENT_HISTORY, availableWordCount - 1));
        for (String word : generatedWords) {
            recentWords.addLast(word);
            while (recentWords.size() > historyLimit) {
                recentWords.removeFirst();
            }
        }
    }

    /**
     * Filters a list of words by difficulty.
     * 
     * @param words the source word list
     * @param difficulty the difficulty level
     * @return filtered words matching the difficulty
     */
    private List<String> filterWordsByDifficulty(List<String> words, Difficulty difficulty){
        List<String> filtered = new ArrayList<>();
        if (difficulty == null){
            return new ArrayList<>(words);
        }

        for (String word : words){
            if (word == null || word.isBlank()){
                continue;
            }
            
            switch (difficulty){
                case EASY:
                    if (word.length() <= 5){
                        filtered.add(word);
                    }
                    break;
                
                case MEDIUM:
                    if (word.length() >= 6 && word.length() <= 7){
                        filtered.add(word);
                    }
                    break;

                case HARD:
                    if (word.length() >= 8){
                        filtered.add(word);
                    }
                    break;

                default:
                    filtered.add(word);
                    break;
            }
        }
        return filtered;
    }
}
