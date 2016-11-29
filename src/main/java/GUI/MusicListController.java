package GUI;

import Model.CurrentMusic;
import Model.Music;
import Model.MusicList;
import Model.MusicListManager;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

public class MusicListController implements Initializable {
    @FXML
    private Button bnPlaylist;
    @FXML
    private Button bnFavoritePlaylist;
    @FXML
    private Button bnRecentPlaylist;
    @FXML
    private ListView<Music> musicListView;

    private enum Now {
        Playlist,
        FavoritePlaylist,
        RecentPlaylist
    }

    Now playinglist;

    // Music list to be drawn
    private ObservableList<Music> musicList;

    @FXML
    private void changeMusic(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Music music = musicListView.getSelectionModel().getSelectedItem();

            CurrentMusic.getInstance().setMedia(music.getFileAddress());
            MusicListManager.getInstance().addToRecentPlayList(CurrentMusic.getInstance().getMusic());
        }
    }

    private void redrawPlaylist(MusicList drawlist) {
        musicList.setAll(drawlist.getMusicList());
        musicListView.setItems(musicList);
    }

    @FXML
    private void showPlaylist(ActionEvent event) {
        redrawPlaylist(MusicListManager.getInstance().getPlaylist());
    }

    @FXML
    private void showFavoritePlaylist(ActionEvent event) {
        redrawPlaylist(MusicListManager.getInstance().getFavoritePlaylist());
    }

    @FXML
    private void showRecentPlaylist(ActionEvent event) {
        redrawPlaylist(MusicListManager.getInstance().getRecentPlaylist());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.musicList = new SimpleListProperty<>(FXCollections.observableArrayList());
        musicListView.setItems(musicList);

        playinglist = null;

        MusicListManager.getInstance().addRecentPlaylistObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (playinglist == Now.RecentPlaylist) {
                    redrawPlaylist(MusicListManager.getInstance().getRecentPlaylist());
                }
            }
        }).addPlaylistObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (playinglist == Now.Playlist) {
                    redrawPlaylist(MusicListManager.getInstance().getPlaylist());
                }
            }
        }).addFavoritePlaylistObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (playinglist == Now.FavoritePlaylist) {
                    redrawPlaylist(MusicListManager.getInstance().getFavoritePlaylist());
                }
            }
        });
    }
}