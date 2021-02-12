// **********************************************************
// Assignment2:
// Student1: (Dropped CSCB07 during sprint 3)
// UTORID user_name: guirgisj
// UT Student #: 1005977819
// Author: John Guirgis
//
// Student2:
// UTORID user_name: kanghung
// UT Student #: 1006356408
// Author: Hungi Kang
//
// Student3:
// UTORID user_name: xushengs
// UT Student #: 1005788970
// Author: Shengsong Xu
//
// Student4:
// UTORID user_name: tanluowe
// UT Student #: 1005789407
// Author: Luowei Tan
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************

package driver;

import filesystem.HistoryArray;
import java.util.Scanner;
import util.CommandExecution;
import util.Parser;

/**
 * Driver for the JShell.
 *
 * @author guirgisj
 * @author kanghung
 * @author xushengs
 */
public class JShell {
  /**
   * Scanner to get user inputs for JShell.
   */
  public static Scanner scanner;

  /**
   * Main method to start a new session of JShell.
   *
   * @param args can be ignored
   */
  public static void main(String[] args) {
    HistoryArray ha = HistoryArray.createHistoryArray();
    scanner = new Scanner(System.in);
    String input = "";

    do { // prompt user for input
      displayIndicator();
      input = scanner.nextLine();
      ha.addHistory(input);
      String[] parsedInput = Parser.parseUserInput(input);

      // execute the command here using parsed input
      if (parsedInput.length != 0) {
        // parsed input has valid command to execute
        CommandExecution.callCommand(parsedInput);
      }
    } while (true);
  }

  /**
   * Display an indicator of our JShell to the user.
   */
  private static void displayIndicator() {
    System.out.print("$ ");
  }

}
