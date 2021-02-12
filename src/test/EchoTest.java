package test;

import static org.junit.Assert.*;

import commands.Echo;
import org.junit.Before;
import org.junit.Test;

public class EchoTest {

  Echo ec;

  @Before
  public void setup() {
    ec = new Echo();
  }

  // when the number of inputs is larger than one
  @Test
  public void testexecuteEcho1() {
    String[] args = {"\"input1\"", "\"input2\""};
    String output = ec.executeEcho(args);
    assertEquals("\"invalid input", output);
  }

  // when the input format is invalid
  @Test
  public void testexecuteEcho2() {
    String[] args1 = {"input1"};
    String[] args2 = {"\"input"};
    String[] args3 = {"input\""};
    String output1 = ec.executeEcho(args1);
    String output2 = ec.executeEcho(args2);
    String output3 = ec.executeEcho(args3);
    assertEquals("\"invalid input", output1);
    assertEquals("\"invalid input", output2);
    assertEquals("\"invalid input", output3);
  }

  // when the input is valid
  @Test
  public void testexecuteEcho3() {
    String[] args = {"\"valid input\""};
    String output = ec.executeEcho(args);
    assertEquals("valid input", output);
  }

}
