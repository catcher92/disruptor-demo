package com.catcher92.demo.disruptordemo.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * 编程模型：
 * 1. 编写工厂Event类
 * 2. 编写监听事件类
 * 3. 实例化disruptor
 * 4. 编写生产者，向disruptor投递数据
 */
public class Main {

    public static void main(String[] args) {
        final LongEventFactory eventFactory = new LongEventFactory();
        final int ringBufferSize = 1024;
        final BlockingWaitStrategy waitStrategy = new BlockingWaitStrategy();
        // 实例化disruptor对象
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, waitStrategy);
        // 添加消费者监听
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动disruptor
        disruptor.start();
        // 获取存储数据的ringBuffer容器
        final RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        final LongEventProducer eventProducer = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (int i = 0; i < 100; i++) {
            bb.putLong(0, i);
            eventProducer.sendData(bb);
        }
        disruptor.shutdown();
    }

}
