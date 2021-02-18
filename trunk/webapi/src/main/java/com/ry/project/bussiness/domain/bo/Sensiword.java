package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 敏感字枚举表
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Sensiword extends Model<Sensiword> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 敏感字类别 0未分类  1暴力  2色情  3反动
     */
    private Integer type;

    private String content;

    /**
     * 状态，0删除，1未删除
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
