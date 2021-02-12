package test;

import static org.junit.Assert.assertEquals;

import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FileSystemTest {

  FileSystem fs;

  // need these below for checkPathTest
  Directory dirOne;
  Directory dirOneOne;
  Directory dirTwo;
  File fileOneOne;
  File fileTwo;
  File fileAtRoot;

  /**
   * set up.
   */
  @Before
  public void setup() {
    fs = FileSystem.createFileSystem();
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

  @Test
  public void getParentTest() {
    String fileName = "file1";
    String[] output1 = fs.getParent(fileName);
    String[] expected1 = {"/", "file1"};
    assertEquals(expected1[0], output1[0]);
    assertEquals(expected1[1], output1[1]);
    String path = "/dir1/dir2/file1";
    String[] output2 = fs.getParent(path);
    String[] expected2 = {"/dir1/dir2/", "file1"};

    assertEquals(expected2[0], output2[0]);

    assertEquals(expected2[1], output2[1]);

  }


  @Test
  public void findPathTest() {
    String output1 = FileSystem.findPath(fs.getRoot());
    assertEquals("/", output1);
    Directory dir1 = fs.createDirectoryWithParentWithReturn("dir1", fs.getRoot());
    String output2 = FileSystem.findPath(dir1);
    assertEquals("/dir1", output2);
    Directory dir2 = fs.createDirectoryWithParentWithReturn("dir2", dir1);
    String output3 = FileSystem.findPath(dir2);
    assertEquals("/dir1/dir2", output3);
  }

  /**
   * Creates a set of dirs to test against checkPath.
   * <p>
   * Note that checkPath tests rely on following methods: fs.createDirectoryCurrentDirectory()
   * fs.createDirectoryWithParentWithReturn() fs.createFileWithParent() fs.setCurrentDirectory()
   * </p>
   * 
   * <p>
   * /dir1/ /dir1/dir11/file11 /dir2/file2 /file
   * </p>
   */
  public void setupForCheckPath() {
    // create /dir1/dir11/
    dirOne = fs.createDirectoryCurrentDirectory("dir1");
    dirOneOne = fs.createDirectoryWithParentWithReturn("dir11", dirOne);
    // create /dir1/dir11/file11
    fileOneOne = fs.createFileWithParent("file11", dirOneOne);
    // create dir2/file2
    dirTwo = fs.createDirectoryCurrentDirectory("dir2");
    fileTwo = fs.createFileWithParent("file2", dirTwo);
    // create /file
    fileAtRoot = fs.createFileWithParent("file", fs.getRoot());
  }

  // BEGIN testing checkPath

  /**
   * checkPath: invalid paths.
   */
  @Test
  public void testCheckPathInvalidInputs() {
    // setup for checkPath
    setupForCheckPath();

    assertEquals(null, fs.checkPath("/non-existent/path", "directory"));
    assertEquals(null, fs.checkPath("/non/existent/path/to/file", "file"));
    // root doesn't have parent dir
    assertEquals(null, fs.checkPath("/..", "directory"));
  }

  /**
   * checkPath: basic user cases.
   */
  @Test
  public void testCheckPathSimpleCases() {
    // setup for checkPath
    setupForCheckPath();

    // basic user cases: absolute path
    String fileLit = "file"; // string literal for file
    assertEquals(fileAtRoot, (File) fs.checkPath("/file", fileLit));
    assertEquals(fileTwo, (File) fs.checkPath("/dir2/file2", fileLit));
    assertEquals(fileOneOne, (File) fs.checkPath("/dir1/dir11/file11", fileLit));
    String dirLit = "directory"; // string literal for dir
    assertEquals(dirOne, (Directory) fs.checkPath("/dir1", dirLit));
    assertEquals(dirTwo, (Directory) fs.checkPath("/dir2", dirLit));
    assertEquals(dirOneOne, (Directory) fs.checkPath("/dir1/dir11", dirLit));

    // basic user cases: some relative paths
    // CWD: /
    assertEquals(dirTwo, (Directory) fs.checkPath("dir2", dirLit));
    assertEquals(fileTwo, (File) fs.checkPath("dir2/file2", fileLit));
    // CWD: /dir1
    fs.setCurrentDirectory(dirOne);
    assertEquals(dirOneOne, (Directory) fs.checkPath("dir11", dirLit));
    // CWD: /dir2
    fs.setCurrentDirectory(dirTwo);
    assertEquals(fs.getRoot(), (Directory) fs.checkPath("..", dirLit));
    assertEquals(fileTwo, (File) fs.checkPath("file2", fileLit));
  }

  /**
   * checkPath: some special user cases.
   */
  @Test
  public void testCheckPathSpecialCases() {
    setupForCheckPath();

    // single dots
    assertEquals(fs.getRoot(), (Directory) fs.checkPath(".", "directory"));
    assertEquals(fs.getRoot(), (Directory) fs.checkPath("./././././.", "directory"));
    // double dots
    // CWD: /dir1
    fs.setCurrentDirectory(dirOne);
    assertEquals(fs.getRoot(), (Directory) fs.checkPath("..", "directory"));
    assertEquals(fileAtRoot, (File) fs.checkPath("../file", "file"));
  }

  // END testing checkPath


  /**
   * test createFileWithParent.
   */
  @Test
  public void createFileWithParentTest() {
    String expectedName = "file1";
    File file1 = fs.createFileWithParent(expectedName, fs.getRoot());

    assertEquals(expectedName, file1.getName());
    assertEquals("file", file1.getType());

  }

  /**
   * test createFileWithParent.
   */
  @Test
  public void createFileUnderCurrentDirectoryTest() {
    fs.setCurrentDirectory(fs.getRoot());
    String expectedName = "file1";
    File file1 = fs.createFileUnderCurrentDirectory(expectedName);

    assertEquals(expectedName, file1.getName());
    assertEquals("file", file1.getType());
  }

  /**
   * test createDirectoryWithParentWitReturn. Also test the reateDirectoryWithParent.
   */
  @Test
  public void createDirectoryWithParentWithReturnTest() {
    String expectedName = "dir1";
    Directory dir1 = fs.createDirectoryWithParentWithReturn(expectedName, fs.getRoot());

    assertEquals(expectedName, dir1.getName());
    assertEquals("directory", dir1.getType());
  }

  /**
   * test createDirectoryCurrentDirectory.
   */
  @Test
  public void createDirectoryCurrentDirectoryTest() {
    fs.setCurrentDirectory(fs.getRoot());
    String expectedName = "dir1";
    Directory dir1 = fs.createDirectoryCurrentDirectory(expectedName);

    assertEquals(expectedName, dir1.getName());
    assertEquals("directory", dir1.getType());
  }

  /**
   * test isValidNameForFileSystem: valid name.
   */
  @Test
  public void isValidNameForFileSystemTest1() {
    String expectedName = "file1";
    assertEquals(true, fs.isValidNameForFileSystem(expectedName));

  }

  /**
   * test isValidNameForFileSystem: invalid name.
   */
  @Test
  public void isValidNameForFileSystemTest2() {
    String expectedName = ".><<>>><<<>";
    assertEquals(false, fs.isValidNameForFileSystem(expectedName));

  }

  /**
   * Test updatePath().
   */
  @Test
  public void testUpdatePathBasic() {
    // CWD: /
    fs.updatePath();
    assertEquals("/", fs.getCurrentPath());

    // CWD: /dir
    fs.setCurrentDirectory(fs.createDirectoryCurrentDirectory("dir"));
    fs.updatePath();
    assertEquals("/dir", fs.getCurrentPath());

    // CWD: /dir/sub_dir
    fs.setCurrentDirectory(fs.createDirectoryCurrentDirectory("sub_dir"));
    fs.updatePath();
    assertEquals("/dir/sub_dir", fs.getCurrentPath());
  }

}
