package com.progressive.minds.chimera.core.datahub.common;

import com.progressive.minds.chimera.core.datahub.tag.ManageTags;
import org.junit.jupiter.api.Test;

class ManageTagsTest {
    @Test
    void TagTest() throws Exception {

        String retval= ManageTags.createTags("MANISH","MANISH IS GOOD BOY");
        System.out.println(retval.toString());
    }
}