package cn.edu.nwpu.serialport.data;

public interface ParseData<E> {

    // Parse byte array by parsing rules
    public E[] parseData(byte[] bytes);
}
