package bigtwo.userinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bigtwo.core.*;
import bigtwo.gamelogic.Deck;
import bigtwo.member.Member;
import bigtwo.member.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CardGameController {
	private final ArrayList<Boolean> handStates = new ArrayList<>();       // Trạng thái các lá bài trên tay
    private int handCount = 8;       // Tổng số lá bài trên tay
    private int visibleCards = 13;     // Số lá bài hiển thị trong mỗi khu vực
    private int lastCardListCount = 8; // Tổng số lá bài "Đã sử dụng"

    private CardList pickedCards = new CardList();
    
    @FXML
    private HBox lastCardListBox;
    @FXML
    private HBox handBox;

    @FXML
    private HBox opponentCard1;
    @FXML
    private HBox opponentCard2;
    @FXML
    private HBox opponentCard3;
    @FXML
    private Button endTurnButton, playButton;
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
    private Label endGameText;
    @FXML
    private VBox endGamePane;
    @FXML 
    private Button newGameButton;
    @FXML 
    private Button quitGameButton;

    public List<Player> players;
    
    public ArrayList <Player> playersList;

    public Player player;
    public Member winner;

    @FXML
    public void initialize() {
    	endGamePane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    	endGamePane.getStyleClass().add("vbox");
    	endGamePane.setVisible(false);
    	setGraphicButton(newGameButton);
    	setGraphicButton(quitGameButton);
    	setGraphicButton(playButton);
    	setGraphicButton(endTurnButton);
    	setGraphicLabel(player1Name);
    	setGraphicLabel(player2Name);
    	setGraphicLabel(player3Name);
    	setGraphicLabel(endGameText);
    }

    public void display() {
        resize();
        update();

        playButton.setDisable(true); // Đặt mặc định là không thể đánh bài
        endTurnButton.setOnAction(e -> endTurn());
        playButton.setOnAction(e -> play());

        updateHand();
        updateLastCardList();
        updateOpponentCard(opponentCard1);
        updateOpponentCard(opponentCard2);
        updateOpponentCard(opponentCard3);
        updateOpponentNumsCard();
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
    
    private void update() {
    	updateHand();
        updateLastCardList();
    }
    private void endTurn() {
        System.out.println("Kết thúc lượt");
        player.setEndTurn(true);
        player.getDeck().endTurn();
    }
    private void play() {
    	player.move(pickedCards);
    }

    private void resize() {
        setHandCount(player.getHand().size());
        setLastCardListCount(player.getLastCardList().size());
        handStates.clear();

        for (int i = 0; i < handCount; i++) {
            handStates.add(false); // Trạng thái mặc định
        }
    }

    private boolean checkMove() {
        pickedCards.removeAll();
        for (int i = 0; i < handCount; i++) {
            if (handStates.get(i)) {
                pickedCards.add(player.getHand().getCard(i));
            }
        }

        if (pickedCards.size() == 0) {
            return false;
        }

        return player.getDeck().checkMove(pickedCards);
    }


    // Hàm cập nhật hiển thị các lá trên tay
    private void updateHand() {
        handBox.getChildren().clear();

        for (int i = 0; i < visibleCards && i < handCount; i++) {
            int cardIndex = i; // Lấy chỉ số lá bài (tuần hoàn)
            Image imageOn = new Image(player.getHand().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Bật"
            Image imageOff = new Image(player.getHand().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Tắt"

            // Tạo ImageView cho hai trạng thái
            ImageView imageViewOn = new ImageView(imageOn);
            ImageView imageViewOff = new ImageView(imageOff);
            ToggleButton cardButton = new ToggleButton();
            cardButton.setTranslateX(i * 0);
            cardButton.setSelected(handStates.get(cardIndex));
            cardButton.setGraphic(imageViewOff);

            int index = cardIndex; // Lưu chỉ số thực tế
            if(handStates.get(index)) {
                cardButton.setTranslateY(-20);
            } else {
                cardButton.setTranslateY(0);
            }
            cardButton.setOnAction(e -> {
                handStates.set(index, cardButton.isSelected());
                playButton.setDisable(!checkMove());
                if(handStates.get(index)) {
                    cardButton.setTranslateY(-20);
                } else {
                    cardButton.setTranslateY(0);
                }
            });
            handBox.getChildren().add(cardButton);
        }

        handBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }


    // Hàm cập nhật hiển thị các lá bài "Đã sử dụng" (Không có tính năng chọn)
    private void updateLastCardList() {
   	 	lastCardListBox.getChildren().clear();
   	    for (int i = 0; i < visibleCards && i < lastCardListCount; i++) {
   	        int cardIndex = i;
   	        Image imageOn = new Image(player.getLastCardList().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Bật"
   	        Image imageOff = new Image(player.getLastCardList().getCard(cardIndex).CardToLink()); // Đường dẫn ảnh "Tắt"

   	        // Tạo ImageView cho hai trạng thái
   	        ImageView imageViewOn = new ImageView(imageOn);
   	        ImageView imageViewOff = new ImageView(imageOff);
   	        ToggleButton cardButton = new ToggleButton();
   	        cardButton.setTranslateX(i * 0);
   	        cardButton.setGraphic(imageViewOff);

   	        lastCardListBox.getChildren().add(cardButton);
   	    }
   	 lastCardListBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
   	 lastCardListBox.getStyleClass().add("speacial-card-box");
   }

    public void setVisibleCards(int visibleCards) {
        this.visibleCards = visibleCards;
    }

    public void setHandCount(int handCount) {
        this.handCount = handCount;
    }

    public void setLastCardListCount(int lastCardListCount) {
        this.lastCardListCount = lastCardListCount;
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
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/Menu.fxml"));
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
