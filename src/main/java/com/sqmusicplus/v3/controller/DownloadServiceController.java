package com.sqmusicplus.v3.controller;

import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.vo.ParserEntity;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.AjaxResult;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.download.vo.DownlaodParserUrl;
import com.sqmusicplus.v3.download.vo.ParserTextParam;
import com.sqmusicplus.v3.parser.TextMusicPlayListParser;
import com.sqmusicplus.v3.parser.UrlMusicPlayListParser;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.utils.MusicUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Classname DownloadServiceController
 * @Description 下载到服务器控制接口
 * @Version 1.0.0
 * @Date 2025/7/25 10:29
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/api/download")
public class DownloadServiceController {

    @Autowired
    List<SearchHanderAbstract> searchHanderAbstractList;

    @Autowired
    private DownloadInfoService downloadInfoService;


    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;

    /**
     * 下载单曲
     * <p>
     * 音质选择优先级：<br>
     * 1. brType（手动指定码率）<br>
     * 2. downloadFormat（参数中指定格式，取该格式最大音质）<br>
     * 3. SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT（全局配置的格式，取该格式最大音质）<br>
     * 4. brTypes 列表中取最大音质（原有逻辑）
     * @param downloadSongParam
     * @return
     */
    @PostMapping("/downloadSong")
   public AjaxResult downloadSong(@RequestBody PlugDownloadSongParam downloadSongParam) {
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(downloadSongParam.getPlugName(), searchHanderAbstractList);
        List<PlugBrType> brTypes = downloadSongParam.getBrTypes();

        PlugBrType targetBr = null;

        // 优先级1: 手动指定码率
        if (downloadSongParam.getBrType() != null) {
            targetBr = downloadSongParam.getBrType();
        }
        // 优先级2: 参数中指定下载格式
        else if (StringUtils.isNotBlank(downloadSongParam.getDownloadFormat())) {
            targetBr = PlugBrType.findMaxByTypeAndPlugNameAndBits(downloadSongParam.getDownloadFormat(), downloadSongParam.getPlugName(),brTypes);
        }
        // 优先级3: 全局配置的下载格式（含 "auto" 由方法内部处理）
        else {
            String configFormat = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT);
            if (StringUtils.isNotBlank(configFormat)) {
                targetBr = PlugBrType.findMaxByTypeAndPlugNameAndBits(configFormat, downloadSongParam.getPlugName(),brTypes);
            }
        }

        // 优先级4: 从 brTypes 列表中取最大音质（原有逻辑兜底）
        if (targetBr == null) {
            if (brTypes == null || brTypes.isEmpty()) {
                throw new SQException("未找到可供下载的bit");
            }
            targetBr = MusicUtils.getMaxBr(brTypes);
        }

        DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(downloadSongParam, targetBr, false);
        Boolean add = downloadInfoService.add(downloadInfo);
        if (add){
            return AjaxResult.success("下载成功",downloadInfo);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载歌手的全部专辑
     * <p>
     * 音质选择优先级：<br>
     * 1. bit（手动指定码率）<br>
     * 2. downloadFormat（参数中指定格式，取该格式最大音质）<br>
     * 3. SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT（全局配置的格式，取该格式最大音质）<br>
     * 4. 不指定（由插件内部兜底）
     * @param plugDownloadArtisParam
     * @return
     */
    @PostMapping("/downloadArtistAlbum")
    public AjaxResult downloadArtistAlbum(@RequestBody PlugDownloadArtisParam plugDownloadArtisParam) {
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugDownloadArtisParam.getPlugName(), searchHanderAbstractList);
        PlugBrType maxBr= null;
        if (plugDownloadArtisParam.getBit()!=null){
            maxBr = PlugBrType.findByPlugNameAndBit(plugDownloadArtisParam.getPlugName(), plugDownloadArtisParam.getBit());
        }
//        else {
//            maxBr = resolveBrByFormat(plugDownloadArtisParam.getPlugName(), plugDownloadArtisParam.getDownloadFormat());
//        }
        List<DownloadInfo> downloadInfos = plugHander.downloadArtistAllAlbum(plugDownloadArtisParam.getArtistid(), maxBr);
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载专辑
     * <p>
     * 音质选择优先级：<br>
     * 1. bit（手动指定码率）<br>
     * 2. downloadFormat（参数中指定格式，取该格式最大音质）<br>
     * 3. SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT（全局配置的格式，取该格式最大音质）<br>
     * 4. 不指定（由插件内部兜底）
     * @param plugDownloadAlbumParam
     * @return
     */
    @PostMapping("/downloadAlbum")
    public AjaxResult downloadAlbum(@RequestBody PlugDownloadAlbumParam plugDownloadAlbumParam){
        SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugDownloadAlbumParam.getPlugName(), searchHanderAbstractList);
        PlugBrType maxBr= null;
        if (plugDownloadAlbumParam.getBit()!=null){
            maxBr = PlugBrType.findByPlugNameAndBit(plugDownloadAlbumParam.getPlugName(), plugDownloadAlbumParam.getBit());
        } else {
//            maxBr = resolveBrByFormat(plugDownloadAlbumParam.getPlugName(), plugDownloadAlbumParam.getDownloadFormat());
        }
        List<String> artistNameList = null;
        String albumid = plugDownloadAlbumParam.getAlbumid();
        if (StringUtils.isNotBlank(plugDownloadAlbumParam.getArtistName())){
            String[] split = plugDownloadAlbumParam.getArtistName().split("&");
            artistNameList= new ArrayList<>();
            for (String s : split) {
                artistNameList.add(s.trim());
            }
        }
        ArrayList<DownloadInfo> downloadInfos = plugHander.downloadAlbum(albumid, maxBr, artistNameList, false, plugDownloadAlbumParam.getAlbumName());
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载解析的URL歌曲
     * <p>
     * 音质选择：使用 DownlaodParserUrl.downloadFormat 或全局配置
     * @param downlaodParserUrl 解析的URL
     * @return
     */
    @PostMapping("/downloadParserUrl")
    public AjaxResult downloadParserUrl(@RequestBody DownlaodParserUrl downlaodParserUrl) {
        try {
            List<Music> parser = urlMusicPlayListParser.parser(downlaodParserUrl);
            if (parser == null){
                return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");
            }
            ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
            for (Music music : parser) {
                SearchHanderAbstract plugHander = MusicUtils.getPlugHander(music.getPlugName(), searchHanderAbstractList);
//                PlugBrType brType = resolveBrByFormat(music.getPlugName(), downlaodParserUrl.getDownloadFormat());
                DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(music, null, false);
                downloadInfos.add(downloadInfo);
            }
            Boolean add = downloadInfoService.add(downloadInfos);
            if (add){
                return AjaxResult.success("下载成功",downloadInfos);
            }

        } catch (Exception e) {
            log.error("解析失败",e);
            return AjaxResult.error("解析失败 仅支持qq 酷我 酷狗概念 网易云");
        }
        return AjaxResult.error("下载失败");
    }


    /**
     * 下载解析的URL歌曲（替代解析方法）
     * <p>
     * 音质选择：使用全局配置 SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT
     * @param musicList
     * @return
     */
    @PostMapping("/downloadParserUrlResult")
    public AjaxResult downloadParserUrlResult(@RequestBody List<Music> musicList) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Music music : musicList) {
            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(music.getPlugName(), searchHanderAbstractList);
//            PlugBrType brType = resolveBrByFormat(music.getPlugName(), null);
            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(music, null, false);
            downloadInfos.add(downloadInfo);
        }
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 下载解析的文本歌曲(替代解析方法)
     * <p>
     * 音质选择：使用 ParserTextParam.downloadFormat 或全局配置
     * @param param
     * @return
     */
    @PostMapping("/downloadParserText")
    public AjaxResult downloadParserText(@RequestBody ParserTextParam param) {
        if (StringUtils.isBlank(param.getText())) {
            return AjaxResult.error("请输入要解析的文本");
        }
        Thread thread = new Thread(() -> {
            try {
                List<ParserEntity> parser = textMusicPlayListParser.parser(param.getText());
                List<ParserEntity> parserEntities = textMusicPlayListParser.parserParserEntity(parser);

                if (parserEntities != null) {
                    try {
                        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                        for (ParserEntity parserEntity : parserEntities) {
                            PlugSearchMusicResult plugSearchMusicResult = parserEntity.getPlugSearchMusicResult();
                            if (StringUtils.isBlank(plugSearchMusicResult.getPlugName())){
                                continue;
                            }
                            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugSearchMusicResult.getPlugName(), searchHanderAbstractList);
//                            PlugBrType brType = resolveBrByFormat(plugSearchMusicResult.getPlugName(), param.getDownloadFormat());
                            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(plugSearchMusicResult, null, false);
                            downloadInfos.add(downloadInfo);
                        }
                        downloadInfoService.add(downloadInfos);
                    } catch (Exception ignored) {

                    }
                }
            } catch (Exception e) {
                log.error("解析失败", e);
            }
        });
        thread.start();
        return AjaxResult.success("开始解析并下载，稍后在下载中查看！（每首识别大致需要500毫秒耐心等待）");
    }

    /**
     * 批量下载解析的文本歌曲 替代解析方法
     * <p>
     * 音质选择：使用全局配置 SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT
     * @param parserEntities
     * @return
     */
    @PostMapping("/downloadParserTextResult")
    public AjaxResult downloadParserTextResult(@RequestBody List<ParserEntity> parserEntities) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (ParserEntity parserEntity : parserEntities) {
            PlugSearchMusicResult plugSearchMusicResult = parserEntity.getPlugSearchMusicResult();
            SearchHanderAbstract plugHander = MusicUtils.getPlugHander(plugSearchMusicResult.getPlugName(), searchHanderAbstractList);
//            PlugBrType brType = resolveBrByFormat(plugSearchMusicResult.getPlugName(), null);
            DownloadInfo downloadInfo = plugHander.musicToDownloadInfo(plugSearchMusicResult, null, false);
            downloadInfos.add(downloadInfo);
        }
        Boolean add = downloadInfoService.add(downloadInfos);
        if (add){
            return AjaxResult.success("下载成功",downloadInfos);
        }
        return AjaxResult.error("下载失败");
    }

    /**
     * 根据下载格式参数和全局配置，解析目标音质
     * @param plugName       插件名称（如 kw/qq/netease）
     * @param downloadFormat 请求参数中的格式（可为 null）
     * @return 匹配到的最大音质 PlugBrType，无匹配则 null
     */
    private PlugBrType resolveBrByFormat(String plugName, String downloadFormat) {
        // 1. 参数中指定的 downloadFormat
        if (StringUtils.isNotBlank(downloadFormat)) {
            PlugBrType found = PlugBrType.findMaxByTypeAndPlugName(downloadFormat, plugName);
            if (found != null) return found;
        }
        // 2. 全局配置 SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT（含 "auto" 由方法内部处理）
        String configFormat = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT);
        if (StringUtils.isNotBlank(configFormat)) {
            return PlugBrType.findMaxByTypeAndPlugName(configFormat, plugName);
        }
        return null;
    }


}
