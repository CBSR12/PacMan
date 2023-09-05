package application;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;


public class SceneControler implements Initializable {
	//booleans for key pressed
	private BooleanProperty wPressed = new SimpleBooleanProperty();
	private BooleanProperty aPressed = new SimpleBooleanProperty();
	private BooleanProperty sPressed = new SimpleBooleanProperty();
	private BooleanProperty dPressed = new SimpleBooleanProperty();
	private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);
	//keeps track of most recent key pressed
	private char pressed;
	//keeps track of pacman Collision
	private boolean collision;
	//speed of movement
	private int movementVariable = 0;
	//keeps track of player score
	private int score = 0;
	@FXML
	private Label forScore;
	//Array of dots
	private Circle[] eaten = new Circle[393];

	//pacman character
	@FXML
	private Arc pacman;
	//colors are the ghost shapes from Scenebuilder
	@FXML
	private Arc red;

	@FXML
	private Arc orange;

	@FXML
	private Arc pink;

	@FXML
	private Arc green;

	//gets maze from scene
	@FXML
	private Pane maze;

	@FXML
	private AnchorPane scene1;

	//ghosts objects
	private static Ghost blinky;
	private static Ghost clyde;
	private static Ghost pinky;
	private static Ghost inky;

	//These are the different Screens
	@FXML
	private Pane title;
	@FXML
	private Pane controls;
	@FXML
	private Pane overScreen;
	@FXML
	private Pane winScreen;
	
	//gets dots from Scene
	@FXML
	private Pane dots;







	//default location if reset
	@FXML
	void start(ActionEvent event) {
		pacman.setLayoutY(200);
		pacman.setLayoutX(200);
	}


	//animation timer that constantly checks for collisions
	AnimationTimer collisionTimer = new AnimationTimer() {

		@Override
		public void handle(long timestamp) {


			try {
				//pacman collisions
				checkCollision(pacman, maze);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//checks ghost collisions
			checkDotCollisions(pacman, dots);
			checkGhostCollision(blinky, maze);
			checkGhostCollision(clyde, maze);
			checkGhostCollision(pinky, maze);
			checkGhostCollision(inky, maze);

		}

	};
	//animation timer for pacman movement
	AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long timestamp) {
			//changes pacman movement based on key pressed
			if(!collision) {
				if(wPressed.get()) {
					pacman.setLayoutY(pacman.getLayoutY() - movementVariable);
					pressed = 'w';
				}

				if(sPressed.get()) {
					pacman.setLayoutY(pacman.getLayoutY() + movementVariable);
					pressed = 's';
				}

				if(aPressed.get()) {
					pacman.setLayoutX(pacman.getLayoutX() - movementVariable);
					pressed = 'a';
				}

				if(dPressed.get()) {
					pacman.setLayoutX(pacman.getLayoutX() + movementVariable);
					pressed = 'd';
				}
			}



		}
	};
	//runs ghost movement
	AnimationTimer ghostTimer = new AnimationTimer() {
		//ghosts change direction when colliding with walls
		@Override
		public void handle(long timestamp) {
			//changes blinky movement 
			String blinkyDirection = blinky.getGhostDirection();
			switch(blinkyDirection) {
				case "up" :
					blinky.getGhost().setLayoutY(red.getLayoutY() - movementVariable);
					break;
				case "down":
					blinky.getGhost().setLayoutY(red.getLayoutY() + movementVariable);
					break;
				case "left":
					blinky.getGhost().setLayoutX(red.getLayoutX() - movementVariable);
					break;
				case "right":
					blinky.getGhost().setLayoutX(red.getLayoutX() + movementVariable);
					break;
			}

			//Controls Clyde's movement
			String clydeDirection = clyde.getGhostDirection();
			switch(clydeDirection) {
				case "up" :
					clyde.getGhost().setLayoutY(clyde.getGhost().getLayoutY() - movementVariable);
					break;
				case "down":
					clyde.getGhost().setLayoutY(clyde.getGhost().getLayoutY() + movementVariable);
					break;
				case "left":
					clyde.getGhost().setLayoutX(clyde.getGhost().getLayoutX() - movementVariable);
					break;
				case "right":
					clyde.getGhost().setLayoutX(clyde.getGhost().getLayoutX() + movementVariable);
					break;
			}

			//Controls Pinky's  movement
			String pinkyDirection = pinky.getGhostDirection();
			switch(pinkyDirection) {
				case "up" :
					pinky.getGhost().setLayoutY(pinky.getGhost().getLayoutY() - movementVariable);
					break;
				case "down":
					pinky.getGhost().setLayoutY(pinky.getGhost().getLayoutY() + movementVariable);
					break;
				case "left":
					pinky.getGhost().setLayoutX(pinky.getGhost().getLayoutX() - movementVariable);
					break;
				case "right":
					pinky.getGhost().setLayoutX(pinky.getGhost().getLayoutX() + movementVariable);
					break;
			}

			//Controls Inky's movement
			String inkyDirection = inky.getGhostDirection();
			switch(inkyDirection) {
				case "up" :
					inky.getGhost().setLayoutY(inky.getGhost().getLayoutY() - movementVariable);
					break;
				case "down":
					inky.getGhost().setLayoutY(inky.getGhost().getLayoutY() + movementVariable);
					break;
				case "left":
					inky.getGhost().setLayoutX(inky.getGhost().getLayoutX() - movementVariable);
					break;
				case "right":
					inky.getGhost().setLayoutX(inky.getGhost().getLayoutX() + movementVariable);
					break;
			}


		}
	};

	//starts timers
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {


		collision = false;
		movementSetup();

		keyPressed.addListener(((observableValue, aBoolean, t1) ->{
			if(!aBoolean) {
				timer.start();
			}
			else {
				timer.stop();
			}
		}));
		ghostTimer.start();
		collisionTimer.start();

	}
	//sets boolean values based on user input and starts ghost movement
	public void movementSetup() {
		blinky = new Ghost(red);
		clyde = new Ghost(orange);
		pinky = new Ghost(pink);
		inky = new Ghost(green);

		scene1.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.W) {
				wPressed.set(true);
				pacman.setRotate(0);

				aPressed.set(false);
				sPressed.set(false);
				dPressed.set(false);
			}

			if(e.getCode() == KeyCode.A) {
				aPressed.set(true);
				pacman.setRotate(-90);

				wPressed.set(false);
				sPressed.set(false);
				dPressed.set(false);

			}

			if(e.getCode() == KeyCode.S) {
				sPressed.set(true);
				pacman.setRotate(180);

				wPressed.set(false);
				aPressed.set(false);
				dPressed.set(false);
			}

			if(e.getCode() == KeyCode.D) {
				dPressed.set(true);
				pacman.setRotate(90);

				wPressed.set(false);
				aPressed.set(false);
				sPressed.set(false);
			}

		});

	}

	//runs statement to see if pacman is colliding with walls
	public void checkCollision(Arc pacman, Pane maze) throws FileNotFoundException  {
		collision = false;
		//checks walls in maze for collision
		for(int i = 0; i<maze.getChildren().size();i++) {
			if(pacman.getBoundsInParent().intersects(maze.getChildren().get(i).getBoundsInParent())) {
				//if pacman hits a wall, pushes him the opposite direction he is moving to stop him
				collision = true;
				switch(pressed) {

					case'w':
						wPressed.set(false);
						pacman.setLayoutY(pacman.getLayoutY() + movementVariable);

						break;
					case's':
						sPressed.set(false);
						pacman.setLayoutY(pacman.getLayoutY() - movementVariable);

						break;
					case 'a':
						aPressed.set(false);
						pacman.setLayoutX(pacman.getLayoutX() + movementVariable);

						break;
					case 'd' :
						dPressed.set(false);
						pacman.setLayoutX(pacman.getLayoutX() - movementVariable);

						break;
				}

				if(collision = true) {
					break;
				}
			}

		}
		//pacman dies if he hits a ghost
		if(pacman.getBoundsInParent().intersects(blinky.getGhost().getBoundsInParent())) {
			gameOver();

		}

		if(pacman.getBoundsInParent().intersects(inky.getGhost().getBoundsInParent())) {
			gameOver();

		}

		if(pacman.getBoundsInParent().intersects(pinky.getGhost().getBoundsInParent())) {
			gameOver();			;
		}

		if(pacman.getBoundsInParent().intersects(clyde.getGhost().getBoundsInParent())) {
			gameOver();
		}


	}
	//checks if ghosts hit walls
	public void checkGhostCollision(Ghost ghost, Pane maze) {
		//loops through all the walls in the maze
		for(int i = 0; i<maze.getChildren().size();i++) {
			//checks for ghost collision with walls
			if(ghost.getGhost().getBoundsInParent().intersects(maze.getChildren().get(i).getBoundsInParent())) {

				//random number to decide new direction
				double random = Math.random()*100;
				//gets collided ghost's direction
				String ghostDirection = ghost.getGhostDirection();

				switch(ghostDirection) {
					case "up":
						ghost.getGhost().setLayoutY(ghost.getGhost().getLayoutY() + movementVariable);
						if(random<=33) {
							ghostDirection = "down";
						}
						else if(random<=66) {
							ghostDirection = "right";
						}
						else {
							ghostDirection = "left";
						}
						break;
					case "down":
						ghost.getGhost().setLayoutY(ghost.getGhost().getLayoutY() - movementVariable);
						ghostDirection = "up";
						if(random<=33) {
							ghostDirection = "up";
						}
						else if(random<=66) {
							ghostDirection = "right";					}
						else {
							ghostDirection = "left";
						}
						break;
					case "left":
						ghost.getGhost().setLayoutX(ghost.getGhost().getLayoutX() + movementVariable);
						ghostDirection = "right";
						if(random<=33) {
							ghostDirection = "up";
						}
						else if(random<=66) {
							ghostDirection = "right";
						}
						else {
							ghostDirection = "down";
						}
						break;
					case "right":
						ghost.getGhost().setLayoutX(ghost.getGhost().getLayoutX() - movementVariable);
						ghostDirection = "left";
						if(random<=33) {
							ghostDirection = "up";
						}
						else if(random<=66) {
							ghostDirection = "left";
						}
						else {
							ghostDirection = "down";
						}
						break;

				}
				ghost.setGhostDirection(ghostDirection);



			}
		}

	}
	
	//removes dot from screen when pacman eats him
	public void checkDotCollisions(Arc pacman, Pane theDots) {

		boolean dotCollision = false;
		for(int i = 0; i < dots.getChildren().size(); i++) {

			if (pacman.getBoundsInParent().intersects(dots.getChildren().get(i).getBoundsInParent())){
				eaten[score] = (Circle) dots.getChildren().get(i);
				score++;
				dots.getChildren().remove(i);
				updateScore(forScore);
				
				//player wins if they eat all dots
				if(score == 393) {
					win();
				}
				break;
			}
		}
	}
	
	//actively updates the score on screen
	public void updateScore(Label a) {
		a.setText("Score: " + ((score*50)));
		a.setTextFill(Color.WHITE);



	}

	//switches to controls screen
	@FXML
	void toControls(ActionEvent event) {
		title.toBack();
		controls.toFront();
	}
	//switches to gameScreen
	@FXML
	void toGame(ActionEvent event) {
		controls.toBack();
		movementVariable = 2;
	}
	
	//switches to game over screen
	public void gameOver() {
		movementVariable = 0;
		overScreen.toFront();
	}
	
	//Resets the maze for next game
	@FXML
	void reset(ActionEvent event) {
		pacman.setLayoutX(250);
		pacman.setLayoutY(278);

		blinky.getGhost().setLayoutX(450);
		blinky.getGhost().setLayoutY(468);

		inky.getGhost().setLayoutX(273);
		inky.getGhost().setLayoutY(468);

		pinky.getGhost().setLayoutX(41);
		pinky.getGhost().setLayoutY(466);

		clyde.getGhost().setLayoutX(233);
		clyde.getGhost().setLayoutY(468);


		for(int thruEaten = 0; thruEaten < score; thruEaten++)
			dots.getChildren().add(eaten[thruEaten]);
		score = 0;

		overScreen.toBack();
		winScreen.toBack();
		updateScore(forScore);
		movementVariable = 2;

	}
	
	//brings player to win screen
	public void win() {
		movementVariable = 0;
		winScreen.toFront();
	}
	//exits game
	@FXML
	void quit(ActionEvent event) {

		Platform.exit();

	}



}
