/*
Name: Daniel Shiang
Date: 2-6-2023
Course: CS622 - Spring 1 2023
Description: This file contains main method, merge method, search method and various hashmap print methods.
 */


import java.io.*; //I just like to import everything
import java.util.*;
//I was having trouble with the header separator using ",", so ChatGPT suggested I download this library into IntelliJ CE
//I know you mentioned during your session we have access to a wide range of libraries, so hopefully this is okay
//org.apache.commons:commons-csv from Maven Repository
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
//The time packages recommended by ChatGPT
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Main {


    //----------------------------------------------------------------------------------------------
    //Method: keyWordSearch - takes arguments: String keyword, String filePath, String column1, String column2
    /*Reads csv document in directory, reads with FileReader, and obtains all headers.
    Identifies relevant columns (column1 - title, column2 - tags, project_id, close_date, funds_raised_percent).
    Searches for title and tagline columns containing user-inputted keyword. Returns associated close_date,
    project_id, title (or tagline depending on merge quality), and funds_raised_percent (multiplied by 100).
     */
    public static Integer keyWordSearch(String keyword, String filePath, String column1, String column2) {
        //return resultsCount to add into hashMap
        int resultsCount = 0;

        //I know bufferedreader is overall more efficient, however since FileReader is specifically
        //for the reading of files, I decided to use it here
        try (Reader in = new FileReader(filePath)) {
            //Use iterable interface, go through the header names - The next couple lines from ChatGPT
            //I know it's not advisable to use deprecated methods, but I struggled and took the easy way
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            //add the headers to a list of strings
            List<String> headerList = new ArrayList<>(records.iterator().next().toMap().keySet());

            //identify column index for each column name
            int column1Index = headerList.indexOf(column1);
            int column2Index = headerList.indexOf(column2);
            int projectIdIndex = headerList.indexOf("project_id");
            int closeDateIndex = headerList.indexOf("close_date");
            int fundsRaisedPercentIndex = headerList.indexOf("funds_raised_percent");

            //iterate over the records in a vertical line
            //if keyword in column1 (title) or column2 (tagsline), then grab and print associated
            //Title (or tags if bad merge), Project ID (or Category if bad merge), close date and % fundraised
            for (CSVRecord record : records) { //this and next 2 lines from ChatGPT
                String title = record.get(column1Index);
                String tagline = record.get(column2Index);
                if (title.contains(keyword) || tagline.contains(keyword)) {
                    resultsCount++;//update resultsCount

                    System.out.println("Title or Tags: " + record.get(column1Index));
                    System.out.println("Project ID or Category: " + record.get(projectIdIndex));
                    System.out.println("Close Date: " + record.get(closeDateIndex));
                    System.out.println("Percent Fundraised of goal: " + Double.parseDouble(record.get((fundsRaisedPercentIndex)))*100 + "%");
                    System.out.println("-------------------------------------");
                }
            }
            //catch exception, printStackTrace
        } catch (Exception e) {
            e.printStackTrace();
        }
        //finish message
        //return "Finished search of Indiegogo projects with keyword: " + keyword;
        System.out.println("Results found: ");
        return resultsCount;
    }



    //Method: merge - takes arguments: String fileAddress
    /* Takes in directory and merges all csv files into one file, merged.csv
     */
    public static void merge(String fileAddress){
        //MERGE ALL CSV FILES----------------------------------------------------------------------
        //create File object, I hardcoded the absolute pathname on my computer
        File folder = new File(fileAddress);
        //create a list data structure with the list of files
        File[] fileList = folder.listFiles();

        //simple try/catch block to read, then write and merge files
        try {
            //message that reading/writing process has begun
            System.out.println("CSV reading and writing process has begun. Please wait a moment...");
            //using FileWriter class which extends OutputStreamWriter, I got this part from ChatGPT
            //the new file name will be called "merged.csv"
            FileWriter writer = new FileWriter(fileAddress + "/" + "merged.csv");

            //iterate over fileList, and if file is .csv, then write file into the merged file
            for (int i = 0; i < fileList.length; i++) {
                //if i is a file and the name has ".csv" ending, then write
                if (fileList[i].isFile() && fileList[i].getName().endsWith(".csv")) {
                    Scanner scan = new Scanner(fileList[i]);

                    while (scan.hasNextLine()) {
                        //write into merged csv, while csv has next line
                        writer.write(scan.nextLine() + "\n");
                    }
                    scan.close(); //close scanner
                }
            }
            writer.close(); //close writer
            System.out.println("Successfully completed file merging! Check the folder for new file");
        } catch (IOException e) {
            //more specific IOException
            e.printStackTrace();
        } catch (Exception e) {
            //more generalized catch
            System.out.println("Check your code again");
        }
    }

    //Method: searchRecordPrint
    public static void searchRecordPrint(HashMap<String, String> searchRecord){
        //Print out each individual search term's timestamp
        for (Map.Entry<String, String> entry : searchRecord.entrySet()) {
            System.out.println("Search Term Entered: " + entry.getValue() +" | "+ "Date & Time Searched: " + entry.getKey());
        }
    }

    //Method: searchCountPrint
    public static void searchCountPrint(HashMap<String, Integer> searchCount){
        //Print out unique Search Terms and frequency of terms searched
        for (Map.Entry<String, Integer> entry : searchCount.entrySet()) {
            System.out.println("Search Term: " + entry.getKey() +" | "+ "Number of Times Searched: " + entry.getValue());
        }
    }

    //Method: resultCountPrint
    public static void resultCountPrint(HashMap<String, Integer> resultCount){
        //Print out number of results per search term
        for (Map.Entry<String, Integer> entry : resultCount.entrySet()) {
            System.out.println("Search Term: " + entry.getKey() +" | "+ "Number of Results: " + entry.getValue());
        }
    }



//----------------------------------------------------------------------------------------------

    //MAIN
    public static void main(String[] args) {

        //Create Hashmap for storing timestamps as key, and search term as value
        HashMap<String, String> searchRecord = new HashMap<>();
        //Create Hashmap for storing searchterm as key and number of searches as value
        HashMap<String, Integer> searchCount = new HashMap<>();
        //Create Hashmap for storing searchterm as key and number of results per term
        HashMap<String, Integer> resultCount = new HashMap<>();

        //Start the file merging------------------------------------------------------------
        File rawAddress = new File("/Users/danielshiang/IdeaProjects/hw2_cs622/src/datasets");
        //File address
        String fileAddress = rawAddress.getAbsolutePath();
        //call the merge method to merge files
        merge(fileAddress);


        //SEARCH THE FILE FOR KEYWORD---------------------------------------------------------------------

        Scanner input = new Scanner(System.in); //open scanner
        boolean keepSearching; //declare this variable for later, to ask user if they want to search again.

        //I decided to put all the logic inside a do-while loop, since I was having trouble with normal while loop
        do {

            //Prompt user for search term input (I got this off stackoverflow and ChatGPT)
            System.out.print("Enter your search term: "); //prompt for search term
            String searchTerm = input.nextLine();

            //Create timestamp with date and time format, LocalDateTime suggested by ChatGPT
            //Add the timestamp (key) and search term (value) into hashmap searchRecord
            //Then add search term (key) with number of searches
            LocalDateTime timeStamp = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTimestamp = formatter.format(timeStamp);
            //put key value pair into hashmap
            searchRecord.put(formattedTimestamp, searchTerm);
            //put search term and number of times searched into hashmap
            //if the search term already exists, then increment value count by +1
            if (searchCount.containsKey(searchTerm)){
                int currentValue = searchCount.get(searchTerm);
                currentValue++;
                searchCount.put(searchTerm, currentValue);

            }else {
                searchCount.put(searchTerm, 1);
            }

            //message indicating beginning of search
            System.out.println("Searching for: " + searchTerm);
            System.out.println("Search may take a moment with large dataset...");//tell user to be patient


            //used getAbsolutePath to help with compatibility in other OS's
            File rawPath = new File("/Users/danielshiang/IdeaProjects/hw2_cs622/src/datasets/merged.csv");
            String filePath = rawPath.getAbsolutePath();

            //feed input into method, keyWordSearch, assign to variable s
            //I am keeping the keyWordSearch method and call, because the same code in the method
            //does not run properly in the KeywordSearcher class
            Integer s = keyWordSearch(searchTerm, filePath, "title", "tagline");

            /*
            I created a new KeywordSearcher class with a search method that is supposed to replace
            the keyWordSearch method in this main class. However, despite a couple hours of effort,
            I was unable to get it to read the merged csv properly. The same code works in this
            class, but does not read properly in the other class for some reason.

            I'm keeping this code here to demonstrate that I gave thought to OOP design, but due to
            some obscure issue, I cannot get the other class to read a csv properly.
             */
            //KeywordSearcher keywordLook = new KeywordSearcher(searchTerm, filePath, "column1", "column2");
            //int s = keywordLook.search();

            //print out results
            System.out.println(s);
            //add to resultCount hashmap
            resultCount.put(searchTerm, s);

            //prompt user to search again or end search session
            System.out.println("Do you want to search again? "); //prompt user to search again or not
            System.out.println("Enter 'yes' or 'y' to continue. To conclude session, enter anything else or nothing.");
            //String keepSearching = input.nextLine();
            String choice = input.nextLine(); //user type 'yes' or 'y' to search again. Otherwise stop session.
            if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y")){
                keepSearching = true;
            } else {
                keepSearching = false;
            }
        } while (keepSearching);

        input.close(); //close
        System.out.println("Search session has concluded. Here is a summary: "); // close message

        //Call searchRecordPrint to print
        searchRecordPrint(searchRecord);

        //Call searchCountPrint to print
        searchCountPrint(searchCount);

        //Call resultCountPrint to print
        resultCountPrint(resultCount);

        //-----------------------------------------------------------------------------------------------
    }



}