/**
 * Program used to check capitalization and spelling errors. Each Checker object is passed through a text file with the dictonary words
 * in order to create myDict, a HashMap that contains all of the words in the dictionary and each words lengths.
 * After these parameters are given, checkWord is called to return the proper spelling or to indicate that word is not in the dictonary
 * and correction is not possible.
 *
 * Considerations and generalizations: Only one word would given to check for each method call to checkWord.
 * Incorrect capitalization can occur anywhere in the given String.
 *
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Checker {

    /**
     * HashMap of all words contained in zip file with word as key and length of word as value
     */
    private static HashMap<String,Integer> myDict;

    /**
     * Error message for when word is not found
     */

    private static final String ERROR_MSG = "No Correction Found.";

    public Checker() {
        myDict = getDataFile();
    }

    public static void unzipFile(String zipName) {
        ClassLoader classLoader = Checker.class.getClassLoader();
        String path = classLoader.getResource(zipName).getPath();

        try (FileInputStream fis = new FileInputStream(path);
             ZipInputStream zis =
                     new ZipInputStream(new BufferedInputStream(fis))) {

            ZipEntry entry;

            // Read each entry from the ZipInputStream until no
            // more entry found indicated by a null return value
            // of the getNextEntry() method.
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Unzipping: " + entry.getName());

                int size;
                byte[] buffer = new byte[2048];

                try (FileOutputStream fos =
                             new FileOutputStream(entry.getName());
                     BufferedOutputStream bos =
                             new BufferedOutputStream(fos, buffer.length)) {

                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public static HashMap<String, Integer> getDataFile() {
        HashMap<String, Integer> hset = new HashMap<>();
        ClassLoader classLoader = Checker.class.getClassLoader();
        String path = classLoader.getResource("words").getPath();
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                hset.putIfAbsent(st, st.length());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hset;
    }

    /**
     *
     * @param wordToCheck String that you are checking for in the dictionary
     * @return String that corrects wordToCheck or error message if correction not possible
     */

    public static String checkWord(String wordToCheck) {
        // correct
        if (myDict.containsKey(wordToCheck)) {
            return wordToCheck;
        }
        // capitalization error
        for (Map.Entry<String, Integer> entry : myDict.entrySet()) {
            if (entry.getKey().toLowerCase().equals(wordToCheck.toLowerCase())){
                return entry.getKey();
            }
        }
        // check all of the words that have the same length when double is removed
        HashMap <Character, Integer> letterCountMap = letterOccurrence(wordToCheck);
        for (Map.Entry<String, Integer> entry : myDict.entrySet()) {
            if (entry.getKey().length() < wordToCheck.length()) {
                HashMap compareMap = letterOccurrence(entry.getKey()); //comparing with each word
                for (Map.Entry<Character, Integer> letterCountEntry : letterCountMap.entrySet()) {
                    if (compareMap.containsKey(letterCountEntry.getKey())) {
                        // if it contains the letter, check if the value is the same int
                        if ((int)compareMap.get(letterCountEntry.getKey()) < letterCountEntry.getValue()) {
                            return entry.getKey();
                        }
                    }
                }
            }
        }
        return ERROR_MSG;
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
        HashMap data = getDataFile();
        Checker checker = new Checker();
        System.out.println(checkWord("aani"));
        System.out.println(checkWord("hello"));
    }

}
