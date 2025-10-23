package Matrices;

import java.util.ArrayList;
import java.util.List;

public class CorteHiperPlano {
    String id; //esto es importante en que parte del arbol estamos
    CalculoMatrices matriz;
    Float z;
    String tipo;
    Float maximo;
    Integer numeroDeSolucionesImportantes;

    List<Float> lsSoluciones;
    public CorteHiperPlano(CalculoMatrices matriz , Integer numeroDeSolucionesImportantes,
                           String id){
        this.matriz = matriz;
        tipo = "Inicial";
        maximo = Float.MIN_VALUE;
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        this.id = id;
        //========= Empezamos con la ecuacion ====================
        System.out.println("===============- empezamos con la matriz "+id+" -==================");
        matriz.simplexMaximizacion();
        z = matriz.getMatrizNumero().get(0).get(0);
        lsSoluciones = matriz.getSolucionLista();
        iterarHastaResolver();

    }
    private void iterarHastaResolver(){
        float auxFloat;List<Float> ls = new ArrayList<>();int posicionChico;
        String salidaAux = "T";
        boolean continuamos = true,boolAux;
        while (continuamos) {
            ls = new ArrayList<>();
            // Aqui buscamos el que vamos a escoger
            for (int i = 1; i < matriz.getNumeroEcuaciones(); i++) {
                auxFloat = matriz.getMatrizNumero().get(i).getFirst();
                ls.add(Math.abs(auxFloat - (int) auxFloat - 0.5f));
            }
            System.out.println("ls -> " + ls);

            //Obtenemos la posicion del mejor valor
            posicionChico = ls.indexOf(ls.stream().min(Float::compare).get()) + 1;
            System.out.println("Posicion del mejor valor -> " + posicionChico);
            ls = new ArrayList<>();
            for (int i = 0; i < matriz.getNumeroIncgnita(); i++) {
                auxFloat = -matriz.getMatrizNumero().get(posicionChico).get(i)
                        + (float) Math.floor(matriz.getMatrizNumero().get(posicionChico).get(i));
                ls.add(Math.round(auxFloat * 100000f) / 100000f);
            }
            ls.add(1.0f);
            matriz.añadirEcuacion(ls);
            matriz.ensenarMatriz();
            salidaAux = matriz.simplexDualMaximizacion();

            if (salidaAux.equals("I")) continuamos = false;
            boolAux = true;
            for (int i = 0; i < numeroDeSolucionesImportantes; i++) {
                if (matriz.getSolucionLista().get(i) % 1 != 0) boolAux = false;
            }
            if (boolAux) continuamos = false;

        }
        System.out.println("Emos terminado la ecuacion -> "+salidaAux);


    }

}
