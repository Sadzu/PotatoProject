package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.cft.template.api.dto.remittance.RemittanceDto;
import ru.cft.template.core.entity.Remittance;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;

import java.util.List;

@Mapper
public interface RemittanceMapper {

    @Mapping(target = "owner", source = "owner", qualifiedByName = "mapUser")
    @Mapping(target = "recipientPhone", source = "recipient", qualifiedByName = "mapUser")
    @Mapping(target = "recipientWallet", source = "recipientWallet", qualifiedByName = "mapWallet")
    RemittanceDto map(Remittance remittance);

    List<RemittanceDto> map(List<Remittance> remittances);

    @Named("mapUser")
    default Long mapUser(User user) {
        if (user == null) return null;
        return user.getPhone();
    }

    @Named("mapWallet")
    default Long mapWallet(Wallet wallet) {
        if (wallet == null) return null;
        return wallet.getWalletId();
    }
}
