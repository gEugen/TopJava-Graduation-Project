package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.Address;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface AddressRepository extends BaseRepository<Address> {

    Address getAddressByCityAndStreetAndBuildingNumber(String city, String street, int buildingNumber);
}