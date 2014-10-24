package ru.ifmo.natural_science;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.BiFunction;

public class Evaluator {
    private double initValue;
    private BiFunction<Double, Double, Double> func;
    private double eps;
    private double maxIterationsNums;

    public Evaluator(double initValue, BiFunction<Double, Double, Double> func, double eps, double maxIterationsNums) {
        this.initValue = initValue;
        this.func = func;
        this.eps = eps;
        this.maxIterationsNums = maxIterationsNums;
    }

    public ArrayList<Double> get(double r){
        ArrayList<Double> values = new ArrayList<>();
        TreeMap<Double, Integer> used = new TreeMap<>((a, b) -> a < b - eps ? -1 : a < b + eps ? 0 : 1);

        double cur = initValue;
        int iterationsNum = 0;
        while(!used.containsKey(cur)){
            used.put(cur, values.size());
            values.add(cur);
            cur = func.apply(cur, r);
            if (iterationsNum++ > maxIterationsNums || Double.isInfinite(cur)) return new ArrayList<>();
        }

        ArrayList<Double> ans = new ArrayList<>();
        for (int i = values.size() - used.get(cur); i > 0; i--){
            ans.add(cur);
            cur = func.apply(cur, r);
        }
        return ans;
    }


}
