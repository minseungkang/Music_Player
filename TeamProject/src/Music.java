import java.util.ArrayList;
import java.io.*;

public class Music {
	private String author;
	private String singer;
	private String name;
	private int play_time;
	private String genre;
	private String nation;
	
	private int play_count;
	
	private String file_address;
	private String file_information_address;
	
	private Lyric lyrics;
	private ArrayList<Integer> recent_play;
	
	private static int music_file_num = 0;
	
	Music(){
		music_file_num++;
	}
	Music(String name, String file_address){
		//파일 읽기
		this.name = name;
		this.file_address = file_address;
		
		music_file_num++;
	}
	Music(String file_infromation_address){
		this.file_information_address = file_information_address;
		
		String[] information = getMusicFileInformation();
		
		music_file_num++;
	}
	Music(String author, String singer, String name,int play_time,
			String file_address, String nation, String[] genre){
		
		setMusicFileInformation(author, singer, name,
				play_time, file_address, nation, genre);
	
		music_file_num++;
	}
	
	/*
	private void getMusicFile(){
		try{
			String file_name = this.name + ".mp3";
			File music_file = new File(file_name);
			
			
		}
		catch(Exception ex){
			
		}
	}*/
	
	
	private void reNameMusicFile(){
		// 음악파일 이름 바꾸기
	}
	
	
	public String[] getMusicFileInformation(){
		String[] information = {};
		return information;
	}
	public void setMusicFileInformation(String author, String singer, String name,int play_time,
			String file_address, String nation, String[] genre){
		this.author = author;
		this.singer = singer;
		this.name = name;
		this.play_time = play_time;
		this.file_address = file_address;
		
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPlay_time() {
		return play_time;
	}
	public void setPlay_time(int play_time) {
		this.play_time = play_time;
	}
	public int getPlay_count() {
		return play_count;
	}
	public void setPlay_count(int play_count) {
		this.play_count = play_count;
	}
	public String getFile_address() {
		return file_address;
	}
	public void setFile_address(String file_address) {
		this.file_address = file_address;
	}
	public Lyric getLyrics() {
		return lyrics;
	}
	public void setLyrics(Lyric lyrics) {
		this.lyrics = lyrics;
	}
	public ArrayList<Integer> getRecent_play() {
		return recent_play;
	}
	public void setRecent_play(ArrayList<Integer> recent_play) {
		this.recent_play = recent_play;
	}

	
}
