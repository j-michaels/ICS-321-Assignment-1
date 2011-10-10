package ics321;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;

// because Java is so completely stupid about converting Floats to strings and back again,
// they have to be wrapped in this class.
class KeyValue implements Comparable {
    Comparable key;
    String value;
    
    public KeyValue(Comparable akey, String avalue) {
        key = akey;
        value = avalue;
    }
    
    public Comparable getValue() { return this.key; }
    public String toString() { return value; }

    

    public int compareTo(Object otherThing) {
        if (otherThing instanceof KeyValue) {
            return key.compareTo(((KeyValue)otherThing).getValue());
        } else {
            return key.compareTo(otherThing);
        }
    }
    
    public boolean equals(Object otherObj) {
        if (otherObj instanceof KeyValue) {
            return key.equals((KeyValue)otherObj.getValue());
        } else {
            return key.equals(otherObj);
        }
    }
     
    
}
// simple struct for the indices
class IndexNode implements Comparable<IndexNode> {
    Comparable value;
    int location;
    
    public IndexNode(Comparable val, int loc) {
        value = val;
        location = loc;
    }
    
    public int compareTo(IndexNode otherNode) {
        //if ((otherNode == null) || (value == null)) { return 0; }
        //else {
            Comparable otherValue = otherNode.getValue();
            
            //if ((value instanceof Integer) || (otherValue instanceof String)) {
                //System.out.println(this);
            //}
            
            return value.compareTo(otherNode.getValue());
        //}
    }
    
    public boolean equals(IndexNode otherNode) {
        if ((otherNode == null) || (value == null)) { return false; }
        else {
            return value.equals(otherNode.getValue());
        }
    }
    
    public String toString() {
        return ((Object)value).toString();
    }
    public Comparable getValue() { return value; }
    public int getLocation() { return location; }
    
}

public class Assignment1 {
    ArrayList<IndexNode>[] indices; // column:array
    int MaxLinesToRead = 6000;
    int TotalIndices = 500;
    int indexInterval;
    int totalColumns;
    int cardinality;
    int[] columnTypes;
    HashSet<Integer> columns;
    String filen;
	
	//load takes a fileName as an argument and
	//reads in the CSV file at the given path.
	//The data may not all fit in memory
	public void load(String fileName, HashSet<Integer> cols)
	{
		try {
			CSVReader reader =  new CSVReader(new FileReader(fileName),'|');
		    filen = fileName;
		    columns = cols;
		    cardinality = -1;
		    indexInterval = MaxLinesToRead / TotalIndices;
			System.out.println("load "+fileName);
            totalColumns = java.util.Collections.max(columns)+1;
            indices = (ArrayList<IndexNode>[])new ArrayList[totalColumns];
            Iterator<Integer> itr = columns.iterator();
            
            //System.out.println("Initializing indices.");
            Integer column;
            while (itr.hasNext()) {
                column = itr.next();
                //System.out.println("Col: '"+column+"'");
                indices[column] = new ArrayList<IndexNode>();
            }
            
            externalSort(fileName);

            itr = columns.iterator();
            // sort the generated indices
            while (itr.hasNext()) {
                column = itr.next();
                //System.out.println("Sorting indices column "+column);
                /*Iterator<IndexNode> itr2=indices[column].iterator();
                while (itr2.hasNext()) {
                    String kind ="";
                    Comparable b = itr2.next().getValue();
                    if (b instanceof Float) {
                        kind="float";
                    } else if (b instanceof Integer) {
                        kind = "Int";
                    } else { kind = "String";}
                    //System.out.print(kind+": " + b + ", ");
                }
                System.out.println(";");*/
                //System.out.println(flattenArray(indices[column], ", "));
                
                
                java.util.Collections.sort(indices[column]);
            }
            
			/*
    	        Plan:
    	            Load up to memory limit
    	            Quicksort that, dump to file data.1
    	            Repeat until all data is sorted in data.n files
    	            Merge sort data.n files
    	                n-way comparison, immediately read to output file all_data
	                
	            
    	            load index for each column into memory
    	    */
    	    //System.out.println("INDICES!!!!!!");
    	    /*Iterator<Integer> itr1 = columns.iterator();
    	    while (itr1.hasNext()) {
    	        Integer column1=itr1.next();
    	        Iterator<IndexNode> itr2 = indices[column1].iterator();
    	        while(itr2.hasNext()) {
    	            IndexNode in = itr2.next();
    	            
    	            System.out.println(in.getValue());
    	        }
    	    }*/
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
            System.out.println("Malformed commands input.");
            e.printStackTrace();
        }
	}
	
	
	// findInIndex takes the column number and value and
	// finds the closest location in the datafile
	public int findInIndex(int columnNumber, Comparable value)
	{
	    // currently linear search O(n) time
	    // find the most great index that is less than over the index of the value
	    //eg
	    // looking for: "candy"
	    // alfred, baseball, fred, soccer, zaphod
	    // ^ greater than, store index and keep going
	    //         ^ same
	    //                   ^ less than, exit
	    
	    //System.out.println("Total indices for column "+columnNumber +": " + indices[columnNumber].size());
	    /*Iterator<IndexNode> itr = indices[columnNumber].iterator();
	    int gtrthanindex = 0;
	    String nextLine;
	    while ((nextLine = itr.next()) != null) {
	        //String[] line = nextLine.split("|");
	        
	        if (line.compareTo(value) >= 0) {
	            gtrthanindex = Integer.parseInt(line[1]);
            } else {
                break;
            }
	    }
	    return gtrthanindex;*/
	    IndexNode compareNode = new IndexNode(value, 0);
	    //System.out.println("Searching column "+columnNumber+"for "+value.toString());
	    return java.util.Collections.binarySearch(indices[columnNumber], compareNode);
	}
	
