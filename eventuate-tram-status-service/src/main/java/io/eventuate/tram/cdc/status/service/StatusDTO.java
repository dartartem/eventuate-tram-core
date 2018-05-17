package io.eventuate.tram.cdc.status.service;

import io.eventuate.local.common.status.CDCStatus;

import java.util.List;

public class StatusDTO {
  private List<String> lastProcessedEvents;
  private long processedEvents;
  private CDCStatus cdcStatus;

  public StatusDTO() {
  }

  public StatusDTO(List<String> lastProcessedEvents, long processedEvents, CDCStatus cdcStatus) {
    this.processedEvents = processedEvents;
    this.lastProcessedEvents = lastProcessedEvents;
    this.cdcStatus = cdcStatus;
  }

  public List<String> getLastProcessedEvents() {
    return lastProcessedEvents;
  }

  public void setLastProcessedEvents(List<String> lastProcessedEvents) {
    this.lastProcessedEvents = lastProcessedEvents;
  }

  public long getProcessedEvents() {
    return processedEvents;
  }

  public void setProcessedEvents(long processedEvents) {
    this.processedEvents = processedEvents;
  }

  public CDCStatus getCdcStatus() {
    return cdcStatus;
  }

  public void setCdcStatus(CDCStatus cdcStatus) {
    this.cdcStatus = cdcStatus;
  }
}
