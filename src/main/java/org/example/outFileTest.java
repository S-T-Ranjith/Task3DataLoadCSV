package org.example;

import java.io.*;
import java.util.*;


public class outFileTest {
    static String instrumentFilePath = "/Users/ranjithkumart/Documents/app/in/Instrument.csv";
    static String outputFilePath = "/Users/ranjithkumart/Documents/app/out/OutPutFile.csv";
    static String PositionFilePath = "/Users/ranjithkumart/Documents/app/in/Position.csv";
    static String outputQuantityValue = "";
    static String outputTotalValue = "";
    static String positionQuantityValue = "";
    static String outPutFileISIN = "";
    static String positionFileFileISIN = "";
    static String InstrumentFileFileISIN = "";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner outPutFile = new Scanner(new FileReader(outputFilePath));
        Map<String, List<String>> outPutMap = new HashMap<>();
        while (outPutFile.hasNextLine()) {
            String line = outPutFile.nextLine();
            String[] values = line.split(",");
            outPutMap.put(values[0], Arrays.asList(values));
        }
        Scanner positionFile = new Scanner(new FileReader(PositionFilePath));
        Map<String, List<String>> positionMap = new HashMap<>();
        while (positionFile.hasNextLine()) {
            String line = positionFile.nextLine();
            String[] values = line.split(",");
            positionMap.put(values[0], Arrays.asList(values));
        }
        Scanner instrumentFile = new Scanner(new FileReader(instrumentFilePath));
        Map<String, List<String>> instrumentMap = new HashMap<>();
        while (instrumentFile.hasNextLine()) {
            String line = instrumentFile.nextLine();
            String[] values = line.split(",");
            instrumentMap.put(values[0], Arrays.asList(values));
        }
        instrumentMap.remove("ID");
        outPutMap.remove("PositionID");
        positionMap.remove("ID");
        System.out.println("InstrumentMap Key: " + instrumentMap.keySet());
        System.out.println("InstrumentMap Value:" + instrumentMap.values() + "\n");
        System.out.println("OutPut Key: " + outPutMap.keySet());
        System.out.println("OutPut Value:" + outPutMap.values() + "\n");
        System.out.println("PositionMap Key: " + positionMap.keySet());
        System.out.println("PositionMap Value:" + positionMap.values() + "\n");


        for (Map.Entry<String, List<String>> outPutData : outPutMap.entrySet()) {
            for (Map.Entry<String, List<String>> positionData : positionMap.entrySet()) {
                if (outPutData.getKey().contains(positionData.getKey())) {
                    System.out.println("Comparing OutputFile Key " + outPutData.getKey() + " with PositionFile Key: " + positionData.getKey());
                    boolean comparison = compareOutPutAndPositionData(outPutData.getValue(), positionData.getValue());
                    if (comparison) {
                        String[] isinOutput = outPutData.getValue().toArray(new String[0]);
                        outPutFileISIN = isinOutput[1];
                        String[] positionInstrumentID = positionData.getValue().toArray(new String[0]);
                        String position_IDValue = positionInstrumentID[1];
                        if (comparePositionWithIns(position_IDValue, instrumentMap)) {
                            System.out.println("Final result: Calculation for " + outPutData.getKey() + " key is correct!\n\r");
                        } else {
                            System.out.println("Final result: " + outPutData.getKey() + " is not correct!\n\r");
                        }
                    } else {
                        System.out.println("Quantity value are not matching");
                    }
                }
            }
        }

    }

    private static boolean comparePositionWithIns(String positionInstrumentIDValue, Map<String, List<String>> instrumentMap) throws FileNotFoundException {
        System.out.println("Checking " + positionInstrumentIDValue + " present on " + instrumentMap.keySet() + "ins file");
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
                System.out.println("Output file: Total price: " + Integer.parseInt(outputTotalValue) + "which is wrong!\n\r");
            }
        }
        return false;
    }


    private static boolean compareOutPutAndPositionData(List<String> outputValue, List<String> positionValue) {
        String[] arr1 = outputValue.toArray(new String[0]);
        String[] arr2 = positionValue.toArray(new String[0]);
        outputQuantityValue = arr1[2];
        outputTotalValue = arr1[3];
        positionQuantityValue = arr2[2];

        if (outputQuantityValue.equals(positionQuantityValue) && outPutFileISIN.equals(InstrumentFileFileISIN)) {
            System.out.println("Quantity data in Output file and Position file are Same!. Item " + positionQuantityValue + " matching on both files..!");
            System.out.println("Expected Results - ISIN: "+ outPutFileISIN+" (Output file). Quantity: "+ outputQuantityValue);
            System.out.println("Actual  Results  - ISIN "+ InstrumentFileFileISIN+"(Instrument file-). Quantity: "+ positionQuantityValue);

            return true;
        } else {
            System.out.println("The two values are not equal.");
        }
        return false;
    }


}
