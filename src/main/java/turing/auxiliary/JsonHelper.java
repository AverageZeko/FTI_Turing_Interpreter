package turing.auxiliary;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import turing.model.data.MachineDefinition;

public class JsonHelper {

    private static final String RULES_FOLDER = "/rules/";
    private static final String JSON_EXTENSION = ".json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonHelper() {
    }

    /**
     * Loads a machine definition stored in src/main/resources/rules.
     * @param machineName the file name without the ".json" sufix (e.g. "b_rule_110")
     * @return the machine definition described by the file
     */
    public static MachineDefinition fetchMachineFromResource(String machineName) {
        return jsonToMachineDefinition(readJsonResource(machineName));
    }

    /**
     * Reads a JSON file from the classpath
     * @param jsonName the file name inside the rules folder
     * @return the content of the file
     * @throws IllegalArgumentException if the file does not exist
     * @throws UncheckedIOException if the file cannot be read
     */
    private static String readJsonResource(String aJsonName) {
        String aFileName = aJsonName + JSON_EXTENSION;
        String aPath = RULES_FOLDER + aFileName;

        try (InputStream input = JsonHelper.class.getResourceAsStream(aPath)) {
            if (input == null) {
                throw new IllegalArgumentException("No se encontro el archivo " + aPath);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException anException) {
            throw new UncheckedIOException("No se pudo leer el archivo " + aPath, anException);
        }
    }

    /**
     * Converts the JSON text into a machine definition using Jackson.
     * @param json the JSON text
     * @return the machine definition
     * @throws IllegalArgumentException if the JSON is malformed or does not match the definition
     */
    private static MachineDefinition jsonToMachineDefinition(String json) {
        try {
            return MAPPER.readValue(json, MachineDefinition.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("El JSON de la maquina es invalido: " + e.getOriginalMessage(), e);
        }
    }
}
