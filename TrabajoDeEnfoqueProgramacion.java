/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabajodeenfoqueprogramacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Adri
 */


public class TrabajoDeEnfoqueProgramacion {

    public static void main(String[] args) {
        WordleFileManager accediendoFichero = new WordleFileManager();

        // Cargamos las palabras desde el archivo
        String[] words = accediendoFichero.leerPalabras("listadepalabras.txt");

        // Pasamos la lista de palabras a WordleGame
        WordleGame game = new WordleGame(words);
        game.start(); // se inicia el juego
    }
}

//logica principal del juego
class WordleFileManager {

    // método para cargar las palabras desde el archivo
    public String[] leerPalabras(String filename) {
        int wordCount = 0;

        // Contamos cuántas líneas tiene el archivo
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                wordCount++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        String[] words = new String[wordCount];
        int index = 0;

        // Leemos las palabras y las guardamos 
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                words[index++] = line.trim();
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return words;
    }
}

class WordleGame {  // Lógica del juego

    private static final int MAX_TRIES = 6;  // Número máximo de intentos
    private static final int WORD_LENGTH = 5; // Longitud de las palabras

    private final String secretWord; // Palabra secreta
    private int remainingAttempts; // Intentos restantes
    private final List<String> triesHistory; // Historial de intentos

    // Contador para el historial de partidas
    private static int gameCount = 1;

    private final String[] fileWords; // Lista de palabras cargadas desde el archivo

    // Constructor
    public WordleGame(String[] fileWords) {
        this.fileWords = fileWords;  // Guardamos la lista de palabras
        this.secretWord = selectRandomWord(fileWords); // Seleccionamos la palabra secreta
        this.remainingAttempts = MAX_TRIES; // Restante intentos
        this.triesHistory = new ArrayList<>();
    }

    public void start() { // Ciclo principal, si adivina o se queda sin intentos
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido a Wordle!");

        // Bucle hasta que adivine o se quede sin intentos
        while (remainingAttempts > 0) {
            System.out.println("Tienes " + remainingAttempts + " intentos restantes.");

            // Mostrar el historial de intentos
            showTriesHistory();

            String userWord = getUserInput(scanner);
            triesHistory.add(userWord);

            if (userWord.equals(secretWord)) {
                System.out.println("¡Felicidades! Has adivinado la palabra: " + secretWord);
                saveGameHistory(triesHistory, gameCount, secretWord); // Guardamos el historial de la partida
                gameCount++;
                return;
            }

            System.out.println(WordleFeedback.feedBackString(userWord, secretWord));  // Corrección aquí
            remainingAttempts--;
        }

        if (remainingAttempts == 0) {
            System.out.println("Has perdido. La palabra era: " + secretWord);
            saveGameHistory(triesHistory, gameCount, secretWord); // Guardamos el historial de la partida
            gameCount++;
        }
    }

    // Método para seleccionar una palabra aleatoria de la lista pasada como parámetro
    private String selectRandomWord(String[] words) {
        Random random = new Random();
        return words[random.nextInt(words.length)];  // Selecciona una palabra aleatoria
    }

    // Obtener entrada del usuario
    private String getUserInput(Scanner scanner) {
        String entradaPalabra;
        do {
            System.out.print("Ingresa una palabra de 5 letras: ");
            entradaPalabra = scanner.next().toLowerCase(); //convertimos la entrada en minuscula
        } while (entradaPalabra.length() != WORD_LENGTH);//aseguramos que tenga 5 letras
        return entradaPalabra;
    }

    // Método para mostrar el historial de intentos
    private void showTriesHistory() {
        if (!triesHistory.isEmpty()) {
            System.out.println("Intentos anteriores:");
            for (String attempt : triesHistory) {
                String feedback = WordleFeedback.feedBackString(attempt, secretWord);
                System.out.println(feedback);  // Imprime la palabra con los colores aplicados
            }
        }
    }

    // metodo para Guardar el historial de los intentos 
    private void saveGameHistory(List<String> history, int gameCount, String secretWord) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("historialpartidas.txt", true))) {
            writer.println("Partida " + gameCount + ":");
            int attemptNumber = 1;
            // guardamos cada intento
            for (String attempt : history) {
                writer.println("Intento " + attemptNumber + ": " + attempt);
                attemptNumber++;
            }

            // Guardamos la respuesta final 
            if (history.get(history.size() - 1).equals(secretWord)) {
                writer.println("¡Felicidades! Has adivinado la palabra: " + secretWord);
            } else {
                writer.println("Has perdido. La palabra era: " + secretWord);
            }
            writer.println("  --- Siguiente partida ---  ");
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de la partida: " + e.getMessage());
        }
    }
}

class WordleFeedback {

    // Método para dar color a las letras
    private static String applyColor(char letter, String color) {
        return color + letter + Color.RESET;
    }

    // Método para dar el feedback sobre cada intento
    static public String feedBackString(String guess, String secretWord) {
        StringBuilder feedbackColor = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);
            if (letter == secretWord.charAt(i)) {
                feedbackColor.append(applyColor(letter, Color.GREEN)); // Letra correcta (verde)
            } else if (secretWord.contains(String.valueOf(letter))) {
                feedbackColor.append(applyColor(letter, Color.YELLOW)); // Letra correcta pero en otro lugar (amarillo)
            } else {
                feedbackColor.append(applyColor(letter, Color.GRAY)); // Letra incorrecta (gris)
            }
        }
        return feedbackColor.toString();
    }
}

