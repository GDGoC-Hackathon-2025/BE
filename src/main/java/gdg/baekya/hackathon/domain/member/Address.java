package gdg.baekya.hackathon.domain.member;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Address implements Serializable {


    private String streetAddress;

    private String detailAddress;

    private int zipcode;

    public static Address of(String streetAddress, String detailAddress, int zipcode) {
        return Address.builder()
                .streetAddress(streetAddress)
                .detailAddress(detailAddress)
                .zipcode(zipcode).build();
    }

}
