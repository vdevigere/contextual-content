package org.target;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.assertj.core.api.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.target.context.UserContext;
import org.uncommons.maths.random.XORShiftRNG;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class Campaign {
    public static final String DATE_FIELD = "date";
    private static Logger logger = LoggerFactory.getLogger(Campaign.class);
    private double total = 0;
    private final Long id;
    private final String name;
    private final NavigableMap<Double, Content<?>> map = new TreeMap<Double, Content<?>>();
    private final HashCode hashcode;
    private Collection<Condition<UserContext>> conditions;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Campaign(Long id, String name, Set<Content<?>> contentSet) {
        this.id = id;
        this.name = name;
        HashFunction hf = Hashing.murmur3_32();
        Hasher hasher = hf.newHasher().putString(this.name, Charsets.UTF_8).putLong(this.id);

        contentSet.forEach(content -> {
            this.total += content.getWeight();
            this.map.put(total, content);
            hasher.putInt(content.hashCode());
        });
        this.hashcode = hasher.hash();
    }

    public Content<?> resolveContent(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[20]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        bb.put(this.hashcode.asBytes());
        byte seed[] = bb.array();
        logger.debug("Seed:{}", seed);
        return resolveContent(seed);
    }

    public Content<?> resolveContent(byte[] seed) {
        double value = new XORShiftRNG(seed).nextDouble() * total;
        logger.debug("Locator:{}", value);
        return map.ceilingEntry(value).getValue();
    }

    @Override
    public int hashCode() {
        return this.hashcode.asInt();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    public boolean matches(UserContext userContext) {
        if (conditions != null) {
            for (Condition<UserContext> condition : conditions) {
                if (!condition.matches(userContext))
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    public Collection<Condition<UserContext>> getConditions() {
        return conditions;
    }

    public void setConditions(Collection<Condition<UserContext>> conditions) {
        this.conditions = conditions;
    }

}
