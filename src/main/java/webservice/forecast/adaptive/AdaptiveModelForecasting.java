package webservice.forecast.adaptive;

import webservice.forecast.adaptive.util.Util;
import webservice.model.Range;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Stream;

import static webservice.forecast.adaptive.util.Util.summOfArray;


/*
*

Модель: Cкользящое среднее с центрированием
Период усреднения: Квартал (4)
Делать прогноз на  периода : ?
 */

public class AdaptiveModelForecasting {

    private final int PERIOD = 4;

    private Range data = new Range();

    {

    }


    public AdaptiveModelForecasting() {
    }

    public Range getMovingAverage(Range range) {
        Range out = new Range(range.size());


        for (int k = 0; k < range.size(); k++) {
            double result = 0;
            for (int i = k; i < k + PERIOD; i = i + 2) {
                result += range.get(i) + range.get(i + 1);
            }

            out.add(result / PERIOD);

            if (k == range.size() - PERIOD) break;
        }

        return out;
    }

    public Range getCentreMovingAverage(Range range) {
        Range out = new Range(range.size());

        int n = 2;
        for (int k = 0; k < range.size(); k++) {
            double result = 0;
            for (int i = k; i < k + n; i = i + 2) {
                if (k == 0) {
                    out.add(range.get(i) / 2);
                }
                result += range.get(i) + range.get(i + 1);
            }

            out.add(result / n);

            if (k == range.size() - n) break;
        }

        return out;
    }

    public Range getSeasonComponentAssessment(Range initialRange, Range centreMovingAvg) {
        Range out = new Range();

        int k = 1;

        for (int i = 0; i < centreMovingAvg.size(); i++) {
            out.add(initialRange.get(i + k) - centreMovingAvg.get(i));
        }

        return out;
    }

    public Range getCorrectSeasonComponent(Range seasonComponent) {
        Range assessmentAVG = new Range();
        Deque<Double> seasonComponentDeque = new LinkedList<>(seasonComponent.getList());
        seasonComponentDeque.addFirst(null);
        seasonComponentDeque.add(null);

        if(seasonComponentDeque.size() % PERIOD != 0)  {
            seasonComponentDeque.add(null);
        }

        int n = (int) Math.ceil((double) (seasonComponentDeque.size()) / PERIOD);

        Double[][] matrix = new Double[n][PERIOD];
        Util.fillMatrixByZero(matrix);


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (seasonComponentDeque.size() > 0) {
                    Double rez = seasonComponentDeque.poll();
                    matrix[i][j] = rez;
                }
            }
        }


        Double sum;
        int countCeil;
        for (int i = 0; i < matrix[0].length; i++) {
            sum = 0d;
            countCeil = 0;
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != null) {
                    countCeil++;
                    sum += matrix[j][i];
                }
            }
            assessmentAVG.add(sum / countCeil);
        }


        double correctionFactor = assessmentAVG.sum() / assessmentAVG.size();


        assessmentAVG.forEach((x) -> x - correctionFactor);

        return assessmentAVG;
    }


    public Range getExcludedInfluenceComponent(Range correctSeasonComponent, Range initialRange) {
        Range range = new Range();
        Deque<Double> deque = new LinkedList<>();
        int k = (int) Math.ceil((double) (initialRange.size()) / PERIOD);

        for (int i = 0; i < k; i++) {
            deque.addAll(correctSeasonComponent.getList());
            for (int j = 0; j < 4; j++) {
                if(j + (i * 4) == initialRange.size()) {
                    break;
                }
                double iData = initialRange.get(j + (i * 4));
                double iStack = deque.poll();
                range.add(iData - iStack);
            }
        }

        return range;
    }

    public Double[] getSolutionOfEquation(Range excludedInfluenceComponentRange) {
        int n = excludedInfluenceComponentRange.size();
        Double sumY = excludedInfluenceComponentRange.sum();

        int[] tArray = Stream.iterate(1, integer -> integer + 1)
                .limit(n)
                .mapToInt(i -> i)
                .toArray();

        int[] t2Array = Stream.iterate(1, integer -> (integer + 1))
                .limit(n)
                .map((i) -> i * i)
                .mapToInt(i -> i)
                .toArray();

        double sumTmY = 0;
        for (int i = 0; i < n; i++) {
            sumTmY += excludedInfluenceComponentRange.get(i) * tArray[i];
        }

        int sumT = summOfArray(tArray);
        int sumT2 = summOfArray(t2Array);

        Double[] rez = new Double[2];

        double delta = n * sumT2 - sumT * sumT;
        double delta_a1 = n * sumTmY - sumT * sumY;
        double delta_a2 = sumT2 * sumY - sumT * sumTmY ;

        rez[0] = delta_a1 / delta;
        rez[1] = delta_a2 / delta;

       // System.out.println(rez[0] + " " + rez[1]) ;
        return rez;

    }

    public Double[] getForecast(Range correctSeasonComponent, Double[] resultOfEquation, int fromIndex) {
        Double[] out = new Double[4];

        for (int i = 0; i < 4; i++) {
            int index = (fromIndex + 3) % 4;
            out[i] = (resultOfEquation[1] + resultOfEquation[0] * fromIndex) + correctSeasonComponent.get(index);
            fromIndex++;
        }

        return out;
    }

    public Range getModelRange(Double[] resultOfEquation, Range correctSeasonComponent, Range initialRange) {
        Range out = new Range();

        for (int i = 0; i < initialRange.size(); i++) {
            double value = resultOfEquation[1] + resultOfEquation[0] * (i + 1);
            value = value + correctSeasonComponent.get(i % 4);
            out.add(value);
        }

        return out;
    }


  /*  public static void main(String[] args) {
        AdaptiveModelForecasting m = new AdaptiveModelForecasting();

        Range s1 = m.getMovingAverage(m.data);
        Range s2 = m.getCentreMovingAverage(s1);
        Range s3 = m.getSeasonComponentAssessment(m.data, s2);
        Range s4 = m.getCorrectSeasonComponent(s3);
        Range s5 = m.getExcludedInfluenceComponent(s4, m.data);
        //System.out.println(s4);
        //System.out.println(s5);
        //System.out.println(m.data.size());
        //System.out.println(s4);
        //System.out.println(s5);
        Double[] getForecast = m.getForecast(s4, m.getSolutionOfEquation(s5), 32);

        System.out.println(Arrays.toString(getForecast));
    }*/
}
