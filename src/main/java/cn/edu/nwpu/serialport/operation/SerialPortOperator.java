package cn.edu.nwpu.serialport.operation;

import cn.edu.nwpu.disruptor.QueueHelper;
import cn.edu.nwpu.utils.PropertiesUtil;
import cn.edu.nwpu.utils.ShowUtil;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.serialport.data.ParseData;
import cn.edu.nwpu.serialport.data.ParseLoRaData;
import cn.edu.nwpu.serialport.data.ParseZigbeeForCC2530;
import cn.edu.nwpu.serialport.listener.DataAvailableListenerInter;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SerialPortOperator {
    private SerialPort mSerialport;
    private QueueHelper queueHelper;
    private ParseData parseData;
    private LinkedListQueue queue = new LinkedListQueue();
    public SerialPortOperator() {
    }
    public SerialPortOperator(QueueHelper queueHelper, int deviceType, SerialPort mSerialPort) {
        this.queueHelper = queueHelper;
        this.mSerialport = mSerialPort;
        if(deviceType == 1){
            parseData = new ParseLoRaData();
        }
        else if(deviceType == 2) {
            parseData = new ParseZigbeeForCC2530();
        }
    }
    public void addSerialPortListener(String commName) {
        if (mSerialport != null) {
            log.info("SerialPort "+ commName+ " is opening" + "\r\n");
        }
        /**
         *  Use anonymous inner classes, in order to be able to make inner
         *  classes to use member variables of outer classes
         */
        SerialPortManager.addListener(mSerialport, new DataAvailableListenerInter() {
            @Override
            public void dataAvailable() {
                byte[] data = null;
                String serialNamePrefix = "";
                PropertiesUtil com = new PropertiesUtil("com");
                //Used it to distinguish whether it is running on Windows or Ubuntu
//                if (System.getProperty("os.name").contains("Windows")) {
                if (com.getUrlValue("AppType").equals("Windows")) {

                    serialNamePrefix = "//./";
                }
                try {
                    if (mSerialport == null) {
                        ShowUtil.errorMessage("The serial port object is empty, monitoring failed!");
                    } else if (mSerialport.getName().equals(serialNamePrefix + com.getUrlValue("LoraCom"))) {
                        log.info("Get the data of the lora end-device and start parsing");
                        queue = SerialPortManager.readFromPort(mSerialport,queue);
                        while (queue.getSize() >= 9) {
                            data = queue.dequeueNEle(9);
                            Short[] sensors = (Short[]) parseData.parseData(data);
                            double tem = sensors[0] / 10.0, hum = sensors[1] / 10.0;
                            Date d = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sDate = ft.format(d);
                            Sensor lora_sensor = new Sensor(1, tem, hum, sDate);
                            log.info("lora_sensor date:" + lora_sensor.toString());
                            queueHelper.SendMessageToHandler(lora_sensor);
                        }
                    } else if (mSerialport.getName().equals(serialNamePrefix + com.getUrlValue("ZigbeeCom"))) {
                        log.info("Get the data of the zigbee end-device and start parsing");
                        queue = SerialPortManager.readFromPort(mSerialport,queue);
                        while (queue.getSize() >= 46) {
                            data = queue.dequeueNEle_forZigbee(46);
                            Sensor zigbee_sensor = parseData.parseDataToSensor(data);
                            log.info("zigbee_toString: " + zigbee_sensor.toString());
                            queueHelper.SendMessageToHandler(zigbee_sensor);
                        }
                    }
                } catch (Exception e) {
                    log.info("reading serial port error  ");
                    e.printStackTrace();
                }
            }
        });
    }
    public static void closeSerialPort(SerialPort mSerialport) {
        SerialPortManager.closePort(mSerialport);
        log.info("Serial port is closed" + "\r\n");
        mSerialport = null;
    }
}