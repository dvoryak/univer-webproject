package webservice.forecast.adaptive;

import webservice.forecast.adaptive.util.Util;
import webservice.model.Range;

import java.util.Arrays;
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
public class AdaptiveModelRangeForecast {

    private final int PERIOD = 4;

    private Range data = new Range();

    {
        data.loadFrom(
                160,
                130,
                159,
                165,
                156,
                141,
                157,
                172,
                157,
                145,
                163,
                177,
                102,
                123,
                233,
                122,
                127,
                143,
                165,
                180,
                172,
                170,
                169,
                161,
                200,
                190,
                188,
                201,
                204,
                196,
                289
       );
    }


    public AdaptiveModelRangeForecast() {
    }

    public Range getMovingAverage(Range data) {
        Range range = new Range(data.size());


        for (int k = 0; k < data.size(); k++) {
            double result = 0;
            for (int i = k; i < k + PERIOD; i = i + 2) {
                result += data.get(i) + data.get(i + 1);
            }

            range.add(result / PERIOD);

            if (k == data.size() - PERIOD) break;
        }

        return range;
    }

    public Range getCentreMovingAverage(Range data) {
        Range range = new Range(data.size());

        int n = 2;
        for (int k = 0; k < data.size(); k++) {
            double result = 0;
            for (int i = k; i < k + n; i = i + 2) {
                if (k == 0) {
                    range.add(data.get(i) / 2);
                }
                result += data.get(i) + data.get(i + 1);
            }

            range.add(result / n);

            if (k == data.size() - n) break;
        }

        return range;
    }

    public Range getSeasonComponentAssessment(Range initialRange, Range centreMovingAvg) {
        Range range = new Range();

        int k = 1;

        for (int i = 0; i < centreMovingAvg.size(); i++) {

            range.add(initialRange.get(i + k) - centreMovingAvg.get(i));
        }

        return range;
    }

    public Range getCorrectSeasonComponent(Range data) {

        Range assessmentAVG = new Range();
        Deque<Double> deque = new LinkedList<>(data.getList());
        deque.addFirst(null);
        deque.add(null);
        deque.add(null);
        Double[][] matrix = new Double[(int) Math.ceil((double) (deque.size()) / PERIOD)][PERIOD];
        Util.fillMatrixByZero(matrix);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (deque.size() > 0) {
                    Double rez = deque.poll();
                    matrix[i][j] = rez;
                }
            }
        }

        Double sum;
        int countCeil = 0;
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


    public Range excludedInfluenceComponent(Range correctSeasonComponent, Range data) {

        Range range = new Range();
        Deque<Double> deque = new LinkedList<>();
        int k = (int) Math.ceil((double) (data.size()) / PERIOD);

        for (int i = 0; i < k; i++) {
            deque.addAll(correctSeasonComponent.getList());
            for (int j = 0; j < 4; j++) {
                if(j + (i * 4) == data.size()) {
                    break;
                }
                double iData = data.get(j + (i * 4));
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

        System.out.println(rez[0]);
        System.out.println(rez[1]);
        return rez;

    }

    public Double[] getForecastForAllPeriods(Range correctSeasonComponent, Double[] result, int from) {
        Double[] out = new Double[4];
        System.out.println(correctSeasonComponent);
        for (int i = 0; i < 4; i++) {
            int index = (from + 3) % 4;
            out[i] = (result[1] + result[0] * from) + correctSeasonComponent.get(index);
            from++;
        }

        return out;
    }


    public static void main(String[] args) {
        AdaptiveModelRangeForecast m = new AdaptiveModelRangeForecast();

        Range s1 = m.getMovingAverage(m.data);
        Range s2 = m.getCentreMovingAverage(s1);
        Range s3 = m.getSeasonComponentAssessment(m.data, s2);
        Range s4 = m.getCorrectSeasonComponent(s3);
        Range s5 = m.excludedInfluenceComponent(s4, m.data);
        //System.out.println(s5);
        //System.out.println(s5);
        //System.out.println(s4);
        //System.out.println(s5);
        Double[] forecastForAllPeriods = m.getForecastForAllPeriods(s4, m.getSolutionOfEquation(s5), 32);

        System.out.println(Arrays.toString(forecastForAllPeriods));
    }
}
