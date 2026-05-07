package com.arthurscarpin.acs.core.capture.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;

public interface CaptureGateway {

    Capture save(Capture capture);
}
