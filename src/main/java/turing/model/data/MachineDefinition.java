package turing.model.data;

import java.util.List;

import turing.model.dto.JsonDto;

public class MachineDefinition {
    String name;
    String emptySpace;
    String initialState;
    List<String> alphabet;
    List<String> haltStates;
    List<JsonDto> transitions;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmptySpace() {
		return emptySpace;
	}
	public void setEmptySpace(String emptySpace) {
		this.emptySpace = emptySpace;
	}
	public String getInitialState() {
		return initialState;
	}
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}
	public List<String> getAlphabet() {
		return alphabet;
	}
	public void setAlphabet(List<String> alphabet) {
		this.alphabet = alphabet;
	}
	public List<String> getHaltStates() {
		return haltStates;
	}
	public void setHaltStates(List<String> haltStates) {
		this.haltStates = haltStates;
	}
	public List<JsonDto> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<JsonDto> transitions) {
		this.transitions = transitions;
	}

}