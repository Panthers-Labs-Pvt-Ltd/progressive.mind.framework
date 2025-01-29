package com.progressive.minds.chimera.core.datahub.users;

import com.linkedin.common.AuditStamp;
import com.linkedin.common.UrnArray;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringArray;
import com.linkedin.identity.*;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.common.genericUtils;
import com.progressive.minds.chimera.core.datahub.modal.Users;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.progressive.minds.chimera.core.datahub.Constants.*;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class ManageUsersAndGroups {
 static String DEFAULT_USER_PASSWORD = "12345";
 static String PROFILE = "https://static.vecteezy.com/system/resources/previews/014/194/219/large_2x/businessman-manager-boss-man-an-office-worker-illustration-flat-design-vector.jpg";
    public static boolean createUsers(List<Users> userInfo) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        for (Users info : userInfo) {

            Objects.requireNonNull(info.getEmail(), "User Email must not be null!");
            Objects.requireNonNull(info.getFirstName(), "FirstName must not be null!");
            Objects.requireNonNull(info.getLastName(), "LastName must not be null!");

            CorpUserInfo result = new CorpUserInfo();
            CorpuserUrn userUrn = CorpuserUrn.createFromString("urn:li:corpuser:"+info.getEmail());
            result.setActive(genericUtils.getOrElse(info.getActive(), true));
            result.setCountryCode(genericUtils.getOrElse(info.getCountryCode(), "-"));
            result.setDepartmentId(genericUtils.getOrElse(info.getDepartmentId(), 0L));
            result.setDepartmentName(genericUtils.getOrElse(info.getDepartmentName(), "-"));
            result.setEmail(genericUtils.getOrElse(info.getEmail(), ""));
            result.setDisplayName(genericUtils.getOrElse(info.getDisplayName(), info.getLastName() + ", " + info.getFirstName()));
            result.setFirstName(genericUtils.getOrElse(info.getFirstName(), "-"));
            result.setLastName(genericUtils.getOrElse(info.getLastName(), "-"));
            result.setFullName(info.getLastName() + info.getFirstName());
            result.setTitle(genericUtils.getOrElse(info.getTitle(), "Read Only User"));

            MetadataChangeProposal UserInfoProposal = createProposal(String.valueOf(userUrn), CORP_USER_ENTITY_NAME,
                    CORP_USER_INFO_ASPECT_NAME, ACTION_TYPE,result);
             emitProposal(UserInfoProposal, CORP_USER_ENTITY_NAME);

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn(userUrn.getUsernameEntity()))
                    .setTime(Instant.now().toEpochMilli());

            UrnArray platform = new UrnArray();
            if (info.getPlatform() != null)
            {
            info.getPlatform().forEach(platformNm ->
            {
                try {
                    platform.add(Urn.createFromString("urn:li:dataPlatform:"+platformNm));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
            }
            else
            {
                platform.add(Urn.createFromString(UNKNOWN_DATA_PLATFORM));
            }

            StringArray skills = new StringArray();
            if (info.getSkills() != null) skills.addAll(info.getSkills()); else skills.add("default");

            StringArray teams = new StringArray();
            if (info.getTeams() != null) teams.addAll(info.getTeams()); else teams.add("default");

            CorpUserEditableInfo corpUserEditableInfo = new CorpUserEditableInfo();
            corpUserEditableInfo.setAboutMe(genericUtils.getOrElse(info.getAboutMe(), "-"))
                    .setPhone(genericUtils.getOrElse(info.getPhone(), "-"))
                    .setPictureLink(new Url(genericUtils.getOrElse(info.getPictureLink(), PROFILE)))
                    .setPlatforms(platform)
                    .setSkills(skills)
                    .setSlack(genericUtils.getOrElse(info.getSlack(), "-"))
                    .setTeams(teams);

            MetadataChangeProposal UserEditableInfoProposal = createProposal(String.valueOf(userUrn), CORP_USER_ENTITY_NAME,
                    CORP_USER_EDITABLE_INFO_ASPECT_NAME, ACTION_TYPE,corpUserEditableInfo);
            emitProposal(UserEditableInfoProposal, CORP_USER_ENTITY_NAME);

            CorpUserStatus corpUserStatus = new CorpUserStatus();
            corpUserStatus.setStatus(CORP_USER_STATUS_ACTIVE);
            corpUserStatus.setLastModified(createdStamp);

            MetadataChangeProposal StatusProposal = createProposal(String.valueOf(userUrn), CORP_USER_ENTITY_NAME,
                    CORP_USER_STATUS_ASPECT_NAME, ACTION_TYPE,corpUserStatus);
            emitProposal(StatusProposal, CORP_USER_ENTITY_NAME);


            UrnArray roleMembershipArray =new UrnArray();
            roleMembershipArray.add(Urn.createFromString("urn:li:dataHubRole:Reader"));
            RoleMembership roleMembership =new RoleMembership();
            roleMembership.setRoles(roleMembershipArray);

            MetadataChangeProposal roleProposal = createProposal(String.valueOf(userUrn), CORP_USER_ENTITY_NAME,
                    ROLE_MEMBERSHIP_ASPECT_NAME, ACTION_TYPE,roleMembership);
            emitProposal(roleProposal, CORP_USER_ENTITY_NAME);
            SecretService _SecretService = new SecretService(DEFAULT_USER_PASSWORD);
            CorpUserCredentials corpUserCredentials = new CorpUserCredentials();
            final byte[] salt = SecretService.generateSalt(SALT_TOKEN_LENGTH);
            String encryptedSalt = SecretService.encrypt(Base64.getEncoder().encodeToString(salt));
            corpUserCredentials.setSalt(encryptedSalt);
            String hashedPassword = SecretService.getHashedPassword(salt, DEFAULT_USER_PASSWORD);
            corpUserCredentials.setHashedPassword(hashedPassword);

            MetadataChangeProposal CorpUserCredentialsProposal = createProposal(String.valueOf(userUrn), CORP_USER_ENTITY_NAME,
                    CORP_USER_CREDENTIALS_ASPECT_NAME, ACTION_TYPE,corpUserCredentials);
            emitProposal(CorpUserCredentialsProposal, CORP_USER_ENTITY_NAME);

   /*       InviteToken inviteToken = new InviteToken();
            inviteToken.setRole(Urn.createFromString("urn:li:dataHubRole:Reader"))
                    .setToken(info.Email);


            MetadataChangeProposal InviteProposal = createProposal(String.valueOf(userUrn), INVITE_TOKEN_ENTITY_NAME,
                    INVITE_TOKEN_ASPECT_NAME, ACTION_TYPE,inviteToken);
            emitProposal(InviteProposal, CORP_USER_ENTITY_NAME);*/
/*
            UrnArray NativeGroupMembershipArray =new UrnArray();
            NativeGroupMembershipArray.add("");
            NativeGroupMembership nativeGroupMembership = new NativeGroupMembership();
            nativeGroupMembership.setNativeGroups(NativeGroupMembershipArray);

            UrnArray GroupMembershipArray =new UrnArray();
            GroupMembership groupMembership = new GroupMembership();
            groupMembership.setGroups(GroupMembershipArray);

            CorpUserViewsSettings corpUserViewsSettings = new CorpUserViewsSettings();
            corpUserViewsSettings.setDefaultView(corpUserViewsSettings.getDefaultView());
            CorpUserSettings corpUserSettings = new CorpUserSettings();
            corpUserSettings.setViews(corpUserViewsSettings);*/

           /* if (info.Manager != null) {
            result.setManager(new CorpUser.Builder().setUrn(info.getManagerUrn().toString()).build());
        }*/
            // return result;
        }
         return false;
    }
}
