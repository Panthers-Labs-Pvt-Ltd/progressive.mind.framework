package com.progressive.minds.chimera.examples.mapper;

import static com.progressive.minds.chimera.examples.mapper.ContractCustomerDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import com.progressive.minds.chimera.examples.model.ContractCustomer;
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
public interface ContractCustomerMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<ContractCustomer>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9146393+05:30", comments="Source Table: test.contract_customer")
    BasicColumn[] selectList = BasicColumn.columnList(contractId, id, name, email, createdAt, customerId, contractStartDate, contractEndDate, contractTerms);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="ContractCustomerResult", value = {
        @Result(column="contract_id", property="contractId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_at", property="createdAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="customer_id", property="customerId", jdbcType=JdbcType.INTEGER),
        @Result(column="contract_start_date", property="contractStartDate", jdbcType=JdbcType.DATE),
        @Result(column="contract_end_date", property="contractEndDate", jdbcType=JdbcType.DATE),
        @Result(column="contract_terms", property="contractTerms", jdbcType=JdbcType.VARCHAR)
    })
    List<ContractCustomer> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("ContractCustomerResult")
    Optional<ContractCustomer> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    default int deleteByPrimaryKey(Integer contractId_) {
        return delete(c -> 
            c.where(contractId, isEqualTo(contractId_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9140989+05:30", comments="Source Table: test.contract_customer")
    default int insert(ContractCustomer row) {
        return MyBatis3Utils.insert(this::insert, row, contractCustomer, c ->
            c.map(contractId).toProperty("contractId")
            .map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(email).toProperty("email")
            .map(createdAt).toProperty("createdAt")
            .map(customerId).toProperty("customerId")
            .map(contractStartDate).toProperty("contractStartDate")
            .map(contractEndDate).toProperty("contractEndDate")
            .map(contractTerms).toProperty("contractTerms")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9146393+05:30", comments="Source Table: test.contract_customer")
    default int insertMultiple(Collection<ContractCustomer> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, contractCustomer, c ->
            c.map(contractId).toProperty("contractId")
            .map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(email).toProperty("email")
            .map(createdAt).toProperty("createdAt")
            .map(customerId).toProperty("customerId")
            .map(contractStartDate).toProperty("contractStartDate")
            .map(contractEndDate).toProperty("contractEndDate")
            .map(contractTerms).toProperty("contractTerms")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9146393+05:30", comments="Source Table: test.contract_customer")
    default int insertSelective(ContractCustomer row) {
        return MyBatis3Utils.insert(this::insert, row, contractCustomer, c ->
            c.map(contractId).toPropertyWhenPresent("contractId", row::getContractId)
            .map(id).toPropertyWhenPresent("id", row::getId)
            .map(name).toPropertyWhenPresent("name", row::getName)
            .map(email).toPropertyWhenPresent("email", row::getEmail)
            .map(createdAt).toPropertyWhenPresent("createdAt", row::getCreatedAt)
            .map(customerId).toPropertyWhenPresent("customerId", row::getCustomerId)
            .map(contractStartDate).toPropertyWhenPresent("contractStartDate", row::getContractStartDate)
            .map(contractEndDate).toPropertyWhenPresent("contractEndDate", row::getContractEndDate)
            .map(contractTerms).toPropertyWhenPresent("contractTerms", row::getContractTerms)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    default Optional<ContractCustomer> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    default List<ContractCustomer> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    default List<ContractCustomer> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    default Optional<ContractCustomer> selectByPrimaryKey(Integer contractId_) {
        return selectOne(c ->
            c.where(contractId, isEqualTo(contractId_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, contractCustomer, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9152042+05:30", comments="Source Table: test.contract_customer")
    static UpdateDSL<UpdateModel> updateAllColumns(ContractCustomer row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(contractId).equalTo(row::getContractId)
                .set(id).equalTo(row::getId)
                .set(name).equalTo(row::getName)
                .set(email).equalTo(row::getEmail)
                .set(createdAt).equalTo(row::getCreatedAt)
                .set(customerId).equalTo(row::getCustomerId)
                .set(contractStartDate).equalTo(row::getContractStartDate)
                .set(contractEndDate).equalTo(row::getContractEndDate)
                .set(contractTerms).equalTo(row::getContractTerms);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.915732+05:30", comments="Source Table: test.contract_customer")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(ContractCustomer row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(contractId).equalToWhenPresent(row::getContractId)
                .set(id).equalToWhenPresent(row::getId)
                .set(name).equalToWhenPresent(row::getName)
                .set(email).equalToWhenPresent(row::getEmail)
                .set(createdAt).equalToWhenPresent(row::getCreatedAt)
                .set(customerId).equalToWhenPresent(row::getCustomerId)
                .set(contractStartDate).equalToWhenPresent(row::getContractStartDate)
                .set(contractEndDate).equalToWhenPresent(row::getContractEndDate)
                .set(contractTerms).equalToWhenPresent(row::getContractTerms);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9162583+05:30", comments="Source Table: test.contract_customer")
    default int updateByPrimaryKey(ContractCustomer row) {
        return update(c ->
            c.set(id).equalTo(row::getId)
            .set(name).equalTo(row::getName)
            .set(email).equalTo(row::getEmail)
            .set(createdAt).equalTo(row::getCreatedAt)
            .set(customerId).equalTo(row::getCustomerId)
            .set(contractStartDate).equalTo(row::getContractStartDate)
            .set(contractEndDate).equalTo(row::getContractEndDate)
            .set(contractTerms).equalTo(row::getContractTerms)
            .where(contractId, isEqualTo(row::getContractId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-04-03T20:51:16.9162583+05:30", comments="Source Table: test.contract_customer")
    default int updateByPrimaryKeySelective(ContractCustomer row) {
        return update(c ->
            c.set(id).equalToWhenPresent(row::getId)
            .set(name).equalToWhenPresent(row::getName)
            .set(email).equalToWhenPresent(row::getEmail)
            .set(createdAt).equalToWhenPresent(row::getCreatedAt)
            .set(customerId).equalToWhenPresent(row::getCustomerId)
            .set(contractStartDate).equalToWhenPresent(row::getContractStartDate)
            .set(contractEndDate).equalToWhenPresent(row::getContractEndDate)
            .set(contractTerms).equalToWhenPresent(row::getContractTerms)
            .where(contractId, isEqualTo(row::getContractId))
        );
    }
}