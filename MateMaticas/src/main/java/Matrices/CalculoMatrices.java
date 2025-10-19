package Matrices;

import java.util.ArrayList;
import java.util.List;

public class CalculoMatrices {
    List<List<Float>> matrizNumero;
    List<Float> funcionObjetivo;

    public List<List<Float>> getMatrizNumero() {
        return matrizNumero;
    }

    public void setMatrizNumero(List<List<Float>> matrizNumero) {
        this.matrizNumero = matrizNumero;
        this.funcionObjetivo = matrizNumero.getFirst();
    }

    public CalculoMatrices(List<String> matriz) {
        matrizNumero = new ArrayList<>();
        List<Float> unaEcuacion;
        String[] numeroEnTexto;

        Integer contador = 0;
        for(String ecuacion : matriz){
            unaEcuacion = new ArrayList<>();
            for(String numero : ecuacion.split(",")){
                unaEcuacion.add( Float.valueOf(numero) );
            }
            matrizNumero.add(unaEcuacion);
        }
        funcionObjetivo = matrizNumero.getFirst();

        ensenarMatriz();

    }

    public void ensenarMatriz(){
        int cont = 0;
        for(List<Float> ec : matrizNumero){
            cont = 0;
            for(Float num : ec){
                System.out.printf("%10.4f" , num);
                if(cont == 0){
                    System.out.print(" | ");
                }
                cont += 1;
            }
            System.out.println();
        }
    }
    //esto es lo mismo pero ahora marca
    public void ensenarMatriz(Integer x , Integer y){
        int cont = 0,contx = 0,conty = 0;
        for(List<Float> ec : matrizNumero){
            cont = 0;
            contx = 0;
            for(Float num : ec){
                if(contx == x && conty == y){
                    System.out.printf("(%8.4f)" , num);
                }else{
                    System.out.printf("%10.4f" , num);
                }

                if(cont == 0){
                    System.out.print(" | ");
                }
                cont += 1;
                contx += 1;
            }
            conty += 1;
            System.out.println();
        }
    }

    public void simplexMaximizacion(){
        String mensajeFinal = "";
        int tam = funcionObjetivo.size();
        int numeroEcuaciones = matrizNumero.size();
        boolean terminado = true;
        for (int i = 0; i < tam; i++) {
            float num = funcionObjetivo.get(i);
            if (num != 0) {
                funcionObjetivo.set(i, -num);
            }

        }
        System.out.println("Ponemos en negativo la funcion objetivo");
        ensenarMatriz();
        // Aqui es donde empieza el bucle =========================
        while (terminado) {


            float minimo = Float.MAX_VALUE;
            int posMin = 0;
            int columna = 0, fila = 0;



            for (int i = 1; i < tam; i++) {
                if (funcionObjetivo.get(i) < minimo && funcionObjetivo.get(i) < 0) {
                    minimo = funcionObjetivo.get(i);
                    posMin = i;
                }
            }
            if (posMin == 0) {
                terminado = false;
                mensajeFinal = "Simplex terminado";
                break;
            } else {
                System.out.println("\nAhora de la funcion objetivo buscamos el mas negativo");
                ensenarMatriz(posMin, 0);
                columna = posMin;
            }

            minimo = Float.MAX_VALUE;
            posMin = 0;
            float division = 0;

            for (int ec = 1; ec < numeroEcuaciones; ec++) {
                if (matrizNumero.get(ec).get(columna) > 0) {
                    division = matrizNumero.get(ec).get(0) / matrizNumero.get(ec).get(columna);
                    if (division < minimo) {
                        minimo = division;
                        posMin = ec;
                    }
                }

            }

            System.out.println("\nAhora buscamos el pivote");
            if (posMin == 0) {
                terminado = false;
                mensajeFinal = "Es infactible";
                break;
            } else {
                ensenarMatriz(columna, posMin);
                fila = posMin;
            }

            System.out.println("\npivotamos fila = " + fila + " columna = " + columna);
            pibotaje(fila, columna);
            ensenarMatriz();

        }

        System.out.println("\nFinal -> "+ mensajeFinal);




    }

    public void simplexDualMaximizacion() {
        String mensajeFinal = "";
        int tam = funcionObjetivo.size();
        int numeroEcuaciones = matrizNumero.size();
        boolean terminado = true;
        float auxFloat;
        while (terminado){
            //Aqui empiezas el bucle
            float minimo = Float.MAX_VALUE;
            int posMin = 0;
            int columna = 0, fila = 0;
            for (int ec = 1; ec < numeroEcuaciones; ec++) {
                if (matrizNumero.get(ec).get(0) < 0) {
                    if (matrizNumero.get(ec).get(0) < minimo) {
                        minimo = matrizNumero.get(ec).get(0);
                        posMin = ec;
                    }
                }
            }
            if (posMin == 0) {
                mensajeFinal = "Terminado";
                terminado = false;
                break;
            } else {
                System.out.println("\nPrimero buscamos la ecuacion con el valor mas negativo");
                ensenarMatriz(0, posMin);
                fila = posMin;
            }

            minimo = Float.MAX_VALUE;
            posMin = 0;

            for (int inc = 1; inc < tam; inc++) {
                if (matrizNumero.get(fila).get(inc) < 0) {
                    auxFloat = -matrizNumero.get(0).get(inc) / matrizNumero.get(fila).get(inc);
                    if (auxFloat > 0 && auxFloat < minimo) {
                        minimo = auxFloat;
                        posMin = inc;
                    }
                }
            }

            if (posMin == 0) {
                mensajeFinal = "Es infactible";
                terminado = false;
                break;
            } else {
                System.out.println("\nSegundo buscamos la incognita pibote");
                columna = posMin;
                ensenarMatriz(columna, fila);

            }
            System.out.println("\nProcedemos a hacer el pibotaje en la columna = " + columna + " fila = " + fila);
            pibotaje(fila, columna);
            ensenarMatriz();
        }

        System.out.println("\nmensaje final -> "+mensajeFinal);

    }

    public void pibotaje(Integer fila, Integer columna){
        float numeroQueMultiplica = 1 / matrizNumero.get(fila).get(columna);
        int tam = funcionObjetivo.size();
        int numeroEcuaciones = matrizNumero.size();
        float auxF1,auxF2;
        for(int i = 0 ; i < tam ; i++){
            matrizNumero.get(fila).set( i ,
                    matrizNumero.get(fila).get(i) * numeroQueMultiplica
                    );
        }
        //System.out.println("=======");ensenarMatriz();
        for(int ec = 0 ; ec < numeroEcuaciones ; ec++){
            if(ec != fila){
                //auxF1 = matrizNumero.get(ec).get(columna);
                //auxF2 = matrizNumero.get(fila).get(columna);
                numeroQueMultiplica = -matrizNumero.get(ec).get(columna) / matrizNumero.get(fila).get(columna);

                for(int inc = 0; inc < tam ; inc++){

                    matrizNumero.get(ec).set( inc ,
                            matrizNumero.get(fila).get(inc) * numeroQueMultiplica + matrizNumero.get(ec).get(inc)
                    );

                }
                //System.out.println("=======");ensenarMatriz();

            }
            //System.out.println("=======");
        }


    }
}
