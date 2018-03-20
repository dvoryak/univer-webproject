package webservice.controller.util;

import java.util.Arrays;

public class Util {

    public static Double[] convertStringToArray(String string) {
        String[] split = string.split("[,; \t\n]");
        Double[] doubles = new Double[split.length];

        for (int i = 0; i < split.length; i++) {
            doubles[i] = Double.parseDouble(split[i]);
        }

        return doubles;
    }

  /*  public static void main(String[] args) {
        Double[] doubles = convertStringToArray("160\n" +
                "130\n" +
                "159");

        System.out.println(Arrays.toString(doubles));

    }*/
}
