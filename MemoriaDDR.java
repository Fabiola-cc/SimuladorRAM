/*
 * Fabiola Contreras 22787
 * --Laboratorio 2 POO--
 * 
 * MemoriaDDR Representa a una memoria de tipo DDR, la cual realiza dos procesos en todo momento, el principal y el que está en espera.
 * Permite que al crear una nueva instancia de esta clase se asignen valores a los atributos establecidos.
 */

import java.util.ArrayList;

public class MemoriaDDR {

    private String[][] RAM;
    private ArrayList<Programa> Enespera;
    private double en_uso;
    private double disponible;

    /**
     * Genera una matriz inicial para guardar datos de programas en la memoria y
     * establece un arrayList para la cola de espera.
     * 
     * @param espacioInicial Cantidad de filas que se necesitan para la matriz
     *                       inicial
     */
    public MemoriaDDR(int espacioInicial) {

        this.RAM = new String[espacioInicial][3]; // Filas variables, 3 Columnas: Ocupado o libre, Nombre, tiempo
        this.Enespera = new ArrayList<Programa>(); // "Columnas": Nombre, tiempo, espacio total

    }

    /*
     * Cada getter permite recuperar los datos de cierto atributo de la clase.
     * Cada setter permite establecer un nuevo valor para ese atributo.
     * Se usan para obtener este valor (y posiblemente modificarlo) fuera de la
     * clase.
     */
    public String[][] getRAM() {
        return RAM;
    }

    public void setRAM(String[][] dDR) {
        RAM = dDR;
    }

    public ArrayList<Programa> getEnespera() {
        return Enespera;
    }

    public void setEnespera(ArrayList<Programa> enespera) {
        Enespera = enespera;
    }

    public double getEn_uso() {
        return en_uso;
    }

    public void setEn_uso(double en_uso) {
        this.en_uso = en_uso;
    }

    public double getDisponible() {
        return disponible;
    }

    public void setDisponible(double disponible) {
        this.disponible = disponible;
    }

}
