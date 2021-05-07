package com.bitozen.zencamp.backend.svc.assembler;

import java.util.List;
import java.util.Set;

public interface IObjectAssembler<X,Y> {
    Y toDTO(X domainObject);
    X toDomain(Y dtoObject);
    List<Y> toDTOs(Set<X> domainObjects);
    List<Y> toDTOs(List<X> domainObjects);
    List<X> toDomains(List<Y> dtoObjects);
}
