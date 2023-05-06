package org.example;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.*;

import org.junit.Assert;

public class csvNew {
    static String instrumentFilePath = "/Users/ranjithkumart/Documents/app/in/Instrument.csv";
    static String outputFilePath = "/Users/ranjithkumart/Documents/app/out/OutPutFile.csv";
    static String PositionFilePath = "/Users/ranjithkumart/Documents/app/in/Position.csv";
    static String outputQuantityValue = "";
    static String outputTotalValue = "";
    static String positionQuantityValue = "";
    static String outPutFileISIN = "";
    static String InstrumentFileFileISIN = "";

    public static void main(String[] args) {
        try {
            CSVReader outPutFile = new CSVReader(new FileReader(outputFilePath));
            CSVReader insFile = new CSVReader(new FileReader(instrumentFilePath));
            CSVReader posFile = new CSVReader(new FileReader(PositionFilePath));
            LinkedHashMap<String, List<String>> linkedMapOutput = new LinkedHashMap<String, List<String>>();
            LinkedHashMap<String, List<String>> positionMap = new LinkedHashMap<String, List<String>>();
            LinkedHashMap<String, List<String>> instrumentMap = new LinkedHashMap<String, List<String>>();

            String[] nline;
            while ((nline = outPutFile.readNext()) != null) {
                String key = nline[0];
                List<String> values = Arrays.asList(nline[0], nline[1], nline[2], nline[3]); // second, third and fourth columns as list of values
                linkedMapOutput.put(key, values);
            }
            linkedMapOutput.remove("PositionID");
            String[] mline;
            while ((mline = posFile.readNext()) != null) {
                String key = mline[0];
                List<String> values = Arrays.asList(mline[0], mline[1], mline[2]);
                positionMap.put(key, values);
            }
            positionMap.remove("ID");
            String[] oline;
            while ((oline = insFile.readNext()) != null) {
                String key = oline[0];
                List<String> values = Arrays.asList(oline[0], oline[1], oline[2], oline[3]); // second, third and fourth columns as list of values
                instrumentMap.put(key, values);
            }
            instrumentMap.remove("ID");
            System.out.println("InstrumentMap Key: " + instrumentMap.keySet());
            System.out.println("InstrumentMap Value:" + instrumentMap.values() + "\n");
            System.out.println("OutPut Key: " + linkedMapOutput.keySet());
            System.out.println("OutPut Value:" + linkedMapOutput.values() + "\n");
            System.out.println("PositionMap Key: " + positionMap.keySet());
            System.out.println("PositionMap Value:" + positionMap.values() + "\n");
            linkedMapOutput.forEach((key, value) -> positionMap.forEach((key1, value1) -> {
                try {
                    if (key.contains(key1)) {
                        System.out.println("Comparing OutputFile Key " + key + " with PositionFile Key: " + key1);
                        System.out.println("Comparing OutputFile value " + value + " with PositionFile value: " + value1);
                        boolean comparison = compareOutPutAndPositionData(value, value1);
                        if (comparison) {
                            String[] isinOutput = value.toArray(new String[0]);
                            outPutFileISIN = isinOutput[1];
                            String[] positionInstrumentID = value1.toArray(new String[0]);
                            String position_IDValue = positionInstrumentID[1];
                            if (comparePositionWithIns(position_IDValue, instrumentMap) && outPutFileISIN.equals(InstrumentFileFileISIN)) {
                                System.out.println("Expected Results - ISIN : " + outPutFileISIN);
                                System.out.println("Actual  Results - ISIN: " + InstrumentFileFileISIN);
                                System.out.println("Final result: Calculation for " + key + " key is correct!\n\r");
                                Assert.assertTrue("Final result: Calculation for " + key + " key is correct! \n\r", true);
                            } else {
                                System.out.println("Final result: " + key + " is not correct!");
                                Assert.fail("Final result: " + key + " is Incorrect!\n\r");
                            }
                        } else {
                            System.out.println("Quantity value are not matching");
                        }
                    }
                } catch (AssertionError e) {
                    System.out.println("Validation failed. Moving to next data ");
                }
            }));
        } catch (Exception e) {
            System.out.println("Test failed " + e);
        }
    }


    private static boolean comparePositionWithIns(String positionInstrumentIDValue, Map<String, List<String>> instrumentMap) {
        try {
            System.out.println("Checking " + positionInstrumentIDValue + " present on " + instrumentMap.keySet() + " instrument file");
            if (instrumentMap.containsKey(positionInstrumentIDValue)) {
                List<String> values = instrumentMap.get(positionInstrumentIDValue);
                System.out.println("Values from ins table to be compared " + values);
                String unitePrice = values.get(3);
                InstrumentFileFileISIN = values.get(2);
                if (Integer.parseInt(unitePrice) * Integer.parseInt(positionQuantityValue) == Integer.parseInt(outputTotalValue)) {
                    return true;
                } else {
                    System.out.println("Incorrect Calculation...!");
                    System.out.println("Instrument: Unit Price: " + Integer.parseInt(unitePrice));
                    System.out.println("Position:  Quantity value: " + Integer.parseInt(positionQuantityValue));
                    System.out.println("Output file: Total price: " + Integer.parseInt(outputTotalValue) + " which is wrong!\n\r");
                }
            }
        } catch (Exception e) {
            System.out.println("comparePositionWithIns  failed " + e);
        }
        return false;
    }


    private static boolean compareOutPutAndPositionData(List<String> outputValue, List<String> positionValue) {
        try {
            String[] arr1 = outputValue.toArray(new String[0]);
            String[] arr2 = positionValue.toArray(new String[0]);
            outputQuantityValue = arr1[2];
            outputTotalValue = arr1[3];
            positionQuantityValue = arr2[2];
            if (outputQuantityValue.equals(positionQuantityValue)) {
                System.out.println("Quantity data in Output file and Position file are Same!. Item " + positionQuantityValue + " & " + outputQuantityValue + " matching on both files..!");
                System.out.println("Expected Results - Quantity: " + outputQuantityValue);
                System.out.println("Actual  Results - Quantity: " + positionQuantityValue);
                return true;
            } else {
                System.out.println("The two values are not equal.");
            }
        } catch (Exception e) {
            System.out.println("compareOutPutAndPositionData  failed " + e);
        }
        return false;
    }


}
