package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.ChannelTarget;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import java.io.Serializable;
import java.util.*;

@Data
public class PlanChannelTargetResponse implements Serializable {
    private String brCode;
    private String bcCode;
    private String staffCode;
    private Long staffId;
    private Integer childStatus;
    private Map<String, ChannelTarget> channelTargetMap = new LinkedHashMap<>();
    private List<ChannelTarget> channels = new LinkedList<>();

    public void buildListChannel() {
        channels = new LinkedList<>();
        for (Map.Entry<String, ChannelTarget> entry : channelTargetMap.entrySet()) {
            ChannelTarget channelTarget = entry.getValue();
            if (channelTarget != null && (channelTarget.getTarget() != null && channelTarget.getTarget().hasData() ||
                    channelTarget.getPlan() != null && channelTarget.getPlan().hasData() ||
                    channelTarget.getResult() != null && channelTarget.getResult().hasData())) {
                channels.add(channelTarget);
            }
        }
        if (CollectionUtils.isNotEmpty(channels)){
            channels.sort(Comparator.comparing(ChannelTarget::getChannelTypeName4Sort));
        }
    }
}
