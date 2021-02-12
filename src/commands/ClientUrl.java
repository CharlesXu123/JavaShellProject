package commands;

import filesystem.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class handles command: curl URL.
 * 
 * @author tanluowei
 *
 */
public class ClientUrl extends UnitCommand {

  /**
   * Return the documentation for curl command.
   *
   * @return String stored documentation
   */
  @Override
  public String toString() {
    StringBuilder doc = new StringBuilder();
    doc.append("< Documentation for ClientURL (curl) >");
    doc.append("\nUsage: curl URL");
    doc.append("\nRetrieve the fle at that URL");
    doc.append("and add it to the current working directory.");
    return doc.toString();
  }

  /**
   * Create a URL object.
   * 
   * @param urlString a URL
   * @return the URL object created
   */
  public URL createUrl(String urlString) {
    try {
      return new URL(urlString);

    } catch (MalformedURLException e) {
      return null;
    }

  }

  /**
   * OpenStrean a URL object.
   * 
   * @param urlString a URL object
   * @return a InputStream object
   */
  public InputStream openStreamHelper(URL urlString) {
    try {
      return urlString.openStream();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Get the valid file name for the FileSystem from a URL.
   * 
   * @param url a url
   * @return the file name is the url is valid. Otherwise, return null
   */
  private String getFileName(String url) {
    int median = 0;
    int start = 0;
    String name = "";

    if (!(url.substring(url.length() - 4).equals(".txt"))) {
      return null;
    }

    for (int i = url.length() - 1; i > 0; i--) {
      if (url.charAt(i) == '.') {
        median = i;
      }
      if (url.charAt(i) == '/') {
        start = i + 1;
        break;
      }
    }
    name = url.substring(start, median) + url.substring(median + 1);
    return name;
  }


  /**
   * A helper method to return the fileName and status.
   * It returns a errorMessage if error is cached
   * 
   * @param args user inputs
   * @return a String array of file name or error msg.
   */
  public String[] run(String[] args) {

    String[] status = {"", ""};
    if (args.length != 1) {
      status[0] = ("Invalid number of arguments");
      status[1] = "error";
      return status;
    }
    URL url = this.createUrl(args[0]);
    if (url == null) {
      status[0] = ("Invalid URL is provided");
      status[1] = "error";
      return status;
    }
    String fileName = getFileName(args[0]);
    if (fs.getCurrentDirectory().containComponent(fileName)) {
      status[0] = (fileName + " has already exist");
      status[1] = "error";
      return status;
    }
    if (fileName == null) {
      status[0] = (args[0] + " does not direct to a file");
      status[1] = "error";
      return status;
    }
    status[0] = fileName;
    status[1] = "works";
    return status;
  }

  /**
   * Execute command: curl URL.
   *
   * @param args User input
   */
  public void executeCommand(String[] args) {

    String[] status = this.run(args);

    if (status[1].equals("error")) {
      sendErrMsg(status[0]);
      return;
    }

    URL url = this.createUrl(args[0]);

    BufferedReader read =
        new BufferedReader(new InputStreamReader(this.openStreamHelper(url)));
    String i;

    File file = new File(status[0], fs.getCurrentDirectory());
    fs.getCurrentDirectory().getComponents().add(file);
    try {
      while ((i = read.readLine()) != null) {

        file.appendContent(i);
        file.appendContent("\n");
      }
      read.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Delete the extra "\n" at the end
    file.setContent(
        file.getContent().substring(0, file.getContent().length() - 1));
  }
}

