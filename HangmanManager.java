// Christian Hahn
// CSE 143 - Section DE 
// TA: Sarah House
// 24 October 2016
// A HangmanManager class that keeps track of the state of an evil hangman
// game which delays picking a word until the computer is forced to. 

import java.util.*;

public class HangmanManager {
   
   // number of guesses in the alphabet the user has
   private int numGuesses;
   // the set of guesses the user has made
   private Set<Character> charGuesses;
   // a map which maps each pattern of dashes and correct guesses to a set
   // of words containing the pattern
   private Map<String, Set<String>> map;
   // the current pattern of dashes and correct guesses based on the users
   // guesses
   private String currentPattern;
   
   // passed a list of strings to use for the game, the integer length of the 
   // word picked, and the integer max amount of guesses the user is allowed to 
   // make
   // creates the state of the game by adding all the words of the given length 
   // into a list of strings and stores that in alphabetical order to     
   // correspond to the current pattern of dashes and letters
   // throws an IllegalArgumentException if the length is less than one or the
   // max is less than 0
   public HangmanManager(List<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      this.numGuesses = max;
      this.charGuesses = new TreeSet<Character>();
      this.map = new TreeMap<String, Set<String>>();
      this.currentPattern = "";
      Set<String> words = new TreeSet<String>();
      for (String s: dictionary) {
         if (s.length() == length) {
            words.add(s);
         }
      }
      for (int i = 0; i < length; i++) {
         this.currentPattern += "-";
      }
      this.map.put(currentPattern, words);
   }
   
   // returns the current set of strings that contains the words being    
   // considered by the HangmanManger class in alphabetical order
   public Set<String> words() {
      return this.map.get(this.currentPattern);
   }
   
   // returns the integer number of guesses the user has left
   // if the user guesses correctly or the same letter, the number of guesses
   // stay the same
   public int guessesLeft() {
      return this.numGuesses;
   }
   
   // returns the set of characters of letters that the user has guessed so 
   // far in alphabetical order
   public Set<Character> guesses() {
      return this.charGuesses;
   }
   
   // returns a string of the current pattern of dashes and letters that
   // the user put in the correct spot in the word
   // throws an IllegalStateException if set of words is empty
   public String pattern() {
      if (this.words().isEmpty()) {
         throw new IllegalStateException();
      }
      return this.currentPattern; 
   }
   
   // accepts a character in the form of a letter that the user has guessed
   // and makes a pattern based on the letter and the words left in the list
   // returns an integer documenting the number of times the letter appears in 
   // the word and decerements the number of guesses the user has left if the 
   // letter does not appear in the word
   // throws IllegalStateException if there are 0 guesses left or if the set of
   // words is empty
   // throws IllegalArgumentException if the set of words is not empty and 
   // the user has already guessed the letter   
   public int record(char guess) {
      if (guessesLeft() < 1 || this.words().isEmpty()){
         throw new IllegalStateException();
      } if (!this.words().isEmpty() && this.charGuesses.contains(guess)) {
         throw new IllegalArgumentException();
      }
      this.charGuesses.add(guess);
      getPatternAndSets(this.currentPattern, guess);
      findLargestSet();
      int numOccurrences = getOccurrences(this.currentPattern, guess);
      if (numOccurrences == 0) {
         this.numGuesses--;
      }
      return numOccurrences;
   }
   
   // passed in the string current pattern of dashes and letters and the 
   // character that is the letter that the user has guessed to determine
   // the new pattern of letters and dashes based on how many occurrences
   // of the guessed letter is in the word
   private void getPatternAndSets(String pattern, char guess) {
      Set<String> temp = this.map.get(pattern);
      // removes old set of words with the old pattern
      this.map.remove(pattern); 
      for (String s : temp) { 
         String tempPattern = "";
         for (int i = 0; i < s.length(); i++) {
            if (guess == s.charAt(i)) {
               tempPattern += guess;
            } else {
               tempPattern += pattern.charAt(i);
            }
         }
         Set<String> tempSet;
         if (this.map.containsKey(tempPattern)) {
            tempSet = this.map.get(tempPattern);
         } else {
            tempSet = new TreeSet<String>();
         }
         tempSet.add(s);
         // puts in new set of words mapped to the new pattern
         this.map.put(tempPattern, tempSet);
      }
   }
   
   // finds the set of words with the largest number of words to delay picking
   // a word  
   private void findLargestSet() {
      int maxSize = 0;
      String tempKey = "";
      for (String i : this.map.keySet()) {
         if (this.map.get(i).size() > maxSize) {
            maxSize = this.map.get(i).size();
            tempKey = i;
            this.currentPattern = tempKey;
         }
      }
      Map<String, Set<String>> newMap = new TreeMap<String, Set<String>>();
      newMap.put(tempKey, this.map.get(tempKey));
      this.map = newMap;
   }
   
   // takes in a string that is the current pattern of words and letters    
   // and the character letter the user has guessed
   // returns an integer that is the number of times the letter appears in
   // the word
   private int getOccurrences(String word, char guess) {
      int count = 0;
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            count++;
         }
      }
      return count;
   }
}


