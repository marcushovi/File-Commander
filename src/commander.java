import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


public class commander {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean run = true;
        String [][] content = {{" "}};
        String  filePath = "" , fisuff = "";


        System.out.println("Type help ...");


        while (run) {

            // load command and split it into two variables (make two parts from command)
            String commands = sc.nextLine(), comm = cmdSplit(commands)[0], comm2 = cmdSplit(commands)[1];

            // switch with all commands
            switch (comm) {
                // list all commands and descriptions to them
                case "help":
                    if  (fileExist("commandDocumantion.txt") && comm2.equals(""))
                        printOutTxtFile(readTxtFile("commandDocumantion.txt"));
                    else if (!comm2.equals(""))
                        System.out.println("\tInvalid command\n");
                    break;
                // open file
                case "of":
                    if (fileExist(comm2)) {
                        filePath = comm2;
                        fisuff = fileSuffix(filePath);// check if file exists
                        content= openfile(filePath, fisuff);
                        // check if was opened some file
                        if  (! content[0][0].equals(" "))
                            printFile(content,fisuff);
                    }
                    break;
                // end program
                case "exit":
                    if  (comm2.equals(""))
                        run = false;
                    else
                        System.out.println("\tInvalid command\n");
                    break;
                // save file with changes
                case "save" :
                    if  (comm2.equals("")) {
                        saveFile(content, filePath, fisuff);
                    }
                    else
                        System.out.println("\tInvalid command\n");
                    break;
                // print out file
                case "pt" :
                    if  (comm2.equals(""))
                        printFile(content,fisuff);
                    else
                        System.out.println("\tInvalid command\n");
                    break;
                case "" :
                    if (!comm2.equals(""))
                        System.out.println("\tInvalid command\n");
                    break;

                // command for txt files

                // add new line to txt files
                case "nl" :
                    if (checkFileSuffix(fisuff, "txt",null)) {
                        content[0][0] += "\n";

                    }break;
                // add text to txt files
                case "adtx" :
                    if (checkFileSuffix(fisuff, "txt",null)) {
                        content[0][0] += comm2;
                        printOutTxtFile(content);
                    }break;
                // print how many times the specific text is there
                case "contx" :
                    if (checkFileSuffix(fisuff, "txt",null)) {
                        containsText(content,comm2);
                    }break;

                // command for csv files

                // add row to csv file
                case "adro" :
                    if (checkFileSuffix(fisuff, "csv" , "tsv")) {
                        content = addRowToCsvAndTsvFile(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;
                // add column to csv file
                case "adcol" :
                    if (checkFileSuffix(fisuff, "csv" , "tsv")) {
                        content= addColToCsvAndTsvFile(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;
                // remove column to csv file
                case "remcol" :
                    if (checkFileSuffix(fisuff, "csv" , "tsv")) {
                        content = removeColInCsvAndTsvFile(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;
                // remove row to csv file
                case "remro" :
                    if (checkFileSuffix(fisuff, "csv" ,"tsv")) {
                        content = removeRowInCsvAndTsvFile(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;
                // change text of cell for another
                case "chace" :
                    if (checkFileSuffix(fisuff, "csv" ,"tsv")) {
                        content = changeValueOfCell(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;
                // remove cell (remove text for space)
                case "remce" :
                    if (checkFileSuffix(fisuff, "csv" ,"tsv")) {
                        content = removeTextOfCell(content,comm2);
                        printOutCsvAndTsvFile(content);
                    }break;

                default:
                    System.out.println("\tInvalid command\n");
            }
        }
    }

    // open file
    public static String[][] openfile(String filePath ,String suffix) {

        String [][] content= {{" "}};

        // call method by the suffix
        switch (suffix) {
            case "csv" :
                content = readCsvFile(filePath);
                break;
            case "txt" :
                content = readTxtFile(filePath);
                break;
            case "tsv" :
                content = readTsvFile(filePath);
                break;
            default:
                System.out.println("\tThis type of file can not be open\n");
        }
        return content;
    }

    // print out file
    public static void printFile(String[][] content, String suffix) {

        // call method by the suffix
        switch (suffix) {
            case "csv" :
                printOutCsvAndTsvFile(content);
                break;
            case "txt" :
                printOutTxtFile(content);
                break;
            case "tsv" :
                printOutCsvAndTsvFile(content);
                break;
            default:
                System.out.println("\tFile was not opened\n");
        }
    }

    // save file
    public static void saveFile(String[][] content,String filePath, String suffix) {

        // call method by the suffix
        switch (suffix) {
            case "csv" :
                saveCsvFile(content,filePath);
                break;

            case "txt" :
                saveTxtFile(content,filePath);
                break;
            case "tsv" :
                saveTsvFile(content,filePath);
                break;
            default:
                System.out.println("\tFile was not opened\n");
        }
    }
    public static boolean checkFileSuffix(String suffix, String suffForMeth , String suffForMeth2) {

        // check if suffix from loaded file equals with suffix for specific method
        if (suffix.equals(suffForMeth) || suffix.equals(suffForMeth2))
            return true;

            // check if file was loaded
        else if (suffix.equals("")){
            System.out.println("\tFile was not opened\n");
            return false;
        }
        // else return false because this method which was called is not for current loaded file
        else {
            System.out.println("\tThis method is not for "+suffix+" files\n");
            return false;
        }
    }

    // return suffix of file
    public static String fileSuffix(String filePath) {

        // check if file was defined
        if (!filePath.equals("")) {
            return filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        else
            return "";
    }


    // check if file exists
    public static boolean fileExist(String filePath) {

        File file = new File(filePath);
        // check if file was defined
        if (filePath.equals(""))
            System.out.println("\tFile is not defined\n");
            // if file doesnt exists
        else if (! file.exists())
            System.out.println("\tFile not exists\n");

        return file.exists();
    }
    // split command into two variables
    public static String[] cmdSplit(String comms){

        String [] field = new String[2];
        // if comms was defined with space split it
        if (comms.contains(" ")){
            field[0] = comms.substring(0,comms.indexOf(" "));
            field[1] = comms.substring(comms.indexOf(" ") + 1);
        }
        // else return this
        else {
            field[0] = comms;
            field[1] = "";
        }
        return field;
    }

    // auxiliary method for removetextOfCell() and changeValueOfCell()
    public static boolean  correctParameters(String[][] table , String comm2 ) {

        // check if can split it and if was defined which cells
        if (! comm2.equals("") && comm2.contains(" ")) {

            // splits it
            String[] sc = comm2.split(" ");


            int num1 = 0 , num2 = 0;

            // count how much digits are there
            for (int i = 0; i < sc[0].length() ; i++) {
                if (Character.isDigit(sc[0].charAt(i)))
                    num1++;
            }
            for (int i = 0; i < sc[1].length() ; i++) {
                if (Character.isDigit(sc[1].charAt(i)))
                    num2++;
            }

            // here check if indexes length equals count numbers there (if there are only digits)
            if (num1 == sc[0].length() && num2 == sc[1].length()) {

                // define x and y for field
                int x = Integer.parseInt(sc[0]) - 1;
                int y = Integer.parseInt(sc[1]) - 1;

                // check if indexes are not out of bound
                if (x < table[0].length && x >= 0 && y < table.length && y >= 0) {
                    return true;
                }
                else {
                    System.out.println("\tIndexes are out of bound\n");
                    return false;
                }
            }
            else {
                System.out.println("\tIndexes of cell are incorrect define\n");
                return false;
            }
        }
        else {
            System.out.println("\tDefine which cell and some text\n");
            return false;
        }
    }
    // remove text from specific cell (change text for space) in table
    public static String[][] removeTextOfCell(String[][] table , String comm2 ){

        // call method which check all possible issues
        if (correctParameters(table,comm2)) {

            String[] sc = comm2.split(" ");
            // check if was defined just indexes nothing else
            if (sc.length == 2) {

                // define x and y for field
                int x = Integer.parseInt(sc[0]) - 1;
                int y = Integer.parseInt(sc[1]) - 1;

                // change "remove" text of cell
                table[y][x] = " ";
                return table;
            }
            else {
                System.out.println("\tIncorrect define indexes\n");
                return table;
            }
        }
        else {
            return table;
        }
    }
    // change text of cell in table
    public static String[][] changeValueOfCell(String[][] table, String comm2) {

        // call method which check all possible issues
        if (correctParameters(table,comm2)) {

            String[] sc = comm2.split(" ");
            // check if was defined at least 3 parameters
            if (sc.length >= 3) {

                String value = "";

                // define x and y for field
                int x = Integer.parseInt(sc[0]) - 1;
                int y = Integer.parseInt(sc[1]) - 1;

                // sum all elements from 2 to end of field (it is text which will change text in other cell)
                for (int i = 2; i < sc.length; i++) {
                    value += sc[i] + " ";
                }
                // changing cell
                table[y][x] = value;
                return table;
            }
            else {
                System.out.println("\tText is not define\n");
                return table;
            }
        }
        else {
            return table;
        }
    }

    // remove columns in table
    public static String[][] removeColInCsvAndTsvFile(String[][] table, String comm2) {
        // check if was defined which column
        if (!comm2.equals("")) {

            // change string to int variable
            int col = Integer.parseInt(comm2) - 1;

            //check if the column number is from table range
            if (col < table[0].length && col >= 0) {

                // create auxiliary 2D field where the count of columns is one less
                String field[][] = new String[table.length][table[0].length - 1];

                // loops where is rewrite variable table to variable field without defined column
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0, z = 0; j < table[i].length; j++, z++) {
                        // if j == defined column then skip it
                        if (j != col)
                            field[i][z] = table[i][j];
                        else
                            z--;
                    }
                }
                return field;
            } else {
                System.out.println("\tColumn at " + (col + 1) + " doesnt exists\n");
                return table;
            }
        }
        else {
            System.out.println("\tYou must define which column\n");
            return table;
        }
    }
    // remove row in table
    public static String[][] removeRowInCsvAndTsvFile(String[][] table, String comm2) {
        // check if was defined which row
        if (!comm2.equals("")) {

            // change string to int variable
            int row = Integer.parseInt(comm2) - 1;

            // check if defined row is from table range
            if (row < table.length && row >= 0) {

                // create auxiliary 2D field where the count of rows is one less
                String[][] field = new String[table.length - 1][];

                // loops where is rewrite variable table to variable field without defined row
                for (int i = 0, z = 0; i < table.length; i++, z++) {
                    // if i == row then skip it
                    if (i != row)
                        field[z] = Arrays.copyOf(table[i], table[i].length);
                    else
                        z--;

                }
                return field;
            } else {
                System.out.println("\tRow at " + (row + 1) + " doesnt exists\n");
                return table;
            }
        }
        else {
            System.out.println("\tYou must define which row\n");
            return table;
        }
    }

    // add columns in table
    public static String[][] addColToCsvAndTsvFile(String[][] table ,String comm2) {
        // check if was defined text for cells
        if (!comm2.equals("")) {
            //create auxiliary  1D field
            String[] pom = comm2.split(" ");

            // create auxiliary 2D field
            String[][] field = new String[table.length][];

            // loop where each row is copy to field with one extra element at each row
            for (int i = 0; i < table.length; i++) {
                field[i] = Arrays.copyOf(table[i], table[i].length + 1);
            }
            // check if count of texts is less than count of columns
            if (pom.length <= table[0].length) {

                //loop where new column is fill with defined text
                for (int i = 0; i < field.length; i++) {
                    for (int j = field[i].length - 1; j < field[i].length; j++) {
                        // if i > pom length then fill others cell with space
                        if (i < pom.length)
                            field[i][j] = pom[i];
                        else
                            field[i][j] = " ";
                    }
                }
            }
            else {
                System.out.println("\tA lot of text for elements\n");
                return table;
            }
            return field;
        }
        else {
            System.out.println("\tText for cells was not defined\n");
            return table;
        }
    }
    // add row to csv file
    public static String[][] addRowToCsvAndTsvFile(String[][] table ,String comm2) {
        // check if was defined text for cells
        if (! comm2.equals("")) {
            // create auxiliary 2D field where the count of rows is one more
            String[][] field = new String[table.length + 1][];
            try {
                // loop where each row is copy to field
                for (int i = 0; i < table.length; i++) {
                    field[i] = Arrays.copyOf(table[i], table[i].length);
                }
                // define size of new row (field)
                field[table.length] = new String[table[0].length];
                // fill new row with space
                Arrays.fill(field[table.length], " ");
                // create auxiliary 1D field where split new text of elements in new field
                String[] pom = comm2.split(" ");

                // check if count of texts is less than count of columns
                if (pom.length <= table[0].length) {

                    for (int i = 0; i < field[0].length; i++) {
                        if (i < pom.length)
                            field[table.length][i] = pom[i];
                    }
                }
                else {
                    System.out.println("\tA lot of text for elements\n");
                    return table;
                }
            }
            catch (Exception e){

            }

            return field;
        }
        else {
            System.out.println("\tText for cells was not defined\n");
            return table;
        }
    }

    // load csv file
    public static String[][] readCsvFile(String filePath )
    {
        String [][] table = declaredTable(filePath);

        try {
            // create CSVWriter object filewiter object as parameter
            CSVReader reader = new CSVReader(new FileReader(filePath));

            String[] nextRecord;

            int i = 0;
            while ((nextRecord = reader.readNext()) != null) {

                for (int j = 0; j < table[i].length; j++) {

                    if (nextRecord.length > j) {
                        if (!nextRecord[j].equals(""))
                            table[i][j] = nextRecord[j];
                    }
                }
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    // save csv file
    public static void saveCsvFile(String[][] table , String filePath)
    {
        try {
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));

            // add data to csv
            for (int i = 0; i < table.length ; i++) {
                writer.writeNext(table[i]);
            }

            writer.close();
        }
        catch (IOException e) {

        }
    }

    // auxiliary function for print tables function
    public static String spaces(String str)
    {
        String space = "";
        // determines wide of column (how much characters can be in one cell)
        // and return how much spaces can be ther with the text of cell
        for (int i = 0; i < (18 - str.length()) ; i++) {
            space += " ";
        }
        return space ;
    }
    // print csv file
    public static void  printOutCsvAndTsvFile(String[][] table){

        System.out.print("      ");
        // print letters of columns
        for (int i = 65; i < table[0].length + 65; i++) {
            System.out.print((char) i + spaces(" "));
        }
        System.out.println();
        System.out.print("      ");

        for (int i = 65; i < table[0].length + 65; i++) {
            System.out.print("_" + spaces(" "));
        }
        System.out.println();
        // print number of rows
        for (int i = 0; i < table.length; i++) {

            if (i < 9)
                System.out.print(i + 1 + "  . |");
            else if (i < 99)
                System.out.print(i + 1 + " . |");
            // print rows of table
            for (int j = 0; j < table[i].length; j++) {
                // check if length of cell is less than 10
                if (table[i][j].length() > 10)
                    // if not crete shortcut of text in cell and print it with spaces
                    System.out.print(table[i][j].substring(0, 9) + "." + spaces(table[i][j].substring(0, 9) + "."));
                else
                    System.out.print(table[i][j] + spaces(table[i][j]));
            }
            System.out.println();
        }
        System.out.println("\nRows : [" + table.length + "] , Columns : [" + table[0].length + "]");
    }
    // return count of row and max count of elements
    public static String sizeOfCsvTable(String filePath) {

        int countRow = 0, maxCol = -1;

        try {
            CSVReader csvReader = new CSVReader(new FileReader(filePath));

            String[] nextRecord;
            // find out count of rows and max count of elements in each row
            while ((nextRecord = csvReader.readNext()) != null) {
                countRow++;
                if (maxCol < nextRecord.length)
                    maxCol = nextRecord.length;
            }
        }
        catch (Exception e) {

        }
        return countRow + " " + maxCol;
    }
    // return count of row and max count of elements
    public static String sizeOfTsvTable(String filePath)
    {
        int countRow = 0, maxCol = -1;

        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String row;

            String[] rowField;
            // find out count of rows and max count of elements in each row
            while ((row = in.readLine()) != null) {
                countRow++;
                rowField = row.split("\t");
                if (maxCol < rowField.length)
                    maxCol = rowField.length;
            }
            in.close();
        }
        catch (Exception e) {

        }
        return countRow + " " + maxCol;
    }
    // auxiliary function for read tsv and csv files functions
    public static String[][] declaredTable(String filePath) {
        String scr;
        // find out what is type of file
        if (fileSuffix(filePath).equals("tsv"))
            scr = sizeOfTsvTable(filePath);
        else
            scr = sizeOfCsvTable(filePath);

        // split values from scr
        int size = Integer.parseInt(scr.substring(0,scr.indexOf(" ")));
        int size2 = Integer.parseInt(scr.substring(scr.indexOf(" ") + 1));

        // create 2D field
        String[][] table = new String[size][size2];

        // fill the field with space
        for (int i = 0; i < table.length ; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = " ";
            }
        }
        return table;
    }

    public static String[][] readTsvFile(String filePath) {
        // create auxiliary variable
        String [][] table = declaredTable(filePath);

        try {
            BufferedReader in  =  new BufferedReader(new FileReader(filePath));
            String row ;

            int i = 0;
            while ((row = in.readLine()) != null) {
                table[i] = row.split("\t");
                i++;
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    // save tsv files
    public static void saveTsvFile(String[][] table, String filePath) {

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            // save separately for each element of table plus tab
            for (int i = 0; i < table.length ; i++) {
                for (int j = 0; j < table[i].length ; j++) {
                    out.write(table[i][j] + "\t");
                }
                out.newLine();
            }
            out.close();
        }
        catch (Exception e){
            System.out.print("");
        }
    }
    public static void containsText(String[][] text, String comm2) {
        // check if text was defined
        if (!comm2.equals("")){
            // create variable for count
            int count = 0;

            // loop where looking if contains defined text
            for (int i = 0; i <  text[0][0].length() ; i++) {
                if (i >= comm2.length()) {
                    // if i >= text length then create substring from i - text length to i
                    String a =  text[0][0].substring(i - comm2.length() , i );
                    // if text == substring
                    if (a.equals(comm2))
                        count++;
                }
            }
            System.out.println("Count : [" + count + "]");
        }
        else {
            System.out.println("\tText is not defined\n");
        }
    }

    // print txt file
    public static void printOutTxtFile(String[][] text) {
        // create auxiliary variable
        String txt = text[0][0];

        System.out.println("-----------------------------------------");
        // print text
        System.out.println(txt);

        // create variables for count words and letters
        int words = 0, letters = 0;

        // loop for count words and letters
        for (int i = 0; i < txt.length() ; i++) {

            // character not equals space or enter
            if (txt.charAt(i) != ' ' && txt.charAt(i) != 10)
                letters++;

            // if i > 0 && character not equals space && is letter && character before not equals space , enter ot tab || character at 0 not equals space && i == 0
            if ((i > 0) && (txt.charAt(i) != ' ' && Character.isLetter(txt.charAt(i)) && (txt.charAt(i - 1) == ' ' || txt.charAt(i - 1) == 10 || txt.charAt(i - 1) == 9)) || ((txt.charAt(0) != ' ') && (i == 0)))
                words++;
        }

        System.out.println("-----------------------------------------");
        System.out.println("Letters : [" + letters +"] , Words : [" + words + "]");


    }
    // load txt file
    public static String[][] readTxtFile(String filePath){

        // create auxiliary variables
        String str[][] = new String[1][1], tr = "";

        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            // create auxiliary string
            String row;
            // add every row to tr with enter till row == null
            while ((row = in.readLine()) != null) {
                tr += row +"\n";
            }
            in.close();
        }
        catch (Exception e){

        }
        // return text without last enter
        str[0][0] = tr.substring(0,tr.lastIndexOf("\n"));

        return str;
    }
    // save txt file
    public static void saveTxtFile(String[][] text,String filePath ){

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            // save text to file
            out.write(text[0][0]);

            out.close();
        }
        catch (Exception e){
            System.out.print("");
        }
    }
}
