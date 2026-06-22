package com.example.forum.web.front.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.example.forum.service.sitemap.model.SiteMapVo;
import com.example.forum.service.sitemap.service.SitemapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.nio.charset.Charset;

/**
 * 生成 sitemap.xml 册"robots.txt
 *
 * @author dev
 * @date 2023/2/13
 */
@RestController
public class SiteMapController {
    private XmlMapper xmlMapper = new XmlMapper();
    @Resource
    private SitemapService sitemapService;

    @RequestMapping(path = "/sitemap",
            produces = "application/xml;charset=utf-8")
    public SiteMapVo sitemap() {
        return sitemapService.getSiteMap();
    }

    @RequestMapping(path = "/sitemap.xml",
            produces = "text/xml")
    public byte[] sitemapXml() throws JsonProcessingException {
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        SiteMapVo vo = sitemapService.getSiteMap();
        String ans = xmlMapper.writeValueAsString(vo);
        ans = ans.replaceAll(" xmlns=\"\"", "");

        return ans.getBytes(Charset.defaultCharset());
    }

    @RequestMapping(path = "/robots.txt",
            produces = "text/plain;charset=utf-8")
    public String robotsTxt() {
        return sitemapService.getRobotsTxt();
    }

    @GetMapping(path = "/sitemap/refresh")
    public Boolean refresh() {
        sitemapService.refreshSitemap();
        return true;
    }
}

