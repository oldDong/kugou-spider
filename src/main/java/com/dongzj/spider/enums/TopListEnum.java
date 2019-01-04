package com.dongzj.spider.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 13:42
 */
@Getter
@AllArgsConstructor
public enum TopListEnum {
    SKY_ROCKET(1, "酷狗飙升榜", "https://www.kugou.com/yy/rank/home/PAGE-6666.html?from=rank", 5),
    TOP_500(2, "酷狗TOP500", "https://www.kugou.com/yy/rank/home/PAGE-8888.html?from=rank", 23),
    NET_HOT(3, "网络红歌榜", "https://www.kugou.com/yy/rank/home/PAGE-23784.html?from=rank", 23),
    KUGOU_SHARE(4, "酷狗分享榜", "https://www.kugou.com/yy/rank/home/PAGE-21101.html?from=rank", 5),
    PURE_MUSIC(5, "纯音乐榜", "https://www.kugou.com/yy/rank/home/PAGE-33164.html?from=rank", 5),
    AMERICA(6, "美国BillBoard榜", "https://www.kugou.com/yy/rank/home/PAGE-4681.html?from=rank", 5),
    UK(7, "英国单曲榜", "https://www.kugou.com/yy/rank/home/PAGE-4680.html?from=rank", 2),
    KOREA(8, "韩国M-net音乐榜", "https://www.kugou.com/yy/rank/home/PAGE-4672.html?from=rank", 3),
    CHINA_TOP(9, "中国TOP排行榜", "https://www.kugou.com/yy/rank/home/PAGE-22163.html?from=rank", 1);

    /**
     * 编码
     */
    private Integer code;

    /**
     * 榜单名称
     */
    private String name;

    /**
     * 榜单链接
     */
    private String url;

    /**
     * 榜单页数（每页22首）
     */
    private Integer length;

    public static TopListEnum getRocketByCode(Integer code) {
        TopListEnum[] listEnums = TopListEnum.values();
        for (TopListEnum en : listEnums) {
            if (en.code.equals(code)) {
                return en;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> listRockets() {
        TopListEnum[] listEnums = TopListEnum.values();
        List<Map<String, Object>> result = new ArrayList<>(listEnums.length);
        for (TopListEnum en : listEnums) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", en.name);
            map.put("value", en.code);
            result.add(map);
        }
        return result;
    }
}
