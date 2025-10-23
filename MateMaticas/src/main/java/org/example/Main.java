package org.example;

import java.util.ArrayList;
import java.util.List;

import Matrices.CalculoMatrices;
import Matrices.SolucionArbol;
import Matrices.CorteHiperPlano;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Poner la matriz sin cambiar signo en maximizacion
        System.out.println("\u001B[31m Primero vamos a usar branch  \u001B[0m");
        List<String> ls = new ArrayList<>();
        
        ls.add(0f+   ","+ 4f+ ","+-3f+    ","+ 0f+   ","+ 0f);
        ls.add(7f+   ","+ 5f+ ","+-2f+    ","+ 1f+   ","+ 0f);
        ls.add(5f+   ","+ 0f+ ","+ 1f+    ","+ 0f+   ","+ 1f);
        /*ls.add(28/5f+","+0f+ ","+7/5f+  ","+4/5f+ ","+0+  ","+0f+ ",");
        ls.add(7/5f+ ","+1f+ ","+-2/5f+ ","+1/5f+ ","+0+  ","+0f+ ",");
        ls.add(5f+   ","+0f+ ","+1f+    ","+0f+   ","+1f+ ","+0f+ ",");
        ls.add(-2/5f+","+0f+ ","+2/5f+  ","+-1/5f+","+0+  ","+1f+ ",");*/
        CalculoMatrices matriz = new CalculoMatrices(ls);
        matriz.ensenarMatriz();
        //matriz.simplexMaximizacion();
        SolucionArbol sol = new SolucionArbol(matriz , 2 , "Cabeza");
        System.out.println("========================================");
        sol.ensenarArbol();
        System.out.println("\n\u001B[31m Ahora vamos a probar con hiper plano  \u001B[0m");

        ls = new ArrayList<>();
        ls.add(0f+   ","+ 4f+ ","+-3f+    ","+ 0f+   ","+ 0f);
        ls.add(7f+   ","+ 5f+ ","+-2f+    ","+ 1f+   ","+ 0f);
        ls.add(5f+   ","+ 0f+ ","+ 1f+    ","+ 0f+   ","+ 1f);
        matriz = new CalculoMatrices(ls);
        matriz.ensenarMatriz();

        CorteHiperPlano corte = new CorteHiperPlano(matriz , 2 , "Cabeza");

    }
}