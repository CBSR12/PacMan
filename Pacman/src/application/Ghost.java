package application;

import javafx.scene.shape.Arc;

public class Ghost {
	//ghost shape in javaFX
	private Arc ghost;
	//states Direction the Ghost is moving
	private String ghostDirection;
	
	//constructor, Default direction is up
	public Ghost(Arc ghost) {
		this.ghost = ghost;
		ghostDirection = "up";
	}
	//getter for ghost
	public Arc  getGhost() {
		return ghost;
	}
	//getter for ghost direction
	public String getGhostDirection(){
		
		return ghostDirection; 
	}
	//setter for ghost direction
	public void setGhostDirection(String ghostDirection) {
		this.ghostDirection = ghostDirection;
	}

}
