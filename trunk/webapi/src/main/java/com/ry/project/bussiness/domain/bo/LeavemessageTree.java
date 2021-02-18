package com.ry.project.bussiness.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ry.common.utils.UrlUtil;
import com.ry.common.utils.sensi.SensitiveFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author
 */
@Data
@Accessors(chain = true)
public class LeavemessageTree implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userName;
    private String userFace;
    private String replayId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<LeavemessageTree> children;

    public LeavemessageTree() {

    }

    public LeavemessageTree(LeavemessageVO message) {
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        this.id = UrlUtil.encrypt(message.getId());
        this.userName = message.getUserName();
        this.userFace = message.getUserFace();
        this.replayId = UrlUtil.encrypt(message.getReplyId());
        this.content = filter.filter(message.getContent(), '*');
        this.createTime = message.getCreateTime();
        this.children = message.getChildren().stream().map(LeavemessageTree::new).collect(Collectors.toList());
    }

}
