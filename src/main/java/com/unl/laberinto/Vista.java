package com.unl.laberinto;

public class Vista {
    public static void main(String[] args) throws Exception {
        int dimension = 50; // Puedes cambiar este valor según lo necesites (entre 30 y 100)

        if (dimension < 30 || dimension > 100) {
            System.out.println("Dimensión inválida. El valor debe estar entre 30 y 100.");
            return;
        }

        System.out.println("Generando laberinto de dimensión " + dimension + "x" + dimension);
        Laberinto.mostrarLaberinto(dimension);

        boolean resolver = true; // Cambiar a false si no quieres resolver el laberinto automáticamente

        if (resolver) {
            System.out.println("Resolviendo laberinto...");
            Laberinto.calcularRuta(dimension);
        } else {
            System.out.println("Saliendo sin resolver el laberinto.");
        }
    }
}
