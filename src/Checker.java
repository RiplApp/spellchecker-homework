/**
 * Program used to check capitalization and spelling errors. Each Checker object is passed through a text file with the dictonary words
 * in order to create myDict, a HashMap that contains all of the words in the dictionary and each words lengths.
 * After these parameters are given, checkWord is called to return the proper spelling or to indicate that word is not in the dictonary
 * and correction is not possible.
 *
 * Considerations and generalizations: Only one word would given to check for each method call to checkWord.
 * Incorrect capitalization can occur anywhere in the given String. Program will only correct capitalization if given mis-capitalized
 * word is in dictionary. Otherwise, program will return the closest word using the Levenshtein Distance algorithm. 
 *
 * External libraries: http://commons.apache.org/proper/commons-text/download_text.cgi
 * Other code repositories: https://coursework.cs.duke.edu/kiori.tanaka
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Apache Commons library downloaded in order to determine similarity between two words when correcting duplicate
 * words.
 */
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Checker {

    /**
     * HashMap of all words contained in zip file with word as key and length of word as value
     */
    private static HashMap<String,Integer> myDict;

    /**
     * Error message for when word is not found
     */
    private static final String ERROR_MSG = "No Correction Found.";

    public Checker(String dictFile) {
        myDict = getDataFile(dictFile);
    }

    /**
     *
     * @param fileName String for file name that contains all the dictionary words in it
     * @return HashMap with dictionary words as keys and dictionary words length as values
     */

    public static HashMap<String, Integer> getDataFile(String fileName) {
        HashMap<String, Integer> dictMap = new HashMap<>();
        ClassLoader classLoader = Checker.class.getClassLoader();
        String pathToDictFile = classLoader.getResource(fileName).getPath();
        File file = new File(pathToDictFile);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                dictMap.putIfAbsent(st, st.length());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dictMap;
    }

    /**
     *
     * @param wordToCheck String that you are checking for in the dictionary
     * @return String that corrects wordToCheck or error message if correction not possible
     */

    public static String checkWord(String wordToCheck) {
        if (wordToCheck.equals("") || wordToCheck == null) {
            throw new NullPointerException("No word given!");
        }

        // Word is correct
        if (myDict.containsKey(wordToCheck)) {
            return wordToCheck;
        }

        // Checking for capitalization error
        for (Map.Entry<String, Integer> dictEntry : myDict.entrySet()) {
            if (dictEntry.getKey().toLowerCase().equals(wordToCheck.toLowerCase())) {
                return dictEntry.getKey();
            }
        }

        // Create a HashMap that has letter occurrence of the wordToCheck
        HashMap<Character, Integer> givenWordLetterMap = letterOccurrence(wordToCheck);
        HashMap<String, Integer> possibleWords = new HashMap<>();

        // Checking one dictionary word
        for (Map.Entry<String, Integer> dictEntry : myDict.entrySet()) {
            String dictWord = dictEntry.getKey();

            // Checking only if the word has less letters than original to narrow down because givenWord with repeating letters will have more letters than the dict word we are considering
            if (dictWord.length() < wordToCheck.length()) {

                // Create a temporary Hashmap of letter occurrence in individual dictionary words
                HashMap<Character, Integer> dictWordLetterMap = letterOccurrence(dictWord);

                // Iterating through the letters of given word to compare and updating possible words
                if (dictWordLetterMap.keySet().equals(givenWordLetterMap.keySet())) {
                    boolean flag;

                    // Checking that each letter occurs more times in the dictionary word
                    for (Map.Entry<Character, Integer> givenWordEntry : givenWordLetterMap.entrySet()) {
                        if (givenWordEntry.getValue() > dictWordLetterMap.get(givenWordEntry.getKey())) {
                            flag = false;
                        }
                    }

                    // Using the Apache Commons Levenshtein Distance algorithm to record similarity degree for dictionary word and given word
                    if (flag = true) {
                        possibleWords.putIfAbsent(dictWord, LevenshteinDistance.getDefaultInstance().apply(dictWord, wordToCheck));
                    }
                }
            }
        }

        if (possibleWords == null) {
            return ERROR_MSG;
        }

        else {
            // Narrowing the closest word to wordToCheck
            Map.Entry<String, Integer> nearestEntry = null;
            for (Map.Entry<String, Integer> possibleWordsEntry : possibleWords.entrySet()) {
                if (nearestEntry == null || nearestEntry.getValue() > possibleWordsEntry.getValue()) {
                    {
                        nearestEntry = possibleWordsEntry;
                    }
                }
            }
            return nearestEntry.getKey();
        }
    }

    /**
     *
     * Helper method for checkWord that returns a Hashmap that records the instance of each letter in wordToCount
     * in order to determine if wordToCheck has repeating or unnecessary characters.
     *
     * @param wordToCount String that you are building your letter occurrence HashMap from
     * @return HashMap with letters as keys and occurrence of each letter as values
     */

    public static HashMap letterOccurrence(String wordToCount) {
        HashMap<Character, Integer> countLetters = new HashMap<>();
        for (int i = 0; i < wordToCount.length(); i++) {
            char letter = wordToCount.charAt(i);
            countLetters.putIfAbsent(Character.toLowerCase(letter),0);
            countLetters.put(Character.toLowerCase(letter),countLetters.get(Character.toLowerCase(letter))+1);
        }
        return countLetters;
    }


    public static void main(String[] args) throws Exception {
        // Testing purposes

        Checker checker = new Checker("words");
        System.out.println(checkWord("aani"));
        System.out.println(checkWord("aaardvark"));
        System.out.println(checkWord("hellooo"));
        System.out.println(checkWord("hhelloo"));
        System.out.println(checkWord(""));
    }

}
