package bank.currency.infrastructure.db;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "currency")
@Table(name = "currency", indexes = {
        @Index(name = "code", columnList = "code", unique = true)
})
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @Column(precision = 10, scale = 4)
    private BigDecimal mid;

    private Date lastUpdated;

    private String source;
}

