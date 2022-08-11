package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PinnedListResponse {

    List<PinnedResponse> pinnedFolders;

    public static PinnedListResponse fromEntity(List<Folder> folders) {
        return PinnedListResponse.builder()
            .pinnedFolders(
                folders.stream().map(PinnedResponse::fromEntity).collect(Collectors.toList())
            )
            .build();
    }
}
