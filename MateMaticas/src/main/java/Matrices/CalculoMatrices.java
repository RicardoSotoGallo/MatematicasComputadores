package Matrices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalculoMatrices {
    private List<List<Float>> matrizNumero;
    private List<Float> funcionObjetivo;
    private HashMap<Integer,Integer> incognitas;
    private HashMap<Integer,Float> soluciones;
    private List<Float> solucionLista;
    public Integer numeroIncgnita;
    public Integer numeroEcuaciones;

    public HashMap<Integer, Float> getSoluciones() {
        return soluciones;
    }

    public HashMap<Integer, Integer> getIncognitas() {
        return incognitas;
    }

    public List<Float> getSolucionLista() {
        return solucionLista;
    }

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

        //ensenarMatriz();

    }

    public CalculoMatrices copiarClase(){
        List<String> lsTexto = new ArrayList<>();
        numeroEcuaciones = matrizNumero.size();
        for(int i = 0; i < numeroEcuaciones;i++){
            lsTexto.add(
                    matrizNumero.get(i).toString().replace("[","").replace("]","")
            );
        }
        return new CalculoMatrices(lsTexto);
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

    public String simplexMaximizacion(){
        String mensajeFinal = "";
        String res = "";
        numeroIncgnita = funcionObjetivo.size();
        numeroEcuaciones = matrizNumero.size();
        boolean terminado = true;
        for (int i = 0; i < numeroIncgnita; i++) {
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



            for (int i = 1; i < numeroIncgnita; i++) {
                if (funcionObjetivo.get(i) < minimo && funcionObjetivo.get(i) < 0) {
                    minimo = funcionObjetivo.get(i);
                    posMin = i;
                }
            }
            if (posMin == 0) {
                terminado = false;
                mensajeFinal = "Simplex terminado";
                res = "T";
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
                res = "I";
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



        return res;
    }

    public String simplexDualMaximizacion() {
        String mensajeFinal = "";
        String res = "";
        numeroIncgnita = funcionObjetivo.size();
        numeroEcuaciones = matrizNumero.size();
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
                res = "T";
                break;
            } else {
                System.out.println("\nPrimero buscamos la ecuacion con el valor mas negativo");
                ensenarMatriz(0, posMin);
                fila = posMin;
            }

            minimo = Float.MAX_VALUE;
            posMin = 0;

            for (int inc = 1; inc < numeroIncgnita; inc++) {
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
                res = "I";
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
        return res;

    }

    public void pibotaje(Integer fila, Integer columna){
        float numeroQueMultiplica = 1 / matrizNumero.get(fila).get(columna);
        numeroIncgnita = funcionObjetivo.size();
        numeroEcuaciones = matrizNumero.size();
        float auxF1,auxF2;
        for(int i = 0 ; i < numeroIncgnita ; i++){
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

                for(int inc = 0; inc < numeroIncgnita ; inc++){

                    matrizNumero.get(ec).set( inc ,
                            matrizNumero.get(fila).get(inc) * numeroQueMultiplica + matrizNumero.get(ec).get(inc)
                    );

                }
                //System.out.println("=======");ensenarMatriz();

            }
            //System.out.println("=======");
        }


    }

    public void sacarIncognitas(){
        incognitas = new HashMap<>();
        soluciones = new HashMap<>();
        solucionLista = new ArrayList<>();
        numeroIncgnita = funcionObjetivo.size();
        numeroEcuaciones = matrizNumero.size();
        int ecuacionEscogida = 0, sumaEc = 0;
        for( int inc = 1 ;  inc < numeroIncgnita ; inc++){
            if(matrizNumero.get(0).get(inc) == 0){
                ecuacionEscogida = 0;
                sumaEc = 0;
                for(int ec = 1 ; ec < numeroEcuaciones ; ec ++){
                    sumaEc += matrizNumero.get(ec).get(inc);
                    if(matrizNumero.get(ec).get(inc) == 1) ecuacionEscogida = ec;
                }
                if(sumaEc == 1 && ecuacionEscogida != 0) {
                    incognitas.put(ecuacionEscogida, inc);
                    soluciones.put( ecuacionEscogida , matrizNumero.get(ecuacionEscogida).getFirst() );
                    //solucionLista.add(matrizNumero.get(ecuacionEscogida).getFirst());
                }
            }


        }
        for(int i = 1 ; i < numeroIncgnita ; i++) solucionLista.add(0f);
        for(int i = 1 ; i < numeroEcuaciones ; i++){
            solucionLista.set(incognitas.get(i) - 1 , soluciones.get(i) );
        }
    }

}
