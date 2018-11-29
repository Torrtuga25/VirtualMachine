
//Cameron Wright and Cameron Foster

package vmPackage;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class VirtualMachine {

    private static int MAX_MEMORY_SIZE = 500;
    private static String[] program = new String[MAX_MEMORY_SIZE];

    private static int counter;
    private static int count = 0;

    private static List<Label> LabelList= new ArrayList<>();
    private static List<Variable> VariableList= new ArrayList<>();

    //Runs the Virtual Machine
    public static void main(String[] args) {
        new VirtualMachine();
    }

    public VirtualMachine() {
        try (Scanner br = new Scanner(new FileReader("src\\vmPackage\\allSubLC3.txt"))) {
            String line;

            //Adds relevant lines to the program array
            for(; (line = br.nextLine()) != null; ) {
                String first = String.valueOf(line.charAt(0));
                if(line.equals("HALT")) {
                    break;
                }
                else if(!first.equals(";")) {
                    program[count] = line;
                    count++;
                }
            }

            //Create all Labels and Variables used in the SubLC3 code
            for(int i = 0; i < count; i++) {
                String command = program[i];
                String[] split = command.split("\\s+");
                //Creates Labels
                if(split.length == 1) {
                    String label = split[0];
                    setLabel(label, i);
                }
                //Creates Variables
                switch(split[0]) {
                    case "ADD": case "SUB": case "MUL": case "DIV":
                        if (!VariableList.contains(new Variable(split[1])))
                            setVariable(split[1], 0);
                        if (!VariableList.contains(new Variable(split[2])))
                            setVariable(split[2], 0);
                        if (!VariableList.contains(new Variable(split[3])))
                            setVariable(split[3], 0);
                        break;
                    case "STO":
                        if (!VariableList.contains(new Variable(split[1])))
                            setVariable(split[1], 0);
                        if (!VariableList.contains(new Variable(split[2])))
                            setVariable(split[2], 0);
                        break;
                    case "IN": case "OUT": case "BRn": case "BRz": case "BRp": case "BRzp": case "BRzn":
                        if (!VariableList.contains(new Variable(split[1])))
                            setVariable(split[1], 0);
                        break;
                }
            }

            //Decodes and executes each command
            for(counter = 0; counter < count; counter++) {
                String command = program[counter];
                String[] split = command.split("\\s+");
                String vPattern = "^[A-Za-z][A-Za-z0-9]+";
                String destination;
                String source1;
                String source2;
                int intSource1;
                int intSource2;
                int ans = 0;
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
                            intSource1 = getValue(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getValue(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 + intSource2;
                        setValue(destination, ans);
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
                            intSource1 = getValue(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getValue(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 - intSource2;
                        setValue(destination, ans);
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
                            intSource1 = getValue(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getValue(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        ans = intSource1 * intSource2;
                        setValue(destination, ans);
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
                            intSource1 = getValue(source1);
                        else
                            intSource1 = Integer.parseInt(source1);
                        if(source2.matches(vPattern))
                            intSource2 = getValue(source2);
                        else
                            intSource2 = Integer.parseInt(source2);
                        if (intSource2 != 0)
                            ans = intSource1 / intSource2;
                        else
                            System.out.println("ERROR: Division by 0! You broke the universe. Are  you happy?");
                        setValue(destination, ans);
                        break;


                    /*
                     Inputs an integer value and stores it in Variable.
                     */
                    case "IN":
                        Scanner scan = new Scanner(System.in);
                        int input = scan.nextInt();
                        setValue(split[1], input);
                        break;


                    /*
                     Display Value.
                     */
                    case "OUT":
                        int length = split.length - 1;
                        if(length == 1 && split[1].matches(vPattern)) {
                            value = getValue(split[1]);
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
                        int intSource;
                        if(source.matches(vPattern))
                            intSource = getValue(source);
                        else
                            intSource = Integer.parseInt(source);
                        setValue(destination, intSource);
                        break;


                    /*
                     If the value of Variable is negative, jump to Label.
                     */
                    case "BRn":
                        jumpLabel = split[2];
                        value = getValue(split[1]);
                        if(value < 0)
                            for(Label label : LabelList)
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
                        value = getValue(split[1]);
                        if(value ==  0)
                            for(Label label : LabelList)
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
                        value = getValue(split[1]);
                        if(value > 0)
                            for(Label label : LabelList)
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
                        value = getValue(split[1]);
                        if(value >=  0)
                            for(Label label : LabelList) {
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
                        value = getValue(split[1]);
                        if(value <=  0)
                            for(Label label : LabelList) {
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
                        for(Label label : LabelList)
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


    private void setLabel(String name, int location) {
        String lPattern = "^[A-Za-z]+";
        if(name.matches(lPattern)) {
            LabelList.add(new Label(name, location));
        }
    }

    private void setVariable (String name, int value) {
        String vPattern = "^[A-Za-z][A-Za-z0-9]+";
        if(name.matches(vPattern))
            VariableList.add(new Variable(name, value));
    }

    private void setValue(String name, int value) {
        for(Variable variable : VariableList)
        {
            if(variable.getName().equals(name)) {
                variable.setValue(value);
            }
        }
    }

    private int getValue (String name) {
        for(Variable variable : VariableList)
        {
            if(variable.getName().equals(name)) {
                return variable.getValue();
            }
        }
        return 0;
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

    public class Variable {
        String name;
        int value;

        Variable (String name, int value) {
            this.name = name;
            this.value = value;
        }

        Variable (String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        int getValue() {
            return value;
        }

        void setValue(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o){
            if(o instanceof Variable){
                Variable toCompare = (Variable) o;
                return this.name.equals(toCompare.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}