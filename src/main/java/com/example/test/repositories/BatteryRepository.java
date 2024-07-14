package com.example.test.repositories;

import com.example.test.models.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Integer> {

    //query and method for getting all batteries in given range
    @Query("SELECT b FROM Battery b WHERE b.postcode BETWEEN :mincode AND :maxcode")
    List<Battery>findByPostcodeRange(@Param("mincode") String mincode, @Param("maxcode") String maxcode);
}
