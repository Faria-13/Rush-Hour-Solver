package rushhour.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourObserver;
import rushhour.model.RushHourSolver;
import rushhour.model.Vehicle;

public class RushHourGUI extends Application implements RushHourObserver{

    static String greyPath = "file:data/gray_photo.png";
    static String redPath = "file:data/red_square.png";
    static String rightArrow = "file:data/rightArrow.jpeg";
    static String leftArrow = "file:data/leftArrow.png";
    static String downArrow = "file:data/downArrow.jpeg";
    static String upArrow = "file:data/upArrow.png";

    static Image greyImage = new Image(greyPath);
    static Image redVehicle = new Image(redPath);
    static Image rightArrowImage = new Image(rightArrow);
    

    private RushHour rh;
    private GridPane grid;
    private Label displayLabel;
    private String filename;
  
    private Map<Character, Image> map;
    private Label moveLabel;

    /**
     * 
     * @param path
     * @return a Image made out of that path
     */    /**
     * 
     * @param path
     * @return a Image made out of that path
     */
    public Image makeImage(String path){
        Image image = new Image(path);
        return image;
    }

    /**
    * The start method makes the GUI and buttons in the first place, sets some button action, also registers the observer
    *
    */
    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.print("Enter a filename: ");
        Scanner scanner = new Scanner(System.in);
        filename = scanner.nextLine();
        rh = new RushHour(filename);

        rh.registerObserver(this);
        rh.printBoard();
        primaryStage.setTitle("Rush Hour");
        map = new HashMap<>();
        map.put('R',redVehicle);
        map.put('O',makeImage("file:data/orange_square.png"));
        map.put('A',makeImage("file:data/teal_square.png"));
        map.put('B', makeImage("file:data/blue_square.png"));
        map.put('D', makeImage("file:data/gold_square.jpeg"));
        map.put('P', makeImage("file:data/hotPink_square.jpeg"));
        map.put('Q', makeImage("file:data/mustard_square.jpeg"));
        map.put('C', makeImage("file:data/yellow_square.png"));
        map.put('E', makeImage("file:data/purple_square.png"));
        map.put('F', makeImage("file:data/pink_square.jpeg"));
        map.put('G', makeImage("file:data/green_square.png"));
        map.put('H', makeImage("file:data/caramel_square.png"));
        map.put('I', makeImage("file:data/apricot_square.png"));

