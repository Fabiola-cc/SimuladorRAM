/*
 * Fabiola Contreras 22787
 * --Laboratorio 2 POO--
 * 
 * Programa es la clase que permite crear otras instancias de programa y al crearlas asignarles valores establecidos a los atributos.
 */

public class Programa {
    private String nombre;
    private int espacio;
    private int tiempo;

    /**
     * Constructor de la clase, da valores a los atributos establecidos
     * 
     * @param nombre
     * @param espacio
     * @param tiempo
     */
    public Programa(String nombre, int espacio, int tiempo) {
        this.nombre = nombre;
        this.espacio = espacio;
        this.tiempo = tiempo;
    }

    /*
     * Cada getter permite recuperar los datos de cierto atributo de la clase
     * Se usan para obtener este valor fuera de la clase.
     */
    public String getNombre() {
        return nombre;
    }

    public int getEspacio() {
        return espacio;
    }

    public int getTiempo() {
        return tiempo;
    }

}
