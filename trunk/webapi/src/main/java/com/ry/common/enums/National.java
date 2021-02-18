package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum National {
    han(0, "汉族"),
    zhuang(1, "壮族"),
    man(2, "满族"),
    hui(3, "回族"),
    miao(4, "苗族"),
    wweiwuer(5, "维吾尔族"),
    tujia(6, "土家族"),
    yi(7, "彝族"),
    menggu(8, "蒙古族"),
    zang(9, "藏族"),
    buyi(10, "布依族"),
    dong(11, "侗族"),
    yao(12, "瑶族"),
    chaoxian(13, "朝鲜族"),
    bai(14, "白族"),
    hani(15, "哈尼族"),
    hasake(16, "哈萨克族"),
    li(17, "黎族"),
    dai(18, "傣族"),
    she(19, "畲族"),
    lili(20, "傈僳族"),
    gelao(21, "仡佬族"),
    dongxiang(22, "东乡族"),
    gaoshan(23, "高山族"),
    lahu(24, "拉祜族"),
    shui(25, "水族"),
    wa(26, "佤族"),
    naxi(27, "纳西族"),
    qiang(28, "羌族"),
    tu(29, "土族"),
    mulao(30, "仫佬族"),
    xibo(31, "锡伯族"),
    keerkezi(32, "柯尔克孜族"),
    dahaner(33, "达斡尔族"),
    jingpo(34, "景颇族"),
    maonan(35, "毛南族"),
    sala(36, "撒拉族"),
    bulang(37, "布朗族"),
    tajike(38, "塔吉克族"),
    achang(39, "阿昌族"),
    pumi(40, "普米族"),
    ewenke(41, "鄂温克族"),
    nu(42, "怒族"),
    jing(43, "京族"),
    jinuo(44, "基诺族"),
    deang(45, "德昂族"),
    baoan(46, "保安族"),
    eluosi(47, "俄罗斯族"),
    yugu(48, "裕固族"),
    wuzibieke(49, "乌孜别克族"),
    menba(50, "门巴族"),
    elunchun(51, "鄂伦春族"),
    dulong(52, "独龙族"),
    tata(53, "塔塔尔族"),
    hezhe(54, "赫哲族"),
    luoba(55, "珞巴族"),
    other(56, "其他民族");

    private final Integer id;
    private final String name;

    National(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * @param id
     * @return
     */
    public static String getNameById(Integer id) {
        var national = Arrays.asList(National.values()).stream()
                .filter(x -> x.getId().equals(id))
                .findFirst().orElse(null);
        if (national != null) {
            return national.getName();
        } else {
            return National.other.getName();
        }
    }
}
