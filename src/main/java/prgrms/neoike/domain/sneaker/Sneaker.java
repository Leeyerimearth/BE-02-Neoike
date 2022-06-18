package prgrms.neoike.domain.sneaker;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import prgrms.neoike.domain.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "sneaker_id_name_code",
            columnNames = {"sneaker_id", "name", "code"}
        )
    }
)
public class Sneaker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "sneaker_id", nullable = false, updatable = false)
    private Long id;

    @Enumerated(value = STRING)
    @Column(name = "member_type", length = 10, nullable = false)
    private MemberType memberType;

    @Enumerated(value = STRING)
    @Column(name = "category", length = 10, nullable = false)
    private Category category;

    @Column(name = "name", length = 50, nullable = false, updatable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Embedded
    private SneakerCode sneakerCode;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate;

    @OneToMany(mappedBy = "sneaker", cascade = ALL)
    private final Set<SneakerImage> sneakerImages = new HashSet<>();

    @Builder
    public Sneaker(
        MemberType memberType,
        Category category,
        String name,
        int price,
        String description,
        SneakerCode sneakerCode,
        LocalDateTime releaseDate
    ) {
        this.memberType = memberType;
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.sneakerCode = sneakerCode;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("memberType", memberType)
            .append("category", category)
            .append("name", name)
            .append("price", price)
            .append("description", description)
            .append("sneakerCode", sneakerCode)
            .append("releaseDate", releaseDate)
            .toString();
    }
}