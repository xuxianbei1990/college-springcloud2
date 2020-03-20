package college.springcloud.order.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author:admin
 */
@Table(name = "undo_log") 
public class UndoLog implements Serializable{

    private static final long serialVersionUID = 1L;
	
	@Id
    private Long id;


	@Column(name = "branch_id")
    private Long branchId;

	@Column(name = "xid")
    private String xid;

	@Column(name = "context")
    private String context;

	@Column(name = "rollback_info")
    private byte[] rollbackInfo;

	@Column(name = "log_status")
    private Integer logStatus;

	@Column(name = "log_created")
    private Date logCreated;

	@Column(name = "log_modified")
    private Date logModified;

	@Column(name = "ext")
    private String ext;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getBranchId() {
        return this.branchId;
    }
    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getXid() {
        return this.xid;
    }
    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return this.context;
    }
    public void setRollbackInfo(byte[] rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
    }

    public byte[] getRollbackInfo() {
        return this.rollbackInfo;
    }
    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    public Integer getLogStatus() {
        return this.logStatus;
    }
    public void setLogCreated(Date logCreated) {
        this.logCreated = logCreated;
    }

    public Date getLogCreated() {
        return this.logCreated;
    }
    public void setLogModified(Date logModified) {
        this.logModified = logModified;
    }

    public Date getLogModified() {
        return this.logModified;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return this.ext;
    }
	
}