package com.lsq.interview.data;

import com.lsq.interview.model.Funding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FundingRepository extends CrudRepository<Funding, String> {
  @Query("SELECT '*' FROM Funding WHERE supplierId=:supplierId")
  List<Funding> fetchFundingsForSupplier(@Param("supplierId") String supplierId);
}
