/**
 * A class for controlling a set of Anim instances
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (21/11/2013)
 */
  
public class Animation {

  public static final int LIGHT_PARAM = 0;
  public static final int PLANE_PARAM = 1;
  public static final int ROBOT_X_PARAM = 2;
  public static final int ROBOT_Y_PARAM = 3;
  public static final int ROBOT_Z_PARAM = 4;
  public static final int ROBOT_RSELF_PARAM = 5;
  
  public static final int CUBE_PARAM  = 6;
  
  public static final int MAX_PARAMS = 10;
  private Anim[] param;
  private int numParams;
  private double globalStartTime, localTime, repeatTime, savedLocalTime; 
    
  /**
   * Constructor.
   *
   * @param keys List of key info, i.e. list of pairs {key frame value, key parameter value}
   */    
  public Animation() {
    param = new Anim[MAX_PARAMS];
    param[LIGHT_PARAM] = create(0.0, 15.0, true, true,   // light rotate
                                new double[]{0.0,0.0, 0.5,-6.0, 1.0,0.0}); 
    param[PLANE_PARAM] = create(10.0, 17.0, true, true,  // plane x
                                new double[]{0.0,0.0, 0.3,0.7, 0.4,1.0, 0.8,0.7, 1.0, 0.0});
    param[ROBOT_X_PARAM] = create(0.0, 5.0, true, true,  // robot x
                               new double[]{0.0,0.0, 0.5,Room.size-6,
			                     1.0,0.0});
    param[ROBOT_Y_PARAM] = create(0.0, 5.0, true, true,  // robot y
                               new double[]{0.0,0.0,1.0,0.0});
    param[ROBOT_Z_PARAM] = create(0.0, 5.0, true, true,  // robot z
                               new double[]{0.0,0.0, 0.25,Room.size/2-3, 0.5,0.0, 0.75, -Room.size/2+3,
    										1.0,0.0});

    param[ROBOT_RSELF_PARAM] = create(0.0, 5.0, true, true,  // robot self rotate
                               new double[]{0.0,0.0,
    										1.0,360.0});
    param[CUBE_PARAM] = create(0.0, 30.0, true, true,  // cube rotate
                               new double[]{0.0,0.0, 1.0, 360.0});  
    numParams = CUBE_PARAM+1;
    localTime = 0;
    savedLocalTime = 0;
    repeatTime = 5;
    globalStartTime = getSeconds();
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
      param[i].update(localTime);
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

  public static void main(String[] args) {
    Animation a = new Animation();  
    System.out.println(a.getParam(a.LIGHT_PARAM));
    double start = a.getSeconds();
    double t=start;
    while (t<start+20) {
      double ls = a.getSeconds();
      double lt = ls;
      while (lt < ls+0.2) lt = a.getSeconds();
      a.update();    
      System.out.println(a.localTime + ", " + a.getParam(a.LIGHT_PARAM));
      t = a.getSeconds();
    }
  }
  
}
