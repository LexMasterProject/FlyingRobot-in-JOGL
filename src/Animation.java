/**
 * A class for controlling a set of Anim instances
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (21/11/2013)
 */
  
public class Animation {

  public static final int ROBOT1_Y_PARAM = 0;
  public static final int ROBOT1_Z_PARAM = 1;
  public static final int ROBOT_X_PARAM = 2;
  public static final int ROBOT_Y_PARAM = 3;
  public static final int ROBOT_Z_PARAM = 4;
  public static final int RSELF_360_PARAM = 5;
  
  public static final int ROBOT_60_R  = 6;
  public static final int ROBOT_90_R  = 7;
  public static final int ROBOT_120_R  = 8;
  
  public static final int MAX_PARAMS = 10;
  private Anim[] param;
  private int numParams;
  private double globalStartTime, localTime, repeatTime, savedLocalTime; 
  private boolean []repeatAfterDuration;
    
  /**
   * Constructor.
   *
   * @param keys List of key info, i.e. list of pairs {key frame value, key parameter value}
   */    
  public Animation() {
	  

    param = new Anim[MAX_PARAMS];
    param[ROBOT1_Y_PARAM] =create(0.0, 5.0, true, true,  // robot y
            	new double[]{0.0,0.0,
							0.125,2,
							0.25,-2,
							0.6,8,
							0.7,2,
							0.875,6,
							1.0,0.0}); 
    param[ROBOT1_Z_PARAM] = create(0.0, 5.0, true, true,  // robot z
            new double[]{0.0,0.0, 
    					 0.25,Room.size/2,
    					 0.5,12,
    					 0.6,6,
    					 0.75,0,
    					 0.85,-Room.size/2+8,
						 1.0,0.0});
    param[ROBOT_Z_PARAM] = create(0.0, 5.0, true, true,  // robot z
            new double[]{0.0,0.0, 
    			0.25,Room.size/2-3,
    				0.5,2,
    				0.6,0.0, 
    				0.75, -Room.size/2+3,
					1.0,0.0});
    param[ROBOT_Y_PARAM] = create(0.0, 5.0, true, true,  // robot y
            new double[]{0.0,0.0,
							0.125,2,
							0.25,-2,
							0.6,-5,
							0.8,2,
							0.875,6,
							1.0,0.0});
    param[ROBOT_X_PARAM] = create(0.0, 5.0, true, true,  // robot x
                               new double[]{0.0,0.0, 0.5,Room.size-6,
			                     1.0,0.0});
   
   
    param[RSELF_360_PARAM] = create(0.0, 5.0, true, true,  // robot self rotate
                               new double[]{0.0,0.0,
    										1.0,360.0});
    
    param[ROBOT_60_R] = create(0.0, 0.5, true, true,  // 60 rotate
            new double[]{0.0,0.0,
							0.25,-30.0,
							0.75, 30.0,
							1.0,0.0}); 
    param[ROBOT_90_R] = create(0.0, 0.5, true, true,  // 60 rotate
                               new double[]{0.0,0.0,
    										0.25,-45.0,
    										0.75, 45.0,
    										1.0,0.0}); 
    param[ROBOT_120_R] = create(0.0, 0.5, true, true,  // 60 rotate
            new double[]{0.0,0.0,
							0.25,-60.0,
							0.75, 60.0,
							1.0,0.0});  
    numParams = ROBOT_120_R+1;
	
    /*
     * indicate whether the Anim should stop or restart 
     * if localTime > duration but localTime < repeatTime
     */
    initAfterDurationAction();
	
    localTime = 0;
    savedLocalTime = 0;
    repeatTime = 5;
    globalStartTime = getSeconds();
  }
  
  private void initAfterDurationAction()
  {
	  repeatAfterDuration=new boolean[numParams];
	  for (int i = 0; i < repeatAfterDuration.length; i++) {
			repeatAfterDuration[i]=false;
	  }
	  repeatAfterDuration[ROBOT_60_R]=true;
	  repeatAfterDuration[ROBOT_90_R]=true;
	  repeatAfterDuration[ROBOT_120_R]=true;
	  
  }
  
  
  public Anim create (double start, double duration, boolean pre, boolean post, double[] data) {
    KeyInfo[] k = new KeyInfo[data.length/2];
    for (int i=0; i<data.length/2; ++i) {
      k[i] = new KeyInfo(data[i*2], data[i*2+1]);
    }    
    return new Anim(start, duration, pre, post, k);
  }
  
  public void startAnimation() {
    globalStartTime = getSeconds() - savedLocalTime;
  }
  
  public void pauseAnimation() {
    savedLocalTime = getSeconds() - globalStartTime;
  }
  
  public void reset() {
    globalStartTime = getSeconds();
    savedLocalTime = 0;
    for (int i=0; i<numParams; ++i) {
      param[i].reset();
    }
    initAfterDurationAction();
  }
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
  /**
   * 
   */ 
  public void update() {
    localTime = getSeconds() - globalStartTime;
    if (localTime > repeatTime) {
      globalStartTime = getSeconds();
      localTime = 0;
      savedLocalTime = 0;
    }  
    for (int i=0; i<numParams; ++i) {
    	if(repeatAfterDuration[i])
    	{
    		param[i].update(localTime%param[i].getDuration());
    	}
    	else
    	{
    		param[i].update(localTime);
    	}
    }
  }

 /**
   * 
   *
   * @return The current parameter value
   */   
  public double getParam(int i) {
    if (i<0 || i>=numParams) {
      System.out.println("EEError: parameter out of range");
      return 0;
    }
    else {
      return param[i].getCurrValue();
    }
  }
  
  /**
   * Standard use of toString method
   * 
   * @return A string representing the key data
   */      
  public String toString() {
    String s = "Anim manager: ";
    return s;
  }

//  public static void main(String[] args) {
//    Animation a = new Animation();  
//    System.out.println(a.getParam(a.LIGHT_PARAM));
//    double start = a.getSeconds();
//    double t=start;
//    while (t<start+20) {
//      double ls = a.getSeconds();
//      double lt = ls;
//      while (lt < ls+0.2) lt = a.getSeconds();
//      a.update();    
//      System.out.println(a.localTime + ", " + a.getParam(a.LIGHT_PARAM));
//      t = a.getSeconds();
//    }
//  }
  
}
