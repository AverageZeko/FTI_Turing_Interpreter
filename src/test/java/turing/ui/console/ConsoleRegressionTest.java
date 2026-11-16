package turing.ui.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import turing.config.AppConfig;

/**
 * Compares the console output with the reference executions stored in test/prueba_*.txt.
 */
class ConsoleRegressionTest {

    private static final Pattern INITIAL_TAPE = Pattern.compile("^Cinta:.*#([01]*)#([01]*)#.*$", Pattern.MULTILINE);

    static Stream<Path> referenceFiles() throws IOException {
        List<Path> files;
        try (Stream<Path> listing = Files.list(Path.of("test"))) {
            files = listing.filter(p -> p.getFileName().toString().matches("prueba_\\d+\\.txt")).sorted().toList();
        }
        assertFalse(files.isEmpty(), "No se encontraron archivos de referencia en test/");
        return files.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("referenceFiles")
    void consoleOutputMatchesReference(Path reference) throws IOException {
        String expected = Files.readString(reference, StandardCharsets.UTF_8);
        Matcher matcher = INITIAL_TAPE.matcher(expected);
        assertFalse(!matcher.find(), "El archivo no tiene la linea 'Cinta:' con la entrada");
        String iterations = matcher.group(1);
        String word = matcher.group(2);

        String actual = runConsole(word + "\n" + iterations + "\n");

        assertEquals(normalize(expected, word, iterations), normalize(actual, word, iterations));
    }

    private static String runConsole(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            new ConsoleApp(AppConfig.from(new Properties())).run();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    /**
     * Some references were recorded in an interactive terminal, so they contain the echo of the typed
     * input and different blank lines. Those differences are not part of the program output.
     */
    private static String normalize(String text, String word, String iterations) {
        StringBuilder result = new StringBuilder();
        for (String line : text.split("\\R")) {
            String trimmed = line.strip();
            if (trimmed.isEmpty() || trimmed.equals(word) || trimmed.equals(iterations)) {
                continue;
            }
            result.append(line.stripTrailing()).append('\n');
        }
        return result.toString();
    }
}
