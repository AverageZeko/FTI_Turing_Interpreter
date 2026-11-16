package turing.ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import turing.config.AppConfig;
import turing.ui.console.ConsoleApp;

class UIRenderFactoryTest {

    private static AppConfig configWithUi(String uiType) {
        Properties properties = new Properties();
        properties.setProperty(AppConfig.UI_TYPE, uiType);
        return AppConfig.from(properties);
    }

    @Test
    void consoleTypeCreatesConsoleApp() {
        assertInstanceOf(ConsoleApp.class, UIRenderFactory.create(configWithUi("console")));
    }

    @Test
    void swingTypeFallsBackToConsoleWithoutDisplay() {
        // surefire runs the tests with -Djava.awt.headless=true
        assertInstanceOf(ConsoleApp.class, UIRenderFactory.create(configWithUi("swing")));
    }
}