        BorderPane bp = new BorderPane();
        grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Label label = makeSquare("", greyImage);
                grid.add(label, i, j);
            }
        }
        populate();
        
        HBox hb = new HBox();
        moveLabel = makeSquare("Moves = 0");
        displayLabel = makeSquare("New Game");
        
        
        Button hintButton = makeButton("Hint");
        Button reset = makeButton("Reset");
        Button solve = makeButton("Solve");

        hintButton.setOnAction((ActionEvent event) -> vanish());
        reset.setOnAction((ActionEvent event) -> {try {reset();} catch (IOException e) {}});
        solve.setOnAction((ActionEvent event)-> {try {solve();} catch (IOException e) {}});

        hb.getChildren().addAll(moveLabel, displayLabel,hintButton, reset, solve);
        
        bp.setBottom(hb);
        bp.setCenter(grid);
        scanner.close();
        Scene scene = new Scene(bp);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method makes a new RushHour object and registers observer and rewrites the grid again
     * @throws IOException
     */

    /**
     * This method makes a new RushHour object and registers observer and rewrites the grid again
     * @throws IOException
     */

    public void reset() throws IOException{
        rh = new RushHour(filename);
        rh.registerObserver(this);
        update();
    }
    /**
     * the method to call the winning move List and play through each of them
     * @throws IOException
     */
    public void solve() throws IOException{
        RushHourSolver rhs = RushHourSolver.solve(rh);
        if (rhs != null){
        ArrayList<Move> moves = rhs.getMovesMade();
        new Thread(() -> {
            // for each move
            for(Move move: moves){
                Platform.runLater(() -> {
                    try {rh.moveVehicle(move);} catch (RushHourException e) {}
                });
            }
                try {Thread.sleep(250);} catch (InterruptedException e) {}
                // sleep small amount time (~250ms)
        }).start();
        update();
        }
        else{
            displayLabel.setText("No solution found");
        }

        if (rh.isGameOver()){
            System.out.println("Game WON. Kudos");
        }
    }
    /**
     * this was named vanish because it removes the possible moves as they are shown on the hint button
     */    /**
     * this was named vanish because it removes the possible moves as they are shown on the hint button
     */
    public void vanish(){
        //displayLabel.clear();
        Move move = rh.getPossibleMove().get(0);
        String str = move.getDirection().toString().toLowerCase();
        char sym = move.getSymbol();
        displayLabel.setText("Try "+ sym + " "+ str);
        displayLabel.setText("Try "+ sym + " "+ str);
        rh.getPossibleMove().remove(move);
    }
    /**
     * this method rewrites the grid with empty labels again and populates the grid with updated positions
     * also updates move count
     */    /**
     * this method rewrites the grid with empty labels again and populates the grid with updated positions
     * also updates move count
     */
    public void update(){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Label label = makeSquare("", greyImage);
                grid.add(label, i, j);
            }
        }
        populate();
        moveLabel.setText("Moves = "+rh.getMoveCount());
        
    }

    /**
     * this method goes through the set of vehicles and puts them on the board, makes button and event handlers for that as long as the game is 
     * not won
     */
    /**
     * this method goes through the set of vehicles and puts them on the board, makes button and event handlers for that as long as the game is 
     * not won
     */
    
    public void populate(){
            for(Vehicle vehicle: rh.getVehicles()){
            if(vehicle.isVert() == false){
                Button buttonR = makeButton("",rightArrowImage,map.get(vehicle.getChar()));
                Button buttonL = makeButton("",makeImage(leftArrow),map.get(vehicle.getChar()));
                Move moveR = new Move(vehicle.getChar(), Direction.RIGHT);
                Move moveL = new Move(vehicle.getChar(), Direction.LEFT);
                rh.getVehicleInfo().put(vehicle.getChar(), vehicle);
                int diff = vehicle.getFront().getCol() - vehicle.getBack().getCol();
                grid.add( buttonR,vehicle.getFront().getCol(),vehicle.getFront().getRow());
                grid.add( buttonL,vehicle.getBack().getCol(),vehicle.getBack().getRow());
                if(diff>1){
                    for(int i = 1; i<diff;i++){
                        Button buttonMm = makeButton("", null,map.get(vehicle.getChar()));
                        grid.add( buttonMm,vehicle.getFront().getCol()-1,vehicle.getFront().getRow());
                    }
                }
                
                if (!rh.isGameOver()){
                buttonR.setOnAction((ActionEvent event)-> {try {rh.moveVehicle(moveR);} catch (RushHourException e) {displayLabel.setText(e.getMessage());}});
                buttonL.setOnAction((ActionEvent event)-> {try {rh.moveVehicle(moveL);} catch (RushHourException e) {displayLabel.setText(e.getMessage());}});
                }
                else{
                    displayLabel.setText("You won");
                }
            }else{
                Button buttonU = makeButton("",makeImage(upArrow),map.get(vehicle.getChar()));
                
                Button buttonD = makeButton("",makeImage(downArrow),map.get(vehicle.getChar()));
                rh.getVehicleInfo().put(vehicle.getChar(), vehicle);
                int diff = vehicle.getFront().getRow() - vehicle.getBack().getRow();
                grid.add( buttonD,vehicle.getFront().getCol(),vehicle.getFront().getRow());
                grid.add( buttonU,vehicle.getBack().getCol(),vehicle.getBack().getRow());
                if(diff>1){
                    for(int i = 1; i<diff;i++){
                        Button buttomM = makeButton("", null,map.get(vehicle.getChar()));
                        grid.add( buttomM,vehicle.getFront().getCol(),vehicle.getFront().getRow()-1);
                        
                    }
                }  
                Move moveU = new Move(vehicle.getChar(), Direction.UP);
                Move moveD = new Move(vehicle.getChar(), Direction.DOWN);

                
                if (!rh.isGameOver()){
                buttonU.setOnAction((ActionEvent event)-> {try {rh.moveVehicle(moveU);displayLabel.setText("Good move");displayLabel.setText("Good move");} catch (RushHourException e) {displayLabel.setText(e.getMessage());}});
                buttonD.setOnAction((ActionEvent event)-> {try {rh.moveVehicle(moveD);displayLabel.setText("Good move");} catch (RushHourException e) {displayLabel.setText(e.getMessage());}});
                }
                else{
                    displayLabel.setText("You won");
                }
            }
        }
    }
        
    /**
     * factory method for basic button
     * @param text
     * @return
     */
    public Button makeButton(String text){
        Button button = new Button(text);
        button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        button.setContentDisplay(ContentDisplay.CENTER);
        return button;

    }
    /**
     * factory method to layer arrows on a button
     * @param text
     * @param foreground
     * @param background
     * @return
     */
    public Button makeButton(String text, Image foreground, Image background){
        
        ImageView view = new ImageView(foreground);
        view.setFitHeight(30);
        view.setFitWidth(30);

        Button button = new Button(text);
        
        button.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        button.setGraphic(view);
        button.setPadding(Insets.EMPTY);
       
        button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        button.setContentDisplay(ContentDisplay.CENTER);
        
        
        return button;
        
    }
    /**
     * observer's method to change things
     */    /**
     * observer's method to change things
     */
    @Override
    public void vehicleMoved(Vehicle vehicle) {
        update();
    }
    /**
     * factory method for making a label
     * @param text
     * @param image
     * @return
     */    /**
     * factory method for making a label
     * @param text
     * @param image
     * @return
     */
    public Label makeSquare(String text, Image image) {
        Label label = new Label(text);
        label.setPadding(new Insets(25));
        label.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));   
                
        label.setBorder(new Border(
                new BorderStroke(Color.BLACK,
            BorderStrokeStyle.SOLID,
            new CornerRadii(5),
            BorderStroke.THIN)));
        return label;
    }
    //basic label//basic label
    public Label makeSquare(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(25));
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}