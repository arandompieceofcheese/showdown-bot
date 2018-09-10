package com.arandompieceofcheese.bot;

public class Message {
	private int timestamp;
	private User author;
	private String content;
	
	public Message(String init) { //|c:|timestamp|author|content
		String split[] = init.split("\\|", 5);
		timestamp = Integer.parseInt(split[2]);
		author = new User(split[3]);
		content = split[4];
	}
	
	public int getTimestamp() {
		return timestamp;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public String getContent() {
		return content;
	}
}
