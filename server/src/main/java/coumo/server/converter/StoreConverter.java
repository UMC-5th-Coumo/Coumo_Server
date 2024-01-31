package coumo.server.converter;

import coumo.server.domain.Store;
import coumo.server.web.dto.StoreResponseDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StoreConverter {
    public static StoreResponseDTO.StoreBasicDTO toResultBasicDTO(Store store){
        StoreResponseDTO.StoreBasicDTO storeBasicDTO = StoreResponseDTO.StoreBasicDTO.builder()
                                                            .name(store.getName())
                                                            .telePhone(store.getTelephone())
                                                            .longitude(String.valueOf(store.getPoint().getX()))
                                                            .latitude(String.valueOf(store.getPoint().getY()))
                                                            .time(new ArrayList<>())
                                                            .location(store.getStoreLocation())
                                                            .category(store.getStoreType().toString())
                                                            .build();
        store.getTimetableList().stream()
                .map(item -> storeBasicDTO.getTime()
                        .add(new StoreResponseDTO.TimeInfo(item.getDay(), item.getStartTime(), item.getEndTime())))
                .collect(Collectors.toList());

        return storeBasicDTO;
    }

    public static StoreResponseDTO.StoreDetailDTO toResultDetailDTO(Store store){
        StoreResponseDTO.StoreDetailDTO storeDetailDTO = StoreResponseDTO.StoreDetailDTO.builder()
                                                                .storeImages(new ArrayList<>())
                                                                .menus(new ArrayList<>())
                                                                .description(store.getStoreDescription())
                                                                .build();
        store.getStoreImageList().stream().
                map(item -> storeDetailDTO.getStoreImages().add(item.getStoreImage()))
                .collect(Collectors.toList());

        store.getMenuList().stream().
                map(item -> storeDetailDTO.getMenus()
                        .add(new StoreResponseDTO.MenuInfo(item.getName(), item.getMenuImage(), item.getMenuDescription(), item.getIsNew())))
                .collect(Collectors.toList());
        return storeDetailDTO;
    }

    public static List<StoreResponseDTO.FamousStoreDTO> toFamousStoreDTO(List<StoreResponseDTO.StoreStampInfo> storeStampInfoList){
        if(storeStampInfoList.isEmpty()) return Collections.emptyList();

        List<StoreResponseDTO.FamousStoreDTO> resultList = new ArrayList<>();
        storeStampInfoList.stream()
                .map(item -> resultList.add(StoreResponseDTO.FamousStoreDTO.builder()
                        .storeId(item.getStore().getId())
                        .name(item.getStore().getName())
                        .description(item.getStore().getStoreDescription())
                        .location(item.getStore().getStoreLocation())
                        .storeImage(item.getStore().getStoreImageList().get(0).getStoreImage())
                        .build()))
                .collect(Collectors.toList());

        return resultList;
    }
}
