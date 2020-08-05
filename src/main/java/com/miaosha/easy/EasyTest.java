package com.miaosha.easy;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EasyTest {
    String PATH = "D:\\daima\\";
    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setGoodsID(i);
            data.setGoodsName("名字"+i);
            data.setGoodsTitle("标题"+i);
            data.setGoodsImage("图片"+i);
            data.setGoodsDetail("细节"+i);
            data.setGoodsPrice(0.56);

            list.add(data);
        }
        return list;
    }


    @Test
    public void simpleWrite() {
        // 写法1
        String fileName = PATH + "EasyTest.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // write (fileName, 格式类)
        // sheet (表明)
        // doWrite (数据)
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
    }
    @Test
    public void simpleRead() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = PATH + "EasyTest.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭

        // 重点注意读取的逻辑 DemoDataListener
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }


}
