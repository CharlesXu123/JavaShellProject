package filesystem;

/**
 * File class extends UnitFileSystemComponent with a String of content.
 * 
 * @author tanluowe
 *
 */
public class File extends UnitFileSystemComponent {

  private String content;

  /**
   * Constructor of a File with name and parentDirectory.
   * 
   * @param n name to set
   * 
   * @param parentDirectory parentDirectory to set
   */
  public File(String n, Directory parentDirectory) {
    this.name = n;
    this.type = "file";
    this.parentDirectory = parentDirectory;
    this.content = "";

  }

  /**
   * Overwrite the content of a File.
   * 
   * @param newContent content to write
   */
  public void setContent(String newContent) {
    this.content = newContent;
  }

  /**
   * get the content of a File.
   * 
   * @return content of the File
   */
  public String getContent() {
    return content;
  }

  /**
   * Append new content to a File.
   * 
   * @param newContent content to append
   */
  public void appendContent(String newContent) {
    this.content += newContent;
  }

  /**
   * Clear the content of a File.
   */
  public void clearContent() {
    this.content = "";
  }

}
