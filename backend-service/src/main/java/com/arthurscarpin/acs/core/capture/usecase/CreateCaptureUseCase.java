package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;

public interface CreateCaptureUseCase {

    Capture execute(String filename, Direction direction);
}
