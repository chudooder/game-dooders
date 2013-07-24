package net;

public enum Team {
	RED (0), 
	BLUE (1);
	
	int num;
	
	Team(int num) {
		this.num = num;
	}
	
	public int getNumber() {
		return num;
	}
	
	public static Team getTeam(int i) {
		if(i == 0) return RED;
		else return BLUE;
	}
}
