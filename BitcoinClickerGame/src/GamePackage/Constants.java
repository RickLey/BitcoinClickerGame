package GamePackage;

import java.awt.Font;

class Constants {
	//Class for constants, a lot of them
	
	//==== GUI Constants ===
	public static final int bitcoinButtonSize = 250;
	
	public static Font getFont(int size){
		return new Font("Helvetica", Font.PLAIN, size);
	}


	//==== Player Constants ====
	public static final double COMBO_INCREMENT_AMOUNT = 0.1;
	public static final int BASE_COINS_PER_CLICK = 1;
}
