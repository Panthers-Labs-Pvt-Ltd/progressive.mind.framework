package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Users {
    public String Title;
    @NotNull
    public String FirstName;
    @NotNull
    public String LastName;
    public String DisplayName;
    @NotNull
    public String Email;
    public String Manager;
    public Boolean Active;
    public String CountryCode;
    public Long DepartmentId;
    public String DepartmentName;
    public String AboutMe;
    public List<String> Skills;
    public List<String> Teams;
    public String Phone;
    public String Slack;
    public List<String> Platform;
    public String PictureLink;
    public Users() {

    }

    public Users(String firstName, String lastName,String email) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;

    }

    public Users(String title, String firstName, String lastName, String displayName, String email,
                 String manager, Boolean active, String countryCode, Long departmentId,
                 String departmentName, String aboutMe, List<String> skills, List<String> teams,
                 String phone, String slack, List<String> platform, String PictureLink) {
        this.Title = title;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.DisplayName = displayName;
        this.Email = email;
        this.Manager = manager;
        this.Active = active;
        this.CountryCode = countryCode;
        this.DepartmentId = departmentId;
        this.DepartmentName = departmentName;
        this.AboutMe = aboutMe;
        this.Skills = skills;
        this.Teams = teams;
        this.Phone = phone;
        this.Slack = slack;
        this.Platform = platform;
        this.PictureLink = PictureLink;
    }
}
