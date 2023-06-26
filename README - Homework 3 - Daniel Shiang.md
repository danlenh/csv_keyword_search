README - Homework 3 - CS622 - Daniel Shiang

This homework submission consists of two classes, Main and KeywordSearcher.

Note: KeywordSearcher class is submitted to demonstrate understanding and an attempt at breaking down a previously bloated method, keyWordSearch, as per Ed Orsini's recommendations. However, due to technical difficulties with the class properly reading CSV files (out of bounds error), despite using the same file reading/search code as the method, all calls to the method have been commented out in the main class. Many hours were spent adjusting the class, however, the method works and the class doesn't. And in the Main class, the bloated method, keyWordSearch will be retained for the sake of having a functioning program. There were experiments with dividing up the keyWordSearch into two methods, with a helper method being the second, however for the sake of cohesion, it was decided to retain the method as is. 

Main Class Functions:
Main class consists of 5 methods: main, keyWordSearch, merge, searchRecordPrint, searchCountPrint, and resultCountPrint. The main class merges all csv files in a "dataset" folder located in the src into a single csv file, and subsequently performs a search of the merged file for the user-inputted keyword. Associated information, such as project close date, project funding percentage of goal, project title (or tags depending on merge quality), and project id number (or category depending on merge quality).

After the merged file is searched by user, there is a prompt which asks the user to repeat search (continue search session) or to conclude the search session. The user may search for new keywords indefinitely, limited to the boundaries of system memory. 

Search terms, search term frequency, search result quantity (per search term), and timestamp (date and time) per search are stored in 3 HashMaps. searchRecord stores timestamps as key and search term as value. searchCount stores search term as key and number of searches as value. resultCount stores search term as key and number of results for search as value. These values are later respectively passed into methods searchRecordPrint, searchCountPrint, and resultCountPrint to print search results for user at the conclusion of the search session. 

 

Class: Main
- Performs csv file merging and keyword search of merged file. 
- Prompts user to perform new search or end search session.
- Stores search information into hashmaps, as described in the previous section.
- At conclusion of search session, provides user with search terms used, number of results per search term, frequency of search terms used, and timestamps (date and time) per search. 
- Methods: 
    - keyWordSearch: searches merged file for keyword and returns results.
    - merge: merges all CSV files in folder into single file, merged.csv
    - searchRecordPrint: prints the timestamps and associated search term stored in the searchRecord HashMap. 
    - searchCountPrint: prints the search term and search term frequency stored in the searchCount HashMap. 
    - resultCount: prints the search term and number of results per search term stored in the searchRecord HashMap. 
    - main: Passes file address to merge method, prompts user to input search term, prompts user to search again or conclude search, and calls all print methods to display search information. 

**Class: KeywordSearcher  
    - This class's call in the main has been commented out.
    - As described this class has been problematic, and is only submitted to demonstrate an attempt of converting a bloated method into a class. 
    - This class performs the same function as the keyWordSearch method in the Main class. 
    - Method: search, performs a search of the CSV file for inputted

Operation and Activities of the Program
1. When the class is run, all csv files in the hardcoded datasets folder in the src are involved in the merging process. The user recieves a message indicating process has begun. 
2. After merged CSV file is completed, user recieves a message.
3. User is then prompted to type in a keyword for the file search. 
4. User recieves a message indicated their input and a follow-up message that the search process has begun. 
5. Results appear looking like:

Title or Tags: Create Apps Online

Project ID or Category: 715470

Close Date: 2014-05-06T23:59:59-07:00

Percent Fundraised of goal: 27.700000000000003%

**If user inputs a keyword with no results, then no results appear.


6. User recieves a prompt to enter "yes" or "y" to repeat search process again. If the user chooses to search again, the user will start again at step 3. 

7. If the user inputs anything else or nothing, then search concludes.

8. At search conclusion, user will get a summary of their search activity, as described in above sections. It will look like:

Search session has concluded. Here is a summary: 
Search Term Entered: spanish | Date & Time Searched: 2023-02-07 05:52:05
Search Term Entered: korean | Date & Time Searched: 2023-02-07 05:51:29
Search Term Entered: sushi | Date & Time Searched: 2023-02-07 05:53:13
Search Term Entered: korean | Date & Time Searched: 2023-02-07 05:52:33
Search Term: spanish | Number of Times Searched: 1
Search Term: sushi | Number of Times Searched: 1
Search Term: korean | Number of Times Searched: 2
Search Term: spanish | Number of Results: 48
Search Term: sushi | Number of Results: 58
Search Term: korean | Number of Results: 0

