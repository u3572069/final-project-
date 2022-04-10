package busInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;



public class VancouvertripSearch{
	public static ArrayList<String[]> search(String searchtime) {
		ArrayList<String[]> tripDetails = new ArrayList<>(0);
		try {
        File map = new File("busInfo/stop_times.txt");
	    Scanner myReader = new Scanner(map);
    	myReader.nextLine();
        while (myReader.hasNextLine()) {
          String[] currentLine = myReader.nextLine().split(",");
          if(currentLine.length>2){
          if(currentLine[1].replace(" ","0").equals(searchtime)){
        	  String[] tempArray = {currentLine[0],currentLine[1].replace(" ","0"),currentLine[2].replace(" ","0"),currentLine[3]
                      ,currentLine[4],currentLine[5],currentLine[6],currentLine[7]};
              tripDetails.add(tempArray);
          }}
        }
        myReader.close();  } catch (FileNotFoundException ex){
            return tripDetails;
        }
        return tripDetails;
}
   

}