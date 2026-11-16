package turing;

import turing.ui.UIRender;
import turing.ui.console.ConsoleApp;

public class Main {
    private final static UIRender consoleApp = new ConsoleApp();

    public static void main(String[] args) {
        consoleApp.run();
        // Scanner aScanner = new Scanner(System.in);
        // MachineDefinition machineDefinition =
        // JsonHelper.fetchMachineFromResource("rule_110_trace");
        // TuringMachine aTuringMachine = new TuringMachine(machineDefinition);

        // System.out.println("\n=== 1. REGLA 110 ===");
        // System.out.println("Escriba su cadena binaria a analizar (8 simbolos
        // minimo)");
        // String word = aScanner.nextLine();
        // System.out.println("Escriba el numero binario de repeticiones");
        // String iterations = aScanner.nextLine();
        // aTuringMachine.setTape(word, iterations);
        // render.printInitialData(aTuringMachine);
        // aTuringMachine.run();
        // aTuringMachine.printResult();

        // aScanner.close();
        // System.out.println("\nPrograma finalizado.");
    }

}