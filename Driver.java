package ics321;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;


public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Assignment1 a1 = new Assignment1();
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = null;
			ArrayList<String[]> commands = new ArrayList<String[]>();
			HashSet<Integer> columns = new HashSet<Integer>();
			
			// Change in how the driver works:
			// first read all the commands into an array
			// then determine the columns that will actually
			// be asked about. Only these columns will be
			// sorte
			while ((line = reader.readLine())!=null)
			{
				String[] arr = line.split(" ");
				commands.add(arr);
				if (!arr[0].equalsIgnoreCase("load")) {
				    int j = Integer.parseInt(arr[1])-1;
				    columns.add(j);
				    //System.out.println("Added " + j + " to the columns Set.");
				}
				
			}
			
	        Iterator<String[]> itr = commands.iterator();
    	    String[] nextLine;
    	    while ((itr.hasNext()) && ((nextLine = itr.next()) != null)) {
    	        
				if (nextLine[0].equalsIgnoreCase("load") && nextLine.length==2)
				{
					a1.load(nextLine[1], columns);
				}
				else if (nextLine[0].equalsIgnoreCase("eq") && nextLine.length==3)
				{
					try
					{
						a1.searchEq(Integer.parseInt(nextLine[1]), nextLine[2]);
					}
					catch (NumberFormatException e) {
						System.out.println("Skipped line: "+line);
					}
				}
				else if (nextLine[0].equalsIgnoreCase("gtr") && nextLine.length==3)
				{
					try
					{
						a1.searchGtr(Integer.parseInt(nextLine[1]), Float.parseFloat(nextLine[2]));
					}
					catch (NumberFormatException e) {
						System.out.println("Skipped line: "+line);
					}
				}
				else
				{
					System.out.println("Skipped line: "+line);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Path to command file required");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Command file "+ args[0] +" not found");
		} catch (IOException e) {
			System.out.println("IOException");
		} 

	}

}
