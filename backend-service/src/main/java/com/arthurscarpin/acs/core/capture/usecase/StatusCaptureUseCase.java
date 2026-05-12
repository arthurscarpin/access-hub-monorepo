package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.capture.domain.CaptureOCRStatus;

public interface StatusCaptureUseCase {

    void execute(CaptureOCRStatus captureOCRStatus);
}
