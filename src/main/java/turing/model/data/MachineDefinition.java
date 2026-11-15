package turing.model.data;

import java.util.List;
import java.util.Set;

import turing.model.dto.JsonDto;

public class MachineDefinition {
    String name;
    String emptySpace;
    Set<String> alphabet;
	Set<String> states;
	String initialState;
    Set<String> haltStates;
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
	public Set<String> getStates() {
		return states;
	}
	public void setStates(Set<String> states) {
		this.states = states;
	}
	public String getInitialState() {
		return initialState;
	}
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}
	public Set<String> getAlphabet() {
		return alphabet;
	}
	public void setAlphabet(Set<String> alphabet) {
		this.alphabet = alphabet;
	}
	public Set<String> getHaltStates() {
		return haltStates;
	}
	public void setHaltStates(Set<String> haltStates) {
		this.haltStates = haltStates;
	}
	public List<JsonDto> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<JsonDto> transitions) {
		this.transitions = transitions;
	}

}