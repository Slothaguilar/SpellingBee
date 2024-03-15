import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
//        words.add("c");
//        words.add("a");
//        words.add("b");
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method
        makeWords("", letters);

    }
    // Generate should call another method: a recursive method. This recursive method should
    // take in at least one parameter. It is okay for this method to use some iteration so long as it is still recursive.
    //You do not need to generate the words in any particular order, but you do need to generate
    // all of them.
    //It is okay to have duplicate words if the given letters contain duplicates. These duplicate
    // words will be removed later by the given method removeDuplicates()
    public void makeWords(String word, String letters) {
        words.add(word);

        for(int i = 0; i< letters.length(); i++){
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i+1));

        }

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        // recurive method
       words = mergesort(words);
//       for (int i = 0; i < words.size(); i++){
//           System.out.println(words.get(i));
//       }

    }
    private ArrayList<String> mergesort(ArrayList<String> arr) {
        if (arr.size() <= 1) {
            return arr;
        }

        int mid = arr.size() / 2;
        ArrayList<String> arr1 = new ArrayList<>(arr.subList(0, mid));
        ArrayList<String> arr2 = new ArrayList<>(arr.subList(mid, arr.size()));

        arr1 = mergesort(arr1);
        arr2 = mergesort(arr2);

        return merge(arr1, arr2);
    }

    private ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> merged = new ArrayList<>();
        int arr1Index = 0;
        int arr2Index = 0;

        while (arr1Index < arr1.size() && arr2Index < arr2.size()) {
            if (arr1.get(arr1Index).compareTo(arr2.get(arr2Index)) < 0) {
                merged.add(arr1.get(arr1Index++));
            } else {
                merged.add(arr2.get(arr2Index++));
            }
        }

        while (arr1Index < arr1.size()) {
            merged.add(arr1.get(arr1Index++));
        }

        while (arr2Index < arr2.size()) {
            merged.add(arr2.get(arr2Index++));
        }

        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int index = 0;
        while (index < words.size()-1) {
            String word = words.get(index);
            if (word.equals(words.get(index + 1)))
                words.remove(index + 1);
            else
                index++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        // goes through each word in the list
        // goes through each word in the list
        for (int i = 0; i< words.size(); i++) {
            if (!binarySearch(words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(words.get(i));
                i--;
            }
        }
        // Remove words not found in the dictionary
    }

    public boolean binarySearch(String word, int start, int end){
        if (start <= end){
            int mid = start + (end - start) / 2;
            int comparisonResult = word.compareTo(DICTIONARY[mid]);

            if (comparisonResult == 0) {
                return true; // Word found
            } else if (comparisonResult < 0) {
                return binarySearch(word, start, mid - 1); // Search left half
            } else {
                return binarySearch(word, mid + 1, end); // Search right half
            }
        }
        return false; // Word not found
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