	//searchEq takes a columnNumber and a value and prints
	//tuples that match the given value on the given column.
	//More points will be given for faster return of this method
	public void searchEq(int columnNumber, String value)
	{
	    //System.out.println("Converting '"+value+"' to int or float");
	    Comparable b = convertIntOrFloat(value);
	    //System.out.print("Converted.");
	    //if (b instanceof Float) { System.out.println(" Now a Float."); }
	    //else if (b instanceof String) { System.out.println(" Still a String."); }
	    //else if (b instanceof Integer){ System.out.println(" Now an Integer."); } 
	    
	    try {
	        System.out.println("searchEq col #"+columnNumber+"="+value);
	        columnNumber--;
    		String [] nextLine;
    		// Find in the index
    		int j = findInIndex(columnNumber, b)-indexInterval;
    		if (j < 0) { j = 0; }
    		
    		String filenc = "file_sorted_col_" + columnNumber+".csv";
    		//System.out.println("Opening file "+filenc+" at position "+j+", looking for column "+columnNumber);
    		CSVReader reader = new CSVReader(new FileReader(filenc), '|',  CSVParser.DEFAULT_QUOTE_CHARACTER, j);
    		while ((nextLine = reader.readNext()) != null) {
    		    //System.out.print("Seq "+columnNumber+"; col0: "+ nextLine[0]+ "; sc: '"+nextLine[columnNumber]+"' vs '"+value+"': ");
    		    if (nextLine[columnNumber].equals(b)) {
    		        //System.out.println("Yes.");
    		        System.out.println(flattenArray(nextLine, "|"));
    		        
    		        //for( int i=0; i<nextLine.length-1; i++) {
       			    //  System.out.print(nextLine[i] + "|");
                    //}
    		    } else {
    		        //System.out.println(" No.");
    		        break;
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
	        Comparable compareValue = null;
	        if (columnTypes[columnNumber] == 0) {
	            compareValue = new Integer((int)value);
	        } else if (columnTypes[columnNumber] == 2) {
	            compareValue = String.valueOf(value);
	        } else {
	            compareValue = new Float(value);
	        }
    		//System.out.println("searchEq col #"+columnNumber+"="+value);
    		String [] nextLine;
    		// Find in the index
    		int j = findInIndex(columnNumber, compareValue)-indexInterval;
    		if (j < 0) { j = 0; };
    		String filenc = "file_sorted_col_" + columnNumber+".csv";
    		//System.out.println("Opening file "+filenc+" at position "+j+", looking for column "+columnNumber);
    		CSVReader reader = new CSVReader(new FileReader(filenc), '|',  CSVParser.DEFAULT_QUOTE_CHARACTER, j);
    		while ((nextLine = reader.readNext()) != null) {
    		    
    		    // because java is retarded when converting between types
    		    Comparable compareLine = null;
    	        if (columnTypes[columnNumber] == 0) {
    	            compareLine = Integer.parseInt(nextLine[columnNumber]);
    	        } else if (columnTypes[columnNumber] == 2) {
    	            compareLine = nextLine[columnNumber];
    	        } else {
    	            compareLine = Float.parseFloat(nextLine[columnNumber]);
    	        }
    		    if (compareLine.compareTo(compareValue) > 0) {
    		        //System.out.println("OOOGAAAA");
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
    private ArrayList<Comparable[]> mergeSort(ArrayList<Comparable[]> arr, int index)
    {
         ArrayList<Comparable[]> left = new ArrayList<Comparable[]>();
         ArrayList<Comparable[]> right = new ArrayList<Comparable[]>();
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
    private ArrayList<Comparable[]> merge(ArrayList<Comparable[]> left, ArrayList<Comparable[]> right, int index)
    {
         ArrayList<Comparable[]> result = new ArrayList<Comparable[]>();
         while (left.size() > 0 && right.size() > 0)
         {
             //if (left.get(0)[index] == null) {System.out.println("OOOH "+index+": "+left.get(0).length);}
             //System.out.println("Left: '"+ left.get(0)[index] + "'; Right: '" + right.get(0)[index]+"'");
             //Left: '160882.76'; Right: '31084.79'
             Object newleft;
             Object newright;
             
             
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
	
	private void externalSort(String relation)
    {
         try
         {
             FileReader initialRelationInput = new FileReader(relation); 
             //BufferedReader initRelationReader = new BufferedReader(intialRelationInput);
 			CSVReader initRelationReader =  new CSVReader(initialRelationInput,'|');
            // String [] header = ;
             Comparable [] row = null;// = initRelationReader.readNext();
             boolean justStarted = true;
             //int indexToCompare = getIndexForColumn(header,attribute);
             ArrayList<Comparable[]> tenKRows = new ArrayList<Comparable[]>();
             ArrayList<Comparable[]> sortedTenKRows = new ArrayList<Comparable[]>();
            
            //System.out.println("Reading 1000 lines from input filename "+relation+"...");
             int numFiles = 0;
             while ((row!=null) || (justStarted==true))
             {
                 justStarted = false;
                 //System.out.println("starting set "+numFiles+1);
                 // get rows
                 for(int i=0; i<MaxLinesToRead; i++)
                 {
                     String[] line = initRelationReader.readNext();
                     if (line==null)
                     {
                         row = null; // make sure it breaks out of the while loop
                         break;
                     } else { // convert floats and integers to their classes
                         if ((cardinality == -1) && (line.length > 0)) {
                             cardinality = line.length;
                             //System.out.println("Initializing columnTypes at length "+cardinality);
                             columnTypes = new int[line.length];
                         }
                        
                         row = line; // just to make sure it doesn't break out of the while loop
                         Comparable[] rowAdded = new Comparable[line.length];
                         for (int x=0; x<line.length; x++) {
                             int columnType=-1;
                             try {
                                 Integer in = Integer.parseInt((String)line[x]);
                                 KeyValue kv = new KeyValue(in, line[x]);
                                 
                                 rowAdded[x] = kv;
                                 columnType = 0;
                             } catch (NumberFormatException e) {
                                 
                             }
                             
                             if (rowAdded[x] == null) {
                            //if (columnType == -1) {
                                try {
                                     Float f = Float.parseFloat((String)line[x]);
                                     columnType = 1;
                                     /*if (String.format("%.2f", f).equals((String)line[x])) {
                                     //    System.out.println("FOOO!!");*/
                                     KeyValue kv = new KeyValue(f, line[x]);
                                        rowAdded[x] = kv;
                                        columnType = -1;

                                 } catch (NumberFormatException e) { }
                             }
                            if (rowAdded[x] == null) {
                                rowAdded[x] = line[x];
                             if (columnType == -1) columnType=2; }
                             //System.out.println("OOGGGA: '"+line[x] + "'");}
                             columnTypes[x] = columnType;
                         }
                         //System.out.println("Adding row: "+flattenArray(rowAdded, "|"));
                         tenKRows.add(rowAdded);
                         
                     }
                     
                     //row = line.split("|");
                     //row = line;
                     //tenKRows.add(getIntsFromStringArray(row));
                     //tenKRows.add(row);
                 }
                 // sort the rows according to the index
                 //System.out.println("Sorting the input and dumping to a new file.");
                //for (int i=1; i<4 ;i++) {
                Iterator<Integer> itr = columns.iterator();
                while (itr.hasNext()) {
                    int i = itr.next();
                    //if ((i == 2) || (i == 1) || (i == 3)) {
                    sortedTenKRows = mergeSort(tenKRows, i);
                     // write to disk
                     FileWriter fw = new FileWriter("file_chunk" + numFiles + "_col"+i+".csv");
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
             Iterator<Integer> itr = columns.iterator();
            while (itr.hasNext()) {
                int i = itr.next();
                mergeFiles("file", numFiles, i);
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
    
    private String flattenArray(Object[] array, String joinstr) {
        String outstr = "";
        //boolean null_happened = false;
        for (int i=0; i<array.length; i++){
            /*if (array[i] == null) {
                null_happened = true;
                outstr = outstr + "NULL" + joinstr;
                System.out.println("NULL!");
            }
            else */
            if (array[i].equals("") != true) {
                if (array[i] instanceof Float) {
                    String s = String.format("%.3f", array[i]);
                    
                    System.out.println("Original: "+array[i] +"; new: "+s.substring(0, s.length()-1));
                    outstr = outstr + String.format("%.2f",array[i]);
                } else {
                    outstr = outstr + array[i].toString();
                }
                outstr = outstr + joinstr;
            }
        }
        //if (null_happened) {
        //System.out.println(outstr); }
        return outstr;
    }
    
    private Comparable convertIntOrFloat(String input) {
        Comparable output = null;
        try {
            Integer in = Integer.parseInt(input);
            
            output = new KeyValue(in, input);
        } catch (NumberFormatException e) {
            
        }
        if (output == null) {
            try {
                Float f = Float.parseFloat(input);
                output = new KeyValue(f, input);
            } catch (NumberFormatException e) {
            }
        }
        if (output == null) {output = input;}
        return output;
    }

    private Comparable[] convertIntsAndFloats(String[] input, int index) {
        Comparable[] output = new Comparable[input.length];
        for (int i=0; i<input.length;i++) {
            if (i != index) {
                output[i] = input[i];
            }
        }
        /*try {
            Integer in = Integer.parseInt(input[index]);
            output[index] = in;
        } catch (NumberFormatException e) {
            
        }
        if (output[index] == null) {
            try {
                Float f = Float.parseFloat(input[index]);
                output[index] = f;
            } catch (NumberFormatException e) {
            }
        }
        if (output[index] == null) {output[index] = input[index];}
        */
        output[index] = convertIntOrFloat(input[index]);
        return output;
    }

    private void mergeFiles(String relation, int numFiles, int compareIndex)
    {
         try
         {
             ArrayList<FileReader> mergefr = new ArrayList<FileReader>();
             ArrayList<CSVReader> mergefbr = new ArrayList<CSVReader>();
             ArrayList<Comparable[]> filerows = new ArrayList<Comparable[]>(); 
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
                     //System.out.println("Before:"+flattenArray(line, "|"));
                     //System.out.print("After:");
                     filerows.add(convertIntsAndFloats(line, compareIndex));
                     someFileStillHasRows = true;
                 }
                 else 
                 {
                     filerows.add(null);
                 }

             }

             Comparable[] row;
             int cnt = 0;
             while (someFileStillHasRows)
             {
                 Comparable min;
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

                     // Indices store up to indexInterval total rows                     
                    if (indexLine % indexInterval == 0) {
                                                
                        IndexNode n = new IndexNode(filerows.get(minIndex)[compareIndex], compareIndex);
                        //System.out.println("Indexline: " + indexLine);
                        //System.out.println("Total: " + totalColumns);
                        indices[compareIndex].add(n);
                    }
                    
                    indexLine++;
                     
                     // get another row from the file that had the min
                     String[] line = mergefbr.get(minIndex).readNext();
                     if (line != null)
                     {
                         filerows.set(minIndex,convertIntsAndFloats(line, compareIndex));
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
