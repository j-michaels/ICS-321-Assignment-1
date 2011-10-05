package ics321;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Assignment1 {
    ArrayList<String>[] indices; // column:array
    String filen;
	
	//load takes a fileName as an argument and
	//reads in the CSV file at the given path.
	//The data may not all fit in memory
	public void load(String fileName)
	{
		try {
			CSVReader reader =  new CSVReader(new FileReader(fileName),'|');
		   filen = fileName;
			System.out.println("load "+fileName);
            String [] tmpline;
            int totalColumns;
            tmpline = reader.readNext();
            totalColumns = tmpline.length;
            indices = (ArrayList<String>[])new ArrayList[tmpline.length];
            for (int i=0; i<tmpline.length; i++) {
                indices[i] = new ArrayList<String>();
            }
            
//            for (int i=0; i < tmpline.length; i++) {
            externalSort(fileName, totalColumns);
//            }
            
            
//	    	String [] nextLine;
//			while ((nextLine = reader.readNext()) != null) {
			    // TODO Load CSV
				 // nextLine[] is an array of values from the line
			    // ex: System.out.println(nextLine[0] + nextLine[1] + "etc...");
			    
			    //externalSort(String relation, String attribute, int indexToCompare)

			    /*
			        Plan:
			            Load up to memory limit
			            Quicksort that, dump to file data.1
			            Repeat until all data is sorted in data.n files
			            Merge sort data.n files
			                n-way comparison, immediately read to output file all_data
			                
			            
			            load index for each column into memory
			    */
//             for( int i=0; i<nextLine.length-1; i++) {
//			      System.out.print(nextLine[i] + "|");
//             }
//			    System.out.println();
//			}
	
            // now reload each one and 
		
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// findInIndex takes the column number and value and
	// finds the closest location in the datafile
	public int findInIndex(int columnNumber, String value)
	{
	    // find the most great index that is less than over the index of the value
	    //eg
	    // looking for: "candy"
	    // alfred, baseball, fred, soccer, zaphod
	    // ^ greater than, store index and keep going
	    //         ^ same
	    //                   ^ less than, exit
	    
	    //System.out.println("Total indices for column "+columnNumber +": " + indices[columnNumber].size());
	    Iterator<String> itr = indices[columnNumber].iterator();
	    int gtrthanindex = 0;
	    String nextLine;
	    while ((nextLine = itr.next()) != null) {
	        String[] line = nextLine.split("|");
	        
	        if (line[0].compareTo(value) >= 0) {
	            gtrthanindex = Integer.parseInt(line[1]);
            } else {
                break;
            }
	    }
	    return gtrthanindex;
	}
	
	//searchEq takes a columnNumber and a value and prints
	//tuples that match the given value on the given column.
	//More points will be given for faster return of this method
	public void searchEq(int columnNumber, String value)
	{
	    try {
	        System.out.println("searchEq col #"+columnNumber+"="+value);
	        columnNumber--;
    		String [] nextLine;
    		// Find in the index
    		int j = findInIndex(columnNumber, value);
    		String filenc = filen+"_sorted_col_" + columnNumber+".csv";
    		//System.out.println("Opening file "+filenc+" at position "+j+", looking for column "+columnNumber);
    		CSVReader reader = new CSVReader(new FileReader(filenc), '|',  CSVParser.DEFAULT_QUOTE_CHARACTER, j);
    		while ((nextLine = reader.readNext()) != null) {
    		    if (nextLine[columnNumber].equals(value)) {
    		        System.out.println(flattenArray(nextLine, "|"));
    		        //for( int i=0; i<nextLine.length-1; i++) {
       			    //  System.out.print(nextLine[i] + "|");
                    //}
    		    }
    		}
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
		//searchArrayClosestMatch(fileIndex[columnNumber], value);
		
	}
	
	//searchGtr takes a columnNumber and a value and prints
	//tuples where the given column is greater than the given value.
	//More points will be given for faster return of this method
	public void searchGtr(int columnNumber, float value)
	{
		System.out.println("searchGtr col #"+columnNumber+">"+value);
		try {
	        columnNumber--;
    		//System.out.println("searchEq col #"+columnNumber+"="+value);
    		String [] nextLine;
    		// Find in the index
    		//int j = findInIndex(columnNumber, String.valueOf(value));
    		String filenc = filen+"_sorted_col_" + columnNumber+".csv";
    		//System.out.println("Opening file "+filenc+" at position "+j+", looking for column "+columnNumber);
    		CSVReader reader = new CSVReader(new FileReader(filenc), '|',  CSVParser.DEFAULT_QUOTE_CHARACTER, 0);
    		while ((nextLine = reader.readNext()) != null) {
    		    if (Float.parseFloat(nextLine[columnNumber]) > value) {
    		        System.out.println(flattenArray(nextLine, "|"));
    		        //for( int i=0; i<nextLine.length-1; i++) {
       			    //  System.out.print(nextLine[i] + "|");
                    //}
    		    }
    		}
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	
	// sort an arrayList of arrays based on the ith column
    private ArrayList<String[]> mergeSort(ArrayList<String[]> arr, int index)
    {
         ArrayList<String[]> left = new ArrayList<String[]>();
         ArrayList<String[]> right = new ArrayList<String[]>();
         if (arr.size() <= 1)
             return arr;
         else
         {
             int middle = arr.size()/2;
             for (int i = 0; i<middle; i++)
                 left.add(arr.get(i));
             for (int j = middle; j<arr.size(); j++)
                 right.add(arr.get(j));
             left = mergeSort(left, index);
             right = mergeSort(right, index);
             return merge(left, right, index);
         }
    }

    // merge the the results for mergeSort back together 
    private ArrayList<String[]> merge(ArrayList<String[]> left, ArrayList<String[]> right, int index)
    {
         ArrayList<String[]> result = new ArrayList<String[]>();
         while (left.size() > 0 && right.size() > 0)
         {
             if(left.get(0)[index].compareTo(right.get(0)[index]) <= 0)
             {
                 result.add(left.get(0));
                 left.remove(0);
             }
             else
             {
                 result.add(right.get(0));
                 right.remove(0);
             }
         }
         if (left.size()>0) 
         {
             for(int i=0; i<left.size(); i++)
                 result.add(left.get(i));
         }
         if (right.size()>0) 
         {
             for(int i=0; i<right.size(); i++)
                 result.add(right.get(i));
         }
         return result;
    }
	
	//private static String getIntsFromStringArray(ArrayList<> line)
	//{
	    
	//}
	
	private void externalSort(String relation, int columns)
    {
         try
         {
             FileReader initialRelationInput = new FileReader(relation); 
             //BufferedReader initRelationReader = new BufferedReader(intialRelationInput);
 			CSVReader initRelationReader =  new CSVReader(initialRelationInput,'|');
            // String [] header = ;
             String [] row = null;// = initRelationReader.readNext();
             boolean justStarted = true;
             //int indexToCompare = getIndexForColumn(header,attribute);
             ArrayList<String[]> tenKRows = new ArrayList<String[]>();
             ArrayList<String[]> sortedTenKRows = new ArrayList<String[]>();
            
            //System.out.println("Reading 1000 lines from input filename "+relation+"...");
             int numFiles = 0;
             while ((row!=null) || (justStarted==true))
             {
                 justStarted = false;
                 //System.out.println("starting set "+numFiles+1);
                 // get 10k rows
                 for(int i=0; i<7000; i++)
                 {
                     String[] line = initRelationReader.readNext();
                     if (line==null)
                     {
                         row = null;
                         break;
                     }
                     //row = line.split("|");
                     row = line;
                     //tenKRows.add(getIntsFromStringArray(row));
                     tenKRows.add(row);
                 }
                 // sort the rows according to the index
                 //System.out.println("Sorting the input and dumping to a new file.");
                for (int i=1; i<4 ;i++) {
                    //if ((i == 2) || (i == 1) || (i == 3)) {
                    sortedTenKRows = mergeSort(tenKRows, i);
                     // write to disk
                     FileWriter fw = new FileWriter(relation + "_chunk" + numFiles + "_col"+i+".csv");
                     BufferedWriter bw = new BufferedWriter(fw);
                     //bw.write(flattenArray(header,",")+"\n");
                     for(int j=0; j<sortedTenKRows.size(); j++)
                     {
                         bw.append(flattenArray(sortedTenKRows.get(j),"|")+"\n");
                     }
                     bw.close();
                     sortedTenKRows.clear();
                    //}
                }
                 numFiles++;
                 tenKRows.clear();
             }
             //System.out.println("Merging files, total columns: "+columns);
             for (int i=1; i<4; i++) {
                 //System.out.println("for column "+i);
                 mergeFiles(relation, numFiles, i);
            }

             initRelationReader.close();
             initialRelationInput.close();

         }
         catch (Exception ex)
         {
             ex.printStackTrace();
             System.exit(-1);
         }


    }
    
    private String flattenArray(String[] str, String joinstr) {
        String outstr = "";
        for (int i=0; i<str.length; i++){
            if (str[i].equals("") != true) {
                outstr = outstr + str[i];
                outstr = outstr + joinstr;
            }
        }
        //System.out.println(outstr);
        return outstr;
    }

    private void mergeFiles(String relation, int numFiles, int compareIndex)
    {
         try
         {
             ArrayList<FileReader> mergefr = new ArrayList<FileReader>();
             ArrayList<CSVReader> mergefbr = new ArrayList<CSVReader>();
             ArrayList<String[]> filerows = new ArrayList<String[]>(); 
             FileWriter fw = new FileWriter(relation + "_sorted_col_" + compareIndex + ".csv");
             BufferedWriter bw = new BufferedWriter(fw);
             String [] header;
             int indexLine = 0;

             boolean someFileStillHasRows = false;

             for (int i=0; i<numFiles; i++)
             {
                 FileReader fr = new FileReader(relation+"_chunk"+i+"_col"+compareIndex+".csv");
                 CSVReader irr =  new CSVReader(fr,'|');
      			
                 mergefr.add(fr);
                 mergefbr.add(irr);
                 // get each one past the header
                 //header = mergefbr.get(i).readLine().split(",");

                 //if (i==0) bw.write(flattenArray(header,",")+"\n");

                 // get the first row
                 String[] line = mergefbr.get(i).readNext();
                 if (line != null)
                 {
                     //filerows.add(line.split("|"));
                     filerows.add(line);
                     someFileStillHasRows = true;
                 }
                 else 
                 {
                     filerows.add(null);
                 }

             }

             String[] row;
             int cnt = 0;
             while (someFileStillHasRows)
             {
                 String min;
                 int minIndex = 0;

                 row = filerows.get(0);
                 if (row!=null) {
                     //System.out.println("index:"+compareIndex+" size:"+row.length);
                     min = row[compareIndex];
                     minIndex = 0;
                 }
                 else {
                     min = null;
                     minIndex = -1;
                 }

                 // check which one is min
                 for(int i=1; i<filerows.size(); i++)
                 {
                     row = filerows.get(i);
                     if (min!=null) {

                         if(row!=null && row[compareIndex].compareTo(min) < 0)
                         {
                             minIndex = i;
                             min = filerows.get(i)[compareIndex];
                         }
                     }
                     else
                     {
                         if (row!=null)
                         {
                             min = row[compareIndex];
                             minIndex = i;
                         }
                     }
                 }

                 if (minIndex < 0) {
                     someFileStillHasRows=false;
                 }
                 else
                 {
                     // write to the sorted file
                     bw.append(flattenArray(filerows.get(minIndex),"|")+"\n");
                     
                     // Indices store up to 200 total rows
                     if ((indexLine % 20) == 0) {
                         indices[compareIndex].add(filerows.get(minIndex)[compareIndex] + "|" + compareIndex);
                    }
                    indexLine++;
                     
                     // get another row from the file that had the min
                     String[] line = mergefbr.get(minIndex).readNext();
                     if (line != null)
                     {
                         filerows.set(minIndex,line);
                     }
                     else 
                     {
                         filerows.set(minIndex,null);
                     }
                 }                                 
                 // check if one still has rows
                 for(int i=0; i<filerows.size(); i++)
                 {

                     someFileStillHasRows = false;
                     if (filerows.get(i)!=null) 
                     {
                         if (minIndex < 0) 
                         {
                             System.out.println("mindex lt 0 and found row not null" + flattenArray(filerows.get(i)," "));
                             System.exit(-1);
                         }
                         someFileStillHasRows = true;
                         break;
                     }
                 }

                 // check the actual files one more time
                 if (!someFileStillHasRows)
                 {

                     //write the last one not covered above
                     for(int i=0; i<filerows.size(); i++)
                     {
                         if (filerows.get(i) == null)
                         {
                             String[] line = mergefbr.get(i).readNext();
                             if (line!=null) 
                             {

                                 someFileStillHasRows=true;
                                 filerows.set(i, line);
                             }
                         }

                     }
                 }

             }

             // close all the files
             bw.close();
             fw.close();
             for(int i=0; i<mergefbr.size(); i++)
                 mergefbr.get(i).close();
             for(int i=0; i<mergefr.size(); i++)
                 mergefr.get(i).close();
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
             System.exit(-1);
         }
    }

}
