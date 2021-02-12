package commands;

/**
 * This class handles command: echo "String" > OUTFILE.
 * 
 * @author xushengsong
 */
public class OverWriteFile extends UnitCommand {
  private AppendFile af = new AppendFile();

  /**
   * This is a helper method for executeCommand.
   * 
   * @param args
   *          input arguments
   * @return String representation of args if args if valid, an error message
   *         otherwise
   */
  public String executeOverWrite(String[] args) {
    return af.executeEcho(args, ">");
  }

  /**
   * Execute command: echo "String" > OUTFILE.
   * 
   * @param args
   *          user inputs
   */
  public void executeCommand(String[] args) {
    String msg = this.executeOverWrite(args);
    if (!msg.equals("")) {
      sendErrMsg(msg);
    }
  }

}
