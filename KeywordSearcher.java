/*
Name: Daniel Shiang
Date: 2-6-2023
Course: CS622 - Spring 1 2023
Description: This class is supposed to search for keyword input. This class is not used
due to technical difficulties, despite using same code as the keyWordSearch method in main.

I have decided to submit this class to demonstrate that I made an attempt at trying OOP concepts with
breaking down a bloated method into a class.
 */




import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class KeywordSearcher {
    //declare
    private int resultsCount = 0;
    private String keyword;
    private String filePath;
    private String column1;
    private String column2;

    //constructor
    public KeywordSearcher(String keyword, String filePath, String column1, String column2) {
        this.keyword = keyword;
        this.filePath = filePath;
        this.column1 = column1;
        this.column2 = column2;
    }

    public Integer search() {
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
}

