/**
 * Fabiola Alejandra Contreras Colindres (Carné 22787)
 * Ing. en Ciencias de la computación y Tecnologías de la información
 * --Laboratorio 2 POO--
 * 
 * Clase Main, llama al controlador
 * Este programa es un simulador del funcionamiento de la Memoria RAM de
 * nuestras computadoras, recibe datos de distintos programas, y su uso, y los
 * almacena en listas conforme al espacio con el que se cuenta.
 */
public class Main {

    public static void main(String[] args) {
        Controlador c = new Controlador();

        c.inicio();
        c.ejecutar();

    }
}