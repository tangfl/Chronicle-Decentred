package net.openhft.chronicle.decentred.util;

import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.salt.Ed25519;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VanillaPublicKeyRegistry implements PublicKeyRegistry {
    private final Map<Long, BytesStore> publicKeyMap = new ConcurrentHashMap<>();
    private boolean internal;

    @Override
    public void register(long address, BytesStore publicKey) {
        publicKeyMap.put(address, publicKey.copy());
    }

    @Override
    public Boolean verify(long address, BytesStore sigAndMsg) {
        if (internal)
            return true;
        BytesStore publicKey = publicKeyMap.get(address);
        if (publicKey == null) return null;
        return Ed25519.verify(sigAndMsg, publicKey);
    }

    @Override
    public boolean internal() {
        return internal;
    }

    @Override
    public PublicKeyRegistry internal(boolean internal) {
        this.internal = internal;
        return this;
    }
}
