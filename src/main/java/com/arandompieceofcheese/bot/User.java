package com.arandompieceofcheese.bot;

public class User {
	private String rank;
	private String name;
	
	private String lastMessage = "";
	private int lastTime = 0;
	private int spamCount = 0;
	
	public User(String init) {  //#xorhash
		rank = init.substring(0,  1);
		name = init.substring(1);
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getName() {
		return name;
	}
	
	public int talked(Message m) {
		if(m.getTimestamp() <= lastTime + 2 && m.getContent().equals(lastMessage)) spamCount++;
		else spamCount = 0;
		lastMessage = m.getContent();
		lastTime = m.getTimestamp();
		return spamCount;
	}
}
