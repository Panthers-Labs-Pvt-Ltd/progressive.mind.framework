package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class UserProfileDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8297194+05:30", comments="Source Table: test.USER_PROFILE")
    public static final UserProfile userProfile = new UserProfile();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8297194+05:30", comments="Source field: test.USER_PROFILE.id")
    public static final SqlColumn<Long> id = userProfile.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8297194+05:30", comments="Source field: test.USER_PROFILE.name")
    public static final SqlColumn<String> name = userProfile.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8297194+05:30", comments="Source field: test.USER_PROFILE.created_at")
    public static final SqlColumn<Date> createdAt = userProfile.createdAt;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8297194+05:30", comments="Source Table: test.USER_PROFILE")
    public static final class UserProfile extends AliasableSqlTable<UserProfile> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<Date> createdAt = column("created_at", JDBCType.TIMESTAMP);

        public UserProfile() {
            super("\"test\".\"USER_PROFILE\"", UserProfile::new);
        }
    }
}