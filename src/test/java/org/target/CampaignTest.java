package org.target;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Calendar;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.uuid.Generators;

public class CampaignTest {
	private static final Logger logger = LoggerFactory.getLogger(CampaignTest.class);

	private Campaign<String> campaign;
	private final Content<String> contentA = new Content<String>("A", "A Content", 0L, "Banner A");
	private final Content<String> contentB = new Content<String>("B", "B Content", 0L, "Banner B");
	
	@Before
	public void initCampaign(){
		Calendar now = Calendar.getInstance();
		Calendar dayFromNow = Calendar.getInstance();
		dayFromNow.add(Calendar.DATE, 1);
		this.campaign = new Campaign<String>(1L, "DUMMY", now, dayFromNow);
	}
	
	@Test(expected=IncorrectWeightException.class)
	public void addZeroWeightContent() throws IncorrectWeightException {
		campaign.addContent(contentA, 0.0);
	}
	
	@Test
	public void ensureContentIsResolvedConsistently() throws IncorrectWeightException{
		campaign.addContent(contentA, 75.0);
		campaign.addContent(contentB, 25.0);
		assertThat(campaign.resolveContent("blahblahblahblahblah".getBytes()), equalTo(contentA));
		assertThat(campaign.resolveContent("fourfourfourfourfour".getBytes()), equalTo(contentB));
	}
	
	@Test
	public void ensureContentIsResolvedConsistentlyHashCode() throws IncorrectWeightException{
		campaign.addContent(contentA, 75.0);
		campaign.addContent(contentB, 25.0);
		UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
		logger.debug("BlahUUID:{}", blahUUID);
		assertThat(campaign.resolveContent(blahUUID), equalTo(contentA));
	}

	@Test 
	public void testHashCode() throws IncorrectWeightException{
		Calendar now = Calendar.getInstance();
		Calendar dayFromNow = Calendar.getInstance();		
		Campaign<String> campaign1 = new Campaign<String>(1L, "DUMMY", now, dayFromNow);
		Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A");
		Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B");
		campaign1.addContent(content1, 75.0);
		campaign1.addContent(content2, 25.0);
		campaign.addContent(contentA, 75.0);
		campaign.addContent(contentB, 25.0);
		assertThat(campaign1.hashCode(), equalTo(campaign.hashCode()));
	}
}
