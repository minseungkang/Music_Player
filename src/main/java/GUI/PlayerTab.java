package GUI;

import Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlayerTab implements Initializable, Observer {
    public static Label lyric;

    /* MP3Music info -> Image, name */
    @FXML
    private Label musicName;
    private Image musicImage;
    @FXML
    private ImageView musicImageView;

    /* Buttons */
    @FXML
    private Button playButton;
    @FXML
    private Button seekNextButton;
    @FXML
    private Button seekPreviousButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button playModeButton;
    @FXML
    private Button starButton; // for favorite
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider currentTimeSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CurrentMusicPlayer.getInstance().addObserver(this);
    }

    private void addCurrentTimeSliderEventHandler() {
        CurrentMusicPlayer currentMusicPlayer = CurrentMusicPlayer.getInstance();

        currentTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double percent = (newValue.floatValue() - currentTimeSlider.getMin()) / (currentTimeSlider.getMax() - currentTimeSlider.getMin());
            if (currentTimeSlider.isValueChanging()) {
                currentMusicPlayer.seek(percent);
            }
        });

        currentMusicPlayer.addChangeTimeEvent(currentTimeSlider, (Slider slider) -> Platform.runLater(() -> {
            Optional<Duration> currentTimeOptional = currentMusicPlayer.getCurrentTime();
            Optional<Duration> totalTimeOptional = currentMusicPlayer.getTotalTime();
            if (currentTimeOptional.isPresent() && totalTimeOptional.isPresent() && !slider.isValueChanging()) {
                double currentTime = currentTimeOptional.get().toMillis();
                double totalTime = totalTimeOptional.get().toMillis();
                slider.setValue(currentTime / totalTime);
            }
        }));
    }

    //add whole Buttons
    private void changeButtonToImage(Button button, String imageFileName, String text) {
        Image buttonImage = new Image(getClass().getResourceAsStream(imageFileName), 20, 20, true, true);
        if (buttonImage.getWidth() != 0) {
            button.setText(null);
            button.setGraphic(new ImageView(buttonImage));
            button.setPrefSize(40, 40);
        } else {
            button.setText(text);
            button.setPrefSize(40, 40);
        }
    }

    @FXML
    private void seekPrevious(ActionEvent event) {
        CurrentMusicPlayer currentMusicPlayer = CurrentMusicPlayer.getInstance();
        currentMusicPlayer.seekPrevious();
    }

    @FXML
    private void play(ActionEvent event) {
        CurrentMusicPlayer currentMusicPlayer = CurrentMusicPlayer.getInstance();
        if (currentMusicPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
            currentMusicPlayer.getStatus() == MediaPlayer.Status.READY ||
            currentMusicPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) {
            currentMusicPlayer.play();
        } else if (currentMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentMusicPlayer.pause();
        }
    }

    @FXML
    private void seekNext(ActionEvent event) {
        CurrentMusicPlayer currentMusicPlayer = CurrentMusicPlayer.getInstance();
        currentMusicPlayer.seekNext();
    }

    @FXML
    private void changePlayMode(ActionEvent event) {
        if (MusicList.playMode == PlayMode.CYCLIC_WHOLE) {
            MusicList.playMode = PlayMode.WHOLE;
            playModeButton.setText("A/N");
        } else if (MusicList.playMode == PlayMode.WHOLE) {
            MusicList.playMode = PlayMode.ONE_REPEAT;
            playModeButton.setText("O/R");
        } else if (MusicList.playMode == PlayMode.ONE_REPEAT) {
            MusicList.playMode = PlayMode.SHUFFLE;
            playModeButton.setText("SHU");
        } else if (MusicList.playMode == PlayMode.SHUFFLE) {
            MusicList.playMode = PlayMode.CYCLIC_WHOLE;
            playModeButton.setText("A/R");
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        CurrentMusicPlayer currentMusicPlayer = CurrentMusicPlayer.getInstance();
        currentMusicPlayer.stop();
        setPlayButtonToPlay();
    }

    @FXML
    private void toggleFavorite() {
        Music music = CurrentMusicPlayer.getInstance().getMusic();
        MusicListManager musicList = MusicListManager.getInstance();
        if (!music.isFavorite()) {
            music.setFavorite(true);
            musicList.addToFavoriteMusicList(music);
            setFavoriteButton(true);
        } else {
            music.setFavorite(false);
            musicList.deleteToFavoriteMusicList(music);
            setFavoriteButton(false);
        }
    }

    private void addVolumeSlider() {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            CurrentMusicPlayer.getInstance().setVolume(newValue.floatValue());
        });
    }

    private void replaceMusicInfo() {
        try {
            Music music = CurrentMusicPlayer.getInstance().getMusic();
            if (music.getAlbumArt() != null) {
                musicImage = new Image(new ByteArrayInputStream(music.getAlbumArt()), 200, 200, true, true);
                musicImageView.setImage(musicImage);
            }
            musicName.setText(music.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            starButton.setText("★");
        } else {
            starButton.setText("☆");
        }
    }

    private void setPlayButtonToPause() {
        changeButtonToImage(playButton, "pause.png", "||");
    }

    private void setPlayButtonToPlay() {
        changeButtonToImage(playButton, "play.jpg", "▶");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CurrentMusicPlayer && arg == null) {
            CurrentMusicPlayer o1 = (CurrentMusicPlayer) o;
            addCurrentTimeSliderEventHandler();
            addVolumeSlider();
            replaceMusicInfo();
        }

        if (arg instanceof MediaPlayer.Status) {
            MediaPlayer.Status st = (MediaPlayer.Status) arg;
            switch (st) {
                case READY:
                    setPlayButtonToPlay();
                    break;
                case PLAYING:
                    setPlayButtonToPause();
                    break;
                case PAUSED:
                    setPlayButtonToPlay();
                    break;
            }
        }
    }
}
