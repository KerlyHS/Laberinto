package com.unl.laberinto;

import com.unl.laberinto.base.controller.datastruct.graphs.Adjacency;
import com.unl.laberinto.base.controller.datastruct.graphs.Prim2;
import com.unl.laberinto.base.controller.datastruct.graphs.UndirectLabelGraph;
import com.unl.laberinto.base.controller.datastruct.list.LinkedList;

public class Laberinto {

    private static char[][] matrizLaberinto = null;

    // Códigos ANSI de colores
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_BLACK = "\u001B[90m";

    public static void mostrarLaberinto(int dimension) throws Exception {
        matrizLaberinto = crearMatrizLaberinto(dimension);
        imprimirMatriz(matrizLaberinto, null);
    }

    public static void calcularRuta(int dimension) throws Exception {
        char[][] lab = matrizLaberinto;

        int[] datos = contarNodosEspeciales(lab);
        int inicio = datos[0];
        int destino = datos[1];
        int totalNodos = datos[2];

        int[][] mapaIndices = crearMapaIndices(lab);
        int[][] mapaCoordenadas = crearMapaCoordenadas(lab, totalNodos);
        int[][] conexiones = obtenerConexiones(lab, mapaIndices);

        LinkedList<LinkedList<Adjacency>> grafo = UndirectLabelGraph.constructAdj(conexiones, totalNodos);
        int[] distancias = UndirectLabelGraph.dijkstra(grafo, inicio, totalNodos);

        boolean[][] ruta = trazarRuta(grafo, distancias, mapaCoordenadas, inicio, destino, lab.length);
        imprimirMatriz(lab, ruta);
        System.out.println("\nDistancia más corta: " + distancias[destino]);
    }

    private static char[][] crearMatrizLaberinto(int dimension) throws Exception {
        Prim2 generador = new Prim2();
        String datos = generador.generar(dimension, dimension);
        char[][] resultado = new char[dimension][dimension];
        String[] lineas = datos.split("\n");

        for (int i = 0; i < dimension; i++) {
            String[] celdas = lineas[i].split(",");
            for (int j = 0; j < dimension; j++) {
                resultado[i][j] = celdas[j].charAt(0);
            }
        }
        return resultado;
    }

    private static int[] contarNodosEspeciales(char[][] lab) {
        int tam = lab.length;
        int start = -1, end = -1, contador = 0;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (lab[i][j] != '0') {
                    if (lab[i][j] == 'S') start = contador;
                    if (lab[i][j] == 'E') end = contador;
                    contador++;
                }
            }
        }
        return new int[]{start, end, contador};
    }

    private static int[][] crearMapaIndices(char[][] lab) {
        int tam = lab.length;
        int[][] mapa = new int[tam][tam];
        int index = 0;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (lab[i][j] != '0') {
                    mapa[i][j] = index++;
                }
            }
        }
        return mapa;
    }

    private static int[][] crearMapaCoordenadas(char[][] lab, int totalNodos) {
        int tam = lab.length;
        int[][] coordenadas = new int[totalNodos][2];
        int contador = 0;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (lab[i][j] != '0') {
                    coordenadas[contador][0] = i;
                    coordenadas[contador][1] = j;
                    contador++;
                }
            }
        }
        return coordenadas;
    }

    private static int[][] obtenerConexiones(char[][] lab, int[][] mapaIndices) {
        int tam = lab.length;
        int[][] direcciones = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        LinkedList<int[]> aristas = new LinkedList<>();

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (lab[i][j] != '0') {
                    for (int[] dir : direcciones) {
                        int ni = i + dir[0];
                        int nj = j + dir[1];
                        if (ni >= 0 && ni < tam && nj >= 0 && nj < tam && lab[ni][nj] != '0') {
                            aristas.add(new int[]{mapaIndices[i][j], mapaIndices[ni][nj], 1});
                        }
                    }
                }
            }
        }

        int[][] resultado = new int[aristas.getSize()][3];
        for (int i = 0; i < aristas.getSize(); i++) {
            int[] e = aristas.get(i);
            resultado[i][0] = e[0];
            resultado[i][1] = e[1];
            resultado[i][2] = e[2];
        }
        return resultado;
    }

    private static boolean[][] trazarRuta(LinkedList<LinkedList<Adjacency>> grafo, int[] distancias, int[][] coordenadas, int inicio, int destino, int tam) {
        boolean[][] ruta = new boolean[tam][tam];
        int actual = destino;

        while (actual != inicio) {
            int i = coordenadas[actual][0];
            int j = coordenadas[actual][1];
            ruta[i][j] = true;

            LinkedList<Adjacency> vecinos = grafo.get(actual);
            int siguiente = -1;
            for (int k = 0; k < vecinos.getSize(); k++) {
                Adjacency vecino = vecinos.get(k);
                if (distancias[vecino.getdestiny()] == distancias[actual] - 1) {
                    siguiente = vecino.getdestiny();
                    break;
                }
            }
            if (siguiente == -1) break;
            actual = siguiente;
        }

        int si = coordenadas[inicio][0], sj = coordenadas[inicio][1];
        ruta[si][sj] = true;
        return ruta;
    }

    private static void imprimirMatriz(char[][] lab, boolean[][] ruta) {
        int tam = lab.length;
        System.out.print("   ");
        for (int i = 0; i < tam; i++) {
            System.out.print("─");
        }
        System.out.println();

        for (int i = 0; i < tam; i++) {
            System.out.print("│ "); // Borde izquierdo
            for (int j = 0; j < tam; j++) {
                char c = lab[i][j];
                if (c == '0') {
                    System.out.print(BRIGHT_BLACK + "█" + RESET);
                } else if (c == 'S') {
                    System.out.print(GREEN + "S" + RESET);
                } else if (c == 'E') {
                    System.out.print(RED + "E" + RESET);
                } else if (ruta != null && ruta[i][j]) {
                    System.out.print(YELLOW + "*" + RESET);
                } else {
                    System.out.print(WHITE + "·" + RESET);
                }
            }
            System.out.println(" │"); 
        }

        System.out.print("   ");
        for (int i = 0; i < tam; i++) {
            System.out.print("─");
        }
        System.out.println();
    }

}
