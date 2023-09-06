package com.sqmusicplus.plug.aria2.aria2.response.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sqmusicplus.plug.aria2.aria2.enums.TaskStatus;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2ResponseList;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2ResponseMulti;
import com.sqmusicplus.plug.aria2.aria2.response.field.Aria2TaskFile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务状态
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 12:36
 */
@Getter
@Setter
public class Aria2TaskStatus {
    String gid;
    TaskStatus status;
    Long totalLength;
    Long completedLength;
    Long uploadLength;
    Long pieceLength;
    Integer numPieces;
    Integer connections;
    String bitfield;
    String dir;
    List<Aria2TaskFile> files;
    Long downloadSpeed;
    Long uploadSpeed;
    Integer errorCode;
    String errorMessage;


    //以下为未确定数据类型的字段

    String infoHash;
    String numSeeders;
    String seeder;
    String followedBy;
    String following;
    String belongsTo;
    Object bittorrent;
    String verifiedLength;
    String verifyIntegrityPending;

    @JsonIgnore
    public List<String> getFilePath() {
        if (files == null || files.size() == 0) {
            return new ArrayList<>();
        }
        return files.stream().map(Aria2TaskFile::getPath).distinct().collect(Collectors.toList());
    }

    @JsonIgnore
    public List<String> getUrls() {
        if (files == null || files.size() == 0) {
            return new ArrayList<>();
        }
        return files.stream().flatMap(i -> i.getUris().stream()).map(Aria2TaskFile.Uri::getUri).distinct().collect(Collectors.toList());
    }

    public static class Response extends Aria2Response<Aria2TaskStatus> {
    }

    public static class ResponseList extends Aria2ResponseList<Aria2TaskStatus> {

    }

    public static class ResMulti extends Aria2ResponseMulti<Aria2TaskStatus> {
    }

    public static class ResMultiList extends Aria2ResponseMulti<List<Aria2TaskStatus>> {
    }
}
