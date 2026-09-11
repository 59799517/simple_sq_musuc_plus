package com.sqmusicplus.v3.utils;

import cn.hutool.core.img.ImgUtil;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.Album;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.flac.FlacFileWriter;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp3.MP3FileWriter;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.OggFileWriter;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.Artwork;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Classname MusicUtils
 * @Description 音乐工具类
 * @Version 1.0.0
 * @Date 2022/6/1 15:43
 * @Created by SQ
 */

@Slf4j
public class MusicUtils {


    public static final String KEY_ARTIST_DIR = "artistDir"; // 歌手目录
    public static final String KEY_ALBUM_DIR = "albumDir";   // 专辑目录
    public static final String KEY_SONG_DIR = "songDir";     // 歌曲所在目录 (完整目录)

    /**
     * 获取文件内容
     *
     * @param file
     * @return
     */
    public static MultimediaInfo getMediaFileInfo(File file) {
        MultimediaObject multimediaObject = new MultimediaObject(file);
        try {
            return multimediaObject.getInfo();
        } catch (EncoderException e) {
            log.error("获取文件信息失败：{}", e.getMessage());
            return null;
        }
    }

    public static  synchronized MultimediaInfo setMediaFileInfo
            (File file,
             String title,
             String albumName,
             String artists,
             String comment,
             String lyrics,
             File image,
             String mainArtist,
             String  albumYear,
             Integer cd,
             Integer track,
             String tags
             ) {
        try {
            if (StringUtils.isBlank(mainArtist)){
                mainArtist="群星";
            }
            AudioFile af =null;
            String s = FileTypeUtils.checkType(file);
            if (s.contains("flac")){
                FlacFileReader flacFileReader = new FlacFileReader();
                af = flacFileReader.read(file);
            }else if (s.contains("wma")||s.contains("wav")||s.contains("ape")){
                return null;
            }else if (s.contains("ogg")){
                OggFileReader oggFileReader = new OggFileReader();
                af = oggFileReader.read(file);

            }else {
                MP3FileReader mp3FileReader = new MP3FileReader();
                af = mp3FileReader.read(file);
            }

//            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();
//            if (tag instanceof ID3v1Tag) {
//                tag = new ID3v24Tag();
//            }
            if (image != null && image.exists()) {
                try {
                    BufferedImage bufferedImage = ImageIOUtils.read(image);
                    if (bufferedImage == null){
                        return null;
                    }
                    ImgUtil.write(bufferedImage, image);
                    Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                    tag.setField(firstArtwork);
                } catch (Exception e) {
                    try {
                        Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                        tag.setField(firstArtwork);
                    }  catch (Exception fex) {
                        BufferedImage bufferedImage = ImageIOUtils.read(image);
                        if (bufferedImage == null){
                            return null;
                        }
                        ImgUtil.write(bufferedImage, image);
                        Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                        tag.setField(firstArtwork);
                    }catch (Error exc) {
                        System.out.println("Caught an error: " + exc.getMessage());
                    }
                }
            }
            tag.setField(FieldKey.TITLE, title.trim());
            tag.setField(FieldKey.ALBUM, albumName.trim());
            tag.setField(FieldKey.ARTIST, artists.trim());
            tag.setField(FieldKey.COMMENT, comment.trim());
            tag.setField(FieldKey.ALBUM_ARTIST, mainArtist.trim());
            tag.setField(FieldKey.YEAR, albumYear);
            if (StringUtils.isNotBlank(tags)){
                tag.setField(FieldKey.GENRE,tags);
            }
            if (cd!=null&&cd!=0){
                tag.setField(FieldKey.DISC_NO,cd.toString());
            }
            if (track!=null&&track!=0){
                tag.setField(FieldKey.TRACK,track.toString());
            }
            if (StringUtils.isNotEmpty(lyrics)) {
                try {
                    tag.setField(FieldKey.LYRICS, lyrics);
                } catch (KeyNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (FieldDataInvalidException e) {
                    throw new RuntimeException(e);
                }
            }

            af.setTag(tag);
//            af.commit();
//            AudioFileIO.write(af);


            if (s.contains("flac")){
                FlacFileWriter flacFileWriter = new FlacFileWriter();
                flacFileWriter.write(af);
            }else if (s.contains("ogg")){
                OggFileWriter oggFileWriter = new OggFileWriter();
                oggFileWriter.write(af);

            }else {
                MP3FileWriter mp3FileWriter = new MP3FileWriter();
                mp3FileWriter.write(af);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置音频标签失败: {}", e.getMessage(), e);
            throw new RuntimeException("设置音频标签失败: " + file.getAbsolutePath(), e);
        }
    }


    /**
     * 获取插件
     * @param plugName 插件名称
     * @return
     */
    public static SearchHanderAbstract getPlugHander(String plugName,List<SearchHanderAbstract> searchHanderAbstractList){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(plugName)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            throw  new SQException("未知的搜索类型");
        }
        return searchHanderAbstract;
    }


    /**
     * 找到最大的bit
     * @param brTypes
     * @return
     */
    public static PlugBrType getMaxBr(List<PlugBrType> brTypes) {
        return brTypes.stream().max(Comparator.comparing(PlugBrType::getBit)).get();
    }


    // 变量分类配置
    private static final Set<String> ARTIST_VARS = Set.of("artists", "artist", "artistsId");
    private static final Set<String> ALBUM_VARS = Set.of("album", "albumId");
// ================== 公共入口方法 ==================

    /**
     * 解析路径模板
     *
     * @param template         主路径模板 (例如: "${artists}/${album}/${musicName}")
     * @param fallbackTemplate 备用模板 (主模板渲染失败时使用，可为 null)
     * @param pathTemplate     模板参数 Map (用于渲染)
     * @param separator        文件分隔符 (建议传入 File.separator)
     * @return 包含 artistDir, albumDir, songDir 的 HashMap，未找到则值为 null
     */
    // ================== 公共入口方法 ==================

    public static Map<String, String> parse(String template, String fallbackTemplate,
                                            Map<String, Object> pathTemplate, String separator) {

        Map<String, String> result = new HashMap<>();
        result.put(KEY_ARTIST_DIR, null);
        result.put(KEY_ALBUM_DIR, null);
        result.put(KEY_SONG_DIR, null);

        if (template == null || template.isEmpty()) return result;

        String sep = (separator == null) ? File.separator : separator;

        // 1. 归一化分隔符
        String activeTemplate = normalizeSeparator(template, sep);
        String safeFallback = (fallbackTemplate != null) ? normalizeSeparator(fallbackTemplate, sep) : null;

        // 2. 选择可用模板
        String finalTemplate = selectWorkingTemplate(activeTemplate, safeFallback, pathTemplate);
        if (finalTemplate == null) return result;

        // 3. 提取目录部分
        String dirTemplate = extractDirectoryPart(finalTemplate, sep);
        if (dirTemplate.isEmpty()) return result;

        // 4. 拆分路径段
        List<String> segments = splitAndClean(dirTemplate, sep);
        if (segments.isEmpty()) return result;

        // ================== 核心修复逻辑：范围截取 ==================

        // 5. 找到关键变量最后一次出现的索引
        int lastArtistIdx = -1;
        int lastAlbumIdx = -1;

        for (int i = 0; i < segments.size(); i++) {
            String seg = segments.get(i);
            if (containsAnyVariable(seg, ARTIST_VARS)) {
                lastArtistIdx = i; // 更新为当前索引，保证是最后一次出现
            }
            if (containsAnyVariable(seg, ALBUM_VARS)) {
                lastAlbumIdx = i; // 更新为当前索引
            }
        }

        // 6. 根据索引范围截取
        // 歌手目录：从 0 到 最后一个歌手变量 (包含)
        List<String> artistSegs = subListSafe(segments, 0, lastArtistIdx);

        // 专辑目录：从 0 到 最后一个专辑变量 (包含)
        List<String> albumSegs = subListSafe(segments, 0, lastAlbumIdx);

        // 7. 渲染
        result.put(KEY_ARTIST_DIR, renderSegments(artistSegs, pathTemplate, sep));
        result.put(KEY_ALBUM_DIR, renderSegments(albumSegs, pathTemplate, sep));
        result.put(KEY_SONG_DIR, renderSegments(segments, pathTemplate, sep));

        return result;
    }

    // ================== 私有辅助方法 ==================

    /**
     * 安全截取 List：如果 endIdx < 0，返回空 list
     */
    private static List<String> subListSafe(List<String> list, int start, int endIdx) {
        if (endIdx < 0) return Collections.emptyList();
        // subList 是 [start, end)，所以要 +1
        int end = Math.min(endIdx + 1, list.size());
        return new ArrayList<>(list.subList(start, end));
    }

    private static String normalizeSeparator(String path, String sep) {
        return path.replace("/", sep).replace("\\", sep);
    }

    private static String selectWorkingTemplate(String main, String fallback, Map<String, Object> ctx) {
        if (tryRender(main, ctx)) return main;
        if (fallback != null && tryRender(fallback, ctx)) return fallback;
        return null;
    }

    private static boolean tryRender(String tpl, Map<String, Object> ctx) {
        try {
            SpelTemplateUtils.formatTemplateWithDollar(tpl, ctx);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String extractDirectoryPart(String template, String sep) {
        int lastIdx = template.lastIndexOf(sep);
        return (lastIdx != -1) ? template.substring(0, lastIdx) : "";
    }

    private static List<String> splitAndClean(String dirTemplate, String sep) {
        return Arrays.stream(dirTemplate.split(Pattern.quote(sep)))
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }

    private static boolean containsAnyVariable(String segment, Set<String> keys) {
        for (String key : keys) {
            if (segment.contains("${" + key + "}")) return true;
        }
        return false;
    }

    private static String renderSegments(List<String> segments, Map<String, Object> ctx, String sep) {
        if (segments == null || segments.isEmpty()) return null;
        String tpl = String.join(sep, segments);
        try {
            String res = SpelTemplateUtils.formatTemplateWithDollar(tpl, ctx);
            return (res == null || res.trim().isEmpty()) ? null : res;
        } catch (Exception e) {
            return null;
        }
    }
}


