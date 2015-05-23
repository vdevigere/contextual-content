package org.target;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.XORShiftRNG;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class Campaign<E> {
	private static Logger logger = LoggerFactory.getLogger(Campaign.class);

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Campaign(Long id, String name, Calendar sDate, Calendar eDate) {
		this.id = id;
		this.name = name;
		this.startDate = sDate;
		this.endDate = eDate;
	}
	
	public void addContent(Content<E> content, Double weight)
			throws IncorrectWeightException {
		if (weight <= 0)
			throw new IncorrectWeightException(weight);
		total += weight;
		map.put(total, content);
	};

	public Content<E> resolveContent(UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[20]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		bb.putInt(this.hashCode());
		byte seed[] = bb.array();
		logger.debug("Seed:{}", seed);
		return resolveContent(seed);
	}

	public Content<E> resolveContent(byte[] seed) {
		double value = new XORShiftRNG(seed).nextDouble() * total;
		logger.debug("Locator:{}", value);
		return map.ceilingEntry(value).getValue();
	}

	private double total = 0;
	private final Long id;
	private final String name;
	private final Calendar startDate, endDate;
	private final NavigableMap<Double, Content<E>> map = new TreeMap<Double, Content<E>>();
	private final HashFunction hf = Hashing.murmur3_32();

	@Override
	public int hashCode() {
		Hasher hasher = hf.newHasher()
				.putString(this.name, Charsets.UTF_8)
				.putLong(this.id);
		map.descendingKeySet().forEach(
				key -> {
					Content<E> content = map.get(key);
					Double lowerKey = (map.lowerKey(key) == null) ? 0.0 : map
							.lowerKey(key);
					Double weight = key - lowerKey;
					logger.debug("Content:{}, Weight:{}", content.getName(),
							weight);
					hasher.putDouble(weight).putObject(content,
							content.new Funnel());
				});
		int hashCode = hasher.hash().asInt();
		logger.debug("HashCode:{}", hashCode);
		return hashCode;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

}
