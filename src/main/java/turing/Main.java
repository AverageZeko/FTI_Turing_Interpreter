package turing;

import java.util.Scanner;

import turing.auxiliary.JsonHelper;
import turing.model.data.MachineDefinition;
import turing.model.turing_machine.TuringMachine;


public class Main {
    public static void main(String[] args) {
        Scanner aScanner = new Scanner(System.in);
        MachineDefinition machineDefinition = JsonHelper.fetchMachineFromResource("rule_110");
        TuringMachine aTuringMachine = new TuringMachine(machineDefinition);

        System.out.println("\n=== 1. REGLA 110 ===");
        System.out.println("Escriba su cadena binaria a analizar (8 simbolos minimo)");
        String word = aScanner.nextLine();
        System.out.println("Escriba el numero binario de repeticiones");
        String iterations = aScanner.nextLine();
        aTuringMachine.setTape(word, iterations);
        aTuringMachine.printInitialData();
        aTuringMachine.run();

        aScanner.close();
        System.out.println("\nPrograma finalizado.");
    }

}