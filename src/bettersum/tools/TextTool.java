package bettersum.tools;

import java.io.Closeable;
import java.io.Serializable;

/**
 * The TextTool offers you a few of String operations
 * @since 1.0
 * @author Michael Rittmeister
 */
@SuppressWarnings("unused")
public class TextTool implements Serializable, Closeable {

    private String separator;

    /**
     * Constructs a TextTool with the default separator " "
     * @see this#TextTool(String) 
     */
    public TextTool() {
        this(" ");
    }

    /**
     * Constructs a TextTool with a specified separator
     * @param separator the separator that is used to split Strings into words
     */
    public TextTool(String separator) {
        this.separator = separator;
    }

    /**
     * Setter for the separator
     * @param separator The new value for the separator
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Returns the length of a string
     * @param string the String which's length is needed
     * @return the length as an integer
     */
    public int length(String string) {
        return string.length();
    }

    /**
     * Returns the char at a specific position
     * @param string The string from that the char is needed
     * @param position the position of the char
     * @return the char at the specified position
     */
    public char charAt(String string, int position) {
        return string.charAt(position);
    }

    /**
     * Returns the word at a specific position
     * @param string The string with the word in it
     * @param wordIndex the position of the word
     * @return The word at the specified position separated by the separator
     * @throws ArrayIndexOutOfBoundsException when there is no word at the specified position
     */
    public String wordAt(String string, int wordIndex) {
        String[] array = string.split(separator);
        if (wordIndex > array.length)
            throw new ArrayIndexOutOfBoundsException("There is no word at the specified position");
        return array[wordIndex];
    }

    /**
     * Counts the words separated by the separator
     * @param string The string from which the words should be counted
     * @return The count of the words in the String
     */
    public int countWords(String string) {
        return string.split(separator).length;
    }

    /**
     * Returns a specific part of a string
     * @param string The string that needs to be modified
     * @param from The start of the new string
     * @param to The stop of the new string
     * @return The modified string
     */
    public String subString(String string, int from, int to) {
        return string.substring(from, to);
    }

    /**
     * Returns a specific part of a string
     * @param string The old string
     * @param from The position where the new string should start
     * @param to The position where the new string should end
     * @return The parts from position1 to position2 of the specified string
     */
    public String substring(String string, int from, int to) {
        return subString(string, from, to);
    }

    /**
     * Returns the position of a string in another string
     * @param string The string with the query in it
     * @param query The query from which the position is needed
     * @return the position of the query in the string
     */
    public int positionOf(String string, String query) {
        return string.indexOf(query);
    }

    /**
     * Extracts a substring from a string
     * @param string The whole string
     * @param from The start of the substring that should be extracted
     * @param to The end of the substring that should be extracted
     * @return The string without the substring
     * @see this#textWithout(String, String)
     */
    public String textWithout(String string, int from, int to) {
        return textWithout(string, string.substring(from, to));
    }

    /**
     * Extracts a string from another string
     * @param string The string from which the other string should be extracted
     * @param extract The string that should be extracted
     * @return The string without the substring
     */
    public String textWithout(String string, String extract) {
        return string.replace(extract, "");
    }

    /**
     * Inserts some text into a string at a specific position
     * @param string The string that needs to be modified
     * @param insert The String that needs to be inserted
     * @param position The position of the insertion
     * @return The modified String
     */
    public String textWith(String string, String insert, int position) {
        String out = "";
        if (position > 1)
            out = string.substring(0, position);
        out = out + insert;
        out = out + string.substring(position);
        return out;
    }

    /**
     * Turns a String into a lower-cased String
     * @param string The string that needs to be lower-cased
     * @return the lower-cased String
     */
    public String toLowerCase(String string) {
        return string.toLowerCase();
    }

    /**
     * Just an alias because of IDK
     * @see this#toLowerCase(String)
     */
    public String tolowercase(String string) {
        return toLowerCase(string);
    }

    /**
     * Turns a String into a upper-cased String
     * @param string The string that needs to be upper-cased
     * @return the upper-cased String
     */
    public String toUpperCase(String string) {
        return string.toUpperCase();
    }

    /**
     * Just an alias because of IDK
     * @see this#toUpperCase(String)
     */
    public String touppercase(String string) {
        return toUpperCase(string);
    }

    /**
     * Tests if two Strings are equal or not
     * @param string The first string
     * @param otherString The other string
     * @return If the strings are equal or not
     */
    public boolean equals(String string, String otherString) {
        return string.equals(otherString);
    }

    /**
     * Tests if two Strings are equal or not
     * @param string The first string
     * @param otherString The other string
     * @return Wether the first string is shorter or not
     */
    public boolean isShorter(String string, String otherString) {
        return string.compareTo(otherString) < 0;
    }

    /**
     * Tests if two Strings are equal or not
     * @param string The first string
     * @param otherString The other string
     * @return Wether the first string is longer or not
     */
    public boolean isLonger(String string, String otherString) {
        return string.compareTo(otherString) > 0;
    }

    /**
     * Checks if a String is a double
     * @param string The string that needs to be checked
     * @return Wether the string is a double or not
     */
    public boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a String is an integer
     * @param string The string that needs to be checked
     * @return Wether the string is an integer or not
     */
    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * @see this#isInteger(String)
     */
    public boolean isInteger(char string) {
        return isInteger("" + string);
    }

    /**
     * Checks if a String is a long
     * @param string The string that needs to be checked
     * @return Wether the string is a long or not
     */
    public boolean isLong(String string) {
        try {
            Long.parseLong(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Converts a string into a double
     * @param string The string that needs to be converted
     * @return The string converted to a double
     * @throws ArithmeticException If the string ist not a double
     */
    public double parseDouble(String string) {
        if (isDouble(string))
            return Double.parseDouble(string);
        throw new ArithmeticException("string must be double");
    }

    /**
     * @see this#asInteger(String)
     */
    public int asInteger(char string) {
        return asInteger("" + string);
    }

    /**
     * Converts a string into an integer
     * @param string The string that needs to be converted
     * @return The string converted to an integer
     * @throws ArithmeticException If the string ist not
     */
    public int asInteger(String string) {
        if (isInteger(string))
            return Integer.parseInt(string);
        throw new ArithmeticException("string must be integer");
    }
    /**
     * Converts a string into a long
     * @param string The string that needs to be converted
     * @return The string converted to a long
     * @throws ArithmeticException If the string ist not a long
     */
    public long asLong(String string) {
        if (isLong(string))
            return Long.parseLong(string);
        throw new ArithmeticException("string must be long");
    }

    /**
     * @see this#asLong(String)
     */
    public long asLong(char string) {
        return asLong("" + string);
    }

    /**
     * Converts a double into a string
     * @param number The double
     * @return The double converted into a string
     */
    public String asString(double number) {
        return "" + number;
    }

    /**
     * Converts a integer into a string
     * @param number The double
     * @return The integer converted into a string
     */
    public String asString(int number) {
        return "" + number;
    }

    /**
     * Combines strings into one string
     * @param strings All strings that needs to be combined
     * @return all strings combined into one string
     */
    public String combineStrings(String... strings) {
        StringBuilder out = new StringBuilder();
        for (String string : strings) {
            out.append(string);
        }
        return out.toString();
    }

    /**
     * This method is only here because the author of the original library likes empty methods
     */
    @Override
    public void close() {

    }
}
