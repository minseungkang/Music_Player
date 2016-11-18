package GUI;

import Music.MusicListManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.swing.*;

class Tab extends JFXPanel {
    /* whole Buttons in Music List Tab */
    private final Button allMusic = new Button("전체 음악");
    private final Button favoriteMusic = new Button("즐겨찾기");
    private final Button recentMusic = new Button("최근 재생한 곡");


    private GridPane buttonPanel;
    private Group root;
    private MusicList musicList;
    private PlayerTab playerTab;

    public static int listNum = 0;

    public Tab() {
        this.setVisible(true);
        Platform.runLater(() -> {
            initFX(this);
        });

    }

    private void initFX(Tab tab) {
        root = new Group();
        Scene scene = createScene();
        tab.setScene(scene);
    }

    private Scene createScene() {
        setActionListeners();
        buttonPanel = new GridPane();
        buttonPanel.setHgap(10);
        buttonPanel.setVgap(10);
        buttonPanel.setPadding(new Insets(25, 25, 25, 25));
        buttonPanel.add(allMusic, 0, 0);
        buttonPanel.add(favoriteMusic, 0, 1);
        buttonPanel.add(recentMusic, 0, 2);
        root.getChildren().add(buttonPanel);
        return (new Scene(root, Color.ALICEBLUE));
    }

    /* can add buttons into Panel. this Function executed when this Object initialize */
    /* add ActionListeners into Buttons */
    private void setActionListeners() {
        this.allMusic.setOnAction(e -> {
            listNum = 0;
            MusicList.listNum = 0;
            tabClicked();
        });

        this.favoriteMusic.setOnAction(e -> {
            listNum = 1;
            MusicList.listNum = 1;
            tabClicked();
        });

        this.recentMusic.setOnAction(e -> {
            listNum = 2;
            MusicList.listNum = 2;
            tabClicked();
        });

    }
    /* connect Buttons to Control other Class' Object */
    public void connectPanels(PlayerTab playerTab, MusicList musicList) {
        this.musicList = musicList;
        this.playerTab = playerTab;
    }
    /* refresh lists and reset whole player's buttons */
    private void tabClicked() {
        musicList.arrayListToListModel(MusicListManager.getInstance().nowList());
        musicList.getPanel().updateUI();
        playerTab.reset();
        playerTab.updateUI();
    }
    /* can give Favorite List Button */
    public Button getFavoriteButton() {
        return this.favoriteMusic;
    }

}
