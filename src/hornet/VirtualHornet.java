package hornet;

import com.digi.xbee.api.exceptions.XBeeException;

import hornet.coms.Coms;
import hornet.coms.ComsEncoder;
import hornet.gui.Navigation;
import hornet.joystrick.JoystickManager;

/**
 * Created by Nicholas on 2/08/2015.
 */
public class VirtualHornet {

    /** The system that manages low level communications */
    private Coms _coms;

    /** The system that encodes messages into spendable packets */
    private ComsEncoder _comsEncoder;

    /** The UI */
    private Navigation _navigation;

    /** THe Joystick Manager */
    private JoystickManager _joystickManager;

    /** The state of the software system */
    enum State{Init,Idle,Connect,Connected}
    private State _state;

    private boolean _joyReady = false;

    /**
     * Default constructor
     * Dose nothing because components are not added yet
     */
    public VirtualHornet()
    {
        _state =State.Init;
    }

    /**
     * Attach all components that need to be constructed externally
     */
    public void attachNavigation(Navigation theNav){ _navigation = theNav; }
    public void attachComs(Coms theComs){_coms = theComs;}
    public void attachComsEncoder(ComsEncoder theComsEncoder){_comsEncoder = theComsEncoder;}
    public void attachJoystickManager(JoystickManager theJoystickManager){_joystickManager = theJoystickManager;}

    /**
     * Start the system (equivalent to a constructor, call after all components are attached)
     */
    public void start()
    {
        _navigation.open();
        _navigation.setComPorts(Coms.getPorts());

        _state =State.Idle;

        _navigation.updateJoystickList();

    }

    /**
     * The user is attempting to connect to the drone
     * @param comPort The com port to use to connect to the drone
     */
    public void UI_connect(String comPort,int baudRate ){
        if(_state == State.Idle) {
            _navigation.setConnectionState(Navigation.ConnectionState.Connecting);
            _state =  State.Connect;
            if(!_coms.open(comPort,baudRate))
            {
                _navigation.setConnectionState(Navigation.ConnectionState.Disconnected);
                //@TODO add failure notification
            }
         }else
         {
             //@toDO throw
         }
    }

    public void UI_joystickConnected(String theJoystick)
    {
        if(_joystickManager.connect(theJoystick))
        {
            _navigation.turnJoyStickConnectedOn();
            _joyReady = false;
        }
    }

    /**
     * A message has been received from the drone requesting to open a connection
     */
    public void C_connectRequest()
    {
        if(_state == State.Connect)
        {
            _state = State.Connected;
            _navigation.setConnectionState(Navigation.ConnectionState.Connected);
            _comsEncoder.send_connectionConfirmation();
        }
    }

    public void C_accGyro(float[] acc,float[] gyro)
    {
        _navigation.accGyro(acc,gyro);
        if(_state == State.Connect)
        {
            _comsEncoder.send_reset();
        }
    }

    public void J_newXY(int xPer, int yPer)
    {
        if(_state != State.Init) {
            _navigation.updateJoystickXY(xPer, yPer);
        }
    }

    public void J_newRotation(int r)
    {

        if(_state != State.Init)
        {
            _navigation.updateJoystickRotation(r);
        }
    }

    public void J_newThrottle(int t)
    {
        if(_state != State.Init)
        {
            _navigation.updateJoystickThrottle(t);
            if(t == 100)
            {
                _joyReady = true;
                _navigation.turnJoyStickReady();
            }

            if(_state == State.Connected && _joyReady)
            {
                _comsEncoder.send_throttle(t);
            }
        }
    }

    public void L_newLidar(float yaw, float distance, float pitch)
    {
       // _navigation.newLidar(yaw, distance, pitch);     //Pass the newly received Lidar data to the UI to be drawn
    }

}
