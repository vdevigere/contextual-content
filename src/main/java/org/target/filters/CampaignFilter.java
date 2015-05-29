package org.target.filters;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.target.Campaign;
import org.target.Content;

public class CampaignFilter {

    private final UUID uuid;

    public CampaignFilter(UUID uuid) {
        this.uuid = uuid;
    }

    public Map<String, Content<?>> filter(List<Campaign> campaignList, Predicate<Campaign> tester) {
        Map<String, Content<?>> resolvedContentMap = new LinkedHashMap<String, Content<?>>();
        campaignList.forEach(campaign -> {
            if (tester.test(campaign)) {
                resolvedContentMap.put(campaign.getName(), campaign.resolveContent(this.uuid));
            }
        });

        return resolvedContentMap;
    }
}
