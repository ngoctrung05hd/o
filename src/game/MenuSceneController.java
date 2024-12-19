package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuSceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int playerNum;
	private int totalMember;
	private boolean graphicMode;
	public ArrayList<String> playerNames;
	@FXML
	private ComboBox<String> numPlayersBox;
	@FXML
	private ComboBox<String> numBotsBox;

	@FXML
	private ComboBox<String> gameModeBox;
	@FXML
	private TextField nameBox;
	@FXML
	private Button startGameButton;
	@FXML
	private Button quitGameButton;
	@FXML
	private Button playGameButton;
	@FXML
	private final List<TextField> nameFields = new ArrayList<>();
	@FXML
	private VBox nameBoxContainer;

	@FXML
	public void initialize() {
		// Thêm danh sách giá trị vào ComboBox
		numPlayersBox.getItems().addAll("2 Players", "3 Players", "4 Players");
		gameModeBox.getItems().addAll("Tan", "TienLenMienNam");
		
		setGraphicButton(startGameButton);
		setGraphicButton(quitGameButton);
		setGraphicButton(playGameButton);
		

		// Ẩn các thành phần không cần thiết
		gameModeBox.setVisible(false);
		numPlayersBox.setVisible(false);
		numBotsBox.setVisible(false);
		startGameButton.setVisible(false);

		// Xử lý sự kiện chọn trên ComboBox
		numPlayersBox.setOnAction(event -> handleNumsofPlayerSelection());
		gameModeBox.setOnAction(event -> handleGameModeSelection());
		// numBotsBox.setOnAction(event -> handleNumofBotSelection());
	}

	

	@FXML
	public void showOptions(ActionEvent event) {
		// Hiển thị VBox khi nhấn nút Play
		gameModeBox.setVisible(true);
		// initialize();
	}

	@FXML
	public void handleGameModeSelection() {
		String selectedMode = gameModeBox.getValue();
		// if ("Mode 1".equals(selectedMode)) {
		// Nếu chọn Mode 1, hiển thị ngay nút Start Game
		numPlayersBox.setVisible(true);
		nameBoxContainer.setVisible(false);
		startGameButton.setVisible(false);
		numBotsBox.setVisible(false);

		/*
		 * } else if ("Mode 2".equals(selectedMode)) { // Nếu chọn Mode 2, hiển thị
		 * numPlayersBox numPlayersBox.setVisible(false); createNameFields(1);
		 * nameBoxContainer.setVisible(true); startGameButton.setVisible(true);
		 */

	}

	@FXML
	public void handleNumsofPlayerSelection() {
		// Kiểm tra nếu chọn đủ số người chơi thì hiển thị nút Start Game
		if (numPlayersBox.getValue() != null) {
			String selectedPlayerNum = numPlayersBox.getValue();
			totalMember = Integer.parseInt(selectedPlayerNum.split(" ")[0]); // Chỉ lấy số
			startGameButton.setVisible(false);
			numBotsBox.setVisible(true);
			numBotsBox.getItems().clear();
			for (int i = 0; i < totalMember; i++) {
				numBotsBox.getItems().add(i + " Bots");
			}
		}
	}
	@FXML
	private void handleNumofBotSelection() {
		
		String selectedBotNum = numBotsBox.getValue();
		if (selectedBotNum != null) {
			playerNum = totalMember - Integer.parseInt(selectedBotNum.split(" ")[0]); // Chỉ lấy số
		}
		// Tạo động các TextField cho từng người chơi
		createNameFields(playerNum);
		nameBoxContainer.setVisible(true);
		startGameButton.setVisible(true);
	}

	private void createNameFields(int numPlayers) {
		// Xóa các TextField cũ trong nameBoxContainer
		nameBoxContainer.getChildren().clear();
		nameFields.clear();

		// Tạo mới các TextField dựa trên số người chơi
		for (int i = 1; i <= numPlayers; i++) {
			TextField nameField = new TextField();
			nameField.setPromptText("Player " + i + " Name");
			nameBoxContainer.getChildren().add(nameField);
			nameFields.add(nameField);
		}
	}

	@FXML
	public void handleStartGameButton(ActionEvent event) throws IOException {
		// Xóa danh sách cũ và tránh trùng lặp
		playerNames = new ArrayList<>();
		String selectedMode = gameModeBox.getValue();

		// Lấy danh sách tên người chơi
		for (TextField nameField : nameFields) {
			String playerName = nameField.getText().trim();
			if (!playerName.isEmpty()) { // Kiểm tra tên không rỗng
				playerNames.add(playerName);
			} else {
				playerNames.add("Player " + (nameFields.indexOf(nameField) + 1)); // Tên mặc định
			}
		}

		
		if ("Tan".equals(selectedMode)) {
			startGame("/atde/userinterface/CardGameLayout.fxml", event);
		} else if ("TienLenMienNam".equals(selectedMode)) {
			startGame("/bigtwo/userinterface/CardGameLayout.fxml", event);
			System.out.println("Player Names: " + playerNames);
		}
	}

	public void exit(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	
	public void startGame(String CardGameLink, ActionEvent event) {
		try {	
			System.out.println("Player Names: " + playerNames);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(CardGameLink));
			Parent root = loader.load();

			
			root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			root.getStyleClass().add("root");
			  
			if ("Tan".equals(gameModeBox.getValue())) {
				atde.gamelogic.GamePlay gamePlay = new atde.gamelogic.GamePlay();
		        gamePlay.main(playerNames, totalMember - playerNum);
				
				atde.userinterface.CardGameController controller;
				atde.gamelogic.Deck deck = gamePlay.getDeck();
				
				controller = loader.getController();
				deck.setController(controller);
				controller.addPlayersList(deck.getPlayersList());
				controller.setPlayer(deck.getPlayer(deck.getStartMemberId()));
			} else if ("TienLenMienNam".equals(gameModeBox.getValue())) {
				bigtwo.gamelogic.GamePlay gamePlay = new bigtwo.gamelogic.GamePlay();
		        gamePlay.main(playerNames, totalMember - playerNum);
				
				bigtwo.userinterface.CardGameController controller;
				bigtwo.gamelogic.Deck deck = gamePlay.getDeck();
				
				controller = loader.getController();
				deck.setController(controller);
				controller.addPlayersList(deck.getPlayersList());
				controller.setPlayer(deck.getPlayer(deck.getStartMemberId()));
			}
			// Tạo scene
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("Game Danh Bai Vippro");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setGraphicButton(Button button) {
    	button.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	button.getStyleClass().add("play-again-button");
    }

}
