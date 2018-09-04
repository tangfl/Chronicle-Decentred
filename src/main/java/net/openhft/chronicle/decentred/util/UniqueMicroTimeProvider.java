package net.openhft.chronicle.decentred.util;

import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.core.time.TimeProvider;

import java.util.concurrent.atomic.AtomicLong;

public enum UniqueMicroTimeProvider implements TimeProvider {
    INSTANCE;
    private final SystemTimeProvider BASE = SystemTimeProvider.INSTANCE;
    private final AtomicLong lastTime = new AtomicLong();

    @Override
    public long currentTimeMillis() {
        return BASE.currentTimeMillis();
    }

    @Override
    public long currentTimeMicros() {
        // not very accurate but light weight in Java.
        long now = BASE.currentTimeMicros();
        while (true) {
            long value0 = lastTime.get();
            final long value;
            if (value0 < now)
                value = now;
            else
                value = value0 + 1;
            if (lastTime.compareAndSet(value0, value))
                return value;
        }
    }
}
