package Matrices;

import java.util.ArrayList;
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

    public SolucionArbol(CalculoMatrices matriz , Integer numeroDeSolucionesImportantes , String id){
        this.matriz = matriz;
        tipo = "Inicial";
        maximo = Float.MIN_VALUE;
        listaPorMirar = new ArrayList<>();
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        this.id = id;
        primerPaso();

    }

    public SolucionArbol(CalculoMatrices matriz , List<CalculoMatrices> lsMirar , Integer numeroDeSolucionesImportantes, String id){
        this.matriz = matriz;
        tipo = "Inicial";
        maximo = Float.MIN_VALUE;
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        this.id = id;
        primerPaso();

    }

    public SolucionArbol(CalculoMatrices matriz,String tipo,Float maximo , Integer numeroDeSolucionesImportantes, String id){
        this.matriz = matriz;
        this.tipo = tipo;
        this.maximo = maximo;
        z = 0f;
        this.numeroDeSolucionesImportantes = numeroDeSolucionesImportantes;
        this.id = id;
    }

    private void primerPaso(){
        System.out.println("===============- empezamos con la matriz "+id+" -==================");
        matriz.simplexMaximizacion();
        matriz.sacarIncognitas();
        z = matriz.getMatrizNumero().get(0).get(0);
        lsSoluciones = matriz.getSolucionLista();
        crearHijos();
        if(!listaPorMirar.isEmpty()){
            listaPorMirar.getFirst().maximo = maximo;
            maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
        }
    }
    
    public Float segundoPaso(List<SolucionArbol> ls){
        this.listaPorMirar = ls;
        listaPorMirar.remove(this);

        System.out.println("===============- empezamos con la matriz "+id+" -==================");
        String resultado = matriz.simplexDualMaximizacion();
        matriz.sacarIncognitas();
        if(resultado == "T"){
            z = matriz.getMatrizNumero().get(0).get(0);
            if(z > maximo){

                if( matriz.getSolucionLista().stream().limit(numeroDeSolucionesImportantes).allMatch( n -> n%1 == 0) ){
                    tipo = "solucion final";
                    maximo = z;
                    System.out.println("tipo -> "+tipo);
                    
                }else{
                    System.out.println("tipo -> "+tipo);
                    this.crearHijos();
                }



            }else{
                tipo = "poda";
                System.out.println("tipo -> "+tipo);
                
            }
        }else{
            tipo = "infactible";
            System.out.println("tipo -> "+tipo);
        }

        if(!listaPorMirar.isEmpty()){
            listaPorMirar.getFirst().maximo = maximo;
            maximo = listaPorMirar.getFirst().segundoPaso(listaPorMirar);
        }
        
        return maximo;
    }
    
    private void crearHijos(){
        List<Float> ls = new ArrayList<>();
        List<Float> lsAux = new ArrayList<>();
        float auxFloat;
        Integer posicionChico;
        CalculoMatrices matrizAux;

        //Aqui calculamos cuales son las soluciones mas cercanas a 1/2
        for(int i = 1 ; i < matriz.numeroEcuaciones ; i ++){
            auxFloat = matriz.getMatrizNumero().get(i).getFirst();
            ls.add( Math.abs(auxFloat - (int)auxFloat - 0.5f) );
        }
        System.out.println("ls -> "+ls);

        //Obtenemos la posicion del mejor valor
        posicionChico = ls.indexOf( ls.stream().min(Float::compare).get() ) + 1;
        System.out.println("Posicion del mejor valor -> " + posicionChico);
        //===- Ahora creamos el hijo menor
        matrizAux = matriz.copiarClase();
        //Aumentamos los ceros
        for(int i = 0; i < matriz.numeroEcuaciones ; i++){
            matrizAux.getMatrizNumero().get(i).add(0f);
        }

        //Creamos la ecuacion que vamos a añadir
        lsAux = new ArrayList<>(Collections.nCopies(matriz.numeroIncgnita, 0f));
        float numero = matriz.getMatrizNumero().get(posicionChico).getFirst();
        lsAux.set(0 , (float)((int)numero));
        lsAux.set(matriz.getIncognitas().get(posicionChico), 1.0f );
        lsAux.add(1f);
        matrizAux.getMatrizNumero().add(lsAux); //añadimos la ecuacion extra
        
        //Ahora resolvemos el pibotaje para que la ecuacion sea correcta
        matrizAux.pibotaje( posicionChico ,  matriz.getIncognitas().get(posicionChico));
        menorQue = new SolucionArbol(matrizAux,"hijo",maximo , numeroDeSolucionesImportantes , id +"-menor");
        //======== ahora el mayor
        matrizAux = matriz.copiarClase();

        //Aumentamos los ceros
        for(int i = 0; i < matriz.numeroEcuaciones ; i++){
            matrizAux.getMatrizNumero().get(i).add(0f);
        }

        //Creamos la ecuacion que vamos a añadir
        lsAux = new ArrayList<>(Collections.nCopies(matriz.numeroIncgnita, 0f));
        numero =matriz.getMatrizNumero().get(posicionChico).getFirst();
        lsAux.set(0 , -((float)((int)numero)+1));
        lsAux.set(matriz.getIncognitas().get(posicionChico), -1.0f );
        lsAux.add(1f);
        matrizAux.getMatrizNumero().add(lsAux); //añadimos la ecuacion extra

        //Ahora resolvemos el pibotaje para que la ecuacion sea correcta
        matrizAux.pibotaje( posicionChico ,  matriz.getIncognitas().get(posicionChico));
        mayorQue = new SolucionArbol(matrizAux,"hijo",maximo , numeroDeSolucionesImportantes, id+"-MAYOR");
        
        //Guardamos las listas
        listaPorMirar.add(menorQue);
        listaPorMirar.add(mayorQue);


    }

    public void ensenarArbol(){
        System.out.println("Matriz con id -> "+id);
        System.out.println("tipos -> "+tipo);
        matriz.sacarIncognitas();
        System.out.println("Soluciones -> "+matriz.getSolucionLista());
        System.out.println("Z -> "+z);
        System.out.println("========================================");
        if(menorQue != null){
            menorQue.ensenarArbol();
        }
        if(mayorQue != null){
            mayorQue.ensenarArbol();
        }
    }

    public void mostrarMatriz(){
        matriz.ensenarMatriz();
        System.out.println("Z -> "+z+" solucion -> "+lsSoluciones+" tipo -> "+tipo);

    }


}
