
//Cameron Wright

package vmPackage;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class VirtualMachine {

    private static int MAX_MEMORY_SIZE = 500;
    private static String[] program = new String[MAX_MEMORY_SIZE];

    private static int counter;
    private static int count = 0;

    private static List<Label> List= new ArrayList<Label>();


    //Add SubLC3 variables here
    private static int increment;
    private static int number;
    private static int tempNum;

    //run the Virtual Machine
    public static void main(String[] args) {
        new VirtualMachine();
    }

    public VirtualMachine() {
        try (Scanner br = new Scanner(new FileReader("src\\vmPackage\\mySubLC3.txt"))) {
            String line;

            //Adds relevant lines to the program array
            for(int i = 0; (line = br.nextLine()) != null; i++) {
                String first = String.valueOf(line.charAt(0));
                if(line.equals("HALT")) {
                    break;
                }
                else if(!first.equals(";")) {
                    program[count] = line;
                    count++;
                }
            }

            for(int i = 0; i < count; i++) {
                String command = program[i];
                String[] split = command.split("\\s+");
                if(split.length == 1) {
                    String label = split[0];
                    setLabel(label, i);
                }
            }

            for(counter = 0; counter < count; counter++) {
                String command = program[counter];
                String[] split = command.split("\\s+");
                String vPattern = "^[A-Za-z][A-Za-z0-9]+";
                String destination;
                String source1;
                String source2;
                int intSource1;
                int intSource2;
                int ans;
                String jumpLabel;
                int value;

                switch(split[0]) {
                    /*
                     ADD accepts two integer values, Source1 and Source2, and stores their
                     sum in the Destination variable, Destination.
                     */
                    case "ADD":
                        destination = split[1];
                        source1 = split[2];
                        source2 = split[3];
                        if(source1.matches(vPattern))
                            intSource1 = getField(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getField(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 + intSource2;
                        setField(destination, ans);
                        break;


                    /*
                     SUB accepts two integer values, Source1 and Source2, and stores the
                     result of (Source1 – Source2) in the Destination variable, Destination.
                     */
                    case "SUB":
                        destination = split[1];
                        source1 = split[2];
                        source2 = split[3];
                        if(source1.matches(vPattern))
                            intSource1 = getField(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getField(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 - intSource2;
                        setField(destination, ans);
                        break;


                    /*
                     MUL accepts two integer values, Source1 and Source2, and stores the
                     result of (Source1 * Source2) in the Destination variable, Destination.
                     */
                    case "MUL":
                        destination = split[1];
                        source1 = split[2];
                        source2 = split[3];
                        if(source1.matches(vPattern))
                            intSource1 = getField(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getField(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 * intSource2;
                        setField(destination, ans);
                        break;


                    /*
                     DIV accepts two integer values, Source1 and Source2, and stores the
                     result of (Source1 / Source2) in the Destination variable, Destination.
                     */
                    case "DIV":
                        destination = split[1];
                        source1 = split[2];
                        source2 = split[3];
                        if(source1.matches(vPattern))
                            intSource1 = getField(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getField(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 / intSource2;
                        setField(destination, ans);
                        break;


                    /*
                     Inputs an integer value and stores it in Variable.
                     */
                    case "IN":
                        Scanner scan = new Scanner(System.in);
                        // ask user for an integer
                        int input = scan.nextInt();
                        scan.close();
                        setField(split[1], input);
                        break;


                    /*
                     Display Value.
                     */
                    case "OUT":
                        int length = split.length - 1;
                        if(length == 1 && split[1].matches(vPattern)) {
                            value = getField(split[1]);
                            System.out.println(value);
                        }
                        else {
                            String str = split[1];
                            for(int i = 2; i <= length; i++) {
                                str += " " + split[i];
                            }
                            if(str.startsWith("\"") && str.endsWith("\"")) {
                                String newStr = str.replaceAll("\"", "");
                                System.out.println(newStr);
                            }else
                                System.out.println("ERROR: Double quotations required!");
                        }
                        break;


                    /*
                     The STO instruction stores the value of Source in Destination variable.
                     */
                    case "STO":
                        destination = split[1];
                        String source = split[2];
                        int intSource = 0;
                        if(source.matches(vPattern))
                            intSource = getField(source);
                        else
                            intSource = Integer.parseInt(source);
                        setField(destination, intSource);
                        break;


                    /*
                     If the value of Variable is negative, jump to Label.
                     */
                    case "BRn":
                        jumpLabel = split[2];
                        value = getField(split[1]);
                        if(value < 0)
                            for(Label label : List)
                            {
                                if(label.getName().equals(jumpLabel)) {
                                    counter = label.getLocation();
                                }
                            }
                        break;


                    /*
                     If the value of Variable is negative, jump to Label.
                     */
                    case "BRz":
                        jumpLabel = split[2];
                        value = getField(split[1]);
                        if(value ==  0)
                            for(Label label : List)
                            {
                                if(label.getName().equals(jumpLabel)) {
                                    counter = label.getLocation();
                                }
                            }
                        break;


                    /*
                     If the value of Variable is positive, jump to Label.
                     */
                    case "BRp":
                        jumpLabel = split[2];
                        value = getField(split[1]);
                        if(value > 0)
                            for(Label label : List)
                            {
                                if(label.getName().equals(jumpLabel)) {
                                    counter = label.getLocation();
                                }
                            }
                        break;


                    /*
                     If the value of Variable is zero or positive, jump to Label.
                     */
                    case "BRzp":
                        jumpLabel = split[2];
                        value = getField(split[1]);
                        if(value >=  0)
                            for(Label label : List) {
                                if(label.getName().equals(jumpLabel)) {
                                    counter = label.getLocation();
                                }
                            }
                        break;


                    /*
                     If the value of Variable is zero or negative, jump to Label.
                     */
                    case "BRzn":
                        jumpLabel = split[2];
                        value = getField(split[1]);
                        if(value <=  0)
                            for(Label label : List) {
                                if(label.getName().equals(jumpLabel)) {
                                    counter = label.getLocation();
                                }
                            }
                        break;


                    /*
                     Jump to Label.
                     */
                    case "JMP":
                        jumpLabel = split[1];
                        for(Label label : List)
                        {
                            if(label.getName().equals(jumpLabel)) {
                                counter = label.getLocation();
                            }
                        }
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void setField(String fieldName, int value) {
        try {
            Field field = getClass().getDeclaredField(fieldName);
            field.setInt(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getField(String fieldName) {
        int value = 0;
        try {
            Field field = getClass().getDeclaredField(fieldName);
            value = field.getInt(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void setLabel(String name, int location) {
        String labelPattern = "^[A-Za-z]+";
        if(name.matches(labelPattern)) {
            List.add(new Label(name, location));
        }
    }

    public class Label {
        String name;
        int loc;
        Label(String name, int loc) {
            this.name = name;
            this.loc = loc;
        }
        String getName() {
            return name;
        }
        int getLocation() {
            return loc;
        }
    }
}