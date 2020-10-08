package logic.abilities;

import utils.IntRef;

public class Projectile {
	private IntRef bd;
	private Board 
	
	public Projectile(Unit unit, IntRef bulletDamage)
	{
		bd = bulletDamage;
	}

	public IntRef getBd() {
		return bd;
	}

	public void setBd(IntRef bd) {
		this.bd = bd;
	}
	
	public boolean unitInFront(Projectile p)
	{
		if()
		return true;
	}
}
