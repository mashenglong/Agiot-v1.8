package cn.edu.nwpu;

import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.Arrays;

public class RFIDTest {
    public static void main(String[] args) {
        SerialPort rfidPort;
        String rfidPortName = "COM3";
        int rfidBaudRate = 57600;
        try {
            rfidPort = SerialPortManager.openPort(rfidPortName, rfidBaudRate);
            LinkedListQueue queue = new LinkedListQueue();
            while (true) {
                queue = SerialPortManager.readFromPort(rfidPort, queue);
                byte[] data = queue.dequeueNEle(queue.getSize());
                System.out.println(Arrays.toString(parsedata(data)));

            }
        } catch (PortInUseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     *  解析数据
     */
    public static byte[] parsedata(byte[] data){
        byte[] data1 = Arrays.copyOfRange(data, 4, 16);
        for (int i = 0; i < data1.length; i++) {
            String s= Integer.toHexString(data1[i] & 0xFF);
            data1[i] = Byte.parseByte(s);
        }
        return data1;
    }
}
