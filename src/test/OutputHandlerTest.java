package test;

import static org.junit.Assert.assertEquals;

import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.OutputHandler;

/**
 * Testing OutputHandler except sendToStdOut() since it's one line of println().
 *
 * @author kanghung
 */
public class OutputHandlerTest {

  /**
   * file system instance.
   */
  private FileSystem fs;

  /**
   * Store string to send to a stream (STDOUT, or to a file).
   */
  private String output;

  /**
   * Path of a file in string to write to during each test.
   */
  private String filePath;

  /**
   * String to over-write a file.
   */
  private static final String OVER_WRITTEN = "ow";

  /**
   * Run overWriteFile() with previously-set parameters.
   */
  private void runOverWrite() {
    this.output = OVER_WRITTEN;
    OutputHandler.overWriteFile(this.output, this.filePath, this.fs);
  }

  /**
   * Run appendFile() with previously-set parameters.
   */
  private void runAppend() {
    this.output = " appended";
    OutputHandler.appendFile(this.output, this.filePath, this.fs);
  }

  /**
   * Compare the content of a file with path filePath against
   * expectedFileContent.
   *
   * @param expectedFileContent String of expected file content
   */
  private void testFileContent(String expectedFileContent) {
    File tmpFile = (File) fs.checkPath(this.filePath, "file");
    assertEquals(expectedFileContent, tmpFile.getContent());
  }

  /**
   * Setup FileSystem before each test.
   * <p>
   * / dir1 file1_1 dir2 dir2_1 file2_1_1 dir3 file1
   * </p>
   */
  @Before
  public void setup() {
    final String dirLiteral = "directory";
    fs = FileSystem.createFileSystem();
    for (int i = 1; i <= 3; i++) {
      fs.createDirectoryCurrentDirectory("dir" + i);
    }
    File tmpFileNode;
    // file1 inside /
    tmpFileNode = fs.createFileUnderCurrentDirectory("file1");
    tmpFileNode.appendContent("file1");
    // file1_1 inside dir1
    tmpFileNode = fs.createFileWithParent("file1_1",
        (Directory) fs.checkPath("/dir1", dirLiteral));
    tmpFileNode.appendContent("file1_1");
    // dir2_1 inside dir2
    fs.createDirectoryWithParent("dir2_1",
        (Directory) fs.checkPath("/dir2", dirLiteral));
    // file2_1_1 inside dir2_1
    tmpFileNode = fs.createFileWithParent("file2_1_1",
        (Directory) fs.checkPath("/dir2/dir2_1", dirLiteral));
    tmpFileNode.appendContent("file2_1_1");
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
   * Simply print the test number.
   *
   * @param num Int test number
   */
  private void printTestNum(int num) {
    System.out.println("\nTEST " + num);
  }

  /**
   * Test appendFile. Standard usage with absolute path, one level.
   */
  @Test
  public void stdAppendFileAbsLevOne() {
    printTestNum(1);
    filePath = "/file1";
    runAppend();
    testFileContent("file1 appended");
  }

  /**
   * Test appendFile. Standard usage with abs path, two level.
   */
  @Test
  public void stdAppendFileAbsLevTwo() {
    filePath = "/dir1/file1_1";
    runAppend();
    printTestNum(2);
    testFileContent("file1_1 appended");
  }

  /**
   * Test appendFile. Standard usage with abs path, three levels.
   */
  @Test
  public void stdAppendFileAbsLevThree() {
    printTestNum(3);
    filePath = "/dir2/dir2_1/file2_1_1";
    runAppend();
    testFileContent("file2_1_1 appended");
  }

  /**
   * Test appendFile. Standard usage with rel path, one level.
   */
  @Test
  public void stdAppendFileRelLevOne() {
    printTestNum(4);
    filePath = "file1";
    runAppend();
    testFileContent("file1" + this.output);
  }

  /**
   * Test appendFile. Std usage with rel path, three levels.
   */
  @Test
  public void stdAppendFileRelLevThree() {
    output = " appended";
    filePath = "dir2/dir2_1/file2_1_1";
    runAppend();
    testFileContent("file2_1_1" + this.output);
  }

  /**
   * Test OverWriteFile. Std usage with abs path, one level.
   */
  @Test
  public void stdOverWriteFileAbsLevOne() {
    filePath = "/file1";
    runOverWrite();
    testFileContent(OVER_WRITTEN);
  }

  /**
   * Test OverWriteFile. Std usage with abs path, two levels.
   */
  @Test
  public void stdOverWriteFileAbsLevTwo() {
    filePath = "/dir1/file1_1";
    runOverWrite();
    testFileContent(OVER_WRITTEN);
  }

  /**
   * Test OverWriteFile. Std usage with abs path, three levels.
   */
  @Test
  public void stdOverWriteFileAbsLevThree() {
    filePath = "/dir2/dir2_1/file2_1_1";
    runOverWrite();
    testFileContent(OVER_WRITTEN);
  }

  /**
   * Test OverWriteFile. Std usage with rel path, one level.
   */
  @Test
  public void stdOverWriteFileRelLevOne() {
    filePath = "file1";
    runOverWrite();
    testFileContent(OVER_WRITTEN);
  }

  /**
   * Test OverWriteFile. Std usage with rel path, three levels.
   */
  @Test
  public void stdOverWriteFileRelLevThree() {
    filePath = "dir2/dir2_1/file2_1_1";
    runOverWrite();
    testFileContent(OVER_WRITTEN);
  }

}
