/*
 * Fabiola Contreras 22787
 * --Laboratorio 2 POO--
 * 
 * MemoriaSDR Representa a una memoria de tipo SDR, la cual solo realiza un proceso en todo momento.
 * Permite que al crear una nueva instancia de esta clase se asignen valores a los atributos establecidos.
 */

public class MemoriaSDR {
    private String[][] memoria;
    private double en_uso;
    private double disponible;

    /**
     * Crea unanueva matriz para registrar los datos de programas en la memoria.
     * 
     * @param bloques son la cantidad de filas necesarias en la matriz para cubrir
     *                todo el espacio de memoria. Cada uno representa 64MB.
     */
    public MemoriaSDR(int bloques) {
        this.memoria = new String[bloques][2]; // Columnas - Nombre, tiempo
    }

    /*
     * Cada getter permite recuperar los datos de cierto atributo de la clase.
     * Cada setter permite establecer un nuevo valor para ese atributo.
     * Se usan para obtener este valor (y posiblemente modificarlo) fuera de la
     * clase.
     */
    public String[][] getMemoria() {
        return memoria;
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
