package turing.model.dto;

public class TransitionDto {
    String state;
    Character read;
    Character write;
    String move;
	Character target;
    String next;
    String comment;
    
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Character getRead() {
		return read;
	}
	public void setRead(Character read) {
		this.read = read;
	}
	public Character getWrite() {
		return write;
	}
	public void setWrite(Character write) {
		this.write = write;
	}
	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}
	public Character getTarget() {
		return target;
	}
	public void setTarget(Character target) {
		this.target = target;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}


    
}
