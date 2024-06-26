package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originFileName;
    private String storeFileName;

    private ImageType imageType;

    public Image(String originFileName, String storeFileName, ImageType imageType) {
        this.originFileName = originFileName;
        this.storeFileName = storeFileName;
        this.imageType = imageType;
    }

    public static Image of(String originalFilename, String storeFileName, ImageType type) {
        return new Image(originalFilename, storeFileName, type);
    }
}
