package com.store.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private AddressDto address;
    private String creditCard;
    private String creditCardType;
}
