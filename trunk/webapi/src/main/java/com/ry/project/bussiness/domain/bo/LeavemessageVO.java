package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 留言表
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@Accessors(chain = true)
public class LeavemessageVO extends Leavemessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String userFace;

    private List<LeavemessageVO> children = new ArrayList<LeavemessageVO>();
}
