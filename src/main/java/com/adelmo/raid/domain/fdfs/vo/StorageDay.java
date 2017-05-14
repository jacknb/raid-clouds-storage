package com.adelmo.raid.domain.fdfs.vo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
    name = "tbstorageday"
)
public class StorageDay {
    private String id;
    private String curStatus;
    private String ipAddr;
    private String srcIpAddr;
    private String domainName;
    private String version;
    private long totalMB;
    private long freeMB;
    private int uploadPriority;
    private Date joinTime;
    private Date upTime;
    private int storePathCount;
    private int subdirCountPerPath;
    private int storagePort;
    private int storageHttpPort;
    private int currentWritePath;
    private long totalUploadCount;
    private long successUploadCount;
    private long totalAppendCount;
    private long successAppendCount;
    private long totalModifyCount;
    private long successModifyCount;
    private long totalTruncateCount;
    private long successTruncateCount;
    private long totalSetMetaCount;
    private long successSetMetaCount;
    private long totalDeleteCount;
    private long successDeleteCount;
    private long totalDownloadCount;
    private long successDownloadCount;
    private long totalGetMetaCount;
    private long successGetMetaCount;
    private long totalCreateLinkCount;
    private long successCreateLinkCount;
    private long totalDeleteLinkCount;
    private long successDeleteLinkCount;
    private long totalUploadBytes;
    private long successUploadBytes;
    private long totalAppendBytes;
    private long successAppendBytes;
    private long totalModifyBytes;
    private long successModifyBytes;
    private long totalDownloadloadBytes;
    private long successDownloadloadBytes;
    private long totalSyncInBytes;
    private long successSyncInBytes;
    private long totalSyncOutBytes;
    private long successSyncOutBytes;
    private long totalFileOpenCount;
    private long successFileOpenCount;
    private long totalFileReadCount;
    private long successFileReadCount;
    private long totalFileWriteCount;
    private long successFileWriteCount;
    private Date lastSourceUpdate;
    private Date lastSyncUpdate;
    private Date lastSyncedTimestamp;
    private Date lastHeartBeatTime;
    private boolean ifTrunkServer;
    private String cpu;
    private float mem;
    private GroupDay group;
    private String groupName;
    private Date created;

    public StorageDay() {
    }

