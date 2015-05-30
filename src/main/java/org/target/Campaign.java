package org.target;

import java.nio.ByteBuffer;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.joda.time.DateTime;
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
    private final DateTime startDate, endDate;
    private final NavigableMap<Double, Content<?>> map = new TreeMap<Double, Content<?>>();
    private final HashCode hashcode;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public Campaign(Long id, String name, DateTime sDate, DateTime eDate, Set<Content<?>> contentSet) {
        this.id = id;
        this.name = name;
        this.startDate = sDate;
        this.endDate = eDate;
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
    
    public boolean matches(UserContext userContext){
        boolean lowerLimitMatch = userContext.getTimeStamp().isAfter(this.startDate);
        logger.debug("is {} after {}? -> {}", userContext.getTimeStamp(), this.startDate, lowerLimitMatch);
        
        boolean upperLimitMatch = userContext.getTimeStamp().isBefore(this.endDate) || userContext.getTimeStamp().equals(this.endDate);
        logger.debug("is {} before or equal to {}? -> {}", userContext.getTimeStamp(), this.endDate, upperLimitMatch);
        
        boolean result = lowerLimitMatch && upperLimitMatch;
        return result;
    }
}
