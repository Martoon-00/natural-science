package ru.ifmo.natural_science;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Evaluator eval = new Evaluator(0.5, (x, r) -> x * r * (1 - x), 1e-5, 1000);

        ArrayList<Double> l = eval.get(3.58);

        System.out.println(l.size());
        for (Double e : l) System.out.println(e);
    }
}
