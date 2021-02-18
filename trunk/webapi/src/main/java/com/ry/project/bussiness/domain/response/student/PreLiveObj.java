package com.ry.project.bussiness.domain.response.student;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2020-06-01
 */
@Data
@Accessors(chain = true)
public class PreLiveObj {

    private String liveDate;

    private List<PreLiveItem> liveList;

    public PreLiveObj() {
        liveList = new ArrayList<>();
    }

}
