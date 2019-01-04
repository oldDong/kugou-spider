package com.dongzj.spider.controll;

import com.dongzj.spider.enums.TopListEnum;
import com.dongzj.spider.service.SpiderService;
import com.dongzj.spider.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 14:26
 */
@RequestMapping("/kugou")
@RestController
public class SpiderController {

    private static final Logger logger = LoggerFactory.getLogger(SpiderController.class);

    @Autowired
    private SpiderService spiderService;

    /**
     * 下载
     *
     * @param code 榜单编码
     * @param path 下载文件路径
     * @return
     */
    @GetMapping("/download")
    public ResultVo download(@RequestParam Integer code, @RequestParam String path) {
        try {
            return spiderService.download(code, path);
        } catch (Exception e) {
            logger.error("download error - msg:{}", e.getMessage());
            return ResultVo.fail(e.getMessage());
        }
    }

    /**
     * 获取所有榜单信息
     *
     * @return
     */
    @GetMapping("/rockets")
    public ResultVo listRockets() {
        List<Map<String, Object>> result = TopListEnum.listRockets();
        return ResultVo.ok(result);
    }
}
