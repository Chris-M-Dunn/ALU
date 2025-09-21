// detectAddOverflow() function gives incorrect output?

public class ALU {
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Usage: java ALU add/sub/and/or/slt **** ****");
            return;
        }

        for (int i=0; i<args[1].length(); i++) {
            if ((args[1].charAt(i) != '0' && args[1].charAt(i) != '1') || args[2].charAt(i) != '0' && args[2].charAt(i) != '1') {
                System.out.println("Valid binary numbers only.");
                return;
            }
        }

        if (args[1].length() != 4 || args[2].length() != 4) {
            System.out.println("Each binary number must be 4-bits.");
            return;
        }

        switch(args[0].toLowerCase()) {
            case "add": 
                String result = add(args[1], args[2]);
                System.out.println("ADD: " + result + ", " + detectAddOverflow(args[1], args[2], result) + ", " + detectZero(result));
                break;
            
            case "sub": 
                result = sub(args[1], args[2]);
                System.out.println("SUB: " + result + ", " + detectSubOverflow(args[1], args[2], result) + ", " + detectZero(result));
                break;

            case "and": 
                result = and(args[1], args[2]);
                System.out.println("AND: " + result + ", 0, " + detectZero(result));
                break;

            case "or": 
                result = or(args[1], args[2]);
                System.out.println("OR: " + result + ", 0, " + detectZero(result));
                break;

            case "slt": 
                result = slt(args[1], args[2]);
                System.out.println("SLT: " + result);
                break;
            default:
                System.out.println(args[0] + " is not recognized as a command. Try add/sub/and/or/slt.");
        }
    }

    public static String add(String bitString1, String bitString2) {
        String result = "";
        boolean carry = false;

        for (int i=bitString1.length()-1; i>=0; i--) {

            char bit1 = bitString1.charAt(i);
            char bit2 = bitString2.charAt(i);

            // Case 1: both bits are 0, so dropdown a 0
            if (bit1 == '0' && bit2 == '0') {
                if (carry) {
                    result = '1' + result;
                    carry = false;
                } else {
                    result = '0' + result;
                }
            }

            // Case 2: one bit is 0 and the other is 1, so dropdown a 1
            else if ((bit1 == '1' && bit2 == '0') || (bit2 == '1' && bit1 == '0')) {
                if (carry) {
                    result = '0' + result;
                } else {
                    result = '1' + result;
                }
            }

            // Case 3: both bits are 1, so carry a 1 and dropdown a 0
            else if (bit1 == '1' && bit2 == '1') {
                if (carry) {
                    result = '1' + result;
                } else {
                    result = '0' + result;
                    carry = true;
                }
            }
        }

        return result;
    }

    // Invert the bits on bitString2 then call the add() function
    public static String sub(String bitString1, String bitString2) {
        // Invert the bits of bitString2 & add 1
        String bitString2Inverted = invert(bitString2);

        // Now, subtract by adding the inversion of bitString2
        return add(bitString1, bitString2Inverted);
    }

    public static String and(String bitString1, String bitString2) {
        String result = "";

        for (int i=bitString1.length()-1; i>=0; i--) {
            if (bitString1.charAt(i) == '1' && bitString2.charAt(i) == '1') {
                result = '1' + result;
            } else {
                result = '0' + result;
            }
        }

        return result;
    }

    public static String or(String bitString1, String bitString2) {
        String result = "";

        for (int i=bitString1.length()-1; i>=0; i--) {
            if (bitString1.charAt(i) == '1' || bitString2.charAt(i) == '1') {
                result = '1' + result;
            } else {
                result = '0' + result;
            }
        }

        return result;
    }

    public static String slt(String bitString1, String bitString2) {
        if (evaluate(bitString1) < evaluate(bitString2)) {
            return "0001";
        } else {
            return "0000";
        }
    }

    // Detect overflows for addition
    public static int detectAddOverflow(String bitString1, String bitString2, String result) {
        char msb1 = bitString1.charAt(0);
        char msb2 = bitString2.charAt(0);
        char msbResult = result.charAt(0);

        if ((msb1 == '1' && msb2 == '1') && msbResult == '0') {
            return 1;
        } else if ((msb1 == '0' && msb2 == '0') && msbResult == '1') {
            return 1;
        } else {
            return 0;
        }
    }

    // Detect overflows for subtraction
    public static int detectSubOverflow(String bitString1, String bitString2, String result) {
        char msb1 = bitString1.charAt(0);
        char msb2 = bitString2.charAt(0);
        char msbResult = result.charAt(0);

        if ((msb1 == '0' && msb2 == '1') && msbResult == '1') {
            return 1;
        } else if ((msb1 == '1' && msb2 == '0') && msbResult == '0') {
            return 1;
        } else {
            return 0;
        }
    }

    public static int detectZero(String bitString) {
        if (evaluate(bitString) == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static String invert(String bitString) {
        String bitStringInverted = "";
        
        for (int i=0; i<bitString.length(); i++) {
            if (bitString.charAt(i) == '1') {
                bitStringInverted += '0';
            } else {
                bitStringInverted += '1';
            }
        }

        return add(bitStringInverted, "0001");
    }

    public static int evaluate(String bitString) {
        boolean isNegative = false;
        
        if (bitString.charAt(0) == '1') {
            isNegative = true;
            bitString = invert(bitString);
        }
        
        int value = 0;
        int index = 0;
        
        for (int i=bitString.length()-1; i>=0; i--) {
            if (bitString.charAt(i) == '1') {
                value += (int)Math.pow(2, index);
            }
            index++;
        }

        if (isNegative) {
            return -value;
        } else {
            return value;
        }
    }
}
