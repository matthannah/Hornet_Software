package hornet;

/**
 * Created by Nicholas on 28/07/2015.
 */
public class CONFIG {

    public class Coms{

        public class PacketCodes
        {
            public static final byte CONNECTION_REQUEST = 'a';
            public static final byte ACCGYRO = 1;
            public static final byte LIDAR = 'l'; //Setting l as the identification byte for a LIDAR data packet
        }

        public class USBSerial
        {
            /** Milliseconds to block while waiting for port open */
            public static final int TIME_OUT = 2000;
        }
    }


   // public class XBEE
   // {
   //     public final int BAUD_RATE = 9600;
   // }
}
