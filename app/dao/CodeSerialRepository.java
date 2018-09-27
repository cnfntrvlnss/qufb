package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPACodeSerialRepository;


@ImplementedBy(JPACodeSerialRepository.class)
public interface CodeSerialRepository {
    String getCodeInfo(int ConfigID);
}
