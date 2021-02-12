package commands;

import filesystem.Directory;
import filesystem.UnitFileSystemComponent;
import java.util.ArrayList;

/**
 * Implementation of tree command.
 *
 * @author kanghung
 */
public class Tree extends UnitCommand {


  /**
   * Return the documentation of tree command.
   *
   * @return String
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for Tree (tree) >");
    doc.append("\nUsage: tree");
    doc.append("\nDisplay the entire file system as a tree.");
    doc.append(" Sub-components will be indented by a");
    doc.append(" tab character for every level of the tree.");
    return doc.toString();
  }

  /**
   * A helper method to recursively traverse the mock file system and return a
   * tree representation in string. If verbose is set to true, this will print
   * "file"/"directory" after the name of each node per line.
   *
   * @param depth   depth of recursion
   * @param node    File/Dir node to traverse
   * @param verbose if true, will also include type after the name of a node
   * @return String tree representation of the mock file system
   */
  protected static String treeHelper(int depth, UnitFileSystemComponent node,
      Boolean verbose) {
    String indent = "";
    for (int i = 0; i < depth; i++) {
      indent += "\t";
    }

    String finalStr = indent + node.getName();
    if (Boolean.FALSE.equals(verbose)) {
      finalStr += "\n";
    } else {
      finalStr += " " + node.getType() + "\n";
    }

    // base case: file OR empty dir
    if (node.getType().equals("file")
        || (((Directory) node).getComponents() != null
            && ((Directory) node).getComponents().isEmpty())) {
      return finalStr;
    }

    // recursive case: dir with sub-components
    ArrayList<UnitFileSystemComponent> currDirComps =
        ((Directory) node).getComponents();
    for (int i = 0; i < currDirComps.size(); i++) {
      finalStr += treeHelper(depth + 1, currDirComps.get(i), verbose);
    }
    return finalStr;
  }


  /**
   * A helper method to return an output of Tree command as a string. This
   * method can be tested instead of executeCommand()
   *
   * @param args arguments by the user
   * @return String output of Tree command
   */
  public String run(String[] args) {
    if (args.length != 0) {
      sendErrMsg("Too many arguments");
      return null;
    } else {
      return treeHelper(0, fs.getRoot(), false);
    }
  }

  /**
   * Output tree representation of the mock file system.
   *
   * @param args should be an array of length zero
   */
  @Override
  public void executeCommand(String[] args) {
    String output = run(args);
    sendOutput(output);
  }
}
