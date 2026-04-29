package com.example.foodproject.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderNoGenerator {
    private static final Snowflake SNOWFLAKE = new Snowflake(1, 1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generate() {
        String ts = LocalDateTime.now().format(FORMATTER);
        long id = SNOWFLAKE.nextId();
        String suffix = String.valueOf(id);
        if (suffix.length() > 6) {
            suffix = suffix.substring(suffix.length() - 6);
        }
        return "ORD" + ts + suffix;
    }

    private static class Snowflake {
        private static final long TWEPOCH = 1288834974657L;
        private static final long WORKER_ID_BITS = 5L;
        private static final long DATACENTER_ID_BITS = 5L;
        private static final long SEQUENCE_BITS = 12L;
        private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
        private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
        private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
        private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
        private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
        private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

        private long workerId;
        private long datacenterId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        public Snowflake(long workerId, long datacenterId) {
            if (workerId > MAX_WORKER_ID || workerId < 0) throw new IllegalArgumentException();
            if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) throw new IllegalArgumentException();
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        public synchronized long nextId() {
            long timestamp = timeGen();
            if (timestamp < lastTimestamp) throw new RuntimeException();
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) timestamp = tilNextMillis(lastTimestamp);
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                    | (datacenterId << DATACENTER_ID_SHIFT)
                    | (workerId << WORKER_ID_SHIFT)
                    | sequence;
        }

        private long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        private long timeGen() {
            return System.currentTimeMillis();
        }
    }
}

