package atde.userinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import atde.core.*;
import atde.components.*;
import atde.gamelogic.*;
import atde.member.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardGameController {
	private final ArrayList<Boolean> handStates = new ArrayList<>();       // Trạng thái các lá bài trên tay
    private final ArrayList<Boolean> needToDefendCardsStates = new ArrayList<>(); // Trạng thái các lá bài "Cần đỡ"
    private final ArrayList<Boolean> usedCardsStates = new ArrayList<>(); // Trạng thái các lá bài "Đã sử dụng"
    private int handCount = 8;       // Tổng số lá bài trên tay
    private int visibleCards = 5;     // Số lá bài hiển thị trong mỗi khu vực
    private int needToDefendCardsCount = 8; // Tổng số lá bài "Cần đỡ"
    private int usedCardsCount = 8; // Tổng số lá bài "Đã sử dụng"
    private int startIndexHand = 0;             // Vị trí bắt đầu hiển thị lá bài trên tay (0-based)
    private int startIndexNeedToDefend = 0;           // Vị trí bắt đầu hiển thị lá bài "Cần đỡ"
    private int startIndexUsed = 0;
    private boolean graphicMode = true;
    private boolean gamePlaying = true;
    private ArrayList<String> playerName = new ArrayList<>();

    private CardList pickedCards = new CardList();
    private CardList defendCards = new CardList();
    @FXML
    private AnchorPane background;
    @FXML
    private HBox needToDefendCardsBox;
    @FXML
    private HBox usedCardsBox;
    @FXML
    private HBox handBox;
    @FXML
    private HBox speacialCardBox;
    @FXML
    private HBox opponentCard1;
    @FXML
    private HBox opponentCard2;
    @FXML
    private HBox opponentCard3;
    @FXML
    private Button prevNeedToDefendButton, prevUsedButton, nextUsedButton;
    @FXML
    private Button nextNeedToDefendButton;
    @FXML
    private Button prevButton, nextButton, endTurnButton, playButton;
    @FXML
    private Button graphicButton;
    @FXML 
    private Button newGameButton;
    @FXML 
    private Button quitGameButton;

    @FXML
    private Label remainingCardLabel;
    @FXML
    private Label cardNums1;
    @FXML
    private Label cardNums2;
    @FXML
    private Label cardNums3;
    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;
    @FXML
    private Label player3Name;
    @FXML
    private Label playRole;
    @FXML
    private VBox endGamePane;
    @FXML
    private Label endGameText;

    public List<Player> players;
    
    public ArrayList <Player> playersList;

    public Player player;
	private Member winner;

    @FXML
    public void initialize() {
    	endGamePane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	endGamePane.getStyleClass().add("vbox");
    	endGamePane.setVisible(false);
    	setGraphicButton(newGameButton);
    	setGraphicButton(quitGameButton);
    	setGraphicButton(playButton);
    	setGraphicButton(endTurnButton);
    	setGraphicButton(prevButton);
    	setGraphicButton(prevNeedToDefendButton);
    	setGraphicButton(nextNeedToDefendButton);
    	setGraphicButton(prevUsedButton);
    	setGraphicButton(nextUsedButton);
    	setGraphicButton(nextButton);
    	setGraphicLabel(player1Name);
    	setGraphicLabel(player2Name);
    	setGraphicLabel(player3Name);
    	setGraphicLabel(playRole);
    	setGraphicLabel(endGameText);
    	
    }

    public void display() {
        resize();
        update();
    	updateSpeacialCards();
        playButton.setDisable(true); // Đặt mặc định là không thể đánh bài
        endTurnButton.setOnAction(e -> endTurn());
        playButton.setOnAction(e -> play());
        updateRemainingCardLabel();
        updateOpponentCard(opponentCard1);
        updateOpponentCard(opponentCard2);
        updateOpponentCard(opponentCard3);
        updateOpponentNumsCard();
        updatePlayRole();
    }

    public void setPlayer(Player player) {
        this.player = player;
        display();
    }
    


    public void addPlayersList(ArrayList <Player> playersList) {
    	playersList = new ArrayList<>();
    	for (Player p : playersList)
    		this.playersList.add(p);
    }
    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerName = new ArrayList<>(playerNames); // Sao chép danh sách để đảm bảo tính toàn vẹn
        System.out.println("Received player names: " + playerName);
    }
    

    private void update() {
    	updateHand();
        updateNeedToDefendCards();
        updateUsedCards();
    }
    private void endTurn() {
        System.out.println("Kết thúc lượt");
        player.setEndTurn(true);
        player.getDeck().endTurn();
    }
    private void play() {
        if (player.getRole().equals("attack")) {
            player.attack(pickedCards);
        }
        else {
            player.defend(defendCards.getFirstCard(), pickedCards.getFirstCard());
        }
    }
    private void resize() {
        setHandCount(player.getHand().size());
        setNeedToDefendCardsCount(player.getNeedToDefend().size());
        setUsedCardsCount(player.getCardsUsed().size());
        handStates.clear();
        needToDefendCardsStates.clear();
        usedCardsStates.clear();
        for (int i = 0; i < handCount; i++) {
            handStates.add(false); // Trạng thái mặc định
        }
        for (int i = 0; i < needToDefendCardsCount; i++) {
            needToDefendCardsStates.add(false); // Trạng thái mặc định cho "Cần đỡ"
        }
        for (int i = 0; i < usedCardsCount; i++) {
            usedCardsStates.add(false); // Trạng thái mặc định cho "Đã sử dụng"
        }
    }

    private boolean checkMove() {
        pickedCards.removeAll();
        for (int i = 0; i < handCount; i++) {
            if (handStates.get(i)) {
                pickedCards.add(player.getHand().getCard(i));
            }
        }

        if (pickedCards.size() == 0)
            return false;
        

        if (player.getRole().equals("attack")) {
            if (player.isFirstMove()) {
                return player.getDeck().checkAttackFirstMove(pickedCards);
            }
            else  {
                return player.getDeck().checkAttack(pickedCards);
            }
        }
        else {
            defendCards.removeAll();
            for (int i = 0; i < needToDefendCardsCount; i++) {
                if (needToDefendCardsStates.get(i)) {
                    defendCards.add(player.getNeedToDefend().getCard(i));
                }
            }

            return player.getDeck().checkDefend(defendCards, pickedCards);
        }
    }


    // Hàm cập nhật hiển thị các lá trên tay
    private void updateHand() {
        handBox.getChildren().clear();
        handBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        if (graphicMode) {
        	handBox.getStyleClass().remove("hand-box-text");
    		handBox.getStyleClass().add("hand-box");
    		System.out.println("gay");
            } else {
            handBox.getStyleClass().add("hand-box");
            handBox.getStyleClass().add("hand-box-text");
            }
        
        for (int i = 0; i < 8 && i < handCount; i++) {
            int cardIndex = (startIndexHand + i) % handCount; // Lấy chỉ số lá bài (tuần hoàn)
            Image imageOn = new Image(player.getHand().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Bật"
            Image imageOff = new Image(player.getHand().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Tắt"

            // Tạo ImageView cho hai trạng thái
            ImageView imageViewOn = new ImageView(imageOn);
            ImageView imageViewOff = new ImageView(imageOff);
            ToggleButton cardButton = new ToggleButton();
            cardButton.setTranslateX(i * 0);
            cardButton.setSelected(handStates.get(cardIndex));
            
            if (graphicMode) {
            cardButton.setGraphic(imageViewOff);
            } else {
            cardButton.setText(player.getHand().getCard(cardIndex).CardToString());
            handBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            handBox.getStyleClass().add("toggle-button-text");
            }
            
            int index = cardIndex; // Lưu chỉ số thực tế
            if(handStates.get(index)) {
                cardButton.setTranslateY(-20);
            } else {
                cardButton.setTranslateY(0);
            }
            cardButton.setOnAction(e -> {
                handStates.set(index, cardButton.isSelected());
                playButton.setDisable(!checkMove());
                System.out.println(player.getNeedToDefend().CardListToString());
                System.out.println(handStates);
                if(handStates.get(index)) {
                    cardButton.setTranslateY(-20);
                } else {
                    cardButton.setTranslateY(0);
                }
            });
            handBox.getChildren().add(cardButton);
        }        
    }

    // Hàm cập nhật hiển thị các lá bài "Cần đỡ"
    private void updateNeedToDefendCards() {
    	needToDefendCardsBox.getChildren().clear();
    	
    	needToDefendCardsBox.getChildren().clear();
    	needToDefendCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        if (graphicMode) {
        	needToDefendCardsBox.getStyleClass().remove("hand-box-text");
        	needToDefendCardsBox.getStyleClass().add("hand-box");
    		System.out.println("gay");
            } else {
            needToDefendCardsBox.getStyleClass().add("hand-box");
            needToDefendCardsBox.getStyleClass().add("hand-box-text");
            }
        
        for (int i = 0; i < visibleCards && i < needToDefendCardsCount; i++) {
            int cardIndex = (startIndexNeedToDefend + i) % needToDefendCardsCount; // Lấy chỉ số lá bài (tuần hoàn)
            Image imageOn = new Image(player.getNeedToDefend().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Bật"
            Image imageOff = new Image(player.getNeedToDefend().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Tắt"

            // Tạo ImageView cho hai trạng thái
            ImageView imageViewOn = new ImageView(imageOn);
            ImageView imageViewOff = new ImageView(imageOff);
            ToggleButton cardButton = new ToggleButton();
            cardButton.setTranslateX(i * 0);
            cardButton.setSelected(needToDefendCardsStates.get(cardIndex));
            if (graphicMode) {
				cardButton.setGraphic(imageViewOff);
			} else {
				cardButton.setText(player.getHand().getCard(cardIndex).CardToString());
				needToDefendCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
				needToDefendCardsBox.getStyleClass().add("toggle-button-text");
			}
			int index = cardIndex; // Lưu chỉ số thực tế
            if(needToDefendCardsStates.get(index)) {
                cardButton.setTranslateY(-20);
            } else {
                cardButton.setTranslateY(0);
            }
            cardButton.setOnAction(e -> {
                needToDefendCardsStates.set(index, cardButton.isSelected());
                playButton.setDisable(!checkMove());
                System.out.println(needToDefendCardsStates);
                if(needToDefendCardsStates.get(index)) {
                    cardButton.setTranslateY(-20);
                } else {
                    cardButton.setTranslateY(0);
                }
            });
            needToDefendCardsBox.getChildren().add(cardButton);
        }

        needToDefendCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        needToDefendCardsBox.getStyleClass().add("speacial-card-box");
    }

    // Hàm cập nhật hiển thị các lá bài "Đã sử dụng" (Không có tính năng chọn)
    private void updateUsedCards() {
    	usedCardsBox.getChildren().clear();
   	 
    	usedCardsBox.getChildren().clear();
    	usedCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
   	 	if (graphicMode) {
   	 		usedCardsBox.getStyleClass().remove("hand-box-text");
   	 		usedCardsBox.getStyleClass().add("hand-box");
   	 		System.out.println("gay");
   	 		} else {
   	 			usedCardsBox.getStyleClass().add("hand-box");
   	 			usedCardsBox.getStyleClass().add("hand-box-text");
   	 		}
   	    for (int i = 0; i < visibleCards && i < usedCardsCount; i++) {
   	        int cardIndex = (startIndexUsed + i) % usedCardsCount; // Lấy chỉ số lá bài (tuần hoàn)
   	        Image imageOn = new Image(player.getCardsUsed().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Bật"
   	        Image imageOff = new Image(player.getCardsUsed().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Tắt"

   	        // Tạo ImageView cho hai trạng thái
   	        ImageView imageViewOn = new ImageView(imageOn);
   	        ImageView imageViewOff = new ImageView(imageOff);
   	        ToggleButton cardButton = new ToggleButton();
   	        cardButton.setTranslateX(i * 0);
   	        cardButton.setSelected(usedCardsStates.get(cardIndex));
   	        if (graphicMode) {
				cardButton.setGraphic(imageViewOff);
			} else {
				cardButton.setText(player.getHand().getCard(cardIndex).CardToString());
				usedCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
				usedCardsBox.getStyleClass().add("toggle-button-text");
			}
			usedCardsBox.getChildren().add(cardButton);
   	    }

   	    usedCardsBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
   	    usedCardsBox.getStyleClass().add("speacial-card-box");
   }
    private void updateSpeacialCards() {
        speacialCardBox.getChildren().clear();
        if (player.getDeck().getCardSet().size() == 0)
        	return;
        Card specialCard = player.getDeck().getCardSet().getCard(0);
        Image specialCardImage = new Image(specialCard.CardToLink());
        ImageView imageViewFront = new ImageView(specialCardImage);
        ToggleButton cardButton1 = new ToggleButton();
        cardButton1.setGraphic(imageViewFront);
        speacialCardBox.getChildren().add(cardButton1);
        if (player.getDeck().getCardSet().size() == 1)
        	return;
        
        Image backCard = new Image("file:src/image/card/b.gif");
        ImageView imageViewBack = new ImageView(backCard);
        ToggleButton cardButton2 = new ToggleButton();
        cardButton2.setGraphic(imageViewBack);
        speacialCardBox.getChildren().add(cardButton2);
        speacialCardBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        speacialCardBox.getStyleClass().add("speacial-card-box");
    }
    // Các hàm dịch bài
    @FXML
    private void shiftHandLeft(ActionEvent e) {
        startIndexHand = (startIndexHand - 1 + handCount) % handCount;
        updateHand();
        // endGamePane();
    }
    @FXML
    private void shiftHandRight(ActionEvent e) {
        startIndexHand = (startIndexHand + 1) % handCount;
        updateHand();
    }
    @FXML
    private void shiftNeedToDefendCardsLeft() {
        startIndexNeedToDefend = (startIndexNeedToDefend - 1 + needToDefendCardsCount) % needToDefendCardsCount;
        updateNeedToDefendCards();
    }
    @FXML
    private void shiftNeedToDefendCardsRight() {
        startIndexNeedToDefend = (startIndexNeedToDefend + 1) % needToDefendCardsCount;
        updateNeedToDefendCards();
    }

    public void setVisibleCards(int visibleCards) {
        this.visibleCards = visibleCards;
    }

    public void setHandCount(int handCount) {
        this.handCount = handCount;
    }

    public void setNeedToDefendCardsCount(int needToDefendCardsCount) {
        this.needToDefendCardsCount = needToDefendCardsCount;
    }

    public void setUsedCardsCount(int usedCardsCount) {
        this.usedCardsCount = usedCardsCount;
    }
    @FXML
    private void shiftUsedCardsLeft() {
        startIndexUsed = (startIndexUsed - 1 + usedCardsCount) % usedCardsCount;
        updateUsedCards();
    }
    @FXML
    private void shiftUsedCardsRight() {
        startIndexUsed = (startIndexUsed + 1) % usedCardsCount;
        updateUsedCards();
    }
    //!!!!!!
    @FXML
    private void switchGraphicMode() {
    	graphicMode = !graphicMode;
    	update();
    }
    //!!!!!

    private void updateRemainingCardLabel() {
    	remainingCardLabel.setText("");
    	if (player.getDeck().getCardSet().size() <= 1)
    		return;
    	remainingCardLabel.setText("" + player.getDeck().getCardSet().size());
    	remainingCardLabel.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	remainingCardLabel.getStyleClass().add("text-bordered");
    }
    private void updateOpponentCard(HBox hbox) {
    	hbox.getChildren().clear();
    	Image backCard = new Image("file:src/image/card/b.gif");
        ImageView imageViewBack = new ImageView(backCard);
        ToggleButton cardButton = new ToggleButton();
        cardButton.setGraphic(imageViewBack);
        hbox.getChildren().add(cardButton);
        
    }
    // update số lá bài đối thủ
    private void updateOpponentNumsCard() {
    	int index = player.getId();
    	int count = player.getDeck().getMembers().size();
    	
        cardNums1.setText("0");
        player1Name.setText("");
        if (count >= 4) {
        	cardNums1.setText("" + player.getDeck().getMember((index + 3) % count).getHand().size());
        	player1Name.setText(player.getDeck().getMember((index + 3) % count).getName());
        }
        cardNums1.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	cardNums1.getStyleClass().add("text-bordered");
    	player1Name.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	player1Name.getStyleClass().add("text-bordered");

    	
        cardNums2.setText("0");
        player2Name.setText("");
        if (count >= 3) {
        	cardNums2.setText("" + player.getDeck().getMember((index + 2) % count).getHand().size());
        	player2Name.setText(player.getDeck().getMember((index + 2) % count).getName());
        }
        cardNums2.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	cardNums2.getStyleClass().add("text-bordered");
    	player2Name.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	player2Name.getStyleClass().add("text-bordered");

    	
        cardNums3.setText("0");
        player3Name.setText("");
        if (count >= 2) {        	
        	cardNums3.setText("" + player.getDeck().getMember((index + 1) % count).getHand().size());
        	player3Name.setText(player.getDeck().getMember((index + 1) % count).getName());
        }
        cardNums3.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	cardNums3.getStyleClass().add("text-bordered");
    	player3Name.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	player3Name.getStyleClass().add("text-bordered");
    }
    private void updatePlayRole() {
    	playRole.setText("this turn you are the " + player.getRole() + "er");
    	playRole.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	playRole.getStyleClass().add("text-bordered");
    }
    
    
    public void endGamePane() {
    	endGamePane.setVisible(true);
    	endGameText.setText(winner.getName() + " la nguoi chien thang");
    	
    }
    @FXML
    private void newGame(ActionEvent e) {
    	Deck deck = player.getDeck();
    	deck.newGame();
    	endGamePane.setVisible(false);
    }
    @FXML
    private void quitGame(ActionEvent e) {
    	try {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Menu.fxml"));
		Parent root = loader.load();
		
		root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		root.getStyleClass().add("root");
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Card Game GUI");
		stage.show();
    	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    	
    }
    
    public boolean isGraphicMode() {
		return graphicMode;
	}

	public void setGraphicMode(boolean graphicMode) {
		this.graphicMode = graphicMode;
	}
	private void setGraphicButton(Button button) {
    	button.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	button.getStyleClass().add("play-again-button");
    }
	private void setGraphicLabel(Label label) {
    	label.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	label.getStyleClass().add("label-style");
    	System.out.print(1);
    }
	public void setWinner(Member member) {
		winner = member;
	}
}
