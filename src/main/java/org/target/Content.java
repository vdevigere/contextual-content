package org.target;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class Content<T> {
    private final String name;
    private final String description;
    private final Long contentId;
    private final T content;
    private final Double weight;

    public Content(String name, String description, Long contentId, T content, Double weight)
            throws IncorrectWeightException {
        this.name = name;
        this.description = description;
        this.contentId = contentId;
        this.content = content;
        if (weight <= 0)
            throw new IncorrectWeightException(weight);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getContentId() {
        return contentId;
    }

    public T getContent() {
        return content;
    }

    public Double getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        HashFunction hf = Hashing.murmur3_32();
        Hasher hasher = hf.newHasher().putString(name, Charsets.UTF_8).putString(description, Charsets.UTF_8)
                .putLong(contentId).putDouble(weight).putInt(content.hashCode());
        return hasher.hash().asInt();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
