package test;

import static org.junit.Assert.assertEquals;

import commands.Manual;
import org.junit.Test;

/**
 * Test for man command. This tests run() method instead of executeCommand()
 * since run() returns the output in string where executeCommand() calls run()
 * and simply sends the output to selected stream if valid args were provided.
 *
 * @author kanghung
 */
public class ManualTest {

  /**
   * Expected string of documentation.
   */
  private String expected;

  /**
   * Array of arguments to pass to man command.
   */
  private String[] args;

  /**
   * Return the actual output of man command in string.
   *
   * @return String Actual output of currently implemented man command
   */
  private String getActual() {
    return Manual.run(this.args);
  }

  /**
   * A helper method to run Junit Test that matches how man command outputs.
   *
   * @param expectedString Expected string of man command
   * @param cmdName        Valid command name string in question
   */
  private void setExpectedAndArgsAndTest(String expectedString,
      String cmdName) {
    if (expectedString != null) {
      this.expected = "\n" + expectedString;
    } else {
      this.expected = null;
    }
    args = new String[1];
    args[0] = cmdName;
    assertEquals(expected, getActual());
  }

  /**
   * Invalid command.
   */
  @Test
  public void invalidCmd() {
    setExpectedAndArgsAndTest(null, "abc");
  }

  /**
   * Multiple commands. This is now invalid in A2b.
   */
  @Test
  public void multipleCmds() {
    args = new String[3];
    args[0] = "cd";
    args[1] = "man";
    args[2] = "echo";

    assertEquals(null, Manual.run(this.args));
  }


  /**
   * Command cd.
   */
  @Test
  public void cd() {
    commands.ChangeDirectory cd = new commands.ChangeDirectory();
    setExpectedAndArgsAndTest(cd.toString(), "cd");
  }

  /**
   * Command cat.
   */
  @Test
  public void cat() {
    commands.Concatenation cat = new commands.Concatenation();
    setExpectedAndArgsAndTest(cat.toString(), "cat");
  }

  /**
   * Command copy.
   */
  @Test
  public void cp() {
    commands.Copy cp = new commands.Copy();
    setExpectedAndArgsAndTest(cp.toString(), "cp");
  }

  /**
   * Command echo.
   */
  @Test
  public void echo() {
    commands.Echo echo = new commands.Echo();
    setExpectedAndArgsAndTest(echo.toString(), "echo");
  }

  /**
   * Command exit.
   */
  @Test
  public void exit() {
    commands.Exit exit = new commands.Exit();
    setExpectedAndArgsAndTest(exit.toString(), "exit");
  }

  /**
   * Command history.
   */
  @Test
  public void history() {
    commands.History hs = new commands.History();
    setExpectedAndArgsAndTest(hs.toString(), "history");
  }

  /**
   * Command list.
   */
  @Test
  public void list() {
    commands.List ls = new commands.List();
    setExpectedAndArgsAndTest(ls.toString(), "ls");
  }

  /**
   * Command loadJShell.
   */
  @Test
  public void loadJShell() {
    commands.LoadJShell ljs = new commands.LoadJShell();
    setExpectedAndArgsAndTest(ljs.toString(), "loadJShell");
  }

  /**
   * Command MakeDirectory.
   */
  @Test
  public void mkdir() {
    commands.MakeDirectory mk = new commands.MakeDirectory();
    setExpectedAndArgsAndTest(mk.toString(), "mkdir");
  }

  /**
   * Command man.
   */
  @Test
  public void man() {
    commands.Manual man = new commands.Manual();
    setExpectedAndArgsAndTest(man.toString(), "man");
  }

  /**
   * Command move.
   */
  @Test
  public void move() {
    commands.Move mv = new commands.Move();
    setExpectedAndArgsAndTest(mv.toString(), "mv");
  }

  /**
   * Command popDir.
   */
  @Test
  public void popd() {
    commands.PopDirectory popd = new commands.PopDirectory();
    setExpectedAndArgsAndTest(popd.toString(), "popd");
  }

  /**
   * Command pwd.
   */
  @Test
  public void pwd() {
    commands.PrintWorkingDirectory pwd = new commands.PrintWorkingDirectory();
    setExpectedAndArgsAndTest(pwd.toString(), "pwd");
  }

  /**
   * Command pushd.
   */
  @Test
  public void pushd() {
    commands.PushDirectory pushd = new commands.PushDirectory();
    setExpectedAndArgsAndTest(pushd.toString(), "pushd");
  }

  /**
   * Command saveJShell.
   */
  @Test
  public void saveJShell() {
    commands.SaveJShell sjs = new commands.SaveJShell();
    setExpectedAndArgsAndTest(sjs.toString(), "saveJShell");
  }

  /**
   * Command search.
   */
  @Test
  public void search() {
    commands.Search sch = new commands.Search();
    setExpectedAndArgsAndTest(sch.toString(), "search");
  }

  /**
   * Command tree.
   */
  @Test
  public void tree() {
    commands.Tree t = new commands.Tree();
    setExpectedAndArgsAndTest(t.toString(), "tree");
  }
}
