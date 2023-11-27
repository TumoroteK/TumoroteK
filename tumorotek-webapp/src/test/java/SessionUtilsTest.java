import org.junit.Test;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class SessionUtilsTest
{
   @Test
   public void testListInSession() {
      // Get the current ZK session
      Session zkSession = Executions.getCurrent().getSession();

      // Create a list of objects
      List<String> myList = new ArrayList<>();
      myList.add("Object 1");
      myList.add("Object 2");
      myList.add("Object 3");

      // Store the list in the ZK session
      zkSession.setAttribute("myList", myList);

      // Retrieve the list from the ZK session
      List<String> retrievedList = (List<String>) zkSession.getAttribute("myList");

      // Verify that the retrieved list is not null
      assertNotNull(retrievedList);

      // Verify the size and content of the retrieved list
      assertEquals(3, retrievedList.size());
      assertEquals("Object 1", retrievedList.get(0));
      assertEquals("Object 2", retrievedList.get(1));
      assertEquals("Object 3", retrievedList.get(2));
   }
}
