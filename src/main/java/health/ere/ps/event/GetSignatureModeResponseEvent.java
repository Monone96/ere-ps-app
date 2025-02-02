package health.ere.ps.event;

import de.gematik.ws.conn.connectorcommon.v5.Status;
import de.gematik.ws.conn.signatureservice.v7_5_5.ComfortSignatureStatusEnum;
import de.gematik.ws.conn.signatureservice.v7_5_5.SessionInfo;

public class GetSignatureModeResponseEvent {
    Status status;
    ComfortSignatureStatusEnum comfortSignatureStatus;
    Integer comfortSignatureMax;
    javax.xml.datatype.Duration comfortSignatureTimer;
    SessionInfo sessionInfo;

    public GetSignatureModeResponseEvent(Status status, ComfortSignatureStatusEnum comfortSignatureStatus, Integer comfortSignatureMax, javax.xml.datatype.Duration comfortSignatureTimer, SessionInfo sessionInfo) {
        this.status = status;
        this.comfortSignatureStatus = comfortSignatureStatus;
        this.comfortSignatureMax = comfortSignatureMax;
        this.comfortSignatureTimer = comfortSignatureTimer;
        this.sessionInfo = sessionInfo;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ComfortSignatureStatusEnum getComfortSignatureStatus() {
        return this.comfortSignatureStatus;
    }

    public void setComfortSignatureStatus(ComfortSignatureStatusEnum comfortSignatureStatus) {
        this.comfortSignatureStatus = comfortSignatureStatus;
    }

    public Integer getComfortSignatureMax() {
        return this.comfortSignatureMax;
    }

    public void setComfortSignatureMax(Integer comfortSignatureMax) {
        this.comfortSignatureMax = comfortSignatureMax;
    }

    public javax.xml.datatype.Duration getComfortSignatureTimer() {
        return this.comfortSignatureTimer;
    }

    public void setComfortSignatureTimer(javax.xml.datatype.Duration comfortSignatureTimer) {
        this.comfortSignatureTimer = comfortSignatureTimer;
    }

    public SessionInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public void setSessionInfo(SessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

}
