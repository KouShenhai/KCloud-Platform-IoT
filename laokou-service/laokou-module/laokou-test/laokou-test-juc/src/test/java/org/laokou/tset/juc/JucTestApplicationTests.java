package org.laokou.tset.juc;

import java.time.LocalDate;
import java.time.Period;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JucTestApplicationTests {

    @Test
    void contextLoads() {
        LocalDate now = LocalDate.now();
        LocalDate localDate = LocalDate.of(2023, 5, 25);
        int days = Period.between(now, localDate).getDays();
        System.out.println(days);
    }

}
