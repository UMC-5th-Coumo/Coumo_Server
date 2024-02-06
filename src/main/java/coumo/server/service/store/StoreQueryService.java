package coumo.server.service.store;

import coumo.server.domain.Menu;
import coumo.server.domain.Store;
import coumo.server.domain.StoreImage;
import coumo.server.domain.Timetable;
import coumo.server.domain.enums.StoreType;
import coumo.server.web.dto.StoreResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreQueryService {
    public Optional<Store> findStore(Long storeId);
    public Optional<List<Timetable>> findTimeTables(Long storeId);
    public Optional<List<Menu>> findMenus(Long storeId);
    public Optional<List<StoreImage>> findStoreImages(Long storeId);
    public List<StoreResponseDTO.StoreStampInfo> findFamousStore(double longitude, double latitude, double distance, Pageable pageable);
    public Page<Store> findNearestStore(double longitude, double latitude, double distance, Optional<String> category, Pageable pageable);
}
