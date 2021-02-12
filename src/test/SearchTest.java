package test;

import static org.junit.Assert.assertEquals;

import commands.Search;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Search command.
 *
 * @author kanghung
 */
public class SearchTest {

  /**
   * Simulate parsed userInput.
   */
  private ArrayList<String> userInputs;

  /**
   * Expected output of search command.
   */
  private StringBuilder expectedStr;

  /**
   * Search object.
   */
  private Search search;

  /**
   * Mock FS instance.
   */
  private FileSystem fs;

  /**
   * Search param constant.
   */
  private static final String PARAM_TYPE = "-type";

  /**
   * Search param constant.
   */
  private static final String PARAM_NAME = "-name";

  /**
   * Search param constant.
   */
  private static final String D_TYPE = "d";

  /**
   * Search param constant.
   */
  private static final String F_TYPE = "f";

  /**
   * Default name for a file in common.
   */
  private static final String DEF_F_NAME = "defFile";

  /**
   * Default name for a directory in common.
   */
  private static final String DEF_D_NAME = "defDir";

  /**
   * Constant for forward slash.
   */
  private static final String FORWARD_SLASH = "/";


  /**
   * Setup each test and setup FileSystem before each test.
   * <p>
   * (level 1)
   * /
   * /defFile
   * /defDir/
   * </p>
   * 
   * <p>
   * (level 2)
   * /dir1/
   * /dir1/file1
   * /dir1/defFile
   * /dir1/defDir/
   * </p>
   * 
   * <p>
   * (level 3)
   * /dir1/dir1_1/
   * /dir1/dir1_1/file1_1
   * /dir1/dir1_1/defFile
   * /dir1/dir1_1/defDir/
   * </p>
   */
  @Before
  public void setup() {
    search = new Search();
    userInputs = new ArrayList<>();
    expectedStr = new StringBuilder();
    fs = FileSystem.createFileSystem();
    // init file structure
    final String dirLiteral = "directory";

    // in /
    Directory currDir = fs.getCurrentDirectory();
    fs.createDirectoryWithParent("dir1", currDir);
    fs.createFileWithParent(DEF_F_NAME, currDir);
    fs.createDirectoryWithParent(DEF_D_NAME, currDir);

    // in dir1
    currDir = (Directory) fs.checkPath("/dir1", dirLiteral);
    fs.createDirectoryWithParent("dir1_1", currDir);
    fs.createFileWithParent("file1", currDir);
    fs.createFileWithParent(DEF_F_NAME, currDir);
    fs.createDirectoryWithParent(DEF_D_NAME, currDir);

    // in dir1_1
    currDir = (Directory) fs.checkPath("/dir1/dir1_1", dirLiteral);
    fs.createFileWithParent(DEF_F_NAME, currDir);
    fs.createFileWithParent("file1_1", currDir);
    fs.createDirectoryWithParent(DEF_D_NAME, currDir);
  }

  /**
   * Tear down fileSystem instance. From Piazza post @579.
   *
   * @throws Exception general exception, @579
   */
  @After
  public void tearDown() throws Exception {
    Field f = (fs.getClass()).getDeclaredField("fs");
    f.setAccessible(true);
    f.set(null, null);
  }

  /**
   * A helper to add expected str with a new line at the end.
   *
   * @param str Str to add
   */
  private void addStr(String str) {
    expectedStr.append(str + "\n");
  }

  /**
   * A helper to add get string wrapped in double quotes.
   *
   * @param str Str to wrap inside double quotes
   */
  private String wrapInDoubleQuotes(String str) {
    return "\"" + str + "\"";
  }

  /**
   * A helper to compare actual and expected.
   */
  private void runTest(boolean nullExpected) {
    String actual =
        search.run(userInputs.toArray(new String[userInputs.size()]));
    String expected = expectedStr.toString();
    if (nullExpected) {
      expected = null;
    }
    assertEquals(expected, actual);
    userInputs.clear();
  }

  /**
   * Invalid inputs tests.
   */
  @Test
  public void testInvalidInputsNoArg() {
    // test1: empty args
    runTest(true);
  }

  /**
   * Invalid input test: without any paths.
   */
  @Test
  public void testInvalidInputsNoPaths() {
    // test2: without any paths
    userInputs.add(PARAM_TYPE);
    userInputs.add(F_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes("name"));
    runTest(true);
  }

  /**
   * Invalid input test: without type.
   */
  @Test
  public void testInvalidInputsNoType() {
    // test3: without type
    userInputs.add(FORWARD_SLASH);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes("a"));
    runTest(true);
  }

  /**
   * Invalid input test: without name at the end.
   */
  @Test
  public void testInvalidInputsNoName() {
    // test4: without name at the end
    userInputs.add(FORWARD_SLASH);
    userInputs.add(PARAM_TYPE);
    userInputs.add(F_TYPE);
    userInputs.add(PARAM_NAME);
    runTest(true);
  }

  /**
   * Search dir1 in root.
   */
  @Test
  public void testSearchDir1FromRoot() {
    userInputs.add(FORWARD_SLASH);
    userInputs.add(PARAM_TYPE);
    userInputs.add(F_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes("dir1"));

    addStr(FORWARD_SLASH + "dir1");

    runTest(false);
  }

  /**
   * Search defFile in from root.
   */
  @Test
  public void testSearchDefFileFromRoot() {
    userInputs.add(FORWARD_SLASH);
    userInputs.add(PARAM_TYPE);
    userInputs.add(D_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes(DEF_D_NAME));

    addStr("/dir1/dir1_1/" + DEF_D_NAME);
    addStr("/dir1/" + DEF_D_NAME);
    addStr(FORWARD_SLASH + DEF_D_NAME);

    runTest(false);
  }

  /**
   * Search defDir from: /dir1/dir1_1/.
   */
  @Test
  public void testSearchDefDirFromDir11() {
    userInputs.add("/dir1/dir1_1/");
    userInputs.add(PARAM_TYPE);
    userInputs.add(D_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes(DEF_D_NAME));

    addStr("/dir1/dir1_1/defDir");

    runTest(false);
  }

  /**
   * Search defDir from: /dir1.
   */
  @Test
  public void testSearchDefDirFromDir1() {
    String target = "dir1/";
    userInputs.add(target);
    userInputs.add(PARAM_TYPE);
    userInputs.add(D_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes(DEF_D_NAME));

    addStr("dir1/dir1_1/" + DEF_D_NAME);
    addStr("dir1/" + DEF_D_NAME);

    runTest(false);
  }

  /**
   * Search for dir1_1 from root while current working dir is dir1.
   */
  @Test
  public void testSearchFromRootDifCwd() {
    // set current dir (shouldn't affect the output or result)
    Directory dir1 = (Directory) fs.checkPath("/dir1/", "directory");
    fs.setCurrentDirectory(dir1);

    userInputs.add(FORWARD_SLASH);
    userInputs.add(PARAM_TYPE);
    userInputs.add(D_TYPE);
    userInputs.add(PARAM_NAME);
    userInputs.add(wrapInDoubleQuotes("dir1_1"));

    addStr("/dir1/dir1_1");

    runTest(false);
  }
}
