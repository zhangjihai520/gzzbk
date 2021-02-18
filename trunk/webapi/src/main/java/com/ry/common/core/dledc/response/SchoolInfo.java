package com.ry.common.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SchoolInfo {
    /// <summary>
    /// 机构
    /// </summary>
    @JSONField(name = "OrganizeId")
    private String OrganizeId;
    /// <summary>
    /// 父id
    /// </summary>
    @JSONField(name = "ParentId")
    private String ParentId;
    /// <summary>
    /// 年级code,1-12对应12个年级
    /// </summary>
    @JSONField(name = "ParentOrganize")
    private String ParentOrganize;

    @JSONField(name = "ParentOrganizeCode")
    private String ParentOrganizeCode;

    @JSONField(name = "AreaSchoolId")
    private String AreaSchoolId;

    @JSONField(name = "Layers")
    private Integer Layers;

    @JSONField(name = "OrganizeCode")
    private String OrganizeCode;

    @JSONField(name = "XXJGDM")
    private String XXJGDM;

    @JSONField(name = "SchoolName")
    private String SchoolName;

    @JSONField(name = "ShortName")
    private String ShortName;

    @JSONField(name = "CategoryId")
    private String CategoryId;

    @JSONField(name = "ManagerId")
    private String ManagerId;

    @JSONField(name = "GroupId")
    private String GroupId;

    @JSONField(name = "TermName")
    private String TermName;

    @JSONField(name = "TermType")
    private String TermType;

    @JSONField(name = "Phase")
    private String Phase;

    @JSONField(name = "PhaseSign")
    private String PhaseSign;

    @JSONField(name = "PhaseName")
    private String PhaseName;

    @JSONField(name = "DeleteMark")
    private Boolean DeleteMark;

    @JSONField(name = "EnabledMark")
    private Boolean EnabledMark;

    @JSONField(name = "Source")
    private Integer Source;

    @JSONField(name = "Status")
    private Integer Status;
}
