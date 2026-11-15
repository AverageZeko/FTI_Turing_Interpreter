package turing.ui.console;

import java.util.Scanner;

import turing.auxiliary.JsonHelper;
import turing.model.data.MachineDefinition;
import turing.model.turing_machine.TuringMachine;
import turing.ui.UIRender;

public class ConsoleApp implements UIRender{
    private final ConsoleRederer render;

    public ConsoleApp() {
        this.render = new ConsoleRederer();
    }

    public void run() {
        Scanner aScanner = new Scanner(System.in);
        MachineDefinition machineDefinition = JsonHelper.fetchMachineFromResource("rule_110_trace");
        TuringMachine aTuringMachine = new TuringMachine(machineDefinition);

        System.out.println("\n=== 1. REGLA 110 ===");
        System.out.println("Escriba su cadena binaria a analizar (8 simbolos minimo)");
        String word = aScanner.nextLine();
        System.out.println("Escriba el numero binario de repeticiones");
        String iterations = aScanner.nextLine();
        aTuringMachine.setTape(word, iterations);
        this.render.printInitialData(aTuringMachine);
        aTuringMachine.run();
        aTuringMachine.printResult();

        aScanner.close();
        System.out.println("\nPrograma finalizado.");
    }
}
