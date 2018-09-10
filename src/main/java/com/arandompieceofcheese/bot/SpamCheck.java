package com.arandompieceofcheese.bot;

public class SpamCheck {
	private int lastTime = 0;
	private int spamLevel = 0;
	private int capsLevel = 0;
	private String lastMessage = "";
	
	public SpamCheck() {
		
	}
	
	public boolean sent(Message m, int tolerances, int tolerancec) {
		boolean isSpam = m.getTimestamp() - lastTime < 5;
		double up = 0;
		for(char c : m.getContent().toCharArray()) {
			if(Character.isUpperCase(c)) {
				up++;
			}
		}
		boolean isCaps = up/m.getContent().length() >= .60d;
		boolean isSame = lastMessage == m.getContent();
		
		if(isSpam) {
			if(isCaps) capsLevel++;
			if(isSame) spamLevel++;
			lastTime = m.getTimestamp();
			lastMessage = m.getContent();
			return capsLevel == tolerancec || spamLevel == tolerances;
		}else {
			lastTime = m.getTimestamp();
			lastMessage = m.getContent();
			if(isCaps) {
				capsLevel = 1;
			}else {
				capsLevel = 0;
			}
			if(isSpam) {
				spamLevel = 1;
			}else {
				spamLevel = 0;
			}
			return false;
		}
	}
}
