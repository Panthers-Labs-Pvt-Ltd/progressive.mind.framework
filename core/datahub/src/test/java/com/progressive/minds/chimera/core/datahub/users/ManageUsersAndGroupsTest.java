package com.progressive.minds.chimera.core.datahub.users;

import com.progressive.minds.chimera.core.datahub.modal.Users;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ManageUsersAndGroupsTest {

    @Test
    void createUsers() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<Users> userInfo = new ArrayList<>();
        Users user = new Users( "Data Engineer", "Manish","Kumar", "Kumar, Manish Gupta", "manish.kumar@natwest.com",null,
                true, "+91", 1L, "Data & Analytics","Manish is a jovial guy", Collections.singletonList("Scala"),
                Collections.singletonList("OBDEF"), "8007464285", "manish.kumar.gupta@outlook.com",Collections.singletonList("UNKNOWN"),
                "https://static.vecteezy.com/system/resources/previews/014/194/219/large_2x/businessman-manager-boss-man-an-office-worker-illustration-flat-design-vector.jpg");

        userInfo.add(user);
        ManageUsersAndGroups.createUsers(userInfo);

    }

    @Test
    void createUsersMandatoryValues() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<Users> userInfo = new ArrayList<>();
        Users user = new Users( "ABC","Kumar",  "ABC@gmail.com");

        userInfo.add(user);
        ManageUsersAndGroups.createUsers(userInfo);

    }
}