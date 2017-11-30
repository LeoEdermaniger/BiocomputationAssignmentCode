/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author leoed
 */
public class FileParser {
    public static ArrayList<Type> type1Parser(String fileName) throws FileNotFoundException{
        Scanner fileScan = new Scanner(FileParser.class.getResourceAsStream(fileName));
        ArrayList<Type> data = new ArrayList<>();
        
        fileScan.nextLine();
        
        while(fileScan.hasNextLine()){
            data.add(new Type1(fileScan.next(), fileScan.next()));
        }
        
        return data;
    }
    
    public static ArrayList<Type> type2Parser(String fileName) throws FileNotFoundException{
        Scanner fileScan = new Scanner(FileParser.class.getResourceAsStream(fileName));
        ArrayList<Type> data = new ArrayList<>();
        
        fileScan.nextLine();
        
        while(fileScan.hasNextLine()){
            data.add(new Type2(fileScan.next(), fileScan.next()));
        }
        
        return data;
    }
    
    public static ArrayList<Type> type3Parser(String fileName) throws FileNotFoundException{
        Scanner fileScan = new Scanner(FileParser.class.getResourceAsStream(fileName));
        ArrayList<Type> data = new ArrayList<>();
        
        fileScan.nextLine();
        
        while(fileScan.hasNextLine()){
            String[] values = new String[6];
            for(int x = 0; x < values.length; x++){
                values[x] = fileScan.next();
            }
            data.add(new Type3(values, fileScan.next()));
        }
        
        return data;
    }
}