class Color {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m"; //color correcto
    public static final String YELLOW = "\u001B[33m"; //color correcto en otra posicion
    public static final String GRAY = "\u001B[37m"; //color incorrecto
}


//editado para que puedas jugar partidas una detras de otra sin recargar


/*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TrabajoDeEnfoqueProgramacion {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordleFileManager accediendoFichero = new WordleFileManager();
        // Cargamos la lista de palabras una sola vez y la reutilizamos para todas las partidas
        String[] words = accediendoFichero.leerPalabras("listadepalabras.txt");

        boolean jugarOtra = true;
        while (jugarOtra) {
            WordleGame game = new WordleGame(words);
            game.start();

            System.out.print("¿Deseas jugar otra partida? (si/no): ");
            String respuesta = scanner.next().toLowerCase();
            if (!respuesta.equals("si")) {
                jugarOtra = false;
            }
        }
        scanner.close();
    }
}

class WordleFileManager {
    // Método para cargar las palabras desde el archivo en una sola pasada
    public String[] leerPalabras(String filename) {
        List<String> wordsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsList.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return wordsList.toArray(new String[0]);
    }
}

class WordleGame {
    private static final int MAX_TRIES = 6;   // Número máximo de intentos
    private static final int WORD_LENGTH = 5; // Longitud de la palabra

    private final String secretWord; // Palabra secreta
    private int remainingAttempts;   // Intentos restantes
    private final List<String> triesHistory; // Historial de intentos
    private String[] fileWords;      // Lista de palabras cargadas

    // Contador para el historial de partidas
    private static int gameCount = 1;

    // Constructor
    public WordleGame(String[] fileWords) {
        this.fileWords = fileWords;
        this.secretWord = selectRandomWord(fileWords);
        this.remainingAttempts = MAX_TRIES;
        this.triesHistory = new ArrayList<>();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido a Wordle!");
        
        // Bucle principal: mientras queden intentos
        while (remainingAttempts > 0) {
            System.out.println("Tienes " + remainingAttempts + " intentos restantes.");
            showTriesHistory();

            String userWord = getUserInput(scanner);
            triesHistory.add(userWord);

            if (userWord.equals(secretWord)) {
                System.out.println("¡Felicidades! Has adivinado la palabra: " + secretWord);
                saveGameHistory(triesHistory, gameCount, secretWord);
                gameCount++;
                return;
            }

            System.out.println(WordleFeedback.feedBackString(userWord, secretWord));
            remainingAttempts--;
        }

        // Si se agotan los intentos
        if (remainingAttempts == 0) {
            System.out.println("Has perdido. La palabra era: " + secretWord);
            saveGameHistory(triesHistory, gameCount, secretWord);
            gameCount++;
        }
    }

    // Seleccionar una palabra aleatoria de la lista
    private String selectRandomWord(String[] words) {
        Random random = new Random();
        return words[random.nextInt(words.length)];
    }

    // Obtener entrada del usuario y asegurar que tenga 5 letras
    private String getUserInput(Scanner scanner) {
        String entradaPalabra;
        do {
            System.out.print("Ingresa una palabra de 5 letras: ");
            entradaPalabra = scanner.next().toLowerCase();
        } while (entradaPalabra.length() != WORD_LENGTH);
        return entradaPalabra;
    }

    // Mostrar el historial de intentos con su feedback
    private void showTriesHistory() {
        if (!triesHistory.isEmpty()) {
            System.out.println("Intentos anteriores:");
            for (String attempt : triesHistory) {
                String feedback = WordleFeedback.feedBackString(attempt, secretWord);
                System.out.println(feedback);
            }
        }
    }

    // Guardar el historial de la partida en un archivo
    private void saveGameHistory(List<String> history, int gameCount, String secretWord) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("historialdepartidas.txt", true))) {
            writer.println("Partida " + gameCount + ":");
            int attemptNumber = 1;
            for (String attempt : history) {
                writer.println("Intento " + attemptNumber + ": " + attempt);
                attemptNumber++;
            }
            if (history.get(history.size() - 1).equals(secretWord)) {
                writer.println("¡Felicidades! Has adivinado la palabra: " + secretWord);
            } else {
                writer.println("Has perdido. La palabra era: " + secretWord);
            }
            writer.println(" --- Siguiente partida --- ");
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de la partida: " + e.getMessage());
        }
    }
}

class WordleFeedback {
    // Aplica color a una letra
    private static String applyColor(char letter, String color) {
        return color + letter + Color.RESET;
    }

    // Devuelve un String con el feedback coloreado según el intento
    public static String feedBackString(String guess, String secretWord) {
        StringBuilder feedbackColor = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);
            if (letter == secretWord.charAt(i)) {
                feedbackColor.append(applyColor(letter, Color.GREEN)); // Correcto y en su sitio
            } else if (secretWord.contains(String.valueOf(letter))) {
                feedbackColor.append(applyColor(letter, Color.YELLOW)); // Correcto pero en otro sitio
            } else {
                feedbackColor.append(applyColor(letter, Color.GRAY)); // Incorrecto
            }
        }
        return feedbackColor.toString();
    }
}

class Color {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";   // Letra correcta
    public static final String YELLOW = "\u001B[33m";  // Letra en posición incorrecta
    public static final String GRAY = "\u001B[37m";    // Letra incorrecta
}

*/
