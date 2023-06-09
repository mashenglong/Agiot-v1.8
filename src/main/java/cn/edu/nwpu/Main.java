package cn.edu.nwpu;
import cn.edu.nwpu.utils.ByteUtil;
import cn.edu.nwpu.utils.PropertiesUtil;
import cn.edu.nwpu.disruptor.QueueHelper;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.operation.SerialPortOperator;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
/**
 * Startup class, when the main class is running, open the serial ports
 * corresponding to lora and zigbee respectively, and register monitoring
 * events for the corresponding serial ports. If lora wants to get data, it
 * needs to send the inquiry frame regularly,set the inquiry once every 10s
 */
@Slf4j
public class Main {

    final static int DEVICE_TYPE_LORA = 1;
    final static int DEVICE_TYPE_ZIGBEE = 2;
    final static int SleepTime = 10000;
    public static void main(String[] args) {

        PropertiesUtil com = new PropertiesUtil("com");
        SerialPortOperator loRaSPortOp = new SerialPortOperator();
        SerialPortOperator zigBeeSPortOp = new SerialPortOperator();
        SerialPort zigBeeSerialPort = null;
        SerialPort loRaSerialPort = null;
        String zigBeePortName = com.getUrlValue("ZigbeeCom");
        String loRaPortName = com.getUrlValue("LoraCom");
        int zigBeeBaudRate = Integer.parseInt(com.getUrlValue("ZigbeeBaudRate"));
        int loRaBaudRate = Integer.parseInt(com.getUrlValue("LoraBaudRate"));;
        QueueHelper queueHelper = null;
//        String orderStr = "f80000000400703702005fc4";
        String orderStr = "010300000002C40B";
        byte[] order = ByteUtil.hexStr2Byte(orderStr);

        try {
            /*Prepare disruptor and bind consumers to disruptor*/
            queueHelper = new QueueHelper();
            queueHelper.start();

            /* open zigbee serialPort , and add addListener for it */
            zigBeeSerialPort = SerialPortManager.openPort(zigBeePortName, zigBeeBaudRate);
            zigBeeSPortOp = new SerialPortOperator(queueHelper, DEVICE_TYPE_ZIGBEE, zigBeeSerialPort);
            zigBeeSPortOp.addSerialPortListener(zigBeePortName);

            /* open Lora serialPort , and add addListener for it */
            loRaSerialPort = SerialPortManager.openPort(loRaPortName, loRaBaudRate);
            loRaSPortOp = new SerialPortOperator(queueHelper, DEVICE_TYPE_LORA, loRaSerialPort);
            loRaSPortOp.addSerialPortListener(loRaPortName);

            while(true){
                log.info("lora GateWay send request frame to LoRa end-device,start");
                Thread.sleep(SleepTime);
                assert loRaSerialPort != null;
                SerialPortManager.sendToPort(loRaSerialPort, order);
                log.info("lora GateWay send request frame to LoRa end-device,over");

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}