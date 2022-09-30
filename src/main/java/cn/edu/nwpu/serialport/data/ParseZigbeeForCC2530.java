package cn.edu.nwpu.serialport.data;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.utils.ByteUtil;
import cn.edu.nwpu.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ParseZigbeeForCC2530  implements  ParseData<Sensor>{
   @Override
   public Sensor[] parseData(byte[] bytes) {

       log.info("parse zigbee data: "+ Arrays.toString(bytes));
       String zigbee_data =  DataUtil.hexStringToString(ByteUtil.byteArrayToHexString(bytes)) ;
       log.info("zigbeeStr:"+zigbee_data);
       String[] zig_arr = zigbee_data.split(" ");
       Sensor zigbee_sensor= new Sensor();
       zigbee_sensor.setsId( 100 + Integer.parseInt(zig_arr[0].substring(3)));
       zigbee_sensor.setsTemperature(Double.parseDouble(zig_arr[1].substring(11)));
       zigbee_sensor.setsHumidity(Integer.parseInt(zig_arr[2].split(":")[1].trim()));
       Date d = new Date();
       SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       String sDate = ft.format(d);
       zigbee_sensor.setsDate(sDate);
       Sensor[] sensor = new Sensor[1];
       sensor[0] = zigbee_sensor;
       return  sensor;
   }
}
