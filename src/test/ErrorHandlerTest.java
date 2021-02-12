package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import util.ErrorHandler;


public class ErrorHandlerTest {

  // Since getErrorMessage do not return anything, I override it.
  @Test
  public void getErrorMessageTest() {
    String expected = "ERROR: There is a error";
    String actual = ErrorHandler.getErrorMessageForTesting("There is a error");
    assertEquals(expected, actual);

  }

}
