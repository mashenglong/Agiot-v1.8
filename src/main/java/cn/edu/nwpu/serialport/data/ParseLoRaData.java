package cn.edu.nwpu.serialport.data;


import cn.edu.nwpu.mysql.pojo.Sensor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Slf4j
public class ParseLoRaData implements ParseData<Short> {
    @Override
    public Short[] parseData(byte[] bytes){
        short temperature = 0, humidity = 0;
        log.info("parse lora data"+ Arrays.toString(bytes));
        Short[] sensors = new Short[2];
        // Take out humidity
        temperature = (short) (((bytes[3]& 0xff) << 8) | (bytes[4]& 0xff));
        humidity = (short) ((bytes[5]&0xff << 8) | bytes[6]&0xff);
        // 存放到数组中
        sensors[0] = temperature;
        sensors[1] = humidity;
        return sensors;
    }


    public Short[] parseData_old(byte[] bytes) {
        short temperature, humidity;
        log.info("parse lora data"+ Arrays.toString(bytes));
        Short[] sensors = new Short[2];
        // Take out humidity
        int iHum = (bytes[10] & 0xFF);
        log.info("iHum:"+iHum);
        iHum |= (bytes[11] & 0xFF) << 8;
        humidity = (short) iHum;
        // Take out temperature
        int iTem = (bytes[12] & 0xFF);
        iTem |= (bytes[13] & 0xFF) << 8;
        temperature = (short) iTem;
        // 存放到数组中
        sensors[0] = temperature;
        sensors[1] = humidity;
        return sensors;
    }

    @Override
    public Sensor parseDataToSensor(byte[] var1) throws UnsupportedEncodingException {
        return null;
    }
}
