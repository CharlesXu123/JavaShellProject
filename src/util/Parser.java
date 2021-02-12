package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Split user inputs by whitespace and double quotes for JShell to understand
 * the user inputs.
 *
 * @author kanghung
 */
public final class Parser {

  /**
   * Constant for redirection when appending to a OUTFILE.
   */
  private static final String DOUBLE_ARROW = ">>";

  /**
   * Constant for redirection when overwriting to a OUTFILE.
   */
  private static final String SINGLE_ARROW = ">";

  /**
   * String literal for invalid input error message.
   */
  private static final String INVAL_INPUT_MSG = "Invalid input";

  /**
   * Separate UserInput by whitespace except the input wrapped with double
   * quotes. If there is invalid command name in userInput, print an error
   * message and return empty array of string. If userInput contains odd number
   * of double quotes, print an error message and return empty array or string.
   *
   * @param userInput what user typed to JShell
   * @return String[] Array of string in a format: {"CommandName", "Argument_1",
   *         "Argument_2", ...} if userInput is valid
   * 
   */
  public static String[] parseUserInput(String userInput) {
    ArrayList<String> parsed = new ArrayList<>();

    // check the number of double quote chars
    int count = returnDoubleQuoteCount(userInput);
    if (count > 0 && count != 2) {
      // wrong usage of double quotes
      return getEmptyStrArr(INVAL_INPUT_MSG);
    } else if (count == 0) {
      // no quotes, process white space!
      String[] tmp = userInput.split("\\s+");
      List<String> temp = Arrays.asList(tmp);
      parsed.addAll(temp);
    } else {
      // correct usage of double quotes
      // process quotes, array with the size of three since two quote chars
      String[] separatedByQuotes = userInput.split("\"");
      processQuotes(separatedByQuotes, parsed);
    }
    // at last, check if there's white space in the beginning
    if (userInput.isBlank()) {
      return getEmptyStrArr("");
    } else if (parsed.get(0).equals("")) {
      parsed.remove(0);
    }

    // process redirection
    // check for redirection usage
    if (isValidArrowUsage(parsed)) {
      parseRedirection(parsed);
    } else {
      // invalid, should not have any redirection arrows
      for (String each : parsed) {
        if (each.contains(SINGLE_ARROW)) {
          return getEmptyStrArr(INVAL_INPUT_MSG);
        }
      }
    }

    // check for invalid commands
    if (!parsed.isEmpty() && !(isValidCommand(parsed.get(0)))) {
      // invalid command => call error handle => return empty array
      return getEmptyStrArr(
          "\'" + parsed.get(0) + "\'" + " is invalid command");
    }

    return parsed.toArray(new String[parsed.size()]);
  }


  /**
   * A helper method to interpret redirection-arrow inside userInput for any
   * command that has output (not error) whether there's whitespace in between
   * the arrow or not.
   * <p>
   * Precondition: parsed has an item of str that contains ">" or ">>" only
   * once.
   * </p>
   *
   * @param parsed ArrayList of string from parseUserInput()
   */
  private static void parseRedirection(ArrayList<String> parsed) {
    // loop till first occurrence
    int i = 0;
    for (i = 0; i < parsed.size(); i++) {
      if (parsed.get(i).contains(SINGLE_ARROW)) {
        break;
      }
    }
    String strWithArrow = parsed.get(i);
    // only check for non-trivial case:
    if (!(strWithArrow.equals(SINGLE_ARROW)
        || strWithArrow.equals(DOUBLE_ARROW))) {
      parseRedirectionHelper(strWithArrow, parsed);
    }
  }

  /**
   * A helper method to process non-trivial case of redirection usage.
   *
   * @param strWithArrow Str that contains an arrow symbol in between or at
   *                     either end of str
   * @param parsed       ArrayList of str passed from parseUserInput()
   */
  private static void parseRedirectionHelper(String strWithArrow,
      ArrayList<String> parsed) {
    // case: arrow at the start (first two) + arrow at the end (next two)
    if (strWithArrow.startsWith(DOUBLE_ARROW)) {
      modifyWithSubStr(parsed, DOUBLE_ARROW, strWithArrow,
          DOUBLE_ARROW.length(), strWithArrow.length(), 0);
    } else if (strWithArrow.startsWith(SINGLE_ARROW)) {
      modifyWithSubStr(parsed, SINGLE_ARROW, strWithArrow,
          SINGLE_ARROW.length(), strWithArrow.length(), 0);
    } else if (strWithArrow.endsWith(DOUBLE_ARROW)) {
      modifyWithSubStr(parsed, DOUBLE_ARROW, strWithArrow, 0,
          strWithArrow.length() - DOUBLE_ARROW.length(), 1);
    } else if (strWithArrow.endsWith(SINGLE_ARROW)) {
      modifyWithSubStr(parsed, SINGLE_ARROW, strWithArrow, 0,
          strWithArrow.length() - SINGLE_ARROW.length(), 1);
    } else {
      // arrow in between
      String[] tmp;
      String arrowUsed;
      if (strWithArrow.contains(DOUBLE_ARROW)) {
        tmp = strWithArrow.split(DOUBLE_ARROW);
        arrowUsed = DOUBLE_ARROW;
      } else {
        tmp = strWithArrow.split(SINGLE_ARROW);
        arrowUsed = SINGLE_ARROW;
      }
      int i = parsed.indexOf(strWithArrow);
      parsed.remove(i);
      parsed.add(i, tmp[0]);
      parsed.add(i + 1, arrowUsed);
      parsed.add(i + 2, tmp[1]);
    }
  }