    @Id
    @GeneratedValue(
        generator = "system_uuid"
    )
    @GenericGenerator(
        name = "system_uuid",
        strategy = "uuid"
    )
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpu() {
        return this.cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public float getMem() {
        return this.mem;
    }

    public void setMem(float mem) {
        this.mem = mem;
    }

    public String getCurStatus() {
        return this.curStatus;
    }

    public void setCurStatus(String curStatus) {
        this.curStatus = curStatus;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getSrcIpAddr() {
        return this.srcIpAddr;
    }

    public void setSrcIpAddr(String srcIpAddr) {
        this.srcIpAddr = srcIpAddr;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getTotalMB() {
        return this.totalMB;
    }

    public void setTotalMB(long totalMB) {
        this.totalMB = totalMB;
    }

    public long getFreeMB() {
        return this.freeMB;
    }

    public void setFreeMB(long freeMB) {
        this.freeMB = freeMB;
    }

    public int getUploadPriority() {
        return this.uploadPriority;
    }

    public void setUploadPriority(int uploadPriority) {
        this.uploadPriority = uploadPriority;
    }

    public Date getJoinTime() {
        return this.joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getUpTime() {
        return this.upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public int getStorePathCount() {
        return this.storePathCount;
    }

    public void setStorePathCount(int storePathCount) {
        this.storePathCount = storePathCount;
    }

    public int getSubdirCountPerPath() {
        return this.subdirCountPerPath;
    }

    public void setSubdirCountPerPath(int subdirCountPerPath) {
        this.subdirCountPerPath = subdirCountPerPath;
    }

    public int getStoragePort() {
        return this.storagePort;
    }

    public void setStoragePort(int storagePort) {
        this.storagePort = storagePort;
    }

    public int getStorageHttpPort() {
        return this.storageHttpPort;
    }

    public void setStorageHttpPort(int storageHttpPort) {
        this.storageHttpPort = storageHttpPort;
    }

    public int getCurrentWritePath() {
        return this.currentWritePath;
    }

    public void setCurrentWritePath(int currentWritePath) {
        this.currentWritePath = currentWritePath;
    }

    public long getTotalUploadCount() {
        return this.totalUploadCount;
    }

    public void setTotalUploadCount(long totalUploadCount) {
        this.totalUploadCount = totalUploadCount;
    }

    public long getSuccessUploadCount() {
        return this.successUploadCount;
    }

    public void setSuccessUploadCount(long successUploadCount) {
        this.successUploadCount = successUploadCount;
    }

    public long getTotalAppendCount() {
        return this.totalAppendCount;
    }

    public void setTotalAppendCount(long totalAppendCount) {
        this.totalAppendCount = totalAppendCount;
    }

    public long getSuccessAppendCount() {
        return this.successAppendCount;
    }

    public void setSuccessAppendCount(long successAppendCount) {
        this.successAppendCount = successAppendCount;
    }

    public long getTotalModifyCount() {
        return this.totalModifyCount;
    }

    public void setTotalModifyCount(long totalModifyCount) {
        this.totalModifyCount = totalModifyCount;
    }

    public long getSuccessModifyCount() {
        return this.successModifyCount;
    }

    public void setSuccessModifyCount(long successModifyCount) {
        this.successModifyCount = successModifyCount;
    }

    public long getTotalTruncateCount() {
        return this.totalTruncateCount;
    }

    public void setTotalTruncateCount(long totalTruncateCount) {
        this.totalTruncateCount = totalTruncateCount;
    }

    public long getSuccessTruncateCount() {
        return this.successTruncateCount;
    }

    public void setSuccessTruncateCount(long successTruncateCount) {
        this.successTruncateCount = successTruncateCount;
    }

    public long getTotalSetMetaCount() {
        return this.totalSetMetaCount;
    }

    public void setTotalSetMetaCount(long totalSetMetaCount) {
        this.totalSetMetaCount = totalSetMetaCount;
    }

    public long getSuccessSetMetaCount() {
        return this.successSetMetaCount;
    }

    public void setSuccessSetMetaCount(long successSetMetaCount) {
        this.successSetMetaCount = successSetMetaCount;
    }

    public long getTotalDeleteCount() {
        return this.totalDeleteCount;
    }

    public void setTotalDeleteCount(long totalDeleteCount) {
        this.totalDeleteCount = totalDeleteCount;
    }

    public long getSuccessDeleteCount() {
        return this.successDeleteCount;
    }

    public void setSuccessDeleteCount(long successDeleteCount) {
        this.successDeleteCount = successDeleteCount;
    }

    public long getTotalDownloadCount() {
        return this.totalDownloadCount;
    }

    public void setTotalDownloadCount(long totalDownloadCount) {
        this.totalDownloadCount = totalDownloadCount;
    }

    public long getSuccessDownloadCount() {
        return this.successDownloadCount;
    }

    public void setSuccessDownloadCount(long successDownloadCount) {
        this.successDownloadCount = successDownloadCount;
    }

    public long getTotalGetMetaCount() {
        return this.totalGetMetaCount;
    }

    public void setTotalGetMetaCount(long totalGetMetaCount) {
        this.totalGetMetaCount = totalGetMetaCount;
    }

    public long getSuccessGetMetaCount() {
        return this.successGetMetaCount;
    }

    public void setSuccessGetMetaCount(long successGetMetaCount) {
        this.successGetMetaCount = successGetMetaCount;
    }

    public long getTotalCreateLinkCount() {
        return this.totalCreateLinkCount;
    }

    public void setTotalCreateLinkCount(long totalCreateLinkCount) {
        this.totalCreateLinkCount = totalCreateLinkCount;
    }

    public long getSuccessCreateLinkCount() {
        return this.successCreateLinkCount;
    }

    public void setSuccessCreateLinkCount(long successCreateLinkCount) {
        this.successCreateLinkCount = successCreateLinkCount;
    }

    public long getTotalDeleteLinkCount() {
        return this.totalDeleteLinkCount;
    }

    public void setTotalDeleteLinkCount(long totalDeleteLinkCount) {
        this.totalDeleteLinkCount = totalDeleteLinkCount;
    }

    public long getSuccessDeleteLinkCount() {
        return this.successDeleteLinkCount;
    }

    public void setSuccessDeleteLinkCount(long successDeleteLinkCount) {
        this.successDeleteLinkCount = successDeleteLinkCount;
    }

    public long getTotalUploadBytes() {
        return this.totalUploadBytes;
    }

    public void setTotalUploadBytes(long totalUploadBytes) {
        this.totalUploadBytes = totalUploadBytes;
    }

    public long getSuccessUploadBytes() {
        return this.successUploadBytes;
    }

    public void setSuccessUploadBytes(long successUploadBytes) {
        this.successUploadBytes = successUploadBytes;
    }

    public long getTotalAppendBytes() {
        return this.totalAppendBytes;
    }

    public void setTotalAppendBytes(long totalAppendBytes) {
        this.totalAppendBytes = totalAppendBytes;
    }

    public long getSuccessAppendBytes() {
        return this.successAppendBytes;
    }

    public void setSuccessAppendBytes(long successAppendBytes) {
        this.successAppendBytes = successAppendBytes;
    }

    public long getTotalModifyBytes() {
        return this.totalModifyBytes;
    }

    public void setTotalModifyBytes(long totalModifyBytes) {
        this.totalModifyBytes = totalModifyBytes;
    }

    public long getSuccessModifyBytes() {
        return this.successModifyBytes;
    }

    public void setSuccessModifyBytes(long successModifyBytes) {
        this.successModifyBytes = successModifyBytes;
    }

    public long getTotalDownloadloadBytes() {
        return this.totalDownloadloadBytes;
    }

    public void setTotalDownloadloadBytes(long totalDownloadloadBytes) {
        this.totalDownloadloadBytes = totalDownloadloadBytes;
    }

    public long getSuccessDownloadloadBytes() {
        return this.successDownloadloadBytes;
    }

    public void setSuccessDownloadloadBytes(long successDownloadloadBytes) {
        this.successDownloadloadBytes = successDownloadloadBytes;
    }

    public long getTotalSyncInBytes() {
        return this.totalSyncInBytes;
    }

    public void setTotalSyncInBytes(long totalSyncInBytes) {
        this.totalSyncInBytes = totalSyncInBytes;
    }

    public long getSuccessSyncInBytes() {
        return this.successSyncInBytes;
    }

    public void setSuccessSyncInBytes(long successSyncInBytes) {
        this.successSyncInBytes = successSyncInBytes;
    }

    public long getTotalSyncOutBytes() {
        return this.totalSyncOutBytes;
    }

    public void setTotalSyncOutBytes(long totalSyncOutBytes) {
        this.totalSyncOutBytes = totalSyncOutBytes;
    }

    public long getSuccessSyncOutBytes() {
        return this.successSyncOutBytes;
    }

    public void setSuccessSyncOutBytes(long successSyncOutBytes) {
        this.successSyncOutBytes = successSyncOutBytes;
    }

    public long getTotalFileOpenCount() {
        return this.totalFileOpenCount;
    }

    public void setTotalFileOpenCount(long totalFileOpenCount) {
        this.totalFileOpenCount = totalFileOpenCount;
    }

    public long getSuccessFileOpenCount() {
        return this.successFileOpenCount;
    }

    public void setSuccessFileOpenCount(long successFileOpenCount) {
        this.successFileOpenCount = successFileOpenCount;
    }

    public long getTotalFileReadCount() {
        return this.totalFileReadCount;
    }

    public void setTotalFileReadCount(long totalFileReadCount) {
        this.totalFileReadCount = totalFileReadCount;
    }

    public long getSuccessFileReadCount() {
        return this.successFileReadCount;
    }

    public void setSuccessFileReadCount(long successFileReadCount) {
        this.successFileReadCount = successFileReadCount;
    }

    public long getTotalFileWriteCount() {
        return this.totalFileWriteCount;
    }

    public void setTotalFileWriteCount(long totalFileWriteCount) {
        this.totalFileWriteCount = totalFileWriteCount;
    }

    public long getSuccessFileWriteCount() {
        return this.successFileWriteCount;
    }

    public void setSuccessFileWriteCount(long successFileWriteCount) {
        this.successFileWriteCount = successFileWriteCount;
    }

    public Date getLastSourceUpdate() {
        return this.lastSourceUpdate;
    }

    public void setLastSourceUpdate(Date lastSourceUpdate) {
        this.lastSourceUpdate = lastSourceUpdate;
    }

    public Date getLastSyncUpdate() {
        return this.lastSyncUpdate;
    }

    public void setLastSyncUpdate(Date lastSyncUpdate) {
        this.lastSyncUpdate = lastSyncUpdate;
    }

    public Date getLastSyncedTimestamp() {
        return this.lastSyncedTimestamp;
    }

    public void setLastSyncedTimestamp(Date lastSyncedTimestamp) {
        this.lastSyncedTimestamp = lastSyncedTimestamp;
    }

    public Date getLastHeartBeatTime() {
        return this.lastHeartBeatTime;
    }

    public void setLastHeartBeatTime(Date lastHeartBeatTime) {
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public boolean isIfTrunkServer() {
        return this.ifTrunkServer;
    }

    public void setIfTrunkServer(boolean ifTrunkServer) {
        this.ifTrunkServer = ifTrunkServer;
    }

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    public GroupDay getGroup() {
        return this.group;
    }

    public void setGroup(GroupDay group) {
        this.group = group;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
