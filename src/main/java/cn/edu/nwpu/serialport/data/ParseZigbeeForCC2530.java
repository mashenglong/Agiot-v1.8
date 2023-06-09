package cn.edu.nwpu.serialport.data;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.utils.ByteUtil;
import cn.edu.nwpu.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
//public class ParseZigbeeForCC2530  implements  ParseData<Sensor>{
//   @Override
//   public Sensor[] parseData(byte[] bytes) {
//
//       log.info("parse zigbee data: "+ Arrays.toString(bytes));
//       String zigbee_data =  DataUtil.hexStringToString(ByteUtil.byteArrayToHexString(bytes)) ;
//       log.info("zigbeeStr:"+zigbee_data);
//       String[] zig_arr = zigbee_data.split(" ");
//       Sensor zigbee_sensor= new Sensor();
//       zigbee_sensor.setsId( 100 + Integer.parseInt(zig_arr[0].substring(3)));
//       zigbee_sensor.setsTemperature(Double.parseDouble(zig_arr[1].substring(11)));
//       zigbee_sensor.setsHumidity(Integer.parseInt(zig_arr[2].split(":")[1].trim()));
//       Date d = new Date();
//       SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       String sDate = ft.format(d);
//       zigbee_sensor.setsDate(sDate);
//       Sensor[] sensor = new Sensor[1];
//       sensor[0] = zigbee_sensor;
//       return  sensor;
//   }
public class ParseZigbeeForCC2530 implements ParseData<Short> {
    private static final Logger log = LoggerFactory.getLogger(ParseZigbeeForCC2530.class);

    public ParseZigbeeForCC2530() {
    }

    public Short[] parseData(byte[] bytes) {
//        log.info("parse zigbee data" + Arrays.toString(bytes));
//        Short[] sensors = new Short[3];
//        System.out.println("data:" + Arrays.toString(bytes));
//        String s = new String(bytes);
//        System.out.println(s);
//        String[] str = s.split("_", -1);
//
//        for(int i = 0; i < str.length; ++i) {
//            System.out.println("" + i + ":" + str[i]);
//        }
//
//        return sensors;
        return null;
    }

    public Sensor parseDataToSensor(byte[] bytes) throws UnsupportedEncodingException {
        Sensor sensors = new Sensor();
        String s = new String(bytes);
        String[] str = s.split("_", -1);
        sensors.setsId(Integer.parseInt(str[0].substring(2)));
//        sensors.setsMAC(str[1].substring(2));
//        sensors.setsKey(Integer.parseInt(str[2].substring(2)));
        sensors.setsTemperature(Double.parseDouble(str[3].substring(2)));
        sensors.setsHumidity(Double.parseDouble(str[4].substring(2)));
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = ft.format(d);
        sensors.setsDate(sDate);
        return sensors;
    }
}