  /**
   * A helper method to modify ArrayList parsed such that strWithArrow is
   * separated from redirection symbol (arrow).
   *
   * @param parsed       ArrayList of str passed from parseUserInput()
   * @param arrow        Either ">" or ">>"
   * @param strWithArrow Str that contains an arrow symbol in between or at
   *                     either end of str
   * @param beginIndex   Index to start substring()
   * @param endIndex     Index to end substring()
   * @param insertDelta  Distance from separated str to insert the arrow symbol
   */
  private static void modifyWithSubStr(ArrayList<String> parsed, String arrow,
      String strWithArrow, int beginIndex, int endIndex, int insertDelta) {
    int i = parsed.indexOf(strWithArrow);
    String sub = strWithArrow.substring(beginIndex, endIndex);
    if (sub.equals(SINGLE_ARROW)) {
      parsed.removeAll((Collection<String>) parsed);
      return;
    }
    parsed.remove(i);
    parsed.add(i, sub);
    parsed.add(i + insertDelta, arrow);
  }

  /**
   * A helper method to check for valid arrow-redirection usage for any command
   * that has output. If user input has only one valid redirection arrow that is
   * not at the end, return true. Return false when there's no use of
   * redirection arrows.
   *
   * @param parsed passed ArrayList of str
   * @return Boolean true iff valid arrow usage
   */
  public static Boolean isValidArrowUsage(ArrayList<String> parsed) {
    // check for invalid usage of redirection: arrow at the end
    if (parsed.get(parsed.size() - 1).endsWith(SINGLE_ARROW)) {
      return false;
    }

    int singleCnt = 0;
    int doubleCnt = 0;
    // check for number of occurences of arrowUsed for each splitted str
    for (String each : parsed) {
      singleCnt += countArrow(each, SINGLE_ARROW);
      doubleCnt += countArrow(each, DOUBLE_ARROW);
    }
    return ((singleCnt == 2 && doubleCnt == 1)
        || (singleCnt == 1 && doubleCnt == 0));
  }


  private static int countArrow(String str, String arrow) {
    if (str.equals(arrow)) {
      return 1;
    } else if (str.endsWith(arrow)) {
      str += "foo";
    }
    return str.split(arrow).length - 1;
  }

  /**
   * A helper method to identify valid command names from UserInput.
   *
   * @param userInputCmd parsed commandName from the userInput
   * @return Boolean true iff userInputCommand is a valid command for A2a
   */
  private static Boolean isValidCommand(String userInputCmd) {
    ArrayList<String> validCmds = new ArrayList<>();
    validCmds.add("exit");
    validCmds.add("mkdir");
    validCmds.add("cd");
    validCmds.add("ls");
    validCmds.add("pwd");
    validCmds.add("pushd");
    validCmds.add("popd");
    validCmds.add("history");
    validCmds.add("cat");
    validCmds.add("echo");
    validCmds.add("man");
    validCmds.add("rm");
    validCmds.add("mv");
    validCmds.add("cp");
    validCmds.add("curl");
    validCmds.add("saveJShell");
    validCmds.add("loadJShell");
    validCmds.add("search");
    validCmds.add("tree");
    Collections.sort(validCmds);
    return Collections.binarySearch(validCmds, userInputCmd) >= 0;
  }

  /**
   * A helper method to get an empty array of string and print an error message.
   *
   * @param msg error message
   * @return String[] empty array
   */
  private static String[] getEmptyStrArr(String msg) {
    if (!msg.equals("")) {
      ErrorHandler.getErrorMessage(msg);
    }
    return new String[0];
  }

  /**
   * A helper method to count the number of double quotes in a string.
   *
   * @param strIn a string to count the number of double quotes from
   * @return Integer number of double quotes inside the string strIn
   */
  private static Integer returnDoubleQuoteCount(String strIn) {
    int count = 0;
    for (int i = 0; i < strIn.length(); i++) {
      if (strIn.charAt(i) == '\"') {
        count++;
      }
    }
    return count;
  }

  /**
   * A helper method to process UserInput containing double quotes.
   *
   * @param separatedInput array of strings splitted by double quotes
   * @param parsedList     ArrayList to add parsed inputs
   */
  private static void processQuotes(String[] separatedInput,
      ArrayList<String> parsedList) {
    for (int i = 0; i < separatedInput.length; i++) {
      if (i % 2 == 0) {
        // separate by whitespace
        String[] tmp = separatedInput[i].split("\\s+");
        for (String each : tmp) {
          // check if there's white space in the beginning
          if (!each.equals("")) {
            parsedList.add(each);
          }
        }
      } else {
        // quoted string part, do not remove any whitespace here
        // also do not remove quote characters
        String includeQuotes = "\"" + separatedInput[i] + "\"";
        parsedList.add(includeQuotes);
      }
    }
  }
}
