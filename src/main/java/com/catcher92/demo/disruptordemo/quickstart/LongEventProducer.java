package com.catcher92.demo.disruptordemo.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer {

    private RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer data) {
        // 需要从ringBuffer里面获取一个可用的序号
        final long sequence = ringBuffer.next();
        try {
            // 根据序号找到具体的MessageEvent元素（空对象）
            final LongEvent longEvent = ringBuffer.get(sequence);
            // 进行赋值处理
            longEvent.setValue(data.getLong(0));
        } finally {
            // 提交发布操作
            ringBuffer.publish(sequence);
        }
    }
}
