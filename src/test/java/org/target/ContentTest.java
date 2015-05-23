package org.target;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContentTest {

    @Test(expected=IncorrectWeightException.class)
    public void testContentCreationWithIncorrectWeight() throws IncorrectWeightException {
        new Content<String>("A", "A Content", 0L, "Banner A", 0.0);
    }
    
    public void testContentEquality() throws IncorrectWeightException{
        Content<String> A = new Content<String>("A", "A Content", 0L, "Banner A", 0.0);
        Content<String> B = new Content<String>("A", "A Content", 0L, "Banner A", 0.0);  
        assertThat(A, equalTo(B));
    }

}
