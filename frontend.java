package busInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class frontend {

	public static void main(String[] args) throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		String s = " ____  _     ____    ____ ___  _ ____  _____  _____ _     \n"
				+ "/  __\\/ \\ /\\/ ___\\  / ___\\\\  \\/// ___\\/__ __\\/  __// \\__/|\n"
				+ "| | //| | |||    \\  |    \\ \\  / |    \\  / \\  |  \\  | |\\/||\n"
				+ "| |_\\\\| \\_/|\\___ |  \\___ | / /  \\___ |  | |  |  /_ | |  ||\n"
				+ "\\____/\\____/\\____/  \\____//_/   \\____/  \\_/  \\____\\\\_/  \\|\n"
				+ "                                                          ";
		boolean over=false;
		while(over!=true) {
			System.out.println(s);
			System.out.println("To select an option between 1,2 or 3, type the number and hit enter\n" + "1. Find shortest paths between two stops\n"
					+ "2. Search for a bus stop\n" + "3. Search for all trips with a given arrival time\n"+"4. quit");
			try {
				int selectedoption = input.nextInt();

				if (selectedoption == 1) {
					System.out.println("Please enter 2 bus stop numbers to find the shortest path between them-\n");
					if (!input.hasNextInt()) {
						System.out.println("Error!!! invalid input,please check each stop number is valid\n");
					}
					int firststop = input.nextInt();
					int secondstop = input.nextInt();
					shortestpathbetweenstops sp = new shortestpathbetweenstops("busInfo/transfers.txt","busInfo/stop_times.txt","busInfo/stops.txt");
					try {
						System.out.println(sp.gettingPathSequence(firststop, secondstop));		
					}
					catch(Exception ArrayIndexOutOfBoundsException) {
						System.out.println("Error!!! invalid input,please check if each stop number is correct\n");
					}
				} 

				else if (selectedoption == 2) {
					System.out.println("Please enter the bus stop name or the first characters of the stop name in upper case\n");
					String tmp=input.nextLine();
					assert tmp=="2";
					String stopName = input.nextLine();
					
					Scanner scanner = new Scanner(new File("busInfo/stops.txt")).useDelimiter(",");
					ArrayList<String> busstops = new ArrayList<String>();
					ArrayList<String> stopNames = new ArrayList<String>();
					String newString="";
					VancouverBusStopSearch a = new VancouverBusStopSearch();
					int key = 9;

					while(scanner.hasNext())
					{
						String temp = scanner.next();
						busstops.add(temp);
					}
					
					for(int i=11; i<busstops.size(); i+=9)
					{
						stopNames.add(busstops.get(i));
					}
					for(int i=0; i<stopNames.size(); i++)
					{
						if(stopNames.get(i).charAt(2) == ' ')
						{
							newString = stopNames.get(i).substring(3) + " " + stopNames.get(i).substring(0, 2);
							stopNames.set(i, newString);
						}
						a.insert(stopNames.get(i), key);
						key += 9;
					}
					a.match(stopName,busstops);
				}

				else if (selectedoption == 3) {
					System.out.println("Please enter an arrival time in the format HH:MM:SS to find the trips which match\n");
					String arrtime = input.next();	
					if(!Character.isDigit(arrtime.charAt(0))||!Character.isDigit(arrtime.charAt(1))||arrtime.charAt(2)!=':'||
							!Character.isDigit(arrtime.charAt(3))||!Character.isDigit(arrtime.charAt(4))||arrtime.charAt(5)!=':'||
							!Character.isDigit(arrtime.charAt(6))||!Character.isDigit(arrtime.charAt(7))) {
						System.out.println("Error invalid input,please enter the arrival time in the format HH:MM:SS\n");
					}
					else {
						String outputString="";
						ArrayList<String[]> returnList= VancouvertripSearch.search(arrtime);
						System.out.println("Trip ID\t\tArrival time\tDeparture time\tStop ID\tStop Sequence\tDropoff type\tShape Distance Travelled");
						for(int i=0;i<returnList.size();i++) {
							for(int j=0;j<8;j++) {
								if(j==5) {
									outputString+="\t";
								}
								else if(j==6) {
									outputString+=returnList.get(i)[j]+",\t\t";
								}
								else {
									outputString+=returnList.get(i)[j]+",\t";
								}
							}
								if(i==returnList.size()-1) {
									outputString=outputString.substring(0, outputString.length()-2)+"\n";
								}
								else outputString+="\n";
							}
							System.out.println(outputString);
					}
				}
				else if(selectedoption == 4) {
					System.out.println("Exited!!");
					over=true;
				}
				else System.out.println("Error!!! invalid input,please select a valid option\n");
			}
			catch(Exception InputMismatchException) {
				System.out.println("Error!!!! invalid input,please try again\n");
				input.nextLine();
			}
		}
	}
}



