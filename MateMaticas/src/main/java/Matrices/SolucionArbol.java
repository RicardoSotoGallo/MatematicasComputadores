package Matrices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SolucionArbol {
    String id; //esto es importante en que parte del arbol estamos
    CalculoMatrices matriz;
    Float z;
    List<Float> lsSoluciones;
    List<SolucionArbol> listaPorMirar;
    String tipo;
    Float maximo;
    SolucionArbol menorQue;
    SolucionArbol mayorQue;
    Integer numeroDeSolucionesImportantes;
    public SolucionArbol(CalculoMatrices matriz , Integer numeroDeSolucionesImportantes){
        this.matriz = matriz;
        tipo = "Inicial";
        maximo = Float.MIN_VALUE;
        listaPorMirar = new ArrayList<>();
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        primerPaso();

    }
    public SolucionArbol(CalculoMatrices matriz , List<CalculoMatrices> lsMirar , Integer numeroDeSolucionesImportantes){
        this.matriz = matriz;
        tipo = "Inicial";
        maximo = Float.MIN_VALUE;
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        primerPaso();

    }
    public SolucionArbol(CalculoMatrices matriz,String tipo,Float maximo , Integer numeroDeSolucionesImportantes){
        this.matriz = matriz;
        this.tipo = tipo;
        this.maximo = maximo;
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
    }

    private void primerPaso(){
        matriz.simplexMaximizacion();
        matriz.sacarIncognitas();
        z = matriz.getMatrizNumero().get(0).get(0);
        lsSoluciones = matriz.getSolucionLista();
        crearHijos();
    }
    public Float segundoPaso(List<SolucionArbol> ls){
        this.listaPorMirar = ls;
        listaPorMirar.remove(this);

        String resultado = matriz.simplexDualMaximizacion();
        matriz.sacarIncognitas();
        if(resultado == "T"){
            z = matriz.getMatrizNumero().get(0).get(0);
            if(z > maximo){

                if( matriz.getSolucionLista().stream().limit(numeroDeSolucionesImportantes).allMatch( n -> n%1 == 0) ){
                    tipo = "solucion final";
                    maximo = z;
                    System.out.println("tipo -> "+tipo);
                    if(listaPorMirar.size() > 0){
                        listaPorMirar.getFirst().maximo = maximo;
                        maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
                    }
                }else{
                    System.out.println("tipo -> "+tipo);
                    this.crearHijos();
                }



            }else{
                tipo = "poda";
                System.out.println("tipo -> "+tipo);
                if(listaPorMirar.size() > 0){
                    listaPorMirar.getFirst().maximo = maximo;
                    maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
                }
            }
        }else{
            tipo = "infactible";
            System.out.println("tipo -> "+tipo);
            if(listaPorMirar.size() > 0){
                listaPorMirar.getFirst().maximo = maximo;
                maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
            }
        }


        return maximo;
    }
    private void crearHijos(){
        List<Float> ls = new ArrayList<>();
        List<Float> lsAux = new ArrayList<>();
        float auxFloat;
        Integer posicionChico;
        Integer auxInteger;
        CalculoMatrices matrizAux;
        for(int i = 1 ; i < matriz.numeroEcuaciones ; i ++){
            auxFloat = matriz.getMatrizNumero().get(i).getFirst();
            ls.add( Math.abs(auxFloat - (int)auxFloat - 0.5f) );
        }
        System.out.println(ls);
        posicionChico = ls.indexOf( ls.stream().min(Float::compare).get() ) + 1;
        System.out.println("Posicion del mejor valor -> " + posicionChico);
        //==========================================
        matrizAux = matriz.copiarClase();
        for(int i = 0; i < matriz.numeroEcuaciones ; i++){
            matrizAux.getMatrizNumero().get(i).add(0f);
        }
        //System.out.println("====================");
        //matrizAux.ensenarMatriz();
        //System.out.println("====================");
        lsAux = new ArrayList<>(Collections.nCopies(matriz.numeroIncgnita, 0f));
        float numero =matriz.getMatrizNumero().get(posicionChico).getFirst();
        lsAux.set(0 , (float)((int)numero));
        lsAux.set(matriz.getIncognitas().get(posicionChico), 1.0f );
        lsAux.add(1f);
        matrizAux.getMatrizNumero().add(lsAux);
        //Aqui
        matrizAux.ensenarMatriz();
        //System.out.println("========= pivotamos");
        matrizAux.pibotaje( posicionChico ,  matriz.getIncognitas().get(posicionChico));
        //matrizAux.ensenarMatriz();
        menorQue = new SolucionArbol(matrizAux,"hijo",maximo , numeroDeSolucionesImportantes);
        //======== ahora la de la izquierda
        System.out.println("Ahora procedemos con el otro hijo");
        matrizAux = matriz.copiarClase();
        for(int i = 0; i < matriz.numeroEcuaciones ; i++){
            matrizAux.getMatrizNumero().get(i).add(0f);
        }
        //System.out.println("====================");
        //matrizAux.ensenarMatriz();
        //System.out.println("====================");
        lsAux = new ArrayList<>(Collections.nCopies(matriz.numeroIncgnita, 0f));
        numero =matriz.getMatrizNumero().get(posicionChico).getFirst();
        lsAux.set(0 , -((float)((int)numero)+1));
        lsAux.set(matriz.getIncognitas().get(posicionChico), -1.0f );
        lsAux.add(1f);
        matrizAux.getMatrizNumero().add(lsAux);
        matrizAux.ensenarMatriz();
        System.out.println("====================");
        //System.out.println("========= pivotamos");
        matrizAux.pibotaje( posicionChico ,  matriz.getIncognitas().get(posicionChico));
        //matrizAux.ensenarMatriz();
        mayorQue = new SolucionArbol(matrizAux,"hijo",maximo , numeroDeSolucionesImportantes);
        listaPorMirar.add(menorQue);
        listaPorMirar.add(mayorQue);

        if(listaPorMirar.size() > 0){
            listaPorMirar.getFirst().maximo = maximo;
            maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
        }





    }

    public void mostrarMatriz(){
        matriz.ensenarMatriz();
        System.out.println("Z -> "+z+" solucion -> "+lsSoluciones+" tipo -> "+tipo);

    }
}
