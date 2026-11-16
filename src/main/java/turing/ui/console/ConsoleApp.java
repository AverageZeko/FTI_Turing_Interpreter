package turing.ui.console;

import java.io.UncheckedIOException;
import java.util.Scanner;

import turing.config.AppConfig;
import turing.model.turing_machine.TuringMachine;
import turing.services.SimulationService;
import turing.ui.UIRender;

public class ConsoleApp implements UIRender {
    private final ConsoleRenderer render;
    private final SimulationService simulationService;
    private final AppConfig config;

    public ConsoleApp(AppConfig config) {
        this.config = config;
        this.render = new ConsoleRenderer();
        this.simulationService = new SimulationService();
    }

    public void run() {
        Scanner aScanner = new Scanner(System.in);
        try {
            TuringMachine aTuringMachine = simulationService.loadMachine(config.getMachineRule());

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
            System.out.println("\nPrograma finalizado.");
        } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException anException) {
            System.err.println("\nError: " + anException.getMessage());
        } finally {
            aScanner.close();
        }
    }
}
