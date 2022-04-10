package busInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class tripSearchTest
{
     @Test
     public void test() {
    	 ArrayList<String[]> numbers = VancouvertripSearch.search("25:10:00");
    	 for (int i = 0; i < numbers.size(); i++) 
             System.out.print(Arrays.toString(numbers.get(i))+"\n");        
     }
}
