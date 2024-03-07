package com.sqmusicplus.plug.aria2.aria2.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2ResponseMulti;
import lombok.Getter;
import lombok.Setter;

/**
 * <a href="https://aria2.github.io/manual/en/html/aria2c.html#input-file">下载参数</a>
 * @author bx002
 */
@Getter
@Setter
public class Aria2Option implements Cloneable {
    @JsonProperty("all-proxy")
    String allProxy;
    @JsonProperty("all-proxy-passwd")
    String allProxyPasswd;
    @JsonProperty("all-proxy-user")
    String allProxyUser;
    @JsonProperty("allow-overwrite")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean allowOverwrite;
    @JsonProperty("allow-piece-length-change")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean allowPieceLengthChange;
    @JsonProperty("always-resume")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean alwaysResume;
    @JsonProperty("async-dns")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean asyncDns;
    @JsonProperty("auto-file-renaming")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean autoFileRenaming;
    @JsonProperty("bt-enable-hook-after-hash-check")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btEnableHookAfterHashCheck;
    @JsonProperty("bt-enable-lpd")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btEnableLpd;
    @JsonProperty("bt-exclude-tracker")
    String btExcludeTracker;
    @JsonProperty("bt-external-ip")
    String btExternalIp;
    @JsonProperty("bt-force-encryption")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btForceEncryption;
    @JsonProperty("bt-hash-check-seed")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btHashCheckSeed;
    @JsonProperty("bt-load-saved-metadata")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btLoadSavedMetadata;
    @JsonProperty("bt-max-peers")
    Integer btMaxPeers;
    @JsonProperty("bt-metadata-only")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btMetadataOnly;
    @JsonProperty("bt-min-crypto-level")
    String btMinCryptoLevel;
    @JsonProperty("bt-prioritize-piece")
    String btPrioritizePiece;
    @JsonProperty("bt-remove-unselected-file")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btRemoveUnselectedFile;
    @JsonProperty("bt-request-peer-speed-limit")
    Integer btRequestPeerSpeedLimit;
    @JsonProperty("bt-require-crypto")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btRequireCrypto;
    @JsonProperty("bt-save-metadata")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btSaveMetadata;
    @JsonProperty("bt-seed-unverified")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean btSeedUnverified;
    @JsonProperty("bt-stop-timeout")
    Integer btStopTimeout;
    @JsonProperty("bt-tracker")
    String btTracker;
    @JsonProperty("bt-tracker-connect-timeout")
    Integer btTrackerConnectTimeout;
    @JsonProperty("bt-tracker-interval")
    Integer btTrackerInterval;
    @JsonProperty("bt-tracker-timeout")
    Integer btTrackerTimeout;
    @JsonProperty("check-integrity")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean checkIntegrity;
    @JsonProperty("checksum")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean checksum;
    @JsonProperty("conditional-get")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean conditionalGet;
    @JsonProperty("connect-timeout")
    Integer connectTimeout;
    @JsonProperty("content-disposition-default-utf8")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean contentDispositionDefaultUtf8;
    @JsonProperty("continue")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean continues;
    @JsonProperty("dir")
    String dir;
    @JsonProperty("dry-run")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean dryRun;
    @JsonProperty("enable-http-keep-alive")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean enableHttpKeepAlive;
    @JsonProperty("enable-http-pipelining")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean enableHttpPipelining;
    @JsonProperty("enable-mmap")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean enableMmap;
    @JsonProperty("enable-peer-exchange")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean enablePeerExchange;
    @JsonProperty("file-allocation")
    String fileAllocation;
    @JsonProperty("follow-metalink")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean followMetalink;
    @JsonProperty("follow-torrent")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean followTorrent;
    @JsonProperty("force-save")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean forceSave;
    @JsonProperty("ftp-passwd")
    String ftpPasswd;
    @JsonProperty("ftp-pasv")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean ftpPasv;
    @JsonProperty("ftp-proxy")
    String ftpProxy;
    @JsonProperty("ftp-proxy-passwd")
    String ftpProxyPasswd;
    @JsonProperty("ftp-proxy-user")
    String ftpProxyUser;
    @JsonProperty("ftp-reuse-connection")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean ftpReuseConnection;
    @JsonProperty("ftp-type")
    String ftpType;
    @JsonProperty("ftp-user")
    String ftpUser;
    @JsonProperty("hash-check-only")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean hashCheckOnly;
    @JsonProperty("header")
    String header;
    @JsonProperty("http-accept-gzip")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean httpAcceptGzip;
    @JsonProperty("http-auth-challenge")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean httpAuthChallenge;
    @JsonProperty("http-no-cache")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean httpNoCache;
    @JsonProperty("http-passwd")
    String httpPasswd;
    @JsonProperty("http-proxy")
    String httpProxy;
    @JsonProperty("http-proxy-passwd")
    String httpProxyPasswd;
    @JsonProperty("http-proxy-user")
    String httpProxyUser;
    @JsonProperty("http-user")
    String httpUser;
    @JsonProperty("https-proxy")
    String httpsProxy;
    @JsonProperty("https-proxy-passwd")
    String httpsProxyPasswd;
    @JsonProperty("https-proxy-user")
    String httpsProxyUser;
    @JsonProperty("index-out")
    String indexOut;
    @JsonProperty("lowest-speed-limit")
    Integer lowestSpeedLimit;
    @JsonProperty("max-connection-per-server")
    Integer maxConnectionPerServer;
    @JsonProperty("max-download-limit")
    Integer maxDownloadLimit;
    @JsonProperty("max-file-not-found")
    Integer maxFileNotFound;
    @JsonProperty("max-mmap-limit")
    Long maxMmapLimit;
    @JsonProperty("max-resume-failure-tries")
    Integer maxResumeFailureTries;
    @JsonProperty("max-tries")
    Integer maxTries;
    @JsonProperty("max-upload-limit")
    Integer maxUploadLimit;
    @JsonProperty("metalink-base-uri")
    String metalinkBaseUri;
    @JsonProperty("metalink-enable-unique-protocol")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean metalinkEnableUniqueProtocol;
    @JsonProperty("metalink-language")
    String metalinkLanguage;
    @JsonProperty("metalink-location")
    String metalinkLocation;
    @JsonProperty("metalink-os")
    String metalinkOs;
    @JsonProperty("metalink-preferred-protocol")
    String metalinkPreferredProtocol;
    @JsonProperty("metalink-version")
    String metalinkVersion;
    @JsonProperty("min-split-size")
    Integer minSplitSize;
    @JsonProperty("no-file-allocation-limit")
    Integer noFileAllocationLimit;
    @JsonProperty("no-netrc")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean noNetrc;
    @JsonProperty("no-proxy")
    String noProxy;
    /**
     * 文件名
     */
    @JsonProperty("out")
    String out;
    @JsonProperty("parameterized-uri")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean parameterizedUri;
    @JsonProperty("pause")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean pause;
    @JsonProperty("pause-metadata")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean pauseMetadata;
    @JsonProperty("piece-length")
    Integer pieceLength;
    @JsonProperty("proxy-method")
    String proxyMethod;
    @JsonProperty("realtime-chunk-checksum")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean realtimeChunkChecksum;
    @JsonProperty("referer")
    String referer;
    @JsonProperty("remote-time")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean remoteTime;
    @JsonProperty("remove-control-file")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean removeControlFile;
    @JsonProperty("retry-wait")
    Integer retryWait;
    @JsonProperty("reuse-uri")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean reuseUri;
    @JsonProperty("rpc-save-upload-metadata")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean rpcSaveUploadMetadata;
    @JsonProperty("save-not-found")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean saveNotFound;
    @JsonProperty("seed-ratio")
    Double seedRatio;
    @JsonProperty("seed-time")
    Integer seedTime;
    @JsonProperty("select-file")
    String selectFile;
    @JsonProperty("split")
    Integer split;
    @JsonProperty("ssh-host-key-md")
    String sshHostKeyMd;
    @JsonProperty("stream-piece-selector")
    String streamPieceSelector;
    @JsonProperty("timeout")
    Integer timeout;
    @JsonProperty("uri-selector")
    String uriSelector;
    @JsonProperty("use-head")
    @JsonSerialize(using = ToStringSerializer.class)
    Boolean useHead;
    @JsonProperty("user-agent")
    String userAgent;

    @Override
    public Aria2Option clone()  {
        try {
            return (Aria2Option) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Response extends Aria2Response<Aria2Option> {
    }
    public static class ResMulti extends Aria2ResponseMulti<Aria2Option> {
    }
}
