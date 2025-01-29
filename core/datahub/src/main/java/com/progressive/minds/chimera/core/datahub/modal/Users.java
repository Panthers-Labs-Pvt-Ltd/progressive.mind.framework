package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {
    private String title;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String displayName;
    @NotNull
    private String email;
    private String manager;
    private Boolean active;
    private String countryCode;
    private Long departmentId;
    private String departmentName;
    private String aboutMe;
    private List<String> skills;
    private List<String> teams;
    private String phone;
    private String slack;
    private List<String> platform;
    private String pictureLink;
    public Users() {

    }

    public Users(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Users(String aboutMe, String title, String firstName, String lastName, String displayName, String email,
                 String manager, Boolean active, String countryCode, Long departmentId, String departmentName,
                 List<String> skills, List<String> teams, String phone, String slack, List<String> platform,
                 String pictureLink) {
        this.aboutMe = aboutMe;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.email = email;
        this.manager = manager;
        this.active = active;
        this.countryCode = countryCode;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.skills = skills;
        this.teams = teams;
        this.phone = phone;
        this.slack = slack;
        this.platform = platform;
        this.pictureLink = pictureLink;
    }
}
