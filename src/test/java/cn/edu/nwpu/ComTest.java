package cn.edu.nwpu;
import cn.edu.nwpu.utils.ByteUtil;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

/**
 * For testing virtual serial port for communication
 * When there is no serial port, use this class to simulate
 * the serial port test, and the operation must be after the main method of the Main class
 * Otherwise, the serial port will be occupied，the main method of the Main class will error！
 */
@Slf4j
public class ComTest {
    private Map<String, SerialPort> comMap = new HashMap<>();
    private List<String> comList = new ArrayList<>();
    public ComTest() {
        SerialPort[] commPorts = SerialPort.getCommPorts();
        for (SerialPort commPort : commPorts) {
            comList.add(commPort.getSystemPortName());
            comMap.put(commPort.getSystemPortName(), commPort);
            log.info("commPort："+commPort+",SystemPortName:"+commPort.getSystemPortName());
            commPort.openPort();
            commPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                }

                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    byte[] newData = serialPortEvent.getReceivedData();
                    System.err.println(String.format("串口%s接收到数据大小：%s,串口数据内容:%s"
                            ,serialPortEvent.getSerialPort().getSystemPortName(),newData.length, Arrays.toString(newData)));
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        ComTest comTest = new ComTest();
        Map<String, SerialPort> comMap = comTest.comMap;
        new Thread(()->{
            while(true){
                //[-8, 0, 0, 0, 8, 0, 112, 55, 2, 0, -110, 2, -101, -1, 53, -34, -24]
                String msg = "F80000000800703702009202010035DEE8";
                SerialPort serialPort = comMap.get("COM2");
                serialPort.setBaudRate(115200);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("ByteUtil.hexStr2Byte(msg lora):"+Arrays.toString(ByteUtil.hexStr2Byte(msg)));
                serialPort.writeBytes(ByteUtil.hexStr2Byte(msg),17);
            }
        }).start();

        new Thread(()->{
            while(true){
                /** According to the pre-set parsing rules of the zigbee CC2530 baseboard
                *   The frame is sent by zigbee end-device : 454e44XX2074656d706572747572653aXXXX2068756d69646974793aXXXX0d0a
                 *  The first XX represent END-device number , range [31,39]
                 *  The second XX represent Temperature in tens , number must be in [31,39],in hexadecimal
                 *  The Third XX represent Temperature in ones , number must be in [31,39] ,in hexadecimal
                 *  The fourth XX represent Humidity in tens , number must be in [31,39],in hexadecimal
                 *  The fifth XX represent Humidity in ones , number must be in [31,39],in hexadecimal
                */
                String msg = "454e44312074656d706572747572653a31312068756d69646974793a31310d0a";
                SerialPort serialPort = comMap.get("COM6");
                serialPort.setBaudRate(115200);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //log.info("ByteUtil.hexStr2Byte(msg zigbee):"+ Arrays.toString(ByteUtil.hexStr2Byte(msg)));
                serialPort.writeBytes(ByteUtil.hexStr2Byte(msg),32);
            }
        }).start();

    }
}
