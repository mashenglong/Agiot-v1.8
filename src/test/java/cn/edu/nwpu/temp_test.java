package cn.edu.nwpu;

import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import cn.edu.nwpu.utils.ByteUtil;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
@Slf4j
public class temp_test {
    public static void main(String[] args) {
       ReadData();
    }
    public static void test(){
        byte[] bytes = new byte[]{48, 49, 48, 51, 48, 52, 48, 49, 50, 48, 48, 49, 50, 48, 48, 66, 50, 48};
        Short[] sensors = parsedata(bytes);
        System.out.println(Arrays.toString(sensors));
    }
    public static void ReadData(){
        String orderStr = "010300000002C40B";
        byte[] order = ByteUtil.hexStr2Byte(orderStr);
        SerialPort loRaSerialPort;
        String loraPortName = "COM8";
        int rfidBaudRate = 115200;
        try {
            loRaSerialPort = SerialPortManager.openPort(loraPortName, rfidBaudRate);
            LinkedListQueue queue = new LinkedListQueue();
            SerialPortManager.sendToPort(loRaSerialPort, order);
            while (true) {
                queue = SerialPortManager.readFromPort(loRaSerialPort, queue);
                System.out.println(queue.getSize());
                byte[] data = queue.dequeueNEle(queue.getSize());
                System.out.println(Arrays.toString(data));
                System.out.println(Arrays.toString(parsedata(data)));

            }
        } catch (PortInUseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析数据
     */
    public static Short[] parsedata(byte[] bytes){
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
}

//I:001_M:00124B00252E1B8D_K:01234567_T:24_H:45
//49 3A 30 30 31 5F 4D 3A 30 30 31 32 34 42 30 30 32 35 32 45 31 42 38 44 5F 4B 3A 30 31 32 33 34 35 36 37 5F 54 3A 32 34 5F 48 3A 34 35 0A