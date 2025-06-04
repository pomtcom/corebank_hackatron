package com.corebank.hackatron.corebank_hackatron.repository;

import com.corebank.hackatron.corebank_hackatron.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Query("""
        SELECT 
            a.accountNumber AS accountNumber,
            a.accountType AS accountType,
            c.citizenId AS citizenId,
            c.firstName AS firstName,
            c.lastName AS lastName,
            c.mobile AS mobile
        FROM Account a
        JOIN Customer c ON a.citizenId = c.citizenId
        WHERE a.accountNumber = :accountNumber
    """)
    AccountCustomerProjection findAccountWithCustomerInfo(String accountNumber);
}