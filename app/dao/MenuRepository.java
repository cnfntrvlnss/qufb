package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAMenuRepository;
import models.Menu;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAMenuRepository.class)
public interface MenuRepository {
    CompletionStage<List<Menu>> listMenu();
}
