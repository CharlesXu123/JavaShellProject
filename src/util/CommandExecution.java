package util;

import commands.UnitCommand;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Calls executeCommand() method for the user-called command.
 *
 * @author kanghung
 */
public class CommandExecution {

  /**
   * This calls executeCommand() method in each command class only if user provided valid command to
   * call. i.e.) parsedUserInput[0] is valid command and exists in commandHashMap.
   *
   * @param parsedUserInput user input parsed by Parser.parseUserInput method.
   */
  public static void callCommand(String[] parsedUserInput) {

    Class<?> cmdClass = returnCmdClass(parsedUserInput);

    try {
      String[] args = Arrays.copyOfRange(parsedUserInput, 1, parsedUserInput.length);
      UnitCommand cmd = (UnitCommand) cmdClass.getDeclaredConstructor().newInstance();

      if (isInArrStr(parsedUserInput, ">>") || isInArrStr(parsedUserInput, ">")) {
        // redirection usage
        cmd.setOutFilePath(args[args.length - 1]);
        if (args.length - 2 >= 0) {
          args = Arrays.copyOfRange(args, 0, args.length - 2);
        }
      }
      if (isInArrStr(parsedUserInput, ">>")) {
        cmd.setRedirectionFlag(">>");
      } else if (isInArrStr(parsedUserInput, ">")) {
        cmd.setRedirectionFlag(">");
      }
      cmd.executeCommand(args);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException | InstantiationException e) {
      // should not happen, ever
    }
  }

  /**
   * A helper method to return the correct Class of the user-called command.
   *
   * @param parsedUserInput parsed user-input in string array by the Parser.
   * @return Class corresponding class of the command that user called.
   */
  private static Class<?> returnCmdClass(String[] parsedUserInput) {

    String cmdClassName = returnCommandClassName(parsedUserInput);

    try {
      return Class.forName("commands." + cmdClassName);
    } catch (ClassNotFoundException e) {
      // will be handled by Parser
      return null; // to suppress Java's warnings
    }
  }


  /**
   * A helper method to return the value of commandHashMap with given key.
   *
   * @param parsedUserInput parsed userInput in form of string array, containing the key as the
   *        first item.
   * @return String the name of the valid command class in string.
   */
  public static String returnCommandClassName(String[] parsedUserInput) {
    HashMap<String, String> commandHashMap = new HashMap<>();
    initializeCommandHashMap(commandHashMap);
    return commandHashMap.get(parsedUserInput[0]);
  }


  /**
   * A helper method to initialize commandHashMap to use in returnCommandClassName() method.
   *
   * @param commandHashMap HashMap to initialize.
   */
  private static void initializeCommandHashMap(HashMap<String, String> commandHashMap) {

    // bit difficult to map <String, Class> because of multiple cases with
    // single key, "echo" :(
    commandHashMap.put("exit", "Exit");
    commandHashMap.put("mkdir", "MakeDirectory");
    commandHashMap.put("cd", "ChangeDirectory");
    commandHashMap.put("ls", "List");
    commandHashMap.put("pwd", "PrintWorkingDirectory");
    commandHashMap.put("pushd", "PushDirectory");
    commandHashMap.put("popd", "PopDirectory");
    commandHashMap.put("history", "History");
    commandHashMap.put("cat", "Concatenation");
    commandHashMap.put("man", "Manual");
    commandHashMap.put("echo", "Echo");
    commandHashMap.put("rm", "Remove");
    commandHashMap.put("mv", "Move");
    commandHashMap.put("cp", "Copy");
    commandHashMap.put("curl", "ClientUrl");
    commandHashMap.put("saveJShell", "SaveJShell");
    commandHashMap.put("loadJShell", "LoadJShell");
    commandHashMap.put("search", "Search");
    commandHashMap.put("tree", "Tree");
  }



  /**
   * A helper method to determine if specific sequence of characters is inside given string array.
   *
   * @param stringArr string array to find a string.
   * @param itemToFind a string of character(s) to find inside StringArr
   * @return Boolean true iff itemToFind is inside stringArr.
   */
  private static Boolean isInArrStr(String[] stringArr, String itemToFind) {
    for (String each : stringArr) {
      if (each.contains(itemToFind)) {
        return true;
      }
    }
    return false;
  }
}
