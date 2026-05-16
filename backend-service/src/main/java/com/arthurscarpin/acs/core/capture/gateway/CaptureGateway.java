package com.arthurscarpin.acs.core.capture.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;

public interface CaptureGateway {

    Capture findById(String captureId);

    Capture saveAndPublish(Capture capture);

    Capture findByCaptureIdAndImageId(String captureId, String imageId);

    Capture update(Capture capture);

    void updateAndPublish(Capture capture);
}
