import java.util.*;
import java.io.*;

public class UnitTestRunner {
   
   private static PrintStream testFile = null;
   
   /**
   *  Processes the command if it is a Test command.
   *  Test commands take one of the following formats:
   *  <ul><li>test {name} [boolean:break_on_fail]</li><li>test create {name}</li><li>test create off</li></ul>
   *  <p>The first test command will read and process the file "tests_checkpoint{name}.txt" which
   *  should exist in the same directory as the *.class files. If the {name} is followed by
   *  "true" then the execution of commands in the test file will break on the first failure.
   *  This can make it easier for students to debug errors in their program.
   *  </p><p>The format of the test file is:</p><p>
   *  <code>//comment about this section<br>command<br>expected response<br>
   *  // subtotal<br></code></p>
   *  <p> The expected resonse can be "&lt;ignore&gt;" if we want to ignore the output.
   *  One would ignore the response to verify that the command does not cause a crash, or 
   *  where the exact response can vary. In these cases, the instructor will want to manually
   *  verify the response. If ignored commands do not throw an exception, the 
   *  command is considered "pass" and is awarded 1 point. 
   *  <p>For all "normal" commands, 
   *  the response is compared to the expected response (ignoring case). If it
   *  matches, it will award 1 point.</p>
   *  <p> Another possible test command is to create a test. To do this, type in the
   *  command "test create {name}". This will then put the Test Runner into "Record"
   *  mode where every subsequent command is used to generate a new test file. The
   *  responses will be considered correct and added to the file. At this time,
   *  comments and subtotals need to be added manually to the file. When the test recording
   *  is complete, stop the recording and file generation with the command "test create off".</p>
   *
   *  @param input The command string that the user entered to be processed.
   *  @return true if the command was a Test command. This means that the caller
   *        should ignore the input and ask the user for the next input.
   *        <br> false if the caller should proceed to process this command.
   */
   public static boolean processCommand(String input) {
      if (generateTestCommand(input) || runTestCommand(input)) {
         // tell the caller that the command was handled and to get the next command
         return true;
      } else if (testFile != null) {        
         // check for the "quit" command.
         if (input.equalsIgnoreCase("quit")) {
            closeTestFile();
            
            // tell the caller that the command was NOT handled
            return false;
         }
         
         // we are generating a test file
         //
         // output the command to the file
         testFile.println(input);
                  
         // get the output from the FracCalc
         String result = FracCalc.processCommand(input);
         if (result == null || result.length() == 0) {
            // the answer is empty. This shouldn't happen!
            System.out.println("Invalid answer to: " + input);
            closeTestFile();
         } else {
            // output the results to the file and output. There should be only one line.
            testFile.println(result);
            System.out.println(result);
         }
         
         // tell the caller that the command was handled and to get the next command
         return true;
      }
   
      // this was not a test command. Tell caller the command was NOT handled.
      return false;
   }
   
   private static void runCheckpointTests(String checkpoint, boolean breakOnFail) {
      String file = "tests_checkpoint" + checkpoint + ".txt";
      runTests(file, breakOnFail); 
   }
      
   /**
   *  If the input command is "test create {name}", then work towoard creating
   *  the test file by redirecting the response to the output file. 
   *  Continue to create the test file until we receive the command, "test create off".
   *
   * @param input The command string that the user entered to be processed.
   * @return true if the command was processed as a "test create" command.
   */
   private static boolean generateTestCommand(String input) {
      // get the tokens
      Scanner parser = new Scanner(input);
      
      if ("test".equalsIgnoreCase(parser.next())) {
         if (parser.hasNext() && "create".equalsIgnoreCase(parser.next())) {
            if (parser.hasNext()) {
               String testName = parser.next();
               if (testFile != null && "off".equalsIgnoreCase(testName)) {
                  closeTestFile();
               } else {
                  try {
                     testFile = new PrintStream(new File("tests_checkpoint" + testName + ".txt"));
                  } 
                  catch (Exception e) {
                	  System.out.println(e.getMessage());
                	  e.printStackTrace();
                  }
               }
               return true;
            }
         }
      }
      
      return false;
   }
   
   private static void closeTestFile() {
      // creating test is done. Turn it off.
      // flush file and close it up.
      testFile.flush();
      testFile.close();
      testFile = null;
      System.out.println("Test recording STOPPED.");
   }
   
