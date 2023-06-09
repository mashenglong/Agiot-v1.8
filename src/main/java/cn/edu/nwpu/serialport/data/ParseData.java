package cn.edu.nwpu.serialport.data;

import java.io.UnsupportedEncodingException;
import cn.edu.nwpu.mysql.pojo.Sensor;

public interface ParseData<E> {

    // Parse byte array by parsing rules
    public E[] parseData(byte[] bytes);
    Sensor parseDataToSensor(byte[] var1) throws UnsupportedEncodingException;
}
