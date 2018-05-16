package univer.webapp.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Range {

    private List<Double> data;

    public Range(int size) {
        this.data = new ArrayList<Double>(size);
    }

    public Range() {
        this.data = new ArrayList<Double>();
    }

    public void loadFrom(File file) {
        throw new UnsupportedOperationException();
    }

    public void loadFrom(Double... numbers) {
        Collections.addAll(data, numbers);
    }

    public void loadFrom(Integer... numbers) {
        Double[] doubles = new Double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            int tmp = numbers[i];
            doubles[i] = (double) tmp;
        }
        Collections.addAll(data,doubles);
    }


    public void add(Double number) {
        data.add(number);
    }

    public Double get(int index) {
        if(index < 0) {
            throw new IllegalArgumentException();
        }

        return data.get(index);
    }

    public List<Double> getList() {
        return data;
    }

    public double sum() {
        return data.stream().mapToDouble(i -> (Double) i).sum();
    }

    public int size() {
        return data.size();
    }

    public void forEach(UnaryOperator<Double> unaryOperator) {
        for (int i = 0; i < data.size(); i++) {
            Double d =  data.get(i);
            data.set(i,unaryOperator.apply(d));
        }
    }

    public Stream stream() {
        return data.stream();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
