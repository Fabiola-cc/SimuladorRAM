/*
 * Fabiola Contreras 22787
 * --Laboratorio 2 POO--
 * 
 * Vista es la clase que se encarga de mostrar y solicitar datos al usuario del programa. 
 */

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controlador {
    public int tipo;
    public int tamaño;
    public Vista view = new Vista();
    public MemoriaSDR sdr;
    public MemoriaDDR ddr;

    /**
     * Crea un objeto de la clase específica de memoria solicitada y almacena esos
     * datos.
     */
    public void inicio() {
        view.show("Bienvenid@ al simulador de memoria RAM");
        Boolean intento = true;
        do {
            tipo = view.solicitarI(
                    "¿Qué tipo de memoria RAM deseas emplear?\nEscribe 1 (para memoria SDR) o 2 (para memoria DDR).");
            if (tipo == 1) {
                boolean bien = false;

                do {
                    tamaño = view.solicitarI(
                            "Ahora, escribe la cantidad, en GB de memoria RAM con la que cuentas.\nTus opciones son: 4, 8, 12, 16, 32 o 64");
                    if (tamaño == 4 || tamaño == 8 || tamaño == 12 || tamaño == 16 || tamaño == 32 || tamaño == 64) {
                        bien = true;
                    } else {
                        view.show(
                                "Disculpa, debes ingresar un tamaño en GB de memoria.");
                        bien = false;
                    }
                } while (bien == false);

                int bloques = tamaño * 1024 / 64;
                sdr = new MemoriaSDR(bloques); // Nuevo objeto de tipo SDR
                intento = false;
            } else if (tipo == 2) {
                ddr = new MemoriaDDR(4 * 1024 / 64); // Nuevo objeto de tipo DDR,inicialmente es de 4GB
                for (int i = 0; i < ddr.getRAM().length; i++) {
                    ddr.getRAM()[i][0] = "Libre"; // Inicialmente todo espacio es libre
                }
                intento = false;
            } else {
                view.show("¡Ciudado! Escribe 1 o 2");
                view.solicitarS("Presiona Enter para continuar.");
            }

        } while (intento == true);
    }

    /**
     * Ordena el proceso de trabajo del programa, llamando a otros métodos según sea
     * necesario.
     */
    public void ejecutar() {
        boolean continuar = true;
        disponible(); // Permite establece valores iniciales sobre el espacio libre en memoria.
        if (tipo == 1) { // Memoria SDR
            do {
                view.show("Su memoria es de tipo SDR\nIniciemos añadiendo programas a la RAM");
                ProgramasIniciales();
                view.mostrarDatosSDR(sdr);

                for (int i = 0; i < sdr.getMemoria().length; i++) {
                    if (sdr.getMemoria()[i][0] != null) {
                        eliminarSDR(sdr.getMemoria()[i][0]);
                    }
                }

                int cont = view.solicitarI("Desea añadir nuevos programas a la memoria\n1-si, 2-no");
                if (cont == 2) {
                    continuar = false;
                }

            } while (continuar);
        } else if (tipo == 2) { // Memoria DDR
            do {
                view.show("Su memoria es de tipo DDR\nIniciemos añadiendo que programas iniciarán a la RAM.");
                ProgramasIniciales();

                view.mostrarDatosDDR(ddr);
                view.show("Ahora, que programas añadirá a la cola de espera.");
                ddr.setEnespera(view.ingresarPrograma());

                new Thread(() -> espera()).start();

                int cont = view.solicitarI("Desea añadir nuevos programas a la lista de espera\n1-si, 2-no");
                if (cont == 2) {
                    continuar = false;
                    view.show("\nConcluyendo procesos...");
                    fin();
                }
            } while (continuar);
        }
    }

    /**
     * Utilizando el método de vista, solicita datos al usuario sobre los programas
     * por añadir a la memoria.
     */
    public void ProgramasIniciales() { // Están en la memoria RAM desde el inicio
        ArrayList<Programa> listaprogramas = view.ingresarPrograma();
        int espacio_total = 0;
        for (Programa p : listaprogramas) {
            espacio_total += p.getEspacio();
        }
        for (Programa p : listaprogramas) {
            if (tipo == 1) { // Añade cada programa a la memoria, considerando el tipo existente.
                Asignar_en_SDR(p);
            } else {
                Asignar_en_DDR(p, espacio_total / 64);
            }
        }
        view.show("Todos los programas se ejecutan en la RAM");
    }

    /**
     * Registra los datos de un programa en la memoria de tipo SDR.
     * 
     * @param por_asignar Es el objeto, de clase programa que será utilizado y del
     *                    cual se sacan los datos para registrar en la memoria.
     */
    public void Asignar_en_SDR(Programa por_asignar) {
        int ocupa = (int) Math.ceil(por_asignar.getEspacio() / 64); // Bloques que ocupa el programa
        if (ocupa <= sdr.getDisponible()) {
            for (int j = 0; j < ocupa; j++) { // Por cada bloque a ocupar (cada 64MB que ocupa el programa)
                for (int i = 0; i < sdr.getMemoria().length; i++) { // Por cada FILA
                    if (sdr.getMemoria()[i][0] == null) {
                        sdr.getMemoria()[i][0] = por_asignar.getNombre(); // Nombre
                        sdr.getMemoria()[i][1] = String.valueOf(por_asignar.getTiempo());
                        break;
                    }

                }
            }
            sdr.setDisponible(sdr.getDisponible() - ((double) por_asignar.getEspacio() / 1024)); // Resta los GB
                                                                                                 // ocupados por el
            // programa al espacio disponible
            sdr.setEn_uso(sdr.getEn_uso() + ((double) por_asignar.getEspacio() / 1024));

            String asignado = por_asignar.getNombre();
            view.show("Se está ejecutando " + asignado);

        } else {
            view.show("No hay suficiente espacio para este programa en la RAM, espera unos segundos");

        }
    }

    /**
     * Se registran datos de un programa, en la memoria DDR.
     * 
     * @param por_asignar Es el objeto, de clase programa que será utilizado y del
     *                    cual se sacan los datos para registrar en la memoria.
     */
    public void Asignar_en_DDR(Programa por_asignar, int espacio_necesario) {
        Modificar_DDR(espacio_necesario); // Cambia el tamaño del espacio, según sea necesario
        int ocupa = (int) Math.ceil(por_asignar.getEspacio() / 64); // Bloques que ocupa el programa;

        for (int j = 0; j < ocupa; j++) { // Por cada bloque a ocupar
            for (int i = 0; i < ddr.getRAM().length; i++) { // Por cada FILA
                if (ddr.getRAM()[i][0] == "Libre") {
                    ddr.getRAM()[i][0] = "Ocupado"; // Ocupado
                    ddr.getRAM()[i][1] = por_asignar.getNombre(); // nombre
                    ddr.getRAM()[i][2] = String.valueOf(por_asignar.getTiempo()); // tiempo

                    break;
                }

            }
        }
        ddr.setDisponible(ddr.getDisponible() - ((double) por_asignar.getEspacio() / 1024)); // Resta los GB ocupados
                                                                                             // por el
        // programa al espacio disponible
        ddr.setEn_uso(ddr.getEn_uso() + ((double) por_asignar.getEspacio() / 1024));

        String asignado = por_asignar.getNombre();
        view.show(asignado + " se ejecuta en la RAM.");
        eliminarDDR(asignado);
    }

    /**
     * Este método cambia el tamaño del arreglo de la memoria, lo aumenta o
     * disminuye, según sea necesario.
     * 
     * @param tamaño tamaño en MB de los datos que serán ingresados a la memoria.
     */
    public void Modificar_DDR(int tamaño) {
        int bloques = ddr.getRAM().length;
        int Actualizar_ram = 0;
        String[][] nuevo;

        if (tamaño <= ddr.getRAM().length) {
            if (ddr.getRAM().length != 64) {
                if (tamaño < ddr.getRAM().length / 8) {
                    Actualizar_ram = bloques / 8;
                } else if (tamaño < ddr.getRAM().length * 0.25) {
                    Actualizar_ram = bloques / 4;
                } else if (tamaño < ddr.getRAM().length * 0.5) {
                    Actualizar_ram = bloques / 2;
                }

                nuevo = new String[Actualizar_ram][3];
                System.arraycopy(ddr.getRAM(), 0, nuevo, 0, Actualizar_ram); // Copiar los datos desde el anterior array
                ddr.setRAM(nuevo);
                view.show("El tamaño de RAM principal cambió a: " + (ddr.getRAM().length * 64 / 1024) + "GB");
            }
        } else {

            if (bloques < 1024) {
                if (bloques <= 192) { // Si es menor o igual a 12 GB se le aumentan 4 GB de memoria
                    Actualizar_ram = bloques + (4 * 1024 / 64);
                } else if (bloques >= 256) { // Si supera los 16GB se duplica el espacio de memoria
                    Actualizar_ram = bloques + (bloques);
                }
                nuevo = new String[Actualizar_ram][3];
                System.arraycopy(ddr.getRAM(), 0, nuevo, 0, bloques); // Copiar los datos en el anterior array
                ddr.setRAM(nuevo);
                view.show("El tamaño de RAM principal cambió a: " + (ddr.getRAM().length * 64 / 1024) + "GB");
            } else {
                view.show("Se excede el límite de la memoria.");
            }
        }
    }

    /**
     * Devuelve el espacio necesario para la creacion de la matriz, según los
     * programas enlistados
     * Además, busca asignar cada programa en la memoria DDR.
     */
    public void espera() {
        int espacio_total = 0;
        for (Programa p : ddr.getEnespera()) { // Sumatoria de todo el espacio necesario
            espacio_total += p.getEspacio();
        }

        for (Programa p : ddr.getEnespera()) {
            if (p != null) {
                Asignar_en_DDR(p, espacio_total / 64);
            }
        }

    }

    /**
     * Elimina los datos de cada programa en el tiempo antes establecido. (Para la
     * memoria SDR)
     * 
     * @param programa_a_borrar Solicita el nombre del programa que se va a borrar.
     */
    public void eliminarSDR(String programa_a_borrar) {
        long tiempo = 0;
        for (int i = 0; i < sdr.getMemoria().length; i++) { // Por cada FILA
            String programa = sdr.getMemoria()[i][0];
            if (programa != null) {
                if (programa.equals(programa_a_borrar)) { // Si contiene el nombre del programa
                    tiempo = (long) Integer.valueOf(sdr.getMemoria()[i][1]) * 1000;
                    break;
                }
            }
        }

        Timer nTimer = new Timer();
        TimerTask borrador = new TimerTask() {
            public void run() {
                for (int i = 0; i < sdr.getMemoria().length; i++) { // Por cada FILA
                    String programa = sdr.getMemoria()[i][0];
                    if (programa != null) {
                        if (programa.equals(programa_a_borrar)) { // Si se cuenta con el nombre en el
                                                                  // listado
                            for (int k = 0; k < sdr.getMemoria()[i].length; k++) { // Por cada "columa" en la fila
                                sdr.setDisponible(sdr.getDisponible() + (64 / 1024));
                                sdr.setEn_uso(sdr.getEn_uso() - (64 / 1024));
                                sdr.getMemoria()[i][k] = null; // Eliminar los datos
                            }
                        }
                    }
                }
                view.show("\t(" + programa_a_borrar + " dejó la memoria RAM)");
            }
        };
        nTimer.schedule(borrador, tiempo);
    }

    /**
     * Elimina los datos de cada programa en el tiempo antes establecido. (Para la
     * memoria DDR)
     * 
     * @param programa_a_borrar Solicita el nombre del programa que se va a borrar.
     */
    public void eliminarDDR(String programa_a_borrar) {
        long tiempo = 0;
        for (int i = 0; i < ddr.getRAM().length; i++) { // Por cada FILA
            String programa = ddr.getRAM()[i][1];
            if (programa != null) {
                if (programa.equals(programa_a_borrar)) { // Si contiene el nombre del programa
                    tiempo = (long) Integer.valueOf(ddr.getRAM()[i][2]) * 1000;
                    break;
                }
            }
        }

        Timer nTimer = new Timer();
        TimerTask borrador = new TimerTask() {
            public void run() {
                for (int i = 0; i < ddr.getRAM().length; i++) { // Por cada FILA
                    String programa = ddr.getRAM()[i][1];
                    if (programa != null) {
                        if (programa.equals(programa_a_borrar)) { // Si se cuenta con el nombre en el
                                                                  // listado
                            for (int k = 1; k < ddr.getRAM()[i].length; k++) { // Por cada "columa" en la fila
                                ddr.setDisponible(ddr.getDisponible() + (64 / 1024));
                                ddr.setEn_uso(ddr.getEn_uso() - (64 / 1024));
                                ddr.getRAM()[i][0] = "Libre"; // Establecer el espacio como libre
                                ddr.getRAM()[i][k] = null; // Eliminar los datos
                            }
                        }
                    }
                }
                view.show(programa_a_borrar + " dejó la memoria RAM");
            }
        };
        nTimer.schedule(borrador, tiempo);
    }

    /**
     * Inicialmente busca y establece el valor de espacio disponible para la memoria
     * del tipo establecido
     */
    public void disponible() {
        double disponible = 0;
        double usado = 0;

        if (tipo == 1) {
            for (String[] fila : sdr.getMemoria()) { // Cada fila
                if (fila[0] == null) {
                    disponible++; // Suma un dato de espacio disponible según las filas vacías
                } else {
                    usado++; // Si la fila está ocupada se considera ocupado
                }
            }
            sdr.setDisponible(disponible * 64 / 1024);
            sdr.setEn_uso(usado * 64 / 1024);

        } else {
            for (String[] fila : ddr.getRAM()) {
                if (fila[0].equals("Libre")) {
                    disponible++;
                } else {
                    usado++;
                }
            }
            ddr.setDisponible(disponible * 64 / 1024);
            ddr.setEn_uso(usado * 64 / 1024);

        }
    }

    /**
     * Termina con toda acción del programa luego de esperar cierto tiempo para
     * eliminar toda posibilidad de que haya un programa de la RAM.
     */
    public void fin() {
        int tiempo = 0;
        if (tipo == 1) {
            for (int i = 0; i < sdr.getMemoria().length; i++) { // Por cada fila
                if (sdr.getMemoria()[i][0] != null) {
                    String nombre = sdr.getMemoria()[i][0];
                    if (sdr.getMemoria()[i][0].equals(nombre)) {
                        if (sdr.getMemoria()[i][1] != null) {
                            tiempo += Integer.valueOf(sdr.getMemoria()[i][1]);
                        }
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < ddr.getRAM().length; i++) {
                if (ddr.getRAM()[i][1] != null) {
                    String nombre = ddr.getRAM()[i][1];
                    if (ddr.getRAM()[i][1].equals(nombre)) {
                        if (ddr.getRAM()[i][2] != null) {
                            tiempo += Integer.valueOf(ddr.getRAM()[i][2]);
                        }
                        break;
                    }
                }
            }
        }
        TimerTask end = new TimerTask() {
            public void run() {
                view.show("Saliendo...");
                System.exit(0);
            }
        };
        new Timer().schedule(end, tiempo);

    }
}
