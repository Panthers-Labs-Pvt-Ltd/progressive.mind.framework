package com.progressive.minds.chimera.mapper;

import static com.progressive.minds.chimera.mapper.UserProfileDynamicSqlSupport.*;

import com.progressive.minds.chimera.model.UserProfile;
import jakarta.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface UserProfileMapper
    extends CommonCountMapper,
        CommonDeleteMapper,
        CommonInsertMapper<UserProfile>,
        CommonUpdateMapper {
  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4023513+05:30",
      comments = "Source Table: test.USER_PROFILE")
  BasicColumn[] selectList = BasicColumn.columnList(id, name, createdAt);

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.3973492+05:30",
      comments = "Source Table: test.USER_PROFILE")
  @SelectProvider(type = SqlProviderAdapter.class, method = "select")
  @Results(
      id = "UserProfileResult",
      value = {
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT),
        @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
        @Result(column = "created_at", property = "createdAt", jdbcType = JdbcType.TIMESTAMP)
      })
  List<UserProfile> selectMany(SelectStatementProvider selectStatement);

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.3983506+05:30",
      comments = "Source Table: test.USER_PROFILE")
  @SelectProvider(type = SqlProviderAdapter.class, method = "select")
  @ResultMap("UserProfileResult")
  Optional<UserProfile> selectOne(SelectStatementProvider selectStatement);

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.3983506+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default long count(CountDSLCompleter completer) {
    return MyBatis3Utils.countFrom(this::count, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.3993501+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default int delete(DeleteDSLCompleter completer) {
    return MyBatis3Utils.deleteFrom(this::delete, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4003478+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default int insert(UserProfile row) {
    return MyBatis3Utils.insert(
        this::insert,
        row,
        userProfile,
        c ->
            c.map(id)
                .toProperty("id")
                .map(name)
                .toProperty("name")
                .map(createdAt)
                .toProperty("createdAt"));
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4013517+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default int insertMultiple(Collection<UserProfile> records) {
    return MyBatis3Utils.insertMultiple(
        this::insertMultiple,
        records,
        userProfile,
        c ->
            c.map(id)
                .toProperty("id")
                .map(name)
                .toProperty("name")
                .map(createdAt)
                .toProperty("createdAt"));
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4023513+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default int insertSelective(UserProfile row) {
    return MyBatis3Utils.insert(
        this::insert,
        row,
        userProfile,
        c ->
            c.map(id)
                .toPropertyWhenPresent("id", row::getId)
                .map(name)
                .toPropertyWhenPresent("name", row::getName)
                .map(createdAt)
                .toPropertyWhenPresent("createdAt", row::getCreatedAt));
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4033489+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default Optional<UserProfile> selectOne(SelectDSLCompleter completer) {
    return MyBatis3Utils.selectOne(this::selectOne, selectList, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4033489+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default List<UserProfile> select(SelectDSLCompleter completer) {
    return MyBatis3Utils.selectList(this::selectMany, selectList, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4033489+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default List<UserProfile> selectDistinct(SelectDSLCompleter completer) {
    return MyBatis3Utils.selectDistinct(this::selectMany, selectList, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4043481+05:30",
      comments = "Source Table: test.USER_PROFILE")
  default int update(UpdateDSLCompleter completer) {
    return MyBatis3Utils.update(this::update, userProfile, completer);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4043481+05:30",
      comments = "Source Table: test.USER_PROFILE")
  static UpdateDSL<UpdateModel> updateAllColumns(UserProfile row, UpdateDSL<UpdateModel> dsl) {
    return dsl.set(id)
        .equalTo(row::getId)
        .set(name)
        .equalTo(row::getName)
        .set(createdAt)
        .equalTo(row::getCreatedAt);
  }

  @Generated(
      value = "org.mybatis.generator.api.MyBatisGenerator",
      date = "2025-03-31T00:48:42.4043481+05:30",
      comments = "Source Table: test.USER_PROFILE")
  static UpdateDSL<UpdateModel> updateSelectiveColumns(
      UserProfile row, UpdateDSL<UpdateModel> dsl) {
    return dsl.set(id)
        .equalToWhenPresent(row::getId)
        .set(name)
        .equalToWhenPresent(row::getName)
        .set(createdAt)
        .equalToWhenPresent(row::getCreatedAt);
  }
}