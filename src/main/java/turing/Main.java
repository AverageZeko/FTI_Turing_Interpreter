package turing;

import turing.config.AppConfig;
import turing.ui.UIRender;
import turing.ui.UIRenderFactory;

public class Main {

    public static void main(String[] args) {
        AppConfig config;
        try {
            config = AppConfig.load(args);
        } catch (IllegalArgumentException anException) {
            System.err.println("Configuracion invalida: " + anException.getMessage());
            System.exit(1);
            return;
        }
        UIRender ui = UIRenderFactory.create(config);
        ui.run();
    }

}
