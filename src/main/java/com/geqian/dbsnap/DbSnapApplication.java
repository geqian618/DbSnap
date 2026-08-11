package com.geqian.dbsnap;

import com.geqian.dbsnap.annotation.PackageScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PackageScan({"com.geqian.dbsnap.entity", "com.geqian.dbsnap.properties"})
public class DbSnapApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbSnapApplication.class, args);
    }

}
