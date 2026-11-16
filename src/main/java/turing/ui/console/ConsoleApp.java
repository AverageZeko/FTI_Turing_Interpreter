package turing.ui.console;

import java.util.Scanner;

import turing.auxiliary.JsonHelper;
import turing.model.data.MachineDefinition;
import turing.model.turing_machine.TuringMachine;
import turing.services.SimulationService;
import turing.ui.UIRender;

public class ConsoleApp implements UIRender {
    private final ConsoleRenderer render;
    private final SimulationService simulationService;

    public ConsoleApp() {
        this.render = new ConsoleRenderer();
        this.simulationService = new SimulationService();
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
        aTuringMachine.setTape(simulationService.setTape(word, iterations));
        this.render.printInitialData(aTuringMachine);
        this.render.printStepHeader();
        while (!aTuringMachine.isHalted())
            this.render.printStep(aTuringMachine.step());

        this.render.printResult(aTuringMachine);

        aScanner.close();
        System.out.println("\nPrograma finalizado.");
    }
}
