package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Matrices.CalculoMatrices;
import Matrices.SolucionArbol;
import Matrices.CorteHiperPlano;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<String> ls = new ArrayList<>();
        ls = leerFichero("Fichero/ecuacion.txt");
        //System.out.println(ls);

        //Poner la matriz sin cambiar signo en maximizacion
        System.out.println("\u001B[31m Primero vamos a usar branch  \u001B[0m");


        
        /*ls.add(0f+   ","+ 4f+ ","+-3f+    ","+ 0f+   ","+ 0f);
        ls.add(7f+   ","+ 5f+ ","+-2f+    ","+ 1f+   ","+ 0f);
        ls.add(5f+   ","+ 0f+ ","+ 1f+    ","+ 0f+   ","+ 1f);*/
        System.out.println(ls);
        CalculoMatrices matriz = new CalculoMatrices(ls);
        matriz.ensenarMatriz();
        //matriz.simplexMaximizacion();
        SolucionArbol sol = new SolucionArbol(matriz , 2 , "Cabeza");
        System.out.println("========================================");
        sol.ensenarArbol();
        System.out.println("\n\u001B[31m Ahora vamos a probar con hiper plano  \u001B[0m");

        matriz = new CalculoMatrices(ls);
        matriz.ensenarMatriz();

        CorteHiperPlano corte = new CorteHiperPlano(matriz , 2 , "Cabeza");

    }

    public static List<String> leerFichero(String ubi){
        List<String> matriz = new ArrayList<>();
        String[] auxStringLs,auxStringLs2;
        String auxString;
        try (BufferedReader br = new BufferedReader(new FileReader(ubi))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                auxStringLs = linea.split(",");
                auxString = "";
                for( String n : auxStringLs){
                    if(n.contains(".")){
                        auxString += n.strip()+",";
                    }else if(n.contains("/")){
                        auxStringLs2 = n.split("/");
                        auxString += Float.parseFloat(auxStringLs2[0])/Float.parseFloat(auxStringLs2[1])+",";
                    }else{
                        auxString += n.strip()+".0,";
                    }
                }
                matriz.add(auxString.substring(0 , auxString.length() -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matriz;
    }
}