   /** 
   * runTestCommand will check if the user input is "test {name} [boolean]". If
   * it is a test command, it will open the Unit Test file and process it.
   * The [boolean] is defaulted to false meaning that it will NOT break on first
   * failure.
   *
   * @param input The command that the user input.
   * @return true if the command is of the format "test {name} [boolean]" and handled here.<br>
   *     false if the caller should continue to process the command.
   */
   private static boolean runTestCommand(String input) {
      // get the tokens
      Scanner parser = new Scanner(input);
      if ("test".equalsIgnoreCase(parser.next())) {
         String checkpoint;
         boolean breakOnFail = false;
            
         if (parser.hasNext()) {
            checkpoint = parser.next();
               
            // check for one optional boolean value
            if (parser.hasNextBoolean()) {
               breakOnFail = parser.nextBoolean();
            }
               
            // execute the tests
            runCheckpointTests(checkpoint, breakOnFail);
                  
            // tests were processed
            return true;
         }   
      }
            
      // not a test command
      return false;
   }
   
   /**
   *  This will open a file and run the commands found inside of it.
   *  Subsequent lines will contain the expected output of running the command.
   *  The format is:<p>
   *     command<br>
   *     expected output<br>
   *  <p>For example:<code>
   *     1/2 + 2_1/2<br>
   *     3<br></code>
   *
   *  @param filename The name of the test file
   *  @param breakOnFail If true, break on first failure.
   */
   private static void runTests(String filename, boolean breakOnFail) {
   
      Scanner file = null;
      int points = 0;
      int total = 0;
      int subSectionPoints = 0;
      int subSectionTotal = 0;
      
      File f = null;
      try {
         // load the file of test cases
         f = new File(filename);
         file = new Scanner(f);
         
         while (file.hasNextLine()) {
         
            try {
               while (file.hasNextLine()) {
                  String input = file.nextLine();
                  if (input.equalsIgnoreCase("// subtotal")) {
                     // output the current sub total of points so far
                     System.out.printf("\tSection Sub-Total: %d / %d\t\tTOTAL: %d / %d\n", 
                           subSectionPoints, subSectionTotal, points, total);
                     subSectionPoints = 0;
                     subSectionTotal = 0;
                     continue;
                  } else if (input.startsWith("//")) {
                     // this line is a comment. Output the comment to the output stream.
                     System.out.println("\n\t"+ input.substring(3));
                     continue;
                  }
                  if (!file.hasNextLine()) {
                     System.out.println("ERROR in test file. Expected integer for count of lines.");
                     return;
                  }
                  
                  // The answer is always 1 line
                  // point value is always 1
                  int pointsWorth = 1;
                  if (!file.hasNextLine()) {
                     System.out.println("ERROR in test file. Unexpected end of file.");
                     return;
                  }
                  String expected = file.nextLine();
                  if (expected.length() == 0) {
                     System.out.println("ERROR in test file. Unexpected empty answer.");
                     return;
                  }
                  
                  total += pointsWorth;
                  subSectionTotal += pointsWorth;
                  
                  System.out.print("Running Test [" + input + "]");
                  String actualFull = FracCalc.processCommand(input);
                  
                  if (goodMatch(actualFull, expected)) {
                     System.out.printf(" passed  (+%d pts)\n", pointsWorth);
                     points += pointsWorth;
                     subSectionPoints += pointsWorth;
                  } else if (breakOnFail) {
                     // we have a failure. Just break out!
                     System.out.println("Tests STOPPED (Set to break on fail)");
                     return;
                  }
               }
            } 
            catch (Exception e) {
               System.out.println(" Failed with exception. Here are details:");
               e.printStackTrace();
               System.out.println(e.getMessage());
               if (breakOnFail) {
                  // we have a failure. Just break out!
                  System.out.println("Tests STOPPED (Set to break on fail)");
                  return;
               }
            }
         }
      } 
      catch (FileNotFoundException e) {
         System.out.println("Cannot find test file. Here are details:");
         if (f != null) {
            System.out.println(" path of file: " + f.getAbsolutePath());
         }
         System.out.println(e.getMessage());
      } 
      finally {
         System.out.printf("\nPoints: %d / %d\n", points, total);
         if (file != null) {
            file.close();
         }
      }
   }
   
  
   private static boolean goodMatch(String actualFull, String expected) {

      if (expected.equals("<ignore>")) {
         // all we are attempting to verify is that the program doesn't crash.
         // Or, the output may vary so we cannot validate the actual output.
         System.out.print("  Actual Output:\n" + actualFull + "\n\t");
         return true;
      }
      
      if (actualFull == null || actualFull.length() == 0) {
         System.out.printf(" failed: expected more output.\n\tExpected: \"%s\"\n\t  Actual: \"", expected);
         return false;
      }
      
      if (!expected.equalsIgnoreCase(actualFull)) {
         System.out.printf(" failed: wrong answer.\n\tExpected: \"%s\"\n\t  Actual: \"%s\"\n", expected, actualFull);
         return false;
      }
      
      // A good match. Passed!
      return true;
   }
   
}