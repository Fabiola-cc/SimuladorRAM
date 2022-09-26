/*
 * Fabiola Contreras 22787
 * --Laboratorio 2 POO--
 * 
 * Vista es la clase que se encarga de mostrar y solicitar datos al usuario del programa. 
 */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vista {
    Scanner scan = new Scanner(System.in);

    /**
     * Imprime un texto solicitado
     * 
     * @param text texto a imprimir
     */
    public void show(String text) {
        System.out.println("\n" + text);
    }

    /**
     * Solicita un dato de tio string al usuario
     * 
     * @param texto lo mostrado para solicitar el dato
     * @return String con respuesta del usuario
     */
    public String solicitarS(String texto) {
        System.out.println(texto);
        String ans = scan.nextLine();
        return ans;
    }

    /**
     * Recolecta los datos de programas que se ejecutarán por cierto tiempo en la
     * memoria RAM.
     * 
     * @return Lista de programas que se agregan a la memoria
     */
    public ArrayList<Programa> ingresarPrograma() {
        int continuar;
        ArrayList<Programa> listaprogramas = new ArrayList<Programa>(); // Lista para almacenar los datos
        do {
            String nombre = solicitarS("Escribe el nombre del programa");
            int espacio = solicitarI("Ahora, escribe cuanto espacio ocupa programa, escribe los MB sin decimales.");
            int tiempo = solicitarI("Finalmente, escribe el tiempo, en segundos, que se mantendrá en la memoria.");
            listaprogramas.add(new Programa(nombre, espacio, tiempo)); // Nuevo programa con las características dadas.

            continuar = solicitarI("¿Deseas añadir un nuevo programa?\n 1- Si, 2-No");
        } while (continuar == 1);

        return listaprogramas;
    }

    /**
     * Imprime los datos más relevantes del estado de la memoria de tipo DDR
     * 
     * @param memoria Memoria de la que se sacan los datos
     */
    public void mostrarDatosDDR(MemoriaDDR memoria) {
        System.out.println("\n**** Estado de memoria ****");
        System.out.println("Memoria RAM total: " + (memoria.getRAM().length * 64 / 1024) + "GB");
        System.out.println("Memoria RAM disponible: " + memoria.getDisponible() + "GB");
        System.out.println("Memoria RAM en uso: " + memoria.getEn_uso() + "GB");
        System.out.println("****   ****\n");
    }

    /**
     * Imprime los datos más relevantes del estado de la memoria de tipo SDR
     * 
     * @param memoria Memoria de la que se sacan los datos
     */
    public void mostrarDatosSDR(MemoriaSDR memoria) {
        System.out.println("\n**** Estado de memoria ****");
        System.out.println("Memoria RAM total: " + (memoria.getMemoria().length * 64 / 1024));
        System.out.println("Memoria RAM disponible: " + memoria.getDisponible() + "GB");
        System.out.println("Memoria RAM en uso: " + memoria.getEn_uso() + "GB");
        System.out.println("****   ****\n");

    }

    /**
     * Imprime una solicitud y recibe un dato tipo int
     * Evita una excpción -InputMismatchException- volviendo a solicitar el dato en
     * caso de que ocurra.
     * 
     * @param solicitud texto que describe lo solicitado
     * @return entero ingresado por el usuario
     */
    public int solicitarI(String solicitud) {
        System.out.println(solicitud);
        int solicitar = 0;
        Boolean correcto = false;
        do {
            try {
                solicitar = scan.nextInt();
                correcto = true;
            } catch (InputMismatchException e) {
                scan.next();
                System.out.println("¡Cuidado! Ingresa un número entero.");
            }
        } while (correcto == false);
        scan.nextLine(); // Elimina la posibilidad de saltarse una linea de solicitud después
        return solicitar;
    }
}
