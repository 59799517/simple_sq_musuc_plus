package com.sqmusicplus.plug.aria2.aria2.response.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/31 16:03
 */
@Getter
@Setter
public class Aria2GlobalOption {
    @JsonProperty("allow-overwrite")
    Boolean allowOverwrite;
    @JsonProperty("allow-piece-length-change")
    Boolean allowPieceLengthChange;
    @JsonProperty("always-resume")
    Boolean alwaysResume;
    @JsonProperty("async-dns")
    Boolean asyncDns;
    @JsonProperty("auto-file-renaming")
    Boolean autoFileRenaming;
    @JsonProperty("auto-save-interval")
    Integer autoSaveInterval;
    @JsonProperty("bt-detach-seed-only")
    Boolean btDetachSeedOnly;
    @JsonProperty("bt-enable-hook-after-hash-check")
    Boolean btEnableHookAfterHashCheck;
    @JsonProperty("bt-enable-lpd")
    Boolean btEnableLpd;
    @JsonProperty("bt-force-encryption")
    Boolean btForceEncryption;
    @JsonProperty("bt-hash-check-seed")
    Boolean btHashCheckSeed;
    @JsonProperty("bt-load-saved-metadata")
    Boolean btLoadSavedMetadata;
    @JsonProperty("bt-max-open-files")
    Integer btMaxOpenFiles;
    @JsonProperty("bt-max-peers")
    Integer btMaxPeers;
    @JsonProperty("bt-metadata-only")
    Boolean btMetadataOnly;
    @JsonProperty("bt-min-crypto-level")
    String btMinCryptoLevel;
    @JsonProperty("bt-remove-unselected-file")
    Boolean btRemoveUnselectedFile;
    @JsonProperty("bt-request-peer-speed-limit")
    Integer btRequestPeerSpeedLimit;
    @JsonProperty("bt-require-crypto")
    Boolean btRequireCrypto;
    @JsonProperty("bt-save-metadata")
    Boolean btSaveMetadata;
    @JsonProperty("bt-seed-unverified")
    Boolean btSeedUnverified;
    @JsonProperty("bt-stop-timeout")
    Integer btStopTimeout;
    @JsonProperty("bt-tracker-connect-timeout")
    Integer btTrackerConnectTimeout;
    @JsonProperty("bt-tracker-interval")
    Integer btTrackerInterval;
    @JsonProperty("bt-tracker-timeout")
    Integer btTrackerTimeout;
    @JsonProperty("check-certificate")
    Boolean checkCertificate;
    @JsonProperty("check-integrity")
    Boolean checkIntegrity;
    @JsonProperty("conditional-get")
    Boolean conditionalGet;
    @JsonProperty("conf-path")
    String confPath;
    @JsonProperty("connect-timeout")
    Integer connectTimeout;
    @JsonProperty("console-log-level")
    String consoleLogLevel;
    @JsonProperty("content-disposition-default-utf8")
    Boolean contentDispositionDefaultUtf8;
    @JsonProperty("continue")
    Boolean continues;
    @JsonProperty("daemon")
    Boolean daemon;
    @JsonProperty("deferred-input")
    Boolean deferredInput;
    @JsonProperty("dht-file-path")
    String dhtFilePath;
    @JsonProperty("dht-file-path6")
    String dhtFilePath6;
    @JsonProperty("dht-listen-port")
    String dhtListenPort;
    @JsonProperty("dht-message-timeout")
    Integer dhtMessageTimeout;
    @JsonProperty("dir")
    String dir;
    @JsonProperty("disable-ipv6")
    Boolean disableIpv6;
    @JsonProperty("disk-cache")
    Integer diskCache;
    @JsonProperty("download-result")
    String downloadResult;
    @JsonProperty("dry-run")
    Boolean dryRun;
    @JsonProperty("dscp")
    Integer dscp;
    @JsonProperty("enable-color")
    Boolean enableColor;
    @JsonProperty("enable-dht")
    Boolean enableDht;
    @JsonProperty("enable-dht6")
    Boolean enableDht6;
    @JsonProperty("enable-http-keep-alive")
    Boolean enableHttpKeepAlive;
    @JsonProperty("enable-http-pipelining")
    Boolean enableHttpPipelining;
    @JsonProperty("enable-mmap")
    Boolean enableMmap;
    @JsonProperty("enable-peer-exchange")
    Boolean enablePeerExchange;
    @JsonProperty("enable-rpc")
    Boolean enableRpc;
    @JsonProperty("event-poll")
    String eventPoll;
    @JsonProperty("file-allocation")
    String fileAllocation;
    @JsonProperty("follow-metalink")
    Boolean followMetalink;
    @JsonProperty("follow-torrent")
    Boolean followTorrent;

