package io.eventuate.tram.cdc.status.service;

import io.eventuate.local.common.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
  @Autowired
  private StatusService statusService;

  @RequestMapping(value = "/status", method = RequestMethod.GET)
  public StatusDTO getStatus() {
    return new StatusDTO(statusService.getLastEvents(), statusService.getProcessedEvents(), statusService.getStatus());
  }
}
