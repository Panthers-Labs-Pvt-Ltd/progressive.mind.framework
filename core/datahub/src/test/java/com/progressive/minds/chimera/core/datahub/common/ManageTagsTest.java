package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.urn.Urn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManageTagsTest {
    @Test
    void TagTest() throws Exception {

        String retval= ManageTags.createTags("MANISH","MANISH IS GOOD BOY");
        System.out.println(retval.toString());
    }
}