    @JsonProperty("force-save")
    Boolean forceSave;
    @JsonProperty("ftp-pasv")
    Boolean ftpPasv;
    @JsonProperty("ftp-reuse-connection")
    Boolean ftpReuseConnection;
    @JsonProperty("ftp-type")
    String ftpType;
    @JsonProperty("hash-check-only")
    Boolean hashCheckOnly;
    @JsonProperty("help")
    String help;
    @JsonProperty("http-accept-gzip")
    Boolean httpAcceptGzip;
    @JsonProperty("http-auth-challenge")
    Boolean httpAuthChallenge;
    @JsonProperty("http-no-cache")
    Boolean httpNoCache;
    @JsonProperty("human-readable")
    Boolean humanReadable;
    @JsonProperty("keep-unfinished-download-result")
    Boolean keepUnfinishedDownloadResult;
    @JsonProperty("listen-port")
    Integer listenPort;
    @JsonProperty("log")
    String log;
    @JsonProperty("log-level")
    String logLevel;
    @JsonProperty("lowest-speed-limit")
    Integer lowestSpeedLimit;
    @JsonProperty("max-concurrent-downloads")
    Integer maxConcurrentDownloads;
    @JsonProperty("max-connection-per-server")
    Integer maxConnectionPerServer;
    @JsonProperty("max-download-limit")
    Integer maxDownloadLimit;
    @JsonProperty("max-download-result")
    Integer maxDownloadResult;
    @JsonProperty("max-file-not-found")
    Integer maxFileNotFound;
    @JsonProperty("max-mmap-limit")
    Long maxMmapLimit;
    @JsonProperty("max-overall-download-limit")
    Integer maxOverallDownloadLimit;
    @JsonProperty("max-overall-upload-limit")
    Integer maxOverallUploadLimit;
    @JsonProperty("max-resume-failure-tries")
    Integer maxResumeFailureTries;
    @JsonProperty("max-tries")
    Integer maxTries;
    @JsonProperty("max-upload-limit")
    Integer maxUploadLimit;
    @JsonProperty("metalink-enable-unique-protocol")
    Boolean metalinkEnableUniqueProtocol;
    @JsonProperty("metalink-preferred-protocol")
    String metalinkPreferredProtocol;
    @JsonProperty("min-split-size")
    Integer minSplitSize;
    @JsonProperty("min-tls-version")
    String minTlsVersion;
    @JsonProperty("netrc-path")
    String netrcPath;
    @JsonProperty("no-conf")
    Boolean noConf;
    @JsonProperty("no-file-allocation-limit")
    Integer noFileAllocationLimit;
    @JsonProperty("no-netrc")
    Boolean noNetrc;
    @JsonProperty("optimize-concurrent-downloads")
    Boolean optimizeConcurrentDownloads;
    @JsonProperty("parameterized-uri")
    Boolean parameterizedUri;
    @JsonProperty("pause-metadata")
    Boolean pauseMetadata;
    @JsonProperty("peer-agent")
    String peerAgent;
    @JsonProperty("peer-id-prefix")
    String peerIdPrefix;
    @JsonProperty("piece-length")
    Integer pieceLength;
    @JsonProperty("proxy-method")
    String proxyMethod;
    @JsonProperty("quiet")
    Boolean quiet;
    @JsonProperty("realtime-chunk-checksum")
    Boolean realtimeChunkChecksum;
    @JsonProperty("referer")
    String referer;
    @JsonProperty("remote-time")
    Boolean remoteTime;
    @JsonProperty("remove-control-file")
    Boolean removeControlFile;
    @JsonProperty("retry-wait")
    Integer retryWait;
    @JsonProperty("reuse-uri")
    Boolean reuseUri;
    @JsonProperty("rpc-allow-origin-all")
    Boolean rpcAllowOriginAll;
    @JsonProperty("rpc-listen-all")
    Boolean rpcListenAll;
    @JsonProperty("rpc-listen-port")
    Integer rpcListenPort;
    @JsonProperty("rpc-max-request-size")
    Integer rpcMaxRequestSize;
    @JsonProperty("rpc-save-upload-metadata")
    Boolean rpcSaveUploadMetadata;
    @JsonProperty("rpc-secure")
    Boolean rpcSecure;
    @JsonProperty("save-not-found")
    Boolean saveNotFound;
    @JsonProperty("save-session")
    String saveSession;
    @JsonProperty("save-session-interval")
    Integer saveSessionInterval;
    @JsonProperty("seed-ratio")
    Double seedRatio;
    @JsonProperty("seed-time")
    Integer seedTime;
    @JsonProperty("server-stat-timeout")
    Integer serverStatTimeout;
    @JsonProperty("show-console-readout")
    Boolean showConsoleReadout;
    @JsonProperty("show-files")
    Boolean showFiles;
    @JsonProperty("socket-recv-buffer-size")
    Integer socketRecvBufferSize;
    @JsonProperty("split")
    Integer split;
    @JsonProperty("stderr")
    Boolean stderr;
    @JsonProperty("stop")
    Integer stop;
    @JsonProperty("stream-piece-selector")
    String streamPieceSelector;
    @JsonProperty("summary-interval")
    Integer summaryInterval;
    @JsonProperty("timeout")
    Integer timeout;
    @JsonProperty("truncate-console-readout")
    Boolean truncateConsoleReadout;
    @JsonProperty("uri-selector")
    String uriSelector;
    @JsonProperty("use-head")
    Boolean useHead;
    @JsonProperty("user-agent")
    String userAgent;


    public static class Response extends Aria2Response<Aria2GlobalOption> {
    }
}
