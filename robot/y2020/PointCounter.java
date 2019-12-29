/**
 * 
 */
package com.zoubworld.robot.y2020;

import com.zoubworld.robot.IPointCount;

/**
 * @author pierre valleau
 *
 */
public class PointCounter implements IPointCount {
	int scoredisplayed;
	int scorefinal;
	boolean bWindSocks;
	static IPointCount inst=null;
	/**
	 * @return the inst
	 */
	public static IPointCount getInst() {
		if (inst==null)
			inst=new PointCounter();
		return inst;
	}
	/**
	 * 
	 */
	private PointCounter() {
		 scoredisplayed=0;
		 scorefinal=0;
		 bWindSocks=false;
		 inst=this;
	}
	public static final int BuoyInPort=0;//1
	public static final int BuoyInFairway =1;//1+1
	public static final int PairBuoyInFairway =2;//2+(1+1)+(1+1)
	public static final int WindSocks =3;//1=>5;2=>15
	public static final int LightHousePresent=4;//2
	public static final int LightHouseOn=5;//+3
	public static final int LightHouseDeployed=6;//+10
	public static final int AnchorSafely =7;//0, 5 or 10;
	public static final int AnchorSafelyGood =71;//0, 5 or 10;
	public static final int HoistFlags=8;//10
	public static final int EstimatePerformance=9;//INT(sum*0.3+0.999)
	public static final int NotForfeit=10;//+5
	
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.y2020.IPointCount#addAction(int)
	 */
	@Override
	public void addAction(int key)
	{
		switch (key) {
		case BuoyInPort:
			scoredisplayed+=1;
			break;
		case BuoyInFairway:
			scoredisplayed+=2;
			break;
		case PairBuoyInFairway:
			scoredisplayed+=6;
			break;
		case WindSocks:
			if(!bWindSocks)
				scoredisplayed+=5;
			else
				scoredisplayed+=10;			
			break;
		case LightHousePresent:
			scoredisplayed+=2;
			break;
		case LightHouseOn:
			scoredisplayed+=3;
			break;
		case LightHouseDeployed:
			scoredisplayed+=10;
			break;

		case AnchorSafelyGood:
			scoredisplayed+=10;
			break;
		case AnchorSafely:
			scoredisplayed+=5;
			break;
		case HoistFlags:
			scoredisplayed+=10;
			break;
		case EstimatePerformance:
			scorefinal=(int)(scoredisplayed*0.3+0.99999);
			if(scoredisplayed!=0)
				scorefinal+=5;
			scorefinal+=scoredisplayed;
			break;
			
		default:
			break;
		}
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.y2020.IPointCount#getScoreDisplayed()
	 */
	@Override
	public int getScoreDisplayed() {
		return scoredisplayed;
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.y2020.IPointCount#getScoreFinal()
	 */
	@Override
	public int getScoreFinal() {
		return scorefinal;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IPointCount pc=PointCounter.getInst();
		/*
		for(int i=0;i<5;i++)//10 roché
			pc.addAction(PairBuoyInFairway);
		for(int i=0;i<6;i++)//12 terrain adverse
			pc.addAction(PairBuoyInFairway);*/
		for(int i=0;i<6;i++)//12 terrain 
			pc.addAction(PairBuoyInFairway);
		pc.addAction(WindSocks);
		pc.addAction(WindSocks);
		pc.addAction(LightHousePresent);
		pc.addAction(LightHouseOn);
		pc.addAction(LightHouseDeployed);
		pc.addAction(AnchorSafelyGood);//AnchorSafely
		pc.addAction(HoistFlags);
		pc.addAction(EstimatePerformance);
		System.out.println("Score displayed "+pc.getScoreDisplayed());
		System.out.println("Score final "+pc.getScoreFinal());

	}

